package com.mdni.scm.shiro.listener;

import com.mdni.scm.shiro.session.ShiroSessionService;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CustomSessionListener implements SessionListener {

    @Autowired
    private ShiroSessionService sessionService;

    @Override
    public void onStart(Session session) {
        //TODO
    }

    //会话被停止时触发
    @Override
    public void onStop(Session session) {
        sessionService.deleteSession(session);
    }

    //会话过期时触发
    @Override
    public void onExpiration(Session session) {
        sessionService.deleteSession(session);
    }

}
