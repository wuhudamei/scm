<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<div id="container" class="wrapper wrapper-content animated fadeInRight">
  <div id="breadcrumb">
    <bread-crumb :crumbs="breadcrumbs"></bread-crumb>
  </div>
   <!-- ibox start -->
    <div class="ibox">
        <div class="ibox-content">
            <validator name="validation">
                <form name="createMirror" novalidate class="form-horizontal" role="form">
                    <div class="modal-body">
                        
                        <div class="form-group" :class="{'has-error':$validation.password.invalid && $validation.touched}">
                            <label for="username" class="col-sm-2 control-label">用户名</label>
                            <div class="col-sm-8">
                                <input  maxlength="30"  type="text"  class="form-control" readonly="readonly" value="<shiro:principal property="loginName"/>  " >
                            </div>
                        </div>
                        
                        <div class="form-group" :class="{'has-error':$validation.password.invalid && $validation.touched}">
                            <label for="password" class="col-sm-2 control-label"><span style="color: red">*</span>新密码</label>
                            <div class="col-sm-8">
                                <input v-model="form.password"  maxlength="20"
                                       data-tabindex="1"  type="password" class="form-control"
                                       placeholder="请输入新密码"   v-validate:password="{
                                        required:{rule:true,message:'请输入新密码'},
                                        maxlength:{rule:20,message:'密码最长不能超过20个字符'}
                                       }">
                                <span v-cloak v-if="$validation.password.invalid && $validation.touched"
                                      class="help-absolute">
		                            <span v-for="error in $validation.password.errors">
		                              {{error.message}} {{($index !== ($validation.password.errors.length -1)) ? ',':''}}
		                            </span>
		                        </span>
                            </div>
                        </div>
                        
                        <div class="form-group"  :class="{'has-error':$validation.confirmpassword.invalid && $validation.touched}">
                            <label for="confirmPassword" class="col-sm-2 control-label"><span style="color: red">*</span>确认密码</label>
                            <div class="col-sm-8">
                                <input v-model="form.confirmPassword" maxlength="20"  data-tabindex="2"   
                                       type="password" class="form-control" placeholder="请输入确认密码" 
                                        v-validate:confirmpassword="{
                                        required:{rule:true,message:'请输入确认密码'},
                                        maxlength:{rule:20,message:'确认密码最长不能超过20个字符'},
                                        confirmPassword:{rule:form.password,message:'两次输入密码不一致'}
                                       }">
                                <span v-cloak v-if="$validation.confirmpassword.invalid && $validation.touched"
                                      class="help-absolute">
		                            <span v-for="error in $validation.confirmpassword.errors">
		                              {{error.message}} {{($index !== ($validation.confirmpassword.errors.length -1)) ? ',':''}}
		                            </span>
		                        </span>
                            </div>
                        </div>
                       
                        <div style="margin-left:165px">
                            <button @click="submit" :disabled="submitting" type="button" class="btn btn-primary">
                                                                                       确认修改
                            </button>
                        </div>
                    </div>
                </form>
            </validator>
        </div>
    </div>
    <!-- ibox end -->
</div>


<script src="/static/admin/js/apps/user/changePwd.js?v=ea38b0f7"></script>