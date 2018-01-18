package com.mdni.scm.service.account;

import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.entity.account.Permission;
import com.mdni.scm.entity.account.Role;
import com.mdni.scm.repository.account.RoleDao;
import com.mdni.scm.repository.account.RolePermissionDao;
import com.mdni.scm.repository.account.UserDao;
import com.mdni.scm.repository.account.UserRoleDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * <dl>
 * <dd>描述: 角色Service</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月8日 下午5:48:02</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@Service
public class RoleService extends CrudService<RoleDao, Role> {

	@Autowired
	private UserDao userDao;
	@Autowired
	private RolePermissionDao rolePermissionDao;
	@Autowired
	private UserRoleDao userRoleDao;

	/**
	 * 返回所有的角色，如果用户userId拥有此角色 则是勾选状态
	 */
	public List<Role> findAllRoleWithCheckedByUserId(final Long userId) {
		return entityDao.findAllRoleWithCheckedByUserId(userId);
	}

	/**
	 * 为用户设置角色
	 * 
	 * @param userId 用户Id
	 * @param roleIdList 用户拥有的角色Id列表
	 */
	public void insertUserRoles(final Long userId, final List<Long> roleIdList) {
		if (userId == null) {
			return;
		}

		entityDao.deleteByUserId(userId);

		if (CollectionUtils.isNotEmpty(roleIdList)) {
			entityDao.batchInsertUserRoleList(userId, roleIdList);
		}
	}

	/**
	 * 为角色设置权限
	 * 
	 * @param roleId 角色Id
	 * @param permIdList 角色拥有的权限列表
	 */
	public void insertRolePermission(final Long roleId, final List<Long> permIdList) {
		if (roleId == null || roleId < 1)
			return;

		rolePermissionDao.deleteByRoleId(roleId);

		if (CollectionUtils.isNotEmpty(permIdList)) {
			rolePermissionDao.insertList(roleId, permIdList);
		}
	}

	/**
	 * 校验角色名称是否存在
	 * 
	 * @param role 校验的角色对象
	 * @return 如果角色已存在则true，不存在返回false
	 */
	public boolean isRoleNameExist(Role role) {
		if (role == null) {
			return false;
		}

		return this.entityDao.checkExistRoleName(role) != null;
	}

	/**
	 * 创建或更新角色
	 */
	public void saveOrUpdate(Role role) {
		if (role.getId() != null) {
			entityDao.update(role);
		} else {
			entityDao.insert(role);
		}
	}

	/**
	 * 删除角色
	 * 
	 * @param roleId 角色Id
	 */
	@Override
	@Transactional
	public void deleteById(Long roleId) {
		if (roleId == null) {
			return;
		}

		entityDao.deleteById(roleId);
		userRoleDao.deleteUserRoleByRoleId(roleId);
		rolePermissionDao.deleteByRoleId(roleId);
	}

	/**
	 * 查找角色绑定的权限及未绑定权限
	 */
	public List<Permission> findAllPermissionWithCheckedByRoleId(Long roleId) {
		return rolePermissionDao.findAllPermissionWithCheckedByRoleId(roleId);
	}

}