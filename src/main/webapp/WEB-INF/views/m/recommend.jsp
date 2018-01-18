<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!doctype html>
<html lang="zh-cn">
<head>
	<title>推荐商品</title>
</head>
<body>	 
	<div class="wrap wrap-blur" id="recommend" v-cloak>
        <div v-show="isTop">
            <go-top :top="top"></go-top>
        </div>
		<go-back :title="title" :id="[top]" :num="num" :none="none"></go-back>
		<!--/.bar-nav-->
		<div class="content">
			<section flex="main:center" class="flex-wrap col-2 mt-10">
				<!--
					注意：选中红心 请在list-block上加class「_active」
				-->
				<div class="list-block item-goods " :class="[item.active?'_active':'']" v-for="item in productList">
                    <a href="/product/{{item.id}}/m" flex="main:center" class="item-media" @click="go">
                        <img src="/static/m/images/material/p-l-334x340.jpg" alt="商品图片" v-lazyload="item.loadableMainImgUrl">
                    </a>
                    <a href="/product/{{item.id}}/m" class="item-inner" @click="go">
                        <div class="item-title _ellipsis">{{item.name}}</div>
                        <span class="item-price">市场价：<em>¥{{item.marketPrice}}</em></span>
                    </a>
                    <div class="click-heart" @click="cookieL(item,item.id,item.active,$index)">
                        <svg class="svg-heart "><use xlink:href="#svg-heart"/></svg>
                    </div>
                </div>
				<!--
					注意： 以下的空div是占位的， 一行想要显示n个数据 ,就要预留n个占位
				 --> 
				<div class="item-goods list-block-placeholder"></div>
				<div class="item-goods list-block-placeholder"></div>
				<div class="item-goods list-block-placeholder"></div>
				<div class="item-goods list-block-placeholder"></div>	
			</section>
			<!--/.col-2-->
			<div class="txt-no-data" v-show="end">向上翻翻吧，已经到底啦！</div>
            <div flex="main:center cross:center" class="loading-product" v-show="loading">
                <svg class="icon-loading"><use xlink:href="#svg-loading"/></svg>
            </div>
		</div>
		<!--/.content-->
	</div>
	<!--/.wrap-->
	<div class="loading-page hidden">
        <svg class="icon-loading"><use xlink:href="#svg-loading"/></svg>
    </div>
    <script src="${ctx}/static/m/js/recommend.js?v=ea38b0f7"></script>
	<!--/.loading-->
</body>
</html>