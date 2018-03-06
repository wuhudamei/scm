package cn.damei.scm.entity.dictionary;

import com.fasterxml.jackson.annotation.JsonFormat;
import cn.damei.scm.common.IdEntity;
import cn.damei.scm.common.utils.DateUtil;

import java.util.Date;
import java.util.List;

public class Dictionary  extends IdEntity {


    private String dicName;


    private String dicValue;


    private Long sort;


    private Long parentId;


    private String remarks;


    private String status;

    private String  createAccount;

    @JsonFormat(pattern = DateUtil.DATE_PATTERN)
    private Date createTime;

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