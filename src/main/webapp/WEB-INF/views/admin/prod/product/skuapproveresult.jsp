<%@ page contentType="text/html;charset=UTF-8" %>
<div id="container" class="wrapper wrapper-content animated fadeInRight">
    <div id="breadcrumb">
        <bread-crumb :crumbs="breadcrumbs"></bread-crumb>
    </div>
    <!-- ibox start -->
    <div class="ibox">
        <div class="ibox-content">
            <div class="row">
                <form id="searchForm" @submit.prevent="query" class="form-horizontal">

                    <div class="col-md-4 form-group">
                        <label for="allStores" class="control-label col-sm-4">门店</label>
                        <div class="col-sm-8">
                            <select name="allStores" id="allStores" class="form-control" v-model="allStoreCode">
                                <option value="">请选择</option>
                                <option value="{{item.code}}" v-for="item of allStores">{{item.name}}</option>
                            </select>
                        </div>
                    </div>
                    <div class="col-md-4 form-group">
                        <label for="allSuppliers" class="control-label col-sm-4">区域供应商</label>
                        <div class="col-sm-8">
                            <select name="allSuppliers" id="allSuppliers" class="form-control" v-model="allSupplierId">
                                <option value="">请选择</option>
                                <option value="{{item.id}}" v-for="item of allSuppliers">{{item.name}}</option>
                            </select>
                        </div>
                    </div>

                    <div class="col-md-4 form-group">
                        <label for="" class="control-label col-sm-4">商品供应商</label>
                        <div class="col-sm-8">
                            <select name="" id="" class="form-control" v-model="form.supplierId">
                                <option value="">请选择</option>
                                <option value="{{item.id}}" v-for="item of suppliers">{{item.name}}</option>
                            </select>
                        </div>
                    </div>
                    <div class="col-md-4 form-group">
                        <label for="allSuppliers" class="control-label col-sm-4">关键字</label>
                        <div class="col-sm-8">
                            <input v-model="form.keyword" type="text" placeholder="sku编码/名称 " class="form-control"/>
                        </div>
                    </div>

                    <div class="col-md-4 form-group">
                        <label for="allStores" class="control-label col-sm-4">商品类目</label>
                        <div class="col-sm-8">
                            <select v-model="form.catalogUrl" class="form-control">
                                <option value="">请选择商品类目</option>
                                <option v-for="category in allCatalog" :value="category.id">{{{category.name}}}</option>
                            </select>
                        </div>
                    </div>

                    <div class="col-md-4 form-group">
                        <label for="allStores" class="control-label col-sm-4"></label>
                        <div class="col-sm-8">
                            <button id="searchBtn" type="submit" class="btn btn-block btn-outline btn-default" alt="搜索"
                                    title="搜索">
                                <i class="fa fa-search"></i>
                            </button>
                        </div>
                    </div>
                </form>
            </div>
            <!-- <div class="columns columns-right btn-group pull-right"></div> -->
            <table id="dataTable" width="100%" class="table table-striped table-bordered table-hover">
            </table>
        </div>
    </div>
    <!-- ibox end -->
</div>


</form>
</validator>
</div>

<div id="approval" class="modal modal-dialog fade" tabindex="-1" data-width="800">
    <div class="modal-header">
        <h3 align="center">审批记录</h3>
    </div>
    <div class="modal-body">
        <div class="row" style="padding-left: 8px;padding-right: 8px">
            <table class="table table-striped table-bordered  table-hover">
                <thead>
                <tr>
                    <th style="text-align: center" width="15%">审批结果</th>
                    <th style="text-align: center" width="15%">操作人</th>
                    <th style="text-align: center;" width="20%">审批时间</th>
                    <th style="text-align: center;" width="20%">审批节点</th>
                    <th style="text-align: center;word-break:break-all; word-wrap:break-word">审批说明</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="item in recordList" align="center">
                    <td>{{{item.approvalResult | result }}}</td>
                    <td>{{item.name}}</td>
                    <td>{{item.approvalTime}}</td>
                    <td>{{item.approvalNode | node}}</td>
                    <td>{{item.approvalNote}}</td>
                </tbody>
            </table>
        </div>
    </div>
    <div class="modal-footer ">
        <button type="button" data-dismiss="modal" class="btn btn-danger" style="margin-left: 5px;">关闭</button>
    </div>
</div>
<style>

</style>

<script src="${ctx}/static/admin/js/components/jquery.form.min.js?v=ea38b0f7"></script>
<script src="/static/admin/vendor/webuploader/webuploader.js?v=ea38b0f7"></script>
<script src="/static/admin/js/components/webuploader.js?v=ea38b0f7"></script>
<script src="/static/admin/js/apps/prod/product/skuapproveresult.js?v=ea38b0f7"></script>