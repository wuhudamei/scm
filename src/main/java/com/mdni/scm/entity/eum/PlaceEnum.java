package com.mdni.scm.entity.eum;

/**
 * Created by 刘铎 on 2017/7/27.
 */
public enum PlaceEnum {
    NORMAL("正常下单"),CHANGE("变更单");
    private String label;

    PlaceEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
