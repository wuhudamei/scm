package cn.damei.scm.repository.account;

import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.entity.account.Permission;

@MyBatisRepository
public interface PermissionDao extends CrudDao<Permission> {
}