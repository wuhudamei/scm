/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.mdni.scm.shiro;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.util.ByteSource;
import org.springside.modules.utils.Encodes;

import com.mdni.scm.entity.account.User;

public class ShiroDbRealm extends ShiroAbstractRealm {

	/**
	 * 认证回调函数,登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		User user = userService.getUserByLoginName(token.getUsername());
		if (user == null) {
			throw new AccountException("不存在会员：" + token.getUsername());
		}

		ShiroUser shiroUser = new ShiroUser(user.getId(), user.getUsername(), user.getName(), user.getAcctType(),user.getSupplierId(),user.getStoreCode());
		shiroUser.setPosition(user.getPosition());
		shiroUser.setSupplierId(user.getSupplierId());
		byte[] salt = Encodes.decodeHex(user.getSalt());
		try {
			return new SimpleAuthenticationInfo(shiroUser, user.getLoginPwd(), ByteSource.Util.bytes(salt), getName());
		} catch (RuntimeException e) {
			throw e;
		}
	}

}
