package com.kailaisi.eshopcache.hystrix.command;

import com.alibaba.fastjson.JSONObject;
import com.kailaisi.eshopcache.model.ProductInfo;
import com.kailaisi.eshopcache.spring.SpringContext;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import redis.clients.jedis.JedisCluster;

/**
 * 描述：从redis获取商品信息
 * <p/>作者：wu
 * <br/>创建时间：2019/5/6 10:39
 */
public class GetProductInfo2RedisCacheCommand extends HystrixCommand<ProductInfo> {
    private Long productId;

    public GetProductInfo2RedisCacheCommand(Long productId) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProductInfoGroup"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(100)
                        .withCircuitBreakerRequestVolumeThreshold(1000)
                        .withCircuitBreakerErrorThresholdPercentage(70)
                        .withMetricsRollingPercentileWindowInMilliseconds(60 * 1000)));
        this.productId = productId;
    }

    @Override
    protected ProductInfo run() throws Exception {
        JedisCluster jedisCluster = (JedisCluster) SpringContext.getApplicationContext().getBean("JedisClusterFactory");
        String key = "product_info_" + productId;
        String json = jedisCluster.get(key);
        return JSONObject.parseObject(json, ProductInfo.class);
    }

    @Override
    protected ProductInfo getFallback() {
        return null;
    }
}
