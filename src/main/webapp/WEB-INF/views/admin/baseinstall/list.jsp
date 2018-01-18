<%@ page contentType="text/html;charset=UTF-8" %>
<style>
    [v-cloak] {
        display: none;
    }
</style>
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
                                    v-model="form.budgetNo"
                                    type="text"
                                    placeholder="预算号" class="form-control"/>
                        </div>
                    </div>
                    <div class="col-md-2">
                        <div class="form-group">
                            <input
                                    v-model="form.customerName"
                                    type="text"
                                    placeholder="客户姓名" class="form-control"/>
                        </div>
                    </div>
                    <div class="col-md-2">
                        <div class="form-group">
                            <input
                                    v-model="form.customerPhone"
                                    type="text"
                                    placeholder="客户电话" class="form-control"/>
                        </div>
                    </div>
                    <%--<div class="col-md-2">--%>
                        <%--<div class="form-group">--%>
                            <%--<select v-model="form.contractStatus" class="form-control">--%>
                                <%--<option value="">请选择状态</option>--%>
                                <%--<option value="NOTCHECKED">合同未核对</option>--%>
                                <%--<option value="CHECKED">合同已核对</option>--%>
                                <%--<option value="COMPLETED">已完成</option>--%>
                            <%--</select>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <div class="col-md-1">
                        <div class="form-group">
                            <button id="searchBtn" type="submit" class="btn btn-block btn-outline btn-default" alt="搜索"
                                    title="搜索">
                                <i class="fa fa-search"></i>
                            </button>
                        </div>
                    </div>


                    <%--<div class="col-md-1">
                        <div class="form-group">
                            <button @click="createBtnClickHandler" type="button"
                                    class="btn btn-block btn-outline btn-primary">新增
                            </button>
                        </div>
                    </div>
                    <div class="col-md-1">
                        <div class="form-group">
                            <button @click="importBtnClickHandler" type="button"
                                    class="btn btn-block btn-outline btn-primary">导入
                            </button>
                        </div>
                    </div>--%>
                </form>
            </div>
            <table id="dataTable" width="100%" class="table table-striped table-bordered table-hover">
            </table>
        </div>
    </div>
    <!-- ibox end -->
</div>


<div id="modal" class="modal fade" tabindex="-1" data-width="760" v-cloak>
    <validator name="validation">
        <form name="user" novalidate class="form-horizontal" role="form">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3 v-if="contractStatus != 'COMPLETED'" style="text-align: center">客户合同录入</h3>
                <h3 v-else  style="text-align: center">客户合同核查</h3>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <label class="control-label col-sm-3"></span>预算号（原始）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractBaseInfo.budgetNo" type="text" placeholder="预算号" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'">
                    <label class="control-label col-sm-3"></span>预算号（录入）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractInputInfo.budgetNo" type="text" placeholder="预算号" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus != 'COMPLETED'" :class="{'has-error':$validation.budgetno.invalid && $validation.touched}">
                    <label  for="budgetno" class="control-label col-sm-3"><span style="color: red">*</span>预算号（录入）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractInputInfo.budgetNo"
                               v-validate:budgetno="{maxlength:{rule:30,message:'预算号最长不能超过30个字符'}}"
                               data-tabindex="1"
                               type="text" placeholder="预算号" class="form-control">
                        <span v-cloak v-if="$validation.budgetno.invalid && $validation.touched" class="help-absolute">
                            <span v-for="error in $validation.budgetno.errors">
                                {{error.message}} {{($index !== ($validation.budgetno.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'" :class="{'has-error':$validation.budgetno.invalid && $validation.touched}">
                    <label  for="budgetno" class="control-label col-sm-3"><span style="color: red">*</span>预算号（核查）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractCheckInfo.budgetNo"
                               v-validate:budgetno="{ maxlength:{rule:30,message:'预算号最长不能超过30个字符'}}"
                               data-tabindex="1"
                               type="text" placeholder="预算号" class="form-control">
                        <span v-cloak v-if="$validation.budgetno.invalid && $validation.touched" class="help-absolute">
                            <span v-for="error in $validation.budgetno.errors">
                                {{error.message}} {{($index !== ($validation.budgetno.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>

                <div class="form-group">
                    <label class="control-label col-sm-3"></span>项目编号（原始）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractBaseInfo.projectCode" type="text" placeholder="项目编号" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'">
                    <label class="control-label col-sm-3"></span>项目编号（录入）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractInputInfo.projectCode" type="text" placeholder="项目编号" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus != 'COMPLETED'" :class="{'has-error':$validation.projectcode.invalid && $validation.touched}">
                    <label for="projectcode" class="control-label col-sm-3"><span style="color: red">*</span>项目编号（录入）：</label>
                    <div class="col-sm-9">
                        <input v-model="contractInputInfo.projectCode"
                               v-validate:projectcode="{maxlength:{rule:30,message:'项目编号最长不能超过30个字符'}}"
                               data-tabindex="2"
                               type="text" placeholder="项目编号" class="form-control">
                        <span v-cloak v-if="$validation.projectcode.invalid && $validation.touched" class="help-absolute">
                            <span v-for="error in $validation.projectcode.errors">
                                {{error.message}} {{($index !== ($validation.projectcode.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'" :class="{'has-error':$validation.projectcode.invalid && $validation.touched}">
                    <label for="projectcode" class="control-label col-sm-3"><span style="color: red">*</span>项目编号（核查）：</label>
                    <div class="col-sm-9">
                        <input v-model="contractCheckInfo.projectCode"
                               v-validate:projectcode="{maxlength:{rule:30,message:'项目编号最长不能超过30个字符'}}"
                               data-tabindex="2"
                               type="text" placeholder="项目编号" class="form-control">
                        <span v-cloak v-if="$validation.projectcode.invalid && $validation.touched" class="help-absolute">
                            <span v-for="error in $validation.projectcode.errors">
                                {{error.message}} {{($index !== ($validation.projectcode.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>

                <div class="form-group">
                    <label class="control-label col-sm-3"></span>客户姓名（原始）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractBaseInfo.customerName" type="text" placeholder="客户姓名" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'">
                    <label class="control-label col-sm-3"></span>客户姓名（录入）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractInputInfo.customerName" type="text" placeholder="客户姓名" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus != 'COMPLETED'" :class="{'has-error':$validation.customername.invalid && $validation.touched}">
                    <label for="customername" class="control-label col-sm-3"><span style="color: red">*</span>客户姓名（录入）：</label>
                    <div class="col-sm-9">
                        <input v-model="contractInputInfo.customerName"
                               v-validate:customername="{maxlength:{rule:30,message:'客户姓名最长不能超过30个字符'}}"
                               data-tabindex="3"
                               type="text" placeholder="客户姓名" class="form-control">
                        <span v-cloak v-if="$validation.customername.invalid && $validation.touched" class="help-absolute">
                            <span v-for="error in $validation.customername.errors">
                                {{error.message}} {{($index !== ($validation.customername.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'" :class="{'has-error':$validation.customername.invalid && $validation.touched}">
                    <label for="customername" class="control-label col-sm-3"><span style="color: red">*</span>客户姓名（核查）：</label>
                    <div class="col-sm-9">
                        <input v-model="contractCheckInfo.customerName"
                               v-validate:customername="{maxlength:{rule:30,message:'客户姓名最长不能超过30个字符'}}"
                               data-tabindex="3"
                               type="text" placeholder="客户姓名" class="form-control">
                        <span v-cloak v-if="$validation.customername.invalid && $validation.touched" class="help-absolute">
                            <span v-for="error in $validation.customername.errors">
                                {{error.message}} {{($index !== ($validation.customername.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>

                <div class="form-group">
                    <label class="control-label col-sm-3"></span>客户电话（原始）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractBaseInfo.customerPhone" type="text" placeholder="客户电话" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'">
                    <label class="control-label col-sm-3"></span>客户电话（录入）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractInputInfo.customerPhone" type="text" placeholder="客户电话" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus != 'COMPLETED'" :class="{'has-error':$validation.customerphone.invalid && $validation.touched}">
                    <label for="customerphone" class="control-label col-sm-3"><span style="color: red">*</span>客户电话（录入）：</label>
                    <div class="col-sm-9">
                        <input v-model="contractInputInfo.customerPhone"
                               v-validate:customerphone="{mobile:{rule:true,message:'请输入合法的手机号'}}"
                               data-tabindex="4"
                               type="text" placeholder="客户电话" class="form-control">
                        <span v-cloak v-if="$validation.customerphone.invalid && $validation.touched" class="help-absolute">
                            <span v-for="error in $validation.customerphone.errors">
                                {{error.message}} {{($index !== ($validation.customerphone.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'" :class="{'has-error':$validation.customerphone.invalid && $validation.touched}">
                    <label for="customerphone" class="control-label col-sm-3"><span style="color: red">*</span>客户电话（核查）：</label>
                    <div class="col-sm-9">
                        <input v-model="contractCheckInfo.customerPhone"
                               v-validate:customerphone="{mobile:{rule:true,message:'请输入合法的手机号'}}"
                               data-tabindex="4"
                               type="text" placeholder="客户电话" class="form-control">
                        <span v-cloak v-if="$validation.customerphone.invalid && $validation.touched" class="help-absolute">
                            <span v-for="error in $validation.customerphone.errors">
                                {{error.message}} {{($index !== ($validation.customerphone.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>

                <div class="form-group">
                    <label class="control-label col-sm-3"></span>客户装修地址（原始）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractBaseInfo.projectAddress" type="text" placeholder="客户装修地址" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'">
                    <label class="control-label col-sm-3"></span>客户装修地址（录入）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractInputInfo.projectAddress" type="text" placeholder="客户装修地址" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus != 'COMPLETED'" :class="{'has-error':$validation.projectaddress.invalid && $validation.touched}">
                    <label for="projectaddress" class="control-label col-sm-3"><span style="color: red">*</span>客户装修地址（录入）：</label>
                    <div class="col-sm-9">
                        <input
                                v-model="contractInputInfo.projectAddress"
                                v-validate:projectaddress="{maxlength:{rule:100,message:'客户装修地址最长不能超过100个字符'}}"
                                data-tabindex="5"
                                type="text" placeholder="客户装修地址" class="form-control">
                        <span v-cloak v-if="$validation.projectaddress.invalid && $validation.touched" class="help-absolute">
                            <span v-for="error in $validation.projectaddress.errors">
                                {{error.message}} {{($index !== ($validation.projectaddress.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'" :class="{'has-error':$validation.projectaddress.invalid && $validation.touched}">
                    <label for="projectaddress" class="control-label col-sm-3"><span style="color: red">*</span>客户装修地址（核查）：</label>
                    <div class="col-sm-9">
                        <input
                                v-model="contractCheckInfo.projectAddress"
                                v-validate:projectaddress="{maxlength:{rule:100,message:'客户装修地址最长不能超过100个字符'}}"
                                data-tabindex="5"
                                type="text" placeholder="客户装修地址" class="form-control">
                        <span v-cloak v-if="$validation.projectaddress.invalid && $validation.touched" class="help-absolute">
                            <span v-for="error in $validation.projectaddress.errors">
                                {{error.message}} {{($index !== ($validation.projectaddress.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>

                <div class="form-group">
                    <label class="control-label col-sm-3"></span>套餐（原始）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractBaseInfo.meal" type="text" placeholder="套餐" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'">
                    <label class="control-label col-sm-3"></span>套餐（录入）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractInputInfo.meal" type="text" placeholder="套餐" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus != 'COMPLETED'">
                    <label class="control-label col-sm-3">套餐（录入）：</label>
                    <div class="col-sm-9">
                        <input v-model="contractInputInfo.meal"
                                data-tabindex="6"
                                type="text" placeholder="套餐" class="form-control">
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'">
                    <label class="control-label col-sm-3">套餐（核查）：</label>
                    <div class="col-sm-9">
                        <input v-model="contractCheckInfo.meal"
                               data-tabindex="6"
                               type="text" placeholder="套餐" class="form-control">
                    </div>
                </div>

                <div class="form-group">
                    <label class="control-label col-sm-3"></span>建筑面积（原始）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractBaseInfo.budgetArea" type="text" placeholder="建筑面积" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'">
                    <label class="control-label col-sm-3"></span>建筑面积（录入）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractInputInfo.budgetArea" type="text" placeholder="建筑面积" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus != 'COMPLETED'" :class="{'has-error':$validation.budgetarea.invalid && $validation.touched}">
                    <label class="control-label col-sm-3"><span style="color: red">*</span>建筑面积（录入）：</label>
                    <div class="col-sm-9">
                        <input v-model="contractInputInfo.budgetArea"
                            v-validate:budgetarea="{positiveNum:{rule:true,message:'建筑面积必须是数字'}}"
                            data-tabindex="7"
                            type="text"
                            placeholder="建筑面积" class="form-control">
                        <span v-cloak v-if="$validation.budgetarea.invalid && $validation.touched" class="help-absolute">
                            <span v-for="error in $validation.budgetarea.errors">
                                {{error.message}} {{($index !== ($validation.budgetarea.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'" :class="{'has-error':$validation.budgetarea.invalid && $validation.touched}">
                    <label class="control-label col-sm-3"><span style="color: red">*</span>建筑面积（核查）：</label>
                    <div class="col-sm-9">
                        <input v-model="contractCheckInfo.budgetArea"
                               v-validate:budgetarea="{positiveNum:{rule:true,message:'建筑面积必须是数字'}}"
                               data-tabindex="7"
                               type="text"
                               placeholder="建筑面积" class="form-control">
                        <span v-cloak v-if="$validation.budgetarea.invalid && $validation.touched" class="help-absolute">
                            <span v-for="error in $validation.budgetarea.errors">
                                {{error.message}} {{($index !== ($validation.budgetarea.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>

                <div class="form-group">
                    <label class="control-label col-sm-3"></span>房型（原始）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractBaseInfo.houseLayout" type="text" placeholder="房型" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'">
                    <label class="control-label col-sm-3"></span>房型（录入）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractInputInfo.houseLayout" type="text" placeholder="房型" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus != 'COMPLETED'">
                    <label class="control-label col-sm-3">房型（录入）：</label>
                    <div class="col-sm-9">
                        <input v-model="contractInputInfo.houseLayout"
                               data-tabindex="8"
                               type="text" placeholder="房型" class="form-control">
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'">
                    <label class="control-label col-sm-3">房型（核查）：</label>
                    <div class="col-sm-9">
                        <input v-model="contractCheckInfo.houseLayout"
                               data-tabindex="8"
                               type="text" placeholder="房型" class="form-control">
                    </div>
                </div>

                <div class="form-group">
                    <label class="control-label col-sm-3"></span>设计师（原始）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractBaseInfo.designerName" type="text" placeholder="设计师" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'">
                    <label class="control-label col-sm-3"></span>设计师（录入）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractInputInfo.designerName" type="text" placeholder="设计师" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus != 'COMPLETED'">
                    <label for="designerName" class="control-label col-sm-3">设计师（录入）：</label>
                    <div class="col-sm-9">
                        <input
                            v-model="contractInputInfo.designerName"
                            data-tabindex="9"
                            type="text" placeholder="设计师" class="form-control">
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'">
                    <label for="designerName" class="control-label col-sm-3">设计师（核查）：</label>
                    <div class="col-sm-9">
                        <input
                            v-model="contractCheckInfo.designerName"
                            data-tabindex="9"
                            type="text" placeholder="设计师" class="form-control">
                    </div>
                </div>

                <div class="form-group">
                    <label class="control-label col-sm-3"></span>设计师手机号（原始）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractBaseInfo.designerPhone" type="text" placeholder="设计师手机号" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'">
                    <label class="control-label col-sm-3"></span>设计师手机号（录入）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractInputInfo.designerPhone" type="text" placeholder="设计师手机号" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus != 'COMPLETED'" :class="{'has-error':$validation.designerphone.invalid && $validation.touched}">
                    <label for="designerphone" class="control-label col-sm-3">设计师手机号（录入）：</label>
                    <div class="col-sm-9">
                        <input
                            v-model="contractInputInfo.designerPhone"
                            v-validate:designerphone="{
                                mobile:{rule:true,message:'请输入合法的手机号'}
                            }"
                            data-tabindex="10"
                            type="text" placeholder="设计师手机号" class="form-control">
                        <span v-cloak v-if="$validation.designerphone.invalid && $validation.touched" class="help-absolute">
                            <span v-for="error in $validation.designerphone.errors">
                                {{error.message}} {{($index !== ($validation.designerphone.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'" :class="{'has-error':$validation.designerphone.invalid && $validation.touched}">
                    <label for="designerphone" class="control-label col-sm-3">设计师手机号（核查）：</label>
                    <div class="col-sm-9">
                        <input
                                v-model="contractCheckInfo.designerPhone"
                                v-validate:designerphone="{
                                mobile:{rule:true,message:'请输入合法的手机号'}
                            }"
                                data-tabindex="10"
                                type="text" placeholder="设计师手机号" class="form-control">
                        <span v-cloak v-if="$validation.designerphone.invalid && $validation.touched" class="help-absolute">
                            <span v-for="error in $validation.designerphone.errors">
                                {{error.message}} {{($index !== ($validation.designerphone.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>

                <div class="form-group">
                    <label class="control-label col-sm-3"></span>工程造价（原始）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractBaseInfo.engineeringCost" type="text" placeholder="工程造价" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'">
                    <label class="control-label col-sm-3"></span>工程造价（录入）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractInputInfo.engineeringCost" type="text" placeholder="工程造价" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus != 'COMPLETED'" :class="{'has-error':$validation.engineeringcost.invalid && $validation.touched}">
                    <label class="control-label col-sm-3"><span style="color: red">*</span>工程造价（录入）：</label>
                    <div class="col-sm-9">
                        <input
                                v-model="contractInputInfo.engineeringCost"
                                v-validate:engineeringcost="{numeric:{rule:true,message:'工程造价必须是数字'}}"
                                data-tabindex="11"
                                type="text" placeholder="工程造价" class="form-control">
                        <span v-cloak v-if="$validation.engineeringcost.invalid && $validation.touched" class="help-absolute">
                            <span v-for="error in $validation.engineeringcost.errors">
                                {{error.message}} {{($index !== ($validation.engineeringcost.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'" :class="{'has-error':$validation.engineeringcost.invalid && $validation.touched}">
                    <label class="control-label col-sm-3"><span style="color: red">*</span>工程造价（核查）：</label>
                    <div class="col-sm-9">
                        <input
                            v-model="contractCheckInfo.engineeringCost"
                            v-validate:engineeringcost="{numeric:{rule:true,message:'工程造价必须是数字'}}"
                            data-tabindex="11"
                            type="text" placeholder="工程造价" class="form-control">
                        <span v-cloak v-if="$validation.engineeringcost.invalid && $validation.touched" class="help-absolute">
                            <span v-for="error in $validation.engineeringcost.errors">
                                {{error.message}} {{($index !== ($validation.engineeringcost.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>

                <div class="form-group">
                    <label class="control-label col-sm-3"></span>旧房拆改费（原始）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractBaseInfo.dismantleFee" type="text" placeholder="旧房拆改费" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'">
                    <label class="control-label col-sm-3"></span>旧房拆改费（录入）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractInputInfo.dismantleFee" type="text" placeholder="旧房拆改费" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus != 'COMPLETED'" :class="{'has-error':$validation.dismantlefee.invalid && $validation.touched}">
                    <label class="control-label col-sm-3"><span style="color: red">*</span>旧房拆改费（录入）：</label>
                    <div class="col-sm-9">
                        <input
                            v-model="contractInputInfo.dismantleFee"
                            v-validate:dismantlefee="{numeric:{rule:true,message:'旧房拆改费必须是数字'}}"
                            data-tabindex="12"
                            type="text" placeholder="旧房拆改费" class="form-control">
                        <span v-cloak v-if="$validation.dismantlefee.invalid && $validation.touched" class="help-absolute">
                            <span v-for="error in $validation.dismantlefee.errors">
                                {{error.message}} {{($index !== ($validation.dismantlefee.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'" :class="{'has-error':$validation.dismantlefee.invalid && $validation.touched}">
                    <label class="control-label col-sm-3"><span style="color: red">*</span>旧房拆改费（核查）：</label>
                    <div class="col-sm-9">
                        <input
                                v-model="contractCheckInfo.dismantleFee"
                                v-validate:dismantlefee="{numeric:{rule:true,message:'旧房拆改费必须是数字'}}"
                                data-tabindex="12"
                                type="text" placeholder="旧房拆改费" class="form-control">
                        <span v-cloak v-if="$validation.dismantlefee.invalid && $validation.touched" class="help-absolute">
                            <span v-for="error in $validation.dismantlefee.errors">
                                {{error.message}} {{($index !== ($validation.dismantlefee.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>

                <%--<div class="form-group">--%>
                    <%--<label class="control-label col-sm-3"></span>变更费（原始）：</label>--%>
                    <%--<div class="col-sm-9" >--%>
                        <%--<input v-model="contractBaseInfo.changeFee" type="text" placeholder="变更费" class="form-control" readonly>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <%--<div class="form-group" v-if="contractStatus == 'COMPLETED'">--%>
                    <%--<label class="control-label col-sm-3"></span>变更费（录入）：</label>--%>
                    <%--<div class="col-sm-9" >--%>
                        <%--<input v-model="contractInputInfo.changeFee" type="text" placeholder="变更费" class="form-control" readonly>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <%--<div class="form-group" v-if="contractStatus != 'COMPLETED'" :class="{'has-error':$validation.changefee.invalid && $validation.touched}">--%>
                    <%--<label class="control-label col-sm-3"><span style="color: red">*</span>变更费（录入）：</label>--%>
                    <%--<div class="col-sm-9">--%>
                        <%--<input--%>
                                <%--v-model="contractInputInfo.changeFee"--%>
                                <%--v-validate:changefee="{numeric:{rule:true,message:'变更费必须是数字'}}"--%>
                                <%--data-tabindex="13"--%>
                                <%--type="text" placeholder="变更费" class="form-control">--%>
                        <%--<span v-cloak v-if="$validation.changefee.invalid && $validation.touched" class="help-absolute">--%>
                            <%--<span v-for="error in $validation.changefee.errors">--%>
                                <%--{{error.message}} {{($index !== ($validation.changefee.errors.length -1)) ? ',':''}}--%>
                            <%--</span>--%>
                        <%--</span>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <%--<div class="form-group" v-if="contractStatus == 'COMPLETED'" :class="{'has-error':$validation.changefee.invalid && $validation.touched}">--%>
                    <%--<label class="control-label col-sm-3"><span style="color: red">*</span>变更费（核查）：</label>--%>
                    <%--<div class="col-sm-9">--%>
                        <%--<input--%>
                                <%--v-model="contractCheckInfo.changeFee"--%>
                                <%--v-validate:changefee="{numeric:{rule:true,message:'变更费必须是数字'}}"--%>
                                <%--data-tabindex="13"--%>
                                <%--type="text" placeholder="变更费" class="form-control">--%>
                        <%--<span v-cloak v-if="$validation.changefee.invalid && $validation.touched" class="help-absolute">--%>
                            <%--<span v-for="error in $validation.changefee.errors">--%>
                                <%--{{error.message}} {{($index !== ($validation.changefee.errors.length -1)) ? ',':''}}--%>
                            <%--</span>--%>
                        <%--</span>--%>
                    <%--</div>--%>
                <%--</div>--%>

                <div class="form-group">
                    <label class="control-label col-sm-3"></span>合同时间（原始）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractBaseInfo.contractSignDate" type="text" placeholder="合同时间" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'">
                    <label class="control-label col-sm-3"></span>合同时间（录入）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractInputInfo.contractSignDate" type="text" placeholder="合同时间" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus != 'COMPLETED'" :class="{'has-error':$validation.contractsigndate.invalid && $validation.touched}">
                    <label class="control-label col-sm-3"><span style="color: red">*</span>合同时间（录入）：</label>
                    <div class="col-sm-9">
                        <input v-model="contractInputInfo.contractSignDate" v-el:contract-sign-date id="contractSignDate"
                               name="contractSignDate" type="text" data-tabindex="14" readonly
                               class="form-control datepicker" placeholder="请选择开始时间">
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'" :class="{'has-error':$validation.contractsigndate.invalid && $validation.touched}">
                    <label class="control-label col-sm-3"><span style="color: red">*</span>合同时间（核查）：</label>
                    <div class="col-sm-9">
                        <input v-model="contractCheckInfo.contractSignDate" v-el:contract-sign-date id="contractSignDate"
                               name="contractSignDate" type="text" data-tabindex="14" readonly
                               class="form-control datepicker" placeholder="请选择开始时间">
                    </div>
                </div>

                <div class="form-group">
                    <label class="control-label col-sm-3"></span>是否有电梯（原始）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractBaseInfo.haveElevator | whether-convert" type="text" placeholder="是否有电梯" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'">
                    <label class="control-label col-sm-3"></span>是否有电梯（录入）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractInputInfo.haveElevator | whether-convert" type="text" placeholder="是否有电梯" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus != 'COMPLETED'">
                    <label class="control-label col-sm-3">是否有电梯（录入）：</label>
                    <div class="col-sm-6" style="padding-top: 5px">
                        <div class="col-sm-3">
                            <input type="radio" id="otherTrue" :value="1"
                                   v-model="contractInputInfo.haveElevator" data-tabindex="15">
                            <label for="otherTrue">是</label>
                        </div>
                        <div class="col-sm-3">
                            <input type="radio" id="otherFalse" :value="0"
                                   v-model="contractInputInfo.haveElevator" data-tabindex="16">
                            <label for="otherFalse">否</label>
                        </div>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'">
                    <label class="control-label col-sm-3">是否有电梯（核查）：</label>
                    <div class="col-sm-6" style="padding-top: 5px">
                        <div class="col-sm-3">
                            <input type="radio" id="otherTrue" :value="1"
                                   v-model="contractCheckInfo.haveElevator" data-tabindex="15">
                            <label for="otherTrue">是</label>
                        </div>
                        <div class="col-sm-3">
                            <input type="radio" id="otherFalse" :value="0"
                                   v-model="contractCheckInfo.haveElevator" data-tabindex="16">
                            <label for="otherFalse">否</label>
                        </div>
                    </div>
                </div>

                <%--<div class="form-group">
                    <label class="control-label col-sm-3"></span>备注（原始）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractBaseInfo.remarks" type="text" placeholder="备注" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'">
                    <label class="control-label col-sm-3"></span>备注（录入）：</label>
                    <div class="col-sm-9" >
                        <input v-model="contractInputInfo.remarks" type="text" placeholder="备注" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus != 'COMPLETED'">
                    <label for="remarks" class="control-label col-sm-3">备注（录入）：</label>
                    <div class="col-sm-9">
                        <input
                            v-model="contractInputInfo.remarks"
                            data-tabindex="17"
                            type="text"
                            placeholder="备注" class="form-control">
                    </div>
                </div>
                <div class="form-group" v-if="contractStatus == 'COMPLETED'">
                    <label for="remarks" class="control-label col-sm-3">备注（核查）：</label>
                    <div class="col-sm-9">
                        <input
                                v-model="contractCheckInfo.remarks"
                                data-tabindex="17"
                                type="text"
                                placeholder="备注" class="form-control">
                    </div>
                </div>--%>
            </div>
            <div class="modal-footer">
                <button :disabled="submitting" type="button" @click="save" class="btn btn-primary">保存客户信息</button>
            </div>
        </form>
    </validator>
</div>

<div id="completeModal" class="modal fade" tabindex="-1" data-width="700">
    <validator name="validation">
        <form name="banner" novalidate class="form-horizontal" role="form">
            <input type="hidden" v-model="id">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×
                </button>
                <h3 align="center">填写竣工时间</h3>
            </div>

            <div class="modal-body" style="height:300px;">
                <div class="form-group">
                    <label class="control-label col-sm-3">竣工时间：</label>
                    <div class="col-sm-9">
                        <input v-model="completeDate" v-el:complete-date id="completeDate"
                               v-validate:time="{required:{rule:true,message:'请输入竣工时间'},}"
                               name="c" type="text" readonly
                               class="form-control datepicker" placeholder="请选择竣工时间">
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

<!--导入 -->
<div id="import" class="modal fade" tabindex="-1" data-width="600">
    <validator name="validation">
        <form name="banner" novalidate class="form-horizontal" role="form">
            <input type="hidden" v-model="id">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3 align="center">批量导入选材合同</h3>
            </div>

            <div class="modal-body">
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
                    <label for="name" class="control-label col-sm-3">客户合同模版:</label>
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

            </div>

            <div class="modal-footer">
                <button type="button" data-dismiss="modal" class="btn">关闭</button>
            </div>
        </form>
    </validator>
</div>
<script src="/static/admin/vendor/webuploader/webuploader.js?v=ea38b0f7"></script>
<script src="/static/admin/js/components/webuploader.js?v=ea38b0f7"></script>
<script src="/static/admin/js/apps/baseinstall/list.js?v=ea38b0f7"></script>