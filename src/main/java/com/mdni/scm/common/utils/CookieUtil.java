package com.mdni.scm.common.utils;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author zhangmin
 */
public final class CookieUtil {

    public static final String CART_ITEM_COOKIE_NAME_PREFIX = "cartItem_";

    public static final String SSO_TOKEN_COOKIE_NAME = "JJCToken16";

    private static final String OPEN_ID_COOKIE_NAME = "cookie_openId";

    /**
     * 添加cookie
     *
     * @param response
     * @param name     不能为null
     * @param value    默认值空字符串
     * @param maxAge   默认值0,单位秒
     * @param path     默认值'/'
     */
    public static void addCookie(HttpServletRequest request, HttpServletResponse response, String name, String value,
                                 Integer maxAge, String path) {
        if (maxAge == null) {
            maxAge = 0;
        }
        if (value == null) {
            value = StringUtils.EMPTY;
        }
        if (path == null) {
            path = "/";
        }

        Cookie cookie = getCookie(request, name);
        if (null == cookie) {
            cookie = new Cookie(name, value);
        }
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);

        response.addCookie(cookie);
    }


    public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookiePath) {
        //Remove cookies
        CookieUtil.addCookie(request, response, cookieName, StringUtils.EMPTY, 0, cookiePath);
    }

    /**
     * @param request
     * @param cookieName
     * @return 指定的cookie
     */
    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
        return readCookieMap(request).get(cookieName);
    }


    public static String getSsoTokenCookieValue(HttpServletRequest request) {
        Cookie cookie = getCookie(request, SSO_TOKEN_COOKIE_NAME);
        if (cookie != null) {
            return cookie.getValue();
        }
        return StringUtils.EMPTY;
    }


    public static Map<String, Cookie> readCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = Maps.newLinkedHashMap();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (int i = 0; i < cookies.length; i++) {
                cookieMap.put(cookies[i].getName(), cookies[i]);
            }
        }
        return cookieMap;
    }


    /**
     * @param request
     * @return Map<单品Id,购买数量>
     */
    public static Map<Long, Integer> readCartItemCookieMap(HttpServletRequest request) {
        Map<Long, Integer> itemQtMap = Maps.newLinkedHashMap();
        Iterator<Entry<String, Cookie>> entryIter = readCookieMap(request).entrySet().iterator();
        while (entryIter.hasNext()) {
            Entry<String, Cookie> entry = entryIter.next();
            final String cookieName = entry.getKey();
            Cookie cookie = entry.getValue();
            if (cookieName.startsWith(WebUtils.getLoggedUserId() + CART_ITEM_COOKIE_NAME_PREFIX)) {
                final String cookieValue = cookie.getValue();
                Long unitProdId = NumberUtils.toLong(cookieName.substring((WebUtils.getLoggedUserId() + CART_ITEM_COOKIE_NAME_PREFIX).length()));
                itemQtMap.put(unitProdId, NumberUtils.toInt(cookieValue));
            }
        }
        return itemQtMap;
    }


    /**
     * 返回  购物车Item 已经存在的数量
     *
     * @param skuId
     * @return
     */
    public static int getCartItemBoughtQuantity(HttpServletRequest request, long skuId) {
        final String cookieName = CART_ITEM_COOKIE_NAME_PREFIX + skuId;
        Cookie cookie = getCookie(request, cookieName);
        int qty = 0;
        if (cookie != null) {
            qty = NumberUtils.toInt(cookie.getValue());
        }
        return qty;
    }

    /**
     * 加入购物车时,把cartItem写入cookie
     *
     * @param skuId 单品Id
     */
    public static void addCartItemToCookie(HttpServletRequest request, HttpServletResponse response, long skuId, int quantity) {
        final String cookieName = WebUtils.getLoggedUserId() + CART_ITEM_COOKIE_NAME_PREFIX + skuId;
        int qty = getCartItemBoughtQuantity(request, skuId);
        qty += quantity;
        addOrUpdateCartItemCookie(request, response, cookieName, String.valueOf(qty));
    }


    /**
     * 修改 cookie item的数量
     *
     * @param skuId    单品Id
     * @param quantity
     */
    public static void modifyCookieCartItemQt(HttpServletRequest request, HttpServletResponse response, final long skuId, int quantity) {
        final String cookieName = WebUtils.getLoggedUserId() + CART_ITEM_COOKIE_NAME_PREFIX + skuId;
        if (quantity < 1) {
            //递减时，保证数量不少于1
            quantity = 1;
        }
        addOrUpdateCartItemCookie(request, response, cookieName, String.valueOf(quantity));
    }


    public static String getCookieOpenId(HttpServletRequest request) {
        String openId = StringUtils.EMPTY;
        Cookie cookie = CookieUtil.getCookie(request, OPEN_ID_COOKIE_NAME);
        if (cookie != null) {
            openId = cookie.getValue();
        }
        return openId;
    }

    public static void addOpenIdToCookie(HttpServletRequest request, HttpServletResponse response, String openId) {
        int maxAge = 3600 * 24 * 365 * 1; //有效期1年

        String cookiePath = getCookieStorePath(request);

        //cookie更新：先删除，后添加
        CookieUtil.removeCookie(request, response, OPEN_ID_COOKIE_NAME, cookiePath);
        CookieUtil.addCookie(request, response, OPEN_ID_COOKIE_NAME, openId, maxAge, cookiePath);
    }


    /**
     * 删除 购物车 cookie Item
     *
     * @param skuIds 单品Id
     */
    public static void removeCartItemCookie(HttpServletRequest request, HttpServletResponse response, Long... skuIds) {
        if (!ArrayUtils.isEmpty(skuIds)) {
            for (Long skuId : skuIds) {
                final String cookieName = WebUtils.getLoggedUserId() + CART_ITEM_COOKIE_NAME_PREFIX + skuId;
                CookieUtil.removeCookie(request, response, cookieName, getCookieStorePath(request));
            }
        }
    }

    /**
     * 清空购物车   item的所有cookie
     */
    public static void clearCartCookies(HttpServletRequest request, HttpServletResponse response) {
        Iterator<Long> iter = readCartItemCookieMap(request).keySet().iterator();
        while (iter.hasNext()) {
            Long skuId = iter.next();
            removeCartItemCookie(request, response, skuId);
        }
    }


    public static void batchSaveCartItemsToCookie(HttpServletRequest request, HttpServletResponse response, Map<Long, Integer> skuIdQtMap) {
        if (!CollectionUtils.isEmpty(skuIdQtMap)) {
            Iterator<Entry<Long, Integer>> entryIter = skuIdQtMap.entrySet().iterator();
            while (entryIter.hasNext()) {
                Entry<Long, Integer> entry = entryIter.next();
                Long skuId = entry.getKey();
                Integer quantity = entry.getValue();
                final String cookieName = WebUtils.getLoggedUserId() + CART_ITEM_COOKIE_NAME_PREFIX + skuId;
                addOrUpdateCartItemCookie(request, response, cookieName, String.valueOf(quantity));
            }
        }
    }


    private static void addOrUpdateCartItemCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue) {
        final String cookieStorePath = getCookieStorePath(request);
        int maxAge = 3600 * 24 * 60; //有效期2个月
        //cookie更新：先删除，后添加
        CookieUtil.removeCookie(request, response, cookieName, cookieStorePath);
        CookieUtil.addCookie(request, response, cookieName, cookieValue, maxAge, cookieStorePath);
    }


    private static String getCookieStorePath(HttpServletRequest request) {
        final String slash = "/";
        String cookieStorePath = request.getContextPath();
        if (!cookieStorePath.endsWith(slash)) {
            cookieStorePath = cookieStorePath.concat(slash);
        }
        return cookieStorePath;
    }
}