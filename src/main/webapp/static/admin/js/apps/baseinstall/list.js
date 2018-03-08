+(function () {
    $('#baseInstall').addClass('active');
    $('#customerContract').addClass('active');

    Vue.filter('whether-convert', function (value) {
        if (value == 1 || value == '1') {
            return '是';
        } else {
            return '否';
        }
    });

    var vueRole = new Vue({
        el: '#container',
        mixins: [DaMeiVueMixins.DataTableMixin],
        data: {
            breadcrumbs: [{
                path: '/admin',
                name: '主页'
            }, {
                path: '/',
                name: '基装数据',
                active: true
            }],
            form: {
                budgetNo: '',
                customerName: '',
                customerPhone: '',
                contractStatus: ''
            },
            $dataTable: null,
            selectedRows: {},
            modalModel: null,
            _$el: null,
            _$dataTable: null
        },
        created: function () {

        },
        ready: function () {
            this.drawTable();
        },
        methods: {
            query: function () {
                this.$dataTable.bootstrapTable('refresh');
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/api/customerContract/list',
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
                    columns: [{
                        field: "id",
                        visible: false
                    }, {
                        field: 'budgetNo',
                        title: '预算号',
                        orderable: false
                    }, {
                        field: 'projectCode',
                        title: '项目编号',
                        orderable: false
                    }, {
                        field: 'customerName',
                        title: '客户姓名',
                        orderable: false
                    }, {
                        field: 'customerPhone',
                        title: '客户电话',
                        orderable: false
                    }, {
                        field: 'projectAddress',
                        title: '工程地址',
                        orderable: false
                    }, {
                        field: 'meal',
                        title: '套餐',
                        orderable: false
                    }, {
                        field: 'designerName',
                        title: '设计师姓名',
                        orderable: false
                    }, {
                        field: 'designerPhone',
                        title: '设计师电话',
                        orderable: false
                    }, {
                        field: 'budgetArea',
                        title: '建筑面积',
                        orderable: false
                    }, {
                        field: 'completeDate',
                        title: '竣工时间',
                        orderable: false
                    }, {
                        field: 'contractStatus',
                        title: '合同状态',
                        orderable: false,
                        formatter: function (value) {
                            switch (value) {
                                case "NOTCHECKED":
                                    return "未核对";
                                    break;
                                case "CHECKED":
                                    return "已核对";
                                    break;
                                case "COMPLETED":
                                    return "竣工";
                                    break;
                            }
                        }
                    }, {
                        field: 'id',
                        title: '操作',
                        align: 'center',
                        orderable: false,
                        formatter: function (value, row, index) {

                            var html = '';
                            var operateName = "编辑";
                            // if (row.contractStatus === 'COMPLETED') {
                            //     operateName = "审核";
                            // }
                            if (row.contractStatus != 'COMPLETED') {
                                html += ' <button style="text-align: center" '
                                html += 'data-handle="edit-click" '
                                html += 'data-id="' + row.id + '" '
                                html += 'data-keyboarder="' + row.keyboarder + '" '
                                html += 'class="m-r-xs btn btn-xs btn-primary" type="button">' + operateName + '</button>'
                            }
                            if (row.contractStatus !== 'COMPLETED') {
                                html += '<button style="margin-left:30px;" data-handle="complete-click" data-id="' + row.id + '" data-contract-status="' + "COMPLETED" + '"class="m-r-xs btn btn-xs btn-primary" type="button">竣工</button>'
                            }
                            if (DaMeiUtils.hasPermission('data_repair')) {
                                html += '<button style="margin-left:30px;" data-handle="repair-click" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-primary" type="button">数据修复</button>'
                            }
                            return html;
                        }
                    }]
                });
                //编辑/审核
                self.$dataTable.on('click', '[data-handle="edit-click"]',
                    function (e) {
                        var id = $(this).data("id");
                        var keyboarder = $(this).data("keyboarder");
                        if (keyboarder != 'undefined' && keyboarder != DaMeiUser.name && DaMeiUser.name != '闫双') {
                            swal({
                                title: '您没有该操作的权限！',
                                text: '此单录入人为【' + keyboarder + '】，您不能执行该操作！',
                                type: 'info',
                                // confirmButtonText: '确定',
                                cancelButtonText: '确定',
                                // showCancelButton: true,
                                // showConfirmButton: true,
                                // showLoaderOnConfirm: true,
                                confirmButtonColor: '#ed5565',
                                closeOnConfirm: false
                            }, function () {
                                swal.close();
                            });
                        } else {
                            self.$http.get('/api/customerContract/getContractDetail?id=' + id).then(function (res) {
                                //var model = $(this).data();
                                createOrEditModal(res.data.data, true);
                                e.stopPropagation();
                            }).catch(function () {

                            }).finally(function () {
                                swal.close();
                            });
                        }
                    });
                self.$dataTable.on('click', '[data-handle="complete-click"]',
                    function (e) {
                        var id = $(this).data('id');
                        self.$http.get('/api/customerContract/getById?id=' + id).then(function (res) {
                            var model = res.data.data;
                            completeModal(model);
                        }).catch(function () {
                        }).finally(function () {
                        });
                        e.stopPropagation();
                    }
                );
                self.$dataTable.on('click', '[data-handle="repair-click"]', function () {
                    var model = $(this).data();
                    window.location.href = '/admin/basedata?id=' + model.id;
                });
            },
            createBtnClickHandler: function (e) {
                createOrEditModal({}, false);
            },
            //导入
            importBtnClickHandler: function () {
                importModal();
            }
        }
    });
    //竣工
    function completeModal(model) {
        var _$modal = $('#completeModal').clone();
        var $el = _$modal.modal({
            height: 150,
            width: 500
        });
        $el.on('shown.bs.modal',
            function () {
                var el = $el.get(0);
                var bill = new Vue({
                    el: el,
                    // 模式窗体必须引用 ModalMixin
                    mixins: [DaMeiVueMixins.ModalMixin],
                    $modal: $el, // 模式窗体 jQuery 对象
                    data: {
                        customerContract: model,
                        completeDate: ''
                    },
                    methods: {
                        activeDatePicker: function () {
                            $(this.$els.completeDate).datetimepicker({
                                format: 'yyyy-mm-dd'
                            });
                        },
                        save: function () {
                            var self = this;
                            var customerContract = self.customerContract;
                            customerContract.contractStatus = 'COMPLETED';
                            customerContract.completeDate = self.completeDate;
                            self.$validate(true,
                                function () {
                                    self.submitting = true;
                                    if (self.$validation.valid) {
                                        self.$http.post('/api/customerContract/update', customerContract, {emulateJSON: true}).then(function (res) {
                                            Vue.toastr.success(res.data.message);
                                            $el.modal('hide');
                                            vueRole.$dataTable.bootstrapTable('refresh');
                                        }).catch(function () {
                                        }).finally(function () {
                                        });
                                    }
                                });
                        }
                    },
                    created: function () {
                    },
                    ready: function () {
                        this.activeDatePicker();
                    }
                });
                return bill;
            });
    }

    // 新增或修改窗口
    function createOrEditModal(model, isEdit) {
        var _modal = $('#modal').clone();
        var $el = _modal.modal({
            height: 600,
            width: 1200
        });
        $el.on('shown.bs.modal', function () {
            var el = $el.get(0);
            isEdit = !!isEdit;
            var vueModal = new Vue({
                el: el,
                mixins: [DaMeiVueMixins.ModalMixin],
                $modal: $el,
                created: function () {
                },
                ready: function () {
                    this.activeDatepiker();
                },
                data: {
                    contractStatus: model.contractStatus,
                    contractBaseInfo: {
                        id: model.contractBaseInfo.id,
                        budgetNo: model.contractBaseInfo.budgetNo === 'undefined' ? '' : model.contractBaseInfo.budgetNo,
                        projectCode: model.contractBaseInfo.projectCode === 'undefined' ? '' : model.contractBaseInfo.projectCode,
                        customerName: model.contractBaseInfo.customerName === 'undefined' ? '' : model.contractBaseInfo.customerName,
                        customerPhone: model.contractBaseInfo.customerPhone === 'undefined' ? '' : model.contractBaseInfo.customerPhone,
                        projectAddress: model.contractBaseInfo.projectAddress === 'undefined' ? '' : model.contractBaseInfo.projectAddress,
                        meal: model.contractBaseInfo.meal === 'undefined' ? '' : model.contractBaseInfo.meal,
                        budgetArea: model.contractBaseInfo.budgetArea === 'undefined' ? '' : model.contractBaseInfo.budgetArea,
                        houseLayout: model.contractBaseInfo.houseLayout === 'undefined' ? '' : model.contractBaseInfo.houseLayout,
                        designerName: model.contractBaseInfo.designerName === 'undefined' ? '' : model.contractBaseInfo.designerName,
                        designerPhone: model.contractBaseInfo.designerPhone === 'undefined' ? '' : model.contractBaseInfo.designerPhone,
                        remarks: model.contractBaseInfo.remarks === 'undefined' ? '' : model.contractBaseInfo.remarks,
                        engineeringCost: model.contractBaseInfo.engineeringCost === 'undefined' ? '' : model.contractBaseInfo.engineeringCost,
                        dismantleFee: model.contractBaseInfo.dismantleFee === 'undefined' ? '' : model.contractBaseInfo.dismantleFee,
                        //changeFee: model.contractBaseInfo.changeFee  === 'undefined' ? '' : model.contractBaseInfo.changeFee,
                        haveElevator: model.contractBaseInfo.haveElevator === 'undefined' ? '' : model.contractBaseInfo.haveElevator,
                        contractSignDate: model.contractBaseInfo.contractSignDate === 'undefined' ? '' : model.contractBaseInfo.contractSignDate
                    },
                    contractInputInfo: {
                        id: model.contractInputInfo == null || model.contractInputInfo.id === 'undefined' ? '' : model.contractInputInfo.id,
                        originalId: model.contractInputInfo == null || model.contractInputInfo.originalId === 'undefined' ? model.contractBaseInfo.id : model.contractInputInfo.originalId,
                        dataType: model.contractInputInfo == null || model.contractInputInfo.dataType === 'undefined' ? 'INPUT' : model.contractInputInfo.dataType,
                        budgetNo: model.contractInputInfo == null || model.contractInputInfo.budgetNo === 'undefined' ? '' : model.contractInputInfo.budgetNo,
                        projectCode: model.contractInputInfo == null || model.contractInputInfo.projectCode === 'undefined' ? '' : model.contractInputInfo.projectCode,
                        customerName: model.contractInputInfo == null || model.contractInputInfo.customerName === 'undefined' ? '' : model.contractInputInfo.customerName,
                        customerPhone: model.contractInputInfo == null || model.contractInputInfo.customerPhone === 'undefined' ? '' : model.contractInputInfo.customerPhone,
                        projectAddress: model.contractInputInfo == null || model.contractInputInfo.projectAddress === 'undefined' ? '' : model.contractInputInfo.projectAddress,
                        meal: model.contractInputInfo == null || model.contractInputInfo.meal === 'undefined' ? '' : model.contractInputInfo.meal,
                        budgetArea: model.contractInputInfo == null || model.contractInputInfo.budgetArea === 'undefined' ? '' : model.contractInputInfo.budgetArea,
                        houseLayout: model.contractInputInfo == null || model.contractInputInfo.houseLayout === 'undefined' ? '' : model.contractInputInfo.houseLayout,
                        designerName: model.contractInputInfo == null || model.contractInputInfo.designerName === 'undefined' ? '' : model.contractInputInfo.designerName,
                        designerPhone: model.contractInputInfo == null || model.contractInputInfo.designerPhone === 'undefined' ? '' : model.contractInputInfo.designerPhone,
                        remarks: model.contractInputInfo == null || model.contractInputInfo.remarks === 'undefined' ? '' : model.contractInputInfo.remarks,
                        engineeringCost: model.contractInputInfo == null || model.contractInputInfo.engineeringCost === 'undefined' ? '' : model.contractInputInfo.engineeringCost,
                        dismantleFee: model.contractInputInfo == null || model.contractInputInfo.dismantleFee === 'undefined' ? '' : model.contractInputInfo.dismantleFee,
                        //changeFee: model.contractInputInfo == null ||  model.contractInputInfo.changeFee === 'undefined' ? '' : model.contractInputInfo.changeFee,
                        haveElevator: model.contractInputInfo == null || model.contractInputInfo.haveElevator === 'undefined' ? 2 : model.contractInputInfo.haveElevator,
                        contractSignDate: model.contractInputInfo == null || model.contractInputInfo.contractSignDate === 'undefined' ? '' : model.contractInputInfo.contractSignDate
                    },
                    contractCheckInfo: {
                        id: model.contractCheckInfo == null || model.contractCheckInfo.id === 'undefined' ? '' : model.contractCheckInfo.id,
                        originalId: model.contractCheckInfo == null || model.contractCheckInfo.originalId === 'undefined' ? model.contractBaseInfo.id : model.contractCheckInfo.originalId,
                        dataType: model.contractCheckInfo == null || model.contractCheckInfo.dataType === 'undefined' ? 'CHECK' : model.contractCheckInfo.dataType,
                        budgetNo: model.contractCheckInfo == null || model.contractCheckInfo.budgetNo === 'undefined' ? '' : model.contractCheckInfo.budgetNo,
                        projectCode: model.contractCheckInfo == null || model.contractCheckInfo.projectCode === 'undefined' ? '' : model.contractCheckInfo.projectCode,
                        customerName: model.contractCheckInfo == null || model.contractCheckInfo.customerName === 'undefined' ? '' : model.contractCheckInfo.customerName,
                        customerPhone: model.contractCheckInfo == null || model.contractCheckInfo.customerPhone === 'undefined' ? '' : model.contractCheckInfo.customerPhone,
                        projectAddress: model.contractCheckInfo == null || model.contractCheckInfo.projectAddress === 'undefined' ? '' : model.contractCheckInfo.projectAddress,
                        meal: model.contractCheckInfo == null || model.contractCheckInfo.meal === 'undefined' ? '' : model.contractCheckInfo.meal,
                        budgetArea: model.contractCheckInfo == null || model.contractCheckInfo.budgetArea === 'undefined' ? '' : model.contractCheckInfo.budgetArea,
                        houseLayout: model.contractCheckInfo == null || model.contractCheckInfo.houseLayout === 'undefined' ? '' : model.contractCheckInfo.houseLayout,
                        designerName: model.contractCheckInfo == null || model.contractCheckInfo.designerName === 'undefined' ? '' : model.contractCheckInfo.designerName,
                        designerPhone: model.contractCheckInfo == null || model.contractCheckInfo.designerPhone === 'undefined' ? '' : model.contractCheckInfo.designerPhone,
                        remarks: model.contractCheckInfo == null || model.contractCheckInfo.remarks === 'undefined' ? '' : model.contractCheckInfo.remarks,
                        engineeringCost: model.contractCheckInfo == null || model.contractCheckInfo.engineeringCost === 'undefined' ? '' : model.contractCheckInfo.engineeringCost,
                        dismantleFee: model.contractCheckInfo == null || model.contractCheckInfo.dismantleFee === 'undefined' ? '' : model.contractCheckInfo.dismantleFee,
                        //changeFee: model.contractCheckInfo == null ||  model.contractCheckInfo.changeFee === 'undefined' ? '' : model.contractCheckInfo.changeFee,
                        haveElevator: model.contractCheckInfo == null || model.contractCheckInfo.haveElevator === 'undefined' ? 2 : model.contractCheckInfo.haveElevator,
                        contractSignDate: model.contractCheckInfo == null || model.contractCheckInfo.contractSignDate === 'undefined' ? '' : model.contractCheckInfo.contractSignDate
                    }
                },
                validators: {
                    numeric: function (val) {
                        return /^(-?\d+)(\.\d+)?$/.test(val) || (val === '');//验证浮点数
                    },
                    mobile: function (val) {
                        return /^1(3|4|5|7|8)\d{9}$/.test(val) || (val === '');//手机号必须为11位数字
                    },
                    positiveNum: function (val) {
                        return /^\d+(\.\d+)?$/.test(val) || (val === '');//这个验证是只可以输入数字
                    }
                },
                methods: {
                    activeDatepiker: function () {
                        var self = this;
                        $(self.$els.contractSignDate).datetimepicker({initialDate: '2016-01-01'});
                    },
                    save: function () {
                        var self = this;
                        self.$validate(true,
                            function () {
                                if (self.$validation.valid) {
                                    self.submitting = true;
                                    var data;
                                    if (self.contractStatus !== 'COMPLETED') {
                                        data = self.contractInputInfo;
                                    } else {
                                        data = self.contractCheckInfo;
                                    }
                                    self.$http.post('/api/customerContract/saveConfirmData', data).then(function (res) {
                                            Vue.toastr.success("操作成功！！！！");
                                            $el.modal('hide');
                                            window.location.href = '/admin/basedata?id=' + self.contractBaseInfo.id;
                                            self.$destroy();
                                        },
                                        function (error) {
                                            Vue.toastr.error(error.responseText);
                                        }).catch(function () {
                                    }).finally(function () {
                                        self.submitting = false;
                                    });
                                }
                            })
                    }
                }
            });

            // 创建的Vue对象应该被返回
            return vueModal;
        });
    }

    //导入
    function importModal(model, isEdit) {
        var _modal = $('#import').clone();
        var $el = _modal.modal({
            height: 250,
            maxHeight: 500
        });
        $el.on('shown.bs.modal',
            function () {
                var el = $el.get(0);
                isEdit = !!isEdit;
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
                                type: 'BRAND'
                            },
                            server: ctx + '/api/customerContract/import',
                        }
                    },
                    methods: {
                        // 下载文件
                        downloadFile: function (url) {
                            // 创建一个 iframe 设置 url 插入 dom
                            var iframe = document.createElement('iframe');
                            iframe.style.display = 'none';
                            iframe.frameborder = 0;
                            document.body.appendChild(iframe);
                            iframe.src = '/api/customerContract/downloadTemplate';
                        }
                    },
                    events: {
                        // 上传成功
                        'webupload-upload-success-bannerfile': function (file, res) {
                            if (res.code == 1) {
                                this.$toastr.success('合同导入成功');
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