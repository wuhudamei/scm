package com.mdni.scm.common;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.net.ssl.*;
import javax.servlet.ServletContext;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * 在ServletContext中设置全局变量。可以在JSP中用EL获取。例如:${ctx}
 * 
 * @author liuwei
 */
@Component
@Lazy(false)
public class PropertyHolder implements ServletContextAware, ApplicationContextAware {

	public static ApplicationContext appCtx;
	private static ServletContext servletContext;
	private static String imageBaseUrl;
	private static String uploadDir;
	private static String baseurl;

	/** 任务中心appid **/
	private static String oauthCenterAppid;

	/** 任务中心secret **/
	private static String oauthCenterSecret;

	/** 创建账号调用地址 **/
	private static String mdniUCenterCreateUserUrl;
	/**sso 域名**/
	private static String oauthCenterDomain;

	private static String oAuthLoginUrl;
	private static String oAuthLogoutUrl;
	private static String oAuthAccessTokenUrl;
	private static String oAuthAppTokenUrl;
	private static String oAuthAppUserUrl;

	//OA系统门店信息同步接口路径
	private static String oaStoreCogradientUrl;

	static {
		disableSslVerification();
	}

	public static ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext ctx) {
		PropertyHolder.servletContext = ctx;
		ctx.setAttribute("ctx", ctx.getContextPath());
		ctx.setAttribute("imageBaseUrl", imageBaseUrl);
	}

	/**
	 * 此BaseUrl 就是 StaticContentServlet 的url-pattern中访问路径 例如：/file/
	 */
	public static String getImageBaseUrl() {
		return PropertyHolder.imageBaseUrl;
	}

	@Value("${image.base.url}")
	public void setImageBaseUrl(String imageBaseUrl) {
		PropertyHolder.imageBaseUrl = imageBaseUrl;
	}

	public static String getUploadDir() {
		return PropertyHolder.uploadDir;
	}

	@Value("${upload.dir}")
	public void setUploadDir(String uploadDir) {
		PropertyHolder.uploadDir = uploadDir;
	}

	public static String getBaseurl() {
		return PropertyHolder.baseurl;
	}

	@Value("${baseurl}")
	public void setBaseurl(String baseurl) {
		PropertyHolder.baseurl = baseurl;
	}

	/** 设置 认证中心appid **/
	@Value("${oauth.center.appid}")
	public void setOauthCenterAppid(String oauthCenterAppid) {
		PropertyHolder.oauthCenterAppid = oauthCenterAppid;
	}

	/** 设置 认证中心secret **/
	@Value("${oauth.center.secret}")
	public void setOAuthCenterSecret(String oauthCenterSecret) {
		PropertyHolder.oauthCenterSecret = oauthCenterSecret;
	}

	/** 获取 认证中心secret **/
	public static String getOauthCenterSecret() {
		return PropertyHolder.oauthCenterSecret;
	}

	/** 获取 认证中心appid **/
	public static String getOauthCenterAppid() {
		return PropertyHolder.oauthCenterAppid;
	}

	@Value("${oauth.login.url}")
	public void setOAuthLoginUrl(String oauthLoginUrl) {
		PropertyHolder.oAuthLoginUrl = oauthLoginUrl;
	}

	public static String getOAuthLoginUrl() {
		return PropertyHolder.oAuthLoginUrl;
	}

	@Value("${oauth.logout.url}")
	public void setOAuthLogoutUrl(String oauthLogoutUrl) {
		PropertyHolder.oAuthLogoutUrl = oauthLogoutUrl;
	}

	public static String getOAuthLogoutUrl() {
		return PropertyHolder.oAuthLogoutUrl;
	}

	@Value("${oauth.accessToken.url}")
	public void setOAuthAccessTokenUrl(String oauthAccessTokenUrl) {
		PropertyHolder.oAuthAccessTokenUrl = oauthAccessTokenUrl;
	}

	public static String getOAuthAccessTokenUrl() {
		return PropertyHolder.oAuthAccessTokenUrl;
	}

	@Value("${oauth.appToken.url}")
	public void setOAuthAppTokenUrl(String oauthAppTokenUrl) {
		PropertyHolder.oAuthAppTokenUrl = oauthAppTokenUrl;
	}

	public static String getOAuthAppTokenUrl() {
		return PropertyHolder.oAuthAppTokenUrl;
	}

	@Value("${oauth.appUser.url}")
	public void setOAuthAppUserUrl(String oauthAppUserUrl) {
		PropertyHolder.oAuthAppUserUrl = oauthAppUserUrl;
	}

	public static String getOAuthAppUserUrl() {
		return PropertyHolder.oAuthAppUserUrl;
	}

	public static String getOauthCenterDomain() {
		return oauthCenterDomain;
	}
	@Value("${oauth.center.domain}")
	public  void setOauthCenterDomain(String oauthCenterDomain) {
		PropertyHolder.oauthCenterDomain = oauthCenterDomain;
	}

	public static String getFullImageUrl(String imagePath) {
		if (imagePath == null) {
			return null;
		}
		return PropertyHolder.getImageBaseUrl() + imagePath;
	}

	public static String getMdniUcenterCreateAccountUrl() {
		return PropertyHolder.mdniUCenterCreateUserUrl;
	}

	@Value("${ucenter.mdni.createAccount.url}")
	public void setMdniUCenterCreateAccoutUrl(String createAccountUrl) {
		PropertyHolder.mdniUCenterCreateUserUrl = createAccountUrl;
	}

	@Value("${oa.storeCogradient.url}")
	public void setOaStoreCogradientUrl(String oaStoreCogradientUrl){
		PropertyHolder.oaStoreCogradientUrl = oaStoreCogradientUrl;
	}

	public static String getOaStoreCogradientUrl(){
		return PropertyHolder.oaStoreCogradientUrl;
	}

	private static void disableSslVerification() {
		try {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			}};

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appCtx = applicationContext;
	}

}