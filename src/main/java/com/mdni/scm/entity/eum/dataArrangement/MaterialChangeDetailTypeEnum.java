package com.mdni.scm.entity.eum.dataArrangement;

/**
 * <dl>
 * <dd>Description: 美得你 材料变更详情 增减项 类型枚举 </dd>
 * <dd>@date：2017/8/5  10:33</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */

public enum MaterialChangeDetailTypeEnum {
    INCREASE("增项"),MINUSITEM("减项"),COMPENSATE("赔付");
    private String label;
    MaterialChangeDetailTypeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
