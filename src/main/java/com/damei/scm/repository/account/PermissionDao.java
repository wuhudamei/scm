package com.damei.scm.repository.account;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.account.Permission;

@MyBatisRepository
public interface PermissionDao extends CrudDao<Permission> {
}