package com.mdni.scm.repository.account;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.account.User;

import java.util.List;

/**
 * 
 * <dl>
 * <dd>描述: 用户表</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月9日 上午10:50:15</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@MyBatisRepository
public interface UserDao extends CrudDao<User> {

	User getByLoginName(String loginName);

	List<User> findUsersByIdIn(List<Long> userIds);
}