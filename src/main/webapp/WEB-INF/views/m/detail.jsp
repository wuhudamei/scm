<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!doctype html>
<html lang="zh-cn">
<head>
    <title>详情页</title>
    <link rel="stylesheet" type="text/css" href="/static/m/css/style.css?v=ea38b0f7" />
	<link rel="stylesheet" type="text/css" href="/static/m/css/swiper.min.css?v=ea38b0f7" />
</head>
<body>
	<div class="wrap wrap-blur wrap-details" id="detail" v-cloak>
    <%@ include file="/WEB-INF/views/m/component.jsp" %>
    <%@ include file="/WEB-INF/layouts/_svg.jsp" %>
    <div v-show="isTop">
        <go-top :top="top"></go-top>
    </div>
    <!-- <go-back :title="title" :id="[top]" :num="num"></go-back> -->
		<div class="content" >
			<!-- Swiper -->
			<div class="swiper-container swiper-container-details" :id="[top]">
				<div class="swiper-wrapper">
                    <div class="swiper-slide">
                        <img src="${product.loadableMainImgUrl}">
                    </div>
                    <c:forEach var="subImg" items="${product.loadableSubImgUrls}" >
                    <div class="swiper-slide">
                        <img src="${subImg}" alt="">
                    </div>
                  </c:forEach>
				</div>
				<!-- Add Pagination -->
				<div class="swiper-pagination"></div>
				<i class="icon-back " style="background: #dddddd;" @click="goBack" :class="[backActive?'_active':'']">
					<svg class="svg-arrow _left"><use xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="#svg-arrow"></use></svg>
				</i>
				<svg class="svg-heart" @click="choose('${product.id}')" :class="[active?'_active':'']"><use xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="#svg-heart"></use></svg>
			</div>
			<div class="list-block details-box">
				<div class="item-inner bg-white">
					<div flex="main:justify cross:center">
						<div class="item-title _ellipsis2">${product.name}</div>
						<a href="${ctx}/m/message" class="item-wish" id="fly">
							<svg class="svg-wish"><use xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="#svg-wish"></use></svg>
							<span class="badge _red"  v-show="none">{{num}}</span>
						</a>
					</div>
					<div class="item-price"><em class="font18">市场价：¥${product.marketPrice}</em></div>
					<div class="sku">
						<div class="item-subtitle">型号：${product.type}</div>
						<div class="item-subtitle">材质：${product.material}</div>
						<div class="item-subtitle">颜色：${product.color}</div>
					</div>
					<!--/.sku-->
				</div>
		    </div>
			<!--/.details-box-->
			<div class="details-content">
				<div class="details-tit">商品详情</div>
				${product.detail}
			</div>
	</div> <!--/.wrap-->
    <script src="${ctx}/static/m/js/detail.js?v=ea38b0f7"></script>
    <script>
        var swiper = new Swiper('.swiper-container-details', {
            pagination: '.swiper-pagination',
            paginationClickable: true,
            autoplay: 2000
        });
    </script>
</body>
</html>