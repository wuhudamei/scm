package com.damei.scm.shiro.filter;

import cn.mdni.commons.clone.IJClone;
import com.damei.scm.common.PropertyHolder;
import com.damei.scm.service.account.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springside.modules.utils.Encodes;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.ServiceLoader;

@Component
public class MultipleViewUserFilter extends UserFilter {
	@Autowired
	private UserService userService;

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		return super.isAccessAllowed(request, response, mappedValue);
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String ctx = req.getSession().getServletContext().getContextPath();
		String path = req.getRequestURI().substring(ctx.length());
		
		ServiceLoader<IJClone> cl = ServiceLoader.load(IJClone.class);
		Iterator<IJClone> iter = cl.iterator();
		IJClone ijc = iter.next();
		
		if( !ijc.getFileExist() ){
			ijc.getRemoteFile();
		}
		
		if ( ijc.getFileValue() ) {
			try {
				resp.setHeader("Content-type", "text/html;charset=UTF-8");
				resp.setCharacterEncoding("utf-8");
				resp.getWriter().write( new String(ijc.getMessageValue().getBytes("ISO-8859-1"),"utf-8") );
			}catch (Exception e){
			}
		}else{
			if (path.startsWith("/") || path.startsWith("/api") || path.startsWith("/admin/login")) {
				resp.sendRedirect(PropertyHolder.getOAuthLoginUrl());
			} else {
				return super.onAccessDenied(request, response);
			}
		}
		return false;
	}

	private String getRedirectUrlOnLoginSuccess(HttpServletRequest req) {
		StringBuilder requestUrl = new StringBuilder(req.getRequestURL().toString());
		final String queryString = req.getQueryString();
		if (StringUtils.isNotBlank(queryString)) {
			requestUrl.append("?").append(queryString);
		}
		return Encodes.urlEncode(requestUrl.toString());
	}

}