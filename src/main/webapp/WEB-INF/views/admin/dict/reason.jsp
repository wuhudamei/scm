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
                  placeholder="作废原因" class="form-control"/>
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

          <div class="col-md-1">
            <div class="form-group">
              <button @click="createBtnClickHandler" id="createBtn" type="button"
                      class="btn btn-block btn-outline btn-primary">新增
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
        <h3 v-if="id==null"  align="center">新增</h3>
        <h3 v-else  align="center">编辑</h3>
      </div>
      
      <div class="modal-body">
        <div class="form-group" :class="{'has-error':$validation.reason.invalid && $validation.touched}">
          <label for="reason" class="control-label col-sm-3"><span style="color: red">*</span>作废原因：</label>
          <div class="col-sm-9">
            <input id="reason" v-model="reason"
                v-validate:reason="{required:{rule:true,message:'请输入作废原因'},maxlength:{rule:30,message:'作废原因最长不能超过30个字符'}}"
                data-tabindex="1"
                type="text" placeholder="作废原因" class="form-control">
            <span v-cloak v-if="$validation.name.invalid && $validation.touched" class="help-absolute">
              <span v-for="error in $validation.name.errors">
                {{error.message}} {{($index !== ($validation.name.errors.length -1)) ? ',':''}}
              </span>
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
<script src="/static/admin/vendor/webuploader/webuploader.js?v=ea38b0f7"></script>
<script src="/static/admin/js/components/webuploader.js?v=ea38b0f7"></script>
<script src="/static/admin/js/apps/dict/reason.js?v=ea38b0f7"></script>