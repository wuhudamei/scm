package com.mdni.scm.rest.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.entity.account.User;
import com.mdni.scm.service.account.UserService;

/**
 * @author liuwei
 */
@RestController
@RequestMapping(value = "/api/logged-user")
public class LoggedUserRestController {

	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.GET)
	public Object get() {
		if (WebUtils.isLogged()) {
			User user = userService.getById(WebUtils.getLoggedUserId());
			return user;
		} else {
			return WebUtils.response400("您尚未登录");
		}
	}

}
