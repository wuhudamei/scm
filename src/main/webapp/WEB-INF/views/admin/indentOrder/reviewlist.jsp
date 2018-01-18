<%@ page contentType="text/html;charset=UTF-8" %>
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
                <form id="searchForm1" @submit.prevent="query">

                    <div class="col-md-4 form-group">
                        <input v-model="form.keyword" type="text"
                               placeholder="订货单号，项目单号，制单人，客户姓名" class="form-control"/>
                    </div>

                    <div class="col-md-4 form-group">
                        <select v-model="form.status" class="form-control">
                            <option value="">请选择状态</option>
                            <option value="0">未复尺</option>
                            <option value="1">已复尺</option>
                            <option value="2">已驳回</option>
                        </select>
                    </div>

                    <div class="col-md-4 form-group">
                        <label class="sr-only" for="brandId"></label>
                        <select v-model="form.brandId"
                                id="brandId"
                                name="brandId"
                                class="form-control">
                            <option value="">请选择品牌</option>
                            <option v-for="brand in brands" :value="brand.id">{{brand.name}}</option>
                        </select>
                    </div>

                    <div class="col-md-4 form-group">
                        <input v-model="form.startDate" v-el:start-date id="startDate"
                               name="startDate" type="text" readonly
                               class="form-control datepicker" placeholder="请选择开始时间">
                    </div>
                    <div class="col-md-4 form-group">
                        <input v-model="form.endDate" v-el:end-date id="endDate"
                               name="endDate" type="text" readonly
                               class="form-control datepicker" placeholder="请选择结束时间">
                    </div>

                    <div class="col-md-4 form-group">
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

<div id="saveModel" class="modal fade" tabindex="-1">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 align="center">填写复尺结果</h3>
    </div>
    <div class="modal-body" style="overflow:auto;height:initial;">
        <!-- ibox start -->
        <div class="ibox">
            <div class="ibox-title">项目信息</div>
            <div class="ibox-content">
                <div class="row">
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
        <div class="ibox">
            <div class="ibox-title">复尺通知信息</div>
            <div class="ibox-content">
                <div class="row">
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
                        <div v-for="image in img" class="pull-left"
                             style="margin-right:5px;position:relative;">
                            <img :src="image.fullPath" @click="bigImg(image.fullPath)" alt="图片" width="60px"
                                 height="60px">
                        </div>
                    </div>
                    <div class="col-sm-4 col-xs-12">
                        <label for="">备注：</label>
                        {{reviewSizeResult.remark}}
                    </div>
                </div>
            </div>
        </div>
        <div class="ibox">
            <div class="ibox-title">
                <b>复尺结果</b>
                <button @click="addData()" id="createBtn" type="button" class="btn btn-default btn-sm"
                        style="margin-left: 15px;">新增
                </button>
            </div>
            <div class="ibox-content">
                <form name="user" novalidate class="form-inline" role="form">
                    <div v-for="item in list" track-by="id">
                        <div class="row" style="border: solid #A6A6A6 2px;margin-top: 15px;padding-bottom: 10px;">
                            <div class="col-md-12 " style="margin-top:10px;">
                                <div class="form-group col-md-4">
                                    <input v-model="item.productName"
                                           type="text"
                                           placeholder="商品名称"
                                           class="form-control"/>
                                </div>
                                <div class="form-group col-md-4">
                                    <input v-model="item.location"
                                           type="text"
                                           placeholder="安装位置"
                                           class="form-control"/>
                                </div>
                                <div class="form-group col-md-4">
                                    <input v-model="item.model"
                                           type="text"
                                           placeholder="型号"
                                           class="form-control"/>
                                </div>
                            </div>
                            <div class="col-md-12 " style="margin-top:10px;">
                                <div class="form-group col-md-4">
                                    <input v-model="item.specification"
                                           type="text"
                                           placeholder="产品名称/规格"
                                           class="form-control"/>
                                </div>
                                <div class="form-group col-md-4">
                                    <input v-model="item.count"
                                           type="number"
                                           placeholder="数量"
                                           class="form-control"/>
                                </div>
                                <div class="form-group col-md-4">
                                    <input v-model="item.unit"
                                           placeholder="单位"
                                           class="form-control"/>
                                </div>
                            </div>
                            <div class="col-md-12 " style="margin-top:10px;">
                                <%--<div class="form-group col-md-4"--%>
                                <%--:class="{'has-error':$validation[item.prodCatalogIdField].invalid && $validation.touched}">--%>
                                <%--<select v-model="item.prodCatalogId"--%>
                                <%--:field="item.prodCatalogIdField"--%>
                                <%--v-validate="item.prodCatalogIdValidate"--%>
                                <%--class="form-control" style="width:190px;">--%>
                                <%--<option value="">请选择类目</option>--%>
                                <%--<option v-for="prodCatalog in prodCatalogs"--%>
                                <%--:value="prodCatalog.id">{{{prodCatalog.name}}}--%>
                                <%--</option>--%>
                                <%--</select>--%>
                                <%--<span v-cloak--%>
                                <%--v-if="$validation[item.prodCatalogIdField].invalid && $validation.touched"--%>
                                <%--class="help-absolute">--%>
                                <%--<span v-for="error in $validation[item.prodCatalogIdField].errors">--%>
                                <%--{{error.message}} {{($index !== ($validation[item.prodCatalogIdField].errors.length -1)) ? ',':''}}--%>
                                <%--</span>--%>
                                <%--</span>--%>
                                <%--</div>--%>
                                <div class="form-group col-md-4">
                                    <textarea v-model="item.remark"
                                              type="text" placeholder="备注"
                                              class="form-control"
                                              style="padding-left: 25px;">
                                    </textarea>
                                </div>
                                <div class="form-group col-md-4">
                                    <button @click="removeData($index)" type="button" style="text-align: center"
                                            class="btn btn-default">删除
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div class="modal-footer" style="text-align: center">
        <button type="button" @click="insert" class="btn btn-primary">保存</button>
        <button type="button" data-dismiss="modal" class="btn">关闭</button>
    </div>
</div>
<div id="viewModel" class="modal fade" tabindex="-1">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 align="center">查看复尺结果</h3>
    </div>
    <div class="modal-body" style="overflow:auto;height:initial;">
        <!-- ibox start -->
        <div class="ibox">
            <div class="ibox-title">项目信息</div>
            <div class="ibox-content">
                <div class="row">
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
        <div class="ibox">
            <div class="ibox-title">复尺通知信息</div>
            <div class="ibox-content">
                <div class="row">
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
                        <div v-for="image in img" class="pull-left"
                             style="margin-right:5px;position:relative;">
                            <img :src="image.fullPath" @click="bigImg(image.fullPath)" alt="图片" width="60px"
                                 height="60px">
                        </div>
                    </div>
                    <div class="col-sm-4 col-xs-12">
                        <label for="">备注：</label>
                        {{reviewSizeResult.remark}}
                    </div>
                </div>
            </div>
        </div>
        <div class="ibox">
            <div class="ibox-title">
                <b>复尺结果</b>
            </div>
            <div class="ibox-content">
                <form name="user" novalidate class="form-inline" role="form">
                    <div v-for="item in list" track-by="id">
                        <div class="row" style="border: solid #A6A6A6 2px;margin-top: 15px;padding-bottom: 10px;">
                            <div class="col-md-12 " style="margin-top:10px;">
                                <div class="form-group col-md-4">
                                    <input v-model="item.productName"
                                           type="text"
                                           placeholder="商品名称"
                                           class="form-control"/>
                                </div>
                                <div class="form-group col-md-4">
                                    <input v-model="item.location"
                                           type="text"
                                           placeholder="安装位置"
                                           class="form-control"/>
                                </div>
                                <div class="form-group col-md-4">
                                    <input v-model="item.model"
                                           type="text"
                                           placeholder="型号"
                                           class="form-control"/>
                                </div>
                            </div>
                            <div class="col-md-12 " style="margin-top:10px;">
                                <div class="form-group col-md-4">
                                    <input v-model="item.specification"
                                           type="text"
                                           placeholder="产品名称/规格"
                                           class="form-control"/>
                                </div>
                                <div class="form-group col-md-4">
                                    <input v-model="item.count"
                                           type="number"
                                           placeholder="数量"
                                           class="form-control"/>
                                </div>
                                <div class="form-group col-md-4">
                                    <input v-model="item.unit"
                                           placeholder="单位"
                                           class="form-control"/>
                                </div>
                            </div>
                            <div class="col-md-12 " style="margin-top:10px;">
                                <%--<div class="form-group col-md-4">--%>
                                    <%--<select v-model="item.prodCatalogId"--%>
                                            <%--class="form-control" style="width:190px;">--%>
                                        <%--<option value="">请选择类目</option>--%>
                                        <%--<option v-for="prodCatalog in prodCatalogs"--%>
                                                <%--:value="prodCatalog.id">{{{prodCatalog.name}}}--%>
                                        <%--</option>--%>
                                    <%--</select>--%>
                                <%--</div>--%>
                                <div class="form-group col-md-4">
                                    <textarea v-model="item.remark"
                                              type="text" placeholder="备注"
                                              class="form-control"
                                              style="padding-left: 25px;">
                                    </textarea>
                                </div>
                                <div class="form-group col-md-4">
                                    <button @click="removeData($index)" type="button" style="text-align: center"
                                            class="btn btn-default">删除
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div class="modal-footer" style="text-align: center">
        <button type="button" @click="save" class="btn btn-primary">保存</button>
        <button type="button" data-dismiss="modal" class="btn">关闭</button>
    </div>
</div>

<div id="rejectModel" class="modal fade" tabindex="-1">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
                aria-hidden="true">×
        </button>
        <h3 align="center">驳回复尺通知单</h3>
    </div>

    <div class="modal-body">
        <validator name="validation">
            <form name="banner" novalidate class="form-horizontal" role="form">
                <input type="hidden" v-model="id">
                <div class="col-sm-12 m-t-lg form-group"
                     :class="{'has-error': $validation.rejecttype.invalid && $validation.touched}">
                    <label for="rejecttype" class="control-label col-sm-5"><span
                            style="color: red">*</span>驳回类型：</label>
                    <div class="col-sm-7">
                        <select v-model="rejectType" class="form-control" v-validate:rejecttype="{required:true}">
                            <option value="">请选择驳回类型</option>
                            <option v-for="reject in rejects" :value="reject.id">{{reject.dicName}}</option>
                        </select>
                        <span v-cloak
                              v-if="$validation.rejecttype.invalid && $validation.touched"
                              class="help-absolute"> 请选择驳回类型 </span>
                    </div>
                </div>
                <div class="col-sm-12 m-t-lg form-group"
                     :class="{'has-error': $validation.remark.invalid && $validation.touched}">
                    <label for="remark" class="control-label col-sm-5"><span
                            style="color: red">*</span>驳回原因：</label>
                    <div class="col-sm-7">
                        <textarea v-model="remark" placeholder="驳回原因" class="form-control"
                                  v-validate:remark="{required:true}"
                                  style="margin-top: 5px;margin-bottom: 5px;">

                        </textarea>
                        <span v-cloak
                              v-if="$validation.remark.invalid && $validation.touched"
                              class="help-absolute"> 请填写驳回原因 </span>
                    </div>
                </div>
            </form>
        </validator>
    </div>
    <div class="modal-footer" style="text-align: center">
        <button type="button" @click="save" class="btn btn-primary">提交</button>
        <button type="button" data-dismiss="modal" class="btn">关闭</button>
    </div>
</div>
<div id="imgModel" class="modal fade" tabindex="-1">
    <div class="modal-body">
        <div style="margin: 0 auto;" class="bigImgWrap">
            <img style="width:100%;" :src="fullPath" alt="">
        </div>
    </div>
</div>
<div id="rejectRecordModel" class="modal fade" tabindex="-1">
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
                <td style="text-align: center" width="10%">{{rejectReason.createName}}</td>
                <td style="text-align: center" width="20%">{{rejectReason.createTime}}</td>
                <td style="text-align: center" width="15%">{{rejectReason.indentOrderRejectType}}</td>
                <td style="text-align: center" width="55%">{{rejectReason.rejectReason}}</td>
                .
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


<!-- 上传 复尺记录-->
<div id="modal" class="modal fade" tabindex="-1" data-width="600">
    <validator name="validation">
        <form name="banner" novalidate class="form-horizontal" role="form">
            <input type="hidden" v-model="id">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3 align="center">导入复尺记录</h3>
            </div>
            <div class="modal-body">
                <!-- 上传文件 -->
                <div class="form-group">
                    <label for="logo" class="col-sm-3 control-label">复尺记录文件:</label>
                    <div class="col-sm-9">
                        <div class="input-group">
                            <input
                                    id="logo"
                                    v-model="logo"
                                    readonly type="text"
                                    data-tabindex="1"
                                    placeholder="点击上传文件" class="input-sm form-control">
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
                        <div class="input-group" style="color: red;margin-top: 10px">上传大小限制为20M,上传格式为xlsx/xls</div>
                        <div
                                style="margin-bottom:0;"
                                :class="{'progress-uploading':uploading}"
                                class="progress progress-striped active">
                            <div
                                    :style="{ width:progress+'%'}"
                                    aria-valuemax="100"
                                    aria-valuemin="0"
                                    aria-valuenow="75"
                                    role="progressbar"
                                    class="progress-bar">
                                <span class="sr-only"></span>
                            </div>
                        </div>
                    </div>
                </div>

            </div>

            <div class="modal-footer">
                <button type="button" data-dismiss="modal" class="btn">关闭</button>
            </div>
        </form>
    </validator>
</div>
<%@include file="/WEB-INF/views/admin/components/select.jsp" %>
<script src="/static/admin/vendor/webuploader/webuploader.js"></script>
<script src="/static/admin/js/components/webuploader.js"></script>
<script src="/static/admin/js/components/select.js"></script>
<script src="/static/admin/js/apps/indentOrder/reviewlist.js?v=ea38b0f7"></script>