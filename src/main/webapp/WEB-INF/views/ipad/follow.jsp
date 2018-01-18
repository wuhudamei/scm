<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html lang="zh-cn">

<head>
    <title>选中商品列表页</title>
    <script src="${ctx}/static/m/ipadjs/follow.js?v=ea38b0f7"></script>
</head>

<body>

    <div class="wrap wrap-blur wrap-ipad">
        <%@ include file="/WEB-INF/views/ipad/_head_includeback_fragment.jsp" %>

        <div class="content">
            <div class="content-space pd-t10">
                <ul class="breadcrumbs">
                    <li><a href="${ctx}/ipad">首页</a></li>
                    <li><a href="${ctx}/ipad/feedback">留言</a></li>
                    <li><span>选中商品</span></li>
                </ul>

                <!--/.filter-box-->
                <div class="text-right">共<em class="text-red" data-wish="_count">0</em>件商品</div>
            </div>
            <!--/.content-space-->

            <section flex="main:center" class="flex-wrap col-4 mt-10" data-list="prolist">
                <!--内容区-->
            </section>
            <!--/.col-2-->
        </div>
        <!--/.content-->
    </div>
    <!--/.wrap-->
    <div class="loading hidden">
        <img class="spinner" src="${ctx}/static/m/images/spinner-white.svg " alt="loading">
    </div>
    <!--/.loading-->
<script id="followtemplate" type="text/mustache">
    {{#listContainer}}
        {{#list}}
        <div class="list-block item-goods _active" data-recommend="item">
            <a href="${ctx}/product/{{id}}/ipad" flex="main:center" class="item-media">
                <img data-original="{{loadableMainImgUrl}}">
            </a>
            <a href="${ctx}/product/{{id}}/ipad" class="item-inner">
                <div class="item-title _ellipsis">{{name}}</div>
                <span class="item-price">市场价：<em>¥{{marketPrice}}</em></span>
            </a>
            <div class="click-heart" data-list="wish" data-id="{{id}}">
                <svg class="svg-heart "><use xlink:href="#svg-heart"/></svg>
            </div>
        </div>
        {{/list}}
        <div class="item-goods list-block-placeholder"></div>
        <div class="item-goods list-block-placeholder"></div>
        <div class="item-goods list-block-placeholder"></div>
        <div class="item-goods list-block-placeholder"></div>
        <div class="item-goods list-block-placeholder"></div>
    {{/listContainer}}
    {{#loading}}
        <div class="clearfix" data-list="loading"></div>
        <div class="more-loading" data-list="loading"><img src="${ctx}/static/pc/images/loading.gif" alt="loading"></div>
    {{/loading}}
</script>
</body>
</html>