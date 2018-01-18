<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<script type="text/javascript">
  var hasEditBrandPrivelege = false;
  <shiro:hasPermission name="brand:edit">
  hasEditBrandPrivelege = true;
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
          <div class="col-md-2">
            <div class="form-group">
              <input
                  v-model="form.keyword"
                  type="text"
                  placeholder="按编码、名称、首字母查询" class="form-control"/>
            </div>
          </div>
          <div class="col-md-2">
            <div class="form-group">
              <select v-model="form.status" class="form-control">
                <option v-for="option in statusOptions" v-bind:value="option.value">
                  {{ option.text }}
                </option>
              </select>
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

          <shiro:hasPermission name="brand:edit">
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
        <h3 align="center">增加/编辑品牌</h3>
      </div>
      
      <div class="modal-body">

        <div class="form-group" :class="{'has-error':$validation.code.invalid && $validation.touched}">
          <label for="brandName" class="control-label col-sm-3"><span style="color: red">*</span>编码：</label>
          <div class="col-sm-9">
            <input
                id="code"
                v-model="code"
                v-validate:code="{
                    required:{rule:true,message:'请输入编码'},
                    maxlength:{rule:30,message:'编码最长不能超过30个字符'}
                }"
                data-tabindex="1"
                type="text" placeholder="编码" class="form-control">
            <span v-cloak v-if="$validation.code.invalid && $validation.touched" class="help-absolute">
                <span v-for="error in $validation.code.errors">
                    {{error.message}} {{($index !== ($validation.code.errors.length -1)) ? ',':''}}
                </span>
            </span>
          </div>
        </div>

        <div class="form-group" :class="{'has-error':$validation.brandname.invalid && $validation.touched}">
          <label for="brandname" class="control-label col-sm-3"><span style="color: red">*</span>名称：</label>
          <div class="col-sm-9">
            <input
                id="brandName"
                v-model="brandName"
                v-validate:brandname="{
                    required:{rule:true,message:'请输入名称'},
                    maxlength:{rule:30,message:'名称最长不能超过30个字符'}
                }"
                data-tabindex="2"
                type="text" placeholder="名称" class="form-control">
            <span v-cloak v-if="$validation.brandname.invalid && $validation.touched" class="help-absolute">
                <span v-for="error in $validation.brandname.errors">
                    {{error.message}} {{($index !== ($validation.brandname.errors.length -1)) ? ',':''}}
                </span>
            </span>
          </div>
        </div>

        <%--<div class="form-group" :class="{'has-error':$validation.pinyininitial.invalid && $validation.touched}">
          <label for="pinyininitial" class="control-label col-sm-3"><span style="color: red">*</span>拼音：</label>
          <div class="col-sm-9">
            <input
                    id="pinyinInitial"
                    v-model="pinyinInitial"
                    v-validate:pinyininitial="{
                        required:{rule:true,message:'请输入拼音'},
                        maxlength:{rule:30,message:'拼音最长不能超过30个字符'}
                    }"
                    data-tabindex="3"
                    type="text" placeholder="拼音" class="form-control">
            <span v-cloak v-if="$validation.pinyininitial.invalid && $validation.touched" class="help-absolute">
                <span v-for="error in $validation.pinyininitial.errors">
                    {{error.message}} {{($index !== ($validation.pinyininitial.errors.length -1)) ? ',':''}}
                </span>
            </span>
          </div>
        </div>--%>

		<!-- 上传图片 -->
        <div class="form-group"
             :class="{'has-error':$validation.logo.invalid && $validation.touched}">
          <label for="logo" class="col-sm-3 control-label"><span style="color: red">*</span>图片上传</label>
          <div class="col-sm-9">
            <div class="input-group">
              <input
                  id="logo"
                  v-model="logo"
                  v-validate:logo="{
                  required:{rule:true,message:'请上传图片'}
                }"
                  readonly type="text"
                  data-tabindex="4"
                  placeholder="点击上传文件上传图片" class="input-sm form-control">
              <span class="input-group-btn">
                <web-uploader :target-id="'webuploader0'"
                              :type="webUploader.type"
                              :w-server="webUploader.server"
                              :w-accept="webUploader.accept"
                              :w-file-size-limit="webUploader.fileSizeLimit"
                              :w-file-single-size-limit="webUploader.fileSingleSizeLimit"
                              :w-form-data="webUploader.formData">
                  <button id="imageUpload" type="button" class="btn btn-sm btn-primary">上传图片</button>
                </web-uploader>
              </span>
            </div>
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
            <span v-cloak v-if="$validation.logo.invalid && $validation.touched" class="help-absolute">
                <span v-for="error in $validation.logo.errors">
                  {{error.message}} {{($index !== ($validation.logo.errors.length -1)) ? ',':''}}
                </span>
              </span>
          </div>
        </div>

        <div class="form-group">
          <label for="description" class="control-label col-sm-3">图片：</label>
          <div class="col-sm-9">
            <img :src='linkUrl' style="height:100px;width:100px">
          </div>
        </div>

        <div class="form-group">
          <label for="description" class="control-label col-sm-3">描述：</label>
          <div class="col-sm-9">
            <textarea
                id="description"
                v-model="description" type="text" placeholder="描述" class="form-control">
            </textarea>
          </div>
        </div>

        <div class="form-group">
          <label for="status" class="control-label col-sm-3">启用状态：</label>
          <div class="col-sm-9">
            <select
                id="status"
                v-model="status" placeholder="状态"
                class="form-control">
              <option v-for="option in statusOptions" v-bind:value="option.value">
                {{ option.text }}
              </option>
            </select>
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
<script src="/static/admin/vendor/webuploader/webuploader.js?v=ea38b0f7"></script>
<script src="/static/admin/js/components/webuploader.js?v=ea38b0f7"></script>
<script src="/static/admin/js/apps/prod/brand/list.js?v=ea38b0f7"></script>