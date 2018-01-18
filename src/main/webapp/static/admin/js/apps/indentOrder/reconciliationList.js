+(function () {
    $('#order').addClass('active');
    $('#reconciliationList').addClass('active');

    var Detail = null;
    var vueIndex = new Vue({
        el: '#container',
        mixins: [RocoVueMixins.DataTableMixin],
        data: {
            breadcrumbs: [{
                path: '/admin',
                name: '主页'
            },
                {
                    path: '/',
                    name: '订货单',
                    active: true
                }],
            form: {
                keyword: '',
                status: '',
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
            this.drawTable();
        },
        methods: {
            query: function () {
                this.$dataTable.bootstrapTable('selectPage', 1);
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/api/order/reconciliationList',
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
                            field: 'customerPhone',
                            title: '客户手机',
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
                            visible:false,
                            orderable: false,
                            align: 'center',
                            formatter: function (val) {
                                return val.name;
                            }
                        }, {
                            field: 'placeEnum',
                            title: '制单类型',
                            orderable: false,
                            visible:false,
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
                            visible:false,
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
                        },{
                            field: 'acceptDate',
                            title: '接单时间',
                            orderable: false,
                            visible:false,
                            align: 'center'
                        },{
                            field: 'downloadNumber',
                            title: '下载状态',
                            orderable: false,
                            visible:false,
                            align: 'center',
                            formatter: function (value) {
                                switch (value) {
                                    case 0:
                                        return "未下载";
                                        break;
                                    default:
                                        return "已下载（"+value+"）";
                                        break;
                                }
                            }
                        },
                        {
                            field: 'downloadDate',
                            title: '下载时间',
                            visible:false,
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'status',
                            title: '对账状态',
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
                            align: 'center',
                            visible:false
                        }, {
                            field: 'noticeInstallTime',
                            title: '通知安装时间',
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'actualInstallationTime',
                            title: '实际安装时间',
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'reconciliationTime',
                            title: '对账时间',
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'reconciliationRemarks',
                            title: '对账备注',
                            orderable: false,
                            align: 'center'
                        },{
                            field: 'id',
                            title: '操作',
                            orderable: false,
                            align: 'center',
                            formatter: function (value, row) {
                                if (row) {
                                    var html = '';
                                    //对账操作
                                    if (RocoUtils.hasPermission('reconciliation:edit')) {
                                        if(row.status!='HASBEENRECONCILED'){
                                            html += '<button data-handle="data-reconciliation" data-id="' + row.id + '"  data-code="' + row.contractCode + '" data-status="' + row.status + '"  data-ordercode="' + row.code + '"class="m-r-xs btn btn-xs btn-primary" type="button">标记对账</button>';
                                        }
                                    }
                                    /*//下载
                                     html += '<button data-handle="data-download" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-dark" type="button">下载</button>';*/
                                    return html;
                                }
                            }
                        }]
                });
                // 下载
                self.$dataTable.on('click', '[data-handle="data-download"]',
                    function () {
                        var id = $(this).data("id");
                        window.location.href = '/api/order/export?id='+id;
                        setTimeout(function () {
                            self.$dataTable.bootstrapTable('refresh');
                        },1000);

                    }
                );
                // 标记对账
                self.$dataTable.on('click', '[data-handle="data-reconciliation"]',
                    function () {
                        var model = $(this).data();
                        detailModal(model);
                    }
                );
                // 标记对账
                self.$dataTable.on('click', '[data-handle="data-detail"]',
                    function () {
                        var model = $(this).data();
                        orderDetailModal(model);
                    }
                );
            }
        }
    });


    // 详情弹窗
    function detailModal(model) {
        var _modal = $('#detailModal').clone();
        var $el = _modal.modal({
            height: 500,
            width: 1500
        });
        $el.on('shown.bs.modal',
            function () {
                var el = $el.get(0);
                Detail = new Vue({
                    el: el,
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
                        remarks:'',
                        id: model.id,
                        contractCode:model.code,
                        code:model.ordercode,
                        status:model.status,
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
                            this.$http.get("/api/order/listReconciliation?id=" + self.id).then(function (res) {
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
                                                if(val.specUnit != null){
                                                    var str = val.specUnit.toString().split("/");
                                                    unit = str[1];
                                                }
                                                return val.quantity + " "+unit;
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
                                        visible:false,
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
                                        visible:false,
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
                        },
                        getDetail: function () {
                            var self = this;
                            self.$http.get('/api/order/' + self.id + '/detail').then(function (res) {
                                if (res.data.code == 1) {
                                    self.table = res.data.data.orderItemList;
                                    self.remarks=res.data.data.reconciliationRemarks;
                                    self.$dataTable.bootstrapTable('load', self.table);
                                } else {
                                }
                            }).catch(function () {

                            }).finally(function () {
                            });
                        },
                        //保存对账信息
                        save:function (val) {
                            var self = this;
                            if(val=="PARTIALRECONCILIATION"){
                                if(self.remarks==''||self.remarks==undefined){
                                    swal({
                                            title: '提示',
                                            text: '部分对账必须填写备注？',
                                            type: 'warning',
                                            confirmButtonText: '确定',
                                            cancelButtonText: '取消',
                                            showCancelButton: true,
                                            showConfirmButton: true,
                                            showLoaderOnConfirm: true,
                                            confirmButtonColor: '#ed5565',
                                            closeOnConfirm: true
                                        },
                                        function () {
                                                swal.close();
                                        });
                                    return;
                                }
                            }
                            var indentOrder={ id:self.id,
                                status:val,
                                reconciliationRemarks:self.remarks,
                                contractCode:self.contractCode,
                                code:self.code
                            };
                            self.$http.post('/api/order/reconciliation',indentOrder).then(function (res) {
                                if (res.data.code == 1) {
                                    toastr.success(res.data.message);
                                    $el.modal('hide');
                                    vueIndex.$dataTable.bootstrapTable('refresh');
                                    self.$destroy();
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

    // 详情弹窗
    function orderDetailModal(model) {
        var _modal = $('#orderDetailModal').clone();
        var $el = _modal.modal({
            height: 500,
            width: 1200
        });
        $el.on('shown.bs.modal',
            function () {
                var el = $el.get(0);
                Detail = new Vue({
                    el: el,
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
                        remarks:'',
                        id: model.id,
                        contractCode:model.code,
                        code:model.ordercode,
                        status:model.status,
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
                            this.$http.get("/api/order/listReconciliation?id=" + self.id).then(function (res) {
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
                                                if(val.specUnit != null){
                                                    var str = val.specUnit.toString().split("/");
                                                    unit = str[1];
                                                }
                                                return val.quantity + " "+unit;
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
                                        visible:false,
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
                                        visible:false,
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
                        },
                        getDetail: function () {
                            var self = this;
                            self.$http.get('/api/order/' + self.id + '/detail').then(function (res) {
                                if (res.data.code == 1) {
                                    self.table = res.data.data.orderItemList;
                                    self.remarks=res.data.data.reconciliationRemarks;
                                    self.$dataTable.bootstrapTable('load', self.table);
                                } else {
                                }
                            }).catch(function () {

                            }).finally(function () {
                            });
                        },
                        //保存对账信息
                        save:function (val) {
                            var self = this;
                            if(val=="PARTIALRECONCILIATION"){
                                if(self.remarks==''||self.remarks==undefined){
                                    swal({
                                            title: '提示',
                                            text: '部分对账必须填写备注？',
                                            type: 'warning',
                                            confirmButtonText: '确定',
                                            cancelButtonText: '取消',
                                            showCancelButton: true,
                                            showConfirmButton: true,
                                            showLoaderOnConfirm: true,
                                            confirmButtonColor: '#ed5565',
                                            closeOnConfirm: true
                                        },
                                        function () {
                                            swal.close();
                                        });
                                    return;
                                }
                            }
                            var indentOrder={ id:self.id,
                                status:val,
                                reconciliationRemarks:self.remarks,
                                contractCode:self.contractCode,
                                code:self.code
                            };
                            self.$http.post('/api/order/reconciliation',indentOrder).then(function (res) {
                                if (res.data.code == 1) {
                                    toastr.success(res.data.message);
                                    $el.modal('hide');
                                    vueIndex.$dataTable.bootstrapTable('refresh');
                                    self.$destroy();
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


})();