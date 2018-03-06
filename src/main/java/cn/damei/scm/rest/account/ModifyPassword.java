package cn.damei.scm.rest.account;


import cn.damei.scm.common.BaseController;
import cn.damei.scm.common.Constants;
import cn.damei.scm.common.PropertyHolder;
import cn.damei.scm.common.dto.StatusDto;
import cn.damei.scm.common.utils.WebUtils;
import cn.damei.scm.shiro.ShiroUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/modify")
public class ModifyPassword extends BaseController {
    private static final String JOB_NUM = "jobNum";
    private static final String REDIRECT_URL = "redirectUrl";
    @Autowired
    private  LogoutRestController logoutRestController;
    @RequestMapping(method = RequestMethod.GET, value = "password")
    public Object modifyPassword(HttpServletRequest request) {
        String hostName = getHostName(request);
        ShiroUser loggedUser = WebUtils.getLoggedUser();
        if (loggedUser != null) {
            String callBack = hostName + "/logout/admin";
            String updateUrl = PropertyHolder.getOauthCenterDomain() + Constants.OAUTH_PASSWORD_URL + "?" + JOB_NUM + "=" + loggedUser.getLoginName() + "&" +
                    REDIRECT_URL + "=" + callBack;
            return new ModelAndView("redirect:" + updateUrl);
        }else{
            return StatusDto.buildFailureStatusDto("用户未登录");
        }
    }



    private String getHostName(HttpServletRequest request){
        StringBuffer url = request.getRequestURL();
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append(request.getServletContext().getContextPath()).toString();
            return tempContextUrl;
    }

}
