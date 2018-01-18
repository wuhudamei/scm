<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="zh-cn">
<head>
    <title>详情页</title>
    <script src="${ctx}/static/m/lib/swiper.min.js?v=ea38b0f7"></script>
    <script src="${ctx}/static/m/ipadjs/detail.js?v=ea38b0f7"></script>
</head>
<body>

    <div class="wrap wrap-blur wrap-ipad wrap-details">
        <%@ include file="/WEB-INF/views/ipad/_head_includeback_fragment.jsp" %>

        <!--/.bar-nav-->
        <div class="content content-details content-space ">
            <ul class="breadcrumbs">
                <li><a href="${ctx}/ipad">首页</a></li>
                <li><a href="javascript:history.go(-1)">返回上一级</a></li>
                <li><span>${product.name}</span></li>
            </ul>
            <!-- Swiper -->
            <div class="swiper-container swiper-container-details">
                <div class="swiper-wrapper">
                    <div class="swiper-slide">
                        <img src="${product.loadableMainImgUrl}">
                    </div>
                    <c:forEach var="subImg" items="${product.loadableSubImgUrls}" >
                        <div class="swiper-slide">
                            <img src="${subImg}">
                        </div>
                    </c:forEach>
                </div>
                <!-- Add Pagination -->
                <div class="swiper-pagination"></div>
                <svg class="svg-heart" data-id="${product.id}" data-detail="add"><use xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="#svg-heart"></use></svg>
            </div>
            <div class="list-block details-box">
                <div class="item-inner bg-white">
                    <div flex="main:justify cross:center">
                        <div class="item-title _ellipsis2">${product.name}</div>

                        <a href="${ctx}/ipad/feedback" class="item-wish">
                            <svg class="svg-wish"><use xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="#svg-wish"></use></svg>
                            <span class="badge _red" data-wish="_count">0</span>
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
</body>
</html>