<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<title>未对账</title>
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
                                <label for="startTime" class="col-sm-3 control-label">发货时间</label>
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
                                <button id="searchBtn" @click="fetchRecord" type="button" class="btn btn-block btn-outline btn-default"
                                        alt="搜索"
                                        title="搜索">
                                    <i class="fa fa-search"></i>
                                </button>
                            </div>
                        </div>
                         <div class="col-md-5"  v-show="acctType != 'ADMIN'" >
                            <button @click="export" id="createBtn" type="button" class="btn btn-outline btn-primary">导出</button>
                            <button @click="exportAndMark" id="createBtn2" type="button" class="btn btn-outline btn-primary">导出并对账</button>
                            <button @click="mark" id="createBtn3" type="button" class="btn btn-outline btn-primary">对账</button>
                         </div>

                    </form>
                </div >
                <div style="color: red;float: right;padding-bottom:10px;">当前选择商品个数：{{pdcount}}（个） &nbsp;&nbsp;   当前选择商品总额：{{totalAmount}} （元）</div>
                 <%--<div style="color: red"></div>--%>

                <div>
                    <validator name="valid">
                        <table id="dataTable2" class="table table-bordered" >
                            <tr id="titileTr" >
                                <td style="text-align: center;font-weight:bold"><input @click="checkAll"  v-model="checkAllValue" class="checkAll" type="checkbox"/></td>
                                <td style="text-align: center;font-weight:bold">项目编号</td>
                                <td style="text-align: center;font-weight:bold">订货单号</td>
                                <td style="text-align: center;font-weight:bold">商品名称</td>
                                <td style="text-align: center;font-weight:bold">商品型号</td>
                                <td style="text-align: center;font-weight:bold">商品规格</td>
                                <td style="text-align: center;font-weight:bold">商品属性1</td>
                                <td style="text-align: center;font-weight:bold">商品属性2</td>
                                <td style="text-align: center;font-weight:bold">商品属性3</td>
                                <td style="text-align: center;font-weight:bold">数量</td>
                                <td style="text-align: center;font-weight:bold">单价</td>
                                <td style="text-align: center;font-weight:bold">其他费用</td>
                                <td style="text-align: center;font-weight:bold">总价</td>
                                <td style="text-align: center;font-weight:bold">状态</td>
                            </tr>
                            <tr v-for="stRdBilltem in stRdBilltems" > <!--@change="countChange(stRdBilltem)"-->
                                <td style="text-align: center;font-weight:bold"><input type="checkbox" class="checkItem" name="checkItem" v-model="checkArr" @change="comm" value="{{stRdBilltem.id}}"/></td>
                                <td style="text-align: center">{{stRdBilltem.contractCode | noName}}</td>
                                <td style="text-align: center">{{stRdBilltem.orderCode | noName}}</td>
                                <td style="text-align: center">{{stRdBilltem.skuName | noName}}</td>
                                <td style="text-align: center">{{stRdBilltem.model | noName}}</td>
                                <td style="text-align: center">{{stRdBilltem.spec | noName}}</td>
                                <td style="text-align: center">{{stRdBilltem.attribute1 | noName}}</td>
                                <td style="text-align: center">{{stRdBilltem.attribute2 | noName}}</td>
                                <td style="text-align: center" >{{stRdBilltem.attribute3 | noName}}</td>
                                <td style="text-align: center">{{stRdBilltem.quantity | noName}}</td>
                                <td style="text-align: center">{{stRdBilltem.supplyPrice | noName}}</td>
                                <td style="text-align: center">{{stRdBilltem.otherFee  | noName}}</td>
                                <td style="text-align: center">{{stRdBilltem.totalMoney | noName}}</td>
                                <td style="text-align: center">{{stRdBilltem.payStatus === 'NOT_PAIED' ? '未对账' : '已对账' | noName}}</td>
                            </tr>
                            <tr v-if="stRdBilltems.length === 0">
                                <td class="text-center" colspan="14">没有找到匹配的记录</td>
                            </tr>
                        </table>
                    </validator>
                </div>
            </div>
        </div>
    </div>
    <!-- ibox end -->
</div>
</div>
<script src="/static/admin/vendor/decimal/decimal.min.js"></script>
<script src="/static/admin/js/apps/indentOrder/materialSupplierAccountChecking.js"></script>
</body>
</html>