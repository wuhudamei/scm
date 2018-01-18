package com.mdni.scm.common.utils;


public class UserAgent {

    /**
     * 操作系统类型
     */
    private OsType osType;

    /**
     * 终端类型
     */
    private ClientType clientType;

    public OsType getOsType() {
        return osType;
    }

    public void setOsType(OsType osType) {
        this.osType = osType;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public static enum OsType {
        WINDOWS, IOS, LINUX
    }

    public static enum ClientType {
        PC, IPAD, ITOUCH, IPHONE, ANDROID, WINDOWS_PHONE
    }
}