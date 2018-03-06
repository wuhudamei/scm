package cn.damei.scm.service.account;

import cn.damei.scm.entity.account.Permission;
import cn.damei.scm.repository.account.RoleDao;
import cn.damei.scm.common.service.CrudService;
import cn.damei.scm.entity.account.Role;
import cn.damei.scm.repository.account.RolePermissionDao;
import cn.damei.scm.repository.account.UserDao;
import cn.damei.scm.repository.account.UserRoleDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService extends CrudService<RoleDao, Role> {

	@Autowired
	private UserDao userDao;
	@Autowired
	private RolePermissionDao rolePermissionDao;
	@Autowired
	private UserRoleDao userRoleDao;

	public List<Role> findAllRoleWithCheckedByUserId(final Long userId) {
		return entityDao.findAllRoleWithCheckedByUserId(userId);
	}

	public void insertUserRoles(final Long userId, final List<Long> roleIdList) {
		if (userId == null) {
			return;
		}

		entityDao.deleteByUserId(userId);

		if (CollectionUtils.isNotEmpty(roleIdList)) {
			entityDao.batchInsertUserRoleList(userId, roleIdList);
		}
	}

	public void insertRolePermission(final Long roleId, final List<Long> permIdList) {
		if (roleId == null || roleId < 1)
			return;

		rolePermissionDao.deleteByRoleId(roleId);

		if (CollectionUtils.isNotEmpty(permIdList)) {
			rolePermissionDao.insertList(roleId, permIdList);
		}
	}

	public boolean isRoleNameExist(Role role) {
		if (role == null) {
			return false;
		}

		return this.entityDao.checkExistRoleName(role) != null;
	}

	public void saveOrUpdate(Role role) {
		if (role.getId() != null) {
			entityDao.update(role);
		} else {
			entityDao.insert(role);
		}
	}

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

	public List<Permission> findAllPermissionWithCheckedByRoleId(Long roleId) {
		return rolePermissionDao.findAllPermissionWithCheckedByRoleId(roleId);
	}

}