var tt = null;
var idArr = [];
+(function (window, RocoUtils) {
    $('#order').addClass('active');
    $('#accountChecking').addClass('active');
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
            supplierFlag: {},
            acctType:acctType,
            // 面包屑
            breadcrumbs: [{
                path: '/',
                name: '主页'
            }, {
                path: '/',
                name: '未对账',
                active: true //激活面包屑的
            }],

            $dataTable: null,
            form: {
                startTime: '',
                endTime: ''
            },
            supplierId: '',
            stRdBilltems: null,
            checkAllValue: false,   //全选按钮
            checkArr: [],     //选中的数组
            totalAmount: 0,
            pdcount: 0
        },

        computed: {
            selection: function () {
                for (var i = 0; i < this.YX.length; i++) {
                    if (this.YX[i].text === this.A) {
                        return this.YX[i].ZY;
                    }
                }
            },
            checkAllValue: function () {
                // 当选中的数量等于列表的行数且列表有数据时全选选中
                return (this.checkArr.length === this.stRdBilltems.length) && this.stRdBilltems.length !== 0
            }
        },
        watch: {
            checkArr: function () {
                idArr = [];
                var item = document.getElementsByName('checkItem');
                for (var i = 0; i < item.length; i++) {
                    if (item[i].checked) {
                        idArr.push(item[i].value);
                    }
                }
                this.getTotal();
            }
        },
        filters: {
            noName: function (value) {
                return value ? value : ' - '
            },
        },
        methods: {
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
            fetchRecord: function () {
                var self = this;
                var data = _.clone(self.form);
                self.$http.post('/api/reconciliation/findByReconciliation', data, {emulateJSON: true}).then(function (res) {
                    if (res.data.code == 1) {
                        self.stRdBilltems = res.data.data;
                        self.supplierId = res.data.data.id;
                        self.MergeCell(self.stRdBilltems);
                    }
                    self.submitting = false;
                }).catch(function () {

                }).finally(function () {
                    self.submitting = false;

                });
            },

            MergeCell: function (data) {

            },

            export: function () { //导出
                if (this.checkArr.length === 0) {
                    this.$toastr.error('请选择要导出的数据');
                    return
                }
                window.location.href = '/api/reconciliation/export?ids=' + this.checkArr;
            },
            exportAndMark: function () { //导出并标记对账
                var self = this;
                if (this.checkArr.length === 0) {
                    this.$toastr.error('请选择要导出并对账的数据');
                    return
                }
                var param = {
                    'ids': idArr
                }
                swal({
                    title: '确认导出并对账？',
                    type: 'info',
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    showCancelButton: true,
                    showConfirmButton: true,
                    showLoaderOnConfirm: true,
                    confirmButtonColor: '#ed5565',
                    closeOnConfirm: false
                }, function () {

                    self.$http.post('/api/reconciliation/update', param, {emulateJSON: true}).then(function (res) {
                        if (res.data.code === '1') {
                            window.location.href = '/api/reconciliation/export?ids=' + this.checkArr;
                            self.$toastr.success('操作成功');
                            tt.checkArr = [];
                            this.fetchRecord();

                        }
                    }).catch(function () {

                    }).finally(function () {
                        swal.close();
                    });

                })
            },
            mark: function () { //标记对账
                var self = this;
                if (this.checkArr.length === 0) {
                    this.$toastr.error('请选择要对账的数据');
                    return
                }
                var param = {
                    'ids': idArr
                }
                swal({
                    title: '确认对账？',
                    type: 'info',
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    showCancelButton: true,
                    showConfirmButton: true,
                    showLoaderOnConfirm: true,
                    confirmButtonColor: '#ed5565',
                    closeOnConfirm: false
                }, function () {
                    self.$http.post('/api/reconciliation/update', param, {emulateJSON: true}).then(function (res) {
                        if (res.data.code === '1') {
                            self.$toastr.success('操作成功');
                            tt.checkArr = [];
                            this.fetchRecord();

                        }
                    }).catch(function () {

                    }).finally(function () {
                        swal.close();
                    });
                })

            },
            // 全选事件
            checkAll: function () {
                if (!this.checkAllValue) {
                    this.checkArr = []
                    var self = this
                    $('.checkItem').each(function (value) {
                        self.checkArr.push(this.value)
                    })
                } else {
                    this.checkArr = []
                }
            },
            // 本次计算数量Change事件
            countChange: function (item) {
                if (this.checkArr.indexOf(item.id) >= 0) {
                    this.getTotal()
                }
            },
            // 计算商品总额
            getTotal: function () {
                var total = 0;
                var total2 = 0;
                for (var i = 0, iLength = this.checkArr.length; i < iLength; i++) {
                    for (var j = 0, jLength = this.stRdBilltems.length; j < jLength; j++) {
                        if (this.checkArr[i] == this.stRdBilltems[j].id) {
                            var sum = 0;
                            var sum2 = 0;
                            if (this.stRdBilltems[j].quantity) {
                                sum = new Decimal(this.stRdBilltems[j].totalMoney);
                                sum2 = new Decimal(this.stRdBilltems[j].quantity);
                                total = new Decimal(total).plus(sum);
                                total2 = new Decimal(total2).plus(sum2);
                            }
                            break;
                        }
                    }
                }
                this.totalAmount = total;
                this.pdcount = total2;
            }
        },

        created: function () {
        },
        ready: function () {
            this.fetchRecord();
            this.activeDate();
        }
    });


})(this.window);