package com.damei.scm.repository.account;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.account.UserRole;
@MyBatisRepository
public interface UserRoleDao extends CrudDao<UserRole> {

	void deleteUserRoleByRoleId(Long roleId);

	void deleteUserRoleByUserId(Long userId);
}