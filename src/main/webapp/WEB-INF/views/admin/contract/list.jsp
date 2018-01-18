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
                  placeholder="项目编号|客户名字|手机|设计师" class="form-control"/>
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

        </form>
      </div>
      <!-- <div class="columns columns-right btn-group pull-right"></div> -->
      <table id="dataTable" width="100%" class="table table-striped table-bordered table-hover">
      </table>
    </div>
  </div>
  <!-- ibox end -->
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
        <div class="form-group" :class="{'has-error':$validation.contractCode.invalid && $validation.touched}">
          <label for="contractCode" class="control-label col-sm-3"><span style="color: red">*</span>项目编号：</label>
          <div class="col-sm-9">
            <input id="contractCode" v-model="contractCode"
                v-validate:contractCode="{required:{rule:true,message:'请输入项目编号'},maxlength:{rule:40,message:'项目编号最长不能超过40个字符'}}"
                data-tabindex="1"
                type="text" placeholder="项目编号" class="form-control">
            <span v-cloak v-if="$validation.contractCode.invalid && $validation.touched" class="help-absolute">
              <span v-for="error in $validation.contractCode.errors">
                {{error.message}} {{($index !== ($validation.contractCode.errors.length -1)) ? ',':''}}
              </span>
            </span>
          </div>
        </div>
        
        
        <div class="form-group" :class="{'has-error':$validation.customerName.invalid && $validation.touched}">
          <label for="contractCode" class="control-label col-sm-3"><span style="color: red">*</span>客户姓名：</label>
          <div class="col-sm-9">
            <input id="customerName" v-model="customerName"
                v-validate:customer-name="{required:{rule:true,message:'请输入客户姓名'},maxlength:{rule:40,message:'客户姓名最长不能超过40个字符'}}"
                disabled="true"
                data-tabindex="2"
                type="text" placeholder="客户姓名" class="form-control">
            <span v-cloak v-if="$validation.customerName.invalid && $validation.touched" class="help-absolute">
              <span v-for="error in $validation.customerName.errors">
                {{error.message}} {{($index !== ($validation.customerName.errors.length -1)) ? ',':''}}
              </span>
            </span>
          </div>
        </div>

        <div class="form-group" :class="{'has-error':$validation.houseaddr.invalid && $validation.touched}">
          <label for="houseaddr" class="control-label col-sm-3"><span style="color: red">*</span>客户装修地址：</label>
          <div class="col-sm-9">
            <input
                id="houseaddr"
                v-model="houseaddr"
                v-validate:houseaddr="{
                  required:{rule:true,message:'请输入客户装修地址'},
                  maxlength:{rule:100,message:'客户装修地址最长不能超过100个字符'}
                }"
                data-tabindex="3"
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
                data-tabindex="4"
                type="text" placeholder="设计师" class="form-control"/>
          </div>
        </div>

        <div class="form-group">
          <label for="designerMobile" class="control-label col-sm-3">设计师手机号：</label>
          <div class="col-sm-9">
            <input
                    id="designerMobile"
                    v-model="designerMobile"
                    data-tabindex="5"
                    type="text" placeholder="设计师手机号" class="form-control"/>
          </div>
        </div>

        <div class="form-group">
          <label class="control-label col-sm-3">监理：</label>
          <div class="col-sm-9">
            <input
                v-model="supervisor"
                data-tabindex="6"
                type="text" placeholder="监理姓名" class="form-control"/>
          </div>
        </div>

        <div class="form-group">
          <label class="control-label col-sm-3">监理手机号：</label>
          <div class="col-sm-9">
            <input
                    v-model="supervisorMobile"
                    data-tabindex="7"
                    type="text" placeholder="监理手机号" class="form-control"/>
          </div>
        </div>

        <div class="form-group" >
          <label for="projectManager" class="control-label col-sm-3">项目经理：</label>
          <div class="col-sm-9">
            <input
                id="projectManager"
                v-model="projectManager"
                data-tabindex="8"
                type="text" placeholder="项目经理姓名" class="form-control"/>
          </div>
        </div>

        <div class="form-group">
          <label class="control-label col-sm-3">项目手机号：</label>
          <div class="col-sm-9">
            <input
              v-model="pmMobile" type="text" placeholder="手机号" class="form-control" maxlength="11" data-tabindex="9"/>
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
<script src="/static/admin/js/apps/contract/list.js?v=ea38b0f7"></script>