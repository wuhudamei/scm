<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<script type="text/javascript">
  var hasEditCatalogPrivelege = false;
  <shiro:hasPermission name="catalog:edit">
  hasEditCatalogPrivelege = true;
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
                  placeholder="分类名称" class="form-control"/>
            </div>
          </div>
          <div class="col-md-2">
            <div class="form-group">
              <select v-model="form.status" placeholder="状态"
                      class="form-control">
                <option value="">全部状态</option>
                <option value="OPEN">启用</option>
                <option value="LOCK">停用</option>
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

          <shiro:hasPermission name="catalog:edit">
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


<div id="modal" class="modal fade" tabindex="-1" data-width="650">
  <validator name="validation">
    <form name="user" novalidate class="form-horizontal" role="form">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3>添加/编辑商品类目</h3>
      </div>
      <div class="modal-body">

        <div class="form-group" :class="{'has-error':$validation.name.invalid && $validation.touched}">
          <label for="name" class="control-label col-sm-3"><span style="color: red">*</span>类目名称：</label>
          <div class="col-sm-9">
            <input
                v-model="name"
                v-validate:name="{
                    required:{rule:true,message:'请输入类目名称'},
                    maxlength:{rule:50,message:'分类名称最长不能超过50个字符'}
                }"
                data-tabindex="1"
                v-model="name" type="text" placeholder="类目名称" class="form-control">
            <span v-cloak v-if="$validation.name.invalid && $validation.touched" class="help-absolute">
                <span v-for="error in $validation.name.errors">
                    {{error.message}} {{($index !== ($validation.name.errors.length -1)) ? ',':''}}
                </span>
            </span>
          </div>
        </div>

         <!-- 父分类 -->
        <div class="form-group">
          <label class="control-label col-sm-3">父类目：</label>
          <div class="col-sm-5">
            <select v-model="parentId"  placeholder="父类目"
                    class="form-control" >
              <option  v-if="!isEdit || parentId == 0" selected :value="0">----请选择----</option>
              <option  v-else :value="0">----请选择----</option>
              <!-- 将选中的url上传上去 -->
              <option  v-for="item in catalogList" v-bind:value="item.id" >{{{item.name}}}</option>
            </select>
          </div>
        </div>

        <div class="form-group" v-if="show" :class="{'has-error':$validation.catalogType.invalid &&  $validation.touched}">
          <label for="CatalogType" class="control-label col-sm-3">类目类型：</label>
          <div class="col-sm-5">
            <select v-model="catalogType" class="form-control" id="catalogType" name="catalogType"
                    v-validate:catalog-type="{required:{rule:true,message:'类目类型'}}">
              <option  value="">类目类型</option>
              <option v-for="type in types" v-bind:value="type.type">{{type.name}}</option>
              <span v-cloak v-if="$validation.catalogType.invalid &&  $validation.touched"
                    class="help-absolute">
                            <span v-for="error in $validation.catalogType.errors">
                                {{error.message}} {{($index !== ($validation.catalogType.errors.length -1)) ? ',':''}}
                            </span>
                            </span>
            </select>
          </div>
        </div>




        <!-- 排序seq -->
        <div class="form-group" :class="{'has-error':$validation.seq.invalid && $validation.touched}">
          <label for="seq" class="control-label col-sm-3"><span style="color: red">*</span>排序值：</label>
          <div class="col-sm-9">
            <input
                id="seq"
                v-validate:seq="{
                    required:{rule:true,message:'请输入排序值'}
                }"
                v-model="seq" type="number" placeholder="排序值" class="form-control">
            <span v-cloak v-if="$validation.seq.invalid && $validation.touched" class="help-absolute">
                <span v-for="error in $validation.seq.errors">
                    {{error.message}} {{($index !== ($validation.seq.errors.length -1)) ? ',':''}}
                </span>
            </span>
          </div>
        </div>

        <div class="form-group">
          <label for="checkScale" class="control-label col-sm-3">是否复尺：</label>
          <div class="col-sm-5">
            <select v-model="checkScale" placeholder="是否复尺" class="form-control">
              <option v-for="option in checkScaleOptions" v-bind:value="option.value">
                {{ option.text }}
              </option>
            </select>
          </div>
        </div>

        <div class="form-group" v-if="showCataLog">
          <label for="convertUnit" class="control-label col-sm-3">计量单位转换：</label>
          <div class="col-sm-5">
            <select v-model="convertUnit" class="form-control">
              <option value="">请选择转换类型</option>
              <option v-for="list in convertUnitList" :value="list.dicValue">{{list.dicName}}</option>
            </select>
          </div>
        </div>

        <div class="form-group">
          <label for="status" class="control-label col-sm-3">状态：</label>
          <div class="col-sm-5">
            <select v-model="status" placeholder="状态"
                    class="form-control">
             <option v-for="option in statusOptions" v-bind:value="option.value">
                {{ option.text }}
              </option>
            </select>
          </div>
        </div>
        <div class="form-group" v-if="showCataLog" :class="{'has-error':$validation.domains.invalid && $validation.touched}">
          <label class="control-label col-sm-3">所属功能区：</label>
          <span v-if="domainIfon != ''">
              <span v-for="domain in domainIfon" v-if="domain.domainStatus == '1'">
                <input type="checkbox"
                       name="domains"
                       v-model="domains"
                       v-validate:domains="{required:{rule:true,message:'请选择功能区'}}"
                       :value="domain.id" />{{domain.domainName}}
               </span>
            <span v-cloak v-if="$validation.domains.invalid && $validation.touched" class="help-absolute">
                <span v-for="error in $validation.domains.errors">
                    {{error.message}} {{($index !== ($validation.domains.errors.length -1)) ? ',':''}}
                </span>
            </span>
          </span>

        </div>


        <div class="form-group" :class="{'has-error':$validation.lossfactor.invalid && $validation.touched}" v-if="showCataLog">
          <label for="seq" class="control-label col-sm-3">损耗系数：</label>
          <div class="col-sm-9">
            <input v-model="lossFactor"
                   v-validate:lossfactor="['num']"
                   type="number" placeholder="损耗系数"
                   onkeypress="return (/[\d{5}.]/.test(String.fromCharCode(event.keyCode)))" class="form-control">
            <span v-cloak v-if="$validation.lossfactor.invalid && $validation.touched" class="help-absolute">
                <span v-for="error in $validation.lossfactor.errors">
                    {{error.message}} {{($index !== ($validation.lossfactor.errors.length -1)) ? ',':''}}
                </span>
            </span>
          </div>
        </div>
        <div class="form-group" v-if="showCataLog">
          <label for="useDecimal"  class="control-label col-sm-3">是否允许位小数：</label>
          <input type="radio" v-model="useDecimal" :value="1">是&nbsp;&nbsp;&nbsp;
          <input type="radio" v-model="useDecimal" :value="0">否&nbsp;&nbsp;&nbsp;
        </div>
        <div class="form-group" v-if="showCataLog">
          <label for="mealCategory" class="control-label col-sm-3">是否套餐类目：</label>
          <input type="radio" v-model="mealCategory" :value="1">是&nbsp;&nbsp;&nbsp;
          <input type="radio" v-model="mealCategory" :value="0">否&nbsp;&nbsp;&nbsp;
        </div>

        <div class="form-group" style="margin-top: 30px">
          <div class="col-sm-10">
            <p id="tip"  style="color: red;text-align: center">温馨提示：创建一级类目时,不用选择任何父类目!</p>
          </div>
        </div>

      </div>
      <div class="modal-footer">
        <button :disabled="disabled" type="button" data-dismiss="modal" class="btn">关闭</button>
        <button :disabled="disabled" type="button" @click="save" class="btn btn-primary">保存</button>
      </div>
    </form>
  </validator>
</div>

<script src="/static/admin/vendor/webuploader/webuploader.js?v=ea38b0f7"></script>
<script src="/static/admin/js/components/webuploader.js?v=ea38b0f7"></script>
<script src="/static/admin/js/apps/prod/catalog/list.js?v=ea38b0f7"></script>