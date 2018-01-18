<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!doctype html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta content="telephone=no" name="format-detection"/>
    <meta content="email=no" name="format-detection" />
    <title><sitemesh:title/></title>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <link href="${ctx}/static/pc/css/style.css?v=<%=Math.random()%>" rel="stylesheet">
    <link href="${ctx}/static/pc/css/swiper.min.css" rel="stylesheet">

    <!-- 必须加载的js -->
    <script src="${ctx}/static/pc/js/jquery-1.12.4.min.js"></script>
    <script src="${ctx}/static/pc/js/cookie.js"></script>
    <script src="${ctx}/static/pc/js/jquery.lazyload.min.js"></script>
    <script src="${ctx}/static/pc/js/velocity.min.js"></script>
    <script src="${ctx}/static/pc/js/lodash.min.js"></script>
    <script src="${ctx}/static/pc/js/util.js?v=<%=Math.random()%>"></script>
    <script src="${ctx}/static/pc/js/gotop.js"></script>
    <script src="${ctx}/static/pc/js/hogan.js"></script>

    <script src="${ctx}/static/pc/js/brand.js?v=<%=Math.random()%>"></script>
    <style>
        #_fly {
            position: fixed;
            z-index: 1000;
            bottom: 100px;
            right: 60px;
            display: none;
            color: #d71818;
            font-weight: bold;
            font-size: 18px;
        }
    </style>
    <script>
    var ctx = '${ctx}';
    </script>
    <!-- 每页特殊样式-->
    <sitemesh:head/>
</head>
<body>

    <sitemesh:body/>

<script id="brandtemplate" type="text/mustache">
    {{#brand}}
        <div class="pull-down disN" data-brands="all" data-brand="_{{classify}}">
            <ul class="sidebar-english clearfix">
                {{#brands}}
                <li> <a data-brand="_{{classify}}_{{letter}}" data-classify="{{classify}}" data-handle="_letterover" href="javascript:void(0)">{{letter}}</a></li>
                {{/brands}}
            </ul>
            {{#brands}}
            <ul class="brand-list clearfix disN" data-brands="_mouseout" data-brand="__{{classify}}" data-b="_{{classify}}_{{letter}}">
                {{#brand}}
                <li> <a class="{{isChooseBorder}}" href="${ctx}/list?cata={{classify}}&brands={{id}}">{{name}} <i class="icon {{isChooseDel}}">icon</i></a></li>
                {{/brand}}
            </ul>
            {{/brands}}
        </div>
    {{/brand}}
    {{#searchHistory}}
    <li><a href="${ctx}/product/{{id}}/pc" target="_blank">{{name}}</a></li>
    {{/searchHistory}}
</script>

<div id="_fly">+1</div>
</body>
</html>
