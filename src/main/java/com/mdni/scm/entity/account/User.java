package com.mdni.scm.entity.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mdni.scm.common.IdEntity;
import com.mdni.scm.entity.eum.AccoutTypeEnum;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * <dl>
 * <dd>描述: 用户</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月8日 下午4:36:16</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends IdEntity {

    //系统内置管理员
    public static final String ADMIN_EMBED = "admin";

    //用户初始密码
    public static final String INITIAL_PASSWORD = "123456";

    private static final long serialVersionUID = -6082607548605543642L;

    //用户名
    private String username;
    //机构编码
    private String orgCode;
    //加密后密码
    private String loginPwd;
    private String salt;

    //名字
    private String name;

    private String mobile;

    //岗位
    private String position;

    //账户类型
    private AccoutTypeEnum acctType;

    //区域供应商id 或  商品供货商id
    private Long supplierId;
    //供应商名称
    @Transient
    private String supplierName;

    //门店id
    private String storeCode;
    //门店名称
    @Transient
    private String storeName;

    //原密码/明文密码
    //	@Transient
    private String plainPwd;

    @Transient
    private List<Role> roles; // 有序的关联对象集合

    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public AccoutTypeEnum getAcctType() {
        return acctType;
    }

    public void setAcctType(AccoutTypeEnum acctType) {
        this.acctType = acctType;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getPlainPwd() {
        return plainPwd;
    }

    public void setPlainPwd(String plainPwd) {
        this.plainPwd = plainPwd;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getStoreName() {return storeName;}

    public void setStoreName(String storeName) { this.storeName = storeName;}

    //是否为内置管理员
    public boolean isAdmin() {
        return ADMIN_EMBED.equals(this.getUsername());
    }

    @JsonIgnore
    public List<String> getRoleNameList() {
        if (CollectionUtils.isEmpty(roles)) {
            return Collections.emptyList();
        }

        ArrayList<String> roleNameList = new ArrayList<String>();
        for (Role role : roles) {
            roleNameList.add(role.getName());
        }
        return roleNameList;
    }

    @JsonIgnore
    public LinkedHashSet<String> getPermissions() {
        LinkedHashSet<String> permissions = new LinkedHashSet<String>();
        if (roles != null) {
            for (Role role : roles) {
                List<Permission> permissionList = role.getPermissionList();
                if (permissionList != null) {
                    for (Permission perm : permissionList) {
                        permissions.add(perm.getPermission());
                    }
                }
            }
        }
        return permissions;
    }
}