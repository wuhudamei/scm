package com.damei.scm.service.account;

import com.damei.scm.common.service.CrudService;
import com.damei.scm.entity.account.UserRole;
import com.damei.scm.repository.account.UserRoleDao;

import org.springframework.stereotype.Service;

@Service
public class UserRoleService extends CrudService<UserRoleDao, UserRole> {
}