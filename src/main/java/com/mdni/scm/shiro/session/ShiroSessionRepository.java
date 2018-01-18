package com.mdni.scm.shiro.session;

import org.apache.shiro.session.Session;

import java.io.Serializable;
import java.util.Collection;

/**
 * shiro session 保存
 *
 * @author zhangmin
 */
public interface ShiroSessionRepository {

    void saveSession(Session session);

    void deleteSession(Serializable sessionId);

    //从redis中获得session,并重置session TimeOut
    Session getSession(Serializable sessionId);

    //只从redis读取session,不重置 session timeout
    Session getSessionWithoutExpire(Serializable sessionId);

    Collection<Session> getAllSessions();
}
