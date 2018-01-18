<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html lang="zh-cn">
<head>
    <title>全部品牌</title>
    <script src="${ctx}/static/pc/js/brandlist.js?v=ea38b0f7"></script>
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
                        <li class="active">品牌</li>
                    </ol>
                </div>
                <!-- /.tab-pane -->
                <div class="brand-content clearfix">
                <!-- 随屏置顶 添加fixed -->
                    <div class="brand-box" data-brandlist="dynamic">
                        <div class="brand-title">全部品牌</div>
                        <ul class="brand-sort clearfix" data-brandlist="letters">
                            <li>按字母筛选：</li>
                        </ul>
                        <!-- 隐藏 disN -->
                        <div class="brand-list choose disN" data-brandlist="choosebrand">
                            <p>您选取的品牌有</p>
                            <ul class="clearfix">
                            </ul>
                            <div class="item-button">
                                <a href="javascript:void(0)" class=" btn btn-red mr20" data-brandlist="ok">确定</a><a href="javascript:void(0)" class=" btn btn-dark" data-brandlist="reset">重置</a>
                            </div>
                        </div>
                    </div>
                    <div class="brand-box">
                        <div class="brand-item-box" data-brandlist="brandcontainer">
                            <!--品牌内容展示区-->
                        </div>
                    </div>
                </div>
                <!-- /.brand-content -->
            </div>
            <!--/.content-->
            <div class="cnzz"><script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1261204796'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s11.cnzz.com/z_stat.php%3Fid%3D1261204796%26show%3Dpic' type='text/javascript'%3E%3C/script%3E"));</script></div>        
            </div>
        <!--/.main-->
        <!-- 右侧悬浮状态 -->
        <%@ include file="/WEB-INF/views/pc/_right_nav_fragment.jsp" %>
        <!-- /#floating_window -->
        </div>
    <!--/.wrap -->
    <!-- 弹出层 -->
    <div id="winBg" data-brandlist="tip"></div>
    <div class="popup-div" data-brandlist="tip">
        <div class="popup-content">
            <p>亲，最多只可以选择20个品牌呦！</p>
        </div>
        <div class="btn-area"><a href="javascript:void(0)" class="btn-confirm" data-brandlist="msgok">确定</a></div>
    </div>
<script id="brandlisttemplate" type="text/mustache">
    {{#choosebrand}}
        <li> <a href="javascript:void(0)">{{name}}<i class="icon icon-del-ch" data-brandlist="del" data-id="{{id}}">icon</i></a></li>
    {{/choosebrand}}
    {{#letters}}
        <li> <a href="javascript:void(0);" data-letter="{{letter}}" id="___{{letter}}">{{letter}}</a></li>
    {{/letters}}
    {{#brands}}
        <div class="brand-item clearfix" id="letter-{{letter}}">
            <div class="first-letter" data-view="letter" data-letter="{{letter}}">
                {{letter}}
            </div>
            <div class="name-list">
                {{#row}}
                <ul>
                    {{#col}}
                    <li><a id="{{id}}" data-id="{{id}}" data-name="{{name}}" href="javascript: void(0)">{{name}}</a></li>
                    {{/col}}
                </ul>
                {{/row}}
            </div>
        </div>
    {{/brands}}
</script>
</body>
</html>
