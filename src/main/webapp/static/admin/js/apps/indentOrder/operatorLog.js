var tt = null;
var queryCount = 0;
+(function (window, RocoUtils) {
    $('#order').addClass('active');
    $('#operatorLog').addClass('active');
    tt = new Vue({
        el: '#container',
        data: {
            // 面包屑
            breadcrumbs: [{
                path: '/',
                name: '主页'
             },{
                path: '/',
                name: '订货单'
            }, {
                path: '/',
                name: '操作日志',
                active: true //激活面包屑的
            }],
            $dataTable: null,
            form: {
                keyword: '',
            },
        },

        methods: {

            drawTable: function () {
                var self = this;
                self.$dataTable = $(this.$els.datatable).bootstrapTable({
                    url: '/api/operatorLog/findall',
                    method: 'get',
                    dataType: 'json',
                    cache: false, // 去缓存
                    pagination: true, // 是否分页
                    sidePagination: 'server', // 客户端分页
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
                            field: 'operator',
                            title: '操作人',
                            align: 'center',
                        },
                        {
                            field: 'operatorTime',
                            title: '操作时间',
                            align: 'center',
                            formatter:function (value) {
                                if (value != null) {
                                    return moment(value).format('YYYY-MM-DD HH:mm:ss');
                                }
                            }
                        },
                        {
                            field: 'operatorExplain',
                            title: '操作说明',
                            align: 'center',
                            formatter:function (value,row) {
                                if(row.orderId){
                                    return  row.operator+"对项目【"+row.contractCode+"】"+
                                        row.operatorExplain+"，订单号为：【"+row.orderId+"】";
                                }
                                return  row.operator+"对项目【"+row.contractCode+"】"+
                                    row.operatorExplain;
                            }
                        }]

                });


            },

            //点击搜索功能
            query: function () {
                this.$dataTable.bootstrapTable('selectPage', 1);
            },


        },

        created: function () {
        },
        ready: function () {
            this.drawTable();
        },
    });
})();