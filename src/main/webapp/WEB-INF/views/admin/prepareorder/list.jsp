<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<head>
</head>
<div id="container" class="wrapper wrapper-content animated fadeInRight">
    <div id="breadcrumb">
        <bread-crumb :crumbs="breadcrumbs"></bread-crumb>
    </div>
    <!-- ibox start -->
    <div class="ibox">
        <div class="ibox-content">
            <div class="row">
                <form id="searchForm" @submit.prevent="query">

                    <div class="col-md-2 form-group">
                        <input v-model="form.keyword" type="text"
                               placeholder="项目编号|客户姓名" class="form-control"/>
                    </div>

                    <div class="col-md-2 form-group">
                        <select v-model="form.status" class="form-control">
                            <option value="">--预备单状态--</option>
                            <option value="WAITING_TRANSFERRED">待转单</option>
                            <option value="ALREADY_TRANSFERRED">已转单</option>
                            <option value="HAS_NULLIFIED">已作废</option>
                        </select>
                    </div>

                    <div class="col-md-2 form-group" v-if="accType != 'PROD_SUPPLIER'">
                        <label class="sr-only" for="brandId"></label>
                        <select v-model="form.brandId"
                                id="brandId"
                                name="brandId"
                                class="form-control">
                            <option value="">--请选择品牌--</option>
                            <option v-for="brand in brands" :value="brand.id">{{brand.name}}</option>
                        </select>
                    </div>

                    <div class="col-md-2 form-group">
                        <input v-model="form.startDate" v-el:start-date id="startDate"
                               name="startDate" type="text" readonly
                               class="form-control datepicker" placeholder="请选择创建开始时间">
                    </div>
                    <div class="col-md-2 form-group">
                        <input v-model="form.endDate" v-el:end-date id="endDate"
                               name="endDate" type="text" readonly
                               class="form-control datepicker" placeholder="请选择创建结束时间">
                    </div>

                    <div class="col-md-2 form-group">
                        <select v-model="form.dataSource" class="form-control">
                            <option value="">--来源--</option>
                            <option value="0">选材</option>
                            <option value="change">变更</option>
                        </select>
                    </div>

                    <div class="col-md-10 form-group"></div>
                    <div class="col-md-1 form-group">
                        <button id="searchBtn" type="submit"
                                class="btn btn-block btn-outline btn-default" alt="搜索"
                                title="搜索">
                            <i class="fa fa-search"></i>
                        </button>
                    </div>
                </form>
            </div>
            <!-- <div class="columns columns-right btn-group pull-right"></div> -->
            <table id="dataTable" width="100%"
                   class="table table-striped table-bordered table-hover">
            </table>
        </div>
    </div>
    <!-- ibox end -->
</div>


<!-- 详情modal -->
<div id="detailModal" class="modal fade" tabindex="-1" data-width="1000">
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"
                    aria-hidden="true">×
            </button>
            <h3 align="center">北京美得你装饰设计有限公司-预备单详情</h3>
            <div :class="{hasHeight:isShow }" class="row detail-stranding-book detail-state" v-cloak
                 style="padding: 10px 1%;">
                <div style="border: 1px solid #c2ccd1;overflow: hidden;clear: both">
                    <div class="col-sm-4 col-xs-12">
                        <label for="">客户姓名：</label>
                        {{customerContract.customer.name}}
                    </div>
                    <div class="col-sm-4 col-xs-12">
                        <label for="">客户电话：</label>
                        {{customerContract.customer.mobile}}
                    </div>
                    <div class="col-sm-4 col-xs-12">
                        <label for="">装修地址：</label>
                        {{customerContract.houseAddr}}
                    </div>
                    <div class="col-sm-4 col-xs-12">
                        <label for="">设计师：</label>
                        {{customerContract.designer}}
                    </div>
                    <div class="col-sm-4 col-xs-12">
                        <label for="">设计师电话：</label>
                        {{customerContract.designerMobile}}
                    </div>
                    <div class="col-sm-4 col-xs-12">
                        <label for="">监理：</label>
                        {{customerContract.supervisor}}
                    </div>
                    <div class="col-sm-4 col-xs-12">
                        <label for="">监理电话：</label>
                        {{customerContract.supervisorMobile}}
                    </div>
                    <div class="col-sm-4 col-xs-12">
                        <label for="">项目经理：</label>
                        {{customerContract.projectManager}}
                    </div>
                    <div class="col-sm-4 col-xs-12">
                        <label for="">项目经理电话：</label>
                        {{customerContract.pmMobile}}
                    </div>
                </div>
            </div>
        </div>
        <div class="modal-body" style="padding: 0">
            <div class="ibox">
                <div class="ibox-content">
                    <table id="detailTable" width="100%"
                           class="table table-striped table-bordered table-hover">
                    </table>
                </div>
            </div>
        </div>
        <div class="modal-footer" style="padding: 0">
            <div class="ibox">
                <div class="ibox-content" style="text-align: center">
                    <template v-if="status == 'WAITING_TRANSFERRED'">
                        <button type="button" @click="toIndentOrder" class="btn btn-warning">转订货单</button>&nbsp;&nbsp;
                        <button type="button" @click="abandonedOrder" class="btn btn-danger">作废</button>
                    </template>&nbsp;&nbsp;
                    <button type="button" data-dismiss="modal" class="btn" >关闭</button>
                </div>
            </div>

        </div>
    </div>
</div>

<script src="/static/admin/js/apps/prepareorder/list.js"></script>
