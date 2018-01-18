package com.mdni.scm.entity.dictionary;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mdni.scm.common.IdEntity;
import com.mdni.scm.common.utils.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * @describe 字典实体
 * @author Ryze
 * @date 2017-8-11 11:08:09
 *
 */
public class Dictionary  extends IdEntity {

    /**
     * 字典名称
     */
    private String dicName;

    /**
     * 字典对应的值
     */
    private String dicValue;

    /**
     * 排序
     */
    private Long sort;

    /**
     * 父id
     */
    private Long parentId;

    /**
     * 备注 
     */
    private String remarks;

    /**
     * 是否删除 0 未删除 1 删除
     */
    private String status;
    /**
     * 创建人
     */
    private String  createAccount;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = DateUtil.DATE_PATTERN)
    private Date createTime;
    /**
     * 子节点列表
     */
    private List<Dictionary> list;

    public List<Dictionary> getList() {
        return list;
    }

    public void setList(List<Dictionary> list) {
        this.list = list;
    }

    public String getCreateAccount() {
        return createAccount;
    }

    public void setCreateAccount(String createAccount) {
        this.createAccount = createAccount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    private static final long serialVersionUID = 1L;


    public String getDicName() {
        return dicName;
    }

    public void setDicName(String dicName) {
        this.dicName = dicName;
    }

    public String getDicValue() {
        return dicValue;
    }

    public void setDicValue(String dicValue) {
        this.dicValue = dicValue;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}