package com.mdni.scm.repository.account;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.account.UserRole;

/**
 * @author weiys
 **/
@MyBatisRepository
public interface UserRoleDao extends CrudDao<UserRole> {

	//删除所有拥有此角色的 userRole
	void deleteUserRoleByRoleId(Long roleId);

	//删除用户拥有的角色
	void deleteUserRoleByUserId(Long userId);
}