<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!doctype html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no" />
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta content="telephone=no" name="format-detection"/>
    <meta content="email=no" name="format-detection" />
    <meta http-equiv="x-ua-compatible" content="IE=Edge, chrome=1" />
    <title><sitemesh:title/></title>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <link href="${ctx}/static/m/css/style.css?v=dfs" rel="stylesheet">
    <link href="${ctx}/static/m/css/swiper.min.css" rel="stylesheet">


    <!-- 必须加载的js -->
    <script src="${ctx}/static/m/lib/vue.min.js"></script>
    <script src="${ctx}/static/m/lib/vue-resource.js"></script>
    <script src="${ctx}/static/m/lib/zepto_fastclick.js"></script>
    <script src="${ctx}/static/m/lib/iscroll.js"></script>
    <script src="${ctx}/static/m/lib/utils.js"></script>
    <script src="${ctx}/static/pc/js/cookie.js"></script>
    <script src="${ctx}/static/m/lib/vue-lazyimg.js"></script>
    <script src="${ctx}/static/m/lib/swiper.min.js"></script>
    <script src="${ctx}/static/pc/js/jquery-1.12.4.min.js"></script>
    <script src="${ctx}/static/m/lib/jquery.fly.min.js"></script>
    <script type="text/javascript">
        Vue.use(Vue.lazyimg,{
        /**
         * 是否开启淡入效果的全局选项
         */
        fadein: false,
        /**
         * 是否忽略横向懒加载的全局选项
         */
        nohori: false,
        /**
         * 对屏幕滚动的速度的阈值，滚动速度高于此值时，不加载图片
         */
        speed: 20
        });
    </script>
    <script>
        var ctx = '${ctx}';
        </script>
    <script src="${ctx}/static/m/js/component.js"></script>

    <!-- 每页特殊样式-->
    <sitemesh:head/>
</head>
<body>
    <%@ include file="/WEB-INF/layouts/_svg.jsp" %>
    <%@ include file="/WEB-INF/views/m/component.jsp" %>
    <sitemesh:body/>

<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1261204796'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s11.cnzz.com/z_stat.php%3Fid%3D1261204796%26show%3Dpic' type='text/javascript'%3E%3C/script%3E"));</script>
</body>
</html>