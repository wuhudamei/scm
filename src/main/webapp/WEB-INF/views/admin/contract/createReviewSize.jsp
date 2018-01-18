<%@ page contentType="text/html;charset=UTF-8" %>
<head>
    <style>
        .webuploader-pick {
            display: block;
        }
        .m-t-lg{
            margin-top: 13px;
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
                <form id="searchForm">

                    <div class="col-md-1">
                        <div class="form-group">
                            <button @click="addData()" id="createBtn" type="button"
                                    class="btn btn-block btn-outline btn-primary">新增
                            </button>
                        </div>
                    </div>
                </form>
            </div>
            <div class="row">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <div class="panel panel-default">

                        <div class="panel-heading" role="tab" id="headingOne" style="padding: 10px 9%;border:1px solid #c4c4c4">
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
                        <div id="collapseOne" class="panel-collapse collapse in" role="tabpanel"
                             aria-labelledby="headingOne">
                            <div class="panel-body">
                                <validator name="validation">
                                    <form novalidate class="form-inline">


                                        <div  v-for="(mainind, item) in list" track-by="id" style="margin-top: 6px;padding: 10px 9%;border:1px solid #c4c4c4">
                                            <div class="row">
                                                <div class="col-md-4 col-sm-4 m-t-lg">
                                                    <label >供应商：</label>
                                                    <div class="form-group "
                                                         :class="{'has-error':$validation[item.supplierField].invalid && $validation.touched}">
                                                        <%--<label :for="item.supplierField">供应商</label>--%>
                                                        <input
                                                                :id="item.supplierField"
                                                                :field="item.supplierField"
                                                                v-validate="{suppliers:{rule:item.supplier,message:'请选择供应商'}}"
                                                                v-model="item.supplier.length"
                                                                type="text"
                                                                class="form-control"
                                                                placeholder="供应商" style="display:none;">
                                                        <vue-select2
                                                                :url="'/api/supplier/findByName'"
                                                                :disabled="false"
                                                                :allow-clear="false"
                                                                :selected-list.sync="item.supplier"
                                                                :multiple="false"></vue-select2>
                                                                <span v-cloak
                                                                      v-if="$validation[item.supplierField].invalid && $validation.touched"
                                                                      class="help-absolute">
                                                                  <span v-for="error in $validation[item.supplierField].errors">
                                                                      {{error.message}} {{($index !== ($validation[item.supplierField].errors.length -1)) ? ',':''}}
                                                                  </span>
                                                                </span>
                                                    </div>
                                                </div>

                                                <div class="col-md-4 col-sm-4 m-t-lg">
                                                    <label >品牌：</label>
                                                    <div class="form-group"
                                                         :class="{'has-error':$validation[item.brandField].invalid && $validation.touched}">
                                                        <label class="sr-only" :for="item.brandField">品牌</label>
                                                        <input
                                                                :id="item.brandField"
                                                                :field="item.brandField"
                                                                v-validate="item.brandValidate"
                                                                v-model="item.brand"
                                                                type="text"
                                                                class="form-control"
                                                                placeholder="品牌" style="display:none;">
                                                        <vue-select2
                                                                :url="'/api/brand/findByName'"
                                                                :disabled="false"
                                                                :allow-clear="false"
                                                                :selected-list.sync="item.brand"
                                                                :multiple="false">
                                                                <span v-cloak
                                                                      v-if="$validation[item.brandField].invalid && $validation.touched"
                                                                      class="help-absolute">
                                                                        <span v-for="error in $validation[item.brandField].errors">
                                                                            {{error.message}} {{($index !== ($validation[item.brandField].errors.length -1)) ? ',':''}}
                                                                        </span>
                                                                </span>
                                                        </vue-select2>
                                                    </div>
                                                </div>

                                                <div class="col-md-4 col-sm-4 m-t-lg">
                                                    <div class="form-group" :class="{'has-error':$validation.prodCatalogId.invalid && $validation.touched}">
                                                        <select v-model="item.prodCatalogId"
                                                                id="prodCatalogId"
                                                                name="prodCatalogId"
                                                                v-validate:prod-catalog-id="{required:{rule:true,message:'请选择类目'}}"
                                                                class="form-control" placeholder="类目" style="margin-left: 0px">
                                                            <option value="" selected>请选择类目</option>
                                                            <option v-for="prodCatalog in prodCatalogs" :value="prodCatalog.id">{{{prodCatalog.name}}}</option>
                                                        </select>
                                                        <div v-cloak v-if="$validation.prodCatalogId.invalid && $validation.touched" class="help-absolute">
                                                            请选择类目
                                                        </div>
                                                    </div>
                                                </div>

                                            <%--<div class="col-md-4 m-t-lg">
                                                <div class="form-group " >
                                                    <label for="reviewSizeDate">复尺时间：</label>
                                                    <input id="reviewSizeDate"
                                                           name="reviewSizeDate"
                                                           class="form-control datepicker"
                                                           v-el:review-size-date
                                                           readonly
                                                           type="text"
                                                           v-model="item.reviewSizeDate">
                                                </div>
                                            </div>--%>
                                            <div class="col-md-4 col-sm-4 m-t-lg" style="margin-top: 25px">
                                                <div class="form-group" style="margin-left:25px"
                                                     :class="{'has-error':$validation[item.imagesField].invalid && $validation.touched}">
                                                    <!-- <label class="sr-only" for=""></label> -->
                                                        <web-uploader :target-id="'webuploader' + $index"
                                                                      :type="webUploader.type"
                                                                      :ind="$index"
                                                                      :validate="item.imagesValidate"
                                                                      :w-object-id="item.id"
                                                                      :w-accept="webUploader.accept"
                                                                      :w-server="webUploader.server"
                                                                      :w-file-size-limit="webUploader.fileSizeLimit"
                                                                      :w-file-single-size-limit="webUploader.fileSingleSizeLimit"
                                                                      :w-form-data="webUploader.formData">
                                                            <button type="button" class="btn btn-primary">上传图片</button>
                                                        </web-uploader>
                                                        <span v-cloak
                                                              v-if="$validation[item.imagesField].invalid && $validation.touched"
                                                              class="help-absolute">
                                                          <span v-for="error in $validation[item.imagesField].errors">
                                                              {{error.message}} {{($index !== ($validation[item.imagesField].errors.length -1)) ? ',':''}}
                                                          </span>
                                                        </span>
                                                </div>


                                                <div class="form-group">
                                                    <div class="clearfix">
                                                        <div v-for="(index,imag ) in item.images" class="pull-left"
                                                             style="margin-right:5px;position:relative;">
                                                            <button @click="deleteImg(index,imag.fullPath,mainind,'main')"
                                                                    style="font-size:18px;line-height:.5;position:absolute;top:1px;right:1px;"
                                                                    type="button" class="close"><span
                                                                    aria-hidden="true">&times;</span><span
                                                                    class="sr-only">Close</span></button>
                                                            <img :src="imag.fullPath" @click="bigImg(imag.fullPath)" alt="图片" width="60px"
                                                                 height="60px">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-4 col-sm-4 m-t-lg">
                                                <div class="form-group"
                                                         :class="{'has-error':$validation[item.remarkField].invalid && $validation.touched}">
                                                        <label  :for="item.remarkField">备注: </label>
                                                        <textarea
                                                                :id="item.remarkField"
                                                                :field="item.remarkField"
                                                                v-validate="item.remarkValidate"
                                                                v-model="item.remark"
                                                                maxlength="100"
                                                                type="text"
                                                                class="form-control"
                                                                placeholder="备注"
                                                                rows="2" cols="26">
                                                        </textarea>
                                                                <span v-cloak
                                                                  v-if="$validation[item.remarkField].invalid && $validation.touched"
                                                                  class="help-absolute">
                                                                    <span v-for="error in $validation[item.remarkField].errors" >
                                                                      {{error.message}} {{($index !== ($validation[item.remarkField].errors.length -1)) ? ',':''}}
                                                                    </span>
                                                                </span>

                                                    </div>
                                            </div>
                                                <div class="col-md-4 col-sm-4 m-t-lg" style="margin-top: 25px">
                                                    <button @click="removeData($index)" type="button"
                                                            class="btn btn-danger">删除
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                            <div class="row m-t-lg"  style="text-align: center">
                                                <button type="button" @click="closeWin" class="btn btn-default  m-t-lg">
                                                    取消
                                                </button>
                                                <button type="button" @click="insert" class="btn btn-primary  m-t-lg">保存
                                                </button>
                                            </div>

                                    </form>
                                </validator>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
    <!-- ibox end -->
</div>

<div id="imgModel" class="modal fade" tabindex="-1" >
    <div class="modal-body">
        <div style="margin: 0 auto;" class="bigImgWrap">
            <img style="width:100%;" :src="fullPath" alt="">
        </div>
    </div>
</div>

<%@include file="/WEB-INF/views/admin/components/select.jsp" %>
<script src="/static/admin/vendor/webuploader/webuploader.js"></script>
<script src="/static/admin/js/components/webuploader.js"></script>
<script src="/static/admin/js/components/select.js"></script>
<script src="/static/admin/js/apps/contract/createReviewSize.js"></script>