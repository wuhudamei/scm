package cn.damei.scm.service.account;

import cn.damei.scm.common.service.CrudService;
import cn.damei.scm.entity.account.UserRole;
import cn.damei.scm.repository.account.UserRoleDao;

import org.springframework.stereotype.Service;

@Service
public class UserRoleService extends CrudService<UserRoleDao, UserRole> {
}