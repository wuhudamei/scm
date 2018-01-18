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
        <form id="searchForm" @submit.prevent="query">

          <div class="col-md-3">
            <div class="form-group">
              <input
                  v-model="form.keyword"
                  type="text"
                  placeholder="客户名字/手机号" class="form-control"/>
            </div>
          </div>
          
          <div class="col-md-1">
            <div class="form-group">
              <button id="searchBtn" type="submit" class="btn btn-block btn-outline btn-default" alt="搜索"
                      title="搜索">
                <i class="fa fa-search"></i>
              </button>
            </div>
          </div>
          <!-- v-if="acctType=='STORE'" -->
          <div class="col-md-1">
            <div class="form-group" v-if="acctType=='FINANCE'">
              <button @click="createBtnClickHandler" id="createBtn" type="button"
                      class="btn btn-block btn-outline btn-primary">创建
              </button>
            </div>
          </div>
          
          <div class="col-md-2">
            <div class="form-group" v-if="acctType=='FINANCE'">
              <button @click="createCustomerAndContract" id="doubleCreateBtn" type="button"
                      class="btn btn-block btn-outline btn-primary">创建 客户&项目
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


<div id="modal" class="modal fade" tabindex="-1" data-width="600">
  <validator name="validation">
    <form name="banner" novalidate class="form-horizontal" role="form">
      <input type="hidden" v-model="id">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 align="center">增加/编辑客户信息</h3>
      </div>
      
      <div class="modal-body">
        <div class="form-group" :class="{'has-error':$validation.name.invalid && $validation.touched}">
          <label for="name" class="control-label col-sm-3"><span style="color: red">*</span>客户姓名：</label>
          <div class="col-sm-9">
            <input
                id="name"
                v-model="name"
                v-validate:name="{
                  required:{rule:true,message:'请输入客户姓名'},
                  maxlength:{rule:30,message:'客户姓名最长不能超过30个字符'}
                }"
                data-tabindex="1"
                type="text" placeholder="客户姓名" class="form-control">
            <span v-cloak v-if="$validation.name.invalid && $validation.touched" class="help-absolute">
              <span v-for="error in $validation.name.errors">
                {{error.message}} {{($index !== ($validation.name.errors.length -1)) ? ',':''}}
                </span>
            </span>
          </div>
        </div>

        <div class="form-group" :class="{'has-error':$validation.mobile.invalid && $validation.touched}">
          <label for="mobile" class="control-label col-sm-3"><span style="color: red">*</span>客户手机号：</label>
          <div class="col-sm-9">
            <input
              id="mobile"
              v-validate:mobile="['telphone']"
              v-model="mobile" type="text" placeholder="手机号" class="form-control" maxlength="11">
            <span v-cloak v-if="$validation.mobile.invalid && $validation.touched" class="help-absolute">
                请输入正确的手机号
            </span>
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


<div id="contractModal" class="modal fade" tabindex="-1" data-width="600">
  <validator name="validation">
    <form name="banner" novalidate class="form-horizontal" role="form">
      <input type="hidden" v-model="id">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 align="center">录入项目</h3>
      </div>
      
      <div class="modal-body">

        <div class="form-group" :class="{'has-error':$validation.customerName.invalid && $validation.touched}">
          <label for="customerName" class="control-label col-sm-3"><span style="color: red">*</span>客户名字：</label>
          <div class="col-sm-9">
            <input id="customerName" v-model="customer.name"
                v-validate:customer-name="{required:{rule:true,message:'请输入客户名字'},maxlength:{rule:40,message:'客户名字最长不能超过40个字符'}}"
                data-tabindex="1"
                :disabled="isRO"
                type="text" placeholder="客户名字" class="form-control">
            <span v-cloak v-if="$validation.customerName.invalid && $validation.touched" class="help-absolute">
              <span v-for="error in $validation.customerName.errors">
                {{error.message}} {{($index !== ($validation.customerName.errors.length -1)) ? ',':''}}
              </span>
            </span>
          </div>
        </div>
        
        
        
        
        <div class="form-group" :class="{'has-error':$validation.mobile.invalid && $validation.touched}">
          <label for="mobile" class="control-label col-sm-3"><span style="color: red">*</span>客户手机号：</label>
          <div class="col-sm-9">
            <input
              id="mobile"
              v-validate:mobile="['telphone']"
              :disabled="isRO"
              v-model="customer.mobile" type="text" placeholder="手机号" class="form-control" maxlength="11">
            <span v-cloak v-if="$validation.mobile.invalid && $validation.touched" class="help-absolute">
                请输入正确的手机号
            </span>
          </div>
        </div>
        
        
        
        
        <div class="form-group" :class="{'has-error':$validation.contractCode.invalid && $validation.touched}">
          <label for="contractCode" class="control-label col-sm-3"><span style="color: red">*</span>项目编号：</label>
          <div class="col-sm-9">
            <input id="contractCode" v-model="contractCode"
                v-validate:contract-code="{required:{rule:true,message:'请输入项目编号'},maxlength:{rule:40,message:'项目编号最长不能超过40个字符'}}"
                data-tabindex="1"
                type="text" placeholder="项目编号" class="form-control">
            <span v-cloak v-if="$validation.contractCode.invalid && $validation.touched" class="help-absolute">
              <span v-for="error in $validation.contractCode.errors">
                {{error.message}} {{($index !== ($validation.contractCode.errors.length -1)) ? ',':''}}
              </span>
            </span>
          </div>
        </div>

        <div class="form-group" :class="{'has-error':$validation.houseaddr.invalid && $validation.touched}">
          <label for="houseaddr" class="control-label col-sm-3"><span style="color: red">*</span>客户装修地址：</label>
          <div class="col-sm-9">
            <input
                id="houseaddr"
                v-model="houseAddr"
                v-validate:houseaddr="{
                  required:{rule:true,message:'请输入客户装修地址'},
                  maxlength:{rule:100,message:'客户装修地址最长不能超过100个字符'}
                }"
                data-tabindex="1"
                type="text" placeholder="客户装修地址" class="form-control">
            <span v-cloak v-if="$validation.houseaddr.invalid && $validation.touched" class="help-absolute">
              <span v-for="error in $validation.houseaddr.errors">
                {{error.message}} {{($index !== ($validation.houseaddr.errors.length -1)) ? ',':''}}
                </span>
            </span>
          </div>
        </div>

        <div class="form-group">
          <label for="designer" class="control-label col-sm-3">设计师：</label>
          <div class="col-sm-9">
            <input
                id="designer"
                v-model="designer"
                data-tabindex="1"
                type="text" placeholder="设计师" class="form-control">
          </div>
        </div>
        <div class="form-group">
          <label for="designerMobile" class="control-label col-sm-3">设计师手机号：</label>
          <div class="col-sm-9">
            <input
                    id="designerMobile"
                    v-model="designerMobile"
                    data-tabindex="1"
                    type="text" placeholder="设计师手机号" class="form-control">
          </div>
        </div>

        <div class="form-group">
          <label for="name" class="control-label col-sm-3">监理：</label>
          <div class="col-sm-9">
            <input
                id="supervisor"
                v-model="supervisor"
                data-tabindex="1"
                type="text" placeholder="监理姓名" class="form-control">
          </div>
        </div>

        <div class="form-group">
          <label for="supervisorMobile" class="control-label col-sm-3">监理手机号：</label>
          <div class="col-sm-9">
            <input
                    id="supervisorMobile"
                    v-model="supervisorMobile"
                    data-tabindex="1"
                    type="text" placeholder="监理手机号" class="form-control">
          </div>
        </div>

        <div class="form-group">
          <label for="projectmanager" class="control-label col-sm-3">项目经理：</label>
          <div class="col-sm-9">
            <input
                id="projectmanager"
                v-model="projectManager"
                data-tabindex="1"
                type="text" placeholder="项目经理姓名" class="form-control">
          </div>
        </div>

        <div class="form-group">
          <label for="pmmobile" class="control-label col-sm-3">项目经理手机号：</label>
          <div class="col-sm-9">
            <input
              id="pmmobile"
              v-model="pmMobile" type="text" placeholder="手机号" class="form-control" maxlength="11">
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
<script src="/static/admin/vendor/webuploader/webuploader.js?v=ea38b0f7"></script>
<script src="/static/admin/js/components/webuploader.js?v=ea38b0f7"></script>
<script src="/static/admin/js/apps/customer/list.js?v=ea38b0f7"></script>