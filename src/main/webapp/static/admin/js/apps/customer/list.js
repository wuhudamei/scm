+(function () {
    $('#customer').addClass('active');
    $('#customerInfo').addClass('active');
    Vue.validator('telphone', function (tel) {
        return /^1[3|4|5|7|8]\d{9}$/.test(tel);
    })
    var vueIndex = new Vue({
        el: '#container',
        mixins: [DaMeiVueMixins.DataTableMixin],
        data: {
            acctType: null,
            breadcrumbs: [{
                path: '/admin',
                name: '主页'
            }, {
                path: '/',
                name: '客户信息',
                active: true
            }],
            form: {
                keyword: '',
                status: ''
            },
            $dataTable: null,
            modalModel: null,
            _$el: null,
            _$dataTable: null
        },
        created: function () {
        },
        ready: function () {
            this.acctType = acctType;
            this.drawTable();
        },
        methods: {
            query: function () {
                this.$dataTable.bootstrapTable('selectPage', 1);
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/api/customer/list',
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
                            field: 'name',
                            title: '客户名字',
                            width: '15%',
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'mobile',
                            title: '客户手机号',
                            width: '10%',
                            align: 'center',
                            orderable: false,
                            formatter: function (value, row) {
                                var mphone = value.substr(0, 3) + '****' + value.substr(7);
                                return mphone;
                            }
                        },
                        {
                            field: 'store',
                            title: '所属门店',
                            width: '10%',
                            align: 'center',
                            orderable: false,
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
                            formatter: function (data, full) {
                                var str = '';
                                if (DaMeiUtils.hasPermission('customer:edit')) {
                                    if (self.acctType == 'FINANCE' && acctType != 'ADMIN') {
                                        str += '<button style="margin-left:10px;" data-handle="data-edit" data-id="' + data + '" data-code="' + full.code + '" data-name="' + full.name + '" data-mobile="' + full.mobile + '" class="m-r-xs btn btn-xs btn-primary" type="button">编辑</button>';
                                    }
                                }
                                if (acctType != 'STORE' && acctType != 'ADMIN' && acctType != 'MATERIAL_MANAGER') {//门店管理员不能操作项目
                                    str += '<button style="margin-left:10px;" data-handle="data-contract" data-id="' + data + '" data-code="' + full.code + '"data-name="' + full.name + '"data-mobile="' + full.mobile + '"class="m-r-xs btn btn-xs btn-primary" type="button">录入项目</button>';
                                    str += '<button style="margin-left:10px;" data-handle="data-look" data-id="' + data + '" class="m-r-xs btn btn-xs btn-primary" type="button">查看项目</button>';
                                }
                                return str;
                            }
                        }]
                });
                //编辑
                self.$dataTable.on('click', '[data-handle="data-edit"]',
                    function (e) {
                        var model = $(this).data();
                        model.houseAddr = model.houseaddr;
                        createOrEditModal(model, true);
                        e.stopPropagation();
                    }
                );
                //录入项目
                self.$dataTable.on('click', '[data-handle="data-contract"]',
                    function (e) {
                        var model = $(this).data();
                        model.pmMobile = "";
                        contract(model, true, true);
                        e.stopPropagation();
                    }
                );
                //查看项目
                self.$dataTable.on('click', '[data-handle="data-look"]',
                    function (e) {
                        location.href = "/admin/contract?id=" + $(this).data().id;
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
            },
            createCustomerAndContract: function (e) {
                var model = "";
                contract(model, true, false);
                e.stopPropagation()
            }
        }
    });

    // 新增或修改窗口
    function createOrEditModal(model, isEdit) {
        var _modal = $('#modal').clone();
        var $el = _modal.modal({
            height: 300,
            maxHeight: 500
        });
        $el.on('shown.bs.modal',
            function () {
                var el = $el.get(0);
                isEdit = !!isEdit;
                var vueModal = new Vue({
                    el: el,
                    validators: {
                        tel: function (val) {
                            if (_.trim(val) === '') {
                                return true;
                            }
                            return /^1[35784]\d{9}$/.test(val);
                        },
                    },
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
                        code: model.code,
                        mobile: model.mobile
                    },
                    methods: {
                        save: function () {
                            var self = this;
                            self.$validate(true,
                                function () {
                                    if (self.$validation.valid) {
                                        self.submitting = true;
                                        if (!self._data.id) {
                                            self._data.id = ''
                                        }
                                        self.$http.post('/api/customer/save', $.param(self._data)).then(function (res) {
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
                        }
                    },
                });
                // 创建的Vue对象应该被返回
                return vueModal;
            });
    }

    // 录入项目弹窗
    function contract(model, isEdit, isReadOnly) {
        var _modal = $('#contractModal').clone();
        var $el = _modal.modal({
            height: 450,
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
                        isRO: isReadOnly,
                        contractCode: '',
                        houseAddr: model.houseAddr === 'undefined' ? '' : model.houseAddr,
                        designer: model.designer === 'undefined' ? '' : model.designer,
                        designerMobile: model.designerMobile === 'undefined' ? '' : model.designerMobile,
                        supervisor: model.supervisor === 'undefined' ? '' : model.supervisor,
                        supervisorMobile: model.supervisorMobile === 'undefined' ? '' : model.supervisorMobile,
                        projectManager: model.projectManager === 'undefined' ? '' : model.projectManager,
                        pmMobile: model.pmMobile === 'undefined' ? '' : model.pmMobile,
                        customer: {
                            id: model.id,
                            name: model.name,
                            mobile: model.mobile
                        }
                    },
                    methods: {
                        save: function () {
                            var self = this;
                            self.$validate(true,
                                function () {
                                    if (self.$validation.valid) {
                                        self.submitting = true;
                                        if (!isReadOnly) {
                                            self.customer.id = '';
                                        }
                                        var dataSave = {
                                            contractCode: self.contractCode === 'undefined' || self.contractCode == null ? '' : self.contractCode,
                                            houseAddr: self.houseAddr === 'undefined' || self.houseAddr == null ? '' : self.houseAddr,
                                            designer: self.designer === 'undefined' || self.designer == null ? '' : self.designer,
                                            designerMobile: self.designerMobile === 'undefined' || self.designerMobile == null ? '' : self.designerMobile,
                                            supervisor: self.supervisor === 'undefined' || self.supervisor == null ? '' : self.supervisor,
                                            supervisorMobile: self.supervisorMobile === 'undefined' || self.supervisorMobile == null ? '' : self.supervisorMobile,
                                            projectManager: self.projectManager === 'undefined' || self.projectManager == null ? '' : self.projectManager,
                                            pmMobile: self.pmMobile === 'undefined' || self.pmMobile == null ? '' : self.pmMobile,
                                            'customer.id': self.customer.id === 'undefined' || self.customer.id == null ? '' : self.customer.id,
                                            'customer.name': self.customer.name === 'undefined' || self.customer.name == null ? '' : self.customer.name,
                                            'customer.mobile': self.customer.mobile === 'undefined' || self.customer.mobile == null ? '' : self.customer.mobile,
                                        };
                                        self.$http.post('/api/customer/contract/save', dataSave, {emulateJSON: true}).then(function (res) {
                                                if (res.data.code === '1') {
                                                    Vue.toastr.success(res.data.message);
                                                    $el.modal('hide');
                                                    vueIndex.$dataTable.bootstrapTable('selectPage', 1);
                                                    self.$destroy();
                                                }
                                            },
                                            function (error) {
                                                console.log(0)
                                                Vue.toastr.error(error.responseText);
                                            }).catch(function () {

                                        }).finally(function () {
                                            self.submitting = false;
                                        });
                                    }
                                })
                        }
                    },
                });
                // 创建的Vue对象应该被返回
                return vueModal;
            });
    }
})();