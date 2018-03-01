package com.damei.scm.redis;

public class CacheKeys {
    public static final String DEFAULT_NAME = "cache";

    public static final String LOGIN_TRIES_IP_PREFIX = "cache.login.tries.ip:";
    public static final String LOGIN_TRIES_LOGINNAME_PREFIX = "cache.login.tries.loginname:";

    public static final String USER_KEY_PREV = "'cache.user.'+";
    public static final String BENCHMARK_ALL_KEY = "'benchmark.all'";
    public static final String FUND_COMPANY_ALL_KEY = "'fundCompany.all'";
    public static final String FUND_COMPANY_KEY_PREV = "'fundCompany.'+";

    public static final String FUND_MANAGER_ALL_KEY = "'fundManger.all'";
    public static final String FUND_MANAGER_KEY_PREV = "'fundManger.'+";

    public static final String CONFIG = "'config'";

}
