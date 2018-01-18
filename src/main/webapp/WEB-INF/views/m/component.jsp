<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<template id="leftBar">
<div class="modal-overlay _blur " :class='[isShow?"_in":""]' @click="isShow=!isShow"></div>
		<div class="bar-side-box " :class='[isShow?"_in":""]'>
			<i class="icon icon-side-btn" @click="isShow=!isShow" :class='[leftBtn?"disN":""]'>icon</i>
			<div class="bar-side">
				<div class="side-search bar-fixed-top">
					<div class="search clearfix" id="search">
						<form v-on:submit.stop.prevent="searchKey">
							<div class="search-input">
								<!--
									注意：
									1. 当 input获取焦点的时候， search-icon-front 的 class 为 「 hidden 」 隐藏搜索图标
									2. 当 input获取焦点的时侯， search-icon 的 class 为 「 show 」显示关闭图标
								-->
								<input data-handle="_searchinput" placeholder="搜索：关键词" v-model="key" debounce="500" type="search" @focus="" :class='[focusState?"_focus":""]'>
								<label class="search-icon-front show "><svg class="svg-search"><use xlink:href="#svg-search"/></svg></label>
								<label class="search-icon hidden" @click='clear'><i class="icon icon-close">icon</i></label>
							</div>
						</form>
					</div>
					<!--/.search -->
					<!--显示class为「 disB 」-->
					<div class="list-search-result" :class='[searchshow?"disB":"disN"]'>
						<div class="item" v-for="item in searchList" @click="choose(item.id)">
						{{item.name}}
						</div> 
					</div>
					<!--/.list-search-result-->
				</div>
				<!--/.item-search-->
				<div class="tabs tabs-line">
					<div flex="box:mean" class="tabs-menu bar-fixed-top">
					<a href="javascript:void(0);" @click="tab('classify')" class="tab-link _active" :class="{_active:currTab == 'classify' ? true:false}">商品类目</a>
					<a href="javascript:void(0);" @click="tab('brand')" class="tab-link"
					:class="{_active:currTab == 'brand' ? true:false}">品牌</a>
				</div>
					<div class="tabs-content">
						<div id="tab1" class="tab _active" v-show="currTab == 'classify'">
							<ul class="list-classify">
								<li>
									<a href="${ctx}/m/list?cata=1" flex=" cross:center main:justify" :class="[cata==1? '_active':'']">
										<div flex="cross:center" class="item">
											<i class="icon icon-side-01">icon</i> 生活电器
										</div>
										<svg class="svg-arrow"><use xlink:href="#svg-arrow"/></svg>
									</a>
								</li>
								<li>
									<a href="${ctx}/m/list?cata=2" flex=" cross:center main:justify" :class="[cata==2? '_active':'']">
										<div flex="cross:center" class="item">
											<i class="icon icon-side-02">icon</i> 厨房电器
										</div>
										<svg class="svg-arrow"><use xlink:href="#svg-arrow"/></svg>
									</a>
								</li>
								<li>
									<a href="${ctx}/m/list?cata=3" flex=" cross:center main:justify" :class="[cata==3? '_active':'']">
										<div flex="cross:center" class="item">
											<i class="icon icon-side-03">icon</i> 个护电器
										</div>
										<svg class="svg-arrow"><use xlink:href="#svg-arrow"/></svg>
									</a>
								</li>
								<li>
									<a href="${ctx}/m/list?cata=4" flex=" cross:center main:justify" :class="[cata==4? '_active':'']">
										<div flex="cross:center" class="item">
											<i class="icon icon-side-04">icon</i> 家居生活
										</div>
										<svg class="svg-arrow"><use xlink:href="#svg-arrow"/></svg>
									</a>
								</li>
								<li>
									<a href="${ctx}/m/list?cata=5" flex=" cross:center main:justify" :class="[cata==5? '_active':'']">
										<div flex="cross:center" class="item">
											<i class="icon icon-side-05">icon</i> 厨房用品
										</div>
										<svg class="svg-arrow"><use xlink:href="#svg-arrow"/></svg>
									</a>
								</li>
								<li>
									<a href="${ctx}/m/list?cata=6" flex=" cross:center main:justify" :class="[cata==6? '_active':'']">
										<div flex="cross:center" class="item">
											<i class="icon icon-side-06">icon</i> 商旅箱包
										</div>
										<svg class="svg-arrow"><use xlink:href="#svg-arrow"/></svg>
									</a>
								</li>
								<li>
									<a href="${ctx}/m/list?cata=7" flex=" cross:center main:justify" :class="[cata==7? '_active':'']">
										<div flex="cross:center" class="item">
											<i class="icon icon-side-07">icon</i> 运动户外
										</div>
										<svg class="svg-arrow"><use xlink:href="#svg-arrow"/></svg>
									</a>
								</li>
								<li>
									<a href="${ctx}/m/list?cata=8" flex=" cross:center main:justify" :class="[cata==8? '_active':'']">
										<div flex="cross:center" class="item">
											<i class="icon icon-side-08">icon</i> 电子数码
										</div>
										<svg class="svg-arrow"><use xlink:href="#svg-arrow"/></svg>
									</a>
								</li>
								<li>
									<a href="${ctx}/m/list?cata=9" flex=" cross:center main:justify" :class="[cata==9? '_active':'']">
										<div flex="cross:center" class="item">
											<i class="icon icon-side-09">icon</i> 汽车用品
										</div>
										<svg class="svg-arrow"><use xlink:href="#svg-arrow"/></svg>
									</a>
								</li>
								<li>
									<a href="${ctx}/m/list?cata=10" flex=" cross:center main:justify" :class="[cata==10? '_active':'']">
										<div flex="cross:center" class="item">
											<i class="icon icon-side-10">icon</i> 母婴儿童
										</div>
										<svg class="svg-arrow"><use xlink:href="#svg-arrow"/></svg>
									</a>
								</li>
								<li>
									<a href="${ctx}/m/list?cata=11" flex=" cross:center main:justify" :class="[cata==11? '_active':'']">
										<div flex="cross:center" class="item">
											<i class="icon icon-side-11">icon</i> 时尚精品
										</div>
										<svg class="svg-arrow"><use xlink:href="#svg-arrow"/></svg>
									</a>
								</li>
								<li>
									<a href="${ctx}/m/list?cata=12" flex=" cross:center main:justify" :class="[cata==12? '_active':'']">
										<div flex="cross:center" class="item">
											<i class="icon icon-side-12">icon</i> 食品卡券
										</div>
										<svg class="svg-arrow"><use xlink:href="#svg-arrow"/></svg>
									</a>
								</li>
								<li>
									<a href="${ctx}/m/list?cata=13" flex=" cross:center main:justify" :class="[cata==13? '_active':'']">
										<div flex="cross:center" class="item">
											<i class="icon icon-side-13">icon</i> 虚拟商品
										</div>
										<svg class="svg-arrow"><use xlink:href="#svg-arrow"/></svg>
									</a>
								</li>
							</ul>
						</div>
						<!--/.tab-->
						<div id="tab2" class="tab _active" v-show="currTab == 'brand'">
							<!--<p>This is tab 2 content</p>-->
							<div flex="box:last" class="brand-box">
								<div class="brand-list">
									<div v-for="item in brandLetter">
										<div :id="['_'+item.letter]" class="list-title" >{{item.letter}}</div>
										<ul class="list-content">
											<li><a href="javascript:;" class="_ellipsis" v-for="list in item.brand" :class="[list.active?'_active':'']" @click="chooseBrand($index,item.letter,list.id)">{{list.en}}    {{list.name}}</a></li>
										</ul>
									</div>				
								</div>
								<div class="brand-letter">
									<svg class="svg-search"><use xlink:href="#svg-search"/></svg>
									<a :href="[item.href]" class="item-letter" v-for="item in brandLetter">{{item.letter}}</a>
								</div>
							</div>
							<div flex="box:mean" class="bar-fixed-bottom side-button-group">
								<a href="javascript:;" class="btn-normal _dark " @click="reset">重置</a>
								<a href="javascript:;" class="button-confirm " 
								:class="[sure?'_red':'']" @click="go">确定</a>

							</div>
						</div>
						<!--/.tab-->
					</div>
					<!--/.tabs-content-->
				</div>
				<!--/.tabs-->
			</div>
			<!--/.bar-side-->
		</div>
		<!--/.bar-side-box-->
	<confirm-window :confirm="confirm"></confirm-window>
</template>

<template id="goback">
	<header flex="cross:center box:justify" class="bar bar-nav bar-fixed-top">
	    	<a href="javascript:;"  flex="cross:center" class="bar-item" @click="goBack">
				<svg class="svg-arrow _left"><use xlink:href="#svg-arrow"/></svg>
			</a>
			<div class="bar-item">
				{{title}}
			</div>
			<a href="${ctx}/m/message" flex="cross:center" class="bar-item" @click="go">
				<svg class="svg-wish"><use xlink:href="#svg-wish"/></svg>
				<span class="badge _red" v-if="none">{{num}}</span>
			</a>
		</header> 
</template>
<!-- 弹窗 -->
<template id="confirm">
	<div class="modal-overlay" :class="[confirm.show ? '_in':'']">
		
	</div>
	<!--class 「_in」 显示提示框-->
	<div class="modal _radius" :class="[confirm.show ? '_in':'']">
		<div class="modal-inner">
			<div class="modal-text">{{confirm.text}}<br/>
			</div>
		</div>
		<div flex="box:mean" class="modal-button-box ">
			<span class="modal-button" @click="cancel" v-show="confirm.cancalShow">取消</span>
			<span class="modal-button _red" @click="ok">确定</span>
		</div> 
	</div>
</template>
<!-- 返回顶部 -->
<template id="goTop">
	<a href="#" class="return-top" @click="scrollTop(top)"><i class="icon icon-return-top">icon</i></a>
</template>