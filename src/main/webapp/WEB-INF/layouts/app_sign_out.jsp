<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<html lang="zh">

<head>
    <meta charset="utf-8">
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
    <link href="${ctx}/static/hplus/css/animate.css" rel="stylesheet">
    <link href="${ctx}/static/hplus/css/style.css" rel="stylesheet">
    <!-- 必须加载的js -->
    <script src="${ctx}/static/hplus/js/jquery-2.1.1.min.js"></script>
    <script src="${ctx}/static/hplus/js/bootstrap.min.js"></script>
    <script src="${ctx}/static/js/utils.js"></script>
    <script>
    var ctx = '${ctx}';
    </script>
    <!-- 每页特殊样式-->
    <sitemesh:head/>
</head>

<body class="gray-bg">

    <div class="middle-box text-center animated fadeInDown">
        <!-- 显示每页的主要内容-->
        <sitemesh:body/>
    </div>


</body>

</html>
