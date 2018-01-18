package com.mdni.scm.repository.account;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.account.Permission;
import com.mdni.scm.entity.account.RolePermission;

/**
 * <dl>
 * <dd>描述:角色权限</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月9日 上午10:56:19</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@MyBatisRepository
public interface RolePermissionDao extends CrudDao<RolePermission> {

	void insertList(@Param("roleId") Long roleId, @Param("permIdList") List<Long> permIdList);

	void deleteByRoleId(Long roleId);

	/**
	 * 返回所有的权限,如果roleId拥有此权限，则是被勾选状态
	 */
	List<Permission> findAllPermissionWithCheckedByRoleId(Long roleId);

	/**
	 * @param roleId 角色Id
	 * @return 返回角色拥有的权限列表
	 */
	List<Permission> findOwnPermissionsByRoleId(Long roleId);
}