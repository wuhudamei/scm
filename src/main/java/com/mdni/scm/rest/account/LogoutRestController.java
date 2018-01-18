package com.mdni.scm.rest.account;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mdni.scm.common.PropertyHolder;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.common.utils.HttpUtils;
import com.mdni.scm.common.utils.JsonUtils;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.service.account.LogoutService;
import com.mdni.scm.shiro.ShiroUser;

/**
 * @author zhangmin
 */
@Controller
@RequestMapping("/logout")
public class LogoutRestController {

	private final static String admin = "admin";
	private final static String pc = "pc";
	private final static String wap = "wap";
	@Autowired
	private LogoutService logoutService;

	@RequestMapping(value = "{clientType}", method = RequestMethod.GET)
	public void logout(@PathVariable String clientType, HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		if (oAuthLogout()) {
			logoutService.logout(request, response);
			if (admin.equals(clientType)) {
				response.sendRedirect("/admin/logout");
			} else if (pc.equals(clientType)) {

			} else if (wap.equals(clientType)) {

			}
		}

	}

	/**
	 * 退出,返回是否退出成功
	 */
	private boolean oAuthLogout() {
		ShiroUser loggedUser = WebUtils.getLoggedUser();
		if (loggedUser == null)
			return false;

		NameValuePair[] quitParams = {new BasicNameValuePair("appid", PropertyHolder.getOauthCenterAppid()),
			new BasicNameValuePair("secret", PropertyHolder.getOauthCenterSecret()),
			new BasicNameValuePair("username", loggedUser.getLoginName())};
		String quitRespJson = HttpUtils.post(PropertyHolder.getOAuthLogoutUrl(), quitParams);
		if (StringUtils.isBlank(quitRespJson)) {
			return false;
		}

		StatusDto<String> quitStatus = JsonUtils.fromJson(quitRespJson, StatusDto.class);
		return quitStatus.isSuccess();
	}

}
