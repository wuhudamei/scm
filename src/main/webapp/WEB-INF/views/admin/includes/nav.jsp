<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!--左侧导航开始-->
<nav id="nav" class="navbar-default navbar-static-side" role="navigation">
    <div class="nav-close">
        <i class="fa fa-times-circle"></i>
    </div>
    <div id="navUser" class="sidebar-collapse">
        <div class="nav-header">
            <div v-cloak v-show="user">
                <div class="dropdown profile-element">
                    <%--<span>--%>
                    <%--<img  :src="user.avatar || '/static/admin/img/avatar_60_60.png'"--%>
                    <%--alt="avatar" class="img-circle"/>--%>
                    <%--</span>--%>
                    <%--<a data-toggle="dropdown" class="dropdown-toggle" href="#">--%>
                    <%--<span class="clear">--%>
                    <%--<span class="block m-t-xs">--%>
                    <%--<strong v-text="user.name" class="font-bold"></strong>--%>
                    <%--</span>--%>
                    <%--<span v-text="user.position"--%>
                    <%--class="text-muted text-xs block">--%>
                    <%--<b class="caret"></b>--%>
                    <%--</span>--%>
                    <%--</span>--%>
                    <%--</a>--%>
                    <%--<ul class="dropdown-menu animated fadeInRight m-t-xs">--%>
                    <%--<li><a class="J_menuItem" href="form_avatar.html">修改头像</a></li>--%>
                    <%--<li><a class="J_menuItem" href="profile.html">个人资料</a></li>--%>
                    <%--<li><a class="J_menuItem" href="contacts.html">联系我们</a></li>--%>
                    <%--<li><a class="J_menuItem" href="mailbox.html">信箱</a></li>--%>
                    <%--<li class="divider"></li>--%>
                    <%--<li><a href="login.html">安全退出</a></li>--%>
                    <%--</ul>--%>
                    <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                   <span class="clear">
                       <span class="block m-t-xs"> <strong class="font-bold"><shiro:principal property="name"/></strong></span>
                   </span>
                    </a>
                </div>
                <div v-text="user.companyNickname" class="logo-element"></div>
            </div>
        </div>
        <!-- 左侧菜单 start-->


        <ul class="nav metismenu" id="sideMenu">
            <li id="homeMenu">
                <a href="/admin">
                    <i class="fa fa-home"></i>
                    <span class="nav-label">主页</span>
                </a>
            </li>

            <shiro:hasPermission name="user:menu">
                <li id="accountManage">
                    <a href="#">
                        <i class="fa fa fa-bar-chart-o"></i>
                        <span class="nav-label">账户管理</span>
                        <span class="fa arrow"></span>
                    </a>
                    <ul class="nav nav-second-level">
                        <shiro:hasPermission name="user:list">
                            <li id="userMenu">
                                <a href="/admin/user">
                                    <i class="fa fa-users"></i>
                                    <span class="nav-label">用户列表</span>
                                </a>
                            </li>
                        </shiro:hasPermission>
                    </ul>
                </li>
            </shiro:hasPermission>

            <shiro:hasPermission name="customer:menu">
                <li id="customer">
                    <a href="#">
                        <i class="fa fa fa-bar-chart-o"></i>
                        <span class="nav-label">客户管理</span>
                        <span class="fa arrow"></span>
                    </a>
                    <ul class="nav nav-second-level">
                        <shiro:hasPermission name="customer:list">
                            <li id="customerInfo">
                                <a href="/admin/customer">
                                    <i class="fa fa fa-bar-chart-o"></i>
                                    <span class="nav-label">客户列表</span>
                                </a>
                            </li>
                        </shiro:hasPermission>

                        <shiro:hasPermission name="contract:list">
                            <li id="contractInfo">
                                <a href="/admin/contract">
                                    <i class="fa fa fa-bar-chart-o"></i>
                                    <span class="nav-label">项目列表</span>
                                </a>
                            </li>
                        </shiro:hasPermission>
                    </ul>
                </li>
            </shiro:hasPermission>

            <shiro:hasPermission name="prepareorder:menu">
                <li id="prepareOrder">
                    <a href="/admin/prepareorder/list">
                        <i class="fa fa-edit"></i>
                        <span class="nav-label">预备单</span>
                    </a>
                </li>
            </shiro:hasPermission>

            <shiro:hasPermission name="indentorder:menu">
                <li id="order">
                    <a href="#">
                        <i class="fa fa fa-bar-chart-o"></i>
                        <span class="nav-label">订货单</span>
                        <span class="fa arrow"></span>
                    </a>
                    <ul class="nav nav-second-level">
                        <shiro:hasPermission name="indentorder:list">
                            <li id="indentOrder">
                                <a href="/admin/indentOrder/list">
                                    <i class="fa fa fa-bar-chart-o"></i>
                                    <span class="nav-label">订货单</span>
                                </a>
                            </li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="reconciliation:list">
                            <li id="reconciliationList">
                                <a href="/admin/indentOrder/reconciliationList">
                                    <i class="fa fa fa-bar-chart-o"></i>
                                    <span class="nav-label">对账列表</span>
                                </a>
                            </li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="indentorder:operatelogList">
                            <li id="operatorLog">
                                <a href="/admin/operatorLog">
                                    <i class="fa fa-edit"></i>
                                    <span class="nav-label">操作日志</span>
                                </a>
                            </li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="indentorder:viewUncheckedList">
                            <li id="accountChecking">
                                <a href="/admin/materialSupplierAccountChecking">
                                    <i class="fa fa-edit"></i>
                                    <span class="nav-label">未对账</span>
                                </a>
                            </li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="indentorder:viewcheckedList">
                            <li id="isAccountChecking">
                                <a href="/admin/isAccountChecking">
                                    <i class="fa fa-edit"></i>
                                    <span class="nav-label">已对账</span>
                                </a>
                            </li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="indentorder:reviewSizeList">
                            <li id="review">
                                <a href="/admin/review">
                                    <i class="fa fa-edit"></i>
                                    <span class="nav-label">复尺查询</span>
                                </a>
                            </li>
                        </shiro:hasPermission>
                    </ul>
                </li>
            </shiro:hasPermission>

            <shiro:hasPermission name="product:menu">
                <li id="applyManage">
                    <a href="#">
                        <i class="fa fa fa-bar-chart-o"></i>
                        <span class="nav-label">商品管理</span>
                        <span class="fa arrow"></span>
                    </a>

                    <ul class="nav nav-second-level">

                        <shiro:hasPermission name="store:list">
                            <li id="storeList">
                                <a href="/admin/product/store/list">
                                    <i class="fa fa fa-bar-chart-o"></i>
                                    <span class="nav-label">门店管理</span>
                                </a>
                            </li>
                        </shiro:hasPermission>

                        <shiro:hasPermission name="region_supplier:list">
                            <li id="declarationFormList">
                                <a href="/admin/product/supplier/region-supplier-list">
                                    <i class="fa fa fa-bar-chart-o"></i>
                                    <span class="nav-label">区域供应商</span>
                                </a>
                            </li>
                        </shiro:hasPermission>

                        <shiro:hasPermission name="product_supplier:list">
                            <li id="supplierList">
                                <a href="/admin/product/supplier/list">
                                    <i class="fa fa fa-bar-chart-o"></i>
                                    <span class="nav-label">商品供应商</span>
                                </a>
                            </li>
                        </shiro:hasPermission>

                        <shiro:hasPermission name="product:list">
                            <li id="productApplyList">
                                <a href="/admin/product">
                                    <i class="fa fa fa-bar-chart-o"></i>
                                    <span class="nav-label">商品列表</span>
                                </a>
                            </li>
                        </shiro:hasPermission>

                        <shiro:hasPermission name="sku:list">
                            <li id="skuList">
                                <a href="/admin/sku">
                                    <i class="fa fa fa-bar-chart-o"></i>
                                    <span class="nav-label">SKU列表</span>
                                </a>
                            </li>
                        </shiro:hasPermission>

                        <shiro:hasPermission name="product:edit">
                            <li id="addProductApply">
                                <a href="/admin/product/edit">
                                    <i class="fa fa fa-bar-chart-o"></i>
                                    <span class="nav-label">新增商品</span>
                                </a>
                            </li>
                        </shiro:hasPermission>

                        <shiro:hasPermission name="catalog:list">
                            <li id="catalogMenu">
                                <a href="/admin/catalog">
                                    <i class="fa fa fa-list"></i>
                                    <span class="nav-label">商品类目管理</span>
                                </a>
                            </li>
                        </shiro:hasPermission>

                        <shiro:hasPermission name="brand:list">
                            <li id="brandMenu">
                                <a href="/admin/brand">
                                    <i class="fa fa fa-tag"></i>
                                    <span class="nav-label">品牌管理</span>
                                </a>
                            </li>
                        </shiro:hasPermission>

                        <shiro:hasPermission name="product:import">
                            <li id="importMenu">
                                <a href="/admin/product/batchimport">
                                    <i class="fa fa fa-tag"></i>
                                    <span class="nav-label">商品导入</span>
                                </a>
                            </li>
                        </shiro:hasPermission>

                        <li id="checkMenu">
                            <a href="/admin/product/prodcheck">
                                <i class="fa fa fa-tag"></i>
                                <span class="nav-label">商品校验</span>
                            </a>
                        </li>
                    </ul>
                </li>
            </shiro:hasPermission>

            <shiro:hasPermission name="mineWork:menu">
                <li id="applyApprove">
                    <a href="#">
                        <i class="fa fa fa-bar-chart-o"></i>
                        <span class="nav-label">我的待办事项</span>
                        <span class="fa arrow"></span>
                    </a>
                    <ul class="nav nav-second-level">

                        <shiro:hasPermission name="mineWork:notApproveSku">
                            <li id="skuApprove">
                                <a href="/admin/skuApprove">
                                    <i class="fa fa fa-bar-chart-o"></i>
                                    <span class="nav-label">待审核SKU</span>
                                </a>
                            </li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="mineWork:ApprovedSku">
                            <li id="skuApproveResult">
                                <a href="/admin/skuApproveResult">
                                    <i class="fa fa fa-bar-chart-o"></i>
                                    <span class="nav-label">已审核SKU</span>
                                </a>
                            </li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="mineWork:notSetPriceSku">
                            <li id="stayPrice">
                                <a href="/admin/stayPrice">
                                    <i class="fa fa fa-bar-chart-o"></i>
                                    <span class="nav-label">待设置价格</span>
                                </a>
                            </li>

                        </shiro:hasPermission>

                    </ul>
                </li>
            </shiro:hasPermission>

            <shiro:hasPermission name="baseData:menu">
                <li id="dictManage">
                    <a href="#">
                        <i class="fa fa fa-bar-chart-o"></i>
                        <span class="nav-label">基础数据维护</span>
                        <span class="fa arrow"></span>
                    </a>
                    <ul class="nav nav-second-level">
                        <shiro:hasPermission name="measureUnit:list">
                            <li id="measureUnit">
                                <a href="/admin/dict/measure">
                                    <i class="fa fa fa-bar-chart-o"></i>
                                    <span class="nav-label">计量单位</span>
                                </a>
                            </li>
                        </shiro:hasPermission>

                        <shiro:hasPermission name="reasonDict:list">
                            <li id="reasonDict">
                                <a href="/admin/dict/reason">
                                    <i class="fa fa fa-bar-chart-o"></i>
                                    <span class="nav-label">作废原因</span>
                                </a>
                            </li>
                        </shiro:hasPermission>

                        <shiro:hasPermission name="dict:list">
                            <li id="dictionary">
                                <a href="/admin/dictionary/list">
                                    <i class="fa fa fa-bar-chart-o"></i>
                                    <span class="nav-label">数据字典</span>
                                </a>
                            </li>
                        </shiro:hasPermission>
                    </ul>
                </li>
            </shiro:hasPermission>

            <shiro:hasPermission name="baseinstall">
                <li id="baseInstall">
                    <a href="#">
                        <i class="fa fa fa-bar-chart-o"></i>
                        <span class="nav-label">基装数据</span>
                        <span class="fa arrow"></span>
                    </a>
                    <ul class="nav nav-second-level">
                        <li id="customerContract">
                            <a href="/admin/baseinstall">
                                <i class="fa fa fa-bar-chart-o"></i>
                                <span class="nav-label">客户合同列表</span>
                            </a>
                        </li>
                    </ul>
                </li>
            </shiro:hasPermission>
            <li id="updatePassword">
                <a href="/api/modify/password">
                    <i class="fa fa-edit"></i>
                    <span class="nav-label">修改密码</span>
                </a>
            </li>
        </ul>
        <!-- 左侧菜单 end-->
    </div>
</nav>
<!--左侧导航结束-->