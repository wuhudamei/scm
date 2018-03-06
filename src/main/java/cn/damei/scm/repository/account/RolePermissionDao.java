package cn.damei.scm.repository.account;

import java.util.List;

import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.account.Permission;
import org.apache.ibatis.annotations.Param;

import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.entity.account.RolePermission;

@MyBatisRepository
public interface RolePermissionDao extends CrudDao<RolePermission> {

	void insertList(@Param("roleId") Long roleId, @Param("permIdList") List<Long> permIdList);

	void deleteByRoleId(Long roleId);

	List<Permission> findAllPermissionWithCheckedByRoleId(Long roleId);

	List<Permission> findOwnPermissionsByRoleId(Long roleId);
}