package cn.damei.scm.common;


public final class Constants {
	public static final String DEFAULT_ENCODING = "UTF-8";
	public static final String ENCODED_FIELD_PREFIX = "encode_";
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
	public static final int RECOMMEND_LIST_SIZE = 12;
	public static final String DEFAULT_PAGE_SIZE = "5";
	public static final int INTERVAL_TIME = 2;
	public static final int WAP_INTERVAL_TIME = 10;
	public static final String SESSION_ATTR_USER_KEY = "sessionUser";
	public static final String SESSION_ATTR_VERIFY_CODE_KEY = "sessionVerifyCode";
	public static final String REGISTER_VCODE_SMS_TIMESTAMP = "registerSmsCodeTimestamp";
	public static final String REGISTER_VCODE_MOBILE_PHONE = "registerVcodeMobilePhone";
	public static final String REGISTER_VCODE = "vcode";
	public static final String REGISTER_WRONGCOUNT = "codeWrongCount";
	public static final String ACCOUNT_MOBILE_PHONE = "accountMobilePhone";
	public static final String TRANSACTION_TOKEN_KEY = "transactionToken";
	public static final String TOKEN_KEY = "htmlformToken";
	public static int MAX_LOGIN_TRIES_IP = 60;
	public static int MAX_LOGIN_TRIES_LOGINNAME = 6;
	public static final String SYSTEM_NAME = "SCM";
	public static final String ORDER_CODE = "OC";


	public static final String OAUTH_PASSWORD_URL = "/oauth/password";

	public static final String DATA_SOURCE_SELECT = "select";
	public static final String DATA_SOURCE_CHANGE = "change";

	private Constants() {
	}

}
