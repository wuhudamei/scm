<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!doctype html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <meta name="keywords" content=" " />
    <meta name="description" content=" " />
    <meta http-equiv="x-ua-compatible" content="IE=Edge, chrome=1" />
    <meta name="renderer" content="webkit">
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no" />
    <meta name = "format-detection" content="telephone = no" />

    <title><sitemesh:title/></title>
    <link href="${ctx}/static/m/css/style_ipad.css?v=<%=Math.random()%>" rel="stylesheet">
    <link href="${ctx}/static/m/css/swiper.min.css" rel="stylesheet">

    <!-- 必须加载的js -->
    <script src="${ctx}/static/pc/js/jquery-1.12.4.min.js"></script>
    <script src="${ctx}/static/pc/js/cookie.js"></script>
    <script src="${ctx}/static/pc/js/jquery.lazyload.min.js"></script>
    <script src="${ctx}/static/pc/js/velocity.min.js"></script>
    <script src="${ctx}/static/pc/js/lodash.min.js"></script>
    <script src="${ctx}/static/pc/js/util.js?v=<%=Math.random()%>"></script>
    <script src="${ctx}/static/pc/js/gotop.js"></script>
    <script src="${ctx}/static/pc/js/hogan.js"></script>

    <script src="${ctx}/static/m/ipadjs/brand.js?v=<%=Math.random()%>"></script>
    <style>
        #_fly {
            position: fixed;
            top: 10px;
            right: 10px;
            display: none;
        }
    </style>
    <script>
    var ctx = '${ctx}';
    </script>
    <!-- 每页特殊样式-->
    <sitemesh:head/>
</head>
<body>
    <%@ include file="/WEB-INF/layouts/_svg.jsp" %>

    <sitemesh:body/>

    <!--返回顶部按钮-->
    <a href="javascript:void(0)" class="return-top" id="floating_window"><i id="gotop" class="icon icon-return-top">icon</i></a>
    <div id="_fly" style="color:red;z-index: 1000">+1</div>

<script id="brandtemplate" type="text/mustache">

    {{#searchHistory}}
    <a class="item" href="${ctx}/product/{{id}}/ipad">{{name}}</a>
    {{/searchHistory}}
</script>

<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1261204796'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s11.cnzz.com/z_stat.php%3Fid%3D1261204796%26show%3Dpic' type='text/javascript'%3E%3C/script%3E"));</script>
</body>
</html>