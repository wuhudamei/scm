<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<script type="text/javascript">
    var hasDictionary = false;
    <shiro:hasPermission name="dict:edit">
        hasDictionary = true;
    </shiro:hasPermission>
</script>
<head>
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

                    <div class="col-md-2 form-group">
                        <input v-model="form.keyword" type="text"
                               placeholder="名称" class="form-control"/>
                    </div>
                    <div class="col-md-2 form-group">
                        <button id="searchBtn" type="submit"
                                class="btn btn-block btn-outline btn-default" alt="搜索"
                                title="搜索">
                            <i class="fa fa-search"></i>
                        </button>
                    </div>
                </form>

                <div class="col-md-7 text-right">
                    <shiro:hasPermission name="dict:edit">
                        <div class="form-group">
                            <button @click="createBtnClickHandler" id="createBtnClickHandler" type="button"
                                    class="btn btn-outline btn-primary">新增
                            </button>
                        </div>
                    </shiro:hasPermission>
                </div>
            </div>
            <!-- <div class="columns columns-right btn-group pull-right"></div> -->
            <table id="dataTable" width="100%"
                   class="table table-striped table-bordered table-hover">
            </table>
        </div>
    </div>
    <!-- ibox end -->
</div>
<div id="model2" class="modal fade" tabindex="-1" style="width: auto;height: auto">
    <validator name="validation">
        <form name="user" novalidate class="form-inline" role="form">
            <div class="modal-header font-bold" align="center" style="font-size: larger;padding-top: 20px">
                数据字典编辑
            </div>
            <div class="modal-content" style="border-radius: 0;">
                <div>
                    <h3 style="color:rgba(7, 128, 236, 0.58);border-bottom: 1px solid #ccc;text-align: center;padding-top: 8px;padding-bottom: 8px;">字典标识（父级标识）</h3>
                    <div style="padding-bottom:20px;clear: both;" class="col-md-6">
                        <div class="form-group"
                             :class="{'has-error':$validation.dicName.invalid && $validation.touched}">
                            <label>字典名称：</label>
                            <input v-model="dictionary.dicName"
                                   v-validate:dic-name="{required:{rule:true,message:'请输入字典名称'},
                                                               maxlength:{rule:30,message:'不能超过20个字符'}}"
                                   type="text"
                                   placeholder="字典名称"
                                   class="form-control"/>
                            <span v-cloak
                                  v-if="$validation.dicName.invalid && $validation.touched"
                                  class="help-absolute">
                                                            <span v-for="error in $validation.dicName.errors">
                                                                {{error.message}} {{($index !== ($validation.dicName.errors.length -1)) ? ',':''}}
                                                            </span>
                                                        </span>
                        </div>
                </div>
                    <div style="padding-bottom:20px;" class="col-md-6">
                        <div class="form-group"
                             :class="{'has-error':$validation.dicValue.invalid && $validation.touched}">
                            <label>字典标识：</label>
                            <input v-model="dictionary.dicValue"
                                   v-validate:dic-value="{required:{rule:true,message:'请输入字典标识'},
                                                               maxlength:{rule:30,message:'不能超过20个字符'}}"
                                   type="text"
                                   placeholder="字典标识"
                                   class="form-control"/>
                            <span v-cloak
                                  v-if="$validation.dicValue.invalid && $validation.touched"
                                  class="help-absolute">
                                                            <span v-for="error in $validation.dicValue.errors">
                                                                {{error.message}} {{($index !== ($validation.dicValue.errors.length -1)) ? ',':''}}
                                                            </span>
                                                        </span>
                        </div>
                    </div>
                    <div style="padding-bottom:20px;" class="col-md-6">
                        <div class="form-group"
                             :class="{'has-error':$validation.sort.invalid && $validation.touched}">
                            <label>字典排序：</label>
                            <input v-model="dictionary.sort"
                                   v-validate:sort="{required:{rule:true,message:'请输入字典标排序'},
                                                               numeric:{rule:true,message:'排序是正整数'}}"
                                   type="text"
                                   placeholder="字典标排序"
                                   class="form-control"/>
                            <span v-cloak
                                  v-if="$validation.sort.invalid && $validation.touched"
                                  class="help-absolute">
                                                            <span v-for="error in $validation.sort.errors">
                                                                {{error.message}} {{($index !== ($validation.sort.errors.length -1)) ? ',':''}}
                                                            </span>
                                                        </span>
                        </div>
                    </div>
                    <div style="padding-bottom:20px;" class="form-group col-md-6"
                             :class="{'has-error':$validation.status.invalid && $validation.touched}">
                            <label>字典状态：</label>
                            <select v-model="dictionary.status" style="width: 200px;"
                                    v-validate:status="{required:{rule:true,message:'请选择字典状态'},
                                                               maxlength:{rule:30,message:'不能超过20个字符'}}"
                                    type="text"
                                    placeholder="字典状态"
                                    class="form-control">
                                <option value="1">启用</option>
                                <option value="0">禁用</option>
                            </select>
                            <span v-cloak
                                  v-if="$validation.status.invalid && $validation.touched"
                                  class="help-absolute">
                                                            <span v-for="error in $validation.status.errors">
                                                                {{error.message}} {{($index !== ($validation.status.errors.length -1)) ? ',':''}}
                                                            </span>
                                                        </span>
                        </div>
                    <div style="padding-bottom:20px;" class="form-group col-md-12"
                         :class="{'has-error':$validation.remarks.invalid && $validation.touched}">
                        <label>字典备注：</label>
                        <input v-model="dictionary.remarks"
                        <%-- v-validate:remarks="{required:{rule:true,message:'请输字典备注'},
                                 maxlength:{rule:30,message:'不能超过20个字符'}}"--%>
                               type="text"
                               placeholder="字典备注"
                               class="form-control"/>
                        <span v-cloak
                              v-if="$validation.remarks.invalid && $validation.touched"
                              class="help-absolute">
                                                        <span v-for="error in $validation.remarks.errors">
                                                            {{error.message}} {{($index !== ($validation.remarks.errors.length -1)) ? ',':''}}
                                                        </span>
                                                    </span>
                    </div>
                </div>
                <shiro:hasPermission name="dict:edit">
                    <div class="row">
                        <div style="margin-left:20px;" class="col-md-1">
                            <div class="form-group">
                                <button @click="addData()" id="createBtn1" type="button"
                                        class="btn btn-block btn-outline btn-primary">新增字典值
                                </button>
                            </div>
                        </div>
                    </div>
                </shiro:hasPermission>
                <h3 style="color:rgba(7, 128, 236, 0.58);border-bottom: 1px solid #ccc;text-align: center;padding-top: 8px;padding-bottom: 8px;">字典标识（子级标识：对应的值）</h3>

                <div v-for="item in list" track-by="id"
                         style="text-align: center;border-bottom: 1px solid #ddd;padding:10px;">
                        <div class="form-group col-md-6" style="padding-bottom:20px;clear: both;"
                             :class="{'has-error':$validation.dicName.invalid && $validation.touched}">
                            <label>字典名称：</label>
                            <input v-model="item.dicName"
                                   v-validate:dic-name="{required:{rule:true,message:'请输入字典名称'},
                                                           maxlength:{rule:30,message:'不能超过20个字符'}}"
                                   type="text"
                                   placeholder="字典名称"
                                   class="form-control"/>
                            <span v-cloak
                                  v-if="$validation.dicName.invalid && $validation.touched"
                                  class="help-absolute">
                                                        <span v-for="error in $validation.dicName.errors">
                                                            {{error.message}} {{($index !== ($validation.dicName.errors.length -1)) ? ',':''}}
                                                        </span>
                                                    </span>
                        </div>
                        <div class="form-group col-md-6" style="padding-bottom:20px;"
                             :class="{'has-error':$validation.dicValue.invalid && $validation.touched}">
                            <label>字典标识：</label>
                            <input v-model="item.dicValue"
                                   v-validate:dic-value="{required:{rule:true,message:'请输入字典标识'},
                                                           maxlength:{rule:30,message:'不能超过20个字符'}}"
                                   type="text"
                                   placeholder="字典标识"
                                   class="form-control"/>
                            <span v-cloak
                                  v-if="$validation.dicValue.invalid && $validation.touched"
                                  class="help-absolute">
                                                        <span v-for="error in $validation.dicValue.errors">
                                                            {{error.message}} {{($index !== ($validation.dicValue.errors.length -1)) ? ',':''}}
                                                        </span>
                                                    </span>
                        </div>
                        <div class="form-group col-md-6" style="padding-bottom:20px;"
                             :class="{'has-error':$validation.sort.invalid && $validation.touched}">
                            <label>字典排序：</label>
                            <input v-model="item.sort"
                                   v-validate:sort="{required:{rule:true,message:'请输入字典标排序'},
                                                           numeric:{rule:true,message:'排序是正整数'}}"
                                   type="text"
                                   placeholder="字典标排序"
                                   class="form-control"/>
                            <span v-cloak
                                  v-if="$validation.sort.invalid && $validation.touched"
                                  class="help-absolute">
                                                        <span v-for="error in $validation.sort.errors">
                                                            {{error.message}} {{($index !== ($validation.sort.errors.length -1)) ? ',':''}}
                                                        </span>
                                                    </span>
                        </div>
                        <div class="form-group col-md-6" style="padding-bottom:20px;"
                             :class="{'has-error':$validation.status.invalid && $validation.touched}">
                            <label>字典状态：</label>
                            <select v-model="item.status"
                                    v-validate:status="{required:{rule:true,message:'请选择字典状态'},
                                                           maxlength:{rule:30,message:'不能超过20个字符'}}"
                                    type="text"
                                    placeholder="字典状态"
                                    class="form-control" style="width: 200px;">
                                <option value="1">启用</option>
                                <option value="0">禁用</option>
                            </select>
                            <span v-cloak
                                  v-if="$validation.status.invalid && $validation.touched"
                                  class="help-absolute">
                                                        <span v-for="error in $validation.status.errors">
                                                            {{error.message}} {{($index !== ($validation.status.errors.length -1)) ? ',':''}}
                                                        </span>
                                                    </span>
                        </div>
                        <div class="form-group col-md-6" style="padding-bottom:20px;"
                             :class="{'has-error':$validation.remarks.invalid && $validation.touched}">
                            <label>字典备注：</label>
                            <input v-model="item.remarks"
                            <%-- v-validate:remarks="{required:{rule:true,message:'请输字典备注'},
                                     maxlength:{rule:30,message:'不能超过20个字符'}}"--%>
                                   type="text"
                                   placeholder="字典备注"
                                   class="form-control"/>
                            <span v-cloak
                                  v-if="$validation.remarks.invalid && $validation.touched"
                                  class="help-absolute">
                                                        <span v-for="error in $validation.remarks.errors">
                                                            {{error.message}} {{($index !== ($validation.remarks.errors.length -1)) ? ',':''}}
                                                        </span>
                                                    </span>
                        </div>
                       <shiro:hasPermission name="dict:edit">
                        <button @click="removeData($index)" type="button" style="text-align: center"
                                class="btn btn-primary btn-sm">删除
                        </button>
                       </shiro:hasPermission>
                    </div>
                    <div style="text-align: center;padding-top:30px;padding-bottom:30px;" >
                        <button type="button" data-dismiss="modal" class="btn">关闭</button>
                        <button type="button" @click="insert" class="btn btn-primary">保存
                        </button>
                    </div>
                </div>

        </form>
    </validator>
</div>
<%@include file="/WEB-INF/views/admin/components/select.jsp" %>
<script src="/static/admin/vendor/webuploader/webuploader.js"></script>
<script src="/static/admin/js/components/webuploader.js"></script>
<script src="/static/admin/js/components/select.js"></script>
<script src="/static/admin/js/apps/dictionary/list.js"></script>