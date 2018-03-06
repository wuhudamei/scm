package cn.damei.scm.rest.account;

import cn.damei.scm.common.BaseComController;
import cn.damei.scm.entity.account.RolePermission;
import cn.damei.scm.service.account.RolePermissionService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/repository/RolePermission")
public class RolePermissionController extends BaseComController<RolePermissionService, RolePermission> {
}