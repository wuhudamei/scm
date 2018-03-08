var tt = null;
var queryCount = 0;
+(function (window, DaMeiUtils) {
    $('#order').addClass('active');
    $('#isAccountChecking').addClass('active');
    tt = new Vue({
        el: '#container',
        validators: {
            laterThanStart: function (val, startDate) {
                try {
                    var end = moment(val);
                    return end.isBefore(startDate) ? false : true;
                } catch (e) {
                    return false;
                }
            }
        },
        data: {
            // 面包屑
            breadcrumbs: [{
                path: '/admin',
                name: '主页'
            }, {
                path: '/',
                name: '已对账',
                active: true //激活面包屑的
            }],
            totalPrice: 0.00,
            skuTotalPrice: 0.00,
            otherPrice: 0.00,
            $dataTable: null,
            form: {
                keyword: '',
                startTime: '',
                endTime: ''
            },
            totalAmount: 0
        },
        computed: {
            selection: function () {
                for (var i = 0; i < this.YX.length; i++) {
                    if (this.YX[i].text === this.A) {
                        return this.YX[i].ZY;
                    }
                }
            }
        },
        methods: {

            drawTable: function () {
                var self = this;
                self.$dataTable = $(this.$els.datatable).bootstrapTable({
                    url: '/api/reconciliation/findByPayTime',
                    method: 'get',
                    dataType: 'json',
                    cache: false, // 去缓存
                    pagination: false, // 是否分页
                    sidePagination: 'client', // 客户端分页
                    content: '',
                    queryParams: function (params) {
                        // 将table 参数与搜索表单参数合并
                        return _.extend({}, params, self.form);
                    },
                    mobileResponsive: true,
                    undefinedText: '-', // 空数据的默认显示字符
                    striped: true, // 斑马线
                    sortName: 'id', // 默认排序列名
                    sortOrder: 'desc', // 默认排序方式
                    columns: [
                        {
                            field: 'contractCode',
                            title: '项目编号',
                            align: 'center',
                        },
                        {
                            field: 'orderCode',
                            title: '订货单号',
                            align: 'center',
                        },
                        {
                            field: 'skuName',
                            title: '商品名称',
                            align: 'center',
                        },
                        {
                            field: 'model',
                            title: '商品型号',
                            align: 'center',

                        },
                        {
                            field: 'spec',
                            title: '商品规格',
                            width: '10%',
                            align: 'center',
                        },
                        {
                            field: 'attribute1',
                            title: '商品属性1',
                            align: 'center',
                        },
                        {
                            field: 'attribute2',
                            title: '商品属性2',
                            align: 'center',
                        },
                        {
                            field: 'attribute3',
                            title: '商品属性3',
                            align: 'center',
                        },
                        {
                            field: 'quantity',
                            title: '数量',
                            align: 'center',

                        },
                        {
                            field: 'supplyPrice',
                            title: "单价",
                            align: 'center',
                            valign: 'middle',
                            formatter: function (value, row) {
                                self.skuTotalPrice += row.supplyPrice * row.quantity;
                                return value;
                            }

                        }, {
                            field: 'otherFee',
                            title: "其他费用",
                            align: 'center',
                            valign: 'middle',
                            formatter: function (value) {
                                if (value == null) {
                                    return otherPrice = 0.00;
                                } else {
                                    self.otherPrice += value;
                                    return value;
                                }
                            }

                        }, {
                            field: 'totalMoney',
                            title: "总价",
                            align: 'center',
                            valign: 'middle',
                            formatter: function (value) {
                                self.totalPrice += value;
                                return value;
                            }

                        }, {
                            field: 'payStatus',
                            title: "状态",
                            align: 'center',
                            valign: 'middle',
                            formatter: function (value) {
                                switch (value) {
                                    case 'NOT_PAIED':
                                        return '未对账';
                                        break;
                                    case 'PAIED':
                                        return '已对账'
                                        break;
                                    default:
                                        return '';
                                        break;
                                }
                            }
                        }, {
                            field: 'operatName',
                            title: "操作人",
                            align: 'center',
                            valign: 'middle',

                        }]

                });


            },
            activeDate: function () {
                $(this.$els.startTime).datetimepicker({
                    minView: 2,
                    format: 'yyyy-mm-dd',
                    weekStart: 1,
                    todayBtn: true
                });
                $(this.$els.endTime).datetimepicker({
                    minView: 2,
                    format: 'yyyy-mm-dd',
                    weekStart: 1,
                    todayBtn: true
                });
            },
            //点击搜索功能
            query: function () {
                if (queryCount > 0) {
                    this.$dataTable.bootstrapTable('refresh', {pageNumber: 1});
                    this.totalPrice = 0.00;
                    this.skuTotalPrice = 0.00;
                    this.otherPrice = 0.00;
                } else {
                    this.drawTable();
                    queryCount++;
                }
            },


        },

        created: function () {
        },
        ready: function () {
            this.activeDate();
            this.drawTable();
        },
    });
})();