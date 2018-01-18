(function() {
    var base = Vue.extend({
        template: '#changemain',
        mixins: [RocoVueMixins.DataTableMixin],
        props: {
            contractId: ''
        },
        data: function() {
            return {
                flag:true,
                detailFlag:false,
                changeId:'',
                baseMain: {
                    contractId: this.contractId, // 合同号
                    changeDate: '', // 日期
                    changeOrderNumber: '', // 变更单序号
                    describation: '' // 描述
                },
                baseMainDetail: {
                    changeId:this.changeId,//修改id
                    materialType:'BASECHANGE',// 主材基装类型
                    projectName:'',// 项目名称
                    location:'',//  位置
                    brand :'',//  品牌
                    unit :'',//  单位
                    amount :'',//  数量
                    specification:'',//  规格
                    model :'',//  型号
                    price :'',//  单价
                    total:'',//  合计
                    description:'', //  工艺说明及材料说明,
                    changeType:'MINUSITEM',  //增,减,赔付
                    wastageCost:'', //损耗
                    materialCost:'',//材料费
                    laborCost:'',   //人工费
                    holeHigh:'',    //洞口尺寸_高
                    holeWide:'',    //洞口尺寸_宽
                    holeThuck:'',   //洞口尺寸_厚
                    addStack:''     //加垛
                },
                submitting: false, // 正在保存
                list: [] ,// 列表
                listDetail: [] // 详情列表
            }
        },
        created: function() {
            this.fetchList()
        },
        ready:function(){
            this.activeDatepicker();
        },
        methods: {
            // 日历初始化
            activeDatepicker: function () {
                var self = this;
                $(self.$els.changeDate).datetimepicker('setStartDate','');
            },
            // 保存
            save: function() {
                var self = this;
                self.submitBtnClick = true;
                self.$validate(true, function () {
                    if (self.$validation.valid ) {
                        self.disabled = true;
                        self.$http.post('/api/material/change/save', JSON.stringify(self.baseMain)  )
                            .then(function(res) {
                                if (res.data.code == 1) {
                                    Vue.toastr.success('操作成功');
                                    self.baseMain = {
                                        contractId: this.contractId, // 合同号
                                        changeDate: '', // 日期
                                        changeOrderNumber: '', // 变更单序号
                                        describation: '' // 描述
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
                self.$http.get('/api/material/change/' + item.id)
                    .then(function(res) {
                        if (res.data.code == 1) {
                            self.baseMain = res.data.data;
                            self.baseMain.changeDate =  moment(res.data.data.changeDate).format('YYYY-MM-DD');
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
                    self.$http.get('/api/material/change/delete?id=' + item.id)
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
                self.$http.get('/api/material/change/findAll?contractId=' + self.contractId)
                    .then(function(res) {
                        if (res.data.code == 1) {
                            self.list = res.data.data
                        }
                    })
            },
            // 获取详情列表
            detailList: function(item) {
                var self = this;
               self.changeId=item.id;
                self.fetchDetailList(item.id);
            },
            //详情的方法
            // 保存
            saveDetail: function() {
                var self = this;
                self.submitBtnClick = true;
                self.$validate(true, function () {
                    if (self.$validation1.valid ) {
                        self.disabled = true;
                        var changeId = self.changeId;
                        self.baseMainDetail.changeId=changeId;
                        self.$http.post('/api/material/changeDetail/save', self.baseMainDetail)
                            .then(function(res) {
                                if (res.data.code == 1) {
                                    Vue.toastr.success('操作成功');
                                    self.baseMainDetail = {
                                        changeId:changeId,//修改id
                                        materialType:'BASECHANGE',// 主材基装类型
                                        projectName:'',// 项目名称
                                        location:'',//  位置
                                        brand :'',//  品牌
                                        unit :'',//  单位
                                        amount :'',//  数量
                                        specification:'',//  规格
                                        model :'',//  型号
                                        price :'',//  单价
                                        total:'',//  合计
                                        description:'', //  工艺说明及材料说明,
                                        changeType:'MINUSITEM',  //增减项
                                        wastageCost:'', //损耗
                                        materialCost:'',//材料费
                                        laborCost:'',   //人工费
                                        holeHigh:'',    //洞口尺寸_高
                                        holeWide:'',    //洞口尺寸_宽
                                        holeThuck:'',   //洞口尺寸_厚
                                        addStack:''     //加垛
                                    }
                                    // 刷新列表
                                    self.fetchDetailList(changeId)
                                }
                                else {
                                    Vue.toastr.error(res.data.message);
                                }
                            })
                    }
                })
            },
            // 获取详情
            editDetail: function(item) {
                var self = this;
                self.$http.get('/api/material/changeDetail/' + item.id)
                    .then(function(res) {
                        if (res.data.code == 1) {
                            self.baseMainDetail = res.data.data
                        }
                    })
            },
            // 删除
            deleteDetail: function (item) {
                var self = this;
                var changeId = self.changeId;
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
                    self.$http.get('/api/material/changeDetail/delete?id=' + item.id)
                        .then(function (res) {
                            if (res.data.code == 1) {
                                self.fetchDetailList(changeId)
                            }
                        }).catch(function () {

                    }).finally(function () {
                        swal.close();
                    });
                });
            },
            // 获取列表
            fetchDetailList: function(id) {
                var self = this;
                self.$http.get('/api/material/changeDetail/findByChangeId?changeId=' + id)
                    .then(function(res) {
                        if (res.data.code == 1) {
                            self.list = res.data.data
                            self.detailFlag=true;
                            self.flag=false;
                        }
                    })
            },
            back:function () {
                var self=this;
                this.fetchList();
                this.detailFlag = false;
                this.flag= true;
            }
        }
    })
    Vue.component('basedata-change', base);
})()