<%@ page contentType="text/html;charset=UTF-8" %>
<head>
  <style>
    .webuploader-pick{
      display: block;
    }
</style>
<link rel="stylesheet" href="/static/admin/css/select.css">
</head>
  <div id="container" class="wrapper wrapper-content animated fadeInRight">
    <div id="breadcrumb">
      <bread-crumb :crumbs="breadcrumbs"></bread-crumb>
    </div>
    <!-- ibox start -->
    <div class="ibox">
      <div class="ibox-content">
        <div class="row">
          <form id="searchForm">

            <div class="col-md-1">
              <div class="form-group">
                <button @click="addData()" id="createBtn" type="button" class="btn btn-block btn-outline btn-primary">新增
              </button>
              </div>
            </div>
          </form>
        </div>
        <div class="row">
          <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
            <div class="panel panel-default">
              <div class="panel-heading" role="tab" id="headingOne">
                <h4 class="panel-title">
                  <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                    合同信息
                  </a>
                </h4>
              </div>
              <div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne">
                <div class="panel-body">
                  <validator name="validation">
                    <form novalidate class="form-inline">
                      <div class="row">
                        <div v-for="item in list" track-by="id" style="padding:10px;">
                          <div class="form-group" :class="{'has-error':$validation[item.supplierField].invalid && $validation.touched}">
                            <label class="sr-only" :for="item.supplierField">供应商</label>
                             <input 
                              :id="item.supplierField"
                              :field="item.supplierField"
                              v-validate="{suppliers:{rule:{peoples:item.supplier},message:'请选择供应商'}}"
                              v-model="item.supplier.length"
                              type="text" 
                              class="form-control" 
                              placeholder="供应商" style="display:none;"> 
                            <vue-select2
                              :disabled="false"
                              :allow-clear="false"
                              :selected-list.sync="item.supplier"
                              :multiple="false"></vue-select2>
                            <span v-cloak v-if="$validation[item.supplierField].invalid && $validation.touched" class="help-absolute">
                              <span v-for="error in $validation[item.supplierField].errors">
                                  {{error.message}} {{($index !== ($validation[item.supplierField].errors.length -1)) ? ',':''}}
                              </span>
                            </span>
                          </div>
                          <div class="form-group" :class="{'has-error':$validation[item.brandField].invalid && $validation.touched}">
                            <label class="sr-only" :for="item.brandField">品牌</label>
                            <input 
                              :id="item.brandField"
                              :field="item.brandField"
                              v-validate="item.brandValidate"
                              v-model="item.brand"
                              type="text" 
                              class="form-control" 
                              placeholder="品牌">
                            <span v-cloak v-if="$validation[item.brandField].invalid && $validation.touched" class="help-absolute">
                              <span v-for="error in $validation[item.brandField].errors">
                                  {{error.message}} {{($index !== ($validation[item.brandField].errors.length -1)) ? ',':''}}
                              </span>
                            </span>
                          </div>
                          <div class="form-group">
                            <!-- <label class="sr-only" for=""></label> -->
                              <web-uploader :target-id="'webuploader' + $index"
                                :type="webUploader.type"
                                :w-object-id="item.id"
                                :w-accept="webUploader.accept"
                                :w-server="webUploader.server"
                                :w-file-size-limit="webUploader.fileSizeLimit"
                                :w-file-single-size-limit="webUploader.fileSingleSizeLimit"
                                :w-form-data="webUploader.formData">
                              <button type="button" class="btn btn-primary">上传图片</button>
                            </web-uploader>
                          </div>
                          <div class="form-group" :class="{'has-error':$validation[item.remarkField].invalid && $validation.touched}">
                            <label class="sr-only" :for="item.remarkField">备注</label>
                            <input 
                              :id="item.remarkField"
                              :field="item.remarkField"
                              v-validate="item.remarkValidate"
                              v-model="item.remark"
                              maxlength="100"
                              type="text" 
                              class="form-control" 
                              placeholder="备注">
                            <span v-cloak v-if="$validation[item.remarkField].invalid && $validation.touched" class="help-absolute">
                              <span v-for="error in $validation[item.remarkField].errors">
                                  {{error.message}} {{($index !== ($validation[item.remarkField].errors.length -1)) ? ',':''}}
                              </span>
                            </span>
                          </div>

                          <button @click="removeData($index)" type="button" class="btn btn-default">删除</button>
                        </div>
                      </div>
                    </form>
                  </validator>
                </div>
              </div>
            </div>
            <!-- <div class="panel panel-default">
              <div class="panel-heading" role="tab" id="headingTwo">
                <h4 class="panel-title">
                  <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" aria-expanded="false"
                    aria-controls="collapseTwo">
                    Collapsible Group Item #2
                  </a>
                </h4>
              </div>
              <div id="collapseTwo" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTwo">
                <div class="panel-body">
                  Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry richardson ad squid. 3 wolf moon officia aute,
                  non cupidatat skateboard dolor brunch. Food truck quinoa nesciunt laborum eiusmod. Brunch 3 wolf moon tempor,
                  sunt aliqua put a bird on it squid single-origin coffee nulla assumenda shoreditch et. Nihil anim keffiyeh
                  helvetica, craft beer labore wes anderson cred nesciunt sapiente ea proident. Ad vegan excepteur butcher
                  vice lomo. Leggings occaecat craft beer farm-to-table, raw denim aesthetic synth nesciunt you probably
                  haven't heard of them accusamus labore sustainable VHS.
                </div>
              </div>
            </div>
            <div class="panel panel-default">
              <div class="panel-heading" role="tab" id="headingThree">
                <h4 class="panel-title">
                  <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseThree" aria-expanded="false"
                    aria-controls="collapseThree">
                    Collapsible Group Item #3
                  </a>
                </h4>
              </div>
              <div id="collapseThree" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingThree">
                <div class="panel-body">
                  Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry richardson ad squid. 3 wolf moon officia aute,
                  non cupidatat skateboard dolor brunch. Food truck quinoa nesciunt laborum eiusmod. Brunch 3 wolf moon tempor,
                  sunt aliqua put a bird on it squid single-origin coffee nulla assumenda shoreditch et. Nihil anim keffiyeh
                  helvetica, craft beer labore wes anderson cred nesciunt sapiente ea proident. Ad vegan excepteur butcher
                  vice lomo. Leggings occaecat craft beer farm-to-table, raw denim aesthetic synth nesciunt you probably
                  haven't heard of them accusamus labore sustainable VHS.
                </div>
              </div>
            </div> -->
          </div>
        </div>
      </div>
    </div>
    <!-- ibox end -->
  </div>
  <%@include file="/WEB-INF/views/admin/components/select.jsp" %>
  <script src="/static/admin/vendor/webuploader/webuploader.js"></script>
  <script src="/static/admin/js/components/webuploader.js"></script>
  <script src="/static/admin/js/components/select.js"></script>
  <script src="/static/admin/js/test.js"></script>