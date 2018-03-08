
+(function () {


new Vue({
    el: '#container',
    mixins: [DaMeiVueMixins.DataTableMixin],
    validators: {
        suppliers: function (val) {
            return val > 0;
        }
    },
    components: {
        'web-uploader': DaMeiVueComponents.WebUploaderComponent,
    },
    data: {
        breadcrumbs: [{
            path: '/',
            name: '主页'
            },
            {
                path: '/',
                name: '查看复尺申请',
                active: true
            }
        ],
        contractId: null,
        customerContract:'',
        loginUserAcctype:null,
        imags:[],
        img:[]
    },
    created: function () {
        this.getLoginUserAcctype();
        this.contractId = this.$parseQueryString()['contractId'];
        this.findCustomerContract();
    },
    ready: function () {
        //this.drawTable();
    },
    methods: {
        getLoginUserAcctype:function () {
            var self = this;
            this.$http.get("/api/user/getLoginUserAccType").then(function (res) {
                if(res.data.code == 1){
                    self.loginUserAcctype = res.data.data;
                }
                this.drawTable();
            })
        },
        drawTable: function () {
            var self = this;
            self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                url: '/api/reviewSize/getReviewSizeNorice?contractId='+self.contractId,
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
                        field: '',
                        title: '序号',
                        width:'5%',
                        align: 'center',
                        formatter: function (value, row, index) {
                            return index+1;
                        }
                    },
                    {
                        field: 'supplierName',
                        title: '供应商',
                        align: 'center',
                    },
                    {
                        field: 'brandName',
                        title: '品牌',
                        orderable: false,
                        align: 'center'
                    },
                    {
                        field: 'prodCataLogName',
                        title: '类目',
                        orderable: false,
                        align: 'center',
                    },
                    {
                        field: 'reviewSizeNoticeImage',
                        title: '图片',
                        orderable: false,
                        align: 'center',
                        formatter:function (data) {
                            var imgstr = '';
                            self.imags = data.split(',');
                            self.imags.forEach(function (item) {
                                fullPath=ctx+'/imgFile/'+item;

                                imgstr += '<img src="'+fullPath+'" data-handle="data-bigImg" data-imgpath="'+fullPath+'" alt="图片" width="30px" height="30px" class="img-rounded">'
                            });
                            return imgstr;
                        }
                    },
                    {
                        field: 'noticeTime',
                        title: '通知时间',
                        orderable: false,
                        align: 'center',
                    },
                    {
                        field: 'remark',
                        title: '备注',
                        orderable: false,
                        align: 'center'
                    },
                    {
                        field: 'uploadUrl',
                        title: '上传状态',
                        orderable: false,
                        align: 'center',
                        formatter: function (value) {
                            if(value==null ||value==''){
                                return '未上传';
                            }else {
                                return '已上传';
                            }
                        }
                    },{
                        field: 'reviewStatus',
                        title: '状态',
                        orderable: false,
                        align: 'center',
                        formatter: function (value) {
                            switch (value) {
                                case 'NORIVEEWSIZE':
                                    return "未复尺";
                                    break;
                                case 'YESRIVEEWSIZE':
                                    return "已复尺";
                                    break;
                                case 'REJECT':
                                    return "已驳回";
                                    break;
                            }
                        }
                    },{
                        field: 'id',
                        title: '操作',
                        orderable: false,
                        align: 'center',
                        formatter: function (value, row) {
                            var fragment = '';
                            if(row.reviewStatus == 'REJECT' ){
                                if (self.loginUserAcctype == 'MATERIAL_CLERK') {
                                    fragment += ('<button data-handle="notice-again" data-id="' + row.id + '"  data-contractcode="' + row.contractCode + '" type="button" class="btn btn-xs btn-primary">重新通知复尺</button>&nbsp;&nbsp;&nbsp;');
                                }
                                fragment += ('<button data-handle="query-reject-record" data-id="' + row.id + '" data-contractcode="' + row.contractCode + '" type="button" class="btn btn-xs btn-primary">查看驳回记录</button>&nbsp;&nbsp;&nbsp;');
                            }else if(row.reviewStatus == 'YESRIVEEWSIZE'){
                                fragment += ('<button data-handle="query-review-result" data-id="' + row.id + '" data-contractid="' + row.contractId + '" type="button" class="btn btn-xs btn-primary">查看复尺结果</button>&nbsp;&nbsp;&nbsp;');
                                if(row.uploadUrl!=null){
                                fragment += ('<button data-handle="notice-download" data-id="' + row.id + '"type="button" class="btn btn-xs btn-primary">下载复尺结果</button>&nbsp;&nbsp;&nbsp;');
                                }
                            }
                            return fragment;
                        }
                    }]
            });
            // 下载复尺
            self.$dataTable.on('click', '[data-handle="notice-download"]',
                function () {
                    var id = $(this).data("id");
                    window.location.href="/api/reviewSize/download/record/"+id;
                }
            );
            // 通知供应商重新复尺
            self.$dataTable.on('click', '[data-handle="notice-again"]',
                function (e) {
                    var id = $(this).data("id");
                    var contractCode = $(this).data("contractcode");
                    swal({
                        title: '确认重新通知复尺？',
                        text: '您确认重新通知供应商复尺',
                        type: 'info',
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        showCancelButton: true,
                        showConfirmButton: true,
                        showLoaderOnConfirm: true,
                        confirmButtonColor: '#ed5565',
                        closeOnConfirm: false
                    }, function () {
                        self.$http.post('/api/reviewSize/updateNoriceStatus?noticeId=' + id +"&contractCode="+contractCode+ "&reviewStatus=NORIVEEWSIZE").then(function (res) {
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
            // 查看驳回记录
            self.$dataTable.on('click', '[data-handle="query-reject-record"]',
                function (e) {
                    var id = $(this).data("id");
                    var contractCode = $(this).data("contractcode");
                    var sourceType = 2;
                    rejectRecordModel(id,sourceType);
                    e.stopPropagation();
                }
            );
            // 查看复尺结果
            self.$dataTable.on('click', '[data-handle="query-review-result"]',
                function (e) {
                    var contractid = $(this).data("contractid");
                    var id = $(this).data("id");
                    editModel(contractid, id);
                }
            );
            self.$dataTable.on('click','[data-handle="data-bigImg"]',
            function (e) {
                model($(this).data("imgpath"));
            })
        },
        findCustomerContract:function () {
            var self = this;
            this.$http.get("/api/customer/contract/getById?contractId="+this.$parseQueryString().contractId).then(function (res) {
                if(res.data.code == 1){
                    self.customerContract = res.data.data;
                }
            })
        }
    }
});
    function model(fullPath) {
        var _$modal = $('#model').clone();
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
            mixins: [DaMeiVueMixins.ModalMixin],
            $modal: $modal, // 模式窗体 jQuery 对象
            data: {
                fullPath:fullPath
            },
            methods: {
            },
            created: function () {
            },
            ready: function () {
            }
        });
        // 创建的Vue对象应该被返回
        return showBill2;
    }
    function rejectRecordModel(id,sourceType) {
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
            mixins: [DaMeiVueMixins.ModalMixin],
            $modal: $modal, // 模式窗体 jQuery 对象
            data: {
                rejectReasons: '',
                reject:'REVIEWSIZE',
                rejectType:''
            },
            methods: {
                getcontract: function () {
                    var self = this;
                    self.$http.get('/api/supplierRejectRecord/getRejectReason?id=' + id +'&sourceType='+sourceType).then(function (res) {
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
    function editModel(contractid, id) {
        var _$modal = $('#editModel').clone();
        var $modal = _$modal.modal({
            height: 500,
            width: 1000,
        });
        // 获取 node
        var el = $modal.get(0);
        // 创建 Vue 对象编译节点
        showBill = new Vue({
            el: el,
            // 模式窗体必须引用 ModalMixin
            mixins: [DaMeiVueMixins.ModalMixin],
            $modal: $modal, // 模式窗体 jQuery 对象
            data: {
                list: [],
                contractId: null,
                reviewSizeResultId: null,
                customerContract: '',
                reviewSizeResult: '',
                img: '',
                reviewSizeResults:''
            },
            methods: {
                bigImg: function (fullpath) {
                    imgModel(fullpath);
                },
                getcontract: function () {
                    var self = this;
                    self.$http.get('/api/reviewSizeResult/getContract?contractId=' + contractid + '&id=' + id).then(function (res) {
                        if (res.data.code == 1) {
                            self.reviewSizeResult = res.data.data;

                            var imagesPath = self.reviewSizeResult.reviewSizeNoticeImage.split(",");
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
                findReviewSizeResult: function () {
                    var self = this;
                    self.$http.get('/api/reviewSizeResult/getReviewSizeResultById?id=' + id).then(function (res) {
                        if (res.data.code == 1) {
                            self.reviewSizeResults = res.data.data;
                        }
                    })
                },
            },

            created: function () {
                this.contractId = contractid;
                this.reviewSizeResultId = id;
                this.getcontract();
                this.findReviewSizeResult();
            },
            ready: function () {
            }
        });
        // 创建的Vue对象应该被返回
        return showBill;
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
            mixins: [DaMeiVueMixins.ModalMixin],
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