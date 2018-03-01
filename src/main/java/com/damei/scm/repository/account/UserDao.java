package com.damei.scm.repository.account;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.account.User;

import java.util.List;

@MyBatisRepository
public interface UserDao extends CrudDao<User> {

	User getByLoginName(String loginName);

	List<User> findUsersByIdIn(List<Long> userIds);
}