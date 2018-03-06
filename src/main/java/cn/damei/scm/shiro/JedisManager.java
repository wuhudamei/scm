package cn.damei.scm.shiro;

//import JedisTemplate;

import cn.damei.scm.redis.JedisTemplate;
import org.springframework.util.Assert;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.Pool;

public class JedisManager {

    private Pool<Jedis> jedisPool;

    private int dbIndex;

    public Jedis getJedis() {
        Jedis jedis = null;
        try {
            jedis = getJedisPool().getResource();
            //jedis.select(dbIndex);
        } catch (Exception e) {
            throw new JedisConnectionException(e);
        }
        return jedis;
    }

    public void returnResource(Jedis jedis, boolean isBroken) {
        if (jedis == null)
            return;
        if (isBroken)
            getJedisPool().returnBrokenResource(jedis);
        else
            getJedisPool().returnResource(jedis);
    }

    public byte[] getValueByKey(byte[] key) throws Exception {
        Jedis jedis = null;
        byte[] result = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            isBroken = true;
            throw e;
        } finally {
            returnResource(jedis, isBroken);
        }
        return result;
    }

    public Boolean del(final String... keys) {
        return execute(new JedisTemplate.JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return jedis.del(keys) == 1 ? true : false;
            }
        });
    }

    public void deleteByKey(byte[] key) throws Exception {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.del(key);
        } catch (Exception e) {
            isBroken = true;
            throw e;
        } finally {
            returnResource(jedis, isBroken);
        }
    }

    public void saveValueByKey(byte[] key, byte[] value, int expireTime) throws Exception {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.set(key, value);
            if (expireTime > 0)
                jedis.expire(key, expireTime);
        } catch (Exception e) {
            isBroken = true;
            throw e;
        } finally {
            returnResource(jedis, isBroken);
        }
    }

    public void execute(JedisTemplate.JedisActionNoResult jedisAction) throws JedisException {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = getJedis();
            jedisAction.action(jedis);
        } catch (JedisConnectionException e) {
            broken = true;
            throw e;
        } finally {
            returnResource(jedis, broken);
        }
    }

    public <T> T execute(JedisTemplate.JedisAction<T> jedisAction) throws JedisException {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = getJedis();
            return jedisAction.action(jedis);
        } catch (JedisConnectionException e) {
            broken = true;
            throw e;
        } finally {
            returnResource(jedis, broken);
        }
    }

    public void setex(final String key, final String value, final int seconds) {
        execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.setex(key, seconds, value);
            }
        });
    }

    public String get(final String key) {
        return execute(new JedisTemplate.JedisAction<String>() {

            @Override
            public String action(Jedis jedis) {
                return jedis.get(key);
            }
        });
    }

    public boolean expire(final String key, final int seconds) {
        return execute(new JedisTemplate.JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return jedis.expire(key, seconds) == 1;
            }
        });
    }

    public Pool<Jedis> getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(Pool<Jedis> jedisPool) {
        this.jedisPool = jedisPool;
    }

    public int getDatabase() {
        return dbIndex;
    }

    public void setDatabase(int index) {
        Assert.isTrue(index >= 0, "invalid DB index (a positive index required)");
        this.dbIndex = index;
    }
}