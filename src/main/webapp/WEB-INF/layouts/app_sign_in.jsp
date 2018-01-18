<%@page import="com.qian.fof.service.fund.ConfigService"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<!doctype html>
<html lang="zh">
<head>
 <%
    ConfigService configService=  com.qian.fof.PropertyHolder.appCtx.getBean(ConfigService.class);
    String configJson = com.qian.fof.utils.JsonUtils.pojoToJson(configService.getConfig());
 	pageContext.setAttribute("configJson", configJson);
 %>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta content="telephone=no" name="format-detection"/>
    <meta content="email=no" name="format-detection" />
    <title><sitemesh:title/></title>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <link href="${ctx}/static/hplus/css/bootstrap.min.css" rel="stylesheet">
    <link href="${ctx}/static/hplus/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="${ctx}/static/hplus/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
    <link href="${ctx}/static/hplus/css/animate.css" rel="stylesheet">
    <link href="${ctx}/static/hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet">
    <link href="${ctx}/static/hplus/css/style.css" rel="stylesheet">
    <link href="${ctx}/static/css/custom.css" rel="stylesheet">

    <!-- 必须加载的js -->
    <script src="${ctx}/static/hplus/js/jquery-2.1.1.min.js"></script>
    <script src="${ctx}/static/hplus/js/plugins/decimal/decimal.min.js"></script>
    <script src="${ctx}/static/hplus/js/plugins/mathjs/math.min.js"></script>
    <script src="${ctx}/static/js/lib/hogan-3.0.2.min.js"></script>
    <script src="${ctx}/static/hplus/js/bootstrap.min.js"></script>
    <script src="${ctx}/static/hplus/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="${ctx}/static/hplus/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <script src="${ctx}/static/hplus/js/plugins/pace/pace.min.js"></script>
    <script src="${ctx}/static/hplus/js/plugins/toastr/toastr.min.js"></script>
    <script src="${ctx}/static/hplus/js/hplus.js"></script>
    <script src="${ctx}/static/js/utils.js"></script>
   <%--  <script src="${ctx}/static/js/common.js"></script> --%>
    <script>
    var ctx = '${ctx}';
    var config = ${configJson};
    var adminUsername = "admin";
    </script>
    <!-- 每页特殊样式-->
    <sitemesh:head/>
</head>
<body class="fixed-nav fixed-sidebar skin-4">
<div id="wrapper">
    <%@ include file="/WEB-INF/views/shim/header.jsp" %>
    <!-- 左侧导航栏-->
    <%@ include file="/WEB-INF/views/shim/nav.jsp" %>
    <!-- 左侧导航栏结束-->
    <!-- 右侧内容-->
    <div id="page-wrapper" class="gray-bg dashbard-1">
        <!-- header -->

        <!-- 显示每页的主要内容-->
        <sitemesh:body/>

        <!-- footer-->
        <%@ include file="/WEB-INF/views/shim/footer.jsp" %>
    </div>
    <!-- 右侧内容结束-->
</div>


</body>
</html>