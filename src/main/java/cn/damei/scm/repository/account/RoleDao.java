package cn.damei.scm.repository.account;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.account.Role;
import cn.damei.scm.entity.account.UserRole;

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