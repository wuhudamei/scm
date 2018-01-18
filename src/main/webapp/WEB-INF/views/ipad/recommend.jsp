
<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html lang="zh-cn">

<head>
    <title>列表页</title>
    <script src="${ctx}/static/m/ipadjs/recommend.js?v=ea38b0f7"></script>
</head>

<body>

    <div class="wrap wrap-blur wrap-ipad">
        <%@ include file="/WEB-INF/views/ipad/_head_includeback_fragment.jsp" %>


        <div class="content">
            <div class="content-space pd-t10">
                <ul class="breadcrumbs">
                    <li><a href="${ctx}/ipad">首页</a></li>
                    <li><a href="${ctx}/ipad/feedback">留言</a></li>
                    <li><span>推荐商品</span></li>
                </ul>

            </div>
            <!--/.content-space-->

            <section flex="main:center" class="flex-wrap col-4 mt-10" data-list="prolist">
                <!--商品展示区-->
            </section>
            <!--/.col-2-->
        </div>
        <!--/.content-->
    </div>
    <!--/.wrap-->
    <div class="txt-no-data disN" data-list="bottom">向上翻翻吧，已经到底啦！</div>
    <div flex="main:center cross:center" class="loading-product disN" data-list="loading">
        <svg class="icon-loading"><use xlink:href="#svg-loading"/></svg>
    </div>
    <!--/.loading-->
<script id="recommendtemplate" type="text/mustache">
    {{#listContainer}}
        {{#list}}
        <div class="list-block item-goods {{isWish}}" data-recommend="item">
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
</script>
</body>
</html>