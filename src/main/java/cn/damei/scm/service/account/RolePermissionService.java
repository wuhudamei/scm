package cn.damei.scm.service.account;

import cn.damei.scm.common.service.CrudService;
import cn.damei.scm.entity.account.RolePermission;
import cn.damei.scm.repository.account.RolePermissionDao;

import org.springframework.stereotype.Service;

@Service
public class RolePermissionService extends CrudService<RolePermissionDao, RolePermission> {
}