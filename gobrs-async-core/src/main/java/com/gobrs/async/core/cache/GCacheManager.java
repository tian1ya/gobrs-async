package com.gobrs.async.core.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type G cache manager.
 * 将所有的 Cache 的实现类缓存起来，k:Cache类型，v: Cache
 * @program: gobrs -async
 * @ClassName GCacheManager
 * @description:
 * @author: sizegang
 * @create: 2023 -01-04
 */
public class GCacheManager {

    /**
     * The Caches.
     */
    Map<Integer, GCache<?, ?, ?>> CACHES = new ConcurrentHashMap<>();

    /**
     * Instantiates a new G cache manager.
     *
     * @param caches the caches
     */
    public GCacheManager(Map<String, GCache<?, ?, ?>> caches) {
        if (caches == null) {
            return;
        }
        caches.forEach((k, v) -> {
            CACHES.put(v.getType(), v);
        });
    }

    /**
     * Gets g cache.
     *
     * @param type the type
     * @return the g cache
     */
    public GCache getGCache(Integer type) {
        return CACHES.get(type);
    }

}
