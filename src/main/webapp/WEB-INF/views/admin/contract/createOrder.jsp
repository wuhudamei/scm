<%@ page contentType="text/html;charset=UTF-8" %>
<div id="container" class="wrapper wrapper-content animated fadeInRight">
    <div id="breadcrumb">
        <bread-crumb :crumbs="breadcrumbs"></bread-crumb>
    </div>
    <!-- ibox start -->
    <div class="ibox">
        <div class="ibox-content">
            <h2 style="text-align:center">{{editID?'编辑订货单':'创建订货单'}}</h2>
            <h3 style="text-align:center">项目编号: {{contractCode}}</h3>
            <div class="row">
                <contract-info :contract-id="contractId"></contract-info>
                <!--col-sm-offset-1-->
                <div class="col-sm-12 " style="border:1px solid #c4c4c4;margin-top: 10px">
                    <validator name="validation">
                        <form novalidate class="form-horizontal">
                            <div class="row">
                                <div class="col-sm-1 m-t-lg col-sm-offset-1" v-if="change != ''">
                                    <button :disabled="submiting" type="button" @click="selectBrands(true)"
                                            class="btn btn-primary m-r-lg col-sm-offset-1">选择订购商品
                                    </button>
                                </div>
                            </div>

                            <div class="col-sm-12 m-t-lg" v-if="form.orderItemList.length!=0">
                                <label for="detail" class="control-label col-sm-1"></label>
                                <div class="border col-sm-10">
                                    <div v-for="(index,order) in form.orderItemList"
                                         class="border col-sm-12 margin10 orderDiv">
                                        <div class="close" @click="remove($index)" style="font-size: 37px">X</div>
                                        <p style="background-color: #c2ccd1;width: 91%;padding: 10px 0 10px 5px">序号： {{$index +1}}
                                            <span style="margin-left:30px">sku编号：</span>{{order.sku.code}}
                                            <span style="margin-left:30px">sku名称：</span>{{order.sku.name}}
                                            <span style="margin-left:30px">型号：</span>{{order.sku.product.model}}<br/>
                                            <span v-if="order.sku.skuMeta.attribute1Name != undefined && order.sku.skuMeta.attribute1Name != ''"><span style="margin-left:30px">{{order.sku.skuMeta.attribute1Name}}</span><span v-if="order.sku.attribute1 != undefined && order.sku.attribute1 != ''">：{{order.sku.attribute1}}</span></span>
                                            <span v-if="order.sku.skuMeta.attribute2Name != undefined && order.sku.skuMeta.attribute2Name != ''"><span style="margin-left:30px">{{order.sku.skuMeta.attribute2Name}}</span><span v-if="order.sku.attribute2 != undefined && order.sku.attribute2 != ''">：{{order.sku.attribute2}}</span></span>
                                            <span v-if="order.sku.skuMeta.attribute3Name != undefined && order.sku.skuMeta.attribute3Name != ''"><span style="margin-left:30px">{{order.sku.skuMeta.attribute3Name}}</span><span v-if="order.sku.attribute3 != undefined && order.sku.attribute3 != ''">：{{order.sku.attribute3}}</span></span>
                                            <span style="margin-left:30px">单位：</span>{{order.sku.product.measureUnitName}}</p>

                                        <div class="row">
                                            <div class="col-md-4 form-group">
                                                <label class="control-label col-sm-4">订货数量</label>
                                                <div class="col-sm-8">
                                                    <input
                                                            v-model="order.quantity"
                                                            type="number"
                                                            placeholder="订货数量"
                                                            class="form-control">
                                                </div>
                                            </div>

                                            <div class="col-md-4 form-group">
                                                <label class="control-label col-sm-5">预计安装时间</label>
                                                <div class="col-sm-7">
                                                    <input :id="'time'+$index"
                                                           v-model="order.installDate"
                                                           type="text"
                                                           placeholder="--预计安装时间--" class="datepicker form-control"
                                                           readonly/>
                                                </div>
                                            </div>
                                            <div class="col-md-4 form-group">
                                                <label class="control-label col-sm-4">安装位置</label>
                                                <div class="col-sm-8">
                                                    <input
                                                            v-model="order.installationLocation"
                                                            placeholder="安装位置"
                                                            class="form-control">
                                                </div>
                                            </div>

                                            <div class="col-md-12 form-group" v-if="order.sku.product.catalog.checkScale == 1">
                                                <label for="" class="control-label col-sm-1 " style="padding: 0 0 0 15px">复尺结果</label>
                                                <div class="col-md-10 " style="margin-left: 20px">
                                                    <input type="button" value="选择复尺结果" @click="selectReviewResults(true, $index)" style="margin-bottom: 10px">
                                                    <textarea
                                                            id="reviewSizeResult"
                                                            v-model="order.reviewSizeResult"
                                                            placeholder="复尺结果"
                                                            class="form-control">
                                                    </textarea>
                                                </div>
                                            </div>

                                            <div class="col-md-12 form-group">
                                                <label class="control-label col-sm-1" style="padding: 0 0 0 15px">备注</label>
                                                <div class="col-sm-10" style="margin-left: 20px">
                                                    <textarea
                                                            v-model="order.note"
                                                            placeholder="备注"
                                                            class="form-control">
                                                    </textarea>
                                                </div>
                                            </div>

                                        </div>

                                        <div class="row">
                                            <div class="col-sm-12" style="margin-bottom: 20px">
                                                <label class="col-sm-2 control-label">是否有其它费用?</label>
                                                <div class="col-sm-6" style="padding-top: 5px">
                                                    <div class="col-sm-3">
                                                        <input type="radio" id="otherTrue" :value="true" @change="hasOtherFeeChange()"
                                                               v-model="order.hasOtherFee">
                                                        <label for="otherTrue">是</label>
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <input type="radio" id="otherFalse" :value="false" @change="hasOtherFeeChange()"
                                                               v-model="order.hasOtherFee">
                                                        <label for="otherFalse">否</label>
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <button @click="addData(index)" id="createBtn" type="button" v-show="order.hasOtherFee"
                                                                class="btn btn-block btn-outline btn-primary btn-xs">新增
                                                        </button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="row col-sm-12 " v-if="order.hasOtherFee">
                                            <div v-for="(ind, item) in order.otherFeesList"  class="col-sm-12">
                                                <div class="col-sm-1"></div>
                                                <div class="col-sm-6">
                                                    <input
                                                            v-model="item.otherFee"
                                                            type="number"
                                                            placeholder="其它费用,例如：100元"
                                                            class="form-control">
                                                </div>
                                                <div class="col-md-4 form-group">
                                                    <select v-model="item.otherFeeNote" class="form-control">
                                                        <option value="">请选择其它费用说明</option>
                                                        <option v-for="dict in otherFeeNoteList" :value="dict.dicValue">{{dict.dicName}}</option>
                                                    </select>
                                                </div>
                                                <div class="col-md-1">
                                                    <button @click="removeData(index, ind)" type="button"
                                                            class="btn btn-default">删除
                                                    </button>
                                                </div>
                                            </div>
                                        </div>


                                    </div>
                                </div>
                            </div>

                            <div class="col-sm-12 m-t-lg" style="padding: 0">
                                <label for="note" class="control-label col-sm-1"></label>
                                <div class="col-sm-10">
                                        <textarea id="note" v-model="form.note" type="text" placeholder="说明"
                                                  class="form-control"></textarea>
                                </div>
                            </div>

                            <div class="col-sm-12 m-t-lg" style="margin-bottom: 30px">
                                <div class=" col-sm-1"></div>
                                <div class=" col-sm-10" style="padding-left: 0">
                                    <button type="button" @click="closeWin" class="btn btn-default m-r-lg">取消</button>
                                    <button :disabled="submiting" type="button" @click="saveProduct(true)"
                                            class="btn btn-primary m-r-lg">保存
                                    </button>
                                </div>
                            </div>
                        </form>
                    </validator>
                </div>

            </div>

        </div>
    </div>
    <!-- ibox end -->
</div>

<div id="modalBrand" class="modal fade" tabindex="-1" data-width="1000" v-cloak>
    <div class="modal-header">
        <h3 align="center">选择订购商品</h3>
    </div>
    <div class="modal-body">
        <div class="row" v-if="type">


            <div class="col-md-6 form-group">
                <label for="allStores" class="control-label col-sm-4">门店</label>
                <div class="col-sm-8">
                    <select name="allStores" id="allStores" class="form-control"
                            v-model="allStoreCode">
                        <option value="">请选择</option>
                        <option value="{{item.code}}" v-for="item of allStores">{{item.name}}</option>
                    </select>
                </div>
            </div>

            <div class="col-md-6 form-group">
                <label for="allSuppliers" class="control-label col-sm-4">区域供应商</label>
                <div class="col-sm-8">
                    <select name="allSuppliers" id="allSuppliers" class="form-control"
                            v-model="allSupplierId">
                        <option value="">请选择</option>
                        <option value="{{item.id}}" v-for="item of allSuppliers">{{item.name}}</option>
                    </select>

                </div>
            </div>

            <div class="col-md-6 form-group">
                <label for="" class="control-label col-sm-4">商品供应商</label>
                <div class="col-sm-8">
                    <select name="" id="" class="form-control"
                            v-model="form.supplierId">
                        <option value="">请选择</option>
                        <option value="{{item.id}}" v-for="item of suppliers">{{item.name}}</option>
                    </select>
                </div>
            </div>

            <div class="col-md-6 form-group">
                <label for="productCategory" class="control-label col-sm-4">商品类目</label>
                <div class="col-sm-8">
                    <select id="productCategrory" v-model="form.catalogUrl" class="form-control">
                        <option value="">请选择商品类目</option>
                        <option v-for="category in categories" :value="category.url">{{{category.name}}}
                        </option>
                    </select>
                </div>
            </div>

            <div class="col-md-6 form-group">
                <label for="" class="control-label col-sm-4">关键字</label>
                <div class="col-sm-8">
                    <input v-model="form.keyword" type="text" placeholder="sku名称 | sku代码 | 商品型号 | 商品品牌" class="form-control"/>
                </div>
            </div>

            <div class="col-md-4 col-md-offset-8">
                <div class="form-group">
                    <button id="searchBtn" @click="query" type="button" class="btn btn-block btn-outline btn-primary"
                            alt="搜索" title="搜索">
                        <i class="fa fa-search"></i>
                    </button>
                </div>
            </div>
        </div>

        <table id="dataTableBrand" width="100%" class="table table-striped table-bordered table-hover"/>
        </table>
    </div>
    <div class="modal-footer">
        <button type="button" data-dismiss="modal" class="btn">关闭</button>
        <button type="button" @click="choose" class="btn btn-primary">选择</button>
    </div>
</div>


<div id="modalReviewResult" class="modal fade" tabindex="-1" data-width="1000" v-cloak>
    <div class="modal-header">
        <h3 align="center">选择复尺信息</h3>
    </div>
    <div class="modal-body">
        <table id="dataTableReviewResult" width="100%" class="table table-striped table-bordered table-hover"/>
        </table>
    </div>
    <div class="modal-footer">
        <button type="button" data-dismiss="modal" class="btn">关闭</button>
        <button type="button" @click="choose" class="btn btn-primary">选择</button>
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

    .sku input, .sku label {
        padding: 6px 12px;
    }
</style>
<%@include file="/WEB-INF/views/admin/components/select.jsp" %>
<script src="/static/admin/js/components/select.js"></script>
<script src="${ctx}/static/admin/js/components/jquery.form.min.js?v=ea38b0f7"></script>
<script src="${ctx}/static/admin/js/components/ImportExcelUtil.js?v=ea38b0f7"></script>
<script src="/static/admin/vendor/webuploader/webuploader.js?v=ea38b0f7"></script>
<script src="/static/admin/js/components/webuploader.js?v=ea38b0f7"></script>
<script src="/static/admin/js/apps/contract/createOrder.js?v=ea38b0f7"></script>