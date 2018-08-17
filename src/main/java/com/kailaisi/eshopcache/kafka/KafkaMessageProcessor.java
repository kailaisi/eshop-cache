package com.kailaisi.eshopcache.kafka;

import com.alibaba.fastjson.JSONObject;
import com.kailaisi.eshopcache.SpringContext;
import com.kailaisi.eshopcache.model.ProductInfo;
import com.kailaisi.eshopcache.model.ShopInfo;
import com.kailaisi.eshopcache.service.CacheService;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

/**
 * kafka消息处理线程
 */
public class KafkaMessageProcessor implements Runnable {
    private KafkaStream kafkaStream;
    private CacheService cacheService;

    public KafkaMessageProcessor(KafkaStream stream) {
        this.kafkaStream = stream;
        cacheService = (CacheService) SpringContext.getApplicationContext().getBean("cacheService");
    }

    @Override
    public void run() {
        System.out.println("启动数据");
        ConsumerIterator<byte[], byte[]> it = kafkaStream.iterator();
        while (it.hasNext()) {
            String message = new String(it.next().message());
            System.out.println("获取信息：" + message);
            try {
                JSONObject object = JSONObject.parseObject(message);
                //获取对应的服务标识
                String serviceId = object.getString("serviceId");
                if ("productInfoService".equals(serviceId)) {
                    processProductInfoChangeMessage(object);
                } else if ("shopInfoService".equals(serviceId)) {
                    processShopInfoChangeMessage(object);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("启动数据完成");
    }

    /**
     * 处理商品信息变更的服务
     *
     * @param object
     */
    private void processProductInfoChangeMessage(JSONObject object) throws Exception {
        Long id = object.getLong("productId");
        //调用服务的接口，getProductInfo,在服务中查询数据库，然后返回
        String productJson = "{\"id\":1,\"name\":\"IPhone7\",\"price\":5999,\"pictureList\":[\"a.png\",\"b.png\",\"c.png\"],\"color\":[\"红色\",\"白色\",\"绿色 \"]}";
        ProductInfo productInfo = JSONObject.parseObject(productJson, ProductInfo.class);
        cacheService.saveProductInfo2LocalCache(productInfo);
        System.out.println("===================获取到刚保存到本地缓存的信息:" + productInfo.toString() + "==================");
        cacheService.saveProductInfo2RedisCache(productInfo);
    }

    /**
     * 处理商品信息变更的服务
     *
     * @param object
     */
    private void processShopInfoChangeMessage(JSONObject object) throws Exception {
        Long id = object.getLong("productId");
        //调用服务的接口，getProductInfo,在服务中查询数据库，然后返回
        String shopJson = "{\"id\":1,\"name\":\"王雷手机店\",\"level\":5,\"rate\":0.99}";
        ShopInfo shopInfo = JSONObject.parseObject(shopJson, ShopInfo.class);
        cacheService.saveShopInfo2LocalCache(shopInfo);
        cacheService.saveShopInfo2RedisCache(shopInfo);
    }
}
