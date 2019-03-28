package com.kailaisi.eshopcache.service;

import com.kailaisi.eshopcache.model.ProductInfo;
import com.kailaisi.eshopcache.model.ShopInfo;

/**
 * 缓存接口
 */
public interface CacheService {
    /**
     * 保存商品的信息到本地缓存
     *
     * @param info
     * @return
     */
    ProductInfo saveLocalCache(ProductInfo info);

    /**
     * 从本地缓存中获取商品信息
     *
     * @param id
     * @return
     */
    ProductInfo getLocalCache(Long id);

    /**
     * 保存店铺信息到redis中
     *
     * @param shopInfo
     */
    void saveShopInfo2RedisCache(ShopInfo shopInfo);

    /**
     * 保存店铺信息到redis中
     *
     * @param productInfo
     */
    void saveProductInfo2RedisCache(ProductInfo productInfo);

    /**
     * 保存店铺信息到echache中
     *
     * @param shopInfo
     */
    ShopInfo saveShopInfo2LocalCache(ShopInfo shopInfo);

    /**
     * echeache中获取店铺信息
     *
     * @param shopId
     */
    ShopInfo getShopInfoFromLocalCache(Long shopId);

    /**
     * 保存商品信息到echache中
     *
     * @param productInfo
     */
    ProductInfo saveProductInfo2LocalCache(ProductInfo productInfo);

    /**
     * 保存商品信息到echache中
     *
     * @param productId
     */
    ProductInfo getProductInfoFromLocalCache(Long productId);

    /**
     * 从redis中获取商品信息
     *
     * @param productId
     * @return
     */
    ProductInfo getProductInfoFromReidsCache(Long productId);

    /**
     * 从redis中获取店铺信息
     *
     * @param shopId
     * @return
     */
    ShopInfo getShopInfoFromReidsCache(Long shopId);
}
