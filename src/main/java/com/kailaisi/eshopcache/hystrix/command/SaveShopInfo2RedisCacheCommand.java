package com.kailaisi.eshopcache.hystrix.command;

import com.alibaba.fastjson.JSONObject;
import com.kailaisi.eshopcache.model.ShopInfo;
import com.kailaisi.eshopcache.spring.SpringContext;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import redis.clients.jedis.JedisCluster;

/**
 * 描述：保存门店信息到redis
 * <p/>作者：wu
 * <br/>创建时间：2019/5/6 10:39
 */
public class SaveShopInfo2RedisCacheCommand extends HystrixCommand<Boolean> {
    private ShopInfo shopInfo;

    public SaveShopInfo2RedisCacheCommand(ShopInfo shopInfo) {
        super(HystrixCommandGroupKey.Factory.asKey("ProductInfoGroup"));
        this.shopInfo = shopInfo;
    }

    @Override
    protected Boolean run() throws Exception {
        JedisCluster jedisCluster = (JedisCluster) SpringContext.getApplicationContext().getBean("JedisClusterFactory");
        String key = "shop_info_" + shopInfo.getId();
        jedisCluster.set(key, JSONObject.toJSONString(shopInfo));
        return true;
    }
}
