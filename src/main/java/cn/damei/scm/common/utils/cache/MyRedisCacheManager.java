package cn.damei.scm.common.utils.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.cache.DefaultRedisCachePrefix;
import org.springframework.data.redis.cache.RedisCachePrefix;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by weiys on 16/5/14.
 */


public class MyRedisCacheManager extends AbstractTransactionSupportingCacheManager {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @SuppressWarnings("rawtypes")//
    private final RedisTemplate template;

    private boolean usePrefix = false;
    private RedisCachePrefix cachePrefix = new DefaultRedisCachePrefix();
    private boolean loadRemoteCachesOnStartup = false;
    private boolean dynamic = true;

    // 0 - never expire
    private long defaultExpiration = 0;
    private Map<String, Long> expires = null;

    /**
     * Construct a {@link MyRedisCacheManager}.
     *
     * @param template
     */
    @SuppressWarnings("rawtypes")
    public MyRedisCacheManager(RedisTemplate template) {
        this(template, Collections.<String>emptyList());
    }

    /**
     * Construct a static {@link MyRedisCacheManager}, managing caches for the specified cache names only.
     *
     * @param template
     * @param cacheNames
     * @since 1.2
     */
    @SuppressWarnings("rawtypes")
    public MyRedisCacheManager(RedisTemplate template, Collection<String> cacheNames) {
        this.template = template;
        setCacheNames(cacheNames);
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = super.getCache(name);
        if (cache == null && this.dynamic) {
            return createAndAddCache(name);
        }

        return cache;
    }

    /**
     * Specify the set of cache names for this CacheManager's 'static' mode. <br/>
     * The number of caches and their names will be fixed after a call to this method, with no creation of further cache
     * regions at runtime.
     */
    public void setCacheNames(Collection<String> cacheNames) {

        if (!CollectionUtils.isEmpty(cacheNames)) {
            for (String cacheName : cacheNames) {
                createAndAddCache(cacheName);
            }
            this.dynamic = false;
        }
    }

    public void setUsePrefix(boolean usePrefix) {
        this.usePrefix = usePrefix;
    }

    /**
     * Sets the cachePrefix. Defaults to 'DefaultRedisCachePrefix').
     *
     * @param cachePrefix the cachePrefix to set
     */
    public void setCachePrefix(RedisCachePrefix cachePrefix) {
        this.cachePrefix = cachePrefix;
    }

    /**
     * Sets the default expire time (in seconds).
     *
     * @param defaultExpireTime time in seconds.
     */
    public void setDefaultExpiration(long defaultExpireTime) {
        this.defaultExpiration = defaultExpireTime;
    }

    /**
     * Sets the expire time (in seconds) for cache regions (by key).
     *
     * @param expires time in seconds
     */
    public void setExpires(Map<String, Long> expires) {
        this.expires = (expires != null ? new ConcurrentHashMap<String, Long>(expires) : null);
    }

    /**
     * If set to {@code true} {@link MyRedisCacheManager} will try to retrieve cache names from redis server using
     * {@literal KEYS} command and initialize {@link RedisCache} for each of them.
     *
     * @param loadRemoteCachesOnStartup
     * @since 1.2
     */
    public void setLoadRemoteCachesOnStartup(boolean loadRemoteCachesOnStartup) {
        this.loadRemoteCachesOnStartup = loadRemoteCachesOnStartup;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.cache.support.AbstractCacheManager#loadCaches()
     */
    @Override
    protected Collection<? extends Cache> loadCaches() {

        Assert.notNull(this.template, "A redis template is required in order to interact with data store");
        return addConfiguredCachesIfNecessary(loadRemoteCachesOnStartup ? loadAndInitRemoteCaches() : Collections
                .<Cache>emptyList());
    }

    /**
     * Returns a new {@link Collection} of {@link Cache} from the given caches collection and adds the configured
     * {@link Cache}s of they are not already present.
     *
     * @param caches must not be {@literal null}
     * @return
     */
    private Collection<? extends Cache> addConfiguredCachesIfNecessary(Collection<? extends Cache> caches) {

        Assert.notNull(caches, "Caches must not be null!");

        Collection<Cache> result = new ArrayList<Cache>(caches);

        for (String cacheName : getCacheNames()) {

            boolean configuredCacheAlreadyPresent = false;

            for (Cache cache : caches) {

                if (cache.getName().equals(cacheName)) {
                    configuredCacheAlreadyPresent = true;
                    break;
                }
            }

            if (!configuredCacheAlreadyPresent) {
                result.add(getCache(cacheName));
            }
        }

        return result;
    }

    private Cache createAndAddCache(String cacheName) {
        addCache(createCache(cacheName));
        return super.getCache(cacheName);
    }

    @SuppressWarnings("unchecked")
    private MyRedisCache createCache(String cacheName) {
        long expiration = computeExpiration(cacheName);
        return new MyRedisCache(cacheName, (usePrefix ? cachePrefix.prefix(cacheName) : null), template, expiration);
    }

    private long computeExpiration(String name) {
        Long expiration = null;
        if (expires != null) {
            expiration = expires.get(name);
        }
        return (expiration != null ? expiration.longValue() : defaultExpiration);
    }

    private List<MyRedisCache> loadAndInitRemoteCaches() {

        List<MyRedisCache> caches = new ArrayList<MyRedisCache>();

        try {
            Set<String> cacheNames = loadRemoteCacheKeys();
            if (!CollectionUtils.isEmpty(cacheNames)) {
                for (String cacheName : cacheNames) {
                    if (null == super.getCache(cacheName)) {
                        caches.add(createCache(cacheName));
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to initialize cache with remote cache keys.", e);
        }

        return caches;
    }

    @SuppressWarnings("unchecked")
    private Set<String> loadRemoteCacheKeys() {
        return (Set<String>) template.execute(new RedisCallback<Set<String>>() {

            @Override
            public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {

                // we are using the ~keys postfix as defined in RedisCache#setName
                Set<byte[]> keys = connection.keys(template.getKeySerializer().serialize("*~keys"));
                Set<String> cacheKeys = new LinkedHashSet<String>();

                if (!CollectionUtils.isEmpty(keys)) {
                    for (byte[] key : keys) {
                        cacheKeys.add(template.getKeySerializer().deserialize(key).toString().replace("~keys", ""));
                    }
                }

                return cacheKeys;
            }
        });
    }
}
