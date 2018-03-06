package cn.damei.scm.entity.eum;

public enum OrderStatusEnum {

	DRAFT("备货单"), NOTIFIED("订货单"), INVALID("已作废"),REJECT("已驳回"),
	REJECTINSTALL("驳回安装"),ALREADY_INSTALLED("已安装"),SETTLEACCOUNTS("已结算"),
	INSTALLEND_WAITCHECK("安装完成，待审核"),INSTALLCHECKNOTPASS("安装审核未通过"),
	NOTRECONCILED("未对账"),
	HASBEENRECONCILED("已对账"),
	PARTIALRECONCILIATION("部分对账"),
	INSTALLCHECKPASS("安装审核已通过");

	private String label;

	OrderStatusEnum(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
