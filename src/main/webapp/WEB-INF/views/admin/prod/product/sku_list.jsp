<%@ page contentType="text/html;charset=UTF-8" %>
<div id="container" class="wrapper wrapper-content animated fadeInRight">
    <div id="breadcrumb">
        <bread-crumb :crumbs="breadcrumbs"></bread-crumb>
    </div>
    <!-- ibox start -->
    <div class="ibox">
        <div class="ibox-content">
                <form id="searchForm" @submit.prevent="query" class="form-horizontal">
                    <div class="row">
                        <div class="col-md-4 form-group">
                            <label for="allStores" class="control-label col-sm-4">门店</label>
                            <div class="col-sm-8">
                                <select name="allStores" id="allStores" class="form-control" v-model="allStoreCode">
                                    <option value="">请选择</option>
                                    <option value="{{item.code}}" v-for="item of allStores">{{item.name}}</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <label for="allSuppliers" class="control-label col-sm-4">区域供应商</label>
                            <div class="col-sm-8">
                                <select name="allSuppliers" id="allSuppliers" class="form-control"
                                        v-model="allSupplierId">
                                    <option value="">请选择</option>
                                    <option value="{{item.id}}" v-for="item of allSuppliers">{{item.name}}</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <label for="" class="control-label col-sm-4">商品供应商</label>
                            <div class="col-sm-8">
                                <select name="" id="" class="form-control" v-model="form.supplierId">
                                    <option value="">请选择</option>
                                    <option value="{{item.id}}" v-for="item of suppliers">{{item.name}}</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-4 form-group">
                            <label for="allSuppliers" class="control-label col-sm-4">关键字</label>
                            <div class="col-sm-8">
                                <input v-model="form.keyword" type="text" placeholder="sku编码/名称 " class="form-control"/>
                            </div>
                        </div>

                        <div class="col-md-4">
                            <label for="allStores" class="control-label col-sm-4">商品类目</label>
                            <div class="col-sm-8">
                                <select v-model="form.catalogUrl" class="form-control">
                                    <option value="">请选择商品类目</option>
                                    <option v-for="category in allCatalog" :value="category.id">{{{category.name}}}</option>
                                </select>
                            </div>
                        </div>

                        <div class="col-md-4">
                            <label for="allStores" class="control-label col-sm-4">sku状态</label>
                            <div class="col-sm-6">
                                <select v-model="form.processStatus" class="form-control">
                                    <option value="" checked>请选择状态</option>
                                    <option v-for="status in processStatusList" :value="status.dicValue">{{status.dicName}}</option>
                                </select>
                            </div>
                            <button id="searchBtn" type="submit" class="btn btn-default col-sm-2" alt="搜索"
                                    title="搜索">
                                <i class="fa fa-search"></i>
                            </button>
                        </div>
                    </div>
                </form>

            <!-- <div class="columns columns-right btn-group pull-right"></div> -->
            <table id="dataTable" width="100%" class="table table-striped table-bordered table-hover">
            </table>
        </form>
    </div>
    <!-- ibox end -->
</div>


<div id="modal" class="modal fade" tabindex="-1" data-width="600">
    <validator name="validation">
        <form name="banner" novalidate class="form-horizontal" role="form">
            <input type="hidden" v-model="id">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3 align="center">批量导入商品</h3>
            </div>

            <div class="modal-body">


                <!-- 上传图片 -->
                <div class="form-group">
                    <label for="logo" class="col-sm-3 control-label">商品文件:</label>
                    <div class="col-sm-9">
                        <div class="input-group">
                            <input id="logo" v-model="logo" readonly type="text" data-tabindex="1" placeholder="点击上传文件"
                                   class="input-sm form-control">
                            <span class="input-group-btn">
                <web-uploader :target-id="'webuploader0'"
                              :type="webUploader.type"
                              :w-server="webUploader.server"
                              :w-file-size-limit="webUploader.fileSizeLimit"
                              :w-file-single-size-limit="webUploader.fileSingleSizeLimit"
                              :w-form-data="webUploader.formData">
                  <button id="imageUpload" type="button" class="btn btn-sm btn-primary">导入文件</button>
                </web-uploader>
              </span>
                        </div>
                        <div style="margin-bottom:0;" :class="{'progress-uploading':uploading}"
                             class="progress progress-striped active">
                            <div :style="{ width:progress+'%'}" aria-valuemax="100" aria-valuemin="0" aria-valuenow="75"
                                 role="progressbar" class="progress-bar">
                                <span class="sr-only"></span>
                            </div>
                        </div>
                    </div>
                </div>


                <div class="form-group">
                    <label  class="control-label col-sm-3">商品模版:</label>
                    <div class="col-sm-9">
                        <div class="col-md-6">
                            <div class="form-group">
                                <button @click="downloadFile('')" type="button"
                                        class="btn btn-block btn-outline btn-primary">下载模版
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="form-group" :class="{'has-error':$validation.name.invalid && $validation.touched}">
                    <label  class="control-label col-sm-3">导入说明:</label>
                    <div class="col-sm-9">
                    </div>
                </div>

            </div>

            <div class="modal-footer">
                <button type="button" data-dismiss="modal" class="btn">关闭</button>
            </div>
        </form>
    </validator>
</div>
</div>
<div id="priceModal" class="modal fade" tabindex="-1" data-width="600" v-cloak>
    <div class="modal-header">
        <h3 align="center">设置价格</h3>
    </div>
    <div class="modal-body">
        <div class="row">
            <div class="col-md-12">
                <div class="col-md-4 tab" @click="tab('aConfig')" :class="{active:current == 'aConfig' ? true:false}"
                     v-if="aShow == 1">网真采购价
                </div>
                <div class="col-md-4 tab" @click="tab('bConfig')" :class="{active:current == 'bConfig' ? true:false}"
                     v-if="bShow == 1">门店采购价
                </div>
                <div class="col-md-4 tab" @click="tab('cConfig')" :class="{active:current == 'cConfig' ? true:false}"
                     v-if="cShow == 1">门店销售价
                </div>
            </div>

        </div>
        <div v-show="current == 'aConfig'" class="col-md-12 ">
            <div class="search">
                <div class="btn btn-primary fr" @click="createBtnClickHandler('SUPPLY')" style="margin:10px 0"
                     v-if="aControl == 1">新增
                </div>
            </div>
            <table id="aTable" width="100%" class="table table-striped table-bordered table-hover">
            </table>
        </div>

        <div v-show="current == 'bConfig'" class="col-md-12">
            <div class="search">
                <div class="btn btn-primary fr" @click="createBtnClickHandler('STORE')" style="margin:10px 0"
                     v-if="bControl == 1">新增
                </div>
            </div>
            <table id="bTable" width="100%" class="table table-striped table-bordered table-hover">
            </table>
        </div>

        <div v-show="current == 'cConfig'" class="col-md-12">
            <div class="search">
                <div class="btn btn-primary fr" @click="createBtnClickHandler('SALE')" style="margin:10px 0"
                     v-if="cControl == 1">新增
                </div>
            </div>
            <table id="cTable" width="100%" class="table table-striped table-bordered table-hover">
            </table>
        </div>

    </div>
    <div class="modal-footer">
        <button type="button" @click="closeFrame" class="btn btn-primary">确定</button>
    </div>
</div>

<div id="contractModal" class="modal modal-dialog fade" tabindex="1" data-width="600">
    <validator name="validation">
        <form name="banner" novalidate class="form-horizontal" role="form">
            <input type="hidden" v-model="skuId">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3 align="center">新增/编辑{{type}}</h3>
            </div>

            <div class="modal-body">

                <div class="form-group" :class="{'has-error':$validation.time.invalid && $validation.touched}">
                    <div class="col-sm-12">
                        <label class="control-label col-sm-4"><span style="color: red">*</span>生效时间</label>
                        <div class="col-sm-8">
                            <input id="timepick" v-model="priceStartDate" type="text" v-validate:time="{
              required:{rule:true,message:'请输入时间'},
              }" placeholder="时间" class="datepicker form-control" readonly :disabled="blank"/>
                            <span v-cloak v-if="$validation.time.invalid && $validation.touched" class="help-absolute">
                请输入时间
              </span>
                        </div>
                    </div>
                </div>

                <div class="form-group" :class="{'has-error':$validation.price.invalid && $validation.touched}">
                    <div class="col-sm-12">
                        <label  class="control-label col-sm-4"><span style="color: red">*</span>价格</label>
                        <div class="col-sm-8">
                            <input v-validate:price="{
              required:{rule:true,message:'请输入价格'},
              }" v-model="price" type="number" placeholder="价格" class="form-control" min="0">
                            <span v-cloak v-if="$validation.price.invalid && $validation.touched" class="help-absolute">
              请输入价格
            </span>
                        </div>
                    </div>
                </div>

            </div>

            <div class="modal-footer">
                <button type="button" data-dismiss="modal" class="btn">关闭</button>
                <button type="button" @click="savePrice" class="btn btn-primary">保存</button>
            </div>

</form>
</validator>
</div>
<div id="reject" class="modal modal-dialog fade" tabindex="-1" data-width="800">
    <div class="modal-header">
        <h3 align="center">审批记录</h3>
    </div>
    <div class="modal-body">
        <div class="ibox">
            <div class="row" style="padding-left: 8px;padding-right: 8px">
                <table class="table table-striped table-bordered  table-hover" >
                    <thead>
                    <tr >
                        <th style="text-align: center" width="15%">审批结果</th>
                        <th style="text-align: center" width="15%">操作人</th>
                        <th style="text-align: center;" width="20%">审批时间</th>
                        <th style="text-align: center;word-break:break-all; word-wrap:break-word">审批说明</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="item in recordList" align="center">
                        <td>{{{item.approvalResult | result }}}</td>
                        <td>{{item.name}}</td>
                        <td>{{item.approvalTime}}</td>
                        <td>{{item.approvalNote}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>

    </div>
    <div class="modal-footer ">
        <button type="button" data-dismiss="modal" class="btn btn-danger" style="margin-left: 5px;">关闭</button>
    </div>
</div>

<style>
    .border {
        border: 1px solid #ccc;
        padding: 10px;
    }

    .margin20 {
        margin-bottom: 20px;
    }

    .margin10 {
        margin-bottom: 10px;
    }

    .sku input,
    .sku label {
        padding: 6px 12px;
    }

    .tab {
        border: 1px solid #f3f3f4;
        text-align: center;
        font-size: 14px;
        line-height: 40px;
        cursor: pointer;
    }

    .tab:hover {
        background-color: #1ab394;
    }

    .tab.active {
        background-color: #1ab394;
    }
</style>

<script src="${ctx}/static/admin/js/components/jquery.form.min.js?v=ea38b0f7"></script>
<script src="/static/admin/vendor/webuploader/webuploader.js?v=ea38b0f7"></script>
<script src="/static/admin/js/components/webuploader.js?v=ea38b0f7"></script>
<script src="/static/admin/js/apps/prod/product/sku_list.js?v=ea38b0f7"></script>