<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!doctype html> 
<html lang="zh-cn"> 
<head> 
    <title>品牌列表页</title>
</head>
<body>
    <div id="brandList" v-cloak>
    <left-bar></left-bar>
    <div class="wrap wrap-blur" >
    <div v-show="isTop">
        <go-top :top="top"></go-top>
    </div>
        <go-back :title="title" :id="[top]" :num="num" :none='none'></go-back>
        <div class="filter-box bar-fixed-top">
                <nav flex="box:mean cross:center" class="bar bar-filter">
                    <a href="javascript:;" class="bar-item " @click="show('attention')" :class="[attentionShow?'_active':'']">
                        关注度
                    </a>
                    <a href="javascript:;" class="bar-item" @click="show('price')" :class="[priceshow?'_active':'']">
                        价格区间
                        <i class="triangle _down">triangle</i>
                    </a>
                    <!--注意： 升序 请加class「_asc」-->
                    <a flex="cross:center main:center" href="javascript:;" class="bar-item item-sort " @click="show('sort')" @click="show('sort')" :class="{'_asc':sortShow,'_active':sortShow}">
                        价格
                        <svg class="svg-sort ">
                            <use xlink:href="#svg-desc" class="svg-desc"/>
                        </svg>
                        <svg class="svg-sort">
                            <use xlink:href="#svg-asc" class="svg-asc" />
                        </svg>
                    </a>
                </nav>  
                <div flex=" cross:center" class="filter-content filter filter-price" :class="[priceshow?'':'hidden']">
                    <div class="item-fiter" v-for="item in price" :class="[item.active?'_active':'']" @click="choosePrice($index,item.startPrice,item.endPrice)">{{item.text}}</div>
                </div>
                <!--/.filter-content-->
            </div>
        <!--/.bar-nav-->    
        <div class="content" > 
            <!--/.filter-box-->
            <!--
                注意：价格区间和品牌需要显示遮罩层 class「_in」
            -->
            <div class="modal-overlay " :class="[priceshow?'_in':'']" @click="priceshow=false"></div>
            <div class="tips-not-found" v-show="notFound">
                <i class="icon icon-cry">icon</i>
                <p>亲，很抱歉，没有找到搜索商品</p>
                <p>为您推荐以下商品</p>
            </div>
            <!--/.bar-tab-->
            <section flex="box:mean" class="flex-wrap waterfal-flow">
                <aside class="aside aside-1">
                    <div flex="cross:center main:center" class="classify-tit" v-show="!notFound">
                        <div class="text-center">
                            <p class="item">{{english}}</p>
                            <p class="item">{{title}}</p>
                        </div>
                        <i class="triangle _all">triangle</i>
                    </div>
                    <!--
                        注意：选中红心 请在list-block上加class「_active」
                    -->
                    <div class="list-block item-goods " :class="[item.active?'_active':'']" v-for="item in productListLeft">
                        <a href="/product/{{item.id}}/m" flex="main:center" class="item-media" @click="go">
                            <img src="/static/m/images/material/p-l-334x340.jpg" v-lazyload="item.loadableMainImgUrl" alt="商品图片">
                        </a>
                        <a href="/product/{{item.id}}/m" class="item-inner" @click="go">
                            <div class="item-title _ellipsis">{{item.name}}</div>
                            <span class="item-price">市场价：<em>¥{{item.marketPrice}}</em></span>
                        </a>
                        <div class="click-heart" @click="cookieL(item,item.id,item.active,$index)">
                            <svg class="svg-heart "><use xlink:href="#svg-heart"/></svg>
                        </div>
                    </div>
                </aside>
                <aside class="aside aside-2">
                    <div class="list-block item-goods" v-for="item in productListRight" :class="[item.active?'_active':'']">
                        <a href="/product/{{item.id}}/m" flex="main:center" class="item-media" @click="go">
                            <img src="/static/m/images/material/p-l-334x340.jpg" v-lazyload="item.loadableMainImgUrl" alt="商品图片">
                        </a>
                        <a href="/product/{{item.id}}/m" class="item-inner" @click="go">
                            <div class="item-title _ellipsis">{{item.name}}</div>
                            <span class="item-price">市场价：<em>¥{{item.marketPrice}}</em></span>
                        </a>
                        <div class="click-heart" @click="cookieR(item,item.id,item.active,$index)">
                            <svg class="svg-heart "><use xlink:href="#svg-heart"/></svg>
                        </div>
                    </div>
                </aside>
            </section>
            <div class="txt-no-data" v-show="end">向上翻翻吧，已经到底啦！</div>
            <div flex="main:center cross:center" class="loading-product" v-show="loading">
                <svg class="icon-loading"><use xlink:href="#svg-loading"/></svg>
            </div>
            <!--/.waterfal-flow-->
        </div> 
        <!--/.content-->
    </div> 
    </div> 
    <script src="${ctx}/static/m/js/brandlist.js?v=ea38b0f7"></script>
</body>
</html>