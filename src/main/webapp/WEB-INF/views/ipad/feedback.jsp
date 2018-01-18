<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html lang="zh-cn">

<head>
    <title>留言</title>
    <script src="${ctx}/static/m/lib/swiper.min.js?v=ea38b0f7"></script>
    <script src="${ctx}/static/m/ipadjs/feedback.js?v=ea38b0f7"></script>
</head>

<body>

    <!--<div class="modal-overlay _in"></div>-->
    <div class="wrap wrap-blur wrap-ipad">
        <%@ include file="/WEB-INF/views/ipad/_head_includeback_fragment.jsp" %>

        <!--/.bar-nav-->
        <div class="content">
            <div class="message-content">
                <ul class="breadcrumbs">
                    <li><a href="${ctx}/ipad">首页</a></li>
                    <li><a href="javascript:history.go(-1);">上一级</a></li>
                    <li><span>留言</span></li>
                </ul>
                <div class="form-box bg-white">
                    <div flex="cross:center main:center" class="form-text">
                        <div data-feedback="noprotip" class="disN">
                            <p>亲，您可以留下联系方式或添加商品需求！<a href="${ctx}/ipad" class="links">去添加商品</a></p>
                        </div>
                        <div data-feedback="hasprotip" class="disN">
                            <p>亲，您已选定 <em class="text-red" data-wish="_count">0</em> 件心仪商品，</p>
                            <p>留下您的信息，让我们联系您，为您制定一个专属商品需求方案。</p>
                        </div>
                    </div>
                    <form class="feedback-form" data-feedback="form">
                        <div class="form-width">
                            <div flex=" " class="item">
                                <div flex-box="0" class="item-label">
                                    <em>*</em>姓名:
                                </div>
                                <!--注意： form表单的错误提示信息 class 「 item-error 」 显示-->
                                <div flex-box="1" class="item-input-box">
                                    <div class="item-input _input-border _input-white">
                                        <input type="text" placeholder="请输入您的姓名" id="name" name="name">
                                    </div>

                                    <em class="form-tips _error disN" id="nametip">请填写您的姓名</em>
                                </div>
                            </div>

                            <div flex=" " class="item">
                                <div flex-box="0" class="item-label">
                                    <em>*</em>电话:
                                </div>
                                <div flex-box="1" class="item-input-box">
                                    <div class="item-input _input-border _input-white">
                                        <input type="tel" maxlength="20" placeholder="请输入您的电话号码" id="mobile" name="mobile">
                                    </div>
                                    <!--注意： form表单的错误提示信息 class 「 disB 」 显示-->
                                    <em class="form-tips _error disN" id="mobiletip">请输入正确的电话号码</em>
                                </div>
                            </div>
                            <div flex=" " class="item">
                                <div flex-box="0" class="item-label">
                                    邮箱:
                                </div>
                                <div flex-box="1" class="item-input-box">
                                    <div class="item-input _input-border _input-white">
                                        <input type="text" placeholder="请输入您的邮箱" id="email" name="email">
                                    </div>
                                    <em class="form-tips _error disN" id="emailtip">请输入有效的邮箱地址</em>
                                </div>
                            </div>
                            <div flex=" " class="item">
                                <div flex-box="0" class="item-label">
                                    <em>*</em>公司名称:
                                </div>
                                <div flex-box="1" class="item-input-box">
                                    <div class="item-input _input-border _input-white">
                                        <input type="text" placeholder="请输入您的公司名称" id="companyname" name="companyname">
                                    </div>
                                    <em class="form-tips _error disN" id="companynametip">请填写您的公司名称</em>
                                </div>
                            </div>
                            <!--部门为其它的时候添加 item-other-->
                            <div flex=" " class="item">
                                <div flex-box="0" class="item-label">
                                    所在部门:
                                </div>
                                <div flex-box="1" class="item-input-box">
                                    <div class="item-input _input-border _input-white">
                                        <select name="part">
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
                            <div flex=" " class="item disN" data-feedback="otherreason">
                                <div flex-box="0" class="item-label">
                                </div>
                                <div flex-box="1" class="item-input-box">
                                    <div class="item-input _input-border _input-white">
                                        <input type="text" placeholder="其它" name="otherreason">
                                    </div>
                                    <em class="form-tips _error disN">其它部门不能为空</em>
                                </div>

                            </div>
                            <div flex=" " class="item">
                                <div flex-box="0" class="item-label">
                                    留言:
                                </div>
                                <div flex-box="1" class="item-input-box">
                                    <div class="item-input _input-border _input-white">
                                        <textarea name="" id="" cols="30" rows="3" placeholder="请输入您中意的品类或品牌信息" name="leaveword"></textarea>
                                    </div>
                                    <em class="form-tips _error disN">留言不能为空</em>
                                </div>
                            </div>
                            <div class="button-line">
                                <!--注意 按钮不可用状态 class 「 _gray _disabled 」 可用 class [_red]-->
                                <button class="btn-normal _gray _disabled" type="button" data-feedbak="ok">提交</button>
                                <button class="btn-normal _dark" type="button" data-feedbak="reset">重置</button>
                            </div>
                        </div>
                    </form>
                </div>
                <!--/.form-box-->
            </div>
            <div class="column-title _line" data-feedback="selectedpro">已选商品 <em class="font13">共</em><em class="text-red" data-wish="_count">0</em><em class="font13">件</em></div>
            <div class="swiper-container swiper-container-love" data-feedback="selectedpro" style="margin: 0 .2rem;">
                <div class="swiper-button-prev swiper-button-white disN" data-feedback="prev"></div>
                <div class="swiper-button-next swiper-button-white" data-feedback="next"></div>

                <div class="swiper-wrapper" data-feedback="choosepro">
                    <!--中意的宝贝-->
                    <!-- <a href="${ctx}/ipad/follow" class="swiper-slide">
                        <div style="height: 1.54rem;" flex="main:center cross:center" class="item-more ">
                            查看全部
                        </div>
                    </a> -->
                </div>
                <!--/.swiper-wrapper-->
            </div>
            <!--/.swiper-container-love-->
            <div class="column-title">推荐商品</div>
            <section flex="main:center" class="flex-wrap col-4 list-love" data-feedback="recommendpro">
                <!--推荐商品展示区-->
            </section>
            <!--/.col-4-->
        </div>
        <!--/.content-->
    </div>
    <!--/.wrap-->

    <!--class 「_in」 显示弹层背景-->
    <div class="modal-overlay" data-feedback="msg">
        <!--class 「_in」 显示提示框-->
        <div class="modal _radius" data-feedback="msg">
            <div class="modal-inner">
                <div class="modal-text" data-feedback="tip">您的留言已提交成功，<br/> 我们会尽快与您联系。</div>
            </div>
            <div flex="box:mean" class="modal-button-box ">
                <!-- <span class="modal-button">取消</span> -->
                <span class="modal-button _red" data-feedback="tipbtn">确定</span>
            </div>
        </div>
    </div>

<script id="feedbacktemplate" type="text/mustache">
    {{#choose}}
        {{#list}}
            <div class="swiper-slide">
                <div class="list-block item-goods _active" data-recommend="item">
                    <a href="${ctx}/product/{{id}}/ipad" flex="main:center" class="item-media">
                        <img class="lazy" data-original="{{loadableMainImgUrl}}">
                    </a>
                    <a href="${ctx}/product/{{id}}/ipad" class="item-inner">
                        <div class="item-title _ellipsis">{{name}}</div>
                        <span class="item-price">市场价：<em>¥{{marketPrice}}</em></span>
                    </a>
                    <div class="click-heart" data-list="wish" data-id="{{id}}">
                        <svg class="svg-heart "><use xlink:href="#svg-heart"/></svg>
                    </div>
                </div>
            </div>
        {{/list}}
        {{#morethan}}
        <a href="${ctx}/ipad/follow" class="swiper-slide">
            <div flex="main:center cross:center" class="item-more ">
                查看更多
            </div>
        </a>
        {{/morethan}}
    {{/choose}}
    {{#recommend}}
        {{#list}}
        <div class="list-block item-goods {{isWish}}" data-recommend="item">
            <a href="${ctx}/product/{{id}}/ipad" flex="main:center" class="item-media">
                <img data-original="{{loadableMainImgUrl}}">
            </a>
            <a href="${ctx}/product/{{id}}/ipad" class="item-inner">
                <div class="item-title _ellipsis">{{name}}</div>
                <span class="item-price">市场价：<em>¥{{marketPrice}}</em></span>
            </a>
            <div class="click-heart" data-list="wish" data-id="{{id}}">
                <svg class="svg-heart "><use xlink:href="#svg-heart"/></svg>
            </div>
        </div>
        {{/list}}
        <a href="${ctx}/ipad/recommend" class="list-block item-goods">
            <div style="height: 2.14rem;" flex="main:center cross:center" class="item-more ">
                查看更多
            </div>
        </a>
        <div class="item-goods list-block-placeholder"></div>
        <div class="item-goodslist-block-placeholder"></div>
        <div class="item-goods list-block-placeholder"></div>
        <div class="item-goods list-block-placeholder"></div>
        <div class="item-goods list-block-placeholder"></div>
    {{/recommend}}

</script>
</body>
</html>