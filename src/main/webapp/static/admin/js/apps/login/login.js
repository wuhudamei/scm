+(function () {
  var login = new Vue({
    el: '#container',
    http: {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    },
    data: {
      form: {
        username: '',
        password: ''
      },
      submitting: false
    },
    methods: {
      submit: function () {
        var self = this;
        self.$validate(true,
          function () {
            if (self.$validation.valid) {
              var data = {
                username: self.form.username,
                password: self.form.password
              };
              self.submitting = true;
              self.$http.post(ctx + '/api/login', $.param(data)).then(function (res) {
                if (res.data.code == "1") {
                  location.href = '/admin';
                } else if (res.data.code == "0" || res.code == "1000") {
                  Vue.toastr.error(res.data.message);
                }
              }).catch(function () {

              }).finally(function () {
                self.submitting = false;
              });
            }
          });
      }
    },
    created: function () {
    },
    ready: function () {
    }
  });
})();