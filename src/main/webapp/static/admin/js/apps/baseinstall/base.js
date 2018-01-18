(function () {
    var base = Vue.extend({
        template: '#basehome',
        props: {
            contractId: ''
        },
        data: function () {
            return {
                baseMain: {
                    contractId: this.contractId, // 合同号
                    projectName: '', // 项目名称
                    unit: '', // 单位
                    quantity: '', // 数量
                    wastage: '', // 损耗
                    mainMetarialPrice: '', // 主材价格
                    accessoriesMetarialPrice: '', // 辅材单价
                    artificialFee: '', // 人工费
                    price: '', // 人工费
                    feeTotal: '', // 费用合计
                    feeType: 'ADD', // 费用类型（基装综合，减项，增项）
                    remarks: '' // 基装说明
                },
                submitting: false, // 正在保存
                list: [], // 列表,
                addTotal: 0.00,
                reduceTotal: 0.00,
                comprehensiveTotal: 0.00,
                totalVal: 0.00
            }
        },
        created: function () {
            this.fetchList();
            this.feeSummaryForType();
        },
        methods: {
            // 保存
            save: function () {
                var self = this;
                self.submitBtnClick = true;
                self.$validate(true, function () {

                    if (self.$validation.valid && self.baseMain.feeType) {
                        self.disabled = true;
                        self.baseMain.metarialType = 'BASE';

                        self.$http.post('/api/material/baseMain/save', self.baseMain)
                            .then(function (res) {
                                if (res.data.code == 1) {
                                    Vue.toastr.success('操作成功');

                                    self.baseMain = {
                                        contractId: this.contractId, // 合同号
                                        projectName: '', // 项目名称
                                        unit: '', // 单位
                                        quantity: '', // 数量
                                        wastage: '', // 损耗
                                        mainMetarialPrice: '', // 主材价格
                                        accessoriesMetarialPrice: '', // 辅材单价
                                        artificialFee: '', // 人工费
                                        price: '', // 人工费
                                        feeTotal: '', // 费用合计
                                        feeType: 'ADD', // 费用类型（基装综合，减项，增项）
                                        remarks: '' // 基装说明
                                    }

                                    // 刷新列表
                                    self.fetchList();
                                    self.feeSummaryForType();
                                }
                                else {
                                    Vue.toastr.error(res.data.message);
                                }
                            })
                    }
                })
            },
            // 获取详情
            edit: function (item) {
                var self = this;
                self.$http.get('/api/material/baseMain/' + item.id)
                    .then(function (res) {
                        if (res.data.code == 1) {
                            self.baseMain = res.data.data
                        }
                    })
            },
            // 删除
            delete: function (item) {
                var self = this;
                swal({
                    title: '确认删除？',
                    text: '将删除该记录！',
                    type: 'info',
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    showCancelButton: true,
                    showConfirmButton: true,
                    showLoaderOnConfirm: true,
                    confirmButtonColor: '#ed5565',
                    closeOnConfirm: false
                }, function () {
                    self.$http.get('/api/material/baseMain/delete?id=' + item.id)
                        .then(function (res) {
                            if (res.data.code == 1) {
                                self.fetchList();
                                self.feeSummaryForType();
                            }
                        }).catch(function () {

                    }).finally(function () {
                        swal.close();
                    });
                });
            },
            // 获取列表
            fetchList: function () {
                var self = this;
                self.$http.get('/api/material/baseMain/findAll?contractId=' + self.contractId + '&metarialType=BASE')
                    .then(function (res) {
                        if (res.data.code == 1) {
                            self.list = res.data.data;
                        }
                    });
            },
            feeSummaryForType: function () {
                var self = this;
                self.$http.get('/api/material/baseMain/feeSummaryForType?contractId=' + self.contractId + '&metarialType=BASE')
                    .then(function (res) {
                        if (res.data.code == 1) {
                            self.addTotal = res.data.data.addTotal != null ? res.data.data.addTotal : 0.00;
                            self.reduceTotal = res.data.data.reduceTotal != null ? res.data.data.reduceTotal : 0.00;
                            self.comprehensiveTotal = res.data.data.comprehensiveTotal != null ? res.data.data.comprehensiveTotal : 0.00;
                            self.totalVal = res.data.data.totalVal != null ? res.data.data.totalVal : 0.00;
                        }
                    });
            }
        }
    })

    Vue.component('basedata-basehome', base);
})()