package cn.damei.scm.service.account;

import com.google.common.collect.Maps;
import cn.damei.scm.common.Constants;
import cn.damei.scm.common.PropertyHolder;
import cn.damei.scm.common.dto.StatusDto;
import cn.damei.scm.common.service.CrudService;
import cn.damei.scm.common.utils.HttpUtils;
import cn.damei.scm.common.utils.JsonDealUtils;
import cn.damei.scm.common.utils.JsonUtils;
import cn.damei.scm.entity.account.Role;
import cn.damei.scm.entity.account.User;
import cn.damei.scm.entity.eum.AccoutTypeEnum;
import cn.damei.scm.entity.eum.StatusEnum;
import cn.damei.scm.repository.account.RoleDao;
import cn.damei.scm.repository.account.RolePermissionDao;
import cn.damei.scm.repository.account.UserDao;
import cn.damei.scm.repository.account.UserRoleDao;
import cn.damei.scm.shiro.PasswordUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.Collections3;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends CrudService<UserDao, User> {

	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private RolePermissionDao rolePermDao;
	@Autowired
	private UserRoleDao userRoleDao;

	private static Logger logger = LoggerFactory.getLogger(UserService.class);// 日志

	public List<User> findAll() {
		return userDao.findAll();
	}

	public boolean isLoginPasswordCorrect(final String loginName, final String loginPassword) {
		User user = getUserByLoginName(loginName);
		if (user == null || user.getLoginPwd() == null || loginPassword == null) {
			return false;
		}
		return PasswordUtil.hashPassword(loginPassword, PasswordUtil.generateSalt()).equals(user.getLoginPwd());
	}

	public boolean isUsernameRegisted(String loginName) {
		return getUserByLoginName(loginName) != null;
	}

	public void createSaveSupplierAccount(Long supplierId, String supplierName, AccoutTypeEnum acctType, String phone) {
		Map<String, String> postParamMap = Maps.newHashMap();
		StringBuffer usernameBuf = new StringBuffer();
		String suffix = StringUtils.leftPad(supplierId.toString(), 5, "0");
		String createRespResult = "";
		if (AccoutTypeEnum.REGION_SUPPLIER.equals(acctType)) {
			usernameBuf.append("region").append(suffix);
		} else if (AccoutTypeEnum.STORE.equals(acctType)) {
			usernameBuf.append("store").append(suffix);
		} else {
			usernameBuf.append("supplier").append(suffix);
		}

		postParamMap.put("account", usernameBuf.toString());
		postParamMap.put("name", supplierName);

		if (StringUtils.isNotBlank(phone)) {
			postParamMap.put("mobile", phone);
		} else {
			postParamMap.put("mobile", User.INITIAL_PASSWORD);
		}
		postParamMap.put("source", Constants.SYSTEM_NAME);

		String createParam = JsonUtils.pojoToJson(postParamMap);
		logger.info("调用认证中心创建接口，传输参数：" + createParam);
		try {
			createRespResult = HttpUtils.postJson(PropertyHolder.getUcenterCreateAccountUrl(), createParam);
			logger.info("调用认证中心创建接口，返回结果：" + createRespResult);
		} catch (Exception e) {
			logger.error("调用认证中心创建接口出现异常，返回结果：" + createRespResult + "异常信息" + e.getMessage());
		}

	}

	@Transactional
	@Override
	public void deleteById(Long userId) {
		if (userId == null || userId < 1)
			return;

		this.entityDao.deleteById(userId);
		this.userRoleDao.deleteUserRoleByUserId(userId);
	}

	public User getUserByLoginName(String loginName) {
		if (StringUtils.isEmpty(loginName)) {
			return null;
		}

		return userDao.getByLoginName(loginName);
	}

	public Map<Long, User> findAllIdUserMap() {
		List<User> users = userDao.findAll();
		return convertListToMap(users);
	}

	@SuppressWarnings("unchecked")
	public Map<Long, User> findIdUserMapByIdIn(List<Long> userIdList) {
		if (Collections3.isEmpty(userIdList)) {
			return MapUtils.EMPTY_MAP;
		}

		return convertListToMap(this.findUsersByIdIn(userIdList));
	}

	public void resetPassword(final Long userId) {
		if (userId == null || userId < 1) {
			return;
		}
		User user = userDao.getById(userId);
		if (user == null || user.isAdmin()) {
			return;
		}
		User usr = new User();
		usr.setId(userId);
		usr.setPlainPwd(User.INITIAL_PASSWORD);
		PasswordUtil.entryptPassword(usr);
		userDao.update(usr);
	}

	public Object updateLoginPassword(final String username, final String plainPwd, final String newPlainPwd) {
		User user = getUserByLoginName(username);
		try {
			if (user.getLoginPwd().equals(PasswordUtil.hashPassword(plainPwd, user.getSalt()))) {
				user.setPlainPwd(newPlainPwd);
				PasswordUtil.entryptPassword(user);
				this.userDao.update(user);
				return StatusDto.buildSuccessStatusDto("修改密码成功！");
			} else {
				return StatusDto.buildFailureStatusDto("原密码输入错误！");
			}
		} catch (Exception e) {
			return StatusDto.buildFailureStatusDto("修改密码失败！");
		}
	}

	public void switchUser(Long id, StatusEnum newStatus) {
		User user = new User();
		user.setId(id);
		this.userDao.update(user);
	}

	public void saveOrUpdateUser(User user) {
		if (AccoutTypeEnum.ADMIN.equals(user.getAcctType())) {
			user.setSupplierId(null);
		}

		if (user.getId() != null) {
			this.userDao.update(user);
		} else {
			this.userDao.insert(user);
		}
	}

	public boolean initUserAccount(){
        boolean resultFlag = false;
		try {
		    //首先调用AppToken接口，获取AppToken
            NameValuePair appid = new BasicNameValuePair("appid", PropertyHolder.getOauthCenterAppid());
            NameValuePair secret = new BasicNameValuePair("secret", PropertyHolder.getOauthCenterSecret());

            String appTokenRespResult = HttpUtils.post(PropertyHolder.getOAuthAppTokenUrl(), appid,secret);
			logger.info("调用认证中心appToken接口，返回结果：" + appTokenRespResult);
            Map<String, Object>  appTokenResultMap = JsonDealUtils.fromJsonAsMap(appTokenRespResult,String.class,Object.class);
            Map<String,String> appTokenData = (Map<String,String>)appTokenResultMap.get("data");
            NameValuePair accessToken = new BasicNameValuePair("accessToken", appTokenData.get("access_token"));
            //请求用户账号接口
            String appUserRespResult = HttpUtils.post(PropertyHolder.getOAuthAppUserUrl(), appid,accessToken);
            logger.info("调用认证中心appUser接口，返回结果：" + appUserRespResult);
            Map<String, Object> appUserResultMap = JsonDealUtils.fromJsonAsMap(appUserRespResult,String.class,Object.class);
            Map<String,Object> appUserData = (Map<String,Object>)appUserResultMap.get("data");
            List<Map<String,String>> accountList = (List<Map<String,String>>)appUserData.get("users");
            if( accountList != null && accountList.size() >0 ){

                //先查询系统中除管理员外的其他人员
                List<User> userExcludeAdminList = this.userDao.findAll();
                Map<String,User> userExcludeAdminMap = Maps.newHashMap();
                if( userExcludeAdminList != null && userExcludeAdminList.size() > 0 ){
                    for(User user : userExcludeAdminList){
                        userExcludeAdminMap.put(user.getUsername(),user);
                    }
                }
                for(Map<String,String> map: accountList){
                    if (userExcludeAdminMap.get( map.get("username") ) == null) {
                        User user = new User();
                        user.setName(map.get("name"));
                        user.setUsername(map.get("username"));
                        if( StringUtils.isNotBlank( map.get("mobile") ) ){
                            user.setMobile(map.get("mobile"));
                        }
                        userDao.insert(user);
                    }else{
                        userExcludeAdminMap.remove(map.get("username") );
                    }
                }

                //遍历删除剩余的原系统人员
                for(User user : userExcludeAdminMap.values()){
                    this.userDao.deleteById(user.getId());
                }
            }
            resultFlag = true;
		} catch (Exception e) {
			logger.error("调用认证中心接口出现异常，异常信息" + e.getMessage());
		}
		return resultFlag;
	}

	public User getUserWithDetailById(Long userId) {
		User usr = getById(userId);
		if (usr != null) {
			buildRolePermissionDetail(usr);
		}
		return usr;
	}

	private void buildRolePermissionDetail(User user) {
		List<Role> roleList = roleDao.findRolesByUserId(user.getId());
		user.setRoles(roleList);

		if (CollectionUtils.isNotEmpty(roleList)) {
			for (Role role : roleList) {
				role.setPermissionList(rolePermDao.findOwnPermissionsByRoleId(role.getId()));
			}
		}

	}

	public List<User> findUsersByIdIn(List<Long> userIds) {
		if (CollectionUtils.isEmpty(userIds)) {
			return Collections.emptyList();
		}

		return this.userDao.findUsersByIdIn(userIds);
	}

	private Map<Long, User> convertListToMap(List<User> userList) {
		Map<Long, User> userMap = Maps.newHashMap();
		if (Collections3.isNotEmpty(userList)) {
			for (User u : userList) {
				userMap.put(u.getId(), u);
			}
		}
		return userMap;
	}

}