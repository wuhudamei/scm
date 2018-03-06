package cn.damei.scm.rest.account;

import cn.damei.scm.common.BaseComController;
import cn.damei.scm.entity.account.Permission;
import cn.damei.scm.service.account.PermissionService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/repository/Permission")
public class PermissionController extends BaseComController<PermissionService, Permission> {
}