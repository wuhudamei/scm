<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!doctype html>
<html lang="zh">
<head>
    <title>列表</title>
    <script src="${ctx}/static/pc/js/list.js?v=ea38b0f7"></script>
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
                        <li class="active" data-list="tip"></li>
                    </ol>
                    <ul class="filter-bar" data-list="filterbar">
                        <li><a href="javascript:void(0);" class="ico-follow-asc" data-list="fakeattention">关注度</a></li>
                        <li>
                            <a href="javascript:void(0)" data-list="chooseprice"><label>价格区间</label><i class="icon ico-follow-des">icon</i></a>
                            <div class="range-list disN" data-list="chooseinterval">
                                <i class="icon icon-arrow-solid"></i>
                                <div><a href="javascript:void(0)" data-start="0" data-end="100" data-text="0-100">0-100</a></div>
                                <div><a href="javascript:void(0)" data-start="101" data-end="200" data-text="101-200">101-200</a></div>
                                <div><a href="javascript:void(0)" data-start="201" data-end="500" data-text="201-500">201-500</a></div>
                                <div><a href="javascript:void(0)" data-start="501" data-end="1000" data-text="501-1000">501-1000</a></div>
                                <div><a href="javascript:void(0)" data-start="1001" data-end="999999999" data-text="1000元以上">1000元以上</a></div>
                                <div><a href="javascript:void(0)" data-start="0" data-end="999999999" data-text="价格区间">全部</a></div>
                           </div>
                        </li>
                        <li><a href="javascript:void(0)" class="ico-sort-asc" data-list="market_price">价格</a></li>
                    </ul>
                </div>
                <div class="tab-state disN" data-list="noprotip">
                    <p><i class="icon icon-fail"></i></p>
                    <p>亲，很抱歉没有找到搜索的商品，为您推荐以下商品。</p>
                </div>
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

<script id="listtemplate" type="text/mustache">
    {{#list}}
    <div class="item {{isFour}}">
        <i class="icon {{isWish}}" data-list="wish" data-id="{{id}}">icon</i>
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
    {{#bottom}}
        <div class="clearfix" data-list="bottom"></div>
        <div class="txt-no-data" data-list="bottom"><span>向上翻翻吧，已经到底啦！</span></div>
    {{/bottom}}
</script>

</body>
</html>
