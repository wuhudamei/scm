<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<head>
    <style>
        .progress {
            height: 0px;
            transition: all .6s ease;
        }

        .progress-uploading {
            margin-top: 2px;
            height: 20px;
        }
    </style>
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

                    <div class="col-md-4 form-group">
                        <input v-model="form.keyword" type="text"
                               placeholder="订货单号，项目编号，客户姓名，客户手机号" class="form-control"/>
                    </div>
                    <div class="col-md-4 form-group">
                        <select v-model="form.status" class="form-control">
                            <option value="">请选择状态</option>
                            <option value="NOTRECONCILED">未对账</option>
                            <option value="HASBEENRECONCILED">已对账</option>
                            <option value="PARTIALRECONCILIATION">部分对账</option>
                        </select>
                    </div>
                    <div class="col-md-2"></div>
                    <div class="col-md-2 form-group">
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



<!-- 对账 -->
<div id="detailModal" class="modal fade" tabindex="-1" >
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
                aria-hidden="true">×
        </button>
        <h3 align="center">对账详情</h3>
    </div>
    <div class="modal-body" style="padding: 0;margin: 0 7px 0 7px;">
        <div :class="{hasHeight:isShow }" class="row detail-stranding-book detail-state" v-cloak
             style="padding: 10px 1%;">
            <div style="border: 1px solid #c2ccd1;overflow: hidden;margin: 0 4px 0 4px;">
                <div class="col-sm-4 col-xs-12">
                    <label >客户姓名：</label>
                    {{customerContract.customerName}}
                </div>
                <div class="col-sm-4 col-xs-12">
                    <label >客户电话：</label>
                    {{customerContract.customerPhone}}
                </div>
                <div class="col-sm-4 col-xs-12">
                    <label >装修地址：</label>
                    {{customerContract.houseAddr}}
                </div>
                <div class="col-sm-4 col-xs-12">
                    <label >设计师：</label>
                    {{customerContract.designer}}
                </div>
                <div class="col-sm-4 col-xs-12">
                    <label >设计师电话：</label>
                    {{customerContract.designerMobile}}
                </div>
                <div class="col-sm-4 col-xs-12">
                    <label >监理：</label>
                    {{customerContract.supervisor}}
                </div>
                <div class="col-sm-4 col-xs-12">
                    <label >监理电话：</label>
                    {{customerContract.supervisorMobile}}
                </div>
                <div class="col-sm-4 col-xs-12">
                    <label >项目经理：</label>
                    {{customerContract.projectManager}}
                </div>
                <div class="col-sm-4 col-xs-12">
                    <label >项目经理电话：</label>
                    {{customerContract.pmMobile}}
                </div>
            </div>
        </div>
        <table id="detailTable" width="100%"
               class="table table-striped table-bordered table-hover">
        </table>
        <div class="ibox">
            <div class="ibox-title"><strong>对账操作</strong></div>

            <div style="border: 1px solid #c2ccd1;overflow: hidden;clear: both;padding: 30px">
                <div  >
                    <label  class="control-label col-md-1" >备注：</label>
                    <div class="col-md-11 ">
                        <textarea class="form-control "
                                  v-model="remarks" placeholder="备注"
                                  class="form-control"
                                  data-tabindex="1"
                                  >
                        </textarea>
                    </div>
                </div>
            </div>

        </div>

    </div>
    <div class="modal-footer">
        <div class="form-group col-sm-12" align="center">
            <button type="button" @click="save('PARTIALRECONCILIATION')" class="btn btn-primary btn-sm">部分对账</button>
            <button type="button" @click="save('HASBEENRECONCILED')" class="btn btn-danger btn-sm">完全对账</button>
            <button type="button" data-dismiss="modal" class="btn btn-sm">关闭</button>
        </div>

    </div>
</div>

<!-- 详情modal -->
<div id="orderDetailModal" class="modal fade" tabindex="-1" data-width="1000">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
                aria-hidden="true">×
        </button>
        <h3 align="center">北京美得你装饰设计有限公司-订货单详情</h3>
        <div :class="{hasHeight:isShow }" class="row detail-stranding-book detail-state" v-cloak
             style="padding: 10px 1%;">
            <div style="border: 1px solid #c2ccd1;overflow: hidden;clear: both">
                <div class="col-sm-4 col-xs-12">
                    <label for="">客户姓名：</label>
                    {{customerContract.customerName}}
                </div>
                <div class="col-sm-4 col-xs-12">
                    <label for="">客户电话：</label>
                    {{customerContract.customerPhone}}
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
        <div class="modal-body" style="padding: 0">
            <table id="detailTable" width="100%"
                   class="table table-striped table-bordered table-hover">
            </table>
        </div>
    </div>
</div>


<script src="/static/admin/vendor/webuploader/webuploader.js?v=ea38b0f7"></script>
<script src="/static/admin/js/components/webuploader.js?v=ea38b0f7"></script>
<script src="/static/admin/js/apps/indentOrder/reconciliationList.js?v=ea38b0f7"></script>
