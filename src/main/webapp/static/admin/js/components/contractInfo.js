/**
 * Created by 巢帅 on 2017/8/3.
 */
+(function() {
    var ContractInfo = Vue.extend({
        template:'#contractinfo',
        props:{
            contractId:{

            }
        },
        data: function(){
            return {
                customerContract: {
                    customer: [],
                    houseAddr: '',
                    designer: '',
                    designerMobile: '',
                    supervisor: '',
                    supervisorMobile: '',
                    projectManager: '',
                    pmMobile: ''
                },
            };
        },
        created:function () {
            this.findCustomerContract()
        },
        methods:{
            //获取合同信息
            findCustomerContract: function () {
                var self = this;
                self.$http.get("/api/customer/contract/getById?contractId=" + self.contractId).then(function (res) {
                    if (res.data.code == 1) {
                        self.customerContract = res.data.data;
                    }
                })
            },
        }
    });

    Vue.component('contract-info', ContractInfo);
})();