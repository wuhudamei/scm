package cn.damei.scm.rest.account;

import cn.damei.scm.service.account.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.damei.scm.common.utils.WebUtils;
import cn.damei.scm.entity.account.User;

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
