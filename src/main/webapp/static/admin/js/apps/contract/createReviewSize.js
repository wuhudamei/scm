+(function () {
    new Vue({
        el: '#container',
        mixins: [RocoVueMixins.DataTableMixin],
        validators: {
            suppliers: function (val) {
                return val > 0;
            }
        },
        components: {
            'web-uploader': RocoVueComponents.WebUploaderComponent,
        },
        data: {
            breadcrumbs: [{
                path: '/',
                name: '主页'
            },
                {
                    path: '/',
                    name: '创建复尺通知单',
                    active: true
                }
            ],
            list: [],
            webUploader: {
                type: 'TEST',
                formData: {
                    type: 'BRAND'
                },
                accept: {
                    title: 'Images',
                    extendsions: 'gif,jpg,jpeg,bmp,png',
                    mimeTypes: 'image/gif,image/jpeg,image/gif,image/bmp,image/png'
                },

                server: ctx + '/api/reviewSize',
                fileNumLimit: 8,
                fileSizeLimit: 50000 * 1024,
                fileSingleSizeLimit: 5000 * 1024
            },
            _$el: null,
            contractId: null,
            contractCode: null,
            customerContract: '',
            prodCatalogs: ''
        },
        created: function () {
            this.addData();
            this.contractId = this.$parseQueryString()['contractId'];
            this.contractCode = this.$parseQueryString()['contractCode'];
            this.findCustomerContract();
            this.getProdCatalogs();

        },
        ready: function () {
        },
        methods: {
            bigImg: function (fullpath) {
                imgModel(fullpath);
            },
            getProdCatalogs: function () {
                var self = this;
                self.$http.get('/api/catalog/findCatalogByIsReject').then(function (res) {
                    if (res.data.code == 1) {
                        self.prodCatalogs = res.data.data;
                    }
                }).catch(function () {

                }).finally(function () {

                });
            },
            findCustomerContract: function () {
                var self = this;
                this.$http.get("/api/customer/contract/getById?contractId=" + this.$parseQueryString().contractId).then(function (res) {
                    if (res.data.code == 1) {
                        self.customerContract = res.data.data;
                    }
                })
            },
            addData: function () {
                var uuid = 'mdni' + new Date().getTime();
                var self = this;
                self.list.push({
                    id: uuid,
                    contractId: this.$parseQueryString().contractId,
                    contractCode: this.$parseQueryString().contractCode,
                    supplier: [],
                    brand: [],
                    remark: '',
                    images: [],
                    prodCataLogId: '',
                    supplierField: 'supplier' + uuid,
                    brandField: 'brandField' + uuid,
                    brandValidate: {
                        required: {
                            rule: true,
                            message: '请选择品牌'
                        }
                    },

                    imagesField: 'imagesField' + uuid,
                    imagesValidate: {
                        required: {
                            rule: true,
                            message: '请上传图片'
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
            insert: function () {
                var self = this;
                self.$validate(true, function () {
                    if (self.$validation.valid) {
                        var lists = [];
                        var review;
                        self.list.forEach(function (item) {
                            var imgUrls = '';
                            item.images.forEach(function (imagesItem) {
                                imgUrls += imagesItem.imagePath + ','
                            })
                            item.supplier.forEach(function (supplierItem, index) {
                                review = {
                                    supplierId: supplierItem.id,
                                    contractId: item.contractId,
                                    contractCode: item.contractCode,
                                    brandId: item.brand[0].id,
                                    remark: item.remark,
                                    prodCataLogId: item.prodCatalogId,
                                    reviewSizeNoticeImage: imgUrls.length > 0 ? imgUrls.substring(0, imgUrls.length - 1) : ''
                                };
                            }),

                                lists.push(review);
                        })
                        self.$http.post('/api/reviewSize/insert', lists).then(function (res) {
                            if (res.data.code == 1) {
                                self.$toastr.success('操作成功');
                                window.location.href = "/admin/contract"
                            }
                        })
                    }
                })

            },
            deleteImg: function (ind, url, index, type) {
                var self = this;
                self.$http.get('/api/reviewSize/delete', {
                    params: {
                        path: url
                    }
                }).then(function (res) {
                    if (res.data.code == 1) {
                        self.list[index].images.splice(ind, 1)
                    }
                }).finally(function () {
                })
            },

            removeData: function (index) {
                this.list.splice(index, 1);
            },
            closeWin: function () {
                window.history.go(-1);
            },
        },
        events: {
            'webupload-upload-success-TEST': function (file, res, objectId, ind) {
                if (res.code == 1) {
                    this.$toastr.success('上传成功');
                    var obj = {
                        imagePath: res.data.path,
                        fullPath: res.data.fullPath
                    }
                    this.list[ind].images.push(obj)

                } else {
                    this.$toastr.error(res.message);
                }
            },
        }
    });
    function imgModel(fullPath) {
        var _$modal = $('#imgModel').clone();
        var $modal = _$modal.modal({
            height: 440,
            width: 750
        });
        // 获取 node
        var el = $modal.get(0);
        // 创建 Vue 对象编译节点
        showBill2 = new Vue({
            el: el,
            // 模式窗体必须引用 ModalMixin
            mixins: [RocoVueMixins.ModalMixin],
            $modal: $modal, // 模式窗体 jQuery 对象
            data: {
                fullPath: fullPath
            },
            methods: {
            },
            created: function () {
            },
            ready: function () {
            }
        });
        // 创建的Vue对象应该被返回
        return showBill2;
    }
})();