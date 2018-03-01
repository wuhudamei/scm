package com.damei.scm.shiro.session;

import com.damei.scm.redis.JedisTemplate.JedisAction;
import com.damei.scm.shiro.JedisManager;
import com.damei.scm.shiro.SerializeObjectUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.util.Collection;


/**
 * redis save shiro session
 *
 * @author zhangmin
 */
public class JedisShiroSessionToStringRepository implements ShiroSessionRepository {

    public static final String REDIS_SHIRO_SESSION_PREFIX = "shiro-session-str:";

    //session默认超时 半小时
    private static final long DEFAULT_SESSION_TIMEOUT = 1800000L;
    private final Logger logger = LoggerFactory.getLogger(JedisShiroSessionToStringRepository.class);
    private JedisManager jedisManager;
    private long sessionTimeOut = DEFAULT_SESSION_TIMEOUT;

    //session对象序列化成 字符串 &保存到redis中
    @Override
    public void saveSession(Session session) {
        if (session == null || session.getId() == null)
            throw new NullPointerException("session is empty");
        try {
            String key = buildRedisSessionKey(session.getId());
            String sessionString = SerializeObjectUtil.serializeToString((SimpleSession) session);
            session.setTimeout(sessionTimeOut);
            long expireTime = sessionTimeOut / 1000;
            getJedisManager().setex(key, sessionString, (int) expireTime);
        } catch (Exception e) {
            logger.error("save session error", e);
        }
    }

    @Override
    public void deleteSession(Serializable id) {
        if (id == null) {
            throw new NullPointerException("session id is empty");
        }
        try {
            getJedisManager().del(buildRedisSessionKey(id));
        } catch (Exception e) {
            logger.error("delete session error", e);
        }
    }

    //从redis中获得session,并重置session TimeOut
    @Override
    public Session getSession(final Serializable id) {
        if (id == null)
            throw new NullPointerException("session id is empty");
        Session session = null;
        try {
            session = getJedisManager().execute(new JedisAction<Session>() {
                @Override
                public Session action(Jedis jedis) {
                    String key = buildRedisSessionKey(id);
                    String sessionString = jedis.get(key);
                    Session sessObj = null;
                    if (StringUtils.isNotBlank(sessionString)) {
                        sessObj = (Session) SerializeObjectUtil.deserializeFromString(sessionString);
                        long expire = sessionTimeOut / 1000;
                        // 重置Redis中缓存过期时间
                        jedis.expire(key, (int) expire);
                    }
                    return sessObj;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("get redis session error {}", id);
        }
        return session;
    }

    //直接从redis获得session，但是不重置 session timeout
    @Override
    public Session getSessionWithoutExpire(Serializable sessionId) {
        if (sessionId == null)
            throw new NullPointerException("session id is empty");
        String key = buildRedisSessionKey(sessionId);
        String sessionString = getJedisManager().get(key);
        Session session = null;
        if (StringUtils.isNotBlank(sessionString)) {
            session = (Session) SerializeObjectUtil.deserializeFromString(sessionString);
        }
        return session;
    }

    @Override
    public Collection<Session> getAllSessions() {
        return null;
    }

    private String buildRedisSessionKey(Serializable sessionId) {
        return REDIS_SHIRO_SESSION_PREFIX + sessionId;
    }

    public JedisManager getJedisManager() {
        return jedisManager;
    }

    public void setJedisManager(JedisManager jedisManager) {
        this.jedisManager = jedisManager;
    }

    public long getSessionTimeOut() {
        return sessionTimeOut;
    }

    public void setSessionTimeOut(long sessionTimeOut) {
        this.sessionTimeOut = sessionTimeOut;
    }
}
