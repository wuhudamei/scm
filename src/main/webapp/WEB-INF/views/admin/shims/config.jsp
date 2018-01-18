<%@ page contentType="text/html;charset=UTF-8" %>
<script>
  // 设置为中文
  try {
    $.fn.bootstrapTable.defaults.locale = 'zh-CN';

    var RocoVueMixins = window.RocoVueMixins = {};
    var RocoVueComponents = window.RocoVueComponents = {};

    // 打开 Vue 调试器
    Vue.config.devtools = /localhost|192\./.test(window.location.href);

    // Vue resource设置
    // 模拟表单提交
    // Vue.http.options.headers = {
    //   'Content-Type':'application/x-www-form-urlencoded;charset = UTF-8;'
    // };
    // Vue.http.options.emulateJSON = true;

    // Vue resource 添加拦截器
    Vue.http.interceptors.push(function (request, next) {
      // get 请求加时间戳参数去缓存
      if (request.method.toLowerCase() == 'get') {
        request.params._ = Date.now();
      }

      //响应处理
      next(function (response) {
        //安卓下框架不会自动json化
        try {
          if (!Vue.util.isObject(response.data)) {
            response.data = JSON.parse(response.body);

          }
        } catch (e) {
          response.data = null;
          console.log(e.toString());
        }

        if (response.data.code == 0 || response.data.code == 9) {
          Vue.toastr.error(response.data.message);
        }
        // 约定公共 code 处理
        // if(response.data.errorcode == 88) {
        //   window.location.href = ctx + '/login?path=' + window.location.pathname;
        // }else if(response.data.errorcode != 0){
        //   alert(response.data.message);
        // }
      });
    });
  } catch (e) {
    console.log(e.toString())
  }
</script>