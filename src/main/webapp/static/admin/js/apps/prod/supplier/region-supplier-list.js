+(function () {
    $('#applyManage').addClass('active');
    $('#declarationFormList').addClass('active');
    Vue.validator('telphone', function (tel) {
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
                    name: '商品管理'
                },
                {
                    path: '/',
                    name: '区域供应商',
                    active: true
                }],
            form: {
                keyword: '',
                storeCode: '',
                status: ''
            },
            regionSupplierStatus: null,
            storeList: [],
            $dataTable: null,
            modalModel: null,
            _$el: null,
            _$dataTable: null
        },
        created: function () {
        },
        ready: function () {
            this.initData();
            this.drawTable();
            this.regionSupplierStatus = [
                {label: "请选择区域供应商状态", value: ""}, {label: "启用", value: "OPEN"},
                {label: "停用", value: "LOCK"},

            ];
        },
        methods: {
            initData: function () {
                var self = this;
                this.$http.get('/api/product/store/all').then(function (resp) {
                    self.storeList = resp.data.data;
                });
            },
            query: function () {
                this.$dataTable.bootstrapTable('selectPage', 1);
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/api/regionSupplier/list',
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
                            field: "store.code",
                            visible: false
                        },
                        {
                            field: 'name',
                            title: '名称',
                            width: '40%',
                            orderable: true,
                            align: 'center'
                        }, {
                            field: 'store',
                            title: '所属门店',
                            width: '40%',
                            orderable: true,
                            align: 'center',
                            formatter: function (val) {
                                return val.name;
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
                                if (hasEditRegionPrivelege && acctType == 'STORE') {
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
                                    if (RocoUtils.hasPermission('region_supplier:edit')) {
                                        html += '<button style="margin-left:10px;"';
                                        html += 'data-handle="data-edit"';
                                        html += 'data-id="' + value + '"';
                                        html += 'data-name="' + row.name + '"';
                                        html += 'data-store="' + row.store.code + '"';
                                        html += 'data-status="' + row.status + '"';
                                        html += 'class="m-r-xs btn btn-xs btn-primary" type="button">编辑</button>';
                                    }
                                    html += '<button data-handle="data-deal" data-supplierId="' + value + '"  data-newStatus="' + nostatus + '" class="' + class_val + '" type="button">' + deal_name + '</button>';
                                }
                                return html;
                            }
                        }]
                });
                //编辑
                self.$dataTable.on('click', '[data-handle="data-edit"]',
                    function (e) {
                        var model = $(this).data();
                        createOrEditModal(model, true);
                        e.stopPropagation();
                    }
                );
                //启用/停用
                self.$dataTable.on('click', '[data-handle="data-deal"]',
                    function (e) {
                        var model = $(this).data()

                        var send = {
                            newStatus: model.newstatus,
                            regionSupplierId: model.supplierid
                        }
                        self.$http.post('/api/regionSupplier/changeStatus', send, {emulateJSON: true}).then(function (res) {
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
            createBtnClickHandler: function (e) {
                //新增
                createOrEditModal({
                        "status": "OPEN"
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
                        this.findAllStore();
                    },
                    data: {
                        name: model.name,
                        id: model.id,
                        storeSelects: [],
                        storeCode: model.store
                    },
                    methods: {
                        save: function () {
                            var self = this;
                            self.$validate(true,
                                function () {
                                    if (self.$validation.valid) {
                                        self.submitting = true;
                                        if (!self.id) {
                                            self.id = '';
                                        }
                                        var data = {
                                            id: self.id,
                                            'store.code': self.storeCode,
                                            name: self.name
                                        };
                                        self.$http.post('/api/regionSupplier/saveOrUpdate', data, {emulateJSON: true}).then(function (res) {
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
                        findAllStore: function () {
                            var self = this;
                            self.$http.get('/api/product/store/all').then(function (res) {
                                if (res.data.code === '1') {
                                    if (!self.storeCode) {
                                        self.storeCode = '';
                                    }
                                    self.storeSelects = res.data.data;
                                }
                            });
                        }
                    },
                });
                // 创建的Vue对象应该被返回
                return vueModal;
            });
    }
})();