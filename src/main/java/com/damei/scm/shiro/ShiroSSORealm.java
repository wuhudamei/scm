package com.damei.scm.shiro;

import com.damei.scm.common.dto.OauthAccessTokenUser;
import com.damei.scm.entity.account.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShiroSSORealm extends ShiroAbstractRealm {

	private final Logger logger = LoggerFactory.getLogger(ShiroSSORealm.class);

	public ShiroSSORealm() {
		setCredentialsMatcher(new CredentialsMatcher() {
			@Override
			public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
				return info != null && token instanceof SSOToken;
			}
		});
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		try {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			SSOShiroUser shiroUser = principals.oneByType(SSOShiroUser.class);
			if (shiroUser != null) {
				info.addRoles(shiroUser.getRoleNameList());
				info.addStringPermissions(shiroUser.getPermissionList());
			}
			return info;
		} catch (RuntimeException e) {
			logger.warn("sso授权时发生异常", e);
			throw e;
		}
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		SSOToken ssoToken = (SSOToken) token;
		try {
			User tokenUser = ssoToken.getTokenUser().getUser();
			User user = userService.getUserByLoginName(tokenUser.getOrgCode());
			SSOShiroUser ssoUser = new SSOShiroUser(tokenUser.getId(), tokenUser.getOrgCode(), tokenUser.getName(),
					user != null ? user.getAcctType() : null,user != null ? user.getSupplierId() : null,
					user != null ? user.getStoreCode() : null );
			ssoUser.setPermissionList(ssoToken.getTokenUser().getPermissionList());
			ssoUser.setRoleNameList(ssoToken.getTokenUser().getRoleNameList());
			return new SimpleAuthenticationInfo(ssoUser, null, getName());
		} catch (RuntimeException e) {
			logger.warn("SSO登录时发生异常", e);
			return null;
		}
	}

	public static class SSOToken implements RememberMeAuthenticationToken {
		private static final long serialVersionUID = 150839122060394008L;
		private OauthAccessTokenUser tokenUser;

		public SSOToken(OauthAccessTokenUser tokenUser) {
			this.tokenUser = tokenUser;
		}

		public OauthAccessTokenUser getTokenUser() {
			return tokenUser;
		}

		@Override
		public Object getPrincipal() {
			return null;
		}

		@Override
		public Object getCredentials() {
			return null;
		}

		@Override
		public boolean isRememberMe() {
			return false;
		}
	}

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof SSOToken;
	}
}
