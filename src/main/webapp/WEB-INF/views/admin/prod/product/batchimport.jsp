<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<script type="text/javascript">
    var hasEditProductPrivelege = false;
    <shiro:hasPermission name="product:edit">
    hasEditProductPrivelege = true;
    </shiro:hasPermission>
</script>

<!-- 导入-->
<div id="container" class="wrapper wrapper-content animated fadeInRight">
    <div id="breadcrumb">
        <bread-crumb :crumbs="breadcrumbs"></bread-crumb>
    </div>
    <div class="ibox">
        <div class="ibox-title">商品导入</div>
        <div class="ibox-content">
            <div class="row">
                <validator name="validation">
                    <form name="banner" novalidate class="form-horizontal" role="form">
                        <!-- 上传图片 -->
                        <div class="form-group">
                            <label for="logo" class="col-sm-2 control-label">商品文件:</label>
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
                                <div style="margin-bottom:0;"
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
                            <label class="control-label col-sm-2">商品模版:</label>
                            <div class="col-sm-9">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <button @click="downloadFile('')" type="button"
                                                class="btn btn-block btn-outline btn-primary">下载模版
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </validator>
            </div>
        </div>
    </div>

    <div class="ibox">
        <div class="ibox-title">导入结果</div>
        <div class="ibox-content">
            <div>
                <p v-for="item in dataResult">{{item}}</p>
            </div>
        </div>
    </div>
</div>

<div id="dealingModal" class="modal fade" tabindex="-1" data-width="150">
    <div>正在处理中..................</div>
</div>

<script src="${ctx}/static/admin/js/components/jquery.form.min.js?v=ea38b0f7"></script>
<script src="/static/admin/vendor/webuploader/webuploader.js?v=ea38b0f7"></script>
<script src="/static/admin/js/components/webuploader.js?v=ea38b0f7"></script>
<script src="/static/admin/js/apps/prod/product/batchimport.js?v=ea38b0f7"></script>