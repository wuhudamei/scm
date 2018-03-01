var tt = null, showBill = null;
+(function () {
    $('#dictManage').addClass('active');
    $('#dictionary').addClass('active');
    tt = new Vue({
        el: '#container',
        mixins: [RocoVueMixins.DataTableMixin],
        data: {
            breadcrumbs: [{
                path: '/admin',
                name: '主页'
            }, {
                path: '/',
                name: '基础数据维护',
                active: true
            }],
            form: {
                keyword: '',
            },
            dictionary: {
                id: null,
                status: 1
            },
            brands: {},
            $dataTable: null,
            modalModel: null,
            _$el: null,
            _$dataTable: null,
            id: null,


        },
        created: function () {
        },
        ready: function () {
            this.drawTable();
        },
        methods: {
            query: function () {
                this.$dataTable.bootstrapTable('selectPage', 1);
            },
            //新增
            createBtnClickHandler: function () {
                model2(null);
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/api/system/dictionary',
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
                            field: 'dicName',
                            title: '字典名称',
                            align: 'center'
                        },
                        {
                            field: 'dicValue',
                            title: '字典值',
                            align: 'center'
                        },
                        {
                            field: 'sort',
                            title: '排序',
                            align: 'center'
                        },
                        {
                            field: 'createName',
                            title: '创建人',
                            align: 'center',
                            visible: false
                        }, {
                            field: 'createTime',
                            title: '创建时间',
                            align: 'center',
                            visible: false
                        }, {
                            field: 'status',
                            title: '状态',
                            align: 'center',
                            formatter: function (value, row) {
                                var fragment = '';
                                if (value == '1') {
                                    fragment = '启用'
                                }
                                if (value == '0') {
                                    fragment = '禁用'
                                }
                                return fragment;
                            }
                        }, {
                            field: 'id',
                            title: '操作',
                            align: 'center',
                            formatter: function (value, row) {
                                var fragment = '';
                                if (hasDictionary) {
                                    if (RocoUtils.hasPermission('dict:edit')) {
                                        fragment += ('<button   data-handle="edit" data-id="' + row.id + '"  type="button" class="btn btn-xs btn-primary">编辑</button>&nbsp');
                                    }
                                    fragment += ('<button   data-handle="delete" data-id="' + row.id + '" type="button" class="btn btn-xs btn-danger">删除</button>&nbsp');
                                    if (row.status == '1') {
                                        fragment += ('<button   data-handle="stop" data-id="' + row.id + '"  data-status="0" type="button" class="btn btn-xs btn-warning">禁用</button>');
                                    }
                                    if (row.status == '0') {
                                        fragment += ('<button   data-handle="stop" data-id="' + row.id + '"   data-status="1" type="button" class="btn btn-xs btn-warning">启用</button>');
                                    }
                                }
                                return fragment;
                            }
                        }
                    ]
                });
                //编辑
                self.$dataTable.on('click', '[data-handle="edit"]',
                    function (e) {
                        var id = $(this).data("id");
                        model2(id)
                        e.stopPropagation()
                    }
                );
                //删除
                self.$dataTable.on('click', '[data-handle="delete"]',
                    function (e) {
                        var id = $(this).data("id");

                        swal({
                            title: "是否删除字典类型？",
                            type: "warning",
                            showCancelButton: true,
                            confirmButtonColor: "#DD6B55",
                            confirmButtonText: "确定",
                            cancelButtonText: "取消",
                            closeOnConfirm: false
                        }, function (isConfirm) {
                            if (isConfirm) {
                                self.$http.get('/api/system/dictionary/' + id + '/del').then(function (res) {
                                    if (res.data.code == 1) {
                                        self.$toastr.success('操作成功');
                                        self.$dataTable.bootstrapTable('refresh');
                                    }
                                }).catch(function () {
                                    swal("操作失败！", "", "error");
                                }).finally(function () {
                                    swal.close();
                                });
                            }
                        });
                        e.stopPropagation()
                    }
                );
                //停用--启用
                self.$dataTable.on('click', '[data-handle="stop"]',
                    function (e) {
                        var id = $(this).data("id");
                        var status = $(this).data("status");
                        self.dictionary.id = id;
                        self.dictionary.status = status;
                        var title = '';
                        if (status === 1) {
                            title = '是否启用该字典类型？'
                        }
                        if (status === 0) {
                            title = '是否禁用该字典类型？'
                        }
                        swal({
                            title: title,
                            type: "warning",
                            showCancelButton: true,
                            confirmButtonColor: "#DD6B55",
                            confirmButtonText: "确定",
                            cancelButtonText: "取消",
                            closeOnConfirm: false
                        }, function (isConfirm) {
                            if (isConfirm) {
                                self.$http.post('/api/system/dictionary/save', self.dictionary).then(function (res) {
                                    if (res.data.code == 1) {
                                        self.$toastr.success('操作成功');
                                        self.$dataTable.bootstrapTable('refresh');
                                    }
                                }).catch(function () {
                                    swal("操作失败！", "", "error");
                                }).finally(function () {
                                    swal.close();
                                });
                            }
                        });

                        e.stopPropagation()
                    }
                );
            },


        }
    });

    function model2(id) {
        var _$modal = $('#model2').clone();
        var $modal = _$modal.modal({
            height: 850,
            width: 1000,
            backdrop: 'static',
            keyboard: false
        });
        // 获取 node
        var el = $modal.get(0);
        // 创建 Vue 对象编译节点
        showBill = new Vue({
            el: el,
            // 模式窗体必须引用 ModalMixin
            mixins: [RocoVueMixins.ModalMixin],
            $modal: $modal, // 模式窗体 jQuery 对象
            data: {
                list: [],
                dictionary: {
                    id: id,
                    status: 1,
                    list: null
                }
            },
            //正整数校验
            validators: {
                numeric: function (val) {
                    var g = /^[1-9]*[1-9][0-9]*$/;
                    return g.test(val);
                    ;//验证正整数
                }
            },
            methods: {
                findDictionary: function () {
                    if (id == undefined || id == 'undefined') {
                    } else {
                        var self = this;
                        self.$http.get('/api/system/dictionary/' + id + '').then(function (res) {
                            if (res.data.code == 1) {
                                self.dictionary = res.data.data;
                                self.list = res.data.data.list;
                            }
                        }).catch(function () {

                        }).finally(function () {

                        });
                    }
                },
                addData: function () {
                    var uuid = 'damei' + new Date().getTime();
                    var self = this;
                    self.list.push({
                        id: uuid,
                        dicName: '',
                        dicValue: '',
                        status: 1,
                        sort: '',
                        remarks: '',
                    })
                },
                //添加
                insert: function () {
                    var self = this;
                    var lists = [];
                    var review;
                    self.$validate(true,
                        function () {
                            self.submitting = true;
                            if (self.$validation.valid) {
                                self.list.forEach(function (item) {
                                    review = {
                                        dicName: item.dicName,
                                        dicValue: item.dicValue,
                                        sort: item.sort,
                                        remarks: item.remarks,
                                        status: item.status,
                                    };
                                    lists.push(review);
                                })
                                self.dictionary.list = lists;
                                self.$http.post('/api/system/dictionary/save', self.dictionary).then(function (res) {
                                    if (res.data.code == 1) {
                                        self.$toastr.success('操作成功');
                                        $modal.modal('hide');
                                        tt.$dataTable.bootstrapTable('refresh');
                                    }
                                })
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
                            if (isNaN(id)) {
                            } else {
                                self.$http.get('/api/system/dictionary/' + id + '/del').then(function (res) {
                                    if (res.data.code == 1) {
                                        self.$toastr.success('操作成功');
                                        swal.close();
                                    }
                                })
                            }
                            self.list.splice(index, 1);
                            swal.close();
                        })
                    } else {
                        self.list.splice(index, 1);
                    }
                },
            },
            created: function () {
            },
            ready: function () {
                this.findDictionary();
            }
        });
        // 创建的Vue对象应该被返回
        return showBill;
    }
})();