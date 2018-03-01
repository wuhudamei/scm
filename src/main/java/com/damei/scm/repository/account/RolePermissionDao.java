package com.damei.scm.repository.account;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.account.Permission;
import com.damei.scm.entity.account.RolePermission;

@MyBatisRepository
public interface RolePermissionDao extends CrudDao<RolePermission> {

	void insertList(@Param("roleId") Long roleId, @Param("permIdList") List<Long> permIdList);

	void deleteByRoleId(Long roleId);

	List<Permission> findAllPermissionWithCheckedByRoleId(Long roleId);

	List<Permission> findOwnPermissionsByRoleId(Long roleId);
}