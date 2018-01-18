<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<title>已对账</title>
<meta name="keywords" content="">
<meta name="description" content="">
<style>
</style>
</head>
<body id="app" class="fixed-sidebar full-height-layout gray-bg">
<div id="container" class="wrapper wrapper-content" v-cloak>
    <div id="breadcrumb">
        <bread-crumb :crumbs="breadcrumbs"></bread-crumb>
    </div>
    <!-- ibox start -->
    <div class="ibox">
        <div class="ibox-content">
            <div>
                <div class="row">
                    <form id="searchForm" @submit.prevent="query">

                        <div class="col-sm-5">
                            <div class="form-group" style="line-height: 34px"
                                 :class="{'has-error':($validation.starttime.invalid && $validation.touched)}">
                                <label for="startTime" class="col-sm-3  control-label">关键字</label>
                                <div class="col-sm-5 no-padding">
                                    <input
                                            v-model="form.keyword"
                                            id="keyword"
                                            name="keyword"
                                            type="text"
                                            placeholder="订单编号/项目编号/商品名称" class="form-control"/>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-5">
                            <div class="form-group" style="line-height: 34px"
                                 :class="{'has-error':($validation.starttime.invalid && $validation.touched)}">
                                <label for="startTime" class="col-sm-3 control-label">对账时间</label>
                                <div class="col-sm-4 no-padding">
                                    <input
                                            v-model="form.startTime"
                                            v-el:start-time
                                            id="startTime"
                                            name="startTime"
                                            type="text"
                                            class="form-control datepicker"
                                            placeholder="请选择开始时间">
                                </div>
                                <label for="endTime" class="control-label col-sm-1">~</label>
                                <div class="col-sm-4 no-padding">
                                    <input
                                            v-model="form.endTime"
                                            v-el:end-time
                                            id="endTime"
                                            name="endTime"
                                            type="text"
                                            class="form-control datepicker"
                                            placeholder="请选择结束时间">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-1" >
                            <div class="form-group" >
                                <button id="searchBtn" @click="query" type="button" class="btn btn-block btn-outline btn-default"
                                        alt="搜索"
                                        title="搜索">
                                    <i class="fa fa-search"></i>
                                </button>
                            </div>
                        </div>

                    </form>
                </div>
                <div>
                    <div style="color: red;float: right;padding-bottom:10px;">对账总金额：{{totalPrice.toFixed(2)}}（元）（商品总金额：{{skuTotalPrice.toFixed(2)}}（元），其他费用总金额：{{otherPrice.toFixed(2)}}（元））</div>
                    <table v-el:dataTable id="dataTable" width="100%"
                           class="table table-striped table-bordered table-hover">
                    </table>
                </div>
    </div>
    <!-- ibox end -->
</div>


</div>
<script src="/static/admin/js/apps/indentOrder/alreadyMaterialSupplierAccountChecking.js"></script>
</body>
</html>