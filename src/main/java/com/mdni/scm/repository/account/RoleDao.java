package com.mdni.scm.repository.account;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.account.Role;
import com.mdni.scm.entity.account.UserRole;

/**
 * 
 * <dl>
 * <dd>描述: 角色Dao</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月8日 下午5:20:28</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@MyBatisRepository
public interface RoleDao extends CrudDao<Role> {

	/**
	 * 校验角色名称是否存在
	 * 
	 * @param role 校验的角色对象
	 * @return 如果角色已存在则返回此角色对象，不存在返回null
	 */
	Role checkExistRoleName(Role role);

	Role getByName(String name);

	//返回用户拥有的角色列表
	List<Role> findRolesByUserId(Long userId);

	//返回所有的角色列表,如果用户含有此角色,则是勾选状态
	List<Role> findAllRoleWithCheckedByUserId(Long userId);

	void deleteByUserId(Long userId);

	void insertUserRole(UserRole userRole);

	void batchInsertUserRoleList(@Param("userId") Long userId, @Param("roleIdList") List<Long> roleIdList);
}