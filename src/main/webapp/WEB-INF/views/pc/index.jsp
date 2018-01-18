<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!doctype html>
<html lang="zh">
<head>
    <title>大诚若谷-商品展示平台</title>
    <script src="${ctx}/static/pc/js/swiper-2.7.0.min.js?v=ea38b0f7"></script>
    <script src="${ctx}/static/pc/js/index.js?v=ea38b0f7"></script>
</head>
<body>

    <div class="wrap">
        <div class="header">
            <%@ include file="/WEB-INF/views/pc/_head_topbar_fragment.jsp" %>
            <div class="header-con">
                <%@ include file="/WEB-INF/views/pc/_head_classify_fragment.jsp" %>

                <div class="banner">
                    <div class="swiper-container">
                        <div class="swiper-wrapper" data-index="swiperlist">
                            <div class="swiper-slide">
                                <a href=""><img src="${ctx}/static/pc/images/banner.jpg" alt="banner图" width="1000" height="220"></a>
                            </div>
                            <div class="swiper-slide">
                                <a href=""><img src="${ctx}/static/pc/images/banner.jpg" alt="banner图" width="1000" height="220"></a>
                            </div>
                            <div class="swiper-slide">
                                <a href=""><img src="${ctx}/static/pc/images/banner.jpg" alt="banner图" width="1000" height="220"></a>
                            </div>
                        </div>
                        <!-- 如果需要导航按钮 -->
                        <div class="swiper-button-prev"></div>
                        <div class="swiper-button-next"></div>
                    </div>
                    <!--/.swiper-container-->
                </div>
                <!-- /.banner -->
            </div>
            <!--/.classify-menu-->
        </div>
        <!--/.header-->
        <div class="content">
            <div class="index-list">
                <div class="index-list-pro">
                    <div class="index-list-box mb10">
                        <div class="index-list-img clearfix">
                            <div class="index-list-img clearfix">
                                <div class="fl mr10">
                                    <a href="${ctx}/brandlist" class="active disN" data-handle="out">
                                        <div class="pro-txt">
                                            <h1>品牌</h1>
                                            <h2>更多</h2></div>
                                    </a>
                                    <img src="${ctx}/static/pc/images/pro/pro-index-01.jpg" data-handle="over">
                                </div>
                                <div class="fl">
                                    <a href="${ctx}/list?cata=1" class="active disN" data-handle="out">
                                        <div class="pro-txt">
                                            <h1>生活电器</h1>
                                            <h2>更多</h2></div>
                                    </a>
                                    <img src="${ctx}/static/pc/images/pro/pro-index-02.jpg" data-handle="over">
                                </div>
                            </div>
                            <div class="fl mt10">
                                <div class="index-list-img clearfix">
                                    <div class="fl mr10">
                                        <a href="${ctx}/list?cata=3" class="active disN" data-handle="out">
                                            <div class="pro-txt">
                                                <h1>个护电器</h1>
                                                <h2>更多</h2></div>
                                        </a>
                                        <img src="${ctx}/static/pc/images/pro/pro-index-03.jpg" data-handle="over">
                                    </div>
                                    <div class="fl mr10">
                                        <a href="${ctx}/list?cata=2" class="active disN" data-handle="out">
                                            <div class="pro-txt">
                                                <h1>厨房用品</h1>
                                                <h2>更多</h2></div>
                                        </a>
                                        <img src="${ctx}/static/pc/images/pro/pro-index-04.jpg" data-handle="over">
                                    </div>
                                </div>
                                <div class="index-list-img clearfix">
                                    <div class="fl mt10">
                                        <a href="${ctx}/list?cata=6" class="active disN" data-handle="out">
                                            <div class="pro-txt">
                                                <h1>商旅箱包</h1>
                                                <h2>更多</h2></div>
                                        </a>
                                        <img src="${ctx}/static/pc/images/pro/pro-index-05.jpg" data-handle="over">
                                    </div>
                                </div>
                            </div>
                            <div class="fl mt10">
                                <a href="${ctx}/list?cata=4" class="active disN" data-handle="out">
                                    <div class="pro-txt">
                                        <h1>家居用品</h1>
                                        <h2>更多</h2></div>
                                </a>
                                <img src="${ctx}/static/pc/images/pro/pro-index-06.jpg" data-handle="over">
                            </div>
                        </div>
                    </div>
                    <div class="index-list-box ml10">
                        <div class="index-list-img">
                            <div>
                                <a href="${ctx}/list?cata=1" class="active disN" data-handle="out">
                                    <div class="pro-txt">
                                        <h1>厨房电器</h1>
                                        <h2>更多</h2></div>
                                </a>
                                <img src="${ctx}/static/pc/images/pro/pro-index-07.jpg" data-handle="over">
                            </div>
                            <div class="mt10">
                                <a href="${ctx}/list?cata=7" class="active disN" data-handle="out">
                                    <div class="pro-txt">
                                        <h1>运动户外</h1>
                                        <h2>更多</h2></div>
                                </a>
                                <img src="${ctx}/static/pc/images/pro/pro-index-08.jpg" data-handle="over">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="index-list-pro clearfix">
                    <div class="index-list-box mr10">
                        <div class="index-list-img fl mr10">
                            <div>
                                <a href="${ctx}/list?cata=8" class="active disN" data-handle="out">
                                    <div class="pro-txt">
                                        <h1>电子数码</h1>
                                        <h2>更多</h2></div>
                                </a>
                                <img src="${ctx}/static/pc/images/pro/pro-index-09.jpg" data-handle="over">
                            </div>
                        </div>
                        <div class="index-list-img fl">
                            <div class="mb10">
                                <a href="${ctx}/list?cata=9" class="active disN" data-handle="out">
                                    <div class="pro-txt">
                                        <h1>汽车用品</h1>
                                        <h2>更多</h2></div>
                                </a>
                                <img src="${ctx}/static/pc/images/pro/pro-index-10.jpg" data-handle="over">
                            </div>
                            <div>
                                <a href="${ctx}/list?cata=11" class="active disN" data-handle="out">
                                    <div class="pro-txt">
                                        <h1>时尚精品</h1>
                                        <h2>更多</h2></div>
                                </a>
                                <img src="${ctx}/static/pc/images/pro/pro-index-11.jpg" data-handle="over">
                            </div>
                        </div>
                    </div>
                    <div class="index-list-box">
                        <div class="index-list-img">
                            <div class="mb10">
                                <a href="${ctx}/list?cata=10" class="active disN" data-handle="out">
                                    <div class="pro-txt">
                                        <h1>母婴儿童</h1>
                                        <h2>更多</h2></div>
                                </a>
                                <img src="${ctx}/static/pc/images/pro/pro-index-12.jpg" data-handle="over">
                            </div>
                        </div>
                        <div class="index-list-img">
                            <div class="fl mr10">
                                <a href="${ctx}/list?cata=11" class="active disN" data-handle="out">
                                    <div class="pro-txt">
                                        <h1>食品卡券</h1>
                                        <h2>更多</h2></div>
                                </a>
                                <img src="${ctx}/static/pc/images/pro/pro-index-13.jpg" data-handle="over">
                            </div>
                            <div class="fl">
                                <a href="${ctx}/list?cata=13" class="active disN" data-handle="out">
                                    <div class="pro-txt">
                                        <h1>虚拟商品</h1>
                                        <h2>更多</h2></div>
                                </a>
                                <img src="${ctx}/static/pc/images/pro/pro-index-14.jpg" data-handle="over">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--/.content-->
        <div class="footer clearfix">
            <ul class="footer-con clearfix">
                <li class="first">
                    <h1>北京大诚若谷信息技术有限公司</h1>
                    <h2>北京市朝阳区广渠路3号竞园41-A（北京总部） 100124</h2>
                    <h2>Tel: (+8610)67719633 Fax: (+8610)67719622</h2>
                </li>
                <li class="hidden">
                    <a href="javascript:void(0);">帮助中心</a>
                </li>
                <li class="last"><img src="${ctx}/static/pc/images/QR-code.png"></li>
            </ul>
            <div class="cnzz"><script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1261204796'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s11.cnzz.com/z_stat.php%3Fid%3D1261204796%26show%3Dpic' type='text/javascript'%3E%3C/script%3E"));</script></div>
        </div>
        <!--/.footer-->


    </div>
    <!--/.wrap -->

<script id="indextemplate" type="text/mustache">
    {{#swiperList}}
        <div class="swiper-slide">
            <a href="{{link}}"><img src="{{imgUrl}}" width="1000" height="220"></a>
        </div>
    {{/swiperList}}
</script>

</body>
</html>