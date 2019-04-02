package com.kailaisi.eshopcache.prewarm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kailaisi.eshopcache.spring.SpringContext;
import com.kailaisi.eshopcache.model.ProductInfo;
import com.kailaisi.eshopcache.service.CacheService;
import com.kailaisi.eshopcache.zk.ZooKeeperSession;

/**
 * 缓存预热线程
 * 1、服务启动的时候，进行缓存预热
 * <p>
 * 2、从zk中读取taskid列表
 * <p>
 * 3、依次遍历每个taskid，尝试获取分布式锁，如果获取不到，快速报错，不要等待，因为说明已经有其他服务实例在预热了
 * <p>
 * 4、直接尝试获取下一个taskid的分布式锁
 * <p>
 * 5、即使获取到了分布式锁，也要检查一下这个taskid的预热状态，如果已经被预热过了，就不再预热了
 * <p>
 * 6、执行预热操作，遍历productid列表，查询数据，然后写ehcache和redis
 * <p>
 * 7、预热完成后，设置taskid对应的预热状态
 */
public class CachePreWarmThread extends Thread {
    @Override
    public void run() {
        CacheService cacheService = (CacheService) SpringContext.getApplicationContext().getBean("cacheService");
        ZooKeeperSession zkSession = ZooKeeperSession.getInstance();
        //获取storm中的taskid的列表
        String taskIds = zkSession.getNodeData("/taskid-list");
        System.out.println("【CachePreWarmThread】获取到taskId列表:" + taskIds);
        if (null != taskIds && !"".equals(taskIds)) {
            String[] split = taskIds.split(",");
            for (String taskId : split) {
                //尝试获取分布式锁
                String taskidLockPath = "/taskid-lock" + taskId;
                boolean result = zkSession.acquireFastFailedDistributedLock(taskidLockPath);
                if (!result) {
                    continue;
                }
                //获取到taskid的锁之后，获取对应的预热状态
                String taskIdStatusLockPath = "taskid-status-lock" + taskId;
                zkSession.acquireDistributedLock(taskIdStatusLockPath);
                String taskidStatus = zkSession.getNodeData("/taskid-status-" + taskId);
                if ("".equals(taskidStatus)) {
                    //获取该任务中的topN的产品列表
                    String productidList = zkSession.getNodeData("/task-hot-product-list-") + taskId;
                    JSONArray productIdJsonArray = JSONArray.parseArray(productidList);
                    for (int i = 0; i < productIdJsonArray.size(); i++) {
                        Long productId = productIdJsonArray.getLong(i);
                        String productInfoJson = "{\"id\":" + productId + ",\"name\":\"IPhone7\",\"price\":5999,\"pictureList\":[\"a.png\",\"b.png\",\"c.png\"],\"color\":[\"红色\",\"白色\",\"绿色 \"]}";
                        ProductInfo productInfo = JSONObject.parseObject(productInfoJson, ProductInfo.class);
                        //将菜品数据加载到本地缓存和redis缓存中
                        cacheService.saveProductInfo2LocalCache(productInfo);
                        cacheService.saveProductInfo2RedisCache(productInfo);
                    }
                    //预热成功后，置为对应的预热状态
                    zkSession.setNodeData(taskIdStatusLockPath, "success");
                }
                zkSession.releaseDistributedLock(taskIdStatusLockPath);
                zkSession.releaseDistributedLock(taskidLockPath);
            }
        }
    }
}
