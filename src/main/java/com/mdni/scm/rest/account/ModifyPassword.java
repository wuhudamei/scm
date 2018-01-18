package com.mdni.scm.rest.account;


import com.mdni.scm.common.BaseController;
import com.mdni.scm.common.Constants;
import com.mdni.scm.common.PropertyHolder;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.shiro.ShiroUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * <dl>
 * <dd>Description: 美得你scm 修改密码</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/7/31</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
@RestController
@RequestMapping("api/modify")
public class ModifyPassword extends BaseController {
    private static final String JOB_NUM = "jobNum";
    private static final String REDIRECT_URL = "redirectUrl";
    @Autowired
    private  LogoutRestController logoutRestController;
    /**
     * 修改密码
     * @param request
     * @return
     */
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



    /**
     * //获取 域名
     * @param request
     * @return
     */
    private String getHostName(HttpServletRequest request){
        //获取 域名
        StringBuffer url = request.getRequestURL();
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append(request.getServletContext().getContextPath()).toString();
            return tempContextUrl;
    }

}
