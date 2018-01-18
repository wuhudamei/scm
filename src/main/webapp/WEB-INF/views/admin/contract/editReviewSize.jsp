<%@ page contentType="text/html;charset=UTF-8" %>
<head>
    <style>
        .webuploader-pick {
            display: block;
        }
    </style>
    <link rel="stylesheet" href="/static/admin/css/select.css">
</head>
<div id="container" class="wrapper wrapper-content animated fadeInRight">
    <div id="breadcrumb">
        <bread-crumb :crumbs="breadcrumbs"></bread-crumb>
    </div>
    <!-- ibox start -->
    <div class="ibox">
        <div class="ibox-content">

            <div class="row">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <div class="panel panel-default">
                        <div class="panel-heading" role="tab" id="headingOne"
                             style="padding: 10px 9%;border:1px solid #c4c4c4">
                            <div :class="{hasHeight:isShow }" class="row detail-stranding-book detail-state" v-cloak>
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
                        <%--<div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne">--%>
                        <%--<div class="panel-body">--%>
                        <%--<validator name="validation">--%>
                        <%--<form novalidate class="form-inline">--%>
                        <%--<div class="row">--%>
                        <%--<div v-for="(mainind, item) in list" track-by="id" style="padding:10px;">--%>
                        <%--<div class="form-group" :class="{'has-error':$validation[item.supplierField].invalid && $validation.touched}">--%>
                        <%--<label class="sr-only" :for="item.supplierField">供应商</label>--%>
                        <%--<input--%>
                        <%--:id="item.supplierField"--%>
                        <%--:field="item.supplierField"--%>
                        <%--v-validate="{suppliers:{rule:item.supplier,message:'请选择供应商'}}"--%>
                        <%--v-model="item.supplier.length"--%>
                        <%--type="text"--%>
                        <%--class="form-control"--%>
                        <%--placeholder="供应商" style="display:none;">--%>
                        <%--<vue-select2--%>
                        <%--:disabled="false"--%>
                        <%--:allow-clear="false"--%>
                        <%--:selected-list.sync="item.supplier"--%>
                        <%--:multiple="false">--%>
                        <%--</vue-select2>--%>
                        <%--<span v-cloak v-if="$validation[item.supplierField].invalid && $validation.touched" class="help-absolute">--%>
                        <%--<span v-for="error in $validation[item.supplierField].errors">--%>
                        <%--{{error.message}} {{($index !== ($validation[item.supplierField].errors.length -1)) ? ',':''}}--%>
                        <%--</span>--%>
                        <%--</span>--%>
                        <%--</div>--%>
                        <%--<div class="form-group" :class="{'has-error':$validation[item.brandField].invalid && $validation.touched}">--%>
                        <%--<label class="sr-only" :for="item.brandField">品牌</label>--%>
                        <%--<input--%>
                        <%--:id="item.brandField"--%>
                        <%--:field="item.brandField"--%>
                        <%--v-validate="item.brandValidate"--%>
                        <%--v-model="item.brand"--%>
                        <%--type="text"--%>
                        <%--class="form-control"--%>
                        <%--placeholder="品牌" style="display:none;">--%>
                        <%--<vue-select2--%>
                        <%--:url="'/api/brand/getAll'"--%>
                        <%--:disabled="false"--%>
                        <%--:allow-clear="false"--%>
                        <%--:selected-list.sync="item.brand"--%>
                        <%--:multiple="false">--%>
                        <%--<span v-cloak v-if="$validation[item.brandField].invalid && $validation.touched" class="help-absolute">--%>
                        <%--<span v-for="error in $validation[item.brandField].errors">--%>
                        <%--{{error.message}} {{($index !== ($validation[item.brandField].errors.length -1)) ? ',':''}}--%>
                        <%--</span>--%>
                        <%--</span>--%>
                        <%--</div>--%>
                        <%--<div class="form-group" :class="{'has-error':$validation[item.imagesField].invalid && $validation.touched}">--%>
                        <%--<!-- <label class="sr-only" for=""></label> -->--%>
                        <%--<web-uploader :target-id="'webuploader' + $index"--%>
                        <%--:type="webUploader.type"--%>
                        <%--:ind="$index"--%>
                        <%--:validate="item.imagesValidate"--%>
                        <%--:w-object-id="item.id"--%>
                        <%--:w-accept="webUploader.accept"--%>
                        <%--:w-server="webUploader.server"--%>
                        <%--:w-file-size-limit="webUploader.fileSizeLimit"--%>
                        <%--:w-file-single-size-limit="webUploader.fileSingleSizeLimit"--%>
                        <%--:w-form-data="webUploader.formData">--%>
                        <%--<button type="button" class="btn btn-primary">上传图片</button>--%>
                        <%--</web-uploader>--%>
                        <%--<span v-cloak v-if="$validation[item.imagesField].invalid && $validation.touched" class="help-absolute">--%>
                        <%--<span v-for="error in $validation[item.imagesField].errors">--%>
                        <%--{{error.message}} {{($index !== ($validation[item.imagesField].errors.length -1)) ? ',':''}}--%>
                        <%--</span>--%>
                        <%--</span>--%>

                        <%--</div>--%>
                        <%--<div class="form-group">--%>
                        <%--<div  class="clearfix">--%>
                        <%--<div v-for="(index,imag ) in item.images" class="pull-left" style="margin-right:5px;position:relative;">--%>
                        <%--<button @click="deleteImg(index,imag.fullPath,mainind,'main')" style="font-size:18px;line-height:.5;position:absolute;top:1px;right:1px;"--%>
                        <%--type="button" class="close"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>--%>
                        <%--<img :src="imag.fullPath" alt="图片" width="60px" height="60px">--%>
                        <%--</div>--%>
                        <%--</div>--%>
                        <%--</div>--%>
                        <%--<div class="form-group" :class="{'has-error':$validation[item.remarkField].invalid && $validation.touched}">--%>
                        <%--<label class="sr-only" :for="item.remarkField">备注</label>--%>
                        <%--<input--%>
                        <%--:id="item.remarkField"--%>
                        <%--:field="item.remarkField"--%>
                        <%--v-validate="item.remarkValidate"--%>
                        <%--v-model="item.remark"--%>
                        <%--maxlength="100"--%>
                        <%--type="text"--%>
                        <%--class="form-control"--%>
                        <%--placeholder="备注">--%>
                        <%--<span v-cloak v-if="$validation[item.remarkField].invalid && $validation.touched" class="help-absolute">--%>
                        <%--<span v-for="error in $validation[item.remarkField].errors">--%>
                        <%--{{error.message}} {{($index !== ($validation[item.remarkField].errors.length -1)) ? ',':''}}--%>
                        <%--</span>--%>
                        <%--</span>--%>
                        <%--</div>--%>

                        <%--<button @click="removeData($index)" type="button" class="btn btn-default">删除</button>--%>
                        <%--</div>--%>
                        <%--<div class="modal-footer" style="text-align: center">--%>
                        <%--<button  type="button" @click="closeWin" class="btn btn-default m-r-lg">取消</button>--%>
                        <%--<button type="button" @click="insert" class="btn btn-primary">保存</button>--%>
                        <%--</div>--%>
                        <%--</div>--%>
                        <%--</form>--%>
                        <%--</validator>--%>
                        <%----%>
                        <%--</div>--%>
                        <%--</div>--%>
                        <table id="dataTable" width="100%"
                               class="table table-striped table-bordered table-hover">
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- ibox end -->
</div>
<div id="model" class="modal fade" tabindex="-1" >
    <%--<div class="modal-header">--%>
        <%--<button type="button" class="close" data-dismiss="modal"--%>
                <%--aria-hidden="true">×--%>
        <%--</button>--%>
        <%--<h3 align="center">图片详情</h3>--%>
    <%--</div>--%>

    <div class="modal-body">
        <div style="margin: 0 auto;" class="bigImgWrap">
            <img style="width:100%;" :src="fullPath" alt="">
        </div>
    </div>
    <%--<div class="modal-footer">--%>
        <%--<button type="button" data-dismiss="modal" class="btn">关闭</button>--%>
    <%--</div>--%>

</div>
<div id="rejectRecordModel" class="modal fade" tabindex="-1" >
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
                aria-hidden="true">×
        </button>
        <h3 align="center">驳回记录</h3>
    </div>
    <div class="modal-body">
        <table id="dataTable3" class="table table-bordered">
            <tr id="titileTr2">
                <td style="text-align: center;font-weight:bold">操作人</td>
                <td style="text-align: center;font-weight:bold">操作时间</td>
                <td style="text-align: center;font-weight:bold">驳回类型</td>
                <td style="text-align: center;font-weight:bold">驳回原因</td>
            </tr>
            <tr v-for="rejectReason in rejectReasons">
                <td  style="text-align: center" width="10%">{{rejectReason.createName}}</td>
                <td  style="text-align: center" width="20%">{{rejectReason.createTime}}</td>
                <td style="text-align: center" width="15%">{{rejectReason.indentOrderRejectType}}</td>
                <td style="text-align: center" width="55%">{{rejectReason.rejectReason}}</td>
            </tr>
            <tr v-if="stRdBilltems.length === 0">
                <td class="text-center" colspan="11">没有找到匹配的记录</td>
            </tr>
        </table>
    </div>
    <div class="modal-footer">
        <button type="button" data-dismiss="modal" class="btn">关闭</button>
    </div>
</div>
<div id="imgModel" class="modal fade" tabindex="-1" >
    <div class="modal-body">
        <div style="margin: 0 auto;" class="bigImgWrap">
            <img style="width:100%;" :src="fullPath" alt="">
        </div>
    </div>
</div>
<div id="editModel" class="modal fade" tabindex="-1">
    <!-- ibox start -->
    <div class="ibox">
        <div class="ibox-content">
            <div class="row">
            </div>
            <div class="row">
                <div class="panel-group" id="accordion1" role="tablist" aria-multiselectable="true">
                    <div class="panel panel-default">


                        <div class="panel-heading" role="tab" id="headingOne1"
                             style="padding: 10px 9%;border:1px solid #c4c4c4">
                            <div :class="{hasHeight:isShow }" class="row detail-stranding-book detail-state" v-cloak>
                                <div class="col-sm-4 col-xs-12">
                                    <label for="">供应商：</label>
                                    {{reviewSizeResult.supplierId}}
                                </div>
                                <div class="col-sm-4 col-xs-12">
                                    <label for="">品牌：</label>
                                    {{reviewSizeResult.brandId}}
                                </div>
                                <div class="col-sm-4 col-xs-12">
                                    <label style="float:left;">图片：</label>
                                    <div v-for="image in img"  class="pull-left"
                                         style="margin-right:5px;position:relative;">
                                        <img :src="image.fullPath" alt="图片" @click="bigImg(image.fullPath)"  width="60px" height="60px">
                                    </div>
                                </div>
                                <div class="col-sm-4 col-xs-12">
                                    <label for="">备注：</label>
                                    {{reviewSizeResult.remark}}
                                </div>
                            </div>
                        </div>
                        <div class="modal-body">
                            <table id="dataTable4" class="table table-bordered">
                                <tr id="titileTr4">
                                    <td style="text-align: center;font-weight:bold">商品名称</td>
                                    <td style="text-align: center;font-weight:bold">安装位置</td>
                                    <td style="text-align: center;font-weight:bold">型号</td>
                                    <td style="text-align: center;font-weight:bold">规格</td>
                                    <td style="text-align: center;font-weight:bold">数量</td>
                                    <td style="text-align: center;font-weight:bold">单位</td>
                                    <td style="text-align: center;font-weight:bold">类目</td>
                                    <td style="text-align: center;font-weight:bold">备注</td>
                                </tr>
                                <tr v-for="reviewSizeResult in reviewSizeResults">
                                    <td  style="text-align: center">{{reviewSizeResult.productName}}</td>
                                    <td  style="text-align: center" >{{reviewSizeResult.location}}</td>
                                    <td style="text-align: center" >{{reviewSizeResult.model}}</td>
                                    <td style="text-align: center" >{{reviewSizeResult.specification}}</td>
                                    <td style="text-align: center">{{reviewSizeResult.count}}</td>
                                    <td style="text-align: center">{{reviewSizeResult.unit}}</td>
                                    <td style="text-align: center">{{reviewSizeResult.prodCatalogName}}</td>
                                    <td style="text-align: center">{{reviewSizeResult.remark}}</td>
                                </tr>
                                <tr v-if="stRdBilltems.length === 0">
                                    <td class="text-center" colspan="11">没有找到匹配的记录</td>
                                </tr>
                            </table>
                        </div>
                        <div class="modal-footer" style="text-align: center">
                            <button type="button" data-dismiss="modal" class="btn">关闭</button>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/views/admin/components/select.jsp" %>
<script src="/static/admin/vendor/webuploader/webuploader.js"></script>
<script src="/static/admin/js/components/webuploader.js"></script>
<script src="/static/admin/js/components/select.js"></script>
<script src="/static/admin/js/apps/contract/editReviewSize.js"></script>