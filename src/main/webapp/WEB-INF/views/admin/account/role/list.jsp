<%@ page contentType="text/html;charset=UTF-8" %>
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
                  placeholder="角色名称" class="form-control"/>
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
      <table id="dataTable" width="100%" class="table table-striped table-bordered table-hover">
      </table>
    </div>
  </div>
  <!-- ibox end -->
</div>


<div id="modal" class="modal fade" tabindex="-1" data-width="760">
  <validator name="validation">
    <form name="user" novalidate class="form-horizontal" role="form">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 v-if="id==null">添加角色</h3>
        <h3 v-else>编辑角色</h3>
      </div>
      <div class="modal-body">
        <div class="form-group">
          <div class="col-sm-6" :class="{'has-error':$validation.name.invalid && $validation.touched}">
            <input v-model="id" type="hidden">
            <label for="name" class="control-label"><span style="color: red">*</span>角色名称：</label>
            <input v-model="name" type="text" placeholder="角色名称" class="form-control"
                   v-validate:name="{
              required:{rule:true,message:'请输入角色名称'},
              maxlength:{rule:100,message:'角色名称最长不能超过100个字符'}
          }">

            <span v-cloak v-if="$validation.name.invalid && $validation.touched" class="help-absolute">
            <span v-for="error in $validation.name.errors">
                {{error.message}} {{($index !== ($validation.name.errors.length -1)) ? ',':''}}
            </span>
        </span>
          </div>
          <div class="col-sm-6" :class="{'has-error':$validation.description.invalid && $validation.touched}">
            <label for="description" class="control-label"><span style="color: red">*</span>描述：</label>
            <div class="">
              <input v-model="description" type="text" placeholder="描述" class="form-control"
                     v-validate:description="{
                  required:{rule:true,message:'请输入角色描述'},
                  maxlength:{rule:25,message:'角色描述最长不能超过25个字符'}
              }">
              <span v-cloak v-if="$validation.description.invalid && $validation.touched"
                    class="help-absolute">
                <span v-for="error in $validation.description.errors">
                    {{error.message}} {{($index !== ($validation.description.errors.length -1)) ? ',':''}}
                </span>
            </span>
            </div>
          </div>
        </div>

      </div>
      <div class="modal-footer">
        <button :disabled="disabled" type="button" data-dismiss="modal" class="btn">关闭</button>
        <button :disabled="submitting" type="button" @click="save" class="btn btn-primary">保存</button>
      </div>
    </form>
  </validator>
</div>

<div id="setPermissionModal" class="modal fade" tabindex="-1" data-width="760">
  <form id="createRoleForm" name="roleForm" class="form-horizontal" role="form">
    <div class="modal-header">
      <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
      <h3>设置角色权限</h3>
    </div>
    <div class="modal-body">
      <div class="form-group">
        <div class="row ">
          <div class="col-md-3 col-xs-3 form-group role-item ellips">
            <label>
              <input @click="selAllCb(permissions,$event)" style="margin-left:40px" id="selAllCb"
                     type="checkbox"
                     data-checkbox="sub">
              所有权限</label>
          </div>
        </div>
        <div v-for="permission in permissions">
          <div class="row ">
            <div class="col-md-3 col-xs-3 form-group role-item ellips">
              <label>
                <input @click="checkAll(permission,$event)" style="margin-left:40px" type="checkbox"
                       data-checkbox="select"> {{permission.key}}</label>
            </div>
          </div>

          <div class="row" style="margin-left:60px">

            <div v-for="content in permission.content"
                 class="col-md-4 col-xs-4  form-group role-item ellips">
              <label>
                <input @click="checkSub(content,$event)" type="checkbox" :checked="content.checked"
                       data-checkbox="sub">{{content.name}}</label>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="modal-footer">
      <button :disabled="disabled" type="button" data-dismiss="modal" class="btn">关闭</button>
      <button :disabled="disabled" type="button" @click="savePermission" class="btn btn-primary">保存</button>
    </div>
  </form>
</div>
<script src="/static/admin/js/apps/role/list.js?v=ea38b0f7"></script>