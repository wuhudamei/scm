+(function () {
    $('#customer').addClass('active');
    $('#contractInfo').addClass('active');
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
                name: '项目列表',
                active: true
            }],
            form: {
                keyword: '',
                customerId: ''
            },
            $dataTable: null,
            modalModel: null,
            _$el: null,
            _$dataTable: null,
            id: null
        },
        created: function () {
        },
        ready: function () {
            this.form.customerId = this.$parseQueryString()['id'];
            this.getCustomerNameById();
            this.drawTable();
        },
        methods: {
            getCustomerNameById: function () {
                var self = this;
                if (self.form.customerId != '' && self.form.customerId != undefined) {
                    self.$http.get('/api/customer/' + self.form.customerId).then(function (res) {
                            if (res.data.code === '1') {
                                self.form.keyword = res.data.data.name;
                            }
                        },
                        function (error) {
                            Vue.toastr.error(error.responseText);
                        }).catch(function () {
                    }).finally(function () {
                        self.submitting = false;
                    });
                }
            },
            query: function () {
                this.$dataTable.bootstrapTable('selectPage', 1);
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/api/customer/contract/list',
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
                            field: 'contractCode',
                            title: '项目编号',
                            orderable: true,
                            align: 'center'
                        },
                        {
                            field: 'customer',
                            title: '客户名字',
                            orderable: false,
                            align: 'center',
                            formatter: function (val) {
                                return val.name;
                            }
                        },
                        {
                            field: 'houseAddr',
                            title: '客户装修地址',
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'designer',
                            title: '设计师',
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'designerMobile',
                            title: '设计师手机号',
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'supervisor',
                            title: '监理',
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'supervisorMobile',
                            title: '监理手机号',
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'projectManager',
                            title: '项目经理',
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'pmMobile',
                            title: '项目经理电话',
                            align: 'center',
                            orderable: false,
                        },
                        {
                            field: 'id',
                            title: '操作',
                            orderable: false,
                            align: 'center',
                            formatter: function (value, row) {
                                var html = '';
                                if (RocoUtils.hasPermission('contract:edit')) {
                                    if (acctType == 'FINANCE' || acctType == 'MATERIAL_MANAGER') {
                                        html += '<button style="margin-left:10px;"'
                                        html += 'data-handle="data-edit"'
                                        html += 'data-id="' + value + '"'
                                        html += 'data-contractcode="' + row.contractCode + '"'
                                        html += 'data-customer="' + row.customer.name + '"'
                                        html += 'data-houseaddr="' + row.houseAddr + '"'
                                        html += 'data-designer="' + row.designer + '"'
                                        html += 'data-designermobile="' + row.designerMobile + '"'
                                        html += 'data-supervisor="' + row.supervisor + '"'
                                        html += 'data-supervisormobile="' + row.supervisorMobile + '"'
                                        html += 'data-projectmanager="' + row.projectManager + '"'
                                        html += 'data-pmmobile="' + row.pmMobile + '"'
                                        html += 'class="m-r-xs m-t-xs btn btn-xs btn-primary" type="button">编辑</button>'
                                    }
                                }
                                if (acctType == 'MATERIAL_CLERK') {
                                    html += '<button style="margin-left:10px;"'
                                    html += 'data-handle="data-add"'
                                    html += 'data-id="' + row.id + '"'
                                    html += 'data-code="' + row.contractCode + '"'
                                    html += 'class="m-r-xs m-t-xs btn btn-xs btn-primary" type="button">创建订货单</button>'


                                    html += '<button style="margin-left:10px;margin-top: 5px;" data-handle="data-look" data-code="' + row.contractCode + '" class="m-r-xs btn btn-xs btn-primary" type="button">查看订货单</button>';

                                    html += '<button style="margin-left:10px;"'
                                    html += 'data-handle="data-addReviewSize"'
                                    html += 'data-id="' + row.id + '"'
                                    html += 'data-code="' + row.contractCode + '"'
                                    html += 'class="m-r-xs m-t-xs btn btn-xs btn-primary" type="button">创建复尺通知单</button>'

                                    html += '<button style="margin-left:10px;"'
                                    html += 'data-handle="data-editReviewSize"'
                                    html += 'data-id="' + row.id + '"'
                                    html += 'data-code="' + row.contractCode + '"'
                                    html += 'class="m-r-xs m-t-xs btn btn-xs btn-primary" type="button">查看复尺通知单</button>'
                                }
                                return html
                            }
                        }]
                });
                // 编辑
                self.$dataTable.on('click', '[data-handle="data-edit"]',
                    function (e) {
                        var model = $(this).data()
                        model.contractCode = model.contractcode;
                        model.houseAddr = model.houseaddr;
                        model.pmMobile = model.pmmobile;
                        model.projectManager = model.projectmanager;
                        model.customerName = model.customer;
                        model.designerMobile = model.designermobile;
                        model.supervisorMobile = model.supervisormobile;
                        contract(model, true)
                        e.stopPropagation()
                    }
                );

                //查看订货单
                self.$dataTable.on('click', '[data-handle="data-look"]',
                    function (e) {
                        location.href = "/admin/indentOrder/list?contractCode=" + $(this).data().code;
                        e.stopPropagation()
                    }
                );

                // 新增订单
                self.$dataTable.on('click', '[data-handle="data-add"]',
                    function (e) {
                        var code = $(this).data('code')
                        var id = $(this).data('id')
                        window.location.href = '/admin/contract/createOrder?contractCode=' + code + '&contractId=' + id
                        e.stopPropagation()
                    }
                );
                // 创建供应商复尺申请
                self.$dataTable.on('click', '[data-handle="data-addReviewSize"]',
                    function (e) {
                        var code = $(this).data('code')
                        var id = $(this).data('id')
                        window.location.href = '/admin/contract/createReviewSize?contractCode=' + code + '&contractId=' + id
                        e.stopPropagation()
                    }
                );
                // 查看供应商复尺申请
                self.$dataTable.on('click', '[data-handle="data-editReviewSize"]',
                    function (e) {
                        var code = $(this).data('code')
                        var id = $(this).data('id')
                        window.location.href = '/admin/contract/editReviewSize?contractCode=' + code + '&contractId=' + id
                        e.stopPropagation()
                    }
                );
                self.checkEventHandle('ordNo');
            }
        }
    });

    // 录入项目弹窗
    function contract(model, isEdit) {
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
                    mixins: [RocoVueMixins.ModalMixin],
                    components: {
                        'web-uploader': RocoVueComponents.WebUploaderComponent
                    },
                    $modal: $el,
                    created: function () {
                    },
                    data: {
                        id: model.id,
                        contractCode: model.contractCode === 'undefined' ? '' : model.contractCode,
                        houseaddr: model.houseAddr === 'undefined' ? '' : model.houseAddr,
                        designer: model.designer === 'undefined' ? '' : model.designer,
                        designerMobile: model.designerMobile === 'undefined' ? '' : model.designerMobile,
                        supervisor: model.supervisor === 'undefined' ? '' : model.supervisor,
                        supervisorMobile: model.supervisorMobile === 'undefined' ? '' : model.supervisorMobile,
                        projectManager: model.projectManager === 'undefined' ? '' : model.projectManager,
                        pmMobile: model.pmMobile === 'undefined' ? '' : model.pmMobile,
                        'customer.id': model.customer.id,
                        customerName: model.customerName === 'undefined' ? '' : model.customerName
                    },
                    methods: {
                        save: function () {
                            var self = this;
                            self.$validate(true,
                                function () {
                                    if (self.$validation.valid) {
                                        self.submitting = true;
                                        self._data.houseAddr = self.houseaddr === 'undefined' ? '' : self.houseaddr;
                                        self._data.designer = self.designer === 'undefined' ? '' : self.designer;
                                        self._data.designerMobile = self.designerMobile === 'undefined' ? '' : self.designerMobile;
                                        self._data.supervisor = self.supervisor === 'undefined' ? '' : self.supervisor;
                                        self._data.supervisorMobile = self.supervisorMobile === 'undefined' ? '' : self.supervisorMobile;
                                        self._data.projectManager = self.projectManager === 'undefined' ? '' : self.projectManager;
                                        self._data.pmMobile = self.pmMobile === 'undefined' ? '' : self.pmMobile;
                                        self.$http.post('/api/customer/contract/save', $.param(self._data)).then(function (res) {
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
})();