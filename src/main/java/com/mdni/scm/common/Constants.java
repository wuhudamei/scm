package com.mdni.scm.common;

/**
 */
public final class Constants {
	public static final String DEFAULT_ENCODING = "UTF-8";
	//数据库中加密的列 mybatis返回映射的Mapper文件时，用的列前缀
	public static final String ENCODED_FIELD_PREFIX = "encode_";
	/**
	 * wx 接口响应状态码
	 */
	public static final String RESP_STATUS_CODE_SUCCESS = "1";
	public static final String RESP_STATUS_CODE_FAIL = "0";
	public static final String RESP_STATUS_CODE_ERROR = "2";
	public static final String RESP_STATUS_CODE_KEY = "code";
	public static final String RESP_DATA_KEY = "data";
	public static final String MESSAGE = "message";
	public static final String ERROR = "error";
	public static final String LOGINURL = "loginUrl";
	public static final String PAGE_OFFSET = "offset";
	public static final String PAGE_SIZE = "pageSize";
	public static final String PAGE_SORT = "sort";
	public static final String DEFAULT_PAGE_SIZE_STR = "50";
	public static final int MAX_PAGE_SIZE = 200;
	public static final int SECONDS_REMEMBER_LOGIN_TRIES = 3600;
	/**
	 * 页面推荐显示个数
	 */
	public static final int RECOMMEND_LIST_SIZE = 12;
	/**
	 * 后台管理系统,分页查询中，1页默认显示的记录条数
	 */
	public static final String DEFAULT_PAGE_SIZE = "5";
	/**
	 * 页面按钮间隔时间
	 */
	public static final int INTERVAL_TIME = 2;
	public static final int WAP_INTERVAL_TIME = 10;
	// =====   session scope  attribute key    ======
	public static final String SESSION_ATTR_USER_KEY = "sessionUser";
	public static final String SESSION_ATTR_VERIFY_CODE_KEY = "sessionVerifyCode";
	//短信验证码
	public static final String REGISTER_VCODE_SMS_TIMESTAMP = "registerSmsCodeTimestamp";
	public static final String REGISTER_VCODE_MOBILE_PHONE = "registerVcodeMobilePhone";
	public static final String REGISTER_VCODE = "vcode";
	public static final String REGISTER_WRONGCOUNT = "codeWrongCount";
	public static final String ACCOUNT_MOBILE_PHONE = "accountMobilePhone";
	//token 防止重复提交
	public static final String TRANSACTION_TOKEN_KEY = "transactionToken";
	public static final String TOKEN_KEY = "htmlformToken";
	public static int MAX_LOGIN_TRIES_IP = 60;
	public static int MAX_LOGIN_TRIES_LOGINNAME = 6;
	/** 调用外系统时传入的系统名称 */
	public static final String SYSTEM_NAME = "SCM";
	/**
	 * 订货单业务编号
	 */
	public static final String ORDER_CODE = "OC";


	/** 认证中心修改密码url**/
	public static final String OAUTH_PASSWORD_URL = "/oauth/password";

	//预备单数据来源 选材
	public static final String DATA_SOURCE_SELECT = "select";
	//预备单数据来源 变更
	public static final String DATA_SOURCE_CHANGE = "change";

	private Constants() {
	}

}
