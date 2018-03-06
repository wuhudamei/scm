package cn.damei.scm.rest.account;

import cn.damei.scm.common.BaseController;
import cn.damei.scm.common.PropertyHolder;
import cn.damei.scm.common.dto.OauthAccessTokenUser;
import cn.damei.scm.common.dto.StatusDto;
import cn.damei.scm.common.utils.DateUtil;
import cn.damei.scm.common.utils.HttpUtils;
import cn.damei.scm.common.utils.JsonUtils;
import cn.damei.scm.service.account.LoginService;
import cn.damei.scm.shiro.ShiroSSORealm;
import com.fasterxml.jackson.databind.JavaType;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Controller
@RequestMapping(value = "/oauthCallBackAutoLogin")
public class OAuthCallbackAutoLoginController extends BaseController {

	@Autowired
	private LoginService loginService;
	
	@RequestMapping(method = RequestMethod.GET)
	public Object getAccessTokenAndAutoLogin(@RequestParam String code, @RequestParam String state,
		HttpServletRequest request, HttpServletResponse response) {
		
		String redirectTo = "redirect:/admin";
		StatusDto<OauthAccessTokenUser> status = getAccessToken(code);
		if (status == null) {
			return redirectTo;
		}
		
		if (!status.isSuccess()) {
			logger.error("获取单点登录系统OAuth accessToken失败,失败原因：{}", status.getMessage());
			return redirectTo;
		}
		
		loginService.login(new ShiroSSORealm.SSOToken(status.getData()), request, response);
		logger.info("用户{}单点登录成功,登录时间:{}", status.getData().getUser().getUsername(),
				DateUtil.formatDate(new Date(), DateUtil.DATE_TIME_PATTERN));
		return "redirect:/admin/index";
	}

	private StatusDto<OauthAccessTokenUser> getAccessToken(String code) {
		NameValuePair[] postParams = {new BasicNameValuePair("appid", PropertyHolder.getOauthCenterAppid()),
			new BasicNameValuePair("secret", PropertyHolder.getOauthCenterSecret()),
			new BasicNameValuePair("code", code), new BasicNameValuePair("scope", "true")};
		String respJson = HttpUtils.post(PropertyHolder.getOAuthAccessTokenUrl(), postParams);
		if (StringUtils.isNotBlank(respJson)) {
			JavaType javaType = JsonUtils.normalMapper.getMapper().getTypeFactory()
				.constructParametricType(StatusDto.class, OauthAccessTokenUser.class);
			StatusDto<OauthAccessTokenUser> tokenUserStatusDto = (StatusDto<OauthAccessTokenUser>) JsonUtils.fromJson(
				respJson, javaType); 
			return tokenUserStatusDto;
		}
		
		return null;
	}
}
