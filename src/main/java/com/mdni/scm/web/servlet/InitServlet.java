package com.mdni.scm.web.servlet;

import org.apache.velocity.app.Velocity;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import java.util.Properties;


/**
 * @author zhangmin
 */
public final class InitServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void init() {
        final ServletContext application = getServletContext();
        initVelocityRuntime(application);
    }

    private void initVelocityRuntime(ServletContext application) {
        Properties velocityProps = new Properties();
        velocityProps.setProperty(Velocity.RUNTIME_LOG, application.getRealPath("/WEB-INF/log/velocity.log"));
        velocityProps.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, application.getRealPath("/WEB-INF/pages/vm"));
        //velocityProps.setProperty(Velocity.INPUT_ENCODING, JsonUtils.DEFAULT_ENCODING);
        //velocityProps.setProperty(Velocity.OUTPUT_ENCODING, JsonUtils.DEFAULT_ENCODING);
        velocityProps.setProperty(Velocity.COUNTER_NAME, "velocityCount");
        velocityProps.setProperty(Velocity.COUNTER_INITIAL_VALUE, "0");
        try {
            Velocity.init(velocityProps);
        } catch (Exception e) {
            throw new ExceptionInInitializerError("org.controller.servlet.InitServlet velocity��ʼ��ʱ�������!");
        }
    }
}