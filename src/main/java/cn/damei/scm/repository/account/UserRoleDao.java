package cn.damei.scm.repository.account;

import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.entity.account.UserRole;
@MyBatisRepository
public interface UserRoleDao extends CrudDao<UserRole> {

	void deleteUserRoleByRoleId(Long roleId);

	void deleteUserRoleByUserId(Long userId);
}