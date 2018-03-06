package cn.damei.scm.shiro.listener;

import cn.damei.scm.shiro.session.ShiroSessionService;
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

    @Override
    public void onStop(Session session) {
        sessionService.deleteSession(session);
    }

    @Override
    public void onExpiration(Session session) {
        sessionService.deleteSession(session);
    }

}
