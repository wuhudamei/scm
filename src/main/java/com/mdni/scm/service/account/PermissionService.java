package com.mdni.scm.service.account;

import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.entity.account.Permission;
import com.mdni.scm.repository.account.PermissionDao;

import org.springframework.stereotype.Service;

/**
 * /*
 * /*@author weiys
 * /*@time 2016-12-13 15:21:49
 **/
@Service
public class PermissionService extends CrudService<PermissionDao, Permission> {
}