+(function () {
    $('#applyManage').addClass('active');
    $('#supplierList').addClass('active');
    Vue.validator('telphone', function (tel) {
        return /^1[3|4|5|7|8]\d{9}$/.test(tel);
    })
    var vueIndex = new Vue({
        el: '#container',
        mixins: [RocoVueMixins.DataTableMixin],
        data: {
            breadcrumbs: [{
                path: '/admin',
                name: '主页'
            }, {
                path: '/',
                name: '商品供应商',
                active: true
            }],
            form: {
                keyword: '',
                status: '',
                regionSupplierId: ''
            },
            supplierStatus: null,
            allRegionSupplier: null,
            $dataTable: null,
            modalModel: null,
            _$el: null,
            _$dataTable: null
        },
        created: function () {
        },
        ready: function () {
            this.initRegionList();
            this.supplierStatus = [
                {label: "请选择供应商状态", value: ""}, {label: "启用", value: "OPEN"},
                {label: "停用", value: "LOCK"},

            ];
        },
        methods: {
            query: function () {
                this.$dataTable.bootstrapTable('selectPage', 1);
            },

            initRegionList: function () {
                var self = this;
                self.$http.get('/api/regionSupplier/all').then(function (res) {
                    if (res.data.code == 1) {
                        self.allRegionSupplier = res.data.data;
                    }
                    self.drawTable();
                }).catch(function () {
                }).finally(function () {
                });
            },

            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/api/supplier/list',
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
                            field: 'code',
                            title: '编码',
                            width: '8%',
                            orderable: true,
                            align: 'center'
                        },
                        {
                            field: 'name',
                            title: '供应商名称',
                            width: '15%',
                            orderable: true,
                            align: 'center'
                        },
                        {
                            field: 'contactor',
                            title: '联系人',
                            width: '6%',
                            orderable: true,
                            align: 'center'
                        },
                        {
                            field: 'mobile',
                            title: '手机',
                            width: '7%',
                            orderable: true,
                            align: 'center'
                        },
                        {
                            field: 'phone',
                            title: '公司固话',
                            width: '7%',
                            orderable: true,
                            align: 'center'
                        },
                        {
                            field: 'address',
                            title: '公司地址',
                            width: '15%',
                            orderable: true,
                            align: 'center'
                        },
                        {
                            field: 'regionSupplier',
                            title: '所属区域供应商',
                            width: '10%',
                            align: 'center',
                            formatter: function (value) {
                                return value.name;
                            }
                        },
                        {
                            field: 'status',
                            title: '状态',
                            width: '7%',
                            align: 'center',
                            orderable: false,
                            formatter: function (value) {
                                return value == 'LOCK' ? "停用" : "启用"
                            }
                        },
                        {
                            field: 'description',
                            title: '描述',
                            width: '10%',
                            align: 'center'
                        },
                        {
                            field: 'id',
                            title: '操作',
                            width: '10%',
                            orderable: true,
                            align: 'center',
                            formatter: function (value, row) {
                                var html = ''
                                if (hasEditSupplierPrivelege && acctType == 'REGION_SUPPLIER') {
                                    var deal_name = "停用";
                                    var class_val = "m-r-xs btn btn-xs btn-danger";
                                    var nostatus = ''
                                    if (row.status == 'LOCK') {
                                        deal_name = "启用";
                                        class_val = "m-r-xs btn btn-xs btn-primary";
                                        nostatus = 'OPEN'
                                    } else {
                                        nostatus = 'LOCK'
                                    }
                                    html += '<button style="margin-left:10px;"'
                                    html += 'data-handle="data-edit"'
                                    html += 'data-id="' + value + '"'
                                    html += 'class="m-r-xs btn btn-xs btn-primary" type="button">编辑</button>'
                                    html += '<button data-handle="data-deal" data-id="' + value + '"  data-newStatus="' + nostatus + '" class="' + class_val + '" type="button">' + deal_name + '</button>';
                                }
                                return html
                            }
                        }]
                });
                //编辑
                self.$dataTable.on('click', '[data-handle="data-edit"]',
                    function (e) {
                        self.$http.get('/api/supplier/getById/' + $(this).data("id")).then(function (res) {
                            if (res.data.code == 1) {
                                var model = res.data.data;
                                model.regionList = self.allRegionSupplier;
                                model.regionSupplierId = model.regionSupplier.id;
                                createOrEditModal(model, true);
                            }
                        }).catch(function () {
                        }).finally(function () {
                        });
                        e.stopPropagation()
                    }
                );

                //启用/停用
                self.$dataTable.on('click', '[data-handle="data-deal"]',
                    function (e) {
                        var send = {
                            newStatus: $(this).data("newstatus"),
                            supplierId: $(this).data("id")
                        }
                        self.$http.post('/api/supplier/changeStatus', send, {emulateJSON: true}).then(function (res) {
                                if (res.data.code === '1') {
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

            //新增
            createBtnClickHandler: function (e) {
                createOrEditModal({"status": "OPEN", regionSupplierId: '', regionList: this.allRegionSupplier}, false);
            }
        }
    });

    //  新增／编辑
    function createOrEditModal(model, isEdit) {
        var _modal = $('#contractModal').clone();
        var $el = _modal.modal({
            maxHeight: 600
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
                    components: {
                        'web-uploader': RocoVueComponents.WebUploaderComponent
                    },
                    mixins: [RocoVueMixins.ModalMixin],
                    $modal: $el,
                    data: {
                        id: model.id,
                        code: model.code,
                        name: model.name,
                        contactor: model.contactor,
                        mobile: model.mobile,
                        phone: model.phone,
                        address: model.address,
                        description: model.description,
                        status: model.status,
                        region: model.region,
                        regionSupplierId: model.regionSupplierId,
                        allRegionSupplier: model.regionList,
                        supplierAbbreviation: model.supplierAbbreviation,
                        cooperativeBrandName: model.cooperativeBrandName,
                        manager: model.manager,
                        managerMobile: model.managerMobile,
                        businessManager: model.businessManager,
                        businessManagerMobile: model.businessManagerMobile,
                        openingBank: model.openingBank,
                        accountNumber: model.accountNumber,
                        taxpayerIdentificationNumber: model.taxpayerIdentificationNumber,
                        taxRegistrationCertificateImageFullUrl: model.taxRegistrationCertificateImageFullUrl,
                        taxRegistrationCertificateImageUrl: model.taxRegistrationCertificateImageUrl,
                        businessLicenseImageUrl: model.businessLicenseImageUrl,
                        businessLicenseImageFullUrl: model.businessLicenseImageFullUrl,
                        uploadUrls: {},
                        uploading: false,
                        progress: 0,
                        webUploader: {
                            type: 'taximage',
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
                        },
                        webUploader2: {
                            type: 'licenseimage',
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
                                        var da = $.extend({}, self._data, {"regionSupplier.id": self.regionSupplierId});

                                        self.$http.post('/api/supplier/saveOrUpdate', $.param(da)).then(function (res) {
                                                if (res.data.code === '1') {
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
                        },
                        deleteImg: function (url) {
                            var self = this;

                            self.$http.get('/api/upload/delete', {
                                params: {
                                    path: url
                                }
                            }).then(function (res) {
                                if (res.data.code == 1) {
                                    for (i = 0; i < self.uploadUrls.length; i++) {
                                        if (self.uploadUrls[i].key == url) {
                                            self.uploadUrls.splice(i, 1);
                                        }
                                    }

                                    if (url === self.taxRegistrationCertificateImageFullUrl) {
                                        self.taxRegistrationCertificateImageFullUrl = '';
                                        self.taxRegistrationCertificateImageUrl = '';
                                    }
                                    if (url === self.businessLicenseImageFullUrl) {
                                        self.businessLicenseImageFullUrl = '';
                                        self.businessLicenseImageUrl = '';
                                    }
                                }
                            }).finally(function () {
                            })
                        }
                    },
                    events: {
                        //税务登记图片
                        'webupload-upload-success-taximage': function (file, res) {
                            if (res.code == 1) {
                                this.taxRegistrationCertificateImageUrl = res.data.path;
                                this.taxRegistrationCertificateImageFullUrl = res.data.fullPath;
                                this.uploadUrls[res.data.fullPath] = res.data.fullPath;
                                this.$toastr.success('上传成功');
                            } else {
                                this.$toastr.error(res.message);
                            }
                        },
                        // 上传进度
                        'webupload-upload-progress-taximage': function (file, percentage) {
                            this.progress = percentage * 100;
                        },
                        // 上传结束
                        'webupload-upload-complete-taximage': function (file) {
                            this.uploading = false;
                        },
                        // 上传开始
                        'webupload-upload-start-taximage': function (file) {
                            this.uploading = true;
                        },

                        //营业执照图片
                        'webupload-upload-success-licenseimage': function (file, res) {
                            if (res.code == 1) {
                                this.businessLicenseImageUrl = res.data.path;
                                this.businessLicenseImageFullUrl = res.data.fullPath;
                                this.uploadUrls[res.data.fullPath] = res.data.fullPath;
                                this.$toastr.success('上传成功');
                            } else {
                                this.$toastr.error(res.message);
                            }
                        },
                        // 上传进度
                        'webupload-upload-progress-licenseimage': function (file, percentage) {
                            this.progress = percentage * 100;
                        },
                        // 上传结束
                        'webupload-upload-complete-licenseimage': function (file) {
                            this.uploading = false;
                        },
                        // 上传开始
                        'webupload-upload-start-licenseimage': function (file) {
                            this.uploading = true;
                        }
                    }
                });
                // 创建的Vue对象应该被返回
                return vueModal;
            });
    }
})();