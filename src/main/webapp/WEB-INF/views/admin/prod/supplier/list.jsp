<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<script type="text/javascript">
  var hasEditSupplierPrivelege = false;
  <shiro:hasPermission name="product_supplier:edit">
  	hasEditSupplierPrivelege = true;
  </shiro:hasPermission>
</script> 
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
    .fixed-table-container tbody tr td:last-child>button{
      margin: 2px;
    }
    .fixed-table-container tbody tr td:last-child>button:first-child{
      margin-left:0px !important;
    }
    @media (min-width: 992px){
    .col-md-1 {
        width: auto !important; 
    }
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

          <div class="col-md-3">
            <div class="form-group">
              <input
                  v-model="form.keyword" type="text"
                  placeholder="供应商编码/名称" class="form-control"/>
            </div>
          </div>
          
          <div class="col-md-3">
            <div class="form-group">
              <select class="form-control" v-model="form.regionSupplierId">
                <option value=''>选择所属区域供应商 </option>
                <option v-for="region in allRegionSupplier" :value="region.id">{{region.name}}</option>
              </select>
            </div>
          </div>
          <div class="col-md-2">
            <select v-model="form.status"
                    placeholder="供应商状态"
                    class="form-control">
              <option v-for="supplierStatusEnum in supplierStatus" :value="supplierStatusEnum.value">{{supplierStatusEnum.label}}</option>
            </select>
          </div>
          <div class="col-md-1">
            <div class="form-group">
              <button id="searchBtn" type="submit" class="btn btn-block btn-outline btn-default" alt="搜索"
                      title="搜索">
                <i class="fa fa-search"></i>
              </button>
            </div>
          </div>
 		 
 		 <shiro:hasPermission name="product_supplier:edit">
          <div class="col-md-1">
            <div class="form-group">
              <button @click="createBtnClickHandler" id="createBtn" type="button"
                      class="btn btn-block btn-outline btn-primary">新增
              </button>
            </div>
          </div>
         </shiro:hasPermission>
        </form>
      </div>
      <table id="dataTable" width="100%" class="table table-striped table-bordered table-hover">
      </table>
    </div>
  </div>
  <!-- ibox end -->
</div>

<div id="contractModal" class="modal modal-dialog fade" tabindex="-1"  data-width="600">
  <validator name="validation">
    <form name="banner" novalidate class="form-horizontal" role="form">
      <input type="hidden" v-model="id">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 v-if="id==null"  align="center">新增供应商</h3>
        <h3 v-else  align="center">编辑供应商</h3>
      </div>
      
      <div class="modal-body">
        <div class="form-group" :class="{'has-error': $validation.code.invalid && $validation.touched}">
          <label for="code" class="control-label col-sm-3"><span style="color: red">*</span>供应商编码：</label>
          <div class="col-sm-9">
            <input id="code" v-model="code"
                  v-validate:code="{
                  required:{rule:true,message:'请输入编码'},
                  maxlength:{rule:50,message:'联系人最长不能超过50个字符'}
              }" type="text" placeholder="编码" class="form-control">
            <span v-cloak v-if="$validation.code.invalid && $validation.touched" class="help-absolute">
                <span v-for="error in $validation.code.errors">
                    {{error.message}} {{($index !== ($validation.code.errors.length -1)) ? ',':''}}
                </span>
            </span>
          </div>
        </div>
        
        <div class="form-group" :class="{'has-error':$validation.name.invalid && $validation.touched}">
          <label for="name" class="control-label col-sm-3"><span style="color: red">*</span>供应商名称：</label>
          <div class="col-sm-9">
            <input id="name" v-model="name"
                v-validate:name="{required:{rule:true,message:'请输入名称'},maxlength:{rule:40,message:'名称最长不能超过40个字符'}}"
                data-tabindex="2" type="text" placeholder="名称" class="form-control">
            <span v-cloak v-if="$validation.name.invalid && $validation.touched" class="help-absolute">
              <span v-for="error in $validation.name.errors">
                {{error.message}} {{($index !== ($validation.name.errors.length -1)) ? ',':''}}
              </span>
            </span>
          </div>
        </div>
        <div class="form-group">
          <label for="description" class="control-label col-sm-3">供应商简称：</label>
          <div class="col-sm-9">
            <input id="supplierAbbreviation" v-model="supplierAbbreviation"
                   type="text" placeholder="简称" class="form-control">
          </div>
        </div>
        <div class="form-group">
          <label for="description" class="control-label col-sm-3">供应商描述：</label>
          <div class="col-sm-9">
            <input id="description" v-model="description"
                   type="text" placeholder="描述" class="form-control">
          </div>
        </div>

        <div class="form-group" :class="{'has-error': $validation.description.invalid && $validation.touched}">
          <label for="description" class="control-label col-sm-3"><span style="color: red">*</span>所属区域供应商：</label>
          <div class="col-sm-9">
            <select class="form-control" v-model='regionSupplierId'>
              <option value=''>选择所属区域 </option>
              <option v-for="region in allRegionSupplier" :value="region.id">{{region.name}}</option>
            </select>
          </div>
        </div>

        <div class="form-group" :class="{'has-error': $validation.contactor.invalid && $validation.touched}">
          <label for="contactor" class="control-label col-sm-3"><span style="color: red">*</span>联系人：</label>
          <div class="col-sm-9">
            <input id="contactor" v-model="contactor"
                  v-validate:contactor="{
                  required:{rule:true,message:'请输入联系人'},
                  maxlength:{rule:50,message:'联系人最长不能超过50个字符'}
              }" type="text" placeholder="联系人" class="form-control">
            <span v-cloak v-if="$validation.contactor.invalid && $validation.touched" class="help-absolute">
                <span v-for="error in $validation.contactor.errors">
                    {{error.message}} {{($index !== ($validation.contactor.errors.length -1)) ? ',':''}}
                </span>
            </span>
          </div>
        </div>
  
        <div class="form-group" :class="{'has-error':$validation.mobile.invalid && $validation.touched}">
          <label for="mobile" class="control-label col-sm-3"><span style="color: red">*</span>联系人手机号：</label>
          <div class="col-sm-9">
            <input 
              v-validate:mobile="['telphone']"
              v-model="mobile" type="text" placeholder="手机号" class="form-control" maxlength="11">
            <span v-cloak v-if="$validation.mobile.invalid && $validation.touched" class="help-absolute">
                	请输入正确的手机号
            </span>
          </div>
        </div>

        <div class="form-group" >
          <label for="phone" class="control-label col-sm-3">固定电话：</label>
          <div class="col-sm-9">
            <input id="phone" v-model="phone" type="tel" placeholder="固定电话" class="form-control">
          </div>
        </div>

		<div class="form-group" :class="{'has-error': $validation.address.invalid && $validation.touched}">
          <label for="address" class="control-label col-sm-3"><span style="color: red">*</span>公司地址：</label>
          <div class="col-sm-9">
            <input id="address" v-model="address"
                  v-validate:address="{
                  required:{rule:true,message:'请输入公司地址'},
                  maxlength:{rule:100,message:'公司地址最长不能超过100个字符'}
              }" type="text" placeholder="公司地址" class="form-control">
            <span v-cloak v-if="$validation.address.invalid && $validation.touched" class="help-absolute">
                <span v-for="error in $validation.address.errors">
                    {{error.message}} {{($index !== ($validation.address.errors.length -1)) ? ',':''}}
                </span>
            </span>
          </div>
        </div> 

        <div class="form-group">
          <label for="description" class="control-label col-sm-3">合作品牌名称：</label>
          <div class="col-sm-9">
            <input id="cooperativeBrandName" v-model="cooperativeBrandName"
                   type="text" placeholder="品牌名称" class="form-control">
          </div>
        </div>
        <div class="form-group">
          <label for="description" class="control-label col-sm-3">负责人：</label>
          <div class="col-sm-9">
            <input id="manager" v-model="manager"
                   type="text" placeholder="负责人" class="form-control">
          </div>
        </div>
        <div class="form-group">
          <label for="description" class="control-label col-sm-3">负责人电话：</label>
          <div class="col-sm-9">
            <input id="managerMobile" v-model="managerMobile"
                   type="text" placeholder="电话" class="form-control">
          </div>
        </div>
        <div class="form-group">
          <label for="description" class="control-label col-sm-3">业务负责人：</label>
          <div class="col-sm-9">
            <input id="businessManager" v-model="businessManager"
                   type="text" placeholder="业务负责人" class="form-control">
          </div>
        </div>
        <div class="form-group">
          <label for="description" class="control-label col-sm-3">业务负责人电话：</label>
          <div class="col-sm-9">
            <input id="businessManagerMobile" v-model="businessManagerMobile"
                   type="text" placeholder="电话" class="form-control">
          </div>
        </div>
        <div class="form-group">
          <label for="description" class="control-label col-sm-3">开户行：</label>
          <div class="col-sm-9">
            <input id="openingBank" v-model="openingBank"
                   type="text" placeholder="开户行" class="form-control">
          </div>
        </div>
        <div class="form-group">
          <label for="description" class="control-label col-sm-3">账号：</label>
          <div class="col-sm-9">
            <input id="accountNumber" v-model="accountNumber"
                   type="text" placeholder="账号" class="form-control">
          </div>
        </div>
        <div class="form-group">
          <label for="description" class="control-label col-sm-3">纳税人识别号：</label>
          <div class="col-sm-9">
            <input id="taxpayerIdentificationNumber" v-model="taxpayerIdentificationNumber"
                   type="text" placeholder="识别号" class="form-control">
          </div>
        </div>

        <div class="form-group">
          <label for="description" class="control-label col-sm-3">税务登记证图片：</label>
          <div class="col-sm-9">
            <input id="taxRegistrationCertificateImageUrl" v-model="taxRegistrationCertificateImageUrl"
                   type="hidden" placeholder="税务登记证图片" class="form-control">
            <div class="pull-left">
              <web-uploader
                            :type="webUploader.type"
                            :w-accept="webUploader.accept"
                            :w-server="webUploader.server"
                            :w-file-size-limit="webUploader.fileSizeLimit"
                            :w-file-single-size-limit="webUploader.fileSingleSizeLimit"
                            :w-form-data="webUploader.formData">
                <button type="button" class="btn btn-primary">上传图片</button>
              </web-uploader>
            </div>
            <div class="pull-left" style="margin-right:5px;position:relative;" v-if="taxRegistrationCertificateImageFullUrl">
              <button @click="deleteImg(taxRegistrationCertificateImageFullUrl)"
                      style="font-size:18px;line-height:.5;position:absolute;top:1px;right:1px;"
                      type="button" class="close">
                <span aria-hidden="true">&times;</span>
                <span class="sr-only">Close</span>
              </button>
              <img :src="taxRegistrationCertificateImageFullUrl" alt="图片" width="60px"height="60px" style="margin-left:15px;">
            </div>
          </div>
        </div>

        <div class="form-group">
          <label for="description" class="control-label col-sm-3">营业执照图片：</label>
          <div class="col-sm-9">
            <input id="businessLicenseImageUrl" v-model="businessLicenseImageUrl"
                   type="hidden" placeholder="营业执照图片" class="form-control">
            <div class="pull-left">
              <web-uploader
                      :type="webUploader2.type"
                      :w-accept="webUploader2.accept"
                      :w-server="webUploader2.server"
                      :w-file-size-limit="webUploader2.fileSizeLimit"
                      :w-file-single-size-limit="webUploader2.fileSingleSizeLimit"
                      :w-form-data="webUploader2.formData">
                <button type="button" class="btn btn-primary">上传图片</button>
              </web-uploader>
            </div>
            <div class="pull-left" style="margin-right:5px;position:relative;" v-if="businessLicenseImageFullUrl">
              <button @click="deleteImg(businessLicenseImageFullUrl)"
                      style="font-size:18px;line-height:.5;position:absolute;top:1px;right:1px;"
                      type="button" class="close">
                <span aria-hidden="true">&times;</span>
                <span class="sr-only">Close</span>
              </button>
              <img :src="businessLicenseImageFullUrl" alt="图片" width="60px"height="60px" style="margin-left:15px;">
            </div>
          </div>
        </div>

        <div class="form-group">
          <label for="status" class="control-label col-sm-3"><span style="color: red">*</span>启用状态：</label>
          <div class="col-sm-9">
            <select  v-model="status" class="form-control" >
              <option value="OPEN">启用</option>
              <option value="LOCK">停用</option>
            </select>
          </div>
        </div>

        <div class="modal-footer">
          <button type="button" data-dismiss="modal" class="btn">关闭</button>
          <button type="button" @click="save" class="btn btn-primary">保存</button>
        </div>

      </div>
    </form>
  </validator>
</div>
<script src="/static/admin/vendor/webuploader/webuploader.js"></script>
<script src="/static/admin/js/components/webuploader.js"></script>
<script src="/static/admin/js/apps/prod/supplier/list.js?v=ea38b0f712"></script>