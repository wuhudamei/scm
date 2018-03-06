package cn.damei.scm.web.filter;

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

public class XSSFilter implements Filter {

	private String stopWords;

	private String escapeChars;

	private String escapeTo;

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

	}
}