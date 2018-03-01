package com.damei.scm.repository.account;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.account.Role;
import com.damei.scm.entity.account.UserRole;

@MyBatisRepository
public interface RoleDao extends CrudDao<Role> {

	Role checkExistRoleName(Role role);

	Role getByName(String name);

	List<Role> findRolesByUserId(Long userId);

	List<Role> findAllRoleWithCheckedByUserId(Long userId);

	void deleteByUserId(Long userId);

	void insertUserRole(UserRole userRole);

	void batchInsertUserRoleList(@Param("userId") Long userId, @Param("roleIdList") List<Long> roleIdList);
}