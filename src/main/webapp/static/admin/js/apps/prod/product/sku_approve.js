+(function () {
    $('#applyApprove').addClass('active')
    $('#skuApprove').addClass('active')
    var vueProductList = new Vue({
        el: '#container',
        mixins: [RocoVueMixins.DataTableMixin],
        data: {
            allCatalog: null,
            allOrganization: null,
            applyStatusList: null,
            breadcrumbs: [{
                path: '/',
                name: '主页'
            },
                {
                    path: '/',
                    name: 'sku未审核',
                    active: true
                }],
            form: {
                keyword: '',
                excludeSkuIdList: '',
                catalogUrl: '',
                supplierId: ''
            },
            //修改的实体
            sku: {
                id: null,
                processStatus: null,
            },
            allStores: null,
            $dataTable: null,
            selectedRows: {},
            allSupplierId: '',
            allSuppliers: null, // 区域供应商
            suppliers: null, // 商品供应商
            allStoreCode: '',
            // 选中列
            modalModel: null,
            // 模式窗体模型
            _$el: null,
            // 自己的 el $对象
            _$dataTable: null, // datatable $对象
            skuId: ''
        },
        created: function () {
        },
        ready: function () {
            // this.activeDatepicker()
            this.fetchCategory()
            this.fetchAllStores()
        },
        methods: {
            // 商品供应商列表
            fetchSuppliers: function (id) {
                var self = this
                if (id == null || id == '' || id == undefined) {
                } else {
                    self.$http.get('/api/supplier/filterByRegionIdAndStatus?status=OPEN&regionSupplierId=' + id).then(function (res) {
                        if (res.data.code == 1) {
                            self.suppliers = res.data.data
                            if (self.productId != undefined) {
                                // self.fetchProductDetail()
                            }
                        }
                    }).catch(function () {
                    }).finally(function () {
                    })
                }
            },
            // 区域供应商
            fetchAllSuppliers: function (val) {
                var self = this
                self.$http.get('/api/regionSupplier/filterByStoreCode?storeCode=' + val).then(function (res) {
                    if (res.data.code == 1) {
                        self.allSuppliers = res.data.data
                        if (self.productId != undefined) {
                            self.fetchProductDetail()
                        }
                    }
                }).catch(function () {
                }).finally(function () {
                })
            },
            fetchAllStores: function () {
                var self = this
                self.$http.get('/api/product/store/all').then(function (res) {
                    if (res.data.code == 1) {
                        self.allStores = res.data.data
                        if (self.productId != undefined) {
                            // self.fetchProductDetail()
                        }
                    }
                }).catch(function () {
                }).finally(function () {
                })
            },
            query: function () {
                var self = this;
                self.form.allSupplierId = self.allSupplierId;
                self.form.allStoreCode = self.allStoreCode;
                this.$dataTable.bootstrapTable('selectPage', 1)
            },

            fetchCategory: function () {
                var self = this
                self.$http.get('/api/catalog/findCatalogList').then(function (res) {
                    if (res.data.code == 1) {
                        self.allCatalog = res.data.data
                        this.drawTable()
                    }
                })
            },
            drawTable: function () {
                var self = this
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/api/sku/approveList',
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
                            field: 'product',
                            title: '型号',
                            align: 'center',
                            orderable: true,
                            formatter:function (val) {
                                return val.model;
                            }
                        },
                        {
                            field: 'processStatus',
                            title: '流程状态',
                            align: 'center',
                            formatter: function (value, row) {
                                var fragment = '';
                                if (value == 'sku_supplier_audit') {
                                    fragment += '待区域供应商审核'
                                }
                                if (value == 'sku_yellow_check') {
                                    fragment += '待黄总审核'
                                }
                                if (value == 'sku_check_purchase') {
                                    fragment += '待审批门店采购价'
                                }
                                if (value == 'sku_check_sale') {
                                    fragment += '待审批门店销售价'
                                }
                                return fragment;
                            }
                        },
                        {
                            field: 'product',
                            title: '规格',
                            align: 'center',
                            orderable: true,
                            formatter:function (val) {
                                return val.spec;
                            }
                        },
                        {
                            field: 'id',
                            title: '操作',
                            width: '20%',
                            orderable: false,
                            align: 'center',
                            formatter: function (value, row) {
                                var fragment = '';
                                var processStatus = row.processStatus;
                                if (processStatus == 'sku_supplier_audit' || processStatus == 'sku_yellow_check' || processStatus == 'sku_check_purchase' || processStatus == 'sku_check_sale') {
                                    if(processStatus == 'sku_check_purchase'){
                                        fragment += ('<button   data-handle="approval" data-id="' + row.id + '"  data-flag="' + 1 + '"  type="button" class="btn btn-xs btn-primary">审批</button>&nbsp');
                                    }else if(processStatus == 'sku_check_sale'){
                                        fragment += ('<button   data-handle="approval" data-id="' + row.id + '"  data-flag="' + 0 + '"  type="button" class="btn btn-xs btn-primary">审批</button>&nbsp');
                                    }else{
                                        fragment += ('<button   data-handle="approval" data-id="' + row.id + '"   type="button" class="btn btn-xs btn-primary">审批</button>&nbsp');
                                    }
                                }
                                return fragment;
                            }
                        }
                    ]
                });
                // 审批
                self.$dataTable.on('click', '[data-handle="approval"]',
                    function (e) {
                        var flag = $(this).data('flag');
                        if(flag == 1 || flag == 0){
                            approval($(this).data('id'),flag);
                        }else {
                            approval($(this).data('id'));
                        }
                        e.stopPropagation();
                    }
                );


            },
            // end of 渲染datatable
        },
        watch: {
            'allSupplierId': function (val) {
                this.fetchSuppliers(val)
            },
            'allStoreCode': function (val) {
                this.fetchAllSuppliers(val)
            }
        }
    })
    // 审批
    function approval(id,flag) {
        var _modal = $('#approval').clone();
        var $el = _modal.modal({
            height: 600,
            maxHeight: 1000
        });
        $el.on('shown.bs.modal',
            function () {
                var el = $el.get(0);
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
                        this.acctType = acctType;
                    },
                    data: {
                        sku: null,
                        remarks: '',
                        result: 1,
                        acctType:''
                    },
                    methods: {
                        // 查询 信息
                        app: function () {
                            if (acctType === 'MATERIAL_MANAGER') {
                                var self = this;
                                self.a = '提交价格'
                                self.b = false;
                            }
                            var self = this;
                            self.$http.get('/api/sku/getInfor?id=' + id).then(function (res) {
                                if (res.data.code == 1) {
                                    self.sku = res.data.data;
                                    if (self.sku.processStatus === 'sku_supplier_audit') {
                                        self.isStore = true;
                                    }
                                    if (self.sku.processStatus === 'sku_sales_price') {
                                        self.isSale = true;
                                    }
                                }
                            }).catch(function () {
                            }).finally(function () {
                            })

                        },
                        //通过  驳回
                        subumitFun: function () {
                            var self = this;
                            var aa = {
                                id: id,
                                processStatus: '',
                                remarks: self.remarks,
                                node: '',
                                result: self.result
                            };
                            //通过
                            if (self.result == 1) {
                                //区域供货商
                                if (acctType === 'REGION_SUPPLIER') {
                                    aa.processStatus = 'sku_yellow_check';
                                    aa.node = 'sku_supplier_audit';
                                }
                                //黄总
                                if (acctType === 'CHAIRMAN_FINANCE') {
                                    aa.processStatus = 'sku_store_purchase';
                                    aa.node = 'sku_yellow_check';
                                }
                                //店总
                                if (acctType === 'STORE') {
                                    if(flag == 1){
                                        aa.processStatus = 'sku_store_sale';
                                        aa.node = 'sku_check_purchase';
                                    }else {
                                        aa.processStatus = 'sku_shelf_failure';
                                        aa.node = 'sku_check_sale';
                                    }
                                }
                            }else{
                                //区域供货商
                                if (acctType === 'REGION_SUPPLIER') {
                                    aa.processStatus = 'sku_draft';
                                    aa.node = 'sku_supplier_audit';
                                }
                                //黄总
                                if (acctType === 'CHAIRMAN_FINANCE') {
                                    aa.processStatus = 'sku_draft';
                                    aa.node = 'sku_yellow_check';
                                }
                                //店总
                                if (acctType === 'STORE') {
                                    if(flag == 1){
                                        aa.processStatus = 'sku_store_purchase';
                                        aa.node = 'sku_check_purchase';
                                    }else {
                                        aa.processStatus = 'sku_store_sale';
                                        aa.node = 'sku_check_sale';
                                    }
                                }

                            }
                            self.$http.post('/api/sku/updateProcess', aa, {emulateJSON: true}).then(function (res) {
                                    self.$toastr.success('审批成功');
                                    $el.modal('hide');
                                    vueProductList.$dataTable.bootstrapTable('refresh');
                            }).catch(function () {
                            }).finally(function () {
                            });
                        }
                    },
                    ready: function () {
                        this.app()
                    },
                });
                // 创建的Vue对象应该被返回
                return vueModal;
            });
    }

})()
