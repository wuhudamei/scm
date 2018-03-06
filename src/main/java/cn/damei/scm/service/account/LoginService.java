package cn.damei.scm.service.account;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.damei.scm.common.Constants;
import cn.damei.scm.common.dto.StatusDto;
import cn.damei.scm.common.utils.WebUtils;
import cn.damei.scm.redis.CacheKeys;
import cn.damei.scm.redis.JedisTemplate;

@SuppressWarnings("all")
@Service
public class LoginService {
	private final Logger logger = LoggerFactory.getLogger(LoginService.class);
	@Autowired
	private LogoutService logoutService;
	@Autowired
	private JedisTemplate jedisTemplate;

	private static String getLoginTriesIpKey(HttpServletRequest request) {
		return CacheKeys.LOGIN_TRIES_IP_PREFIX + WebUtils.getClientIpAddr(request);
	}

	private static String getLoginTriesLoginNameKey(String loginName) {
		return CacheKeys.LOGIN_TRIES_LOGINNAME_PREFIX + loginName;
	}

	public Object login(String loginName, String password, boolean rememberMe, HttpServletRequest request,
		HttpServletResponse response) {
		try {

			login(new UsernamePasswordToken(loginName, password, rememberMe), request, response);

			return StatusDto.buildSuccessStatusDto();
		} catch (AuthenticationException e) {
			saveLoginTries(request, loginName);
			return StatusDto.buildFailureStatusDto(getErrorResponseEntity(e));
		}
	}

	public void login(AuthenticationToken token, HttpServletRequest request, HttpServletResponse response)
		throws AuthenticationException {
		Subject subject = SecurityUtils.getSubject();
		if (subject.getPrincipal() != null) {
			logoutService.logout(request, response);
		}
		subject.login(token);
	}

	private void saveLoginTries(final HttpServletRequest request, final String loginName) {
		String lockIpKey = getLoginTriesIpKey(request);
		Long ipTryTimes = jedisTemplate.incr(lockIpKey);
		jedisTemplate.expire(lockIpKey, Constants.SECONDS_REMEMBER_LOGIN_TRIES);
		int count = ipTryTimes.intValue();

		logger.debug("IP {} 登录失败 {} 次", request.getRemoteHost(), count);
		String lockLoginNameKey = getLoginTriesLoginNameKey(loginName);
		Long loginNameTryTimes = jedisTemplate.incr(lockLoginNameKey);
		jedisTemplate.expire(lockLoginNameKey, Constants.SECONDS_REMEMBER_LOGIN_TRIES);
		count = loginNameTryTimes.intValue();
		logger.debug("用户 {} 登录失败 {} 次", loginName, count);
	}

	private boolean checkLoginTries(HttpServletRequest request, String loginName) {
		int ipTries = NumberUtils.toInt(jedisTemplate.get(getLoginTriesIpKey(request)), 0);
		int loginNameTries = NumberUtils.toInt(jedisTemplate.get(getLoginTriesLoginNameKey(loginName)), 0);

		return ipTries <= Constants.MAX_LOGIN_TRIES_IP && loginNameTries <= Constants.MAX_LOGIN_TRIES_LOGINNAME;
	}

	private String getErrorResponseEntity(AuthenticationException e) {
		final String loginFail = "账户或密码不正确";

		if (e instanceof IncorrectCredentialsException) {
			return "账户或密码不正确";
		}
		if (e instanceof ExpiredCredentialsException) {
			return "密码已过期";
		}
		if (e instanceof CredentialsException) {
			return "密码验证失败";
		}
		if (e instanceof UnknownAccountException) {
			return "账户或密码不正确";
		}
		if (e instanceof LockedAccountException) {
			return "用户已被锁定";
		}
		if (e instanceof DisabledAccountException) {
			return "用户已被禁用";
		}
		if (e instanceof ExcessiveAttemptsException) {
			return "尝试次数太多";
		}
		if (e instanceof AccountException) {
			String error = e.getMessage();
			if (StringUtils.isBlank(error)) {
				error = loginFail;
			}
			return error;
		}

		logger.debug(loginFail, e);
		return loginFail;
	}
}
