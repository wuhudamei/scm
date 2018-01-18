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
                    <div class="col-md-3">
                        <div class="form-group">
                            <input
                                    v-model="form.keyword"
                                    type="text"
                                    placeholder="用户名/名字/手机" class="form-control"/>
                        </div>
                    </div>

                    <div class="col-md-3">
                        <div class="form-group">
                            <select v-model="form.acctType" class="form-control">
                                <option value="">账户类型</option>
                                <option value="ADMIN">管理员</option>
                                <option value="REGION_SUPPLIER">区域供应商</option>
                                <option value="PROD_SUPPLIER">商品供应商</option>
                                <option value="STORE">门店管理员</option>
                                <option value="FINANCE">财务人员</option>
                                <option value="MATERIAL_MANAGER">材料部主管</option>
                                <option value="CHAIRMAN_FINANCE">董事长财务</option>
                                <option value="MATERIAL_CLERK">材料部下单员</option>
                            </select>
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
                          <button @click="initAccount" id="initAccountBtn" type="button"
                                  class="btn btn-block btn-outline btn-primary">同步
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


<div id="modal" class="modal fade" tabindex="-1" data-width="600">
    <validator name="validation">
        <form name="user" novalidate class="form-horizontal" role="form">
            <input v-model="id" type="hidden">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3 v-if="id==null" align="center">添加用户</h3>
                <h3 v-else align="center">编辑用户</h3>
            </div>

            <div class="modal-body">
                <div class="form-group">
                    <div class="col-sm-9" :class="{'has-error':$validation.loginname.invalid && $validation.touched}">
                        <label for="loginName" class="control-label"><span style="color: red">*</span>用户名：</label>
                        <input :disabled="id!=null" v-model="username"
                               type="text" placeholder="用户名" class="form-control"
                               v-validate:loginname="{
              required:{rule:true,message:'请输入用户名'},
              maxlength:{rule:20,message:'用户名最长不能超过20个字符'}
          }">
                        <span v-cloak v-if="$validation.loginname.invalid && $validation.touched" class="help-absolute">
            <span v-for="error in $validation.loginname.errors">
                {{error.message}} {{($index !== ($validation.loginname.errors.length -1)) ? ',':''}}
            </span>
        </span>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-sm-9" :class="{'has-error':$validation.name.invalid && $validation.touched}">
                        <label for="name" class="control-label"><span style="color: red">*</span>用户姓名：</label>
                        <input
                            v-model="name" type="text" placeholder="用户姓名" class="form-control" disabled="true"
                            v-validate:name="{
                                required:{rule:true,message:'请输入用户姓名'},
                                maxlength:{rule:20,message:'用户姓名最长不能超过20个字符'}
                            }
                        ">

                        <span v-cloak v-if="$validation.name.invalid && $validation.touched" class="help-absolute">
                            <span v-for="error in $validation.name.errors">
                                {{error.message}} {{($index !== ($validation.name.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-sm-9">
                        <label for="mobile" class="control-label">手机号：</label>
                        <input v-model="mobile" type="number" placeholder="手机号" disabled="true" class="form-control">
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-sm-9">
                        <label for="acctType" class="control-label">账户类型：</label>
                        <select id="acctType" v-model="acctType" class="form-control">
                            <option value="">请选择账户类型</option>
                            <option value="ADMIN">管理员</option>
                            <option value="STORE">门店管理员</option>
                            <option value="REGION_SUPPLIER">区域供应商</option>
                            <option value="PROD_SUPPLIER">商品供应商</option>
                            <option value="FINANCE">财务人员</option>
                            <option value="MATERIAL_MANAGER">材料部主管</option>
                            <option value="CHAIRMAN_FINANCE">董事长财务</option>
                            <option value="MATERIAL_CLERK">材料部下单员</option>
                        </select>
                    </div>
                </div>
                <div class="form-group" v-show="acctType!='ADMIN' && acctType!='CHAIRMAN_FINANCE'">
                    <div class="col-sm-9">
                        <label for="store" class="control-label">门店：</label>
                        <select v-model="storeCode" class="form-control" @change="chooseStore">
                            <option value="">请选择门店</option>
                            <option v-for="store in storeArray" :value="store.code" v-text="store.name"></option>
                        </select>
                    </div>
                </div>
                <div class="form-group" v-show="acctType == 'REGION_SUPPLIER' || acctType == 'PROD_SUPPLIER'">
                    <div class="col-sm-9">
                        <label for="region" class="control-label">区域供应商：</label>
                        <select v-model="regionId" class="form-control" @change="choolseRegionSupply">
                            <option value="">请选择区域供应商</option>
                            <option v-for="region in regionSupplierArray" :value="region.id"
                                    v-text="region.name"></option>
                        </select>
                    </div>
                </div>

                <div class="form-group" v-show="acctType=='PROD_SUPPLIER'">
                    <div class="col-sm-9">
                        <label for="org" class="control-label">商品供应商：</label>
                        <select v-model="supplierId" class="form-control">
                            <option value="">请选择商品供应商</option>
                            <option v-for="supplier in supplierArray" :value="supplier.id"
                                    v-text="supplier.name"></option>
                        </select>
                    </div>
                </div>

                <!--
                <div class="form-group">
                    <div class="col-sm-9">
                        <label for="position" class="control-label">岗位：</label>
                        <input v-model="position" disabled="true" type="text" placeholder="岗位" class="form-control">
                    </div>
                </div>

                <div class="form-group">
                   <div class="col-sm-9">
                   <label for="org" class="control-label"><span style="color: red">*</span>状态：</label>
                    <select  v-model="status" class="form-control">
                     <option value="OPEN">启用</option>
                     <option value="LOCK">禁用</option>
                   </select>
                 </div>
                </div> -->

            </div>
            <div class="modal-footer">
                <button :disabled="disabled" type="button" data-dismiss="modal" class="btn">关闭</button>
                <button :disabled="submitting" type="button" @click="save" class="btn btn-primary">保存</button>
            </div>
        </form>
    </validator>
</div>

<div id="setRoleModal" class="modal fade" tabindex="-1" data-width="760">
    <form novalidate class="form-horizontal" role="form">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            <h3>设置角色</h3>
        </div>
        <div class="modal-body">
            <div class="form-group">
                <div class="row " style="margin-left:60px">
                    <div v-for="role in roles" class="col-md-4 col-xs-4  form-group role-item ellips">
                        <label title="{{description}}">
                            <input type="checkbox" :checked="role.checked"
                                   @click="checkSub(role,$event)" data-checkbox="sub">
                            {{role.name}}</label>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <button :disabled="disabled" type="button" data-dismiss="modal" class="btn">关闭</button>
            <button :disabled="disabled" type="button" @click="saveRoles" class="btn btn-primary">保存</button>
        </div>
    </form>
</div>

<script src="/static/admin/js/apps/user/list.js?v=ea38b0f7"></script>