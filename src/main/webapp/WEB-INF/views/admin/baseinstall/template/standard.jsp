<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<template id="stand">
    <div>
        <validator name="validation">
            <form class="form-inline" >
                <div class="row">
                    <div class="form-group base-form">
                        <label for="standardProjectName">项目名称：</label>
                        <input type="text" class="form-control" v-model="baseMain.standardProjectName"
                        v-validate:standard-project-name="{required:true}"
                        :class="{'error': !$validation.standardProjectName.valid}">
                    </div>
                    <div class="form-group base-form">
                        <label for="location">位置：</label>
                        <input type="text" class="form-control"  v-model="baseMain.location"
                        >
                    </div>
                    <div class="form-group base-form">
                        <label for="brandMeal">品牌及套餐：</label>
                        <input type="text" class="form-control"  v-model="baseMain.brandMeal">
                    </div>
                    <div class="form-group base-form">
                        <label for="model">型号：</label>
                        <input type="text" class="form-control"  v-model="baseMain.model">
                    </div>
                </div>
                <div class="row">
                    <div class="form-group base-form">
                        <label for="wastageQuantity">损耗数量：</label>
                        <input type="text" class="form-control"  v-model="baseMain.wastageQuantity">
                    </div>
                    <div class="form-group base-form">
                        <label for="spec">规格：</label>
                        <input type="text" class="form-control"  v-model="baseMain.spec">
                    </div>
                    <div class="form-group base-form">
                        <label for="unit">单位：</label>
                        <input type="text" class="form-control"  v-model="baseMain.unit">
                    </div>
                    <div class="form-group base-form">
                        <label for="quantity">数量：</label>
                        <input type="text" class="form-control"  v-model="baseMain.quantity">
                    </div>

                </div>
                <div class="row">
                    <div class="form-group base-form">
                        <label for="actualQuantity">实发数量：</label>
                        <input type="text" class="form-control"  v-model="baseMain.actualQuantity">
                    </div>
                    <div class="form-group base-form">
                        <label for="remark">备注：</label>
                        <input type="text" class="form-control" v-model="baseMain.remark">
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
                    <th>项目名称</th>
                    <th>位置</th>
                    <th>品牌及套餐</th>
                    <th>型号</th>
                    <th>规格</th>
                    <th>单位</th>
                    <th>数量</th>
                     <th>含损耗数量</th>
                    <th>实发数量</th>
                    <th>备注</th>
                    <th width="5%" style="text-align: center">操作</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="item in list">
                    <td>{{item.standardProjectName}}</td>
                    <td>{{item.location}}</td>
                    <td>{{item.brandMeal}}</td>
                    <td>{{item.model}}</td>
                    <td>{{item.spec}}</td>
                    <td>{{item.unit}}</td>
                    <td>{{item.quantity}}</td>
                    <td>{{item.wastageQuantity}}</td>
                    <td>{{item.actualQuantity}}</td>
                    <td>{{item.remark}}</td>
                    <td style="text-align: center">
                        <a href="javascript:void(0);" @click="edit(item)">编辑</a>
                        <a href="javascript:void(0);" @click="delete(item)">删除</a>
                    </td>
                </tr>
                
            </tbody>
        </table>
    </div>
</template>