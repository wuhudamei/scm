<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html lang="zh-cn">
<head>
    <title>首页</title>
    <script src="${ctx}/static/m/lib/swiper.min.js?v=ea38b0f7"></script>
    <script src="${ctx}/static/m/ipadjs/index.js?v=ea38b0f7"></script>
</head>
<body>

    <div class="wrap wrap-blur wrap-ipad">
        <%@ include file="/WEB-INF/views/ipad/_head_index_fragment.jsp" %>

        <%@ include file="/WEB-INF/views/ipad/_head_classify_fragment.jsp" %>

        <div class="content">
            <!-- Swiper -->
            <div class="swiper-container swiper-container1">
                <div class="swiper-wrapper" data-index="swiperlist">
                    <!--轮播内容-->
                </div>
                <!-- Add Pagination -->
                <div class="swiper-pagination"></div>
            </div>

            <!--使用 flexbox 不规则布局-->
            <div class="banner-box">
                <div flex="box:mean">
                    <section class="banner-section">
                        <aside class="item">
                            <a href="${ctx}/ipad/brandlist"  flex="cross:center main:center" class="banner">
                                <img src="${ctx}/static/m/images/b-pinpai.png" alt="">
                            </a>
                        </aside>
                        <aside flex="box:mean" >
                            <a href="${ctx}/ipad/list?cata=3" flex="cross:center main:center" class="banner ">
                                <img src="${ctx}/static/m/images/b-gedian.png" alt="">
                            </a>
                            <a href="${ctx}/ipad/list?cata=2" flex="cross:center main:center" class="banner  ">
                                <img src="${ctx}/static/m/images/b-chuyong.png" alt="">
                            </a>
                        </aside>
                        <aside class="item">
                            <a href="${ctx}/ipad/list?cata=6"  flex="cross:center main:center" class="banner">
                                <img src="${ctx}/static/m/images/b-shangxiang.png" alt="">
                            </a>
                        </aside>
                    </section>
                    <section flex="box:mean" class="banner-section" >
                        <aside class="item aside-1">
                            <a href="${ctx}/ipad/list?cata=1" flex="cross:center main:center" class="banner ">
                                <img src="${ctx}/static/m/images/b-shengdian.png" alt="">
                            </a>
                            <a href="${ctx}/ipad/list?cata=4" flex="cross:center main:center" class="banner  ">
                                <img src="${ctx}/static/m/images/b-jiayong.png" alt="">
                            </a>
                        </aside>
                        <aside class="item aside-2">
                            <a href="${ctx}/ipad/list?cata=2" flex="cross:center main:center" class="banner ">
                                <img src="${ctx}/static/m/images/b-chudian.png" alt="">
                            </a>
                            <a href="${ctx}/ipad/list?cata=7" flex="cross:center main:center" class="banner  ">
                                <img src="${ctx}/static/m/images/b-yunhu.png" alt="">
                            </a>
                        </aside>
                    </section>
                </div>

                <div flex="box:mean">
                    <section flex="box:mean" class="banner-section" >
                        <aside class="item aside-1">
                            <a href="${ctx}/ipad/list?cata=8" flex="cross:center main:center" class="banner ">
                                <img src="${ctx}/static/m/images/b-dianshu.png" alt="">
                            </a>
                        </aside>
                        <aside class="item aside-2">
                            <a href="${ctx}/ipad/list?cata=9" flex="cross:center main:center" class="banner ">
                                <img src="${ctx}/static/m/images/b-qiyong.png" alt="">
                            </a>
                            <a href="${ctx}/ipad/list?cata=11" flex="cross:center main:center" class="banner  ">
                                <img src="${ctx}/static/m/images/b-shijing.png" alt="">
                            </a>
                        </aside>
                    </section>
                    <section class="banner-section" >
                        <aside class="item">
                            <a href="${ctx}/ipad/list?cata=10"  flex="cross:center main:center" class="banner">
                                <img src="${ctx}/static/m/images/b-muer.png" alt="">
                            </a>
                        </aside>
                        <aside flex="box:mean" >
                            <a href="${ctx}/ipad/list?cata=12" flex="cross:center main:center" class="banner ">
                                <img src="${ctx}/static/m/images/b-shika.png" alt="">
                            </a>
                            <a href="${ctx}/ipad/list?cata=13" flex="cross:center main:center" class="banner  ">
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
<script id="indextemplate" type="text/mustache">
    {{#swiperList}}
        <a href="{{link}}" class="swiper-slide">
            <img src="{{imgUrl}}">
        </a>
    {{/swiperList}}
</script>
</body>
</html>