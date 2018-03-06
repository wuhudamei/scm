package cn.damei.scm.repository.account;

import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.entity.account.User;

import java.util.List;

@MyBatisRepository
public interface UserDao extends CrudDao<User> {

	User getByLoginName(String loginName);

	List<User> findUsersByIdIn(List<Long> userIds);
}