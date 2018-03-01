package com.damei.scm.entity.eum;

public enum ReviewSizeNoticeEnum {
    NORIVEEWSIZE("未复尺"), YESRIVEEWSIZE("已复尺"),REJECT("已驳回");
    private String label;

    ReviewSizeNoticeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
