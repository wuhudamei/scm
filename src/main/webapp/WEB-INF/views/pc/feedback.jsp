<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html lang="zh-cn">

<head>
    <title>留言页</title>
    <script src="${ctx}/static/pc/js/swiper-2.7.0.min.js?v=ea38b0f7"></script>
    <script src="${ctx}/static/pc/js/feedback.js?v=ea38b0f7"></script>
</head>

<body>
    <div class="wrap">
        <div class="header">
            <%@ include file="/WEB-INF/views/pc/_head_topbar_fragment.jsp" %>
            <div class="header-con">
                <%@ include file="/WEB-INF/views/pc/_head_classify_fragment.jsp" %>
            </div>
            <!--/.classify-menu-->
        </div>
        <!--/.header-->
        <div class="main">
            <div class="content">
                <div class="tab-pane">
                    <ol class="breadcrumb">
                        <li><a href="${ctx}/">首页 > </a></li>
                        <li><a href="javascript:history.go(-1)">上一级 > </a></li>
                        <li class="active">留言</li>
                    </ol>
                </div>
                <div class="feedback">
                    <div class="feedback-content">
                        <div class="feedback-title">
                            <p data-feedback="hasprotip" class="disN">亲，您已选定 <em class="font24" data-wish="_count">0</em> 件心仪商品，</p>
                            <p data-feedback="hasprotip" class="disN">留下您的信息，让我们联系您，为您制定一个专属商品需求方案。</p>
                            <p data-feedback="noprotip" class="disN">亲，您可以留下联系方式或添加商品需求！去 <a href="${ctx}/">添加商品</a></p>
                        </div>
                        <form class="feedback-form" data-feedback="form">
                            <p><span><em>*</em>姓名</span>
                                <input type="text" class="inp-txt" maxlength="10" id="name" name="name">
                                <label for="name" class="error disN" id="nametip">请填写您的姓名</label>
                            </p>
                            <p><span><em>*</em>电话</span>
                                <input type="text" maxlength="20" class="inp-txt" id="mobile" name="mobile">
                                <label for="mobile" class="error disN" id="mobiletip">请输入正确的电话号码</label>
                            </p>
                            <p><span>邮箱</span>
                                <input type="text" class="inp-txt" id="email" name="email">
                                <label for="email" class="error disN" id="emailtip">请输入有效的邮箱地址</label>
                            </p>
                            <p><span><em>*</em>公司名称</span>
                                <input type="text" class="inp-txt" maxlength="25" id="companyname" name="companyname">
                                <label for="companyname" class="error disN" id="companynametip">请填写您的公司名称</label>
                            </p>
                            <p>
                                <span>所在部门</span>
                                <select class="select-list" name="part">
                                    <option value="">请选择部门</option>
                                    <option value="市场部">市场部</option>
                                    <option value="采购部">采购部</option>
                                    <option value="行政管理">行政管理</option>
                                    <option value="人力资源">人力资源</option>
                                    <option value="其它" >其它</option>
                                </select>
                            </p>
                            <p data-feedback="otherreason" class="disN"><span></span>
                                <input type="text" class="inp-txt" name="otherreason">
                            </p>
                            <p><span>留言</span>
                                <textarea cols="40" name="leaveword" rows="10" maxlength="250" class="inp-txt" placeholder="你好，请联系我"></textarea>
                            </p>
                            <p class="item-button">
                                <a href="javascript:void(0)" class=" btn btn-red mr20 btn-disabled" data-feedbak="ok">提交</a>
                                <a href="javascript:void(0)" class=" btn btn-dark" data-feedbak="reset">重置</a>
                            </p>
                        </form>
                    </div>
                    <div class="product-list choose disN" data-feedback="selectedpro">
                        <div class="item-title">
                            <span class="choose">已选商品 <i class="font18">共 <em class="font24" data-wish="_count">0</em> 件</i></span>
                        </div>
                        <div class="pro-choose clearfix">
                            <div class="pro-choose">
                                <div class="swiper-container clearfix">
                                    <div class="swiper-wrapper" data-feedback="choosepro">
                                        <!--已选商品-->
                                        <!-- <div class="swiper-slide item">
                                            <a href="${ctx}/follow" class="row"><span class="more-view" style="height: 213px;">查看更多</span></a>
                                        </div> -->
                                    </div>
                                    <!-- 按钮 -->
                                    <div class="swiper-button-prev disN" data-feedback="swiperpre"></div>
                                    <div class="swiper-button-next" data-feedback="swipernext"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="product-list" data-feedback="recommendpro">
                    <div class="item-title">
                        <span><i class="icon icon-unfollow recommend"></i>推荐商品</span>
                    </div>
                    <div class="clearfix"></div>
                    <!--推荐的商品-->

                </div>
                <!--/.product-list-->
            </div>
            <!--/.content-->
            <div class="cnzz"><script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1261204796'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s11.cnzz.com/z_stat.php%3Fid%3D1261204796%26show%3Dpic' type='text/javascript'%3E%3C/script%3E"));</script></div>
        </div>
        <!--/.main-->

        <%@ include file="/WEB-INF/views/pc/_right_nav_fragment.jsp" %>
    </div>
    <!--/.wrap -->
    <div id="winBg" data-feedback="msg"></div>
    <div class="popup-div" data-feedback="msg">
        <div class="popup-content">
            <!-- 提示语-->
            <p data-feedback="tip">您的留言已提交成功，我们会尽快与您取得联系。</p>
        </div>
        <div class="btn-area" data-feedback="tipbtn"><a href="javascript:void(0)" class="btn-confirm">确定</a></div>
    </div>
<script id="feedbacktemplate" type="text/mustache">
    {{#choose}}
        {{#list}}
            <div class="swiper-slide swiper-no-swiping item">
                <i class="icon icon-follow" data-list="wish" data-id="{{id}}">icon</i>
                <a class="row" href="${ctx}/product/{{id}}/pc?id={{id}}" target="_blank">
                    <img data-original="{{loadableMainImgUrl}}" class="lazy">
                    <div class="item-name">
                        <h1>{{name}}</h1>
                        <h2>市场价：<span class="pro-price">¥{{marketPrice}}</span></h2></div>
                </a>
            </div>
        {{/list}}
        {{#morethan}}
        <div class="swiper-slide item">
            <a href="${ctx}/follow" class="row"><span class="more-view" style="height: 213px;">查看更多</span></a>
        </div>
        {{/morethan}}
    {{/choose}}
    {{#recommend}}
        {{#list}}
        <div class="item {{isFour}}">
            <i class="icon {{isWish}}" data-list="wish" data-id="{{id}}">icon</i>
            <a class="row" href="${ctx}/product/{{id}}/pc?id={{id}}" target="_blank">
                <img class="lazy" data-original="{{loadableMainImgUrl}}">
                <div class="item-name">
                    <h1>{{name}}</h1>
                    <h2>市场价：<span class="pro-price">¥{{marketPrice}}</span></h2></div>
            </a>
        </div>
        {{/list}}
        <div class="item"><a href="${ctx}/recommend" class="row"><span class="more-view">查看更多</span></a></div>
    {{/recommend}}

</script>
</body>

</html>
