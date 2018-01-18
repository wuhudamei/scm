// 下拉选择格式
// {
//   "data":{
//     "list":[
//       id: accountName,
//       name: chsName,
//       accountName:accountName,
//       orgName:orgName,
//       gradeName:gradeName,
//       _selected: self.isSelected(element.accountName)
//     ]
//   }
// }


new Vue({
    el: '#container',
    mixins: [RocoVueMixins.DataTableMixin],
    validators: {
      suppliers: function (val) {
        return val.length === 0;
      }
    },
    components: {
      'web-uploader': RocoVueComponents.WebUploaderComponent
    },
    data: {
      breadcrumbs: [{
          path: '/',
          name: '主页'
        },
        {
          path: '/',
          name: '测试',
          active: true
        }
      ],
      list: [],
      webUploader: {
        type: 'bannerfile',
        formData: {
          type: 'BRAND'
        },
        accept: {
          title: 'Images',
          extendsions: 'gif,jpg,jpeg,bmp,png',
          mimeTypes: 'image/gif,image/jpeg,image/gif,image/bmp,image/png'
        },
        server: ctx + '/api/product/import',
        fileNumLimit: 8,
        fileSizeLimit: 50000 * 1024,
        fileSingleSizeLimit: 5000 * 1024
      }
    },
    created: function () {
      this.addData();
    },
    ready: function () {},
    methods: {
      addData: function () {
        var uuid = 'mdni' + new Date().getTime();
        var self = this;
        self.list.push({
          id: uuid,
          supplier: [],
          brand: '',
          remark: '',
          images: [],
          supplierField: 'supplier' + uuid,
          // supplierValidate: {
          //   required: {
          //     rule: {
          //       supplier: item.supplier
          //     },
          //     message: '请选择供应商'
          //   }
          // },
          brandField: 'brandField' + uuid,
          brandValidate: {
            required: {
              rule: true,
              message: '请选择品牌'
            }
          },
          remarkField: 'remark' + uuid,
          remarkValidate: {
            maxlength: {
              rule: 100,
              message: '备注不能超过100个字符'
            }
          }
        })
      },
      removeData: function (index) {
        this.list.splice(index, 1);
      }
    },
    events: {
      'webupload-upload-success-TEST': function (file, res, objectId) {
        if (res.code == 1) {
          this.$toastr.success('上传成功');

          var obj = {
            imagePath: res.data.path,
            fullPath: res.data.fullPath
          }
          for (var i = 0, len = this.list.length; i < len; i++) {
            var item = this.list[i];
            if (item.id === objectId) {
              item.images.push(obj);
              break;
            }
          }
        } else {
          this.$toastr.error(res.message);
        }
      },
    }
  });