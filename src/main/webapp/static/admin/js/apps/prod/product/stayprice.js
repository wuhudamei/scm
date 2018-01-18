+(function () {
    $('#applyApprove').addClass('active')
    $('#stayPrice').addClass('active')
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
                    name: '待设置价格',
                    active: true
                }],
            form: {
                keyword: '',
                excludeSkuIdList: '',
                catalogUrl: '',
                //商品供货商
                supplierId: '',
                //区域供货商
                allSupplierId: '',
                //门店
                allStoreCode: ''
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
            this.fetchCategory();
            this.fetchAllStores();
        },
        methods: {
            // 商品供应商列表
            fetchSuppliers: function (id) {
                var self = this;
                if (id == null || id == '' || id == undefined) {
                } else {
                    self.$http.get('/api/supplier/filterByRegionIdAndStatus?regionSupplierId=' + id).then(function (res) {
                        if (res.data.code == 1) {
                            self.suppliers = res.data.data;
                            if (self.productId != undefined) {
                                // self.fetchProductDetail()
                            }
                        }
                    }).catch(function () {
                    }).finally(function () {
                    });
                }
            },
            // 区域供应商
            fetchAllSuppliers: function (val) {
                var self = this;
                self.$http.get('/api/regionSupplier/filterByStoreCode?storeCode=' + val).then(function (res) {
                    if (res.data.code == 1) {
                        self.allSuppliers = res.data.data;
                        if (self.productId != undefined) {
                            self.fetchProductDetail();
                        }
                    }
                }).catch(function () {
                }).finally(function () {
                })
            },
            fetchAllStores: function () {
                var self = this;
                self.$http.get('/api/product/store/all').then(function (res) {
                    if (res.data.code == 1) {
                        self.allStores = res.data.data;
                        if (self.productId != undefined) {
                            // self.fetchProductDetail();
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
                var self = this;
                self.$http.get('/api/catalog/findCatalogList').then(function (res) {
                    if (res.data.code == 1) {
                        self.allCatalog = res.data.data;
                        this.drawTable();
                    }
                })
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/api/sku/priceList',
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
                        },{
                            field: 'product',
                            title: '品牌',
                            align: 'center',
                            orderable: true,
                            formatter: function (val) {
                                return val.brand.brandName;
                            }
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
                                    case 'sku_store_purchase':
                                        label = '<font color="blue">待填写门店采购价</font>';
                                        break;
                                    case 'sku_store_sale':
                                        label = '<font color="green">待填写门店销售价</font>';
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
                                if (acctType != 'MATERIAL_CLERK') {
                                    html += '<button style="margin-left:10px;"';
                                    html += 'data-handle="set"';
                                    html += 'data-id="' + value + '"';
                                    html += 'class="m-r-xs btn btn-xs btn-primary" type="button">设置价格</button>';
                                }
                                if (acctType != 'MATERIAL_CLERK') {
                                    html += '<button style="margin-left:10px;"';
                                    html += 'data-handle="refer"';
                                    html += 'data-id="' + value + '"';
                                    html += 'data-status="' + row.processStatus + '"';
                                    html += 'data-price-flag="' + row.priceFlag + '"';
                                    html += 'class="m-r-xs btn btn-xs btn-primary" type="button">提交</button>';
                                }
                                if (acctType == 'REGION_SUPPLIER' || acctType == 'MATERIAL_MANAGER') {
                                    html += ('<button   data-handle="reject" data-id="' + row.id + '"  type="button" class="btn btn-xs btn-primary">查看审批记录</button>&nbsp');
                                }
                                return html;
                            }
                        }
                    ]
                });
                // 设置价格
                self.$dataTable.on('click', '[data-handle="set"]',
                    function (e) {
                        var id = $(this).data('id');
                        self.skuId = id;
                        priceModal(id);
                        e.stopPropagation();
                    }
                );
                // 查看驳回记录
                self.$dataTable.on('click', '[data-handle="reject"]',
                    function (e) {
                        var id = $(this).data('id');
                        reject(id);
                    }
                );
                //提交
                self.$dataTable.on('click', '[data-handle="refer"]',
                    function (e) {
                        var id = $(this).data('id');
                        var status = $(this).data('status');
                        var priceFlag = $(this).data('price-flag');

                        var flag = true;
                        var title = "确定提交吗?";
                        var text = "";
                        console.log(priceFlag);
                        if (!priceFlag || priceFlag.indexOf("SUPPLY") == -1) {
                            title = "警告:";
                            text = "请先填写网真采购价!";
                            flag = false;
                        } else if (acctType == "REGION_SUPPLIER" && priceFlag.indexOf("STORE") == -1) {
                            title = "警告:";
                            text = "请先填写门店采购价!";
                            flag = false;
                        } else if (acctType == "MATERIAL_MANAGER" && priceFlag.indexOf("SALE") == -1) {
                            title = "警告:";
                            text = "请先填写门店销售价!";
                            flag = false;
                        }
                        //未填写 相应价格
                        if (flag) {
                            swal({
                                    title: title,
                                    text: text,
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
                                    var aa = {
                                        skuId: id,
                                        processStatus: null
                                    };
                                    if (status == 'sku_store_purchase') {
                                        aa.processStatus = 'sku_check_purchase';
                                    } else if (status == 'sku_store_sale') {
                                        aa.processStatus = 'sku_check_sale';
                                    }
                                    self.$http.post('/api/sku/updateSkuStatus', aa, {emulateJSON: true}).then(function (res) {
                                        self.$toastr.success('操作成功');
                                        vueProductList.$dataTable.bootstrapTable('refresh');
                                    }).catch(function () {
                                    }).finally(function () {
                                        swal.close();
                                    })
                                });
                            return;
                        }
                        swal({
                            title: title,
                            text: text,
                            type: 'info',
                            confirmButtonText: '确定',
                            showCancelButton: false,
                            showConfirmButton: true,
                            showLoaderOnConfirm: true,
                            confirmButtonColor: '#ed5565',
                            closeOnConfirm: false
                        });
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

    // 设置价格
    function priceModal(id) {
        var $modal = $('#priceModal').clone();
        $modal.modal({
            height: 400,
            maxHeight: 500,
            width: 600
        });
        $modal.on('shown.bs.modal',
            function () {
                vueModal2 = new Vue({
                    el: $modal.get(0),
                    mixins: [RocoVueMixins.DataTableMixin],
                    data: {
                        form: {
                            keyword: '',
                            declareCode: null,
                            orgId: null,
                            declareStartDate: null,
                            declareEndDate: null
                        },
                        id: id,
                        current: 'aConfig', // 当前tab
                        aKeyword: '', // a关键字
                        bKeyword: '', // b关键字
                        cKeyword: '', // c关键字
                        $aDataTable: null, // A表格
                        $bDataTable: null, // B表格
                        $cDataTable: null, // C表格
                        $dataTable: null,
                        selectedRows: {},
                        // 选中列
                        modalModel: null,
                        // 模式窗体模型
                        _$el: null,
                        aControl: 0, // 是否可以新增 0 不可以 1 可以
                        bControl: 0, // 是否可以新增 0 不可以 1 可以
                        cControl: 0, // 是否可以新增 0 不可以 1 可以
                        aShow: 0, // 是否可以显示tab 0 不可以 1 可以
                        bShow: 0, // 是否可以显示tab 0 不可以 1 可以
                        cShow: 0 // 是否可以显示tab 0 不可以 1 可以
                        // 自己的 el $对象
                    },
                    created: function () {
                    },
                    ready: function () {
                        // 0 表示只能查看 1 表示可以查看、新增、编辑
                        switch (acctType) {
                            // 管理员
                            case 'MATERIAL_MANAGER':
                                this.aTable(1);
                                this.bTable(1);
                                this.cTable(1);
                                this.aControl = 0;
                                this.bControl = 0;
                                this.cControl = 1;
                                this.aShow = 1;
                                this.bShow = 1;
                                this.cShow = 1;
                                break;
                            // 区域
                            case 'REGION_SUPPLIER':
                                this.aTable(0);
                                this.bTable(1);
                                this.aControl = 0;
                                this.bControl = 1;
                                this.cControl = 0;
                                this.aShow = 1;
                                this.bShow = 1;
                                this.cShow = 0;
                                break;
                            // 商品
                            case 'PROD_SUPPLIER':
                                this.aTable(1);
                                this.aControl = 1;
                                this.bControl = 0;
                                this.cControl = 0;
                                this.aShow = 1;
                                this.bShow = 0;
                                this.cShow = 0;
                                break;
                            // 门店
                            case 'STORE':
                                this.current = 'bConfig';
                                this.bTable(0);
                                this.cTable(1);
                                this.aControl = 0;
                                this.bControl = 0;
                                this.cControl = 1;
                                this.aShow = 0;
                                this.bShow = 1;
                                this.cShow = 1;
                                break;
                        }
                    },
                    methods: {
                        createBtnClickHandler: function (priceType) {
                            createOrEditModal({priceType: priceType}, false)
                        },

                        // tab切换
                        tab: function (config) {
                            this.current = config
                        },
                        // A表格
                        aTable: function (control) {
                            var self = this;
                            self.$aDataTable = $('#aTable', self._$el).bootstrapTable({
                                url: '/api/product/price/list',
                                method: 'get',
                                dataType: 'json',
                                cache: false,
                                pagination: true,
                                sidePagination: 'server',
                                queryParams: function (params) {
                                    return _.extend({}, params, {priceType: 'SUPPLY', skuId: id})
                                },
                                mobileResponsive: true,
                                undefinedText: '',
                                striped: true,
                                maintainSelected: true,
                                sortOrder: 'ASC',
                                columns: [
                                    {
                                        field: 'priceStartDate',
                                        title: '生效时间',
                                        align: 'center'
                                    },
                                    {
                                        field: 'price',
                                        title: '价格',
                                        align: 'center',
                                        formatter: function (value) {
                                            return '¥' + value
                                        }
                                    },
                                    {
                                        field: 'editor',
                                        title: '操作人',
                                        align: 'center',
                                        formatter: function (value) {
                                            return value.name
                                        }
                                    },
                                    {
                                        field: 'editTime',
                                        title: '操作时间',
                                        align: 'center'
                                    },
                                    {
                                        field: 'id',
                                        title: '操作',
                                        className: 'td_center',
                                        orderable: false,
                                        align: 'center',
                                        formatter: function (value, row, index) {
                                            var html = '';
                                            if (row.editable == true && control == '1' && acctType == 'PROD_SUPPLIER') {
                                                html += '<button style="margin-left:10px;"';
                                                html += 'data-handle="aEdit"';
                                                html += 'data-priceStartDate="' + row.priceStartDate + '"';
                                                html += 'data-price="' + row.price + '"'
                                                html += 'data-priceType="' + row.priceType + '"';
                                                html += 'data-id="' + row.id + '"';
                                                html += 'class="m-r-xs btn btn-xs btn-primary" type="button">编辑</button>';
                                                html += '<button style="margin-left:10px;" data-handle="delete" data-id="' + value + '" class="m-r-xs btn btn-xs btn-primary" type="button">删除</button>';
                                            }
                                            return html
                                        }
                                    }]
                            })
                            // 编辑
                            self.$aDataTable.on('click', '[data-handle="aEdit"]',
                                function (e) {
                                    var model = $(this).data();
                                    model.priceStartDate = model.pricestartdate;
                                    model.priceType = model.pricetype;
                                    createOrEditModal(model, true);
                                    e.stopPropagation()
                                });

                            // 删除
                            self.$aDataTable.on('click', '[data-handle="delete"]',
                                function (e) {
                                    var id = $(this).data('id');
                                    swal({
                                            title: '',
                                            text: '确定删除吗？',
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
                                            vueModal2.$http.post('/api/product/price/' + id + '/del').then(function (res) {
                                                if (res.data.code == 1) {
                                                    Vue.toastr.success(res.data.message);
                                                    self.$aDataTable.bootstrapTable('refresh');
                                                }
                                            }).catch(function () {
                                            }).finally(function () {
                                                swal.close();
                                            })
                                        });
                                    e.stopPropagation();
                                })
                        },
                        // B表格
                        bTable: function (control) {
                            var self = this;
                            self.$bDataTable = $('#bTable', self._$el).bootstrapTable({
                                url: '/api/product/price/list',
                                method: 'get',
                                dataType: 'json',
                                cache: false,
                                pagination: true,
                                sidePagination: 'server',
                                queryParams: function (params) {
                                    return _.extend({}, params, {priceType: 'STORE', skuId: id})
                                },
                                mobileResponsive: true,
                                undefinedText: '',
                                striped: true,
                                maintainSelected: true,
                                sortOrder: 'ASC',
                                columns: [
                                    {
                                        field: 'priceStartDate',
                                        title: '生效时间',
                                        align: 'center'
                                    },
                                    {
                                        field: 'price',
                                        title: '价格',
                                        align: 'center',
                                        formatter: function (value) {
                                            return '¥' + value
                                        }
                                    },
                                    {
                                        field: 'editor',
                                        title: '操作人',
                                        align: 'center',
                                        formatter: function (value) {
                                            return value.name
                                        }
                                    },
                                    {
                                        field: 'editTime',
                                        title: '操作时间',
                                        align: 'center'
                                    },
                                    {
                                        field: 'id',
                                        title: '操作',
                                        className: 'td_center',
                                        orderable: false,
                                        align: 'center',
                                        formatter: function (value, row, index) {
                                            var html = '';
                                            if (control == '1' && acctType == 'REGION_SUPPLIER') {
                                                html += '<button style="margin-left:10px;"'
                                                html += 'data-handle="bEdit"'
                                                html += 'data-priceStartDate="' + row.priceStartDate + '"'
                                                html += 'data-price="' + row.price + '"'
                                                html += 'data-priceType="' + row.priceType + '"'
                                                html += 'data-id="' + row.id + '"';
                                                html += 'class="m-r-xs btn btn-xs btn-primary" type="button">编辑</button>';
                                                html += '<button style="margin-left:10px;" data-handle="delete" data-id="' + value + '" class="m-r-xs btn btn-xs btn-primary" type="button">删除</button>';
                                            }
                                            return html;
                                        }
                                    }]
                            });
                            // 编辑
                            self.$bDataTable.on('click', '[data-handle="bEdit"]',
                                function (e) {
                                    var model = $(this).data();
                                    model.priceStartDate = model.pricestartdate;
                                    model.priceType = model.pricetype;
                                    createOrEditModal(model, true);
                                    e.stopPropagation();
                                });

                            // 删除
                            self.$bDataTable.on('click', '[data-handle="delete"]',
                                function (e) {
                                    var id = $(this).data('id');
                                    swal({
                                            title: '',
                                            text: '确定删除吗？',
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
                                            vueModal2.$http.post('/api/product/price/' + id + '/del').then(function (res) {
                                                if (res.data.code == 1) {
                                                    Vue.toastr.success(res.data.message);
                                                    self.$bDataTable.bootstrapTable('selectPage', 1);
                                                }
                                            }).catch(function () {
                                            }).finally(function () {
                                                swal.close();
                                            })
                                        });
                                    e.stopPropagation();
                                })
                        },
                        // C表格
                        cTable: function (control) {
                            var self = this;
                            self.$cDataTable = $('#cTable', self._$el).bootstrapTable({
                                url: '/api/product/price/list',
                                method: 'get',
                                dataType: 'json',
                                cache: false,
                                pagination: true,
                                sidePagination: 'server',
                                queryParams: function (params) {
                                    return _.extend({}, params, {priceType: 'SALE', skuId: id})
                                },
                                mobileResponsive: true,
                                undefinedText: '',
                                striped: true,
                                maintainSelected: true,
                                sortOrder: 'ASC',
                                columns: [
                                    {
                                        field: 'priceStartDate',
                                        title: '生效时间',
                                        align: 'center'
                                    },
                                    {
                                        field: 'price',
                                        title: '价格',
                                        align: 'center',
                                        formatter: function (value) {
                                            return '¥' + value
                                        }
                                    },
                                    {
                                        field: 'editor',
                                        title: '操作人',
                                        align: 'center',
                                        formatter: function (value) {
                                            return value.name
                                        }
                                    },
                                    {
                                        field: 'editTime',
                                        title: '操作时间',
                                        align: 'center'
                                    },
                                    {
                                        field: 'id',
                                        title: '操作',
                                        className: 'td_center',
                                        orderable: false,
                                        align: 'center',
                                        formatter: function (value, row, index) {
                                            var html = '';
                                            if (control == '1' && acctType == 'MATERIAL_MANAGER') {
                                                html += '<button style="margin-left:10px;"';
                                                html += 'data-handle="cEdit"';
                                                html += 'data-priceStartDate="' + row.priceStartDate + '"';
                                                html += 'data-price="' + row.price + '"';
                                                html += 'data-priceType="' + row.priceType + '"';
                                                html += 'data-id="' + row.id + '"';
                                                html += 'class="m-r-xs btn btn-xs btn-primary" type="button">编辑</button>';
                                                html += '<button style="margin-left:10px;" data-handle="delete" data-id="' + value + '" class="m-r-xs btn btn-xs btn-primary" type="button">删除</button>';
                                            }
                                            return html;
                                        }
                                    }]
                            });
                            // 编辑
                            self.$cDataTable.on('click', '[data-handle="cEdit"]',
                                function (e) {
                                    var model = $(this).data();
                                    model.priceStartDate = model.pricestartdate;
                                    model.priceType = model.pricetype;
                                    createOrEditModal(model, true);
                                    e.stopPropagation();
                                });

                            // 删除
                            self.$cDataTable.on('click', '[data-handle="delete"]',
                                function (e) {
                                    var id = $(this).data('id');
                                    swal({
                                            title: '',
                                            text: '确定删除吗？',
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
                                            vueModal2.$http.post('/api/product/price/' + id + '/del').then(function (res) {
                                                if (res.data.code == 1) {
                                                    Vue.toastr.success(res.data.message);
                                                    self.$cDataTable.bootstrapTable('selectPage', 1);
                                                }
                                            }).catch(function () {
                                            }).finally(function () {
                                                swal.close();
                                            })
                                        });
                                    e.stopPropagation();
                                })
                        },
                        closeFrame: function () {
                            $modal.modal('hide');
                            vueProductList.$dataTable.bootstrapTable('refresh');
                        }
                    }
                })
            })
    }

    //  新增／编辑价格
    function createOrEditModal(model, isEdit) {
        var _modal = $('#contractModal').clone();
        var atableData = vueModal2.$aDataTable.bootstrapTable('getData');
        var btableData = vueModal2.$bDataTable.bootstrapTable('getData');
        if(vueModal2.$cDataTable){
            var ctableData = vueModal2.$cDataTable.bootstrapTable('getData');
        }
        var $el = _modal.modal({
            maxHeight: 600,
            width: 400
        });
        $el.on('shown.bs.modal',
            function () {
                var el = $el.get(0)
                isEdit = !!isEdit
                var vueModal = new Vue({
                    el: el,
                    http: {
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                    },
                    mixins: [RocoVueMixins.ModalMixin],
                    $modal: $el,
                    data: {
                        id: model.id,
                        priceStartDate: model.priceStartDate,
                        price: model.price,
                        skuId: vueProductList.skuId,
                        priceType: model.priceType,
                        type: '价格',
                        blank: false
                    },
                    ready: function () {
                        $('#timepick', this._$el).datetimepicker({
                            startDate: new Date()
                        })
                    },
                    created: function () {
                        switch (this.priceType) {
                            case 'SUPPLY':
                                this.type = '网真采购价';
                                break;
                            case 'STORE':
                                this.type = '门店采购价';
                                break;
                            case 'SALE':
                                this.type = '门店销售价';
                                break;
                        }
                        if (atableData.length == 0) {
                            this.blank = true;
                            this.priceStartDate = moment().format('YYYY-MM-DD');
                        }
                        if (btableData.length == 0) {
                            this.blank = true;
                            this.priceStartDate = moment().format('YYYY-MM-DD');
                        }
                        if (vueModal2.$cDataTable && ctableData.length == 0) {
                            this.blank = true;
                            this.priceStartDate = moment().format('YYYY-MM-DD');
                        }
                    },
                    methods: {
                        savePrice: function () {
                            var self = this;
                            self.$validate(true,
                                function () {
                                    if (self.$validation.valid) {
                                        self.submitting = true;
                                        delete self._data.type;
                                        self.$http.post('/api/product/price/saveOrUpdate', $.param(self._data)).then(function (res) {
                                                if (res.data.code === '1') {
                                                    Vue.toastr.success(res.data.message);
                                                    $el.modal('hide');
                                                    if (vueModal2.current == 'aConfig') {
                                                        vueModal2.$aDataTable.bootstrapTable('refresh');
                                                    } else if (vueModal2.current == 'bConfig') {
                                                        vueModal2.$bDataTable.bootstrapTable('refresh');
                                                    } else {
                                                        vueModal2.$cDataTable.bootstrapTable('refresh');
                                                    }
                                                    self.$destroy();
                                                }
                                            },
                                            function (error) {
                                                Vue.toastr.error(error.responseText);
                                            }).catch(function () {
                                        }).finally(function () {
                                            self.submitting = false;
                                        })
                                    }
                                })
                        }
                    }
                });
                // 创建的Vue对象应该被返回
                return vueModal
            })
    }

    // 查看驳回记录
    function reject(id) {
        var _modal = $('#reject').clone();
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
                    $modal: $el,
                    created: function () {
                    },
                    filters: {
                        result: function (value) {
                            var label = '';
                            if (value == 1) {
                                label = '<font color="green">通过</font>';
                                return label;
                            } else {
                                label = '<font color="red">驳回</font>';
                                return label;
                            }
                        }
                    },
                    data: {
                        recordList: null
                    },
                    methods: {
                        // 查询 信息
                        app: function () {
                            var self = this;
                            self.$http.get('/api/sku/recordListByStatus ?skuId=' + id).then(function (res) {
                                if (res.data.code == 1) {
                                    self.recordList = res.data.data;
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
