package com.damei.scm.rest.account;

import com.damei.scm.common.BaseComController;
import com.damei.scm.entity.account.RolePermission;
import com.damei.scm.service.account.RolePermissionService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/repository/RolePermission")
public class RolePermissionController extends BaseComController<RolePermissionService, RolePermission> {
}