var skuModal = null;
+(function () {
    $('#applyManage').addClass('active');
    $('#productApplyList').addClass('active');
    var vueProductList = new Vue({
        el: '#container',
        mixins: [DaMeiVueMixins.DataTableMixin],
        data: {
            allCatalog: null,
            allOrganization: null,
            applyStatusList: null,
            breadcrumbs: [{
                path: '/admin',
                name: '主页'
            }, {
                path: '/',
                name: '商品管理',
                active: true
            }],
            form: {
                keyword: '',
                supplierCodeName: '',
                orgId: '',
                catalogUrl: '',
                status: '',
                applyStatus: ''
            },
            $dataTable: null,
            selectedRows: {},
            //选中列
            modalModel: null,
            //模式窗体模型
            _$el: null,
            //自己的 el $对象
            _$dataTable: null, //datatable $对象
        },
        created: function () {
            var supCodeName = this.$parseQueryString()['supplierCodeName'];
            if (supCodeName) {
                this.form.supplierCodeName = supCodeName;
            }
            this.applyStatusList = [
                {label: "请选择商品状态", value: ""},
                {label: "正常", value: "LIST"}, {label: "作废", value: "DELIST"}
            ];

        },
        ready: function () {
            // this.activeDatepicker();
            this.fetchCategory();
        },
        methods: {
            // 导入
            importExcel: function (type) {
                importModal(type);
            },
            query: function () {
                this.$dataTable.bootstrapTable('selectPage', 1);
            },
            //新增
            createProduct: function (e) {
                window.location.href = '/admin/product/edit';
                e.stopPropagation();
            },

            fetchCategory: function () {
                var self = this;
                self.$http.get('/api/catalog/findCatalogList').then(function (res) {
                    if (res.data.code == 1) {
                        self.allCatalog = res.data.data;
                        this.drawTable();
                    }
                });
            },
            showSku: function (val, status) {
                showSkuModal(val, status);
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/api/product/adminSearch',
                    method: 'get',
                    dataType: 'json',
                    cache: false,
                    //去缓存
                    pagination: true,
                    //是否分页
                    sidePagination: 'server',
                    pageSize: 10,
                    pageList: [10, 50, 100, 200],
                    //服务端分页
                    queryParams: function (params) {
                        // 将table 参数与搜索表单参数合并
                        return _.extend({},
                            params, self.form);
                    },
                    mobileResponsive: true,
                    undefinedText: '',
                    //空数据的默认显示字符
                    striped: true,
                    //斑马线
                    maintainSelected: true,
                    //维护checkbox选项
                    sortOrder: 'desc',
                    //默认排序方式
                    columns: [
                        {
                            field: "id",
                            visible: false
                        },
                        {
                            field: 'name',
                            title: '商品名称',
                            align: 'center',
                        },
                        {
                            field: 'supplier',
                            title: '供应商',
                            defaultContent: '',
                            align: 'center',
                            formatter: function (val) {
                                return val.name;
                            }
                        },
                        {
                            field: 'processStatus',
                            title: '是否可以作废',
                            align: 'center',
                            visible: false
                        },
                        {
                            field: 'catalog',
                            title: '分类名称',
                            align: 'center',
                            formatter: function (value, row, index) {
                                if (self.allCatalog) {
                                    for (var i = 0; i < self.allCatalog.length; i++) {
                                        if (self.allCatalog[i].url === value.url) {
                                            var cateName = self.allCatalog[i].name;
                                            return cateName.replace(/╋|├|&nbsp;/g, '');
                                        }
                                    }
                                }
                                return cateName;
                            }
                        },
                        {
                            field: 'measureUnitName',
                            title: '计量单位',
                            align: 'center',
                        },
                        {
                            field: 'spec',
                            title: '商品规格',
                            defaultContent: '',
                            align: 'center',
                        },
                        {
                            field: 'model',
                            title: '商品型号',
                            defaultContent: '',
                            align: 'center',
                        },
                        {
                            field: 'shelfMark',
                            title: '是否显示批量下架',
                            align: 'center',
                            visible: false
                        },
                        {
                            field: 'status',
                            title: '商品状态',
                            align: 'center',
                            formatter: function (value, row, index) {
                                switch (value) {
                                    case "LIST":
                                        return "正常";
                                        break;
                                    case "DELIST":
                                        return "已作废";
                                        break;
                                }
                            }
                        },
                        {
                            field: '',
                            title: '操作',
                            align: 'center',
                            align: 'center',
                            orderable: false,
                            formatter: function (value, row, index) {
                                var html = '';
                                if (DaMeiUtils.hasPermission('product:edit')) {
                                    //可编辑
                                    if (acctType == 'PROD_SUPPLIER' && row.processStatus == '1' && row.status == 'LIST') {
                                        html += '<button data-handle="edit-click" data-id="' + row.id + '" type="button" class="m-r-xs btn btn-xs btn-primary">编辑</button>';
                                        // ke作废
                                        html += '<button data-handle="edit-void" data-id="' + row.id + '" type="button" class="m-r-xs btn btn-xs btn-danger">作废</button>';
                                    }
                                    html += '<button data-handle="edit-sku_manager" data-id="' + row.id + '"  data-status="' + row.status + '"  type="button" class="m-r-xs btn btn-xs btn-primary">SKU管理</button>';
                                }
                                if (acctType == 'MATERIAL_MANAGER' && row.shelfMark == '1') {
                                    html += '<button data-handle="edit-sku" data-id="' + row.id + '" type="button" class="m-r-xs btn btn-xs btn-primary">批量下架sku</button>';
                                }
                                return html;
                            }
                        }]
                });
                // 管理sku
                self.$dataTable.on('click', '[data-handle="edit-sku_manager"]',
                    function (e) {
                        var productId = $(this).data('id');
                        var status = $(this).data('status');
                        self.showSku(productId, status);
                    });
                // 批量下架sku
                self.$dataTable.on('click', '[data-handle="edit-sku"]',
                    function (e) {
                        var productId = $(this).data('id');
                        swal({
                            title: '批量下架sku',
                            text: '您确认批量下架?',
                            type: 'info',
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            showCancelButton: true,
                            showConfirmButton: true,
                            showLoaderOnConfirm: true,
                            confirmButtonColor: '#ed5565'
                        }, function () {
                            self.$http.get('/api/product/' + productId + '/batchSku/2').then(function (resp) {
                                if (resp.data.code == 1) {
                                    Vue.toastr.success(resp.data.message);
                                    self.$dataTable.bootstrapTable('refresh');
                                }
                            });
                        });
                        e.stopPropagation();
                    });
                // 作废
                self.$dataTable.on('click', '[data-handle="edit-void"]',
                    function (e) {
                        var productId = $(this).data('id');
                        swal({
                            title: '作废商品',
                            text: '您确认作废商品?',
                            type: 'info',
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            showCancelButton: true,
                            showConfirmButton: true,
                            showLoaderOnConfirm: true,
                            confirmButtonColor: '#ed5565'
                        }, function () {
                            self.$http.get('/api/product/' + productId + '/batchSku/1').then(function (resp) {
                                if (resp.data.code == 1) {
                                    Vue.toastr.success(resp.data.message);
                                    self.$dataTable.bootstrapTable('refresh');
                                }
                            });
                        });
                        e.stopPropagation();
                    });
                //编辑
                self.$dataTable.on('click', '[data-handle="edit-click"]',
                    function (e) {
                        var productId = $(this).data("id");
                        window.location.href = '/admin/product/edit?productId=' + productId;
                        e.stopPropagation();
                    });
            }
            //end of 渲染datatable
        }
    });

    // 导入
    function importModal(model, isEdit) {
        var _modal = $('#modal').clone();
        var $el = _modal.modal({
            height: 250,
            maxHeight: 500
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
                    mixins: [DaMeiVueMixins.ModalMixin],
                    components: {
                        'web-uploader': DaMeiVueComponents.WebUploaderComponent
                    },
                    $modal: $el,
                    created: function () {
                    },
                    data: {
                        id: model.id,
                        name: model.name,
                        logo: model.logo,
                        linkUrl: model.logo,
                        uploading: false,
                        progress: 0,
                        webUploader: {
                            type: 'bannerfile',
                            formData: {
                                type: 'BRAND'
                            },
                            server: ctx + '/api/product/import',
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
                            iframe.src = '/api/product/downloadTemplate';
                        },
                    },
                    events: {
                        // 上传成功
                        'webupload-upload-success-bannerfile': function (file, res) {
                            if (res.code == 1) {
                                this.$toastr.success('商品导入成功');
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

    };

    // 管理sku 界面
    function showSkuModal(productId, status) {
        var _modal = $('#manageSku').clone();
        var $el = _modal.modal({
            height: 600,
            maxHeight: 800,
            width: 1300
        });
        $el.on('shown.bs.modal',
            function () {
                var el = $el.get(0);
                skuModal = new Vue({
                    el: el,
                    $modal: $el,
                    created: function () {
                    },
                    data: {
                        status: status,
                        addFlag: status == 'DELIST' ? false : true,
                        form: {
                            keyword: null,
                            productId: productId
                        }
                    },
                    ready: function () {
                        this.drawTable();
                    },
                    methods: {
                        addSku: function () {
                            addSkuModal(productId);
                        },
                        query: function () {
                            var self = this;
                            this.$dataTable.bootstrapTable('selectPage', 1)
                        },
                        drawTable: function () {
                            var self = this
                            self.$dataTable = $('#skuTable', self._$el).bootstrapTable({
                                url: '/api/sku/adminSearch',
                                method: 'get',
                                dataType: 'json',
                                cache: false,
                                // 去缓存
                                pagination: true,
                                // 是否分页
                                sidePagination: 'server',
                                pageSize: 10,
                                pageList: [10, 50, 100, 200],
                                // 服务端分页
                                queryParams: function (params) {
                                    // 将table 参数与搜索表单参数合并
                                    return _.extend({},
                                        params, self.form)
                                },
                                mobileResponsive: true,
                                undefinedText: '',
                                // 空数据的默认显示字符
                                striped: true,
                                // 斑马线
                                maintainSelected: true,
                                // 维护checkbox选项
                                sortOrder: 'desc',
                                // 默认排序方式
                                columns: [
                                    {
                                        field: 'code',
                                        title: 'sku编码',
                                        align: 'center',
                                        orderable: true
                                    },
                                    {
                                        field: 'name',
                                        title: 'sku名称',
                                        align: 'center',
                                        orderable: true
                                    },
                                    {
                                        field: 'attribute1',
                                        title: '属性值1',
                                        align: 'center',
                                        orderable: true
                                    },
                                    {
                                        field: 'attribute2',
                                        title: '属性值2',
                                        align: 'center',
                                        orderable: true
                                    },
                                    {
                                        field: 'attribute3',
                                        title: '属性值3',
                                        align: 'center',
                                        orderable: true
                                    },
                                    {
                                        field: 'stock',
                                        title: '库存量',
                                        align: 'center',
                                        orderable: true
                                    },
                                    {
                                        field: 'product',
                                        title: '型号',
                                        align: 'center',
                                        orderable: true,
                                        formatter: function (val) {
                                            return val.model;
                                        }
                                    },
                                    {
                                        field: 'product',
                                        title: '规格',
                                        align: 'center',
                                        orderable: true,
                                        formatter: function (val) {
                                            return val.spec;
                                        }
                                    },
                                    {
                                        field: 'priceFlag',
                                        title: '价格标记',
                                        align: 'center',
                                        orderable: true,
                                        visible: false
                                    },
                                    {
                                        field: 'priceFlag',
                                        title: '网真采购价',
                                        align: 'center',
                                        orderable: true,
                                        formatter: function (value, row) {
                                            var label = '';
                                            if (value != undefined && value.indexOf("SUPPLY") >= 0) {
                                                label = '已填写';
                                            } else {
                                                label = '未填写';
                                            }
                                            return label;
                                        }
                                    },
                                    {
                                        field: 'priceFlag',
                                        title: '门店采购价',
                                        align: 'center',
                                        orderable: true,
                                        formatter: function (value, row) {
                                            var label = '';
                                            if (value != undefined && value.indexOf("STORE") >= 0) {
                                                label = '已填写';
                                            } else {
                                                label = '未填写';
                                            }
                                            return label;
                                        }
                                    },
                                    {
                                        field: 'priceFlag',
                                        title: '门店销售价',
                                        align: 'center',
                                        orderable: true,
                                        formatter: function (value, row) {
                                            var label = '';
                                            if (value != undefined && value.indexOf("SALE") >= 0) {
                                                label = '已填写';
                                            } else {
                                                label = '未填写';
                                            }
                                            return label;
                                        }

                                    },
                                    {
                                        field: 'processStatus',
                                        title: '状态',
                                        align: 'center',
                                        orderable: true,
                                        formatter: function (value, row) {
                                            var label = '';
                                            switch (value) {
                                                case 'sku_draft':
                                                    label = '<font color="blue">草稿</font>';
                                                    break;
                                                case 'sku_supplier_audit':
                                                    label = '待区域供应商审核';
                                                    break;
                                                case 'sku_yellow_check':
                                                    label = '待黄总审核';
                                                    break;
                                                case 'sku_store_purchase':
                                                    label = '待填写门店采购价';
                                                    break;
                                                case 'sku_check_purchase':
                                                    label = '待审批门店采购价';
                                                    break;
                                                case 'sku_store_sale':
                                                    label = '待填写门店门店销售价';
                                                    break;
                                                case 'sku_check_sale':
                                                    label = '待审批门店门店销售价';
                                                    break;
                                                case 'sku_shelf_failure':
                                                    label = '<font color="red">未上架</font>';
                                                    break;
                                                case 'sku_shelf_shelves':
                                                    label = '<font color="green">已上架</font>';
                                                    break;
                                                case 'sku_void':
                                                    label = '作废';
                                                    break;
                                                default:
                                                    break;
                                            }
                                            return label;
                                        }
                                    },
                                    {
                                        field: 'id',
                                        title: '操作',
                                        width: '20%',
                                        orderable: false,
                                        align: 'center',
                                        formatter: function (value, row) {
                                            var html = '';
                                            var class_val = "m-r-xs btn btn-xs btn-danger";
                                            var nostatus = '';
                                            if (acctType == 'PROD_SUPPLIER' && row.processStatus != 'sku_void' && (row.processStatus == 'sku_draft')) {
                                                html += '<button style="margin-left:10px;"';
                                                html += 'data-handle="set"';
                                                html += 'data-id="' + value + '"';
                                                html += 'class="m-r-xs btn btn-xs btn-primary" type="button">作废</button>';
                                            }
                                            return html;
                                        }
                                    }
                                ]
                            })
                            // 作废
                            self.$dataTable.on('click', '[data-handle="set"]',
                                function (e) {
                                    var id = $(this).data('id')
                                    swal({
                                            title: '作废SKU',
                                            text: '确定作废SKU吗？',
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
                                            self.$http.get('/api/sku/deleteSku/' + id).then(function (res) {
                                                if (res.data.code == 1) {
                                                    Vue.toastr.success(res.data.message);
                                                    skuModal.$dataTable.bootstrapTable('selectPage', 1);
                                                    skuModal.$dataTable.bootstrapTable('refresh');
                                                }
                                            }).catch(function () {

                                            }).finally(function () {
                                                swal.close();
                                            });
                                        });

                                    e.stopPropagation();
                                })
                        },
                        // end of 渲染datatable
                    }
                });

                // 创建的Vue对象应该被返回
                return skuModal;
            });

    };
    Vue.validator('stock', function (val) {
        var g = /^[1-9]*[1-9][0-9]*$/;
        return g.test(val);
    })
    // sku添加 界面
    function addSkuModal(productId) {
        var _modal = $('#addSkuModel').clone();
        var $el = _modal.modal({
            height: 300,
            maxHeight: 600,
            width: 600
        });
        $el.on('shown.bs.modal',
            function () {
                var el = $el.get(0);
                var addSkuModal = new Vue({
                    el: el,
                    $modal: $el,
                    created: function () {
                        this.getMate();
                    },
                    components: {
                        'web-uploader': DaMeiVueComponents.WebUploaderComponent
                    },
                    data: {
                        //删除的路径
                        path: null,
                        sku: {
                            //展示的路径
                            fullPath: null,
                            productId: productId,
                            attribute1: null,
                            attribute2: null,
                            attribute3: null,
                            stock: null
                        },
                        mate: {
                            attribute1Name: null,
                            attribute2Name: null,
                            attribute3Name: null
                        },
                        webUploaderMain: {
                            sku: 'forSku',
                            type: 'main',
                            formData: {
                                type: 'PRODUCT'
                            },
                            accept: {
                                title: 'Images',
                                extendsions: 'gif,jpg,jpeg,bmp,png',
                                mimeTypes: 'image/*' //'image/jpg,image/png,image/gif,image/jpeg,image/bmp'
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
                    },
                    ready: function () {

                    },
                    methods: {
                        getMate: function () {
                            var self = this;
                            self.$http.get('/api/sku//getMate/' + productId).then(function (res) {
                                if (res.data.code == 1) {
                                    self.mate = res.data.data;
                                }
                            });
                        },
                        // 删除图片
                        deleteImg: function (url, index, type) {
                            var self = this;
                            self.$http.get('/api/upload/delete', {
                                params: {
                                    path: url
                                }
                            }).then(function (res) {
                                if (res.data.code == 1) {
                                    self.sku.fullPath = null;
                                    self.path = null;
                                    this.$toastr.success('删除成功');

                                }
                            }).finally(function () {
                            })
                        },
                        submitAdd: function () {
                            var self = this;
                            self.$validate(true, function () {
                                if (self.$validation.valid) {
                                    self.$http.post('/api/sku/saveSku', self.sku, {emulateJSON: true}).then(function (res) {
                                            if (res.data.code === '1') {
                                                Vue.toastr.success(res.data.message);
                                                $el.modal('hide');
                                                skuModal.$dataTable.bootstrapTable('selectPage', 1);
                                                skuModal.$dataTable.bootstrapTable('refresh');
                                                self.$destroy();
                                            }
                                        },
                                        function (error) {
                                            Vue.toastr.error(error.responseText);
                                        }).catch(function () {
                                    }).finally(function () {

                                    });
                                }
                            })
                        },
                    },
                    events: {
                        'webupload-upload-success-main': function (file, res) {
                            var self = this;
                            if (res.code == 1) {
                                self.sku.fullPath = ctx + res.data.fullPath;
                                self.path = ctx + res.data.path;
                                this.$toastr.success('上传成功');
                            } else {
                                this.$toastr.error(res.message);
                            }
                        },
                    }
                });
                // 创建的Vue对象应该被返回
                return addSkuModal;
            });

    }
})();