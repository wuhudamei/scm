var vueIndex;
+(function () {
    $('#prepareOrder').addClass('active');

    var Detail = null;
    vueIndex = new Vue({
        el: '#container',
        mixins: [DaMeiVueMixins.DataTableMixin],
        data: {
            breadcrumbs: [{
                path: '/admin',
                name: '主页'
            }, {
                path: '/',
                name: '预备单',
                active: true
            }],
            form: {
                keyword: '',
                status: '',
                endDate: '',
                startDate: '',
                dataSource: '',
                brandId: ''
            },
            brands: {},
            $dataTable: null,
            _$el: null,
            _$dataTable: null,
        },
        created: function () {
        },
        ready: function () {
            this.activeDatepiker();
            this.drawTable();
            this.getBrands();
        },
        methods: {
            activeDatepiker: function () {
                var self = this;
                $(self.$els.endDate).datetimepicker('setStartDate', '');
                $(self.$els.startDate).datetimepicker('setStartDate', '');
            },
            //获取品牌
            getBrands: function () {
                var self = this;
                self.$http.get('/api/brand/all').then(function (res) {
                    if (res.data.code == 1) {
                        self.brands = res.data.data;
                    }
                }).catch(function () {

                }).finally(function () {

                });
            },
            query: function () {
                this.$dataTable.bootstrapTable('selectPage', 1);
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/material/prepareorder/list',
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
                        }, {
                            field: 'contractCode',
                            title: '项目编号',
                            orderable: true,
                            align: 'center'
                        }, {
                            field: 'customerName',
                            title: '客户姓名',
                            orderable: false,
                            align: 'center'
                        }, {
                            field: 'installationLocation',
                            title: '装修位置',
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'brandName',
                            title: '品牌',
                            width: '20%',
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'dataSource',
                            title: '来源',
                            orderable: false,
                            align: 'center',
                            formatter: function (val) {
                                if (val == 'select') {
                                    return "选材";
                                } else if (val == 'change') {
                                    return "变更";
                                }
                            }
                        }, {
                            field: 'createTime',
                            title: '创建时间',
                            orderable: false,
                            align: 'center',
                        }, {
                            field: 'updateTime',
                            title: '修改时间',
                            orderable: false,
                            align: 'center',
                        }, {
                            field: 'switchTime',
                            title: '转订货单时间',
                            orderable: false,
                            align: 'center',
                        }, {
                            field: 'status',
                            title: '状态',
                            orderable: false,
                            align: 'center',
                            formatter: function (val) {
                                if (val == 'WAITING_TRANSFERRED') {
                                    return '<font color="#ffd700">待转单</font>';
                                } else if (val == 'ALREADY_TRANSFERRED') {
                                    return '<font color="blue">已转单</font>';
                                } else if (val == 'HAS_NULLIFIED') {
                                    return '<font color="red">已作废</font>';
                                }
                            }
                        }, {
                            field: 'id',
                            title: '操作',
                            orderable: false,
                            align: 'center',
                            formatter: function (value, row) {
                                var html = '';
                                html += '<button data-handle="data-detail" data-id="' + row.id + '" data-contractid="'
                                    + row.contractId + '"  data-code="' + row.contractCode
                                    + '"data-status="' + row.status + '"  type="button" class="m-r-xs btn btn-xs btn-primary">详情</button>';
                                return html;
                            }
                        }]
                });
                // 详情
                self.$dataTable.on('click', '[data-handle="data-detail"]',
                    function (e) {
                        var id = $(this).data('id');
                        var code = $(this).data('code');
                        var status = $(this).data('status');
                        detailModal(id, code, status);
                    });
            },
        }
    });

    // 详情弹窗
    function detailModal(id, code, status) {
        var _modal = $('#detailModal').clone();
        var $el = _modal.modal({
            height: 300,
            maxHeight: 500,
            width: 1200
        });
        $el.on('shown.bs.modal',
            function () {
                var el = $el.get(0);
                Detail = new Vue({
                    el: el,
                    http: {
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                    },
                    mixins: [DaMeiVueMixins.ModalMixin],
                    $modal: $el,
                    created: function () {
                    },
                    ready: function () {
                        this.getDetail();
                        this.drawTable();
                        this.findCustomerContract();
                    },
                    data: {
                        id: id,
                        table: '',
                        status: status,
                        customerContract: {
                            customerName: '',
                            houseAddr: '',
                            designer: '',
                            designerMobile: '',
                            supervisor: '',
                            supervisorMobile: '',
                            projectManager: '',
                            pmMobile: ''
                        }
                    },
                    methods: {
                        findCustomerContract: function () {
                            var self = this;
                            this.$http.get("/api/customer/contract/getById?code=" + code).then(function (res) {
                                if (res.data.code == 1) {
                                    self.customerContract = res.data.data;
                                }
                            })
                        },
                        drawTable: function () {
                            var self = this;
                            self.$dataTable = $('#detailTable', self._$el).bootstrapTable({
                                cache: false,
                                data: self.customerContract,
                                pagination: true,
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
                                        field: 'skuName',
                                        title: '商品名称',
                                        width: '10%',
                                        orderable: true,
                                        align: 'center'
                                    },
                                    {
                                        field: 'model',
                                        title: '商品型号',
                                        width: '5%',
                                        orderable: false,
                                        align: 'center'
                                    },
                                    {
                                        field: 'spec',
                                        title: '商品规格',
                                        width: '10%',
                                        orderable: false,
                                        align: 'center'
                                    },
                                    {
                                        field: 'attribute1',
                                        title: '属性值1',
                                        width: '5%',
                                        orderable: false,
                                        align: 'center'
                                    }, {
                                        field: 'attribute2',
                                        title: '属性值2',
                                        width: '5%',
                                        orderable: false,
                                        align: 'center'
                                    }, {
                                        field: 'attribute3',
                                        title: '属性值3',
                                        width: '5%',
                                        orderable: false,
                                        align: 'center'
                                    },
                                    {
                                        field: 'quantity',
                                        title: '订货数量',
                                        width: '3%',
                                        orderable: false,
                                        align: 'center',
                                        formatter: function (val, row) {
                                            if (row.tabletNum > 0) {
                                                //有片数时, 表示单位是平米
                                                return val + " " + "㎡";
                                            } else if (row.specUnit) {
                                                //获取specUnit单位
                                                var i = row.specUnit.indexOf("/");
                                                if (i != -1) {
                                                    return val + " " + row.specUnit.substring(i + 1, row.specUnit.length)
                                                } else {
                                                    return val + " " + row.specUnit;
                                                }
                                            } else {
                                                //没有单位
                                                return val;
                                            }
                                        }
                                    }, {
                                        field: 'installationLocation',
                                        title: '安装位置',
                                        width: '10%',
                                        orderable: false,
                                        align: 'center'
                                    }]
                            });
                        },
                        getDetail: function () {
                            var self = this;
                            self.$http.get('/material/prepareorder/finddetail?prepareOrderId=' + id).then(function (res) {
                                if (res.data.code == 1) {
                                    self.table = res.data.data.indentPrepareOrderItemList;
                                    self.$dataTable.bootstrapTable('load', self.table);
                                } else {
                                }
                            }).catch(function () {

                            }).finally(function () {
                            });
                        },
                        // 作废
                        abandonedOrder: function () {
                            var self = this;
                            var id = self.id;
                            swal({
                                title: '确认作废该预备单？',
                                text: '',
                                type: 'info',
                                confirmButtonText: '确定',
                                cancelButtonText: '取消',
                                showCancelButton: true,
                                showConfirmButton: true,
                                showLoaderOnConfirm: true,
                                confirmButtonColor: '#ed5565',
                                closeOnConfirm: false
                            }, function () {
                                var data = {
                                    id: id,
                                    status: 'HAS_NULLIFIED'
                                };
                                self.$http.post('/material/prepareorder/save', data, {emulateJSON: true}).then(function (res) {
                                    if (res.data.code == 1) {
                                        Vue.toastr.success("操作成功");
                                        self.$dataTable.bootstrapTable('refresh');
                                    } else {
                                        Vue.toastr.error(res.data.message);
                                    }
                                }).catch(function () {
                                }).finally(function () {
                                    swal.close();
                                    _modal.modal('hide');
                                    vueIndex.$dataTable.bootstrapTable('refresh');
                                });
                            });

                        },
                        // 转订货单
                        toIndentOrder: function () {
                            var self = this;
                            var id = self.id;
                            swal({
                                title: '确认转为订货单？',
                                text: '请再次确认!',
                                type: 'info',
                                confirmButtonText: '确定',
                                cancelButtonText: '取消',
                                showCancelButton: true,
                                showConfirmButton: true,
                                showLoaderOnConfirm: true,
                                confirmButtonColor: '#ed5565',
                                closeOnConfirm: false
                            }, function () {
                                self.$http.get('/material/prepareorder/turntoindentorder?id=' + id).then(function (res) {
                                    if (res.data.code == 1) {
                                        Vue.toastr.success("操作成功");
                                        self.$dataTable.bootstrapTable('refresh');
                                    } else {
                                        Vue.toastr.error(res.data.message);
                                    }
                                }).catch(function () {
                                }).finally(function () {
                                    swal.close();
                                    _modal.modal('hide');
                                    vueIndex.$dataTable.bootstrapTable('refresh');
                                });
                            });
                        }
                    },
                });
                // 创建的Vue对象应该被返回
                return Detail;
            });
    }

    // 编辑详情弹窗
    function editDetailModal(model) {
        var _modal = $('#editDetailModal').clone();
        var $el = _modal.modal({
            height: 600,
            maxHeight: 700
        });
        $el.on('shown.bs.modal',
            function () {
                var el = $el.get(0);
                var editDetail = new Vue({
                    el: el,
                    http: {
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                    },
                    mixins: [DaMeiVueMixins.ModalMixin],
                    $modal: $el,
                    created: function () {
                    },
                    ready: function () {
                        this.activeDatepiker();
                    },
                    data: {
                        id: model.id,
                        quantity: model.quantity,
                        note: model.note,
                        installDate: model.installdate
                    },
                    methods: {
                        activeDatepiker: function () {
                            var self = this;
                            $(self.$els.installDate).datetimepicker('setStartDate', self.installDate);
                        },
                        save: function () {
                            var self = this;
                            self.$validate(true,
                                function () {
                                    if (self.$validation.valid) {
                                        self.submitting = true;
                                        data = {
                                            id: self.id,
                                            quantity: self.quantity,
                                            note: self.note,
                                            installDate: self.installDate
                                        }
                                        self.$http.post('/api/orderItem/save', data, {emulateJSON: true}).then(function (res) {
                                                if (res.data.code === '1') {
                                                    Vue.toastr.success(res.data.message);
                                                    $el.modal('hide');
                                                    vueIndex.$dataTable.bootstrapTable('selectPage', 1);
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
                return editDetail;
            });
    }

})();