package com.kailaisi.eshopcache.hystrix.command;

import com.alibaba.fastjson.JSONObject;
import com.kailaisi.eshopcache.model.ProductInfo;
import com.kailaisi.eshopcache.spring.SpringContext;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import redis.clients.jedis.JedisCluster;

/**
 * 描述：保存商品信息到redis
 * <p/>作者：wu
 * <br/>创建时间：2019/5/6 10:39
 */
public class SaveProductInfo2RedisCacheCommand extends HystrixCommand<Boolean> {
    private ProductInfo productInfo;

    public SaveProductInfo2RedisCacheCommand(ProductInfo productInfo) {
        super(HystrixCommandGroupKey.Factory.asKey("ProductInfoGroup"));
        this.productInfo = productInfo;
    }

    @Override
    protected Boolean run() throws Exception {
        JedisCluster jedisCluster = (JedisCluster) SpringContext.getApplicationContext().getBean("JedisClusterFactory");
        String key = "product_info_" + productInfo.getId();
        jedisCluster.set(key, JSONObject.toJSONString(productInfo));
        return true;
    }
}
