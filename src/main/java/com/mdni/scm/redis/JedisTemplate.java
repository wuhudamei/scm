package com.mdni.scm.redis;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springside.modules.nosql.redis.JedisUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.Pool;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * JedisTemplate 提供了一个template方法，负责对Jedis连接的获取与归还。 JedisAction<T> 和 JedisActionNoResult两种回调接口，适用于有无返回值两种情况。
 * 同时提供一些最常用函数的封装, 如get/set/zadd等。
 */
public class JedisTemplate {
    private static Logger logger = LoggerFactory.getLogger(JedisTemplate.class);

    private final Pool<Jedis> jedisPool;

    public JedisTemplate(Pool<Jedis> jedisPool) {
        this.jedisPool = jedisPool;
    }

    /**
     * 执行有返回结果的action。
     */
    public <T> T execute(JedisAction<T> jedisAction) throws JedisException {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = jedisPool.getResource();
            return jedisAction.action(jedis);
        } catch (JedisConnectionException e) {
            logger.error("Redis connection lost.", e);
            broken = true;
            throw e;
        } finally {
            closeResource(jedis, broken);
        }
    }

    /**
     * 执行无返回结果的action。
     */
    public void execute(JedisActionNoResult jedisAction) throws JedisException {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = jedisPool.getResource();
            jedisAction.action(jedis);
        } catch (JedisConnectionException e) {
            logger.error("Redis connection lost.", e);
            broken = true;
            throw e;
        } finally {
            closeResource(jedis, broken);
        }
    }

    /**
     * 根据连接是否已中断的标志，分别调用returnBrokenResource或returnResource。
     */
    protected void closeResource(Jedis jedis, boolean connectionBroken) {
        if (jedis != null) {
            try {
                if (connectionBroken) {
                    //destroy redis对象
                    jedisPool.returnBrokenResource(jedis);
                } else {
                    //返还到连接池
                    jedisPool.returnResource(jedis);
                }
            } catch (Exception e) {
                logger.error("Error happen when return jedis to pool, try to close it directly.", e);
                JedisUtils.closeJedis(jedis);
            }
        }
    }

    /**
     * 基于乐观锁 实现的计数器
     */
    public Long incrByOptimisticLock(final String counterKey) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                // 监视指定key，如果在事务中key的值被另一个客户端修改，那么当前客户端事务执行失败，但是事务前面已经执行的语句不会回滚
                jedis.watch(counterKey);
                // 开启事务
                Transaction redisTranct = jedis.multi();
                //执行key自增操作
                redisTranct.incr(counterKey);
                //提交事务，result值为null，表示事务对key进行自增操作执行失败
                List<Object> result = redisTranct.exec();
                if (result != null && result.size() == 1) {
                    return NumberUtils.toLong(result.get(0).toString());
                }
                return null;
            }
        });
    }

    /**
     * 通过乐观锁 执行操作数据库的业务代码
     */
    public void executeByOptimisticLock(final String lockCounter, final BussiCallback action) {

        execute(new JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                // 监视指定key，如果在事务中key的值被另一个客户端修改，那么当前客户端事务执行失败，但是事务前面已经执行的语句不会回滚
                jedis.watch(lockCounter);

                // 开启事务
                Transaction redisTranct = jedis.multi();
                //执行key自增操作
                redisTranct.incr(lockCounter);
                action.doBussiInAction();

                //提交事务，result值为null，表示事务对key进行自增操作执行失败
                List<Object> result = redisTranct.exec();
                if (result == null) {
                    action.rollback();//业务代码回滚
                }
            }
        });

    }

    /**
     * 获取内部的pool做进一步的动作。
     */
    public Pool<Jedis> getJedisPool() {
        return jedisPool;
    }

    /**
     * 删除key, 如果key存在返回true, 否则返回false。
     */
    public Boolean del(final String... keys) {
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return jedis.del(keys) == 1 ? true : false;
            }
        });
    }

    public void flushDB() {
        execute(new JedisActionNoResult() {

            @Override
            public void action(Jedis jedis) {
                jedis.flushDB();
            }
        });
    }

    /**
     * 如果key不存在, 返回null.
     */
    public String get(final String key) {
        return execute(new JedisAction<String>() {

            @Override
            public String action(Jedis jedis) {
                return jedis.get(key);
            }
        });
    }

    // ////////////// 常用方法的封装 ///////////////////////// //

    // ////////////// 公共 ///////////////////////////

    /**
     * 判断key是否存在
     *
     * @param key
     */
    public boolean hasKey(final String key) {
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return jedis.exists(key);
            }
        });
    }

    /**
     * 设置key的有效期,true表示设置成功
     */
    public boolean expire(final String key, final int seconds) {
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return jedis.expire(key, seconds) == 1;
            }
        });
    }

    // ////////////// 关于String ///////////////////////////

    public boolean expireAt(final String key, final long expireAt) {
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return jedis.expireAt(key, expireAt) == 1;
            }
        });
    }

    /**
     * 如果key不存在, 返回null.
     */
    public Long getAsLong(final String key) {
        String result = get(key);
        return result != null ? NumberUtils.toLong(result) : null;
    }

    public Long getAsLong(final String key, Long defautValue) {
        Long num = getAsLong(key);
        return num != null ? num : defautValue;
    }

    /**
     * 如果key不存在, 返回null.
     */
    public Integer getAsInt(final String key) {
        String result = get(key);
        return result != null ? NumberUtils.toInt(result) : null;
    }

    public Integer getAsInt(final String key, Integer defaultValue) {
        String result = get(key);
        return result != null ? NumberUtils.toInt(result) : defaultValue;
    }

    public void set(final String key, final String value) {
        execute(new JedisActionNoResult() {

            @Override
            public void action(Jedis jedis) {
                jedis.set(key, value);
            }
        });
    }

    public void setex(final String key, final String value, final int seconds) {
        execute(new JedisActionNoResult() {

            @Override
            public void action(Jedis jedis) {
                jedis.setex(key, seconds, value);
            }
        });
    }

    /**
     * 如果key还不存在则进行设置，返回true，否则返回false.
     */
    public Boolean setnx(final String key, final String value) {
        return execute(new JedisAction<Boolean>() {

            @Override
            public Boolean action(Jedis jedis) {
                return jedis.setnx(key, value) == 1 ? Boolean.TRUE : Boolean.FALSE;
            }
        });
    }

    /**
     * 设置新值，并返回旧值
     */
    public String getset(final String key, final String value) {
        return execute(new JedisAction<String>() {

            @Override
            public String action(Jedis jedis) {
                return jedis.getSet(key, value);
            }
        });
    }

    public Long getsetAsLong(final String key, final long value) {
        return NumberUtils.toLong(getset(key, String.valueOf(value)), Long.MAX_VALUE);
    }

    /**
     * 综合setNX与setEx的效果。
     */
    public Boolean setnxex(final String key, final String value, final int seconds) {
        return execute(new JedisAction<Boolean>() {

            @Override
            public Boolean action(Jedis jedis) {
                String result = jedis.set(key, value, "NX", "EX", seconds);
                return JedisUtils.isStatusOk(result);
            }
        });
    }

    public Long incr(final String key) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.incr(key);
            }
        });
    }

    public Long incrBy(final String key, final long increment) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.incrBy(key, increment);
            }
        });
    }

    public Long decr(final String key) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.decr(key);
            }
        });
    }

    /**
     * 压栈操作,后压入的在栈顶
     */
    public void lpush(final String key, final String... values) {
        execute(new JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.lpush(key, values);
            }
        });
    }

    /**
     * 在key对应list的尾部添加字符串元素,相当于加入队列
     */
    public void rpush(final String key, final String... values) {
        execute(new JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.rpush(key, values);
            }
        });
    }

    /**
     * 清空list
     */
    public void clearList(final String key) {
        execute(new JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                //ltrim操作，如果start > end 则清空整个List =>等同于 del key
                jedis.ltrim(key, 1, 0);
            }
        });
    }

    // ////////////// 关于List ///////////////////////////

    public List<String> getListAllItem(final String key) {
        return execute(new JedisAction<List<String>>() {
            @Override
            public List<String> action(Jedis jedis) {
                return jedis.lrange(key, 0, -1);
            }
        });
    }

    public String rpop(final String key) {
        return execute(new JedisAction<String>() {

            @Override
            public String action(Jedis jedis) {
                return jedis.rpop(key);
            }
        });
    }

    /**
     * 返回List长度, key不存在时返回0，key类型不是list时抛出异常.
     */
    public Long llen(final String key) {
        return execute(new JedisAction<Long>() {

            @Override
            public Long action(Jedis jedis) {
                return jedis.llen(key);
            }
        });
    }

    /**
     * 删除List中的第一个等于value的元素，value不存在或key不存在时返回false.
     */
    public Boolean lremOne(final String key, final String value) {
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                Long count = jedis.lrem(key, 1, value);
                return (count == 1);
            }
        });
    }

    /**
     * 删除List中的所有等于value的元素，value不存在或key不存在时返回false.
     */
    public Boolean lremAll(final String key, final String value) {
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                Long count = jedis.lrem(key, 0, value);
                return (count > 0);
            }
        });
    }

    /**
     * 加入Sorted set, 如果member在Set里已存在, 只更新score并返回false, 否则返回true.
     */
    public Boolean zadd(final String key, final String member, final double score) {
        return execute(new JedisAction<Boolean>() {

            @Override
            public Boolean action(Jedis jedis) {
                return jedis.zadd(key, score, member) == 1 ? true : false;
            }
        });
    }

    /**
     * 向 Sorted set 一次加入多个成员，返回成功加入的元素个数，如果member在Set里已存在, 只更新score
     */
    public Long zmutiadd(final String key, final Map<String, Double> scoreMemberMap) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.zadd(key, scoreMemberMap);
            }
        });
    }

    public boolean hasMemberInzset(final String key, final String member) {
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                Long index = jedis.zrank(key, member);
                return index != null && index >= 0;
            }
        });
    }

    // ////////////// 关于Sorted Set ///////////////////////////

    /**
     * 删除sorted set中的元素，成功删除返回true，key或member不存在返回false。
     */
    public Boolean zrem(final String key, final String member) {
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return jedis.zrem(key, member) == 1 ? Boolean.TRUE : Boolean.FALSE;
            }
        });
    }

    /**
     * 按照评分逆序排列所有成员,评分高的排前
     */
    public List<String> findAllMemberOfzsetByScoreDesc(final String key) {
        Set<String> memberSet = execute(new JedisAction<Set<String>>() {
            @Override
            public Set<String> action(Jedis jedis) {
                return jedis.zrevrangeByScore(key, "+inf", "-inf");
            }
        });

        List<String> memberList = Collections.emptyList();
        if (!CollectionUtils.isEmpty(memberSet)) {
            memberList = Lists.newArrayList(memberSet.iterator());
        }
        return memberList;
    }

    /**
     * 删除指定索引位置的成员, 成员是按照score升序排列的
     *
     * @param key
     * @param index 索引序号
     * @return 是否删除成功
     */
    public boolean zremOfIndexByScoreAsc(final String key, final long index) {
        return zremRangeByScoreAsc(key, index, index) == 1;
    }

    /**
     * 返回被删除的数量
     */
    public Long zremRangeByScoreAsc(final String key, final long startIdx, final long endIndex) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.zremrangeByRank(key, startIdx, endIndex);
            }
        });
    }

    /**
     * 当key不存在时返回null.
     */
    public Double zscore(final String key, final String member) {
        return execute(new JedisAction<Double>() {

            @Override
            public Double action(Jedis jedis) {
                return jedis.zscore(key, member);
            }
        });
    }

    /**
     * 返回sorted set长度, key不存在时返回0.
     */
    public Long zcard(final String key) {
        return execute(new JedisAction<Long>() {

            @Override
            public Long action(Jedis jedis) {
                return jedis.zcard(key);
            }
        });
    }

    /**
     * 有返回结果的回调接口定义。
     */
    public interface JedisAction<T> {
        T action(Jedis jedis);
    }

    public interface BussiCallback {
        public void doBussiInAction();

        public void rollback();
    }

    /**
     * 无返回结果的回调接口定义。
     */
    public interface JedisActionNoResult {
        void action(Jedis jedis);
    }
}
