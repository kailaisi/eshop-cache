package com.kailaisi.eshopcache.hystrix.command;

import com.alibaba.fastjson.JSONObject;
import com.kailaisi.eshopcache.model.ShopInfo;
import com.kailaisi.eshopcache.spring.SpringContext;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import redis.clients.jedis.JedisCluster;

/**
 * 描述：从redis获取门店信息
 * <p/>作者：wu
 * <br/>创建时间：2019/5/6 10:39
 */
public class GetShopInfo2RedisCacheCommand extends HystrixCommand<ShopInfo> {
    private Long shopId;

    public GetShopInfo2RedisCacheCommand(Long shopId) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProductInfoGroup"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(100)
                        .withCircuitBreakerRequestVolumeThreshold(1000)
                        .withCircuitBreakerErrorThresholdPercentage(70)
                        .withMetricsRollingPercentileWindowInMilliseconds(60 * 1000)));
        this.shopId = shopId;
    }

    @Override
    protected ShopInfo run() throws Exception {
        JedisCluster jedisCluster = (JedisCluster) SpringContext.getApplicationContext().getBean("JedisClusterFactory");
        String key = "shop_info_" + shopId;
        String json = jedisCluster.get(key);
        return JSONObject.parseObject(json, ShopInfo.class);
    }

    @Override
    protected ShopInfo getFallback() {
        return null;
    }
}
