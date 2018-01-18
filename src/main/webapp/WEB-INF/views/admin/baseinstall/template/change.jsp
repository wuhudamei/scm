<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<template id="changemain">
    <div>
        <div class="main" v-show="flag">
            <validator name="validation">
                <form class="form-inline">
                    <div class="row">
                        <div class="form-group base-form">
                            <label for="changeDate">变更日期：</label>
                            <input id="changeDate" class="form-control datepicker" v-el:change-date readonly type="text"
                                   v-model="baseMain.changeDate"
                                   v-validate:change-date="{required:true}"
                                   :class="{'error': !$validation.changeDate.valid}">
                        </div>
                        <div class="form-group base-form base-form-w30">
                            <label for="exampleInputEmail2">变更单序号：</label>
                            <input type="email" class="form-control base-form-w30" v-model="baseMain.changeOrderNumber"
                                   v-validate:change-order-number="{required:true}"
                                   :class="{'error': !$validation.changeOrderNumber.valid}">
                        </div>
                        <div class="form-group base-form base-form-w30">
                            <label for="exampleInputEmail2">变更描述：</label>
                            <input type="email" class="form-control base-form-w30" v-model="baseMain.describation"
                                   v-validate:describation="{required:true}"
                                   :class="{'error': !$validation.describation.valid}">
                        </div>
                        <div class="form-group base-form">
                            <button type="button" :disabled="submitting" @click="save" class="btn btn-primary m-r-lg">
                                保存
                            </button>
                        </div>
                    </div>
                </form>
            </validator>
            <div class="row text-right base-tip"></div>
            <table id="dataTable" width="100%" class="table table-striped table-bordered table-hover">
                <thead>
                <tr>
                    <th>日期</th>
                    <th>变更单序号</th>
                    <th>描述</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="item in list">
                    <td>{{item.changeDate | dateFormater-filter}}</td>
                    <td>{{item.changeOrderNumber}}</td>
                    <td>{{item.describation}}</td>
                    <td>
                        <a href="javascript:void(0);" @click="edit(item)">编辑</a>
                        <a href="javascript:void(0);"@click="detailList(item)">详情</a>
                        <a href="javascript:void(0);" @click="delete(item)">删除</a>
                    </td>
                </tr>

                </tbody>
            </table>
        </div>

        <div class="detail" v-show="detailFlag">
            <validator name="validation1">
                <form class="form-inline">
                    <div class="row">
                        <div class="form-group base-form" :class="{'error-border': !baseMainDetail.materialType}">
                            <label class="radio-inline">
                                <input type="radio" name="materialType" id="inlineRadio11" value="CHANGESUBJECTMATERIAL"
                                       v-model="baseMainDetail.materialType"> 主材变更
                            </label>
                            <label class="radio-inline">
                                <input type="radio" name="materialType" id="inlineRadio21" value="BASECHANGE"
                                       v-model="baseMainDetail.materialType"> 基装变更
                            </label>
                        </div>
                        <label for="projectName">项目名称：</label>
                        <input type="text" class="form-control" v-model="baseMainDetail.projectName"
                               v-validate:projectname="{required:true}"
                               :class="{'error': !$validation1.projectname.valid}">
                        <div class="form-group base-form">
                            <label for="location">位置：</label>
                            <input type="text" class="form-control" v-model="baseMainDetail.location">
                        </div>
                        <div class="form-group base-form">
                            <label for="brand">品牌：</label>
                            <input type="email" class="form-control" v-model="baseMainDetail.brand">
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group base-form">
                            <label for="specification">规格：</label>
                            <input type="email" class="form-control"
                                   v-model="baseMainDetail.specification">
                        </div>
                        <div class="form-group base-form">
                            <label for="model">型号：</label>
                            <input type="email" class="form-control" v-model="baseMainDetail.model">
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group base-form">
                            <label for="price">单价：</label>
                            <input type="email" class="form-control" v-model="baseMainDetail.price">
                        </div>
                        <div class="form-group base-form">
                            <label for="unit">单位：</label>
                            <input type="email" class="form-control" v-model="baseMainDetail.unit">
                        </div>
                        <div class="form-group base-form">
                            <label for="amount">数量：</label>
                            <input type="email" class="form-control" v-model="baseMainDetail.amount">
                        </div>
                        <div class="form-group base-form">
                            <label for="total">合计：</label>
                            <input type="email" class="form-control" v-model="baseMainDetail.total">
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group base-form">
                            <label for="wastageCost">损耗：</label>
                            <input type="email" class="form-control" v-model="baseMainDetail.wastageCost">
                        </div>
                        <div class="form-group base-form">
                            <label for="materialCost">材料费：</label>
                            <input type="email" class="form-control" v-model="baseMainDetail.materialCost">
                        </div>
                        <div class="form-group base-form">
                            <label for="laborCost">人工费：</label>
                            <input type="email" class="form-control" v-model="baseMainDetail.laborCost">
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group base-form">
                            <label for="holeHigh">洞口尺寸（高）：</label>
                            <input type="email" class="form-control" v-model="baseMainDetail.holeHigh">
                        </div>
                        <div class="form-group base-form">
                            <label for="holeWide">洞口尺寸（宽）：</label>
                            <input type="email" class="form-control" v-model="baseMainDetail.holeWide">
                        </div>
                        <div class="form-group base-form">
                            <label for="holeThuck">洞口尺寸（厚）：</label>
                            <input type="email" class="form-control" v-model="baseMainDetail.holeThuck">
                        </div>
                        <div class="form-group base-form">
                            <label for="addStack">加垛：</label>
                            <input type="email" class="form-control" v-model="baseMainDetail.addStack">
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group base-form">
                            <label for="description">工艺说明及材料说明：</label>
                            <input type="email" class="form-control" v-model="baseMainDetail.description">
                        </div>
                        <div class="form-group base-form" :class="{'error-border': !baseMainDetail.changeType}">
                            <label class="radio-inline">
                                <input type="radio" name="changeType" id="inlineRadio1" value="INCREASE"
                                       v-model="baseMainDetail.changeType"> 增项
                            </label>
                            <label class="radio-inline">
                                <input type="radio" name="changeType" id="inlineRadio2" value="MINUSITEM"
                                       v-model="baseMainDetail.changeType"> 减项
                            </label>
                            <label class="radio-inline">
                                <input type="radio" name="changeType" id="inlineRadio3" value="COMPENSATE"
                                       v-model="baseMainDetail.changeType"> 赔付
                            </label>
                        </div>
                        <div class="form-group base-form">
                            <button type="button" :disabled="submitting" @click="saveDetail"
                                    class="btn btn-primary m-r-lg">保存
                            </button>
                            <button type="button" @click="back" class="btn btn-primary m-r-lg">返回</button>
                        </div>
                    </div>
                </form>
            </validator>
            <div class="row text-right base-tip"></div>
            <table id="dataTable" width="100%" class="table table-striped table-bordered table-hover">
                <thead>
                <tr>
                    <th>类型</th>
                    <th>项目名称</th>
                    <th>位置</th>
                    <th>品牌</th>
                    <th>单位</th>
                    <th>数量</th>
                    <th>规格</th>
                    <th>型号</th>
                    <th>单价</th>
                    <th>合计</th>
                    <th>工艺说明及材料说明</th>
                    <th width="5%" style="text-align: center">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="item in list">
                    <td><p v-if="item.materialType=='CHANGESUBJECTMATERIAL'">主材</p>
                        <p v-if="item.materialType=='BASECHANGE'">基装</p>
                        <p v-if="item.changeType=='INCREASE'">增项</p>
                        <p v-if="item.changeType=='MINUSITEM'">减项</p>
                        <p v-if="item.changeType=='COMPENSATE'">赔付</p></td>
                    <td>{{item.projectName}}</td>
                    <td>{{item.location}}</td>
                    <td>{{item.brand}}</td>
                    <td>{{item.unit}}</td>
                    <td>{{item.amount}}</td>
                    <td>{{item.specification}}</td>
                    <td>{{item.model}}</td>
                    <td>{{item.price}}</td>
                    <td>{{item.total}}</td>
                    <td>{{item.description}}</td>
                    <td style="text-align: center">
                        <a href="javascript:void(0);" @click="editDetail(item)">编辑</a>
                        <a href="javascript:void(0);" @click="deleteDetail(item)">删除</a>
                    </td>
                </tr>

                </tbody>
            </table>
        </div>
    </div>

</template>
