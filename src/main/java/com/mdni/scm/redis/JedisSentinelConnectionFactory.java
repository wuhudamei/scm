package com.mdni.scm.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConverters;
import org.springframework.util.Assert;
import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

public final class JedisSentinelConnectionFactory implements DisposableBean, RedisConnectionFactory {

    private static Logger log = LoggerFactory.getLogger(JedisSentinelConnectionFactory.class);

    private Pool<Jedis> pool = null;
    private int dbIndex = 0;
    private boolean convertPipelineAndTxResults = true;

    /**
     * Constructs a new <code>JedisConnectionFactory</code> instance. Will override the other connection parameters
     * passed to the factory.
     *
     * @param shardInfo shard information
     */
    public JedisSentinelConnectionFactory(Pool<Jedis> pool) {
        this(pool, 0);
    }

    public JedisSentinelConnectionFactory(Pool<Jedis> pool, int dbIndex) {
        this.pool = pool;
        this.dbIndex = dbIndex;
    }

    /**
     * Returns a Jedis instance to be used as a Redis connection. The instance can be newly created or retrieved from a
     * pool.
     *
     * @return Jedis instance ready for wrapping into a {@link RedisConnection}.
     */
    protected Jedis fetchJedisConnector() {
        try {
            return pool.getResource();
        } catch (Exception ex) {
            throw new RedisConnectionFailureException("Cannot get Jedis connection", ex);
        }
    }

    /**
     * Post process a newly retrieved connection. Useful for decorating or executing initialization commands on a new
     * connection. This implementation simply returns the connection.
     *
     * @param connection
     * @return processed connection
     */
    protected JedisConnection postProcessConnection(JedisConnection connection) {
        return connection;
    }

    @Override
    public void destroy() {
        try {
            pool.destroy();
        } catch (Exception ex) {
            log.warn("Cannot properly close Jedis pool", ex);
        }
        pool = null;
    }

    @Override
    public JedisConnection getConnection() {
        Jedis jedis = fetchJedisConnector();
        JedisConnection connection = new JedisConnection(jedis, pool, dbIndex);
        connection.setConvertPipelineAndTxResults(convertPipelineAndTxResults);
        return postProcessConnection(connection);
    }

    @Override
    public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
        return JedisConverters.toDataAccessException(ex);
    }

    /**
     * Returns the index of the database.
     *
     * @return Returns the database index
     */
    public int getDatabase() {
        return dbIndex;
    }

    /**
     * Sets the index of the database used by this connection factory. Default is 0.
     *
     * @param index database index
     */
    public void setDatabase(int index) {
        Assert.isTrue(index >= 0, "invalid DB index (a positive index required)");
        this.dbIndex = index;
    }

    /**
     * Specifies if pipelined results should be converted to the expected data type. If false, results of
     * {@link JedisConnection#closePipeline()} and {@link JedisConnection#exec()} will be of the type returned by the
     * Jedis driver
     *
     * @return Whether or not to convert pipeline and tx results
     */
    @Override
    public boolean getConvertPipelineAndTxResults() {
        return convertPipelineAndTxResults;
    }

    /**
     * Specifies if pipelined results should be converted to the expected data type. If false, results of
     * {@link JedisConnection#closePipeline()} and {@link JedisConnection#exec()} will be of the type returned by the
     * Jedis driver
     *
     * @param convertPipelineAndTxResults Whether or not to convert pipeline and tx results
     */
    public void setConvertPipelineAndTxResults(boolean convertPipelineAndTxResults) {
        this.convertPipelineAndTxResults = convertPipelineAndTxResults;
    }

}
