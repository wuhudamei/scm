package com.mdni.scm.entity.eum.dataArrangement;

/**
 * <dl>
 * <dd>Description: 美得你 材料变更详情 类型枚举 </dd>
 * <dd>@date：2017/8/5  10:33</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */

public enum MaterialChangeDetailChangeTypeEnum {
    CHANGESUBJECTMATERIAL("主材变更"),BASECHANGE("基装变更");
    private String label;
    MaterialChangeDetailChangeTypeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
