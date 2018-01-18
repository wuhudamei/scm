<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<script type="text/javascript">
  var hasEditRegionPrivelege = false;
  <shiro:hasPermission name="region_supplier:edit">
    hasEditRegionPrivelege = true;
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
<div id="container" class="wrapper wrapper-content  animated fadeInRight">
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
                  placeholder="名称" class="form-control"/>
            </div>
          </div>
          <div class="col-md-3">
            <div class="form-group">
              <select v-model="form.storeCode" class="form-control">
                <option value="">请选择门店</option>
                <option v-for="store in storeList" :value="store.code" v-text="store.name">{{store.name}}
                </option>
              </select>
            </div>
          </div>
          <div class="col-md-3">
            <select v-model="form.status"
                    placeholder="区域供应商状态"
                    class="form-control">
              <option v-for="regionSupplierStatusEnum in regionSupplierStatus" :value="regionSupplierStatusEnum.value">{{regionSupplierStatusEnum.label}}</option>
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
        
         <shiro:hasPermission name="region_supplier:edit">
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
        <div class="form-group" :class="{'has-error':$validation.name.invalid && $validation.touched}">
          <label for="name" class="control-label col-sm-3"><span style="color: red">*</span>名称：</label>
          <div class="col-sm-6">
            <input id="name" v-model="name"
                v-validate:name="{required:{rule:true,message:'请输入名称'},maxlength:{rule:40,message:'名称最长不能超过40个字符'}}"
                data-tabindex="1"
                type="text" placeholder="名称" class="form-control">
            <span v-cloak v-if="$validation.name.invalid && $validation.touched" class="help-absolute">
              <span v-for="error in $validation.name.errors">
                {{error.message}} {{($index !== ($validation.name.errors.length -1)) ? ',':''}}
              </span>
            </span>
          </div>
        </div>
        
        
        <div class="form-group" 
			:class="{'has-error':($validation.store.invalid && $validation.touched)}">
			<label for="store" class="control-label col-sm-3"><span style="color: red">*</span>所属门店：</label>
			<div class="col-sm-6">
				<select id="store" name="store"
					v-validate:store="{required:{rule:true,message:'请选择所属门店'}}"
					v-model="storeCode" class="form-control">
					<option value="">请选择所属门店</option>
					<option v-for="store in storeSelects"
						value="{{store.code}}">{{store.name}}</option>
				</select>
				<span v-cloak v-if="$validation.store.invalid && $validation.touched" class="help-absolute">
              <span v-for="error in $validation.store.errors">
                {{error.message}} {{($index !== ($validation.store.errors.length -1)) ? ',':''}}
              </span>
            </span>
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
<script src="/static/admin/js/apps/prod/supplier/region-supplier-list.js?v=ea38b0f7"></script>