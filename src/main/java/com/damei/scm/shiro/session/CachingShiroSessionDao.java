package com.damei.scm.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * 针对自定义的ShiroSession的Redis CRUD操作，通过isChanged标识符，确定是否需要调用Update方法
 * 通过配置securityManager在属性cacheManager查找从缓存中查找Session是否存在，如果找不到才调用下面方法
 * Shiro内部相应的组件（DefaultSecurityManager）会自动检测相应的对象（如Realm）是否实现了CacheManagerAware并自动注入相应的CacheManager。
 * <p>
 * session缓存到本地 EhCache中
 **/
@Component
public class CachingShiroSessionDao extends CachingSessionDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ShiroSessionRepository shiroSessionRepository;

    /**
     * 重写CachingSessionDAO中readSession方法，如果Session中没有登陆信息就调用doReadSession方法从Redis中重读
     * session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null 代表没有登录，登录后Shiro会放入该值
     */
    @Override
    public Session readSession(Serializable sessionId) throws UnknownSessionException {
        //本地缓存的session
        Session session = super.getCachedSession(sessionId);
        Object principalSessionKey = null;

        /**if (session != null) {
            principalSessionKey = (Object) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        }
        boolean isLogined = principalSessionKey != null;
        if (session == null || !isLogined) {
         **/
        if (session == null) {
            session = this.doReadSession(sessionId);
            if (session == null) {
                throw new UnknownSessionException("There is no session with id [" + sessionId + "]");
            } else {
                // 重置Redis中缓存过期时间并缓存起来, 只有设置change才能更改最后一次访问时间
                ((ShiroSession) session).setChanged(true);
                super.update(session);
            }
        }
        return session;
    }

    /**
     * 从Redis中读取Session,并重置过期时间
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        return getShiroSessionRepository().getSession(sessionId);
    }

    /**
     * 直接从Redis中读取，但不重置Redis中缓存过期时间
     */
    public Session doReadSessionWithoutExpire(Serializable sessionId) {
        return getShiroSessionRepository().getSessionWithoutExpire(sessionId);
    }

    /**
     * 如DefaultSessionManager在创建完session后会调用该方法； 如保存到关系数据库/文件系统/NoSQL数据库；即可以实现会话的持久化；
     * 返回会话ID；主要此处返回的ID.equals(session.getId())；
     */
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        getShiroSessionRepository().saveSession(session);
        return sessionId;
    }

    /**
     * 更新会话；如更新会话最后访问时间/停止会话/设置超时时间/设置移除属性等会调用
     */
    @Override
    protected void doUpdate(Session session) {
        //如果会话过期/停止 没必要再更新了
        if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
            return;
        }

        try {
            if (session instanceof ShiroSession) {
                // 如果没有主要字段(除lastAccessTime以外其他字段)发生改变
                ShiroSession ss = (ShiroSession) session;
                if (!ss.isChanged()) {
                    return;
                }
                ss.setChanged(false);
                ss.setLastAccessTime(new Date());
                shiroSessionRepository.saveSession(session);
            }
        } catch (Exception e) {
            logger.error("更新Session失败", e);
        }
    }

    /**
     * 删除会话；当会话过期/会话停止（如用户退出时）会调用
     */
    @Override
    public void doDelete(Session session) {
        Serializable id = session.getId();
        if (id != null) {
            getShiroSessionRepository().deleteSession(id);
        }
    }

    /**
     * 删除cache中缓存的Session
     */
    public void uncache(Serializable sessionId) {
        try {
            Session session = super.getCachedSession(sessionId);
            super.uncache(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回本机Ehcache中Session
     */
    public Collection<Session> getEhCacheActiveSessions() {
        return super.getActiveSessions();
    }

    public ShiroSessionRepository getShiroSessionRepository() {
        return shiroSessionRepository;
    }

    public void setShiroSessionRepository(ShiroSessionRepository shiroSessionRepository) {
        this.shiroSessionRepository = shiroSessionRepository;
    }
}
