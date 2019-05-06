package com.kailaisi.eshopcache.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kailaisi.eshopcache.hystrix.command.GetProductInfo2RedisCacheCommand;
import com.kailaisi.eshopcache.hystrix.command.GetShopInfo2RedisCacheCommand;
import com.kailaisi.eshopcache.hystrix.command.SaveProductInfo2RedisCacheCommand;
import com.kailaisi.eshopcache.hystrix.command.SaveShopInfo2RedisCacheCommand;
import com.kailaisi.eshopcache.model.ProductInfo;
import com.kailaisi.eshopcache.model.ShopInfo;
import com.kailaisi.eshopcache.service.CacheService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;

/**
 * 缓存实现类
 */
@Service("cacheService")
public class CacheServiceImpl implements CacheService {
    public static final String CACHE_NAME = "local";
    @Resource
    JedisCluster jedisCluster;

    @CachePut(value = CACHE_NAME, key = "'key_'+#productInfo.getId()")
    @Override
    public ProductInfo saveLocalCache(ProductInfo info) {
        return info;
    }

    @Cacheable(value = CACHE_NAME, key = "'key_'+#id")
    @Override
    public ProductInfo getLocalCache(Long id) {
        return null;
    }

    @CachePut(value = CACHE_NAME, key = "'shop_info_'+#shopInfo.getId()")
    @Override
    public ShopInfo saveShopInfo2LocalCache(ShopInfo shopInfo) {
        return shopInfo;
    }

    @Cacheable(value = CACHE_NAME, key = "'shop_info_'+#shopId")
    @Override
    public ShopInfo getShopInfoFromLocalCache(Long shopId) {
        return null;
    }

    @CachePut(value = CACHE_NAME, key = "'product_info_'+#productInfo.getId()")
    @Override
    public ProductInfo saveProductInfo2LocalCache(ProductInfo productInfo) {
        return productInfo;
    }

    @Cacheable(value = CACHE_NAME, key = "'product_info_'+#productId")
    @Override
    public ProductInfo getProductInfoFromLocalCache(Long productId) {
        return null;
    }

    @Override
    public void saveShopInfo2RedisCache(ShopInfo shopInfo) {
        /*String key = "shop_info_" + shopInfo.getId();
        jedisCluster.set(key, JSONObject.toJSONString(shopInfo));*/
        SaveShopInfo2RedisCacheCommand command = new SaveShopInfo2RedisCacheCommand(shopInfo);
        command.execute();
    }

    @Override
    public void saveProductInfo2RedisCache(ProductInfo productInfo) {
       /* String key = "product_info_" + productInfo.getId();
        jedisCluster.set(key, JSONObject.toJSONString(productInfo));*/
        SaveProductInfo2RedisCacheCommand command = new SaveProductInfo2RedisCacheCommand(productInfo);
        command.execute();
    }

    /**
     * 从redis中获取商品信息
     */
    public ProductInfo getProductInfoFromReidsCache(Long productId) {
       /* String key = "product_info_" + productId;
        String json = jedisCluster.get(key);
        return JSONObject.parseObject(json, ProductInfo.class);*/
        GetProductInfo2RedisCacheCommand command = new GetProductInfo2RedisCacheCommand(productId);
        return command.execute();
    }

    /**
     * 从redis中获取店铺信息
     */
    public ShopInfo getShopInfoFromReidsCache(Long shopId) {
        /*String key = "shop_info_" + shopId;
        String json = jedisCluster.get(key);
        return JSONObject.parseObject(json, ShopInfo.class);*/
        GetShopInfo2RedisCacheCommand command = new GetShopInfo2RedisCacheCommand(shopId);
        return command.execute();
    }

}
