package cn.damei.scm.common.utils;

import cn.damei.scm.common.Constants;
import cn.damei.scm.common.PropertyHolder;
import cn.damei.scm.entity.account.User;
import cn.damei.scm.entity.eum.AccoutTypeEnum;
import cn.damei.scm.entity.prod.RegionSupplier;
import cn.damei.scm.entity.prod.Supplier;
import cn.damei.scm.service.account.UserService;
import cn.damei.scm.service.prod.RegionSupplierService;
import cn.damei.scm.service.prod.SupplierService;
import cn.damei.scm.shiro.ShiroUser;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springside.modules.utils.Collections3;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangmin
 * @author liuwei
 */
@SuppressWarnings("all")
public class WebUtils {

    public static ResponseEntity RESPONSE_200 = new ResponseEntity(HttpStatus.OK);

    private WebUtils() {
    }

    /**
     * 返回站点访问Base路径
     *
     * @return http://localhost:8080/ctxPath
     */
    public static String getBaseSiteUrl(HttpServletRequest request) {
        final StringBuilder basePath = new StringBuilder();
        basePath.append(request.getScheme()).append("://").append(request.getServerName());
        if (request.getServerPort() != 80) {
            basePath.append(":").append(request.getServerPort());
        }
        basePath.append(request.getContextPath());
        return basePath.toString();
    }

    public static ShiroUser getLoggedUser() {
        try {
            UserService userService = PropertyHolder.appCtx.getBean(UserService.class);
            ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
            User user = userService.getUserByLoginName(shiroUser.getLoginName());
            if (user != null) {
                shiroUser.setId(user.getId());
                shiroUser.setAcctType(user.getAcctType());
                shiroUser.setSupplierId(user.getSupplierId());
                shiroUser.setStoreCode(user.getStoreCode());
            }
            return shiroUser;
        } catch (UnavailableSecurityManagerException ex) {
            return null;
        }
    }

    /**
     * 获得当前登录登录账户能管理的供应商Id列表 <br/>
     * (1)返回null,表示是管理员账户,能管理所有的供应商 <br/>
     * (2)返回[]空列表,则表示一个供应商都不能管理 <br/>
     * (3)如果没用登录,也返回[]空列表
     *
     * @return
     */
    public static List<Long> getManagedSupplierIdsOfLoginUser() {
        ShiroUser shiroUser = WebUtils.getLoggedUser();
        if (shiroUser == null) {
            //没有登录
            return Collections.emptyList();
        }

        if (AccoutTypeEnum.ADMIN.equals(shiroUser.getAcctType())) {
            return null;
        }

//        if (!AccoutTypeEnum.MATERIAL_CLERK.equals(shiroUser.getAcctType())) {
//            if (shiroUser.getSupplierId() == null) {
//                //非管理员 没有设置供应商
//                return Collections.emptyList();
//            }
//        }
        if (AccoutTypeEnum.STORE.equals(shiroUser.getAcctType()) || AccoutTypeEnum.MATERIAL_CLERK.equals(shiroUser.getAcctType()) || AccoutTypeEnum.MATERIAL_MANAGER.equals(shiroUser.getAcctType())) {
            RegionSupplierService regionSupplierService = PropertyHolder.appCtx.getBean(RegionSupplierService.class);
            SupplierService supplierService = PropertyHolder.appCtx.getBean(SupplierService.class);

            //门店下所有的区域
            List<RegionSupplier> regionList = regionSupplierService.findRegionSuppliersByStoreCode(
                    shiroUser.getStoreCode());

            List<Supplier> supplierList = supplierService.findSuppliersByStatusAndRegionIdsIn(
                    Collections3.extractToList(regionList, Supplier.ID_FIELD_NAME), null);

            return Collections3.extractToList(supplierList, Supplier.ID_FIELD_NAME);
        }

        if (AccoutTypeEnum.REGION_SUPPLIER.equals(shiroUser.getAcctType())) {
            //区域供应商Id
            SupplierService supplierService = PropertyHolder.appCtx.getBean(SupplierService.class);
            List<Supplier> supplierList = supplierService.findSuppliersByRegionIdAndStatus(shiroUser.getSupplierId(),
                    null);
            return Collections3.extractToList(supplierList, Supplier.ID_FIELD_NAME);
        }

        if (AccoutTypeEnum.PROD_SUPPLIER.equals(shiroUser.getAcctType())) {
            return Lists.newArrayList(shiroUser.getSupplierId());
        }

        return null;
    }

    /**
     * 返回登录用户ID，没有登录返回 null
     */
    public static Long getLoggedUserId() {
        ShiroUser user = getLoggedUser();
        if (user == null) {
            return null;
        } else {
            return user.getId();
        }
    }

    public static boolean isAjaxRequest(HttpServletRequest req) {
        String constant = "x-requested-with";
        String xRequestedWith = req.getHeader(constant);
        return StringUtils.isNotBlank(xRequestedWith) && StringUtils.equalsIgnoreCase(xRequestedWith, "XMLHttpRequest");
    }

    /**
     * @return true 已登录, false 未登录
     */
    public static boolean isLogged() {
        return getLoggedUserId() != null;
    }

    /**
     * 短信验证码是否正确，或已经过期
     *
     * @param vcode 用户输入的验证码
     */
    public static boolean isSmsVCodeValid(Session session, final String vcode) {
        String sessionVcode = (String) session.getAttribute(Constants.REGISTER_VCODE);
        if (StringUtils.isEmpty(sessionVcode)) {
            return false;
        }

        Long sendVcodeTimestamp = (Long) session.getAttribute(Constants.REGISTER_VCODE_SMS_TIMESTAMP);
        int count = Integer.parseInt(session.getAttribute(Constants.REGISTER_WRONGCOUNT).toString());
        //短信验证码最多只能错误5次
        if (count > 5) {
            session.removeAttribute(Constants.REGISTER_VCODE);
            return false;
        }

        //验证码有效期是5分钟
        if ((System.currentTimeMillis() - sendVcodeTimestamp) > DateUtils.MILLIS_PER_MINUTE * 5) {
            return false;
        }
        if (!sessionVcode.equalsIgnoreCase(vcode)) {
            session.setAttribute(Constants.REGISTER_WRONGCOUNT, count + 1);
        }

        return sessionVcode.equalsIgnoreCase(vcode);
    }

    /**
     * 获得客户端ip地址
     */
    public static String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");

        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        List<String> ips = QueryStringUtils.findMatchsContentsByRegex(ip, "[\\d\\.]{7,15}");

        String ipAddr = ips.size() > 0 ? ips.get(0) : "0.0.0.0";
        if (StringUtils.isNotBlank(ipAddr) && ipAddr.length() > 20) {
            ipAddr = ipAddr.substring(0, 20);
        }
        return ipAddr;
    }

    public static UserAgent parseUserAgent(HttpServletRequest request) {
        final String ua = StringUtils.trimToEmpty(request.getHeader("User-Agent"));
        UserAgent userAgent = new UserAgent();

        if (ua.contains("Windows")) { // 平台是 windows
            /**
             * ****************** 台式机 Windows 系列 ****************** Windows NT 6.2 - Windows 8 Windows NT 6.1 - Windows
             * 7 Windows NT 6.0 - Windows Vista Windows NT 5.2 - Windows Server 2003; Windows XP x64 Edition Windows NT
             * 5.1 - Windows XP Windows NT 5.01 - Windows 2000, Service Pack 1 (SP1) Windows NT 5.0 - Windows 2000
             * Windows NT 4.0 - Microsoft Windows NT 4.0 Windows 98; Win 9x 4.90 - Windows Millennium Edition (Windows
             * Me) Windows 98 - Windows 98 Windows 95 - Windows 95 Windows CE - Windows CE Mozilla/5.0 (compatible; MSIE
             * 9.0; Windows Phone OS 7.5; Trident/5.0; IEMobile/9.0
             */

            userAgent.setOsType(UserAgent.OsType.WINDOWS);

            if (ua.contains("Windows Phone")) {
                userAgent.setClientType(UserAgent.ClientType.WINDOWS_PHONE);
            } else {
                userAgent.setClientType(UserAgent.ClientType.PC);
            }
        } else if (ua.contains("Mac OS X")) {
            /**
             * 苹果系列 iPod - Mozilla/5.0 (iPod; U; CPU iPhone OS 4_3_1 like Mac OS X; zh-cn) AppleWebKit/533.17.9 (KHTML,
             * like Gecko) Version/5.0.2 Mobile/8G4 Safari/6533.18.5 iPad - Mozilla/5.0 (iPad; U; CPU OS 3_2 like Mac OS
             * X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B334b Safari/531.21.10 iPad2 -
             * Mozilla/5.0 (iPad; CPU OS 5_1 like Mac OS X; en-us) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1
             * Mobile/9B176 Safari/7534.48.3 iPhone 4 - Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_0 like Mac OS X; en-us)
             * AppleWebKit/532.9 (KHTML, like Gecko) Version/4.0.5 Mobile/8A293 Safari/6531.22.7 iPhone 5 - Mozilla/5.0
             * (iPhone; CPU iPhone OS 5_0 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9A334
             * Safari/7534.48.3
             */
            userAgent.setOsType(UserAgent.OsType.IOS);

            if (ua.contains("iPhone")) {
                userAgent.setClientType(UserAgent.ClientType.IPHONE);
            } else if (ua.contains("iPod")) {
                // iPod touch
                userAgent.setClientType(UserAgent.ClientType.ITOUCH);
            } else if (ua.contains("iPad")) {
                userAgent.setClientType(UserAgent.ClientType.IPAD);
            } else {
                // 苹果台式机或苹果笔记本
                userAgent.setClientType(UserAgent.ClientType.PC);
            }
        } else if (ua.contains("Linux")) {
            userAgent.setOsType(UserAgent.OsType.LINUX);

            if (ua.contains("Android")) {
                // andriod手机
                userAgent.setClientType(UserAgent.ClientType.ANDROID);
            } else {
                userAgent.setClientType(UserAgent.ClientType.PC);
            }
        }
        return userAgent;
    }

    /**
     * 发生业务错误，或请求参数不符合条件，响应400状态码.
     *
     * @param message 错误信息, 直接作为HTTP body输出
     */
    @SuppressWarnings("unchecked")
    public static ResponseEntity response400(String message) {
        return new ResponseEntity(message, null, HttpStatus.BAD_REQUEST);
    }

    /**
     * 发生服务器内部错误，响应500状态码.
     *
     * @param message 错误信息, 直接作为HTTP body输出
     */
    @SuppressWarnings("unchecked")
    public static ResponseEntity response500(String message) {
        return new ResponseEntity(message, null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][0-9]{10}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 电话号码验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isPhone(String str) {
        Pattern p1 = null, p2 = null;
        Matcher m = null;
        boolean b = false;
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$"); // 验证带区号的
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // 验证没有区号的
        if (str.length() > 9) {
            m = p1.matcher(str);
            b = m.matches();
        } else {
            m = p2.matcher(str);
            b = m.matches();
        }
        return b;
    }

    public static String join(String join, List<String> strAry) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strAry.size(); i++) {
            if (i == (strAry.size() - 1)) {
                sb.append(strAry.get(i));
            } else {
                sb.append(strAry.get(i)).append(join);
            }
        }
        return new String(sb);
    }

    /**
     * 返回a-b的新List. 去掉a和b的重复元素再相减
     */
    public static <T> List<T> subtractUnique(final Collection<T> a, final Collection<T> b) {
        List<T> listA = new ArrayList<T>();
        List<T> listB = new ArrayList<T>();
        for (T t : a) {
            if (!listA.contains(t)) {
                listA.add(t);
            }
        }
        for (T t : b) {
            if (!listB.contains(t)) {
                listB.add(t);
            }
        }
        for (T element : listB) {
            listA.remove(element);
        }

        return listA;
    }

}