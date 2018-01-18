<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!doctype html>
<html lang="zh-cn">
<head>
	<title>留言</title>
</head>
<body>
	<div class="wrap wrap-blur wrap-index" id="message" v-cloak>
        <go-back :title="title" :id="[top]" :num="num" :none='none'></go-back>
		<!--/.bar-nav-->
		<div class="content">
			<div flex="cross:center main:center" class="form-text">
				<div v-if="!selectedShow">
					<p>亲，您可以留下联系方式或添加商品需求！<a href="${ctx}/m" class="links">去添加商品</a></p>
				</div>
				<div v-if="selectedShow">
					<p>亲，您已选定 <em class="text-red">{{num}}</em> 件心仪商品，</p>
					<p>留下您的信息，让我们联系您，为您制定一个专属商品需求方案。</p>
				</div>
			</div>
			<div class="form-box bg-white">
				<div class="form-width">
					<div flex=" " class="item">
						<div flex-box="0" class="item-label">
							<em>*</em>姓名:
						</div>
						<!--注意： form表单的错误提示信息 class 「 item-error 」 显示-->
						<div flex-box="1" class="item-input-box" :class="[nameError?'item-error':'']">
							<div class="item-input _input-border _input-white">
								<input type="text" placeholder="请输入您的姓名" v-model="name">
							</div>

							<em class="form-tips _error">请填写您的姓名</em>
						</div>
					</div>

					<div flex=" " class="item">
						<div flex-box="0" class="item-label">
							<em>*</em>电话:
						</div>
						<div flex-box="1" class="item-input-box" :class="[phoneError?'item-error':'']">
							<div class="item-input _input-border _input-white">
								<input type="text" placeholder="请输入您的电话号码" v-model="phone" maxlength="20" @blur="phoneEp">
							</div>
							<!--注意： form表单的错误提示信息 class 「 disB 」 显示-->
							<em class="form-tips _error" :class="[phoneError?'disB':'disN']">请重新输入正确的电话号码</em>
						</div>
					</div>
					<div flex=" " class="item">
						<div flex-box="0" class="item-label">
							邮箱:
						</div>
						<div flex-box="1" class="item-input-box" :class="[emailError?'item-error':'']">
							<div class="item-input _input-border _input-white">
								<input type="text" placeholder="请输入您的邮箱" v-model="email" @blur="emailEp">
							</div>
							<em class="form-tips _error" :class="[emailError?'disB':'disN']">请输入有效的邮箱地址</em>
						</div>
					</div>
					<div flex=" " class="item">
						<div flex-box="0" class="item-label">
							<em>*</em>公司名称:
						</div>
						<div flex-box="1" class="item-input-box" :class="[companyError?'item-error':'']">
							<div class="item-input _input-border _input-white">
								<input type="text" placeholder="请输入您的公司名称" v-model="company" maxlength="25">
							</div>
							<em class="form-tips _error" :class="[companyError?'disB':'disN']">请填写您的公司名称</em>
						</div>
					</div>
					<div flex=" " class="item" :class="[department=='其它'?'item-other':'']">
						<div flex-box="0" class="item-label">
							所在部门:
						</div>
						<div flex-box="1" class="item-input-box">
							<div class="item-input _input-border _input-white">
								<select v-model="department">
									<option value="">请选择部门</option>
                                    <option value="市场部">市场部</option>
                                    <option value="采购部">采购部</option>
                                    <option value="行政管理">行政管理</option>
                                    <option value="人力资源">人力资源</option>
                                    <option value="其它" >其它</option>
								</select>
								<i class="triangle _down">triangle</i>
							</div>
							<em class="form-tips _error disN">错误提示</em>
						</div>
					</div>
					<div flex=" " class="item" v-if="department=='其它'">
					    <div flex-box="0" class="item-label">
						</div>
						<div flex-box="1" class="item-input-box">
							<div class="item-input _input-border _input-white">
								<input type="text" placeholder="" v-model='other'>
							</div>
							<em class="form-tips _error disN">错误提示</em>
						</div>
					</div>
					<div flex=" " class="item">
						<div flex-box="0" class="item-label">
							留言:
						</div>
						<div flex-box="1" class="item-input-box">
							<div class="item-input _input-border _input-white">
								<textarea name="" id="" cols="30" rows="3" placeholder="请输入您中意的品类或品牌信息" v-model="content" maxlength="250"></textarea>
							</div>
							<em class="form-tips _error disN">错误提示</em>
						</div>
					</div>
					<div class="button-line">
						<!--注意 按钮不可用状态 class 「 _gray _disabled 」 -->
						<button class="btn-normal " @click="submit" :class="[btnState?'_red':'_gray']">提交</button>
						<button class="btn-normal _dark" @click="reset">重置</button>
					</div>
				</div>
			</div>
			<!--/.form-box-->
			<div class="column-title _line" v-if="selectedShow">已选商品 <span class="text-red font10">共<em class="font10">{{num}}</em>件</span></div>
			<div class="swiper-container swiper-container-love " v-if="selectedShow">
                <div class="swiper-wrapper">
                    <div class="swiper-slide" v-for="item in selectedList" >
                        <div class="list-block item-goods " :class="[item.active?'_active':'']">
                            <div flex="main:center" class="item-media">
                            	<a href="/product/{{item.id}}/m" @click="go">
                                	<img  :src="item.loadableMainImgUrl" alt="商品图片" >
                                </a>
                            </div>
                            <div class="item-inner">
                                <div class="item-title _ellipsis font10">{{item.name}}</div>
                                <span class="item-price font7">市场价：<em class="font10">¥{{item.marketPrice}}</em></span>
                            </div>
                            <div class="click-heart">
                        <svg class="svg-heart " @click="cookieR(item,item.id,item.active,$index)"><use xlink:href="#svg-heart"/></svg>
                    </div>
                        </div>
                    </div>
					<a href="${ctx}/m/selected" class="swiper-slide" @click="go" v-show="selectedMoreShow">
						<div flex="main:center cross:center" class="item-more ">
							查看全部
						</div>
					</a>
				</div>
				<!--/.swiper-wrapper-->
			</div>
			<!--/.swiper-container-love-->
			<div class="column-title">推荐商品</div>
			<section flex="main:center" class="flex-wrap col-2 list-love">
			<!--注意：选中红心 请在list-block上加class「_active」-->
                <div class="list-block item-goods" v-for="item in likeList" :class="[item.active?'_active':'']">
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
				<!--
					注意： 以下的空div是占位的， 一行想要显示n个数据 ,就要预留n个占位
				 -->
				<div class="item-goods list-block-placeholder"></div>
				<div class="item-goodslist-block-placeholder"></div>
				<div class="item-goods list-block-placeholder"></div>
				<div class="item-goods list-block-placeholder"></div>
				<div class="item-goods list-block-placeholder"></div>
				<a flex="main:center cross:center" class="link-more" href="${ctx}/m/recommend" @click="go">查看更多 》</a>
			</section>
			<!--/.col-2-->
		</div>
		<!--/.content-->
        <div class="loading-page hidden">
        <svg class="icon-loading"><use xlink:href="#svg-loading"/></svg>
    </div>
    <confirm-window :confirm="confirm"></confirm-window>
	</div>
	<!--/.wrap-->
    <script src="${ctx}/static/m/js/message.js?v=ea38b0f7"></script>
    <script src="${ctx}/static/m/lib/swiper.min.js?v=ea38b0f7"></script>

</body>
</html>