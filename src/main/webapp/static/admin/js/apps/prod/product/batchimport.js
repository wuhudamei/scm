+(function () {
    $('#applyManage').addClass('active');
    $('#importMenu').addClass('active');
    var vueModal = new Vue({
        el: '#container',
        http: {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        },
        mixins: [DaMeiVueMixins.ModalMixin],
        components: {
            'web-uploader': DaMeiVueComponents.WebUploaderComponent
        },
        created: function () {
        },
        data: {
            id: '',
            name: '',
            logo: '',
            linkUrl: '',
            uploading: false,
            progress: 0,
            dataResult: [],
            webUploader: {
                type: 'productfile',
                formData: {
                    type: 'PRODUCT'
                },
                server: ctx + '/api/productimport/import',
                //上传图片路径
                fileNumLimit: 8,
                fileSizeLimit: 50000 * 1024,
                fileSingleSizeLimit: 5000 * 1024
            }
        },
        methods: {
            // 下载文件
            downloadFile: function (url) {
                // 创建一个 iframe 设置 url 插入 dom
                var iframe = document.createElement('iframe');
                iframe.style.display = 'none';
                iframe.frameborder = 0;
                document.body.appendChild(iframe);
                iframe.src = '/api/productimport/downloadTemplate';
            }
        },
        events: {
            // 上传成功
            'webupload-upload-success-productfile': function (file, res) {
                var self = this;
                if (res.code == 1) {
                    self.dataResult = res.data;
                    this.$toastr.success(res.message);
                } else {
                    self.dataResult = res.data;
                    this.$toastr.error(res.message);
                }
            },
            // 上传进度
            'webupload-upload-progress-productfile': function (file, percentage) {
                this.progress = percentage * 100;
            },
            // 上传结束
            'webupload-upload-complete-productfile': function (file) {
                this.uploading = false;
                $('#dealingModal').modal('hide');
                dealingVue.$destroy();
            },
            // 上传开始
            'webupload-upload-start-productfile': function (file) {
                openDealingModal();
                this.uploading = true;
            }
        }

    });

    function openDealingModal() {

        //var _modal = $('#dealingModal').clone();
        var $el = $('#dealingModal').modal({
            height: 50,
            maxHeight: 50,
            backdrop: 'static',
            keyboard: false
        });

        var el = $el.get(0);

        dealingVue = new Vue({
            el: el,
            $modal: $el,
            created: function () {},
            data: {},
            methods: {}
        });
    }

})();