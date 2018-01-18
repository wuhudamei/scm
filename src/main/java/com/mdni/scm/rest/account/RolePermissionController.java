package com.mdni.scm.rest.account;

import com.mdni.scm.common.BaseComController;
import com.mdni.scm.entity.account.RolePermission;
import com.mdni.scm.service.account.RolePermissionService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * /*
 * /*@author weiys
 * /*@time 2016-12-13 15:21:49
 **/
@RestController
@RequestMapping(value = "/api/repository/RolePermission")
public class RolePermissionController extends BaseComController<RolePermissionService, RolePermission> {
}