+(function () {
    $('#dictManage').addClass('active');
    $('#measureUnit').addClass('active');
    Vue.validator('telphone', function (tel) {
        return /^1[3|4|5|7|8]\d{9}$/.test(tel);
    })
    var vueIndex = new Vue({
        el: '#container',
        mixins: [RocoVueMixins.DataTableMixin],
        data: {
            breadcrumbs: [{
                path: '/admin',
                name: '主页'
            }, {
                path: '/',
                name: '计量单位',
                active: true
            }],
            form: {
                keyword: '',
                status: ''
            },
            $dataTable: null,
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
                this.$dataTable.bootstrapTable('selectPage', 1);
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/api/dict/measure',
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
                            field: 'name',
                            title: '名称',
                            width: '15%',
                            orderable: true,
                            align: 'center'
                        },
                        {
                            field: 'status',
                            title: '状态',
                            width: '10%',
                            align: 'center',
                            orderable: false,
                            formatter: function (value) {
                                var val = '启用'
                                if (value == 'LOCK') {
                                    val = '停用'
                                }
                                return val
                            }
                        },
                        {
                            field: 'id',
                            title: '操作',
                            width: '20%',
                            orderable: false,
                            align: 'center',
                            formatter: function (value, row) {
                                var html = ''
                                var deal_name = "停用";
                                var class_val = "m-r-xs btn btn-xs btn-danger";
                                var nostatus = ''
                                if (row.status == 'LOCK') {
                                    deal_name = "启用";
                                    class_val = "m-r-xs btn btn-xs btn-primary";
                                    nostatus = 'OPEN'
                                } else {
                                    nostatus = 'LOCK'
                                }
                                html += '<button style="margin-left:10px;"'
                                html += 'data-handle="data-edit"'
                                html += 'data-id="' + value + '"'
                                html += 'data-name="' + row.name + '"'
                                html += 'data-status="' + row.status + '"'
                                html += 'class="m-r-xs btn btn-xs btn-primary" type="button">编辑</button>'
                                html += '<button style="margin-left:15px;" data-handle="data-deal" data-id="' + value + '"  data-status="' + nostatus + '" class="' + class_val + '" type="button">' + deal_name + '</button>';
                                return html
                            }
                        }]
                });
                //编辑
                self.$dataTable.on('click', '[data-handle="data-edit"]',
                    function (e) {
                        var model = $(this).data()
                        createOrEditModal(model, true)
                        e.stopPropagation()
                    }
                );
                //起停
                self.$dataTable.on('click', '[data-handle="data-deal"]',
                    function (e) {
                        var model = $(this).data()
                        self.$http.post('/api/dict/measure/save', model, {emulateJSON: true}).then(function (res) {
                                if (res.data.code === '1') {
                                    Vue.toastr.success(res.data.message);
                                    vueIndex.$dataTable.bootstrapTable('selectPage', 1);
                                }
                            },
                            function (error) {
                                Vue.toastr.error(error.responseText);
                            }).catch(function () {
                        }).finally(function () {
                            self.submitting = false;
                        });
                        e.stopPropagation()
                    }
                );
            },
            createBtnClickHandler: function (e) {
                //新增
                createOrEditModal({
                        "status": "OPEN"
                    },
                    false);
            }
        }
    });

    //  新增／编辑
    function createOrEditModal(model, isEdit) {
        var _modal = $('#contractModal').clone();
        var $el = _modal.modal({
            height: 250,
            maxHeight: 400
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
                    mixins: [RocoVueMixins.ModalMixin],
                    components: {
                        'web-uploader': RocoVueComponents.WebUploaderComponent
                    },
                    $modal: $el,
                    created: function () {
                    },
                    data: model,
                    methods: {
                        save: function () {
                            var self = this;
                            self.$validate(true,
                                function () {
                                    if (self.$validation.valid) {
                                        self.submitting = true;
                                        self.$http.post('/api/dict/measure/save', $.param(self._data)).then(function (res) {
                                                if (res.data.code === '1') {
                                                    Vue.toastr.success(res.data.message);
                                                    $el.modal('hide');
                                                    vueIndex.$dataTable.bootstrapTable('refresh');
                                                    self.$destroy();
                                                }
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
                    },
                });
                // 创建的Vue对象应该被返回
                return vueModal;
            });
    }
})();