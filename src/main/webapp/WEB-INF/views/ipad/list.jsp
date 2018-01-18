<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html lang="zh-cn">

<head>
    <title>列表页</title>
    <script src="${ctx}/static/m/ipadjs/list.js?v=ea38b0f7"></script>
</head>

<body>

    <div class="wrap wrap-blur wrap-ipad">
        <%@ include file="/WEB-INF/views/ipad/_head_includeback_fragment.jsp" %>

        <%@ include file="/WEB-INF/views/ipad/_head_classify_fragment.jsp" %>

        <div class="content">
            <div class="content-space">
                <ul class="breadcrumbs">
                    <li><a href="${ctx}/ipad">首页</a></li>
                    <!-- <li><a href="javascript:;">品牌</a></li> -->
                    <li><span data-list="tip"></span></li>
                </ul>
                <div class="filter-box" data-list="filterbar">
                    <nav flex="box:mean cross:center" class="bar bar-filter">
                        <a href="javascript:void(0);" class="bar-item _active" data-list="fakeattention">
                            关注度
                        </a>
                        <a href="javascript:void(0);" class="bar-item" data-list="brand">
                            品牌
                            <i class="triangle _down">triangle</i>
                        </a>
                        <a href="javascript:void(0);" class="bar-item" data-list="chooseprice">
                            <label>价格区间</label>
                            <i class="triangle _down">triangle</i>
                        </a>
                        <!--注意： 升序 请加class「_asc」-->
                        <a flex="cross:center main:center" href="javascript:;" class="bar-item item-sort" data-list="market_price">
                            价格
                            <svg class="svg-sort ">
                                <use xlink:href="#svg-desc" class="svg-desc" />
                            </svg>
                            <svg class="svg-sort">
                                <use xlink:href="#svg-asc" class="svg-asc"  />
                            </svg>
                        </a>
                    </nav>
                    <!--价格区间-->
                    <div flex=" cross:center" class="filter-content filter filter-price disN" data-list="chooseinterval">
                        <div class="item-fiter" data-start="0" data-end="100" data-text="0-100">0-100</div>
                        <div class="item-fiter" data-start="101" data-end="200" data-text="101-200">101-200</div>
                        <div class="item-fiter" data-start="201" data-end="500" data-text="201-500">201-500</div>
                        <div class="item-fiter" data-start="501" data-end="1000" data-text="501-1000">501-1000</div>
                        <div class="item-fiter" data-start="1001" data-end="999999999" data-text="1000以上">1000以上</div>
                        <div class="item-fiter" data-start="0" data-end="999999999" data-text="价格区间">全部</div>
                    </div>
                    <!--/.filter-content hidden-->
                    <div class="filter-content hidden" data-list="brandcontainer">
                        <!--品牌显示区域-->
                    </div>
                    <!--/.filter-content-->
                </div>
                <!--/.filter-box-->
            </div>
            <!--/.content-space-->
            <div class="tips-not-found disN" data-list="noprotip">
                <i class="icon icon-cry">icon</i>
                <p>亲，很抱歉，没有找到对应的搜索商品</p>
                <p>为您推荐以下商品</p>
            </div>

            <section flex="main:center" class="flex-wrap col-4 mt-10" data-list="prolist">
                <!--端口展示区-->
            </section>
            <!--/.col-2-->
        </div>
        <!--/.content-->
    </div>
    <div class="txt-no-data disN" data-list="bottom">向上翻翻吧，已经到底啦！</div>
    <!--/.wrap-->
    <div flex="main:center cross:center" class="loading-product disN" data-list="loading">
        <svg class="icon-loading"><use xlink:href="#svg-loading"/></svg>
    </div>
    <!--/.loading-->
<script id="listtemplate" type="text/mustache">
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
    {{#brand}}
        <div class="filter-letter-box" >
            <div flex=" " class="filter filter-letter _show">
                {{#letter}}
                <span class="item {{isActive}}" data-list="swap" data-letter="{{letter}}">{{letter}}</span>
                {{/letter}}
            </div>
        </div>
        {{#letter}}
        <div class="filter-brand-box {{isFirst}}" data-list="allbrand" data-letter="_{{letter}}">
            <div flex=" " class="filter filter-brand" style="width: {{witdh}}rem;">
                {{#brand}}
                <div class="item-fiter _ellipsis" data-id="{{id}}" data-brand="allitem">{{en}} {{name}}</div>
                {{/brand}}
            </div>
        </div>
        {{/letter}}
    {{/brand}}
</script>
</body>
</html>