<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html lang="zh-cn">

<head>
    <title>选中商品列表页</title>
    <script src="${ctx}/static/pc/js/follow.js?v=ea38b0f7"></script>
</head>

<body>
    <div class="wrap">
        <div class="header">
            <%@ include file="/WEB-INF/views/pc/_head_topbar_fragment.jsp" %>
            <div class="header-con">
                <%@ include file="/WEB-INF/views/pc/_head_classify_fragment.jsp" %>
            </div>
            <!--/.classify-menu-->
        </div>
        <!--/.header-->
        <div class="main">
            <div class="content">
                <div class="tab-pane">
                    <ol class="breadcrumb">
                        <li><a href="${ctx}/">首页 > </a></li>
                        <li><a href="${ctx}/feedback">留言 > </a></li>
                        <li class="active">选中商品</li>
                    </ol>
                </div>
                <div class="tab-num">共 <span data-wish="_count">0</span> 件商品</div>
                <div class="product-list" data-list="prolist">
                    <!--商品展示区-->
                </div>
                <!--/.product-list-->
            </div>
            <!--/.content-->
           <div class="cnzz"><script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1261204796'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s11.cnzz.com/z_stat.php%3Fid%3D1261204796%26show%3Dpic' type='text/javascript'%3E%3C/script%3E"));</script></div>
        </div>
        <!--/.main-->

        <%@ include file="/WEB-INF/views/pc/_right_nav_fragment.jsp" %>
    </div>
    <!--/.wrap -->
<script id="followtemplate" type="text/mustache">
    {{#list}}
    <div class="item {{isFour}}">
        <i class="icon icon-follow" data-list="wish" data-id="{{id}}">icon</i>
        <a class="row" href="${ctx}/product/{{id}}/pc?id={{id}}" target="_blank">
            <img class="lazy" data-original="{{loadableMainImgUrl}}"  alt="">
            <div class="item-name">
                <h1>{{name}}</h1>
                <h2>市场价：<span class="pro-price">¥{{marketPrice}}</span></h2></div>
        </a>
    </div>
    {{/list}}
    {{#loading}}
        <div class="clearfix" data-list="loading"></div>
        <div class="more-loading" data-list="loading"><img src="${ctx}/static/pc/images/loading.gif" alt="loading"></div>
    {{/loading}}
</script>
</body>
</html>
