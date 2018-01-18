<%@ page contentType="text/html;charset=UTF-8" %>
  <div id="container" class="wrapper wrapper-content animated fadeInRight" v-cloak>
    <div id="breadcrumb">
      <bread-crumb :crumbs="breadcrumbs"></bread-crumb>
    </div>
    <!-- ibox start -->
    <div class="ibox">
      <div class="ibox-content">
        <div class="row">
          <h3>{{productId ? '编辑' :'新建'}}商品</h3>
          <!--col-sm-offset-1-->
          <div class="col-sm-11 ">
            <validator name="validation">
              <form novalidate class="form-horizontal">
              
              
              <div class="col-sm-6 m-t-lg" :class="{'has-error': $validation.allStores.invalid && $validation.touched}">
                  <label for="allStores" class="control-label col-sm-4"><span style="color: red">*</span>门店</label>
                  <div class="col-sm-8">
                    <select name="allStores" id="allStores" class="form-control" 
                    v-validate:all-stores="{required:true}" 
                    v-model="form.allStore.code" >
                    <option value="">请选择</option>
                    <option value="{{item.code}}" v-for="item of allStores">{{item.name}}</option>
                  </select>
                    <span v-cloak v-if="$validation.allStores.invalid && $validation.touched" class="help-absolute">
                       	 请选择门店
                    </span>
                  </div>
                </div>

                <div class="col-sm-6 m-t-lg" :class="{'has-error': $validation.allSuppliers.invalid && $validation.touched}">
                  <label for="allSuppliers" class="control-label col-sm-4"><span style="color: red">*</span>区域供应商</label>
                  <div class="col-sm-8">
                    <select name="allSuppliers" id="allSuppliers" class="form-control" 
                    v-validate:all-suppliers="{required:true}" 
                    v-model="form.allSupplier.id" >
                    <option value="">请选择</option>
                    <option value="{{item.id}}" v-for="item of allSuppliers">{{item.name}}</option>
                  </select>
                    <span v-cloak v-if="$validation.allSuppliers.invalid && $validation.touched" class="help-absolute">
                       	 请选择供应商
                    </span>
                  </div>
                </div>

                <div class="col-sm-6 m-t-lg" :class="{'has-error': $validation.supplierid.invalid && $validation.touched}">
                  <label for="supplierid" class="control-label col-sm-4"><span style="color: red">*</span>商品供应商</label>
                  <div class="col-sm-8">
                    <select name="supplierid" id="supplierid" class="form-control" 
                    v-validate:supplierid="{required:true}" 
                    v-model="form.supplier.id" >
                    <option value="">请选择</option>
                    <option value="{{item.id}}" v-for="item of suppliers">{{item.name}}</option>
                  </select>
                    <span v-cloak v-if="$validation.supplierid.invalid && $validation.touched" class="help-absolute">
                       	 请选择商品供应商
                    </span>
                  </div>
                </div>

               <%-- <div class="col-sm-6 m-t-lg" :class="{'has-error': $validation.code.invalid && $validation.touched}">
                  <label for="code" class="control-label col-sm-4"><span style="color: red">*</span>商品编码</label>
                  <div class="col-sm-8">
                    <input id="code" v-model="form.code" v-validate:code="{
                          required:{rule:true,message:'请输入商品编码'},
                          maxlength:{rule:50,message:'商品编码最长不能超过50个字符'}
                      }" type="text" placeholder="商品编码" class="form-control">
                    <span v-cloak v-if="$validation.code.invalid && $validation.touched" class="help-absolute">
                        <span v-for="error in $validation.code.errors">
                            {{error.message}} {{($index !== ($validation.code.errors.length -1)) ? ',':''}}
                        </span>
                    </span>
                  </div>
                </div>
--%>
                <div class="col-sm-6 m-t-lg" :class="{'has-error':$validation.name.invalid && $validation.touched}">
                  <label for="name" class="control-label col-sm-4"><span style="color: red">*</span>商品名称</label>
                  <div class="col-sm-8">
                    <input id="name" v-model="form.name" v-validate:name="{
                          required:{rule:true,message:'请输入商品名称'},
                          maxlength:{rule:50,message:'商品名称最长不能超过100个字符'}
                      }" type="text" placeholder="商品名称" class="form-control">
                    <span v-cloak v-if="$validation.name.invalid && $validation.touched" class="help-absolute">
                        <span v-for="error in $validation.name.errors">
                            {{error.message}} {{($index !== ($validation.name.errors.length -1)) ? ',':''}}
                        </span>
                    </span>
                  </div>
                </div>

                <div class="col-sm-6 m-t-lg" :class="{'has-error':$validation.category.invalid  && $validation.touched}">
                  <label for="productCategory" class="control-label col-sm-4"><span style="color: red">*</span>商品类目</label>
                  <div class="col-sm-8">
                    <select id="productCategrory" v-validate:category="{required:true}" v-model="form.catalog.url" class="form-control">
                    <option value="">请选择商品类目</option>
                    <option v-for="category in categories" :value="category.url">{{{category.name}}}
                    </option>
                  </select>
                    <span v-if="$validation.category.invalid && $validation.touched" class="help-absolute">
                      <span v-if="$validation.category.invalid">请选择商品类目</span>
                    </span>
                  </div>
                </div>
                <div class="col-sm-6 m-t-lg" :class="{'has-error': $validation.brand.invalid && $validation.touched}">
                  <label for="" class="control-label col-sm-4"><span style="color: red">*</span>商品品牌</label>
                  <div class="col-sm-8">
                    <input @click="selectBrands(true)" v-validate:brand="{required:true}" id="brandId"
                           v-model="brandName" type="text"
                      data-tabindex="9" placeholder="请选择品牌" class="form-control" readonly>
                    <span v-if="$validation.brand.invalid && $validation.touched" class="help-absolute">
                      <span v-if="$validation.brand.invalid">请选择商品品牌</span>
                    </span>
                  </div>
                </div>

                <div class="col-sm-6 m-t-lg">
                  <label for="model" class="control-label col-sm-4">商品型号</label>
                  <div class="col-sm-8">
                    <input id="model" v-model="form.model" type="text" placeholder="商品型号" class="form-control">
                  </div>
                </div>

                <div class="col-sm-6 m-t-lg" :class="{'has-error':$validation.spec.invalid && $validation.touched}">
                  <label for="spec" class="control-label col-sm-4"><span style="color: red">*</span>商品规格</label>
                  <div class="col-sm-8">
                    <input id="spec" v-model="form.spec" v-validate:spec="{
                          specvalidate:{rule:true,message:'规格必须类似100*200*300'}}"
                           type="text" placeholder="商品名称" class="form-control" :disabled="specDisabled">
                    <span v-cloak v-if="$validation.spec.invalid && $validation.touched" class="help-absolute">
                        <span v-for="error in $validation.spec.errors">
                            {{error.message}} {{($index !== ($validation.spec.errors.length -1)) ? ',':''}}
                        </span>
                    </span>
                  </div>
                </div>

                <div class="col-sm-6 m-t-lg" :class="{'has-error':$validation.measureList.invalid  && $validation.touched}">
                  <label for="measureList" class="control-label col-sm-4"><span style="color: red">*</span>计量单位</label>
                  <div class="col-sm-8">
                    <select id="measureList" v-validate:measure-list="{required:true}" v-model="form.measureUnit.id" class="form-control">
                    <option value="">请选择计量单位</option>
                    <option v-for="item in measureList" :value="item.id">{{{item.name}}}
                    </option>
                  </select>
                    <span v-if="$validation.measureList.invalid && $validation.touched" class="help-absolute">
                      <span v-if="$validation.measureList.invalid">请选择计量单位</span>
                    </span>
                  </div>
                </div>

                <div class="col-sm-6 m-t-lg">
                  <label for="imageUploadMain" class="control-label col-sm-4">商品图片</label>
                  <div class="col-sm-4">
                    <div>
                      <web-uploader :type="webUploaderMain.type" :w-server="webUploaderMain.server" :w-accept="webUploaderMain.accept" :w-file-size-limit="webUploaderMain.fileSizeLimit"
                        :w-file-single-size-limit="webUploaderMain.fileSingleSizeLimit" :w-thumb="webUploaderMain.thumb" :w-form-data="webUploaderMain.formData">
                        <button id="imageUploadMain" type="button" class="btn btn-sm btn-primary">上传图片
                    </button>
                      </web-uploader>
                    </div>
                    <div  class="clearfix">
                      <div v-for="imag in form.productImages" class="pull-left" style="margin-right:5px;position:relative;">
                        <button @click="deleteImg(imag.fullPath,$index,'main')" style="font-size:18px;line-height:.5;position:absolute;top:1px;right:1px;"
                          type="button" class="close"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                        <img :src="imag.fullPath" alt="图片" width="60px" height="60px">
                      </div>
                    </div>
                  </div>

                </div>
                <div class="col-sm-6 m-t-lg">
                </div>
                <div class="col-sm-6 m-t-lg" style="display:none;">
                  <label for="spec" class="control-label col-sm-4">是否有sku</label>
                  <div class="col-sm-8">
                    <input id="check" v-model="form.hasSku" type="checkbox" checked placeholder="商品规格" class="form-control" @click="setSku">
                  </div>
                </div>

                <div class="col-sm-12 m-t-lg" style="margin-bottom:10px;" >
                  <label for="detail" class="control-label col-sm-2">商品详情</label>
                  <div class="col-sm-10">
                    <div v-el:ueditor></div>
                  </div>
                </div>
          <div v-if="isShowSku">
                <div class="col-sm-12 m-t-lg" style="margin-bottom:10px;" v-if="form.hasSku">
                  <label for="detail" class="control-label col-sm-2"><span style="color: red">*</span>销售属性 </label>
                  <div class="border col-sm-10 sku">
                    <div class="col-sm-6 margin20">
                      <label for="detail" class="col-sm-3">属性名1 </label>
                      <input type="text" class="col-sm-6" v-model="form.skuMeta.attribute1Name" placeholder="例如：颜色">
                    </div>
                    <div class="col-sm-6 margin20">
                      <label for="detail" class="col-sm-3">属性值 </label>
                      <input type="text" class="col-sm-9" v-model="attribute1" placeholder="例如：白色,红色">
                    </div>
                    <div class="col-sm-6 margin20">
                      <label for="detail" class="col-sm-3">属性名2 </label>
                      <input type="text" class="col-sm-6" v-model="form.skuMeta.attribute2Name" placeholder="例如：尺寸">
                    </div>
                    <div class="col-sm-6 margin20">
                      <label for="detail" class="col-sm-3">属性值 </label>
                      <input type="text" class="col-sm-9" v-model="attribute2" placeholder="例如：XL,XXL">
                    </div>
                    <div class="col-sm-6 margin20">
                      <label for="detail" class="col-sm-3">属性名3 </label>
                      <input type="text" class="col-sm-6" v-model="form.skuMeta.attribute3Name" placeholder="例如：规格">
                    </div>
                    <div class="col-sm-6 margin20">
                      <label for="detail" class="col-sm-3">属性值 </label>
                      <input type="text" class="col-sm-9" v-model="attribute3" placeholder="例如：R15,R16">
                    </div>
                    <div class="col-sm-6 margin20">
                      <button :disabled="submiting" type="button" @click="createSku" class="btn btn-primary m-r-lg" v-if="showSkuBtn">生成</button>
                      <button :disabled="submiting" type="button" @click="clear" class="btn btn-primary m-r-lg" v-if="!showSkuBtn">解除生成</button>
                    </div>
                  </div>
                </div>

                <div class="col-sm-12 m-t-lg" v-if="skus.length != 0 && form.hasSku">
                  <label for="detail" class="control-label col-sm-2"><span style="color: red">*</span>商品sku</label>
                  <div class="border col-sm-10" >
                  <div class="border col-sm-12 margin10" v-for="(index,sku) in skus">
                    <p>序号: {{$index}} {{sku.attribute1}} {{sku.attribute2}} {{sku.attribute3}}</p>


                    <div class="col-sm-6 margin20" :class="{'has-error': $validation['stock'+$index].invalid && $validation.touched}">
                      <label  class="control-label col-sm-3"><span style="color: red">*</span>库存</label>
                      <div class="col-sm-8">
                        <input 
                          v-model="sku.stock" 
                          v-validate="{required:{rule:true,message:'请输入库存'},maxlength:{rule:50,message:'库存最长不能超过50个字符'}}" 
                          type="text" 
                          placeholder="库存" 
                          class="form-control"
                          :field="'stock'+$index" @change="skuChange(sku.stock,'stock')">
                        <span v-cloak v-if="$validation['stock'+$index].invalid && $validation.touched" class="help-absolute">
                            <span v-for="error in $validation['stock'+$index].errors">
                                {{error.message}} {{($index !== ($validation['stock'+$index].errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                      </div>
                    </div>            

<!--                     <div class="col-sm-6 margin20" :class="{'has-error':$validation['status'+$index].invalid && $validation.touched}"> -->
<!--                       <label  class="control-label col-sm-3"><span style="color: red">*</span>状态</label> -->
<!--                       <div class="col-sm-8"> -->
<!--                         <select  -->
<!--                           class="form-control"  -->
<!--                           v-model="sku.status"  -->
<!--                           v-validate="{required:{rule:true,message:'启用/停用状态'}}"  -->
<!--                           :field="'status'+$index"> -->
<!--                           <option value="">请选择</option> -->
<!--                           <option value="OPEN">启用</option> -->
<!--                           <option value="LOCK">停用</option> -->
<!--                         </select> -->
<!--                         <span v-cloak v-if="$validation['status'+$index].invalid && $validation.touched" class="help-absolute"> -->
<!--                           请选择状态 -->
<!--                         </span> -->
<!--                       </div> -->
<!--                     </div> -->


<!--                     <div class="col-sm-6 margin20" v-if="productId != null && sku.id"> -->
<!--                       <label class="control-label col-sm-3">价格</label> -->
<!--                       <div class="col-sm-8"> -->
<!--                         <button type="button" @click="setPrice(sku.id)" class="btn btn-primary m-r-lg">设置价格</button> -->
<!--                       </div> -->
<!--                     </div>       -->

                    <div class="col-sm-6 ">
                      <label for="imageUploadMain" class="control-label col-sm-3">sku图片</label>
                      <div class="col-sm-7">
                        <div>
                          <web-uploader :type="webUploaderMain.sku" :w-server="webUploaderMain.server" :w-accept="webUploaderMain.accept" :w-file-size-limit="webUploaderMain.fileSizeLimit"
                            :w-file-single-size-limit="webUploaderMain.fileSingleSizeLimit" :w-thumb="webUploaderMain.thumb" :w-form-data="webUploaderMain.formData" :ind="$index">
                            <button id="imageUploadMain" type="button" class="btn btn-sm btn-primary" >上传图片
                        </button>
                          </web-uploader>
                        </div>
                        <div  class="clearfix">
                          <div v-for="(ind,path) in sku.productImages" class="pull-left" style="margin-right:5px;position:relative;">
                            <button @click="deleteSkuImg(path.fullPath,index,ind)" style="font-size:18px;line-height:.5;position:absolute;top:1px;right:1px;"
                              type="button" class="close"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                            <img :src="path.fullPath" alt="图片" width="60px" height="60px">
                          </div>
                        </div>
                      </div>
                    </div>
                
  
                  </div>

                  </div>
                </div>
          </div>

                <div class="col-sm-12 m-t-lg">
                  <div class="col- -offset-2 col-sm-10">
                    <button  type="button" @click="closeWin" class="btn btn-default m-r-lg">取消</button>
                    <button :disabled="submiting" type="button" @click="saveProduct(false)" class="btn btn-primary m-r-lg">保存</button>
<!--                     <button :disabled="submiting" type="button" @click="saveProduct(true)" class="btn btn-primary m-r-lg">提交审批</button> -->
                  </div>
                </div>
              </form>
            </validator>
          </div>
        </div>
      </div>
    </div>
    <!-- ibox end -->
  </div>

  <div id="modalBrand" class="modal fade" tabindex="-1" data-width="600" v-cloak>
    <div class="modal-header">
      <h3 align="center">{{type ? '选择品牌' : '选择申请批次号'}}</h3>
    </div>
    <div class="modal-body">
      <div class="row" v-if="type">
        <div class="col-md-6">
          <div class="form-group">
            <input v-model="form.keyword" type="text" placeholder="品牌编码|品牌名称" class="form-control" />
          </div>
        </div>

        <div class="col-md-2">
          <div class="form-group">
            <button id="searchBtn" @click="query" type="button" class="btn btn-block btn-outline btn-default" alt="搜索" title="搜索">
            <i class="fa fa-search"></i>
          </button>
          </div>
        </div>
      </div>
      <div class="row" v-if="!type">
        <form id="searchForm" @submit.prevent="query">
          <div class="col-md-5">
            <div class="form-group">
              <input
                  v-model="form.declareCode"
                  type="text"
                  placeholder="申请批次号" class="form-control"/>
            </div>
          </div>
          
          <div class="col-md-5">
            <div class="form-group">
              <select v-model="form.orgId" placeholder="提报机构" class="form-control">
                <option value="" selected>--选择提报机构--</option>
                <option v-for="org in orgnizations" v-bind:value="org.id">{{org.name}}</option>
              </select>
            </div>
          </div>
          
          <div class="col-md-5">
            <div class="form-group input-group">
            	<input id="declareStartDate" v-model="form.declareStartDate" type="text" 
                  placeholder="--起始提报日期--" class="datepicker form-control" readonly/>
            </div>
          </div>
          
          <div class="col-md-5">
            <div class="form-group input-group">
            	<input id="declareEndDate" v-model="form.declareEndDate" type="text"
                  placeholder="--截止提报日期--" class="datepicker form-control" readonly/>
            </div>
          </div>
          <div class="col-md-2">
            <div class="form-group">
              <button id="searchBtn" type="submit" class="btn btn-block btn-outline btn-default" alt="搜索"
                      title="搜索">
                <i class="fa fa-search"></i>
              </button>
            </div>
          </div>

        </form>
      </div>
      <table id="dataTableBrand" width="100%" class="table table-striped table-bordered table-hover" />
      </table>
    </div>
    <div class="modal-footer">
      <button type="button" data-dismiss="modal" class="btn">关闭</button>
      <button type="button" @click="choose" class="btn btn-primary">选择</button>
    </div>
  </div>



  <div id="priceModal" class="modal fade" tabindex="-1" data-width="600" v-cloak>
    <div class="modal-header">
      <h3 align="center">设置价格</h3>
    </div>
    <div class="modal-body">
      <div class="row">
        <div class="col-md-12">
            <div class="col-md-4 tab" @click="tab('aConfig')" :class="{active:current == 'aConfig' ? true:false}" v-if="aShow == 1">供货价</div>
            <div class="col-md-4 tab" @click="tab('bConfig')" :class="{active:current == 'bConfig' ? true:false}" v-if="bShow == 1">门店价</div>
            <div class="col-md-4 tab" @click="tab('cConfig')" :class="{active:current == 'cConfig' ? true:false}" v-if="cShow == 1">销售价</div>
        </div>

      </div>
      <div v-show="current == 'aConfig'" class="col-md-12 ">
        <div class="search">
            <div  class="btn btn-primary fr" @click="createBtnClickHandler('SUPPLY')" style="margin:10px 0" v-if="aControl == 1">新增</div>
        </div>
        <table id="aTable" width="100%" class="table table-striped table-bordered table-hover">
        </table>
      </div>

       <div v-show="current == 'bConfig'" class="col-md-12">
          <div class="search">
            <div  class="btn btn-primary fr" @click="createBtnClickHandler('STORE')" style="margin:10px 0" v-if="bControl == 1">新增</div>
          </div>
          <table id="bTable" width="100%" class="table table-striped table-bordered table-hover">
          </table>
        </div> 

        <div v-show="current == 'cConfig'" class="col-md-12">
          <div class="search">
            <div  class="btn btn-primary fr" @click="createBtnClickHandler('SALE')" style="margin:10px 0" v-if="cControl == 1">新增</div>
          </div>
          <table id="cTable" width="100%" class="table table-striped table-bordered table-hover">
          </table>
        </div>  

    </div>
    <div class="modal-footer">
      <button type="button" data-dismiss="modal" class="btn">关闭</button>
    </div>
  </div>


  <div id="contractModal" class="modal modal-dialog fade" tabindex="1"  data-width="600">
  <validator name="validation">
    <form name="banner" novalidate class="form-horizontal" role="form">
      <input type="hidden" v-model="skuId">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3  align="center">新增/编辑{{type}}</h3>
      </div>
      
      <div class="modal-body">
        
        <div class="form-group" :class="{'has-error':$validation.time.invalid && $validation.touched}">
          <div class="col-sm-12">
            <label  class="control-label col-sm-4"><span style="color: red">*</span>时间</label>
            <div class="col-sm-8">
              <input id="timepick"
              v-model="priceStartDate" 
              type="text"
              v-validate:time="{
              required:{rule:true,message:'请输入时间'},
              }"
              placeholder="时间" class="datepicker form-control" readonly/>
              <span v-cloak v-if="$validation.time.invalid && $validation.touched" class="help-absolute">
                请输入时间
              </span>
            </div>
          </div>
        </div>
  
        <div class="form-group" :class="{'has-error':$validation.price.invalid && $validation.touched}">
          <div class="col-sm-12">
            <label for="mobile" class="control-label col-sm-4"><span style="color: red">*</span>价格</label>
            <div class="col-sm-8">
            <input 
              v-validate:price="{
              required:{rule:true,message:'请输入价格'},
              }"
              v-model="price" type="number" placeholder="价格" class="form-control" min="0">
            <span v-cloak v-if="$validation.price.invalid && $validation.touched" class="help-absolute">
              请输入价格
            </span>
            </div>
          </div>
        </div>
	
        </div> 

        <div class="modal-footer">
          <button type="button" data-dismiss="modal" class="btn">关闭</button>
          <button type="button" @click="savePrice" class="btn btn-primary">保存</button>
        </div>
      </div>
    </form>
  </validator>
</div>


  <style>
    .border{
      border: 1px solid #ccc;
      padding: 10px;
    }
    .margin20{
      margin-bottom: 20px;
    }
    .margin10{
      margin-bottom: 10px;
    }
    .sku input,.sku label{
      padding: 6px 12px;
    }

    .tab{
      border: 1px solid #f3f3f4;
      text-align: center;
      font-size: 14px;
      line-height: 40px;
      cursor: pointer;
    }
    .tab:hover{
      background-color: #1ab394;
    }
    .tab.active{
      background-color: #1ab394;
    }
  </style>
  <script src="/static/admin/vendor/ueditor/ueditor.config.js?v=ea38b0f7"></script>
  <script src="/static/admin/vendor/ueditor/ueditor.all.js?v=ea38b0f7"></script>
  <script src="${ctx}/static/admin/vendor/hogan/hogan.min.js?v=ea38b0f7"></script>
  <script src="${ctx}/static/admin/js/components/jquery.form.min.js?v=ea38b0f7"></script>
  <script src="${ctx}/static/admin/js/components/ImportExcelUtil.js?v=ea38b0f7"></script>
  <script src="/static/admin/vendor/webuploader/webuploader.js?v=ea38b0f7"></script>
  <script src="/static/admin/js/components/webuploader.js?v=ea38b0f7"></script>
  <script src="/static/admin/js/apps/prod/product/edit.js?v=ea38b0f7"></script>