<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<template id="contractinfo">
  <div class="panel-heading" role="tab" id="headingOne" style="padding: 10px 9%;border:1px solid #c4c4c4">
    <div :class="{hasHeight:isShow }" class="row detail-stranding-book detail-state" v-cloak>
      <div class="col-sm-4 col-xs-12">
        <label for="">客户姓名：</label>
        {{customerContract.customer.name}}
      </div>
      <div class="col-sm-4 col-xs-12">
        <label for="">装修地址：</label>
        {{customerContract.houseAddr}}
      </div>
      <div class="col-sm-4 col-xs-12">
        <label for="">设计师：</label>
        {{customerContract.designer}}
      </div>
      <div class="col-sm-4 col-xs-12">
        <label for="">设计师电话：</label>
        {{customerContract.designerMobile}}
      </div>
      <div class="col-sm-4 col-xs-12">
        <label for="">监理：</label>
        {{customerContract.supervisor}}
      </div>
      <div class="col-sm-4 col-xs-12">
        <label for="">监理电话：</label>
        {{customerContract.supervisorMobile}}
      </div>
      <div class="col-sm-4 col-xs-12">
        <label for="">项目经理：</label>
        {{customerContract.projectManager}}
      </div>
      <div class="col-sm-4 col-xs-12">
        <label for="">项目经理电话：</label>
        {{customerContract.pmMobile}}
      </div>
    </div>
  </div>
</template>