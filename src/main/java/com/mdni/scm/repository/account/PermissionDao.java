package com.mdni.scm.repository.account;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.account.Permission;

/**
 * 
 * <dl>
 * <dd>描述:权限Dao</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月8日 下午5:16:48</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@MyBatisRepository
public interface PermissionDao extends CrudDao<Permission> {
}