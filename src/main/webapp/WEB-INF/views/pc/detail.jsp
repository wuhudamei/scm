<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="zh-cn">
<head>
    <title>${product.type}-${product.name}</title>
    <script src="${ctx}/static/pc/js/detail.js?v=ea38b0f7"></script>
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
        <div class="main" style="">
            <div class="content">
                <div class="tab-pane">
                    <ol class="breadcrumb">
                        <li><a href="${ctx}/">首页 > </a></li>
                        <li><a href="javascript:history.go(-1)">上一级 > </a></li>
                        <li class="active">详情页</li>
                    </ol>
                </div>
                <!-- /.tab-pane -->
                <div class="product-detail clearfix">
                    <div class="view-pic fl clearfix">
                        <div class="switch-img fl">
                            <ul data-detail="imgList">
                              <c:forEach var="subImg" items="${product.loadableSubImgUrls}" >
                                <li class=""><img src="${subImg}" width="98" height="98"></li>
                              </c:forEach>
                            </ul>
                        </div>
                        <div class="main-img fl"><img data-detail="bigImg" src="${product.loadableMainImgUrl}" width="328" height="328"></div>
                    </div>
                    <div class="view-content fl">
                        <div class="item-name">${product.name}
                        </div>
                        <div class="item-state">
                            <p>市场价：<span class="item-price">${product.marketPrice}</span></p>
                            <p>型号：${product.type}</p>
                            <p>材质：${product.material}</p>
                            <p>颜色：${product.color}</p>
                        </div>
                        <div class="item-button">
                            <a href="javascript:void(0)" class=" btn" data-id="${product.id}" data-detail="add">添加至心愿单</a><a href="${ctx}/feedback" class=" btn btn-dark">去心愿单</a>
                        </div>
                    </div>
                    <div class="view-detail fl">
                        <p>${product.detail}</p>
                    </div>
                </div>
                <!-- /.product-detail -->
            </div>
            <!--/.content-->
           <div class="cnzz"><script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1261204796'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s11.cnzz.com/z_stat.php%3Fid%3D1261204796%26show%3Dpic' type='text/javascript'%3E%3C/script%3E"));</script></div>
        </div>
        <!--/.main-->

        <%@ include file="/WEB-INF/views/pc/_right_nav_fragment.jsp" %>
    </div>
    <!--/.wrap -->
</body>
</html>
