package com.damei.scm.service.account;

import com.damei.scm.common.service.CrudService;
import com.damei.scm.entity.account.RolePermission;
import com.damei.scm.repository.account.RolePermissionDao;

import org.springframework.stereotype.Service;

@Service
public class RolePermissionService extends CrudService<RolePermissionDao, RolePermission> {
}