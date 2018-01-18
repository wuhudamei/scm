<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<template id="house">
    <div>
        <validator name="validation">
            <form class="form-inline" >
                <div class="row">
                    <div class="form-group base-form">
                        <label for="refomProjectName">项目名称：</label>
                        <input type="text" class="form-control" v-model="baseMain.refomProjectName"
                        v-validate:refom-project-name="{required:true}"
                        :class="{'error': !$validation.refomProjectName.valid}">
                    </div>
                    <div class="form-group base-form">
                        <label for="materialMasterFee">材料费（主材单价）：</label>
                        <input type="email" class="form-control"  v-model="baseMain.materialMasterFee">
                    </div>
                    <div class="form-group base-form">
                        <label for="matrialAssistFee">材料费（辅材单价）：</label>
                        <input type="email" class="form-control"  v-model="baseMain.matrialAssistFee">
                    </div>
                </div>
                <div class="row">

                </div>
                <div class="row">
                    <div class="form-group base-form">
                        <label for="price">单价：</label>
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
                    <div class="form-group base-form">
                        <label for="amount">合计：</label>
                        <input type="email" class="form-control"  v-model="baseMain.amount">
                    </div>
                </div>
                <div class="row">
                    <div class="form-group base-form">
                        <label for="wastageQuantity">损耗：</label>
                        <input type="email" class="form-control"  v-model="baseMain.wastageQuantity">
                    </div>
                    <div class="form-group base-form">
                        <label for="manMadeFee">人工费：</label>
                        <input type="email" class="form-control"  v-model="baseMain.manMadeFee">
                    </div>
                    <div class="form-group base-form">
                        <label for="technologyMaterialExplain">工艺说明及材料说明：</label>
                        <input type="email" class="form-control" v-model="baseMain.technologyMaterialExplain">
                    </div>
                    <div class="form-group base-form">
                        <button type="button" :disabled="submitting" @click="save" class="btn btn-primary m-r-lg">保存</button>
                    </div>
                </div>
            </form>
        </validator>
        <div class="row text-right base-tip"></div>
        <table id="dataTable" width="100%" class="table table-striped table-bordered table-hover">
            <thead>
                <tr>
                    <th width="10%" style="text-align: center">项目名称</th>
                    <th width="5%" style="text-align: center">单位</th>
                    <th width="5%" style="text-align: center">数量</th>
                    <th width="5%" style="text-align: center">损耗</th>
                    <th width="7%" style="text-align: center">材料费<br>(主材单价)</th>
                    <th width="7%" style="text-align: center">材料费<br>(辅材单价)</th>
                    <th width="5%" style="text-align: center">人工费</th>
                     <th width="5%" style="text-align: center">单价</th>
                    <th width="5%" style="text-align: center">合计</th>
                    <th>工艺说明及材料说明</th>
                    <th width="5%" style="text-align: center">操作</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="item in list">
                    <td>{{item.refomProjectName}}</td>
                    <td>{{item.unit}}</td>
                    <td>{{item.quantity}}</td>
                    <td>{{item.wastageQuantity}}</td>
                    <td>{{item.materialMasterFee}}</td>
                    <td>{{item.matrialAssistFee}}</td>
                    <td>{{item.manMadeFee}}</td>
                    <td>{{item.price}}</td>
                    <td>{{item.amount}}</td>
                    <td>{{item.technologyMaterialExplain}}</td>
                    <td style="text-align: center">
                        <a href="javascript:void(0);" @click="edit(item)">编辑</a>
                        <a href="javascript:void(0);" @click="delete(item)">删除</a>
                    </td>
                </tr>
                
            </tbody>
        </table>
    </div>
</template>