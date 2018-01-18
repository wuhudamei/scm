<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!doctype html> 
<html lang="zh-cn"> 
<head> 
    <title>首页</title>
</head>
<body>
	<div id="index" v-cloak>
	<!--
		注意：毛玻璃效果弹层，必须放在wrap-blur前面
		class「 _in 」显示弹层
	-->
	<left-bar :is-show.sync="isShow" :left-btn="leftBtn"></left-bar>
	<div v-show="isTop">
        <go-top :top="top"></go-top>
    </div>
	<div class="wrap wrap-blur wrap-index">
		<header flex="cross:center box:justify" class="bar bar-nav bar-search " id="top">
	    	<a href="javascript:;" flex="cross:center" class="bar-item" @click="isS">
				<i class="icon icon-menu">icon</i>
			</a>
			<div class="item-search">
	            <div class="search clearfix" id="search">
	            	<form v-on:submit.stop.prevent="searchKey">
		                <div class="search-input">
							<!--
								注意：
								1. 当 input获取焦点的时候， search-icon-front 的 class 为 「 hidden 」 隐藏搜索图标
								2. 当 input获取焦点的时侯， search-icon 的 class 为 「 show 」显示关闭图标
							-->					
		                    <input  data-handle="_searchinput" placeholder="搜索：关键词" v-model="key" debounce="500" type="search" @focus="" :class='[focusState?"_focus":""]'>
							<label class="search-icon-front show "><svg class="svg-search"><use xlink:href="#svg-search"/></svg></label>
							<label class="search-icon hidden" @click='clear'><i class="icon icon-close">icon</i></label>
		                </div>
	                </form>
	            </div> 
				<!--/.search -->
				<!--显示class为「 show 」-->
	            <div class="list-search-result" :class='[searchshow?"show":"hidden"]'>
					<a class="item" v-for="item in searchList" @click="choose(item.id)">
						{{item.name}}
					</a> 
				</div> 
				<!--/.list-search-result-->
			</div>
			<a href="${ctx}/m/message" flex="cross:center" class="bar-item">
				<svg class="svg-wish"><use xlink:href="#svg-wish"/></svg>
				<div class="badge _red" v-if="none">{{num}}</div>
			</a>
		</header> 
		<!--/.bar-nav-->
		<div class="content">
			<!-- Swiper -->
			<div class="swiper-container swiper-container1">
				<div class="swiper-wrapper">
					<a class="swiper-slide" v-for="item in bannerList" :href="item.link">
						<img :src="item.imgUrl" alt="">
					</a> 
				</div>
				<!-- Add Pagination -->
				<div class="swiper-pagination"></div>
			</div>
			<!--使用 flexbox 不规则布局-->
			<!--<div class="banner-box">
				<section flex="box:mean" class="banner-section" >
					<aside class="item aside-1">
						<a href="${ctx}/m/list?cata=1" class="banner ">
							<img src="${ctx}/static/m/images/wap-banner/b-shengdian.png" alt="">
							<div  class="banner-text">
								<span flex="cross:center main:center">生活电器</span>
							</div>
						</a>
						<a href="${ctx}/m/list?cata=3" class="banner  ">
							<img src="${ctx}/static/m/images/wap-banner/b-gedian.png" alt="">
							<div  class="banner-text">
								<span flex="cross:center main:center">个护电器</span>
							</div>
						</a>
					</aside>
					<aside class="item aside-2">
						<a href="${ctx}/m/list?cata=2" class="banner ">
							<img src="${ctx}/static/m/images/wap-banner/b-chudian.png" alt="">
							<div  class="banner-text">
								<span flex="cross:center main:center">厨房电器</span>
							</div>
						</a>
						<a href="${ctx}/m/list?cata=4" class="banner  ">
							<img src="${ctx}/static/m/images/wap-banner/b-jiayong.png" alt="">
							<div  class="banner-text">
								<span flex="cross:center main:center">家居生活</span>
							</div>
						</a>
					</aside>
				</section>
				<section class="banner-section" >
					<a href="${ctx}/m/list?cata=5" class="banner">
						<img src="${ctx}/static/m/images/wap-banner/b-chuyong.png" alt="">
						<div  class="banner-text">
							<span flex="cross:center main:center">厨房用品</span>
						</div>
					</a>
				</section>
				<section flex="box:mean" class="banner-section" >
					<aside class="item aside-1">
						<a href="${ctx}/m/list?cata=6" class="banner ">
							<img src="${ctx}/static/m/images/wap-banner/b-shangxiang.png" alt="">
							<div  class="banner-text">
								<span flex="cross:center main:center">商旅箱包</span>
							</div>
						</a>
						<a href="${ctx}/m/list?cata=8" class="banner  ">
							<img src="${ctx}/static/m/images/wap-banner/b-dianshu.png" alt="">
							<div  class="banner-text">
								<span flex="cross:center main:center">电子数码</span>
							</div>
						</a>
					</aside>
					<aside class="item aside-2">
						<a href="${ctx}/m/list?cata=7" class="banner ">
							<img src="${ctx}/static/m/images/wap-banner/b-yunhu.png" alt="">
							<div  class="banner-text">
								<span flex="cross:center main:center">运动户外</span>
							</div>
						</a>
						<a href="${ctx}/m/list?cata=9" class="banner  ">
							<img src="${ctx}/static/m/images/wap-banner/b-qiyong.png" alt="">
							<div  class="banner-text">
								<span flex="cross:center main:center">汽车用品</span>
							</div>
						</a>
					</aside>
				</section>
				<section class="banner-section" >
					<a href="${ctx}/m/list?cata=10" class="banner">
						<img src="${ctx}/static/m/images/wap-banner/b-muer.png" alt="">
						<div  class="banner-text">
							<span flex="cross:center main:center">母婴儿童</span>
						</div>
					</a>
				</section>
				<section flex="box:mean" class="banner-section" >
					<a href="${ctx}/m/list?cata=11" class="banner ">
						<img src="${ctx}/static/m/images/wap-banner/b-shijing.png" alt="">
						<div  class="banner-text">
							<span flex="cross:center main:center">时尚精品</span>
						</div>
					</a>
					<a href="${ctx}/m/list?cata=12" class="banner  ">
						<img src="${ctx}/static/m/images/wap-banner/b-shika.png" alt="">
						<div  class="banner-text">
							<span flex="cross:center main:center">食品卡券</span>
						</div>
					</a>
				</section>
				<section class="banner-section" >
					<a href="${ctx}/m/list?cata=13" class="banner">
						<img src="${ctx}/static/m/images/wap-banner/b-xushang.png" alt="">
						<div  class="banner-text">
							<span flex="cross:center main:center">虚拟商品</span>
						</div>
					</a>
				</section>
			</div> -->
			<!--/.banner-box -->

			<!--使用 flexbox 不规则布局-->
			<div class="banner-box">
				<div flex=" " class="flex-wrap">
					<section class="banner-section">
						<aside flex="box:mean" >
							<a href="${ctx}/m/list?cata=3" flex="cross:center main:center" class="banner ">
								<img src="${ctx}/static/m/images/b-gedian.png" alt="">
							</a>
							<a href="${ctx}/m/list?cata=5" flex="cross:center main:center" class="banner  ">
								<img src="${ctx}/static/m/images/b-chuyong.png" alt="">
							</a>
						</aside>
						<aside class="item">
							<a href="${ctx}/m/list?cata=6"  flex="cross:center main:center" class="banner">
								<img src="${ctx}/static/m/images/b-shangxiang.png" alt="">
							</a>
						</aside>
					</section>
					<section flex="box:mean" class="banner-section" >
						<aside class="item aside-1">
							<a href="${ctx}/m/list?cata=1" flex="cross:center main:center" class="banner ">
								<img src="${ctx}/static/m/images/b-shengdian.png" alt="">
							</a>
							<a href="${ctx}/m/list?cata=4" flex="cross:center main:center" class="banner  ">
								<img src="${ctx}/static/m/images/b-jiayong.png" alt="">
							</a>
						</aside>
						<aside class="item aside-2">
							<a href="${ctx}/m/list?cata=2" flex="cross:center main:center" class="banner ">
								<img src="${ctx}/static/m/images/b-chudian.png" alt="">
							</a>
							<a href="${ctx}/m/list?cata=7" flex="cross:center main:center" class="banner  ">
								<img src="${ctx}/static/m/images/b-yunhu.png" alt="">
							</a>
						</aside>
					</section>
				</div>

				<div flex=" " class="flex-wrap">
					<section flex="box:mean" class="banner-section" >
						<aside class="item aside-1">
							<a href="${ctx}/m/list?cata=8" flex="cross:center main:center" class="banner ">
								<img src="${ctx}/static/m/images/banner-index/b-dianshu.png" alt="">
							</a>
						</aside>
						<aside class="item aside-2">
							<a href="${ctx}/m/list?cata=9" flex="cross:center main:center" class="banner ">
								<img src="${ctx}/static/m/images/banner-index/b-qiyong.png" alt="">
							</a>
							<a href="${ctx}/m/list?cata=11" flex="cross:center main:center" class="banner  ">
								<img src="${ctx}/static/m/images/banner-index/b-shijing.png" alt="">
							</a>
						</aside>
					</section>
					<section class="banner-section" >
						<aside class="item">
							<a href="${ctx}/m/list?cata=10"  flex="cross:center main:center" class="banner">
								<img src="${ctx}/static/m/images/b-muer.png" alt="">
							</a>	
						</aside>
						<aside flex="box:mean" >
							<a href="${ctx}/m/list?cata=12" flex="cross:center main:center" class="banner ">
								<img src="${ctx}/static/m/images/b-shika.png" alt="">
							</a>
							<a href="${ctx}/m/list?cata=13" flex="cross:center main:center" class="banner  ">
								<img src="${ctx}/static/m/images/b-xushang.png" alt="">
							</a>
						</aside>
					</section>
				</div>
			</div> 
			<!--/.banner-box -->
		</div>
		<!--/.content-->
		<footer class="footer">
			<h1 class="title">北京大诚若谷信息技术有限公司</h1>
			<div class="txt">
				<p>北京市朝阳区广渠路3号竞园41-A（北京总部） 100124</p>
				<p>Tel: (+8610)67719633 Fax: (+8610)67719622</p>
			</div>
		</footer>
	</div> 
	<!--/.wrap-->
	<!--左导航显示 -->
	<div>
	<script src="${ctx}/static/m/js/index.js?v=ea38b0f7"></script>
	<script src="${ctx}/static/m/lib/swiper.min.js?v=ea38b0f7"></script>
	<!-- <script>
		// 让 css hover 起作用
		// document.body.addEventListener('touchstart', function () { }); 

		var swiper = new Swiper('.swiper-container1', {
			pagination: '.swiper-pagination',
	        paginationClickable: true,
			autoplay: 2000
	    });

		var swiper = new Swiper('.swiper-container2', {
			// direction: 'vertical',
	        slidesPerView: 18,
	    });
	</script> -->
</body>
</html>