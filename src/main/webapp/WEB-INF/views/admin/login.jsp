<%@ page contentType="text/html;charset=UTF-8" %>
<title>登录</title>
<head></head>
<body>
<div id="container" class="wrap-login">
  <div class="content-login" style="background:none;">
    <div class="login-area">
      <validator name="validation">
        <form v-on:submit.prevent="submit" role="form">
          <div class="form-group"
               :class="{'has-error':$validation.username.invalid && $validation.touched}">
            <label for="username">用户名</label>
            <input
                v-model="form.username"
                v-validate:username="{required:true}"
                id="username" name="username" type="text" class="form-control" placeholder="请输入用户名">
            <span v-cloak v-if="$validation.username.invalid && $validation.touched" class="help-absolute">
                用户名是必填项
              </span>
          </div>
          <div class="form-group" :class="{'has-error':$validation.password.invalid && $validation.touched}">
            <label for="exampleInputPassword1">密码</label>
            <input
                v-model="form.password"
                v-validate:password="{required:true}"
                type="password" class="form-control" placeholder="请输入密码">
            <span v-cloak v-if="$validation.password.invalid && $validation.touched" class="help-absolute">
                密码是必填项
              </span>
          </div>
          <button :disabled="submitting" type="submit" class="btn btn-block btn-primary">登 录</button>
        </form>
      </validator>
    </div>
  </div>
</div>
<script src="/static/admin/js/apps/login/login.js?v=ea38b0f7"></script>
</body>