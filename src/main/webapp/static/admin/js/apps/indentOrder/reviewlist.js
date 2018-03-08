var tt = null, showBill = null;
+(function () {
    $('#order').addClass('active');
    $('#review').addClass('active');

    var Detail = null;
    tt = new Vue({
        el: '#container',
        mixins: [DaMeiVueMixins.DataTableMixin],
        data: {
            breadcrumbs: [{
                path: '/admin',
                name: '主页'
            }, {
                path: '/',
                name: '复尺查询',
                active: true
            }],
            form: {
                keyword: '',
                status: '',
                endDate: '',
                startDate: '',
                contractCode: '',
                brandId: ''
            },
            brands: {},
            loginUserAcctype: null,
            $dataTable: null,
            modalModel: null,
            _$el: null,
            _$dataTable: null,
            id: null,


        },
        created: function () {
            this.getLoginUserAcctype();
        },
        ready: function () {
            this.form.contractCode = this.$parseQueryString()['contractCode'];
            this.activeDatepiker();
            this.getBrands();
        },
        methods: {
            getLoginUserAcctype: function () {
                var self = this;
                this.$http.get("/api/user/getLoginUserAccType").then(function (res) {
                    if (res.data.code == 1) {
                        self.loginUserAcctype = res.data.data;
                    }
                    this.drawTable();
                })
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
            activeDatepiker: function () {
                var self = this;
                $(self.$els.endDate).datetimepicker('setStartDate', '');
                $(self.$els.startDate).datetimepicker('setStartDate', '');
            },
            query: function () {
                this.$dataTable.bootstrapTable('selectPage', 1);
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/api/reviewSize/all',
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
                            field: 'contractCode',
                            title: '项目单号',
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'customerName',
                            title: '客户姓名',
                            orderable: false,
                            align: 'center'
                        }, {
                            field: 'brandName',
                            title: '品牌',
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'createName',
                            title: '创建人',
                            orderable: false,
                            align: 'center'
                        }, {
                            field: 'noticeTime',
                            title: '通知复尺时间',
                            orderable: false,
                            align: 'center'
                        },
                        {
                            field: 'reviewStatus',
                            title: '复尺状态',
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
                        },
                        {
                            field: 'uploadUrl',
                            title: '上传复尺',
                            orderable: false,
                            align: 'center',
                            formatter: function (value) {
                                if (value == null || value == '') {
                                    return "未上传";
                                } else {
                                    return "已上传";
                                }
                            }
                        }, {
                            field: 'id',
                            title: '操作',
                            orderable: false,
                            align: 'center',
                            formatter: function (value, row) {
                                var fragment = '';
                                if (self.loginUserAcctype == 'PROD_SUPPLIER') {

                                    if (row.reviewStatus != 'NORIVEEWSIZE') {
                                        fragment += ('<button   data-handle="edit" data-id="' + row.id + '" data-code="' + row.contractId + '" type="button" class="btn btn-xs btn-primary">查看复尺记录</button>&nbsp;&nbsp;&nbsp;');
                                    }
                                    if (row.reviewStatus != 'REJECT') {
                                        fragment += ('<button   data-handle="reviewSize" data-id="' + row.id + '" data-code="' + row.contractId + '" type="button" class="btn btn-xs btn-primary">填报复尺记录</button>&nbsp;&nbsp;&nbsp;');
                                        //上传
                                        if (row.uploadUrl == null) {
                                            fragment += ('<button   data-handle="upload" data-id="' + row.id + '" data-code="' + row.contractId + '" type="button" class="btn btn-xs btn-warning">上传复尺记录</button>&nbsp;&nbsp;&nbsp;');
                                        }
                                        /*//下载
                                         if(row.uploadUrl!=null) {
                                         fragment += ('<button   data-handle="download" data-id="' + row.id + '" data-code="' + row.contractId + '" type="button" class="btn btn-xs btn-primary">下载复尺记录</button>&nbsp;&nbsp;&nbsp;');
                                         }*/
                                    }
                                    if (row.reviewStatus == 'NORIVEEWSIZE') {
                                        fragment += ('<button   data-handle="reject" data-id="' + row.id + '"  data-contractCode="' + row.contractCode + '"  type="button" class="btn btn-xs btn-danger">驳回</button>&nbsp;&nbsp;&nbsp;');
                                    }
                                }

                                if (row.reviewStatus == 'REJECT' && self.loginUserAcctype != 'ADMIN') {
                                    fragment += ('<button   data-handle="rejectReason" data-id="' + row.id + '" data-contractCode="' + row.contractCode + '"  type="button" class="btn btn-xs btn-primary">驳回记录</button>&nbsp;&nbsp;&nbsp;');
                                }
                                return fragment;
                            }
                        }
                    ]
                });

                // 上传
                self.$dataTable.on('click', '[data-handle="upload"]',
                    function (e) {
                        var id = $(this).data("id");
                        importModal(id);
                    }
                );
                // 下载
                self.$dataTable.on('click', '[data-handle="download"]',
                    function (e) {
                        var id = $(this).data("id");
                        window.location.href = "/api/reviewSize/download/record/" + id;
                    }
                );
                // 驳回原因
                self.$dataTable.on('click', '[data-handle="rejectReason"]',
                    function (e) {
                        var id = $(this).data("id");
                        var contractCode = $(this).data("contractCode");
                        var sourceType = 2;
                        rejectRecordModel(sourceType, id);
                    }
                );
                // 驳回
                self.$dataTable.on('click', '[data-handle="reject"]',
                    function (e) {
                        var model = $(this).data();
                        showRejectModel(model);
                    }
                );
                //填报复尺记录
                self.$dataTable.on('click', '[data-handle="reviewSize"]',
                    function (e) {
                        var code = $(this).data("code");
                        var id = $(this).data("id");
                        saveModel(code, id);
                        e.stopPropagation()
                    }
                );
                //编辑复尺记录
                self.$dataTable.on('click', '[data-handle="edit"]',
                    function (e) {
                        var code = $(this).data("code");
                        var id = $(this).data("id");
                        viewReviewResult(code, id);
                        e.stopPropagation()
                    }
                );
            }

        }
    });

    function saveModel(code, id) {
        var _$modal = $('#saveModel').clone();
        var $modal = _$modal.modal({
            height: 610,
            width: 1000,
            backdrop: 'static',
            keyboard: false
        });
        // 获取 node
        var el = $modal.get(0);
        // 创建 Vue 对象编译节点
        var newReviewSizeResult = new Vue({
            el: el,
            // 模式窗体必须引用 ModalMixin
            mixins: [DaMeiVueMixins.ModalMixin],
            $modal: $modal, // 模式窗体 jQuery 对象
            data: {
                list: [],
                contractId: code,
                reviewSizeResultId: null,
                form: {
                    productName: '',
                    location: '',
                    model: '',
                    specification: '',
                    unit: '',
                    count: '',
                    remark: '',
                    prodCatalogId: '',
                    contractId: code,
                    reviewSizeNoticeId: id
                },
                customerContract: '',
                prodCatalogs: '',
                reviewSizeResult: '',
                img: '',
                rejects: null
            },
            validators: {
                numeric: function (val) {
                    return /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/.test(val) || (val === '');//验证浮点数
                }
            },
            methods: {
                bigImg: function (fullpath) {
                    imgModel(fullpath);
                },

                getcontract: function () {
                    var self = this;
                    self.$http.get('/api/reviewSizeResult/getContract?contractId=' + code + '&id=' + id).then(function (res) {
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
                getProdCatalogs: function () {
                    var self = this;
                    self.$http.get('/api/catalog/findCatalogList').then(function (res) {
                        if (res.data.code == 1) {
                            self.prodCatalogs = res.data.data;
                        }
                    }).catch(function () {

                    }).finally(function () {

                    });
                },
                findCustomerContract: function () {
                    var self = this;
                    this.$http.get("/api/customer/contract/getById?contractId=" + this.contractId).then(function (res) {
                        if (res.data.code == 1) {
                            self.customerContract = res.data.data;
                        }
                    })
                },
                addData: function () {
                    var uuid = 'damei' + new Date().getTime();
                    var self = this;
                    self.list.push({
                        id: uuid,
                        productName: '',
                        productNameField: 'productNameField' + uuid,
                        // productNameValidate: {
                        //     required: {rule: false,message: '请输入商品名称'},
                        //     maxlength:{rule:20,message:'不能超过20个字符'}
                        // },
                        location: '',
                        locationField: 'locationField' + uuid,
                        // locationValidate: {
                        //     required: {rule: false,message: '请输入安装位置'},
                        //     maxlength:{rule:20,message:'不能超过20个字符'}
                        // },
                        model: '',
                        modelField: 'modelField' + uuid,
                        // modelValidate: {
                        //     required: {rule: false,message: '请输入型号'},
                        //     maxlength:{rule:20,message:'不能超过20个字符'}
                        // },
                        specification: '',
                        specificationField: 'specificationField' + uuid,
                        // specificationValidate: {
                        //     required: {rule: false,message: '请输入规格'},
                        //     maxlength:{rule:20,message:'不能超过20个字符'}
                        // },
                        unit: '',
                        unitField: 'unitField' + uuid,
                        // unitValidate: {
                        //     required: {rule: false,message: '请输入单位'},
                        //     maxlength:{rule:10,message:'不能超过10个字符'}
                        // },
                        count: '',
                        countField: 'countField' + uuid,
                        // countValidate: {
                        //     required: {rule: false,message: '请输入数量'},
                        //     numeric:{rule:true,message:'数量必须是正数'}
                        // },
                        remark: '',
                        remarkField: 'remarkField' + uuid,
                        // remarkValidate: {
                        //     maxlength:{rule:200,message:'备注不能超过200个字符'}
                        // },
                        // prodCatalogId: self.reviewSizeResult.prodCatalogId,
                        // prodCatalogIdField: 'prodCatalogIdField' + uuid,
                        // prodCatalogIdValidate: {
                        //     required: {rule: true,message: '请选择商品类目'}
                        // }
                    })
                },
                insert: function () {
                    var self = this;
                    var lists = [];
                    var review;
                    self.submitting = true;
                    self.list.forEach(function (item) {
                        review = {
                            productName: item.productName,
                            location: item.location,
                            model: item.model,
                            specification: item.specification,
                            unit: item.unit,
                            count: item.count,
                            remark: item.remark,
                            prodCatalogId: self.reviewSizeResult.prodCataLogId,
                            reviewSizeNoticeId: self.reviewSizeResultId,
                            contractId: self.contractId,
                        };
                        lists.push(review);
                    });
                    self.$http.post('/api/reviewSizeResult/insert', lists).then(function (res) {
                        if (res.data.code == 1) {
                            self.$toastr.success('操作成功');
                            window.location.href = "/admin/review"
                        }
                    })
                },
                removeData: function (index) {
                    this.list.splice(index, 1);
                },
            },
            created: function () {
                this.addData();
                this.contractId = code;
                this.reviewSizeResultId = id;
                this.findCustomerContract();
                this.getProdCatalogs();
                this.getcontract();
            },
            ready: function () {
            }
        });
        // 创建的Vue对象应该被返回
        return newReviewSizeResult;
    }

    function viewReviewResult(code, id) {
        var _$modal = $('#viewModel').clone();
        var $modal = _$modal.modal({
            height: 610,
            width: 1000,
            backdrop: 'static',
            keyboard: false
        });
        // 获取 node
        var el = $modal.get(0);
        // 创建 Vue 对象编译节点
        var viewReviewResultVue = new Vue({
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
                img: ''
            },
            validators: {
                numeric: function (val) {
                    return /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/.test(val) || (val === '');//验证浮点数
                }
            },
            created: function () {
                this.contractId = code;
                this.reviewSizeResultId = id;
                this.findCustomerContract();
                this.getProdCatalogs();
                this.getcontract();
            },
            ready: function () {
                this.findReviewSizeResult();
            },
            methods: {
                bigImg: function (fullpath) {
                    imgModel(fullpath);
                },
                getcontract: function () {
                    var self = this;
                    self.$http.get('/api/reviewSizeResult/getContract?contractId=' + code + '&id=' + id).then(function (res) {
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
                getProdCatalogs: function () {
                    var self = this;
                    self.$http.get('/api/catalog/findCatalogList').then(function (res) {
                        if (res.data.code == 1) {
                            self.prodCatalogs = res.data.data;
                        }
                    }).catch(function () {
                    }).finally(function () {
                    });
                },
                findCustomerContract: function () {
                    var self = this;
                    this.$http.get("/api/customer/contract/getById?contractId=" + this.contractId).then(function (res) {
                        if (res.data.code == 1) {
                            self.customerContract = res.data.data;
                        }
                    })
                },
                findReviewSizeResult: function () {
                    var self = this;
                    self.$http.get('/api/reviewSizeResult/getReviewSizeResultById?id=' + id).then(function (res) {
                        if (res.data.code == 1) {
                            res.data.data.forEach(function (reviewItem) {
                                var uuid = 'damei' + new Date().getTime();
                                self.list.push({
                                    id: reviewItem.id,
                                    productName: reviewItem.productName,
                                    productNameField: 'productNameField' + uuid,
                                    // productNameValidate: {
                                    //     required: {rule: true,message: '请输入商品名称'},
                                    //     maxlength:{rule:20,message:'不能超过20个字符'}
                                    // },
                                    location: reviewItem.location,
                                    locationField: 'locationField' + uuid,
                                    // locationValidate: {
                                    //     required: {rule: true,message: '请输入安装位置'},
                                    //     maxlength:{rule:20,message:'不能超过20个字符'}
                                    // },
                                    model: reviewItem.model,
                                    modelField: 'modelField' + uuid,
                                    // modelValidate: {
                                    //     required: {rule: true,message: '请输入型号'},
                                    //     maxlength:{rule:20,message:'不能超过20个字符'}
                                    // },
                                    specification: reviewItem.specification,
                                    specificationField: 'specificationField' + uuid,
                                    // specificationValidate: {
                                    //     required: {rule: true,message: '请输入规格'},
                                    //     maxlength:{rule:20,message:'不能超过20个字符'}
                                    // },
                                    unit: reviewItem.unit,
                                    unitField: 'unitField' + uuid,
                                    // unitValidate: {
                                    //     required: {rule: true,message: '请输入单位'},
                                    //     maxlength:{rule:10,message:'不能超过10个字符'}
                                    // },
                                    count: reviewItem.count,
                                    countField: 'countField' + uuid,
                                    // countValidate: {
                                    //     required: {rule: true,message: '请输入数量'},
                                    //     numeric:{rule:true,message:'数量必须是正数'}
                                    // },
                                    remark: reviewItem.remark,
                                    remarkField: 'remarkField' + uuid,
                                    // remarkValidate: {
                                    //     maxlength:{rule:200,message:'备注不能超过200个字符'}
                                    // },
                                    // prodCatalogId: reviewItem.prodCatalogId,
                                    // prodCatalogIdField: 'prodCatalogIdField' + uuid,
                                    // prodCatalogIdValidate: {
                                    //     required: {rule: true,message: '请选择商品类目'}
                                    // },
                                    contractId: code,
                                    reviewSizeNoticeId: id
                                })
                            });
                        }
                    })
                },
                save: function () {
                    var self = this;
                    var lists = [];
                    var review;
                    self.submitting = true;
                    self.list.forEach(function (item) {
                        review = {
                            id: item.id,
                            productName: item.productName,
                            location: item.location,
                            model: item.model,
                            specification: item.specification,
                            unit: item.unit,
                            count: item.count,
                            remark: item.remark,
                            prodCatalogId: self.reviewSizeResult.prodCataLogId,
                            reviewSizeNoticeId: self.reviewSizeResultId,
                        };
                        lists.push(review);
                    })
                    self.$http.post('/api/reviewSizeResult/update', lists).then(function (res) {
                        if (res.data.code == 1) {
                            self.$toastr.success('操作成功');
                            window.location.href = "/admin/review"
                        }
                    })

                },
                removeData: function (index) {
                    var self = this;
                    var id = self.list[index].id;
                    if (id) {
                        swal({
                            title: '删除记录',
                            text: '确定删除该记录么？',
                            type: 'info',
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            showCancelButton: true,
                            showConfirmButton: true,
                            showLoaderOnConfirm: true,
                            confirmButtonColor: '#ed5565',
                            closeOnConfirm: false
                        }, function () {
                            self.$http.get('/api/reviewSizeResult/delete?id=' + id).then(function (res) {
                                if (res.data.code == 1) {
                                    self.$toastr.success('操作成功');
                                    swal.close();
                                }
                            })
                            self.list.splice(index, 1);
                        })
                    } else {
                        self.list.splice(index, 1);
                    }
                }
            }
        });
        // 创建的Vue对象应该被返回
        return viewReviewResultVue;
    }

    function showRejectModel(model) {
        var _$modal = $('#rejectModel').clone();
        var $modal = _$modal.modal({
            height: 300,
            width: 500,
        });
        // 获取 node
        var el = $modal.get(0);
        // 创建 Vue 对象编译节点
        var rejectModel2 = new Vue({
            el: el,
            // 模式窗体必须引用 ModalMixin
            mixins: [DaMeiVueMixins.ModalMixin],
            $modal: $modal, // 模式窗体 jQuery 对象
            data: {
                id: model.id,
                contractCode: model.contractcode,
                rejectType: '',
                remark: '',
                reject: 'REVIEWSIZE',//复尺单驳回
                rejects: null
            },
            methods: {
                //选择驳回类型
                findOtherFeeNoteList: function () {
                    var self = this;
                    self.$http.get('/api/system/dictionary/findByValue?dicValue=review_size_reject_type').then(function (res) {
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
                                self.$http.post('/api/supplierRejectRecord/updateRejectType?id=' + self.id + '&rejectType=' + self.rejectType + '&remark=' + self.remark + '&reject=' + self.reject + '&contractCode=' + self.contractCode).then(function (res) {
                                    if (res.data.code == 1) {
                                        self.$toastr.success('操作成功');
                                        swal.close();
                                        $modal.modal('hide')
                                        tt.$dataTable.bootstrapTable('refresh');
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
        return rejectModel2;
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

    function rejectRecordModel(sourceType, id) {
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

    // 上传复尺
    function importModal(id) {
        var _modal = $('#modal').clone();
        var $el = _modal.modal({
            height: 250,
            maxHeight: 500
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
                    mixins: [DaMeiVueMixins.ModalMixin],
                    components: {
                        'web-uploader': DaMeiVueComponents.WebUploaderComponent
                    },
                    $modal: $el,
                    created: function () {
                    },
                    data: {
                        uploading: false,
                        progress: 0,
                        webUploader: {
                            type: 'bannerfile',
                            formData: {
                                type: 'SUPPLIER'
                            },
                            accept: {
                                title: '文件',
                                extendsions: 'xlsx,xls',
                            },
                            server: ctx + '/api/reviewSize/upload/record/' + id,
                            //上传图片路径
                            fileNumLimit: 8,
                            fileSizeLimit: 50000 * 1024,
                            fileSingleSizeLimit: 20000 * 1024
                        }
                    },
                    methods: {},
                    events: {
                        // 上传成功
                        'webupload-upload-success-bannerfile': function (file, res) {
                            if (res.code == 1) {
                                this.$toastr.success('复尺导入成功');
                                tt.$dataTable.bootstrapTable('refresh');
                            } else {
                                this.$toastr.error(res.message);
                            }
                        },
                        // 上传进度
                        'webupload-upload-progress-bannerfile': function (file, percentage) {
                            this.progress = percentage * 100;
                        },
                        // 上传结束
                        'webupload-upload-complete-bannerfile': function (file) {
                            this.uploading = false;
                        },
                        // 上传开始
                        'webupload-upload-start-bannerfile': function (file) {
                            this.uploading = true;
                        }
                    }

                });

                // 创建的Vue对象应该被返回
                return vueModal;
            });

    }

})();