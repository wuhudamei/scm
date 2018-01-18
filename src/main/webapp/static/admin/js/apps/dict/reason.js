+(function () {
  $('#dictManage').addClass('active');
  $('#reasonDict').addClass('active');
  Vue.validator('telphone', function(tel) {
    return /^1[3|4|5|7|8]\d{9}$/.test(tel);
  })
  var vueIndex = new Vue({
    el: '#container',
    mixins: [RocoVueMixins.DataTableMixin],
    data: {
      breadcrumbs: [{
        path: '/',
        name: '主页'
      },
      {
        path: '/',
        name: '作废原因',
        active: true
      }],
      form: {
        keyword: '',
        status: ''
      },
      $dataTable: null,
      modalModel: null,
      _$el: null,
      _$dataTable: null
    },
    created: function () {
    },
    ready: function () {
      this.drawTable();
    },
    methods: {
      query: function () {
        this.$dataTable.bootstrapTable('selectPage', 1);
      },
      drawTable: function () {
        var self = this;
        self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
          url: '/api/dict/reason',
          method: 'get',
          dataType: 'json',
          cache: false,
          pagination: true,
          sidePagination: 'server',
          queryParams: function (params) {
            return _.extend({},
              params, self.form);
          },
          mobileResponsive: true,
          undefinedText: '-',
          striped: true,
          maintainSelected: true,
          sortOrder: 'desc',
          columns: [
            {
              field: "id",
              visible: false
            },
            {
              field: 'reason',
              title: '作废原因',
              width: '15%',
              orderable: true,
              align: 'center'
            },
            {
              field: 'id',
              title: '操作',
              width: '20%',
              orderable: false,
              align: 'center',
              formatter: function (value, row) {
                var html = ''
                html += '<button style="margin-left:10px;"'
                html += 'data-handle="data-edit"'
                html += 'data-id="' + value + '"'
                html += 'data-reason="' + row.reason + '"'
                html += 'class="m-r-xs btn btn-xs btn-primary" type="button">编辑</button>'
                return html 
              }
            }]
        });
        //编辑
        self.$dataTable.on('click', '[data-handle="data-edit"]',
          function (e) {
            var model = $(this).data()
            createOrEditModal(model, true)
            e.stopPropagation()
          }
        );
        //起停
        self.$dataTable.on('click', '[data-handle="data-deal"]',
          function (e) {
            var model = $(this).data()     
              self.$http.post('/api/dict/reason/save', model,{emulateJSON: true}).then(function (res) {
                if(res.data.code === '1'){
                  Vue.toastr.success(res.data.message);
                  vueIndex.$dataTable.bootstrapTable('selectPage', 1);
                }
                },
              function (error) {
                Vue.toastr.error(error.responseText);
              }).catch(function () {
              }).finally(function () {
                self.submitting = false;
              }); 
            e.stopPropagation()
          }
        );
      },
      createBtnClickHandler: function (e) {
    	  //新增
        createOrEditModal({
          },
          false);
      }
    }
  });

  //  新增／编辑
  function createOrEditModal(model, isEdit) {
    var _modal = $('#contractModal').clone();
    var $el = _modal.modal({
      height: 250,
      maxHeight: 400
    });
    $el.on('shown.bs.modal',
      function () {
        var el = $el.get(0);
        isEdit = !!isEdit;
        var vueModal = new Vue({
          el: el,
          http: {
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded'
            }
          },
          mixins: [RocoVueMixins.ModalMixin],
          components: {
            'web-uploader': RocoVueComponents.WebUploaderComponent
          },
          $modal: $el,
          created: function () {
          },
          ready: function () {
              this.fetch();
          },
          data: model,
          methods: {
              fetch: function () {
                  var self = this;
                  if (model.id !== undefined) {
                      self.$http.get('/api/dict/reason/' + model.id).then(function (res) {
                          if (res.data.code == 1) {
                              self.reason = res.data.data.reason;
                          }
                      })
                  }
              },
            save: function () {
              var self = this;
              self.$validate(true,
                function () {
                  if (self.$validation.valid) {
                    self.submitting = true;
                    self.$http.post('/api/dict/reason/save', $.param(self._data)).then(function (res) {
                      if(res.data.code === '1'){
                        Vue.toastr.success(res.data.message);
                        $el.modal('hide');
                        vueIndex.$dataTable.bootstrapTable('refresh');
                        self.$destroy();
                      }
                      },
                    function (error) {
                      Vue.toastr.error(error.responseText);
                    }).catch(function () {
                    }).finally(function () {
                      self.submitting = false;
                    });
                  }
                })
            }
          },
        });
        // 创建的Vue对象应该被返回
        return vueModal;
      });
  }
})();