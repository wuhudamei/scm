<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="keywords" content="美得你,供应链,系统">
  <meta name="description" content="美得你供应链平台">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp" />
  <title><sitemesh:title/></title>

  <!-- style -->
  <link rel="shortcut icon" href="/static/admin/img/favicon.ico">
  <link rel="stylesheet" href="/static/admin/css/lib.css">
  <link rel="stylesheet" href="/static/admin/css/style.css">




  <script src="/static/admin/js/lib.js"></script>
  <%@include file="/WEB-INF/views/admin/shims/polyfill.jsp" %>
  <%@include file="/WEB-INF/views/admin/shims/config.jsp" %>

  <!-- 页面公用 -->
  <script src="/static/admin/js/util.js"></script>
  <script src="/static/admin/js/main.js"></script>
  <script src="/static/admin/js/mixins/mixins.js"></script>
  <script src="/static/admin/js/filters/filters.js"></script>
  <script src="/static/admin/js/components/breadcrumb.js"></script>
  <script src="/static/admin/js/components/contractInfo.js"></script>
  <script>
  var ctx = '${ctx}';
  </script>

  <!-- 每页特殊样式特殊js-->
  <sitemesh:head/>
</head>
<body id="app" class="fixed-sidebar full-height-layout gray-bg">
  <!-- 添加组件模板到这里 -->
  <%@include file="/WEB-INF/views/admin/components/breadcrumb.jsp" %>
  <%@include file="/WEB-INF/views/admin/components/contractinfo.jsp" %>
  <div id="wrapper">
    <%@include file="/WEB-INF/views/admin/includes/nav.jsp" %>
    <%@include file="/WEB-INF/views/admin/includes/header.jsp" %>
    <!--右侧部分开始-->
    <div id="page-wrapper" class="gray-bg dashbard-1">
      <sitemesh:body/>
    </div>
    <%@include file="/WEB-INF/views/admin/includes/footer.jsp" %>
    <!--右侧部分结束-->
  </div>
</body>
</html>