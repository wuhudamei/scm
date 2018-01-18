+(function () {
    var vueIndex = new Vue({
        el: '#container',
        http: {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      },
        data: {
            // 面包屑
            breadcrumbs: [{
                path: '/admin',
                name: '主页'
            }, {
                path: '/',
                name: '修改密码',
                active: true
            }],
            // 查询表单
            form: {
                password: '',
                confirmPassword: ''
            },
            submitting: false
        },
        validators: {
            confirmPassword: function (val, password) {
                return val === password;
            }
        },
        methods: {
            submit: function () {
                var self = this;
                self.$validate(true, function () {
                    if (self.$validation.valid) {
                        self.submitting = true;
                        self.$http.post('/api/user/changePwd', $.param(self.form)).then(function (res) {
                            if (res.data.code == 1) {
                                self.$toastr.success('密码修改成功！');
                            }
                        }).catch(function () {
                        }).finally(function () {
                            self.submitting = false;
                        });
                    }
                });
            }
        }
    });
})();

