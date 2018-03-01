package com.damei.scm.entity.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.damei.scm.common.IdEntity;
import com.damei.scm.entity.eum.AccoutTypeEnum;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends IdEntity {

    public static final String ADMIN_EMBED = "admin";

    public static final String INITIAL_PASSWORD = "123456";

    private static final long serialVersionUID = -6082607548605543642L;

    private String username;
    private String orgCode;
    private String loginPwd;
    private String salt;

    private String name;

    private String mobile;

    private String position;

    private AccoutTypeEnum acctType;

    private Long supplierId;
    @Transient
    private String supplierName;

    private String storeCode;
    @Transient
    private String storeName;

    private String plainPwd;

    @Transient
    private List<Role> roles;

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