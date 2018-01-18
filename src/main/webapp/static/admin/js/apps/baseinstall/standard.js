(function() {
    var base = Vue.extend({
        template: '#stand',
        props: {
            contractId: ''
        },
        data: function() {
            return {
                baseMain: {
                    contractId: this.contractId, // 合同号
                    standardProjectName: '', // 项目名称
                    location:'',//位置
                    brandMeal: '', // 品牌及套餐
                    model: '', // 型号
                    spec:'',// 规格
                    unit:'', //单位
                    quantity:'',//数量
                    wastageQuantity:'',  //含损耗数量
                    actualQuantity:'',  //实发数量
                    remark :'' //备注
                },
                submitting: false, // 正在保存
                list: [] // 列表
            }
        },
        created: function() {
            this.fetchList()
        },
        methods: {
            // 保存
            save: function() {
                var self = this;
                self.submitBtnClick = true;
                self.$validate(true, function () {
                    if (self.$validation.valid) {
                        self.disabled = true;
                        self.$http.post('/api/material/standardConfig/save', self.baseMain)
                            .then(function(res) {
                                if (res.data.code == 1) {
                                    Vue.toastr.success('操作成功');
                                    self.baseMain = {
                                        contractId: this.contractId, // 合同号
                                        standardProjectName: '', // 项目名称
                                        location:'',//位置
                                        brandMeal: '', // 品牌及套餐
                                        model: '', // 型号
                                        spec:'',// 规格
                                        unit:'', //单位
                                        quantity:'',//数量
                                        wastageQuantity:'',  //含损耗数量
                                        actualQuantity:'',  //实发数量
                                        remark :'' //备注
                                    }
                                    // 刷新列表
                                    self.fetchList()
                                }
                                else {
                                    Vue.toastr.error(res.data.message);
                                }
                            })
                    }
                })
            },
            // 获取详情
            edit: function(item) {
                var self = this;
                self.$http.get('/api/material/standardConfig/' + item.id)
                    .then(function(res) {
                        if (res.data.code == 1) {
                            self.baseMain = res.data.data
                        }
                    })
            },
            // 删除
            delete: function(item) {
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
                    self.$http.get('/api/material/standardConfig/delete?id=' + item.id)
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
            fetchList: function() {
                var self = this;
                self.$http.get('/api/material/standardConfig/findByContractId?contractId=' + self.contractId)
                    .then(function(res) {
                        if (res.data.code == 1) {
                            self.list = res.data.data
                        }
                    })
            }
        }
    })

    Vue.component('basedata-stand', base);
})()