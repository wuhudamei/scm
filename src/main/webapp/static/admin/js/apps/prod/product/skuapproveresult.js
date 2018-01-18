+(function () {
    $('#applyApprove').addClass('active')
    $('#skuApproveResult').addClass('active')
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
                    name: 'sku已审核',
                    active: true
                }],
            form: {
                keyword: '',
                excludeSkuIdList: '',
                catalogUrl: '',
                supplierId: ''
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
                if(id==null || id==''||id==undefined ){
                }else {
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
                var self=this;
                self.form.allSupplierId=self.allSupplierId;
                self.form.allStoreCode=self.allStoreCode;
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
                    url: '/api/sku/checkedList',
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
                                    fragment += ('<button   data-handle="approval" data-id="' + row.id + '"  type="button" class="btn btn-xs btn-primary">查看审批记录</button>&nbsp');
                                 return fragment;
                            }
                        }
                    ]
                });
                // 查看审批记录
                self.$dataTable.on('click', '[data-handle="approval"]',
                    function (e) {
                       var id = $(this).data('id');
                        approval(id);
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
    // 查看审批记录
    function approval(id) {
        var _modal = $('#approval').clone();
        var $el = _modal.modal({
            height: 400,
            maxHeight: 600
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
                    },
                    filters:{
                        result: function (value) {
                            var label = '';
                            if(value == 1 ){
                                label = '<font color="green">通过</font>';
                                return label;
                            }else {
                                label = '<font color="red">驳回</font>';
                                return label;
                            }
                        },
                        node:function (value) {
                            var label = '';
                            switch (value) {
                                case 'sku_draft':
                                    label = '草稿';
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
                                    label = '待填写门店销售价';
                                    break;
                                case 'sku_check_sale':
                                    label = '待审批门店销售价';
                                    break;
                                case 'sku_shelf_failure':
                                    label = '未上架';
                                    break;
                                case 'sku_shelf_shelves':
                                    label = '上架';
                                    break;
                                default:
                                    break;
                            }
                            return label;
                        }
                    },
                    data: {
                        recordList:null
                    },
                    methods: {
                        // 查询 信息
                        app: function () {
                            var self=this;
                            self.$http.get('/api/sku/recordList?skuId='+id).then(function (res) {
                                if (res.data.code == 1) {
                                    self.recordList=res.data.data;
                                }
                            }).catch(function () {
                            }).finally(function () {
                            })

                        },
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
