package com.mdni.scm.web.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Splitter;

/**
 * 过滤器功能：解决跨站脚本攻击和注入
 * 
 * @author zhangmin
 */
public class XSSFilter implements Filter {

	private String stopWords;

	//需要转义的字符
	private String escapeChars;

	//转义成目标字符
	private String escapeTo;

	//不需要执行xss转义的路径
	private List<String> excludeXssPathList;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		stopWords = filterConfig.getInitParameter("stopWords");
		escapeChars = filterConfig.getInitParameter("escapeChars");
		escapeTo = filterConfig.getInitParameter("escapeTo");
		String excludeXssPath = filterConfig.getInitParameter("excludeXssPath");
		excludeXssPathList = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(excludeXssPath);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
		ServletException {

		HttpServletRequest req = (HttpServletRequest) request;

		StopWordHttpServletRequestWrapper stopWordRequest = new StopWordHttpServletRequestWrapper(req, stopWords);
		XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(stopWordRequest, escapeChars,
			escapeTo, excludeXssPathList);

		chain.doFilter(xssRequest, response);
	}

	@Override
	public void destroy() {
		//do nothing
	}
}