+(function () {
    $('#order').addClass('active');
    $('#indentOrder').addClass('active');

    var Detail = null;
    var vueIndex = new Vue({
        el: '#container',
        mixins: [RocoVueMixins.DataTableMixin],
        data: {
            breadcrumbs: [{
                path: '/admin',
                name: '主页'
            }, {
                path: '/',
                name: '订货单',
                active: true
            }],
            form: {
                acceptStatus: "",
                download: "",
                keyword: '',
                status: '',
                endDate: '',
                startDate: '',
                contractCode: '',
                brandId: '',
                dateType:''
            },
            brands: {},
            $dataTable: null,
            modalModel: null,
            _$el: null,
            _$dataTable: null,
            id: null
        },
        created: function () {
        },
        ready: function () {
            this.form.contractCode = this.$parseQueryString()['contractCode'];
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
            //获取合同信息
            query: function () {
                this.$dataTable.bootstrapTable('selectPage', 1);
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/api/order/list',
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
                            title: '订货单号',
                            orderable: true,
                            align: 'center',
                            formatter: function (value, row) {
                                return '<a data-handle="data-detail" data-id="' + row.id + '">' + value + '</a>';
                            }
                        },
                        {
                            field: 'contractCode',
                            title: '项目编号',
                            orderable: false,
                            align: 'center'
                        }, {
                            field: 'customerName',
                            title: '客户姓名',
                            orderable: false,
                            align: 'center'
                        }, {
                            field: 'houseAddr',
                            title: '装修位置',
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'brandName',
                            title: '品牌',
                            width: '10%',
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'creator',
                            title: '制单人',
                            orderable: false,
                            align: 'center',
                            formatter: function (val) {
                                return val.name;
                            }
                        }, {
                            field: 'placeEnum',
                            title: '制单类型',
                            orderable: false,
                            align: 'center',
                            formatter: function (value) {
                                switch (value) {
                                    case "NORMAL":
                                        return "正常下单";
                                        break;
                                    case "CHANGE":
                                        return "变更单";
                                        break;
                                }
                            }
                        },
                        {
                            field: 'acceptStatus',
                            title: '接单状态',
                            orderable: false,
                            align: 'center',
                            formatter: function (value) {
                                switch (value) {
                                    case "YES":
                                        return "已接收";
                                        break;
                                    case "NO":
                                        return "未接收";
                                        break;
                                }
                            }
                        }, {
                            field: 'acceptDate',
                            title: '接单时间',
                            orderable: false,
                            align: 'center'
                        }, {
                            field: 'downloadNumber',
                            title: '下载状态',
                            orderable: false,
                            align: 'center',
                            formatter: function (value) {
                                switch (value) {
                                    case 0:
                                        return "未下载";
                                        break;
                                    default:
                                        return "已下载（" + value + "）";
                                        break;
                                }
                            }
                        },
                        {
                            field: 'downloadDate',
                            title: '下载时间',
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'status',
                            title: '订货单状态',
                            orderable: false,
                            align: 'center',
                            formatter: function (data) {
                                switch (data) {
                                    case "DRAFT":
                                        return "备货单";
                                        break;
                                    case "NOTIFIED":
                                        return "订货单";
                                        break;
                                    case "INVALID":
                                        return "已作废";
                                        break;
                                    case "REJECT":
                                        return "已驳回";
                                        break;
                                    case "ALREADY_INSTALLED":
                                        return "已安装";
                                        break;
                                    case "REJECTINSTALL":
                                        return "驳回安装";
                                        break;
                                    case "INSTALLEND_WAITCHECK":
                                        return "安装完成，待审核";
                                        break;
                                    case "INSTALLCHECKNOTPASS":
                                        return "安装审核未通过";
                                        break;
                                    case "NOTRECONCILED":
                                        return "未对账";
                                        break;
                                    case "HASBEENRECONCILED":
                                        return "已对账";
                                        break;
                                    case "PARTIALRECONCILIATION":
                                        return "部分对账";
                                        break;
                                    case "INSTALLCHECKPASS":
                                        return "安装审核已通过";
                                        break;
                                }

                            }
                        }, {
                            field: 'createTime',
                            title: '创建时间',
                            orderable: false,
                            align: 'center'
                        }, {
                            field: 'noticeInstallTime',
                            title: '通知安装时间',
                            orderable: false,
                            align: 'center'
                        }, {
                            field: 'id',
                            title: '操作',
                            orderable: false,
                            align: 'center',
                            formatter: function (value, row) {
                                if (row) {
                                    var html = '';
                                    //下单员操作
                                    if (RocoUtils.hasPermission('indentorder:edit')) {
                                        //接受后
                                        if (row.acceptStatus == 'YES') {
                                            if (row.status == 'INSTALLEND_WAITCHECK' && acctType == 'MATERIAL_CLERK') {//安装提交待审核
                                                html += '<button data-handle="checkInstall" data-id="' + row.id + '" data-code="' + row.code + '" data-contractcode="' + row.contractCode + '" class="m-r-xs btn btn-xs btn-primary" type="button">审核</button>';
                                            }
                                            //备货单状态
                                            if (row.status == 'DRAFT') {
                                                html += '<button  data-handle="data-notify" data-id="' + row.id + '"  data-status="' + "NOTIFIED" + '" class="m-r-xs btn btn-xs btn-success " type="button">通知安装</button>';
                                            } else if (row.status == 'REJECTINSTALL') {//驳回安装状态
                                                html += ('<button data-handle="indent-again" data-id="' + row.id + '" data-code="' + row.code + '" data-contractcode="' + row.contractCode + '"  type="button" class="btn btn-xs btn-primary">重新通知安装</button>&nbsp;&nbsp;&nbsp;');
                                            } else if (row.status == 'INVALID') {//已作废状态
                                                html += '<button ';
                                                html += 'data-handle="data-edit"';
                                                html += 'data-id="' + row.id + '"';
                                                html += 'data-code="' + row.contractCode + '"';
                                                html += 'data-contractid="' + row.contractId + '"';
                                                html += 'class="m-r-xs btn btn-xs btn-primary" type="button">复制</button>';
                                            } else if (row.status == 'ALREADY_INSTALLED') {//已安装状态
                                                html += '<button ';
                                                html += 'data-handle="data-pay"';
                                                html += 'data-id="' + row.id + '"';
                                                html += 'class="m-r-xs btn btn-xs btn-primary" type="button">支付</button>';
                                            } else if (row.status != 'SETTLEACCOUNTS') {//不等于已计算
                                                if (row.status != 'INVALID' && row.status != 'HASBEENRECONCILED' && row.status != 'PARTIALRECONCILIATION') {
                                                    html += '<button style="margin-left:15px;" data-handle="data-cancle" data-id="' + row.id + '"  data-code="' + row.code + '"  data-status="' + "INVALID" + '" class="m-r-xs btn btn-xs btn-danger" type="button">作废</button>';
                                                }
                                            }
                                        }
                                    }
                                    //变更
                                    if (RocoUtils.hasPermission('indentorder:change')) {
                                        //接受后
                                        if (row.acceptStatus == 'YES') {
                                            if (row.placeEnum != 'CHANGE') {
                                                if (row.status == 'DRAFT' || row.status == 'NOTIFIED') {
                                                    html += '<button ';
                                                    html += 'data-handle="data-change"';
                                                    html += 'data-id="' + row.id + '"';
                                                    html += 'data-code="' + row.contractCode + '"';
                                                    html += 'data-contractid="' + row.contractId + '"';
                                                    html += 'class="m-r-xs btn btn-xs btn-primary" type="button">变更</button>';
                                                }
                                            }
                                        }
                                    }
                                    //供应商能操作
                                    if (RocoUtils.hasPermission('indentorder:supplyDeal')) {
                                        //接受按钮
                                        if (row.acceptStatus == 'NO') {
                                            html += ('<button   data-handle="acceptOrder" data-id="' + row.id + '"   type="button" class="m-r-xs btn btn-xs btn-warning">接收</button>');
                                        } else if (row.acceptStatus == 'YES') {
                                            if (row.status == 'INSTALLCHECKNOTPASS') {//提交安装被驳回
                                                html += ('<button data-handle="again-install" data-id="' + row.id + '" data-code="' + row.code + '" data-contractcode="' + row.contractCode + '"  type="button" class="btn btn-xs btn-primary">重新提交安装</button>&nbsp;&nbsp;&nbsp;');
                                            }
                                            if (row.status == 'NOTIFIED' || row.status == 'DRAFT') {
                                                html += ('<button   data-handle="reject" data-id="' + row.id + '" data-code="' + row.code + '" data-contractcode="' + row.contractCode + '" data-status="' + row.status + '" type="button" class="m-r-xs btn btn-xs btn-danger">驳回</button>');
                                                html += '<button data-handle="data-storage" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-primary" type="button">入库</button>';
                                            }
                                            //下载
                                            html += '<button data-handle="data-download" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-dark" type="button">下载</button>';
                                            if (row.status == 'NOTIFIED') {
                                                html += '<button ';
                                                html += 'data-handle="data-installdate"';
                                                html += 'data-id="' + row.id + '"';
                                                html += 'class="m-r-xs btn btn-xs btn-primary" type="button">填写计划安装时间</button>';

                                                html += '<button ';
                                                html += 'data-handle="data-install"';
                                                html += 'data-id="' + row.id + '"';
                                                html += 'data-code="' + row.code + '"';
                                                html += 'data-contractcode="' + row.contractCode + '"';
                                                html += 'class="m-r-xs btn btn-xs btn-primary" type="button">安装完成</button>';
                                            }
                                        }
                                    }
                                    if (row.status == 'REJECT' || row.status == 'REJECTINSTALL') {
                                        html += ('<button   data-handle="rejectReason" data-id="' + row.id + '"   type="button" class="m-r-xs btn btn-xs btn-primary">驳回记录</button>');
                                    }
                                    if (row.status == 'INSTALLCHECKNOTPASS') {
                                        html += ('<button   data-handle="installRejectReason" data-id="' + row.id + '"   type="button" class="m-r-xs btn btn-xs btn-primary">驳回记录</button>');
                                    }
                                    if (RocoUtils.hasPermission('indentorder:printBtn')) {
                                        html += ('<button data-handle="print" data-id="' + row.id + '"   type="button" class="m-r-xs btn btn-xs btn-primary">打印</button>');
                                    }
                                    return html ;
                                }
                            }
                        }]
                });
                // 打印
                self.$dataTable.on('click', '[data-handle="print"]', function () {
                        var id = $(this).data("id");
                        window.open('/api/order/printorderdetail?id=' + id);
                    }
                );
                // 下载
                self.$dataTable.on('click', '[data-handle="data-download"]',
                    function () {
                        var id = $(this).data("id");
                        window.location.href = '/api/order/export?id=' + id;
                        setTimeout(function () {
                            self.$dataTable.bootstrapTable('refresh');
                        }, 1000);

                    }
                );
                // 接受
                self.$dataTable.on('click', '[data-handle="acceptOrder"]',
                    function () {
                        var id = $(this).data("id");
                        swal({
                            title: '确认接收？',
                            text: '您确认接收这条订货单?',
                            type: 'info',
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            showCancelButton: true,
                            showConfirmButton: true,
                            showLoaderOnConfirm: true,
                            confirmButtonColor: '#ed5565',
                            closeOnConfirm: false
                        }, function () {
                            self.$http.get('/api/order/accept?id=' + id).then(function (res) {
                                if (res.data.code == 1) {
                                    Vue.toastr.success("操作成功");
                                    self.$dataTable.bootstrapTable('refresh');
                                } else {
                                    Vue.toastr.error(res.data.message);
                                }
                            }).catch(function () {
                            }).finally(function () {
                                swal.close();
                            });
                        });

                    }
                );
                // 重新审核安装
                self.$dataTable.on('click', '[data-handle="again-install"]',
                    function (e) {
                        var model = $(this).data();
                        installSubmitModel(model);
                    }
                );
                // 安装审核驳回
                self.$dataTable.on('click', '[data-handle="installRejectReason"]',
                    function (e) {
                        var id = $(this).data("id");
                        var sourceType = 3;
                        rejectRecordModel(id, sourceType);
                    }
                );
                // 审核安装
                self.$dataTable.on('click', '[data-handle="checkInstall"]',
                    function (e) {
                        var model = $(this).data();
                        checkInstallModel(model);
                    }
                );
                // 重新提交订货单
                self.$dataTable.on('click', '[data-handle="indent-again"]',
                    function (e) {
                        var id = $(this).data("id");
                        var code = $(this).data("code");
                        var contractCode = $(this).data("contractcode");
                        swal({
                            title: '确认重新通知安装？',
                            text: '您确认重新通知安装?',
                            type: 'info',
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            showCancelButton: true,
                            showConfirmButton: true,
                            showLoaderOnConfirm: true,
                            confirmButtonColor: '#ed5565',
                            closeOnConfirm: false
                        }, function () {
                            self.$http.post('/api/order/updateStatus?indentId=' + id + '&code=' + code + '&contractCode=' + contractCode).then(function (res) {
                                if (res.data.code == 1) {
                                    Vue.toastr.success("操作成功");
                                    self.$dataTable.bootstrapTable('refresh');
                                } else {
                                    Vue.toastr.error(res.data.message);
                                }
                            }).catch(function () {
                            }).finally(function () {
                                swal.close();
                            });
                        });

                    }
                );
                // 驳回记录
                self.$dataTable.on('click', '[data-handle="rejectReason"]',
                    function (e) {
                        var id = $(this).data("id");
                        var sourceType = 1;
                        rejectRecordModel(id, sourceType);
                    }
                );
                // 驳回
                self.$dataTable.on('click', '[data-handle="reject"]',
                    function (e) {
                        var model = $(this).data();
                        rejectModel(model);
                    }
                );
                // 支付
                self.$dataTable.on('click', '[data-handle="data-pay"]',
                    function (e) {
                        var model = $(this).data();
                        console.log(model);
                        swal({
                            title: '确认支付？',
                            text: '将修改订货单为已支付状态',
                            type: 'info',
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            showCancelButton: true,
                            showConfirmButton: true,
                            showLoaderOnConfirm: true,
                            confirmButtonColor: '#ed5565',
                            closeOnConfirm: false
                        }, function () {
                            self.$http.get('/api/orderItem/' + model.id + '/updatePayStatus').then(function (res) {
                                if (res.data.code == 1) {
                                    Vue.toastr.success("操作成功");
                                    self.$dataTable.bootstrapTable('refresh');
                                } else {
                                    Vue.toastr.error(res.data.message);
                                }
                            }).catch(function () {

                            }).finally(function () {
                                swal.close();
                            });
                        });

                    }
                );
                // 安装
                self.$dataTable.on('click', '[data-handle="data-install"]',
                    function (e) {
                        var model = $(this).data();
                        installSubmitModel(model);
                        e.stopPropagation();
                    }
                );
                // 通知安装
                self.$dataTable.on('click', '[data-handle="data-notify"]',
                    function (e) {
                        var model = $(this).data()
                        swal({
                            title: '确认通知安装？',
                            text: '将修改状态为"已通知安装"',
                            type: 'info',
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            showCancelButton: true,
                            showConfirmButton: true,
                            showLoaderOnConfirm: true,
                            confirmButtonColor: '#ed5565',
                            closeOnConfirm: false
                        }, function () {
                            self.$http.get('/api/order/' + model.id + '/notify').then(function (res) {
                                if (res.data.code == 1) {
                                    Vue.toastr.success("操作成功");
                                    self.$dataTable.bootstrapTable('refresh');
                                } else {
                                    Vue.toastr.error(res.data.message);
                                }
                            }).catch(function () {

                            }).finally(function () {
                                swal.close();
                            });
                        });

                    }
                );
                // 复制
                self.$dataTable.on('click', '[data-handle="data-edit"]',
                    function (e) {
                        var editID = $(this).data('id');
                        var code = $(this).data('code');
                        var contractid = $(this).data('contractid');
                        swal({
                            title: '复制',
                            text: '您确认复制?',
                            type: 'info',
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            showCancelButton: true,
                            showConfirmButton: true,
                            showLoaderOnConfirm: true,
                            confirmButtonColor: '#ed5565'
                        }, function () {
                            window.location.href = '/admin/contract/createOrder?editID=' + editID + '&contractCode=' + code + '&contractId=' + contractid + '&copy=';
                            e.stopPropagation();
                        });

                        e.stopPropagation();
                    });
                // 详情
                self.$dataTable.on('click', '[data-handle="data-detail"]',
                    function (e) {
                        var model = $(this).data();
                        detailModal(model);
                    });
                // 作废
                self.$dataTable.on('click', '[data-handle="data-cancle"]',
                    function (e) {
                        var model = $(this).data();
                        var code = $(this).data('code')
                        cancleModal(model);
                    }
                );
                // 变更
                self.$dataTable.on('click', '[data-handle="data-change"]',
                    function (e) {
                        var editID = $(this).data('id');
                        var code = $(this).data('code');
                        var contractid = $(this).data('contractid');
                        swal({
                            title: '变更',
                            text: '您确认变更?',
                            type: 'info',
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            showCancelButton: true,
                            showConfirmButton: true,
                            showLoaderOnConfirm: true,
                            confirmButtonColor: '#ed5565'
                        }, function () {
                            window.location.href = '/admin/contract/createOrder?editID=' + editID + '&contractCode=' + code + '&contractId=' + contractid + '&change=';
                            e.stopPropagation();
                        });

                        e.stopPropagation();
                    });
                // 入库
                self.$dataTable.on('click', '[data-handle="data-storage"]',
                    function (e) {
                        var model = $(this).data();
                        var status = 'storage';
                        swal({
                            title: '确认入库？',
                            text: '将修改订货单为已入库状态',
                            type: 'info',
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            showCancelButton: true,
                            showConfirmButton: true,
                            showLoaderOnConfirm: true,
                            confirmButtonColor: '#ed5565',
                            closeOnConfirm: false
                        }, function () {
                            self.$http.get('/api/orderItem/' + model.id + '/updateStatus?status=' + status).then(function (res) {
                                if (res.data.code == 1) {
                                    Vue.toastr.success("操作成功");
                                    self.$dataTable.bootstrapTable('refresh');
                                } else {
                                    Vue.toastr.error(res.data.message);
                                }
                            }).catch(function () {

                            }).finally(function () {
                                swal.close();
                            });
                        });

                    }
                );
                // 填写计划安装时间
                self.$dataTable.on('click', '[data-handle="data-installdate"]',
                    function (e) {
                        var id = $(this).data("id");
                        installdateModel(id);
                    }
                );
            }
        }
    });

    // 作废弹窗
    function cancleModal(model) {
        var _modal = $('#cancleModal').clone();
        var $el = _modal.modal({
            height: 300,
            maxHeight: 350
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
                    ready: function () {
                        var self = this;
                        self.getReason();
                    },
                    data: {
                        id: model.id,
                        code: model.code,
                        status: model.status,
                        reason: '',
                        reasonSelect: []
                    },
                    methods: {
                        getReason: function () {
                            var self = this;
                            self.$http.get('/api/dict/reason/findAll').then(function (res) {
                                if (res.data.code == 1) {
                                    self.reasonSelect = res.data.data;
                                } else {
                                    Vue.toastr.error(res.data.message);
                                }
                            }).catch(function () {

                            }).finally(function () {
                            });
                        },
                        save: function () {
                            var self = this;
                            self.$validate(true,
                                function () {
                                    if (self.$validation.valid) {
                                        self.submitting = true;
                                        var data = {
                                            orderId: self.id,
                                            reason: self.reason
                                        }
                                        self.$http.post('/api/order/cancle', data, {emulateJSON: true}).then(function (res) {
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

    // 详情弹窗
    function detailModal(model) {
        var _modal = $('#detailModal').clone();
        var $el = _modal.modal({
            height: 500,
            maxHeight: 500,
            width: 1300
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
                    mixins: [RocoVueMixins.ModalMixin],
                    $modal: $el,
                    created: function () {
                    },
                    ready: function () {
                        this.getDetail();
                        this.drawTable();
                        this.findCustomerContract();
                    },
                    data: {
                        id: model.id,
                        table: '',
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
                            this.$http.get("/api/order/list?id=" + self.id).then(function (res) {
                                if (res.data.code == 1) {
                                    self.customerContract = res.data.data.rows[0];
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
                                        field: 'sku',
                                        title: '商品名称',
                                        width: '10%',
                                        orderable: true,
                                        align: 'center',
                                        formatter: function (value, row) {
                                            return value.name;
                                        }
                                    },
                                    {
                                        field: 'sku',
                                        title: '商品型号',
                                        width: '5%',
                                        orderable: false,
                                        align: 'center',
                                        formatter: function (value, row) {
                                            return value.product.model;
                                        }
                                    },
                                    {
                                        field: 'sku',
                                        title: '商品规格',
                                        width: '10%',
                                        orderable: false,
                                        align: 'center',
                                        formatter: function (value, row) {
                                            return value.product.spec;
                                        }
                                    },
                                    {
                                        field: 'sku',
                                        title: '属性值1',
                                        width: '5%',
                                        orderable: false,
                                        align: 'center',
                                        formatter: function (value, row) {
                                            return value.attribute1;
                                        }
                                    }, {
                                        field: 'sku',
                                        title: '属性值2',
                                        width: '5%',
                                        orderable: false,
                                        align: 'center',
                                        formatter: function (value, row) {
                                            return value.attribute2;
                                        }
                                    }, {
                                        field: 'sku',
                                        title: '属性值3',
                                        width: '5%',
                                        orderable: false,
                                        align: 'center',
                                        formatter: function (value, row) {
                                            return value.attribute3;
                                        }
                                    },
                                    {
                                        field: 'quantity',
                                        title: '订货数量',
                                        width: '3%',
                                        orderable: false,
                                        align: 'center',
                                        formatter: function (row, val) {
                                            if (val.tabletNum != null && val.tabletNum != 0) {
                                                return val.quantity + "m²" + "/" + val.tabletNum + "片";
                                            } else {
                                                var unit = "";
                                                if (val.specUnit != null) {
                                                    var str = val.specUnit.toString().split("/");
                                                    unit = str[1];
                                                }
                                                return val.quantity + " " + unit;
                                            }
                                        }
                                    }, {
                                        field: 'installationLocation',
                                        title: '安装位置',
                                        width: '10%',
                                        orderable: false,
                                        align: 'center'
                                    }, {
                                        field: 'noticeInstallDate',
                                        title: '通知安装时间',
                                        width: '8%',
                                        align: 'center',
                                        orderable: false,
                                    }, {
                                        field: 'installDate',
                                        title: '预计安装时间',
                                        width: '8%',
                                        align: 'center',
                                        orderable: false,
                                    }, {
                                        field: 'actualInstallDate',
                                        title: '实际安装时间',
                                        width: '8%',
                                        align: 'center',
                                        orderable: false,
                                    }, {
                                        field: 'payStatus',
                                        title: '支付状态',
                                        width: '5%',
                                        orderable: false,
                                        align: 'center',
                                        formatter: function (value, row) {
                                            if (value == 'NOT_PAIED') {
                                                return '待结算';
                                            } else {
                                                return '已结算';
                                            }
                                        }
                                    }, {
                                        field: 'status',
                                        title: '安装状态',
                                        width: '5%',
                                        align: 'center',
                                        orderable: false,
                                        formatter: function (data) {
                                            switch (data) {
                                                case "PENDING_INSTALLATION":
                                                    return "待安装";
                                                    break;
                                                case "ALREADY_INSTALLED":
                                                    return "已安装";
                                                    break;
                                                case "STAY_STORAGE":
                                                    return "待入库";
                                                    break;
                                                case "STORAGE":
                                                    return "已入库";
                                                    break;
                                            }
                                        }
                                    }, {
                                        field: 'otherFee',
                                        title: '其他费用',
                                        width: '5%',
                                        orderable: false,
                                        align: 'center'
                                    }, {
                                        field: 'note',
                                        title: '备注',
                                        width: '10%',
                                        orderable: false,
                                        align: 'center'
                                    }]
                            });


                            // 编辑
                            self.$dataTable.on('click', '[data-handle="data-edit"]',
                                function (e) {
                                    var model = $(this).data();
                                    editDetailModal(model);
                                }
                            );
                        },
                        getDetail: function () {
                            var self = this;
                            self.$http.get('/api/order/' + self.id + '/detail').then(function (res) {
                                if (res.data.code == 1) {
                                    self.table = res.data.data.orderItemList;
                                    console.log(self.table)
                                    self.$dataTable.bootstrapTable('load', self.table);
                                } else {
                                }
                            }).catch(function () {

                            }).finally(function () {
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
                    mixins: [RocoVueMixins.ModalMixin],
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

    //驳回弹窗
    function rejectModel(model) {
        var _$modal = $('#rejectModel').clone();
        var $modal = _$modal.modal({
            height: 300,
            width: 500,
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
                rejects: null,
                id: model.id,
                rejectType: '',
                remark: '',
                bh: model.status,//驳回
                code: model.code,
                contractCode: model.contractcode
            },
            methods: {
                //选择驳回类型
                findOtherFeeNoteList: function () {
                    var self = this;
                    self.$http.get('/api/system/dictionary/findByValue?dicValue=indent_order_reject').then(function (res) {
                        if (res.data.code == 1) {
                            self.rejects = res.data.data;
                        }
                    });
                },
                save: function () {
                    var self = this;
                    self.$validate(true,
                        function () {
                            self.submitting = true;
                            self.remark = $.trim(self.remark);
                            if (self.$validation.valid) {
                                self.$http.post('/api/supplierRejectRecord/updateRejectType?id=' + self.id + '&code=' + self.code + '&contractCode=' + self.contractCode + '&rejectType=' + self.rejectType + '&remark=' + self.remark + '&reject=' + self.bh).then(function (res) {
                                    if (res.data.code == 1) {
                                        self.$toastr.success('操作成功');
                                        swal.close();
                                        $modal.modal('hide');
                                        vueIndex.$dataTable.bootstrapTable('refresh');
                                    }
                                })
                            }
                        });
                }
            },
            created: function () {
                this.findOtherFeeNoteList();
            },
            ready: function () {
            }
        });
        // 创建的Vue对象应该被返回
        return showBill2;
    }

    function rejectRecordModel(id, sourceType) {
        var _$modal = $('#rejectRecordModel').clone();
        var $modal = _$modal.modal({
            height: 400,
            width: 700,
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
                rejectReasons: '',
                reject: 'REVIEWSIZE',
                rejectType: ''
            },
            methods: {
                getcontract: function () {
                    var self = this;
                    self.$http.get('/api/supplierRejectRecord/getRejectReason?id=' + id + '&sourceType=' + sourceType).then(function (res) {
                        if (res.data.code == 1) {
                            self.rejectReasons = res.data.data;
                        }
                    }).catch(function () {

                    }).finally(function () {

                    });
                }
            },
            created: function () {
            },
            ready: function () {
                this.getcontract();
            }
        });
        // 创建的Vue对象应该被返回
        return showBill2;
    }

    function installdateModel(id) {
        var _$modal = $('#installdateModel').clone();
        var $modal = _$modal.modal({
            height: 300,
            width: 500,
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
                id: id,
                installDate: ''
            },
            methods: {
                activeDatePicker: function () {
                    $(this.$els.installDate).datetimepicker({
                        format: 'yyyy-mm-dd'
                    });
                },
                save: function () {
                    var self = this;
                    self.$validate(true,
                        function () {
                            self.submitting = true;
                            if (self.$validation.valid) {
                                self.$http.get('/api/orderItem/' + id + '/updateInstallDate?installDate=' + self.installDate).then(function (res) {
                                    if (res.data.code == 1) {
                                        self.$toastr.success('操作成功');
                                        $modal.modal('hide');
                                        vueIndex.$dataTable.bootstrapTable('refresh');
                                    }
                                })
                            }
                        });
                }
            },
            created: function () {
            },
            ready: function () {
                this.activeDatePicker();
            }
        });
        // 创建的Vue对象应该被返回
        return showBill2;
    }

    function installSubmitModel(model) {
        var _$modal = $('#installRejectModel').clone();
        var $el = _$modal.modal({
            height: 300,
            width: 500,
        });
        // 获取 node
        $el.on('shown.bs.modal',
            function () {
                var el = $el.get(0);
                // 创建 Vue 对象编译节点
                var installObject = new Vue({
                    el: el,
                    // 模式窗体必须引用 ModalMixin
                    mixins: [RocoVueMixins.ModalMixin],
                    components: {
                        'web-uploader': RocoVueComponents.WebUploaderComponent
                    },
                    //$modal: $modal, // 模式窗体 jQuery 对象
                    $modal: $el,
                    created: function () {
                    },
                    data: {
                        contractCode: model.contractcode,
                        orderId: model.id,
                        orderCode: model.code,
                        remark: '',
                        images: [],
                        uploading: false,
                        progress: 0,
                        webUploader: {
                            type: 'SUPPLIER',
                            formData: {
                                type: 'SUPPLIER'
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
                            if (self.images.length < 1) {
                                Vue.toastr.warning("至少需要上传一张图片！");
                                return false;
                            }
                            var lists = [];
                            var review;
                            var imgUrls = '';
                            self.images.forEach(function (item) {
                                imgUrls += item.imagePath + ','
                            })
                            review = {
                                orderId: self.orderId,
                                remark: self.remark,
                                orderCode: self.orderCode,
                                contractCode: self.contractCode,
                                orderInstallImg: imgUrls.length > 0 ? imgUrls.substring(0, imgUrls.length - 1) : ''
                            }
                            lists.push(review);
                            self.$validate(true,
                                function () {
                                    if (self.$validation.valid) {
                                        swal({
                                            title: '确认安装？',
                                            text: '将修改订货单为安装状态',
                                            type: 'info',
                                            confirmButtonText: '确定',
                                            cancelButtonText: '取消',
                                            showCancelButton: true,
                                            showConfirmButton: true,
                                            showLoaderOnConfirm: true,
                                            confirmButtonColor: '#ed5565',
                                            closeOnConfirm: false
                                        }, function () {
                                            self.$http.post('/api/order/orderInstallData', lists).then(function (res) {
                                                if (res.data.code == 1) {
                                                    Vue.toastr.success("操作成功");
                                                    $el.modal('hide');
                                                    vueIndex.$dataTable.bootstrapTable('refresh');
                                                } else {
                                                    Vue.toastr.error(res.data.message);
                                                }
                                            }).catch(function () {

                                            }).finally(function () {
                                                swal.close();
                                            });
                                        });
                                    }
                                });
                        },
                        deleteImg: function (url, index) {
                            var self = this;
                            self.$http.get('/api/order/delete', {
                                params: {
                                    path: url
                                }
                            }).then(function (res) {
                                if (res.data.code == 1) {
                                    self.images.splice(index, 1)
                                }
                            }).finally(function () {
                            })
                        },
                    },
                    events: {
                        // 上传成功
                        'webupload-upload-success-SUPPLIER': function (file, res) {
                            if (res.code == 1) {
                                this.$toastr.success('上传成功');
                                var obj = {
                                    imagePath: res.data.path,
                                    fullPath: res.data.fullPath
                                }
                                this.images.push(obj);
                            } else {
                                this.$toastr.error(res.message);
                            }
                        },
                        // 上传进度
                        'webupload-upload-progress-SUPPLIER': function (file, percentage) {
                            this.progress = percentage * 100;
                        },
                        // 上传结束
                        'webupload-upload-complete-SUPPLIER': function (file) {
                            this.uploading = false;
                        },
                        // 上传开始
                        'webupload-upload-start-SUPPLIER': function (file) {
                            this.uploading = true;
                        }
                    }
                });
                // 创建的Vue对象应该被返回
                return installObject;
            });
    }

    function checkInstallModel(model) {
        var _$modal = $('#checkInstallEndModel').clone();
        var $modal = _$modal.modal({
            height: 500,
            width: 800,
        });
        // 获取 node
        var el = $modal.get(0);
        // 创建 Vue 对象编译节点
        var approveInstallDataVue = new Vue({
            el: el,
            // 模式窗体必须引用 ModalMixin
            mixins: [RocoVueMixins.ModalMixin],
            $modal: $modal, // 模式窗体 jQuery 对象
            data: {
                approvalOpinion: true,
                showRejectInfo: false,
                installDatas: '',
                img: '',
                rejectType: '',
                remark: '',
                bh: 'INSTALLCHECK',
                rejects: ''
            },
            methods: {
                approvalOpinionChange: function () {
                    var self = this;
                    if (self.approvalOpinion) {
                        self.showRejectInfo = false;
                    } else {
                        self.showRejectInfo = true;
                    }
                },
                //选择驳回类型
                findOtherFeeNoteList: function () {
                    var self = this;
                    self.$http.get('/api/system/dictionary/findByValue?dicValue=indent_order_reject').then(function (res) {
                        if (res.data.code == 1) {
                            self.rejects = res.data.data;
                        }
                    });
                },
                bigImg: function (fullpath) {
                    imgModel(fullpath);
                },
                getInstallData: function () {
                    var self = this;
                    self.$http.get('/api/orderInstallData/getByOrderId?orderId=' + model.id).then(function (res) {
                        if (res.data.code == 1) {
                            self.installDatas = res.data.data;
                            var imagesPath = self.installDatas.orderInstallImg.split(",");
                            var imageObject = [];
                            imagesPath.forEach(function (item) {
                                imageObject.push(
                                    {
                                        imagePath: item,
                                        fullPath: ctx + "/imgFile/" + item
                                    }
                                );
                            });
                            self.img = imageObject;
                        }
                    }).catch(function () {

                    }).finally(function () {

                    });
                },
                submitFun: function () {
                    var self = this;

                    if (self.approvalOpinion) {
                        swal({
                            title: '确认提交？',
                            type: 'info',
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            showCancelButton: true,
                            showConfirmButton: true,
                            showLoaderOnConfirm: true,
                            confirmButtonColor: '#ed5565',
                            closeOnConfirm: false
                        }, function () {
                            self.$http.get('/api/orderInstallData/updateOrderStatus?orderId=' + model.id + '&orderCode=' + model.code + '&contractcode=' + model.contractcode).then(function (res) {
                                if (res.data.code == 1) {
                                    Vue.toastr.success("操作成功");
                                    $modal.modal('hide');
                                    vueIndex.$dataTable.bootstrapTable('refresh');
                                }
                            }).catch(function () {
                            }).finally(function () {
                                swal.close();
                            });
                        })
                    } else {
                        self.$validate(true, function () {
                            if (self.$validation.valid) {
                                swal({
                                    title: '确认提交？',
                                    type: 'info',
                                    confirmButtonText: '确定',
                                    cancelButtonText: '取消',
                                    showCancelButton: true,
                                    showConfirmButton: true,
                                    showLoaderOnConfirm: true,
                                    confirmButtonColor: '#ed5565',
                                    closeOnConfirm: false
                                }, function () {
                                    self.$http.get('/api/supplierRejectRecord/updateRejectType?id=' + model.id + '&code=' + model.code + '&contractCode=' + model.contractcode + '&rejectType=' + self.rejectType + '&remark=' + self.remark + '&reject=' + self.bh).then(function (res) {
                                        if (res.data.code == 1) {
                                            Vue.toastr.success("操作成功");
                                            $modal.modal('hide');
                                            vueIndex.$dataTable.bootstrapTable('refresh');
                                        }
                                    }).catch(function () {

                                    }).finally(function () {
                                        swal.close();
                                    });
                                })
                            }
                        })
                    }
                }
            },
            created: function () {
                this.findOtherFeeNoteList();
            },
            ready: function () {
                this.getInstallData();
            }
        });
        // 创建的Vue对象应该被返回
        return approveInstallDataVue;
    }

    function imgModel(fullPath) {
        var _$modal = $('#imgModel').clone();
        var $modal = _$modal.modal({
            height: 440,
            width: 750,
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
            methods: {},
            created: function () {
            },
            ready: function () {
            }
        });
        // 创建的Vue对象应该被返回
        return showBill2;
    }

})();