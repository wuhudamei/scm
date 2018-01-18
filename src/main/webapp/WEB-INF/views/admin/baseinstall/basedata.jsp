<%@ page contentType="text/html;charset=UTF-8" %>
<div id="container" class="wrapper wrapper-content animated fadeInRight">
  <div id="breadcrumb">
    <bread-crumb :crumbs="breadcrumbs"></bread-crumb>
  </div>

    <div class="ibox">
        <div class="ibox-title">
            <h5>基本信息</h5>
        </div>
        <div class="ibox-content">
            <div class="row">
                <div class="col-xs-3">项目编号：{{baseMain.projectCode}}</div>
                <div class="col-xs-3">客户姓名：{{baseMain.customerName}}</div>
                <div class="col-xs-3">客户电话：{{baseMain.customerPhone}}</div>
                <div class="col-xs-3">设计师姓名：{{baseMain.designerName}}</div>
            </div>
            <div class="row">
                <div class="col-xs-3">设计师电话：{{baseMain.designerPhone}}</div>
                <div class="col-xs-3">工程地址：{{baseMain.projectAddress}}</div>
                <div class="col-xs-3">建筑面积：{{baseMain.budgetArea}}</div>
                <div class="col-xs-3">房型：{{baseMain.houseLayout}}</div>
            </div>
            <div class="row">
                <div class="col-xs-3">工程造价：{{baseMain.engineeringCost}}</div>
                <div class="col-xs-3">旧房拆改费：{{baseMain.dismantleFee}}</div>
                <div class="col-xs-3">变更费：{{baseMain.changeFee}}</div>
                <div class="col-xs-3">是否有电梯：{{baseMain.haveElevator | haveElevator-filter}}</div>
            </div>
            <div class="row">
                <div class="col-xs-3">预算号：{{baseMain.budgetNo}}</div>
            </div>
        </div>
    </div>

    <div class="ibox">
        <div class="ibox-content">
            <div>
            <!-- Nav tabs -->
            <ul class="nav nav-tabs" role="tablist">
                <li role="presentation" class="active"><a href="#base" role="tab" data-toggle="tab">基装</a></li>
                <li role="presentation"><a href="#major" role="tab" data-toggle="tab">主材增减</a></li>
                <li role="presentation"><a href="#standard" role="tab" data-toggle="tab">标配</a></li>
                <li role="presentation"><a href="#oldhouse" role="tab" data-toggle="tab">旧房拆改</a></li>
                <li role="presentation"><a href="#change" role="tab" data-toggle="tab">变更</a></li>
            </ul>
            <!-- Tab panes -->
            <div class="tab-content">
                <div role="tabpanel" class="tab-pane active" id="base">
                    <basedata-basehome :contract-id="baseMain.id" v-if="baseMain.id"></basedata-basehome>
                </div>
                <div role="tabpanel" class="tab-pane" id="major">
                    <basedata-basepm :contract-id="baseMain.id" v-if="baseMain.id" ></basedata-basepm>
                </div>
                <div role="tabpanel" class="tab-pane" id="standard">
                    <!--标配 位置-->
                    <basedata-stand :contract-id="baseMain.id" v-if="baseMain.id" ></basedata-stand>
                </div>
                <div role="tabpanel" class="tab-pane" id="oldhouse">
                    <!--旧房拆改 位置-->
                    <basedata-house :contract-id="baseMain.id" v-if="baseMain.id" ></basedata-house>
                </div>
                <div role="tabpanel" class="tab-pane" id="change">
                    <!--变更 位置-->
                    <basedata-change :contract-id="baseMain.id" v-if="baseMain.id" ></basedata-change>
                </div>
            </div>

            </div>
        </div>
    </div>

</div>

<%@ include file="./template/base.jsp" %>
<%@ include file="./template/base-pm.jsp" %>

<%@ include file="./template/change.jsp" %>
<%@ include file="./template/house.jsp" %>
<%@ include file="./template/standard.jsp" %>

<script src="/static/admin/js/apps/baseinstall/base.js"></script>
<script src="/static/admin/js/apps/baseinstall/basepm.js"></script>
<script src="/static/admin/js/apps/baseinstall/change.js"></script>
<script src="/static/admin/js/apps/baseinstall/house.js"></script>
<script src="/static/admin/js/apps/baseinstall/standard.js"></script>

<script src="/static/admin/js/apps/baseinstall/basedata.js"></script>

