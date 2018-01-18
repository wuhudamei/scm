<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<script type="text/javascript">
  var hasEditProductPrivelege = false;
  <shiro:hasPermission name="product:edit">
  hasEditProductPrivelege = true;
  </shiro:hasPermission>
</script>
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
            <input
                v-model="form.keyword" type="text"
                placeholder="商品编码/名称" class="form-control"/>
          </div> 

          <div class="col-md-2">
            <select v-model="form.status"
                    placeholder="商品状态"
                    class="form-control">
              <option v-for="applyStatusEnum in applyStatusList" :value="applyStatusEnum.value">{{applyStatusEnum.label}}</option>
            </select>
          </div>
          
          <div class="col-md-3">
            <select
                v-model="form.catalogUrl"
                class="form-control">
              <option value="">请选择商品类目</option>
              <option v-for="category in allCatalog" :value="category.id">{{{category.name}}}</option>
            </select>
          </div>

          <div  class="col-md-1">
            <div class="form-group">
              <button id="searchBtn" type="submit" class="btn btn-block btn-outline btn-default" alt="搜索"
                      title="搜索">
                <i class="fa fa-search"></i>
              </button>
            </div>
          </div>

          <shiro:hasPermission name="product:edit">
          <div  class="col-md-1">
            <div class="form-group">
              <button @click="createProduct" type="button"
                      class="btn btn-block btn-outline btn-primary">新增
              </button>
            </div>
          </div>

          <div  class="col-md-2">
            <div class="form-group">
              <button @click="importExcel" type="button"
                      class="btn btn-block btn-outline btn-primary">批量导入
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


<!-- 导入-->
<div id="modal" class="modal fade" tabindex="-1" data-width="600">
  <validator name="validation">
    <form name="banner" novalidate class="form-horizontal" role="form">
      <input type="hidden" v-model="id">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 align="center">批量导入商品</h3>
      </div>
      
      <div class="modal-body">


		<!-- 上传图片 -->
        <div class="form-group">
          <label for="logo" class="col-sm-3 control-label">商品文件:</label>
          <div class="col-sm-9">
            <div class="input-group">
              <input
                  id="logo"
                  v-model="logo"
                  readonly type="text"
                  data-tabindex="1"
                  placeholder="点击上传文件" class="input-sm form-control">
              <span class="input-group-btn">
                <web-uploader :target-id="'webuploader0'"
                              :type="webUploader.type"
                              :w-server="webUploader.server"
                              :w-file-size-limit="webUploader.fileSizeLimit"
                              :w-file-single-size-limit="webUploader.fileSingleSizeLimit"
                              :w-form-data="webUploader.formData">
                  <button id="imageUpload" type="button" class="btn btn-sm btn-primary">导入文件</button>
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
          </div>
        </div>
        
        
        <div class="form-group">
          <label  class="control-label col-sm-3">商品模版:</label>
          <div class="col-sm-9">
            <div  class="col-md-6">
            <div class="form-group">
              <button @click="downloadFile('')" type="button"
                      class="btn btn-block btn-outline btn-primary">下载模版
              </button>
            </div>
          </div>
          </div>
        </div>

        <div class="form-group" :class="{'has-error':$validation.name.invalid && $validation.touched}">
          <label  class="control-label col-sm-3">导入说明:</label>
          <div class="col-sm-9">
          </div>
        </div>

      </div>
      
      <div class="modal-footer">
        <button type="button" data-dismiss="modal" class="btn">关闭</button>
      </div>
    </form>
  </validator>
</div>

<!-- 管理sku-->
<div id="manageSku" class="modal fade" tabindex="-1" >
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 align="center">管理sku</h3>
      </div>

      <div class="modal-body">
              <form  @submit.prevent="query">
                <div class="col-md-4">
                  <input
                          v-model="form.keyword" type="text"
                          placeholder="sku编码/名称" class="form-control"/>
                </div>
                <div  class="col-md-4"></div>
                <div  class="col-md-2">
                  <div class="form-group">
                    <button  type="submit" class="btn btn-block btn-outline btn-default" alt="搜索"
                             title="搜索">
                      <i class="fa fa-search"></i>
                    </button>
                  </div>
                </div>

                <div  class="col-md-2">
                  <div class="form-group">
                    <button @click="addSku" type="button" v-if="addFlag"
                            class="btn btn-block btn-outline btn-primary">新增
                    </button>
                  </div>
                </div>
              </form>
            <!-- <div class="columns columns-right btn-group pull-right"></div> -->
            <table id="skuTable" width="100%" class="table table-striped table-bordered table-hover">
            </table>
        <!-- ibox end -->

      </div>

      <div class="modal-footer">
        <button type="button" data-dismiss="modal" class="btn">关闭</button>
      </div>


</div>
<!-- 增加sku-->
<div id="addSkuModel" class="modal fade" tabindex="-1" >
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
    <h3 align="center">增加sku</h3>
  </div>

  <div class="modal-body">
    <validator name="validation">
    <form novalidate class="form-horizontal" role="form">

      <div class="form-group"  v-if="mate.attribute1Name!=null&& mate.attribute1Name!='' "
           :class="{'has-error':$validation.attribute1.invalid && $validation.touched}">
        <label for="attribute1" class="control-label col-sm-3"> <span style="color:red" >*</span>{{mate.attribute1Name}}：</label>
        <div class="col-sm-9">
          <input type="text"id="attribute1" class="form-control"
                 v-validate:attribute1="{required:true}"
                 v-model="sku.attribute1" placeholder="请填写{{mate.attribute1Name}}">
          <span  v-if="$validation.attribute1.invalid && $validation.touched" class="help-absolute">
                           请填写{{mate.attribute1Name}}
                      </span>
        </div>
      </div>

      <div class="form-group" v-if="mate.attribute2Name!=null&& mate.attribute2Name!='' "
           :class="{'has-error':$validation.attribute2.invalid && $validation.touched}">
        <label for="attribute2" class="control-label col-sm-3"> <span style="color:red" >*</span>{{mate.attribute2Name}}：</label>
        <div class="col-sm-9">
          <input type="text"id="attribute2" class="form-control"
                 v-validate:attribute2="{required:true}"
                 v-model="sku.attribute2" placeholder="请填写{{mate.attribute2Name}}">
          <span  v-if="$validation.attribute2.invalid && $validation.touched" class="help-absolute">
                           请填写{{mate.attribute2Name}}
                      </span>
        </div>
      </div>
      <div class="form-group" v-if="mate.attribute3Name!=null&& mate.attribute3Name!='' "
           :class="{'has-error':$validation.attribute3.invalid && $validation.touched}">
        <label for="attribute3" class="control-label col-sm-3"> <span style="color:red" >*</span>{{mate.attribute3Name}}：</label>
        <div class="col-sm-9">
          <input type="text"id="attribute3" class="form-control"
                 v-validate:attribute3="{required:true}"
                 v-model="sku.attribute3" placeholder="请填写{{mate.attribute3Name}}">
          <span  v-if="$validation.attribute3.invalid && $validation.touched" class="help-absolute">
                           请填写{{mate.attribute3Name}}
                      </span>
        </div>
      </div>
      <div class="form-group"
           :class="{'has-error':$validation.stock.invalid && $validation.touched}">
        <label for="stock" class="control-label col-sm-3"> <span style="color:red" >*</span>库存：</label>
        <div class="col-sm-9">
          <input type="text" id="stock" class="form-control"
                 v-validate:stock="['stock']"
                 v-model="sku.stock" placeholder="填写库存"
                  >
          <span  v-if="$validation.stock.invalid && $validation.touched" class="help-absolute">
                           请填写正确的库存
                      </span>
        </div>
      </div>


      <div  class="form-group">
          <label for="imageUploadMain" class="control-label col-sm-3">sku图片：</label>
          <div class="col-sm-4">
            <div>
              <web-uploader :type="webUploaderMain.type" :w-server="webUploaderMain.server" :w-accept="webUploaderMain.accept" :w-file-size-limit="webUploaderMain.fileSizeLimit"
                            :w-file-single-size-limit="webUploaderMain.fileSingleSizeLimit" :w-thumb="webUploaderMain.thumb" :w-form-data="webUploaderMain.formData">
                <button id="imageUploadMain" type="button" class="btn btn-sm btn-primary">上传图片
                </button>
              </web-uploader>
            </div>
            <div v-if="sku.fullPath!=null">
              <div class="pull-left" style="margin-right:5px;position:relative;">
                <button @click="deleteImg(path,$index,'main')" style="font-size:18px;line-height:.5;position:absolute;top:1px;right:1px;"
                        type="button" class="close"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <img :src="sku.fullPath"  alt="图片" width="60px" height="60px">
              </div>
            </div>
          </div>

      </div>

    </form>
    </validator>
  </div>

  <div class="modal-footer">
      <button type="button" @click.stop.prevent="submitAdd" class="btn btn-primary">提交</button>
      <button type="button" data-dismiss="modal" class="btn">关闭</button>
  </div>


</div>


<script src="${ctx}/static/admin/js/components/jquery.form.min.js?v=ea38b0f7"></script>
<script src="/static/admin/vendor/webuploader/webuploader.js?v=ea38b0f7"></script>
<script src="/static/admin/js/components/webuploader.js?v=ea38b0f7"></script>
<script src="/static/admin/js/apps/prod/product/list.js?v=ea38b0f7"></script>