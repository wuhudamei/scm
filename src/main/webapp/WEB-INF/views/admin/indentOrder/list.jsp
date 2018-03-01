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
                               placeholder="订货单号，项目单号，制单人，客户姓名" class="form-control"/>
                    </div>

                    <div class="col-md-4 form-group">
                        <select v-model="form.status" class="form-control">
                            <option value="">请选择状态</option>
                            <option value="DRAFT">备货单</option>
                            <option value="NOTIFIED">订货单</option>
                            <option value="INVALID">已作废</option>
                            <option value="REJECT">已驳回</option>
                            <option value="REJECTINSTALL">驳回安装</option>
                            <option value="ALREADY_INSTALLED">已安装</option>
                            <option value="NOTRECONCILED">未对账</option>
                            <option value="HASBEENRECONCILED">已对账</option>
                        </select>
                    </div>

                    <div class="col-md-4 form-group" v-if="accType != 'PROD_SUPPLIER'">
                        <label class="sr-only" for="brandId"></label>
                        <select v-model="form.brandId"
                                id="brandId"
                                name="brandId"
                                class="form-control">
                            <option value="">请选择品牌</option>
                            <option v-for="brand in brands" :value="brand.id">{{brand.name}}</option>
                        </select>
                    </div>

                    <div class="col-md-4 form-group" >
                        <label class="sr-only" for="dateType"></label>
                        <select v-model="form.dateType"
                                id="dateType"
                                name="dateType"
                                class="form-control">
                            <option value="">选择时间类型</option>
                            <option value="CREATE_DATE">创建时间</option>
                            <option value="ACCEPT_DATE">接单时间</option>
                            <option value="DOWNLOAD_DATE">下载时间</option>
                            <option value="NOTICE_INSTALL_TIME">通知安装时间</option>
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

                    <div class="col-md-4 form-group" >
                        <label class="sr-only" for="acceptStatus"></label>
                        <select v-model="form.acceptStatus"
                                id="acceptStatus"
                                name="acceptStatus"
                                class="form-control">
                            <option value="">选择接单状态</option>
                            <option value="YES">已接收</option>
                            <option value="NO">未接受</option>
                        </select>
                    </div>
                    <div class="col-md-4 form-group" >
                        <label class="sr-only" for="download"></label>
                        <select v-model="form.download"
                                id="download"
                                name="download"
                                class="form-control">
                            <option value="">选择下载状态</option>
                            <option value="YES">已下载</option>
                            <option value="NO">未下载</option>
                        </select>
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

<!-- 作废Modal -->
<div id="cancleModal" class="modal fade" tabindex="-1" data-width="600">
    <validator name="validation">
        <form name="banner" novalidate class="form-horizontal" role="form">
            <input type="hidden" v-model="id">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×
                </button>
                <h3 align="center">作废订货单</h3>
            </div>

            <div class="modal-body" style="height:300px;">
                <div class="col-sm-8 m-t-lg form-group"
                     :class="{'has-error':$validation.code.invalid && $validation.touched}">
                    <label for="code" class="control-label col-sm-4"><span
                            style="color: red">*</span>订货单号：</label>
                    <div class="col-sm-8">
                        <input id="code" v-model="code"
                               v-validate:contractCode="{required:{rule:true,message:'请输入订货单号'},maxlength:{rule:40,message:'订货单号最长不能超过40个字符'}}"
                               data-tabindex="1" readonly type="text" placeholder="订货单号"
                               class="form-control"> <span v-cloak
                                                           v-if="$validation.code.invalid && $validation.touched"
                                                           class="help-absolute"> <span
                            v-for="error in $validation.code.errors"> {{error.message}}
							{{($index !== ($validation.code.errors.length -1)) ? ',':''}} </span>
					</span>
                    </div>
                </div>
                <div class="col-sm-8 m-t-lg form-group"
                     :class="{'has-error': $validation.reason.invalid && $validation.touched}">
                    <label for="reason" class="control-label col-sm-4"><span
                            style="color: red">*</span>作废理由：</label>
                    <div class="col-sm-8">
                        <select name="reason" id="reason" class="form-control"
                                v-validate:reason="{required:true}" v-model="reason">
                            <option value="">请选择</option>
                            <option value="{{item.reason}}" v-for="item of reasonSelect">{{item.reason}}</option>
                        </select> <span v-cloak
                                        v-if="$validation.reason.invalid && $validation.touched"
                                        class="help-absolute"> 请选择作废理由 </span>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" @click="save" class="btn btn-primary">保存</button>
                <button type="button" data-dismiss="modal" class="btn">关闭</button>
            </div>
        </form>
    </validator>
</div>


<!-- 详情modal -->
<div id="detailModal" class="modal fade" tabindex="-1" data-width="1000">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
                aria-hidden="true">×
        </button>
        <h3 align="center">大美装饰管理平台-订货单详情</h3>
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


<!-- 详情中编辑modal -->
<div id="editDetailModal" class="modal fade" tabindex="-1"
     data-width="600">
    <validator name="validation">
        <form name="banner" novalidate class="form-horizontal" role="form">
            <input type="hidden" v-model="id">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×
                </button>
                <h3 align="center">详情编辑</h3>
            </div>


            <div class="form-group"
                 :class="{'has-error':$validation.quantity.invalid && $validation.touched}">
                <label for="quantity" class="control-label col-sm-3"><span
                        style="color: red">*</span>订货数量：</label>
                <div class="col-sm-9">
                    <input id="quantity" v-model="quantity"
                           v-validate:quantity="{
                  required:{rule:true,message:'请输入数量'}
                }"
                           data-tabindex="1" type="text" placeholder="数量" class="form-control">
                    <span v-cloak
                          v-if="$validation.quantity.invalid && $validation.touched"
                          class="help-absolute"> <span
                            v-for="error in $validation.quantity.errors">
						{{error.message}} {{($index !==
						($validation.quantity.errors.length -1)) ? ',':''}} </span>
				</span>
                </div>
            </div>

            <div class="form-group"
                 :class="{'has-error':($validation.installdate.invalid && $validation.touched)}">
                <label for="installDate" class="col-sm-3 control-label">安装时间</label>
                <div class="col-sm-8">
                    <input v-validate:installdate="{required:true}"
                           v-model="installDate" v-el:install-date id="installDate"
                           name="installDate" type="text" readonly
                           class="form-control datepicker" placeholder="请选择安装时间">
                    <div v-if="$validation.installdate.invalid && $validation.touched"
                         class="help-absolute">
                        <span v-if="$validation.installdate.invalid">请选择安装时间</span>
                    </div>
                </div>
            </div>


            <div class="form-group"
                 :class="{'has-error':$validation.note.invalid && $validation.touched}">
                <label for="note" class="control-label col-sm-3"><span
                        style="color: red">*</span>说明：</label>
                <div class="col-sm-9">
                    <input id="note" v-model="note"
                           v-validate:note="{
                  required:{rule:true,message:'请输入说明'},
                  maxlength:{rule:30,message:'说明最长不能超过30个字符'}
                }"
                           data-tabindex="1" type="text" placeholder="说明" class="form-control">
                    <span v-cloak v-if="$validation.note.invalid && $validation.touched"
                          class="help-absolute"> <span
                            v-for="error in $validation.note.errors"> {{error.message}}
						{{($index !== ($validation.note.errors.length -1)) ? ',':''}} </span>
				</span>
                </div>
            </div>

            <div class="modal-footer">
                <button type="button" data-dismiss="modal" class="btn">关闭</button>
                <button type="button" @click="save" class="btn btn-primary">保存</button>
            </div>
</div>

<div id="rejectModel" class="modal fade" tabindex="-1" data-width="600">
    <validator name="validation">
        <form name="banner" novalidate class="form-horizontal" role="form">
            <input type="hidden" v-model="id">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×
                </button>
                <h3 align="center">驳回订货单</h3>
            </div>

            <div class="modal-body" style="height:300px;">
                <div class="col-sm-12 m-t-lg form-group"
                     :class="{'has-error': $validation.rejecttype.invalid && $validation.touched}">
                    <%--<label for="reason" class="control-label col-sm-4" style="text-align: right;line-height: 34px;><span--%>
                            <%--style=" color: red">*</span>驳回类型：</label>--%>
                        <label for="reason" class="control-label col-sm-4" style="text-align: right;"><span
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
                    <label for="reason" class="control-label col-sm-4" style="text-align: right;"><span
                            style="color: red">*</span>驳回原因：</label>
                    <div class="col-sm-7">
                        <textarea v-model="remark" placeholder="驳回原因" class="form-control"
                                  v-validate:remark="{required:true}"
                                  style="resize:none;">

                        </textarea>
                        <span v-cloak
                              v-if="$validation.remark.invalid && $validation.touched"
                              class="help-absolute"> 请填写驳回原因 </span>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" data-dismiss="modal" class="btn">关闭</button>
                <button type="button" @click="save" class="btn btn-primary">保存</button>
            </div>
        </form>
    </validator>
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
<%--填写计划安装时间--%>
<div id="installdateModel" class="modal fade" tabindex="-1" data-width="600">
    <validator name="validation">
        <form name="banner" novalidate class="form-horizontal" role="form">
            <input type="hidden" v-model="id">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×
                </button>
                <h3 align="center">填写计划安装时间</h3>
            </div>

            <div class="modal-body" style="height:300px;">
                <div class="form-group">
                    <label class="control-label col-sm-3">计划安装时间：</label>
                    <div class="col-sm-9">
                        <input v-model="installDate" v-el:install-date id="installDate"
                               v-validate:time="{required:{rule:true,message:'请输入时间'},}"
                               name="c" type="text" readonly
                               class="form-control datepicker" placeholder="请选择开始时间">
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" data-dismiss="modal" class="btn">关闭</button>
                <button type="button" @click="save" class="btn btn-primary">保存</button>
            </div>
        </form>
    </validator>
</div>

<div id="installRejectModel" class="modal fade" tabindex="-1">
    <validator name="validation">
        <form name="banner" novalidate class="form-horizontal" role="form">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×
                </button>
                <h3 align="center">提交安装审批</h3>
            </div>
            <div class="modal-body">
                <div class="col-md-12 form-group"
                     :class="{'has-error': $validation.remark.invalid && $validation.touched}">
                    <label for="remark" class="control-label col-md-3" >提交说明：</label>
                    <div class="col-md-8">
                        <textarea v-model="remark"
                                  placeholder="提交说明"
                                  class="form-control"
                                  v-validate:remark="{
                                        required: {rule: true,message: '请输入提交说明'},
                                        maxlength:{rule:100,message:'不能超过100个字符'}
                                    }"
                                  style="resize:none;">

                        </textarea>
                        <span v-cloak
                              v-if="$validation.remark.invalid && $validation.touched"
                              class="help-absolute">
                              <span v-for="error in $validation.remark.errors">
                                  {{error.message}} {{($index !== ($validation.remark.errors.length -1)) ? ',':''}}
                              </span>
                        </span>
                    </div>
                    <div class="col-md-12 form-group">
                        <web-uploader
                                :type="webUploader.type"
                                :w-server="webUploader.server"
                                :w-accept="webUploader.accept"
                                :w-file-size-limit="webUploader.fileSizeLimit"
                                :w-file-single-size-limit="webUploader.fileSingleSizeLimit"
                                :w-form-data="webUploader.formData">
                            <button id="imageUpload" type="button" class="btn btn-sm btn-primary">上传图片</button>
                        </web-uploader>
                    </div>
                    <div class="form-group col-md-12">
                        <div class="clearfix" style="margin-left: 25px;">
                            <div v-for="imag  in images" class="pull-left"
                                 style="margin-right:5px;position:relative;">
                                <button @click="deleteImg(imag.fullPath,$index)"
                                        style="font-size:18px;line-height:.5;position:absolute;top:1px;right:1px;"
                                        type="button" class="close"><span
                                        aria-hidden="true">&times;</span><span
                                        class="sr-only">Close</span></button>
                                <img :src="imag.fullPath" alt="图片" width="60px"
                                     height="60px">
                            </div>
                        </div>
                    </div>
                    <div class="form-group col-md-12" style="margin-bottom: 2px;">
                        <h4 align="center" style="color: red">安装已完成，请上传图片，并提交至材料部进行审核！</h4>
                    </div>
                </div>
            </div>
            <div class="modal-footer" style="text-align: center">
                <button type="button" @click="save" class="btn btn-primary">提交</button>
                <button type="button" data-dismiss="modal" class="btn">关闭</button>
            </div>
        </form>
    </validator>
</div>

<div id="checkInstallEndModel" class="modal fade" tabindex="-1">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
                aria-hidden="true">×
        </button>
        <h3 align="center">安装完成，待审批</h3>
    </div>
    <div class="modal-body">
        <div class="ibox">
            <div class="ibox-title">安装信息</div>
            <div class="ibox-content">
                <table id="dataTable5" class="table table-bordered">
                    <tr id="titileTr4">
                        <td style="text-align: center;font-weight:bold">提交人</td>
                        <td style="text-align: center;font-weight:bold">提交时间</td>
                        <td style="text-align: center;font-weight:bold">提交说明</td>
                        <td style="text-align: center;font-weight:bold">提交图片</td>
                    </tr>
                    <tr>
                        <td style="text-align: center" width="10%">{{installDatas.creator}}</td>
                        <td style="text-align: center" width="20%">{{installDatas.creatTime}}</td>
                        <td style="text-align: center" width="30%">{{installDatas.remark}}</td>
                        <td style="text-align: center" width="40%">
                            <div v-for="image in img" class="pull-left" style="margin-right:5px;position:relative;">
                                <img :src="image.fullPath" alt="图片" @click="bigImg(image.fullPath)" width="60px"
                                     height="60px">
                            </div>
                        </td>
                    </tr>
                    <tr v-if="installDatas.length === 0">
                        <td class="text-center" colspan="11">没有找到匹配的记录</td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="ibox">
            <div class="ibox-title">审批信息</div>
            <div class="ibox-content">
                <validator name="validation">
                    <form  novalidate class="form-horizontal" role="form">
                        <div class="row">
                            <div class="col-md-12 form-group">
                                <label class="control-label col-md-3" >审核结果：</label>
                                <div class="col-md-7">
                                    <input type="radio" id="OpinionTrue" :value="true" @change="approvalOpinionChange()"
                                           v-model="approvalOpinion">
                                    <label for="OpinionTrue">通过</label>
                                    <input type="radio" id="OpinionFalse" :value="false" @change="approvalOpinionChange()"
                                           v-model="approvalOpinion">
                                    <label for="OpinionFalse">驳回</label>
                                </div>
                            </div>
                            <div  v-if="showRejectInfo">
                                <div class="col-md-12 form-group"
                                     :class="{'has-error': $validation.rejecttype.invalid && $validation.touched}">
                                    <label for="reason" class="control-label col-md-3" >驳回类型：</label>
                                    <div class="col-md-7">
                                        <select v-model="rejectType" class="form-control" v-validate:rejecttype="{required:true}">
                                            <option value="">请选择驳回类型</option>
                                            <option v-for="reject in rejects" :value="reject.id">{{reject.dicName}}</option>
                                        </select>
                                        <span v-cloak
                                              v-if="$validation.rejecttype.invalid && $validation.touched"
                                              class="help-absolute"> 请选择驳回类型 </span>
                                    </div>
                                </div>
                                <div class="col-sm-12 form-group"
                                     :class="{'has-error': $validation.remark.invalid && $validation.touched}">
                                    <label for="reason" class="control-label col-md-3">驳回原因：</label>
                                    <div class="col-md-7">
                                        <textarea v-model="remark" placeholder="驳回原因" class="form-control"
                                                  v-validate:remark="{required:true}"
                                                  style="resize:none;">

                                        </textarea>
                                        <span v-cloak
                                              v-if="$validation.remark.invalid && $validation.touched"
                                              class="help-absolute"> 请填写驳回原因 </span>
                                        </div>
                                </div>
                            </div>
                        </div>
                    </form>
                    </validator>
            </div>
        </div>
    </div>
    <div class="modal-footer" style="text-align: center">
        <button type="button" @click="submitFun" class="btn btn-primary">提交</button>
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
<script src="/static/admin/vendor/webuploader/webuploader.js?v=ea38b0f7"></script>
<script src="/static/admin/js/components/webuploader.js?v=ea38b0f7"></script>
<script src="/static/admin/js/apps/indentOrder/list.js?v=ea38b0f7"></script>
