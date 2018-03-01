package com.damei.scm.rest.account;

import com.damei.scm.common.BaseComController;
import com.damei.scm.entity.account.Permission;
import com.damei.scm.service.account.PermissionService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/repository/Permission")
public class PermissionController extends BaseComController<PermissionService, Permission> {
}