var vueIndex;
var vueModal;
+(function () {
  $('#applyManage').addClass('active');
  $('#brandMenu').addClass('active');
    vueIndex= new Vue({
    el: '#container',
    mixins: [RocoVueMixins.DataTableMixin],
    data: {
      breadcrumbs: [{
        path: '/',
        name: '主页'
      },
        {
          path: '/',
          name: '品牌管理',
          active: true
        }],
      form: {
        keyword: '',
        status: ''
      },
      $dataTable: null,
      selectedRows: {},
      modalModel: null,
      _$el: null,
      _$dataTable: null,
      statusOptions: [{
        text: '全部状态',
        value: ''
      },
        {
          text: '启用',
          value: 'OPEN'
        },
        {
          text: '停用',
          value: 'LOCK'
        }]
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
          url: '/api/brand/list',
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
          columns: [{
            field: "id",
            visible: false
          },
            {
              field: 'code',
              title: '编码',
              width: '15%',
              orderable: true,
              align: 'center'
            },
            {
              field: 'logo',
              title: '缩略图',
              width: '15%',
              orderable: false,
              visible: false,
              align: 'center',
              formatter: function (value, row, index) {
                return '<img src="' + window.location.origin + value + '" width="60px">';
              }
            },
            {
	          field: 'brandName',
	          title: '名称',
	          width: '10%',
	          orderable: false,
              align: 'center'
	        },
            {
	          field: 'pinyinInitial',
	          title: '品牌拼音首字母',
	          width: '10%',
	          orderable: false,
              align: 'center'
	        },
            {
              field: 'status',
              title: '启用状态',
              width: '10%',
              orderable: true,
              align: 'center',
              formatter: function (data) {
                if (data == "OPEN") {
                  return "启用";
                } else {
                  return "停用";
                }
              }
            },
            {
              field: 'id',
              title: '操作',
              width: '20%',
              orderable: false,
              align: 'center',
              formatter: function (data, full, type, meta) {
                  var html = ''
                  if( hasEditBrandPrivelege ){
                      var deal_name = "停用";
                      var class_val = "m-r-xs btn btn-xs btn-danger";
                      if (full.status == 'LOCK') {
                          deal_name = "启用";
                          class_val = "m-r-xs btn btn-xs btn-success";
                      }

                      html += '<button style="margin-left:10px;"'
                      html += 'data-handle="data-edit"'
                      html += 'data-id="' + data + '"'
                      html += 'data-name="' + full.brandName + '"'
                      html += 'data-code="' + full.code + '"'
                      html += 'data-pinyininitial="' + full.pinyinInitial + '"'
                      html += 'data-status="' + full.status + '"'
                      html += 'data-logo="' + full.logo + '"'
                      html += 'data-description="' + full.description + '"'
                      html += 'class="m-r-xs btn btn-xs btn-primary" type="button">编辑</button>';
                      return html + '<button style="margin-left:15px;" data-handle="data-deal" data-id="' + data + '"  data-status="' + full.status + '" class="' + class_val + '" type="button">' + deal_name + '</button>';
                  }else{
                      return html;
                  }
              }
            }]
        });
        //编辑
        self.$dataTable.on('click', '[data-handle="data-edit"]',
          function (e) {
            var model = $(this).data();
            model.pinyinInitial = model.pinyininitial;
            createOrEditModal(model, true)
            e.stopPropagation();
          });

        //停用启用
        self.$dataTable.on('click', '[data-handle="data-deal"]',
          function (e) {
            var id = $(this).data('id');
            var status = $(this).data('status');
            var tmpText = "";
            if (status == "LOCK") {
              tmpText = "是否确定将此品牌上线么?";
              status = "OPEN";
            } else if (status == "OPEN") {
              tmpText = "是否确定将此品牌下线么?";
              status = "LOCK";
            }
            swal({
                title: '修改品牌状态',
                text: tmpText,
                type: 'info',
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                showCancelButton: true,
                showConfirmButton: true,
                showLoaderOnConfirm: true,
                confirmButtonColor: '#ed5565',
                closeOnConfirm: false
              },
              function () {
                var data = {
                  'id': id,
                  'status': status
                };
                self.$http.post('/api/brand/changeStatus', data, {
                  emulateJSON: true
                }).then(function (res) {
                  if (res.data.code == 1) {
                    Vue.toastr.success("操作成功");
                    self.$dataTable.bootstrapTable('refresh');
                  } else {
                    Vue.toastr.error(res.data.message);
                  }
                }).catch(function () {

                }).finally(function () {
                  swal.close();
                });
              });

            e.stopPropagation();
          });

        self.checkEventHandle('ordNo');
      },
      createBtnClickHandler: function (e) {
    	  //新增
        createOrEditModal({
            "status": "OPEN"
          },
          false);
      }
    }
  });

  // 新增或修改窗口
  function createOrEditModal(model, isEdit) {
    var _modal = $('#modal').clone();
    var $el = _modal.modal({
      height: 480,
      maxHeight: 500
    });
    $el.on('shown.bs.modal',
      function () {
        var el = $el.get(0);
        isEdit = !!isEdit;
          vueModal = new Vue({
          el: el,
          validators: {
            url: function (val) {
              if (_.trim(val) === '') {
                return true;
              }
              return /^(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\ + #] * [\w\ - \@ ? ^=%&amp;/~\+#])?$/.test(val);
            },
          },
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
          data: {
            id: model.id,
            brandName: model.name,
            logo: model.logo == 'undefined'? '': model.logo,
            linkUrl:model.logo == 'undefined'? '': ctx + model.logo,
            status: model.status,
            code: model.code,
            description: model.description == 'undefined'? '': model.description,
            pinyinInitial: model.pinyininitial,
            statusOptions: [{
              text: '启用',
              value: 'OPEN'
            },
              {
                text: '停用',
                value: 'LOCK'
              }],
            uploading: false,
            progress: 0,
            webUploader: {
              type: 'bannerfile',
              formData: {
                type: 'BRAND'
              },
              accept: {
                title: 'Images',
                extendsions: 'gif,jpg,jpeg,bmp,png',
                mimeTypes: 'image/jpg,image/png,image/gif,image/jpeg,image/bmp'
              },
              thumb: {
                width: 40,
                height: 40,
                // 图片质量，只有type为`image/jpeg`的时候才有效。
                quality: 70,
                // 是否允许放大，如果想要生成小图的时候不失真，此选项应该设置为false.
                allowMagnify: false,
                // 是否允许裁剪。
                crop: true,
                // 为空的话则保留原有图片格式。
                // 否则强制转换成指定的类型。
                type: "image/jpeg"
              },
              server: ctx + '/api/upload',
              //上传图片路径
              fileNumLimit: 8,
              fileSizeLimit: 50000 * 1024,
              fileSingleSizeLimit: 5000 * 1024
            }
          },
          methods: {
            save: function () {
              var self = this;
              self.$validate(true,
                function () {
                  if (self.$validation.valid) {
                    self.submitting = true;
                    self.$http.post('/api/brand/saveOrUpdate', $.param(self._data)).then(function (res) {
                      if(res.data.code == '1'){
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
          events: {
            // 上传成功
            'webupload-upload-success-bannerfile': function (file, res) {
              if (res.code == 1) {
                this.logo = res.data.fullPath;
                this.linkUrl = res.data.fullPath;
                // this.form.size = file.size;
                this.$toastr.success('上传成功');
              } else {
                this.$toastr.error(res.message);
              }
            },
            // 上传进度
            'webupload-upload-progress-bannerfile': function (file, percentage) {
              this.progress = percentage * 100;
            },
            // 上传结束
            'webupload-upload-complete-bannerfile': function (file) {
              this.uploading = false;
            },
            // 上传开始
            'webupload-upload-start-bannerfile': function (file) {
              this.uploading = true;
            }
          }

        });

        // 创建的Vue对象应该被返回
        return vueModal;
      });

  }
})();