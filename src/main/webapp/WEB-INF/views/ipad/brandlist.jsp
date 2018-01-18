<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html lang="zh-cn">

<head>
    <title>品牌</title>
    <script src="${ctx}/static/m/ipadjs/brandlist.js?v=ea38b0f7"></script>
</head>

<body>

    <!--<div class="modal-overlay _in"></div>-->
    <div class="wrap wrap-blur wrap-ipad">
        <%@ include file="/WEB-INF/views/ipad/_head_includeback_fragment.jsp" %>

        <%@ include file="/WEB-INF/views/ipad/_head_classify_fragment.jsp" %>

        <!--/.bar-nav-->
        <div class="content">
            <div class="message-content filter-box filter-brand-single">
                <ul class="breadcrumbs">
                    <li><a href="javascript:;">首页</a></li>
                    <li><span>品牌</span></li>
                </ul>
                <!--
                        注意： bg-white 随屏置顶添加class fixed
                     -->
                <div class="bg-white" data-brandlist="dynamic">
                     <div flex="cross:center" class="filter filter-letter _show" data-brandlist="letters">
                        <span>按字母筛选</span>
                    </div>
                    <!--/.filter-letter-->
                    <!--
                        注意： brand-select 显示所选择的品牌请加class disB
                     -->
                    <div class="brand-select disN" data-brandlist="choosebrand">
                        <div class="text">你选取的品牌有</div>
                        <div flex class="flex-wrap" data-brandlist="choosecontent">
                            <!--选择的品牌展示区-->
                        </div>
                        <div class="text-center button-brand">
                            <!--注意 按钮不可用状态 class 「 _gray _disabled 」 -->
                            <button class="btn-normal _red" data-brandlist="ok">确定</button>
                            <button class="btn-normal _dark" data-brandlist="reset">重置</button>
                        </div>
                    </div>
                </div>
                    <div class="bg-white">
                    <div class="filter-brand-box" data-brandlist="brandcontainer">
                        <!--所有品牌展示区-->
                    </div>
                    <!--/.filter-brand-boxs-->
                </div>
                <!--/.filter-letter-box -->
            </div>
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
                <span class="modal-button _red" data-brandlist="msgok">确定</span>
            </div>
        </div>
    </div>

<script id="brandlisttemplate" type="text/mustache">
    {{#choosebrand}}
        <div class="tag-solid _gray" data-brandlist="delparent">
             {{name}}
            <i class="icon icon-close-normal" data-brandlist="del" data-id="{{id}}">icon</i>
        </div>
    {{/choosebrand}}
    {{#letters}}
        <a class="item" href="javascript:void(0);" data-letter="{{letter}}" id="___{{letter}}">{{letter}}</a>
    {{/letters}}
    {{#brands}}
        <div class="brand-list-single" id="letter-{{letter}}">
            <div class="list-title" data-view="letter" data-letter="{{letter}}">{{letter}}</div>
            <div flex="main:justify" class="filter flex-wrap list-content">
                {{#brand}}
                <div class="item-fiter _ellipsis" id="{{id}}" data-id="{{id}}" data-name="{{name}}" data-brandlist="choosecurrbrand">{{en}} {{name}}</div>
                {{/brand}}
            </div>
        </div>
    {{/brands}}
</script>
</body>
</html>