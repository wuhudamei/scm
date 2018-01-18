<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<template id="basehome">
    <div>
        <validator name="validation">
            <form class="form-inline" >
                <div class="row">
                    <div class="form-group base-form">
                        <label for="projectName">项目名称：</label>
                        <input type="text" class="form-control" v-model="baseMain.projectName"
                        v-validate:project-name="{required:true}"
                        :class="{'error': !$validation.projectName.valid}">
                    </div>
                    <div class="form-group base-form">
                        <label for="price">项目单价：</label>
                        <input type="email" class="form-control"  v-model="baseMain.price">
                    </div>
                    <div class="form-group base-form">
                        <label for="unit">单位：</label>
                        <input type="email" class="form-control"  v-model="baseMain.unit"
                        v-validate:unit="{required:true}"
                        :class="{'error': !$validation.unit.valid}">
                    </div>
                    <div class="form-group base-form">
                        <label for="quantity">数量：</label>
                        <input type="email" class="form-control"  v-model="baseMain.quantity"
                        v-validate:quantity="{required:true}"
                        :class="{'error': !$validation.quantity.valid}">
                    </div>


                </div>
                <div class="row">
                    <div class="form-group base-form">
                        <label for="mainMetarialPrice">主材单价：</label>
                        <input type="email" class="form-control"  v-model="baseMain.mainMetarialPrice">
                    </div>
                    <div class="form-group base-form">
                        <label for="accessoriesMetarialPrice">辅材单价：</label>
                        <input type="email" class="form-control"  v-model="baseMain.accessoriesMetarialPrice">
                    </div>
                    <div class="form-group base-form">
                        <label for="artificialFee">人工费：</label>
                        <input type="email" class="form-control"  v-model="baseMain.artificialFee">
                    </div>
                    <div class="form-group base-form">
                        <label for="wastage">损耗：</label>
                        <input type="email" class="form-control"  v-model="baseMain.wastage">
                    </div>
                </div>
                <div class="row">
                    <div class="form-group base-form">
                        <label for="feeTotal">费用合计：</label>
                        <input type="email" class="form-control"  v-model="baseMain.feeTotal ">
                    </div>
                    <div class="form-group base-form">
                        <label for="remarks">工艺说明及材料说明：</label>
                        <input type="email" class="form-control" v-model="baseMain.remarks">
                    </div>
                    <div class="form-group base-form" :class="{'error-border': !baseMain.feeType}">
                        <label class="radio-inline">
                        <input type="radio" name="inlineRadioOptions" id="inlineRadio1" value="ADD" v-model="baseMain.feeType"> 增项
                        </label>
                        <label class="radio-inline">
                        <input type="radio" name="inlineRadioOptions" id="inlineRadio2" value="REDUCE" v-model="baseMain.feeType"> 减项
                        </label>
                        <label class="radio-inline">
                        <input type="radio" name="inlineRadioOptions" id="inlineRadio3" value="COMPREHENSIVE" v-model="baseMain.feeType"> 基装综合费
                        </label>
                    </div>
                    <div class="form-group base-form">
                        <button type="button" :disabled="submitting" @click="save" class="btn btn-primary m-r-lg">保存</button>
                    </div>
                </div>
            </form>
        </validator>
        <div class="row text-right base-tip">
            <h4>基装增项：{{addTotal}}   基装减项：{{reduceTotal}}    基装综合费：{{comprehensiveTotal}}     合计：{{totalVal}}</h4>
        </div>
        <table id="dataTable" width="100%" class="table table-striped table-bordered table-hover">
            <thead>
                <tr>
                    <th width="5%" style="text-align: center">类型</th>
                    <th>项目名称</th>
                    <th width="5%" style="text-align: center">单价</th>
                    <th width="5%" style="text-align: center">单位</th>
                    <th width="5%" style="text-align: center">数量</th>
                    <th width="7%" style="text-align: center">材料费<br>(主材单价)</th>
                    <th width="7%" style="text-align: center">材料费<br>(辅材单价)</th>
                    <th width="5%" style="text-align: center">人工费</th>
                    <th width="5%" style="text-align: center">损耗</th>
                    <th width="5%" style="text-align: center">合计</th>
                    <th>工艺说明及材料说明</th>
                    <th width="5%" style="text-align: center">操作</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="item in list">
                    <td>{{item.feeType | feeType-filter}}</td>
                    <td>{{item.projectName}}</td>
                    <td>{{item.price}}</td>
                    <td>{{item.unit}}</td>
                    <td>{{item.quantity}}</td>
                    <td>{{item.mainMetarialPrice}}</td>
                    <td>{{item.accessoriesMetarialPrice}}</td>
                    <td>{{item.artificialFee}}</td>
                    <td>{{item.wastage}}</td>
                    <td>{{item.feeTotal}}</td>
                    <td>{{item.remarks}}</td>
                    <td style="text-align: center">
                        <a href="javascript:void(0);" @click="edit(item)">编辑</a>
                        <a href="javascript:void(0);" @click="delete(item)">删除</a>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>