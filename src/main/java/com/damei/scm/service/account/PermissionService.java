package com.damei.scm.service.account;

import com.damei.scm.common.service.CrudService;
import com.damei.scm.entity.account.Permission;
import com.damei.scm.repository.account.PermissionDao;

import org.springframework.stereotype.Service;

@Service
public class PermissionService extends CrudService<PermissionDao, Permission> {
}