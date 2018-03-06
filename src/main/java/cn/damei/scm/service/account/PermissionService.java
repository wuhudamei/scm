package cn.damei.scm.service.account;

import cn.damei.scm.entity.account.Permission;
import cn.damei.scm.common.service.CrudService;
import cn.damei.scm.repository.account.PermissionDao;

import org.springframework.stereotype.Service;

@Service
public class PermissionService extends CrudService<PermissionDao, Permission> {
}