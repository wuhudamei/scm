package com.mdni.scm.rest.account;

import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.service.account.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录。
 * <p/>
 * 系统有多个登录界面: 包括前台、后台、微信网页版、及将来可能会有的手机客户端版。
 * <p/>
 * 所有登录界面提交地址都是此接口。
 *
 * @author zhangmin
 */
@RestController
@RequestMapping("/api/login")
public class LoginRestController {

    @Autowired
    private LoginService loginService;

    @RequestMapping(method = RequestMethod.POST)
    public Object login(@RequestParam String username, @RequestParam String password,
                        @RequestParam(defaultValue = "false") boolean rememberMe, HttpServletRequest request,
                        HttpServletResponse response) {

        if (StringUtils.isAnyBlank(username, password)) {
            return StatusDto.buildFailureStatusDto("账户和密码都不能为空");
        }

        return loginService.login(username, password, rememberMe, request, response);
    }

}
