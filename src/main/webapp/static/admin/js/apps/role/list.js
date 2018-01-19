+(function () {
    $('#accountManage').addClass('active');
    $('#roleMenu').addClass('active');
    var vueRole = new Vue({
        el: '#container',
        mixins: [RocoVueMixins.DataTableMixin],
        data: {
            breadcrumbs: [{
                path: '/admin',
                name: '主页'
            }, {
                path: '/',
                name: '角色管理',
                active: true
            }],
            form: {
                keyword: ''
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
                this.$dataTable.bootstrapTable('selectPage', 1);
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/api/role',
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
                    },
                        {
                            field: 'name',
                            title: '角色',
                            width: '35%',
                            orderable: false
                        },
                        {
                            field: 'description',
                            title: '描述',
                            width: '35%',
                            orderable: false
                        },
                        {
                            field: 'id',
                            title: '操作',
                            width: '20%',
                            orderable: false,
                            formatter: function (value, row, index) {
                                if (row.name != 'administrator') {
                                    var html = ' <button style="margin-left:10px;" data-handle="edit-click" data-id="' + row.id + '" data-name="' + row.name + '" data-description="' + row.description + '" class="m-r-xs btn btn-xs btn-primary" type="button">编 辑</button>' + '<button style="margin-left:10px;" data-handle="authorize-click" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-primary" type="button">授权</button>' + '<button style="margin-left:10px;" data-handle="del-click" data-id="' + row.id + '" class="m-r-xs btn btn-xs btn-danger" type="button">删 除</button>'
                                    return html;
                                } else {
                                    return "";
                                }
                            }
                        }]
                });
                //编辑
                self.$dataTable.on('click', '[data-handle="edit-click"]',
                    function (e) {
                        createOrEditModal($(this).data(), true);
                        e.stopPropagation();
                    });
                //授权
                self.$dataTable.on('click', '[data-handle="authorize-click"]',
                    function (e) {
                        setPermissionsModal($(this).data('id'));
                        e.stopPropagation();
                    });
                //删除
                self.$dataTable.on('click', '[data-handle="del-click"]',
                    function (e) {
                        var id = $(this).data('id');
                        swal({
                                title: '删除角色',
                                text: '确定删除吗？',
                                type: 'info',
                                confirmButtonText: '确定',
                                cancelButtonText: '取消',
                                showCancelButton: true,
                                showConfirmButton: true,
                                showLoaderOnConfirm: true,
                                confirmButtonColor: '#ed5565',
                                closeOnConfirm: false
                            },
                            function () {
                                vueRole.$http.post('/api/role/' + id + '/del').then(function (res) {
                                    if (res.data.code == 1) {
                                        Vue.toastr.success(res.data.message);
                                        vueRole.$dataTable.bootstrapTable('refresh');
                                    }
                                }).catch(function () {

                                }).finally(function () {
                                    swal.close();
                                });
                            });
                        e.stopPropagation();
                    });
            },

            createBtnClickHandler: function (e) {
                createOrEditModal({},
                    false);
            }
        }
    });

    //弹窗授权
    function setPermissionsModal(roleId) {
        vueRole.$http.get('/api/role/findRolePermission/' + roleId).then(function (res) {
            if (res.data.code == 1) {
                var perArr = [];
                for (var key in res.data.data) {
                    var obj = {
                        'key': key,
                        'content': res.data.data[key]
                    }
                    perArr.push(obj);
                }
                showPermissionsModal(roleId, perArr);
            }
        }).catch(function () {

        }).finally(function () {
        });
    };

    //实现弹窗授权
    function showPermissionsModal(roleId, data) {
        // 选择的权限
        var selectedRoles = {};
        if (data) {
            $(data).each(function (index, el) {
                el.content.forEach(function (_this) {
                    if (_this.checked == true) {
                        selectedRoles[_this.name] = _this.id;
                    }
                })

            })
        }

        var _modal = $('#setPermissionModal').clone();
        var $el = _modal.modal({
            height: 450,
            maxHeight: 450
        });
        var el = $el.get(0);

        var permissionModal = new Vue({
            el: el,
            http: {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            },
            mixins: [RocoVueMixins.ModalMixin],
            $modal: $el,
            created: function () {
            },
            data: {
                permissions: data
            },
            methods: {
                //全选
                checkAll: function (permission, e) {
                    var self = this;
                    var checked = e.target.checked;
                    _.each(permission.content,
                        function (content, index, array) {
                            content.checked = checked;
                            self.checkSub(content, e);
                        });
                },
                //所有权限选择
                selAllCb: function (permissions, e) {
                    var self = this;
                    _.each(permissions,
                        function (permission, index, array) {
                            self.checkAll(permission, e);
                        });
                },
                //选择子项
                checkSub: function (content, e) {
                    var checked = e.target.checked;
                    if (checked) {
                        selectedRoles[content.name] = content.id;
                    } else {
                        selectedRoles[content.name] = null;
                    }
                },
                //报销选择的权限
                savePermission: function () {
                    var self = this;
                    var permissions = [];
                    for (var key in selectedRoles) {
                        if (selectedRoles[key]) {
                            permissions.push(selectedRoles[key]);
                        }
                    }

                    $.ajax({
                        url: ctx + '/api/role/setPerm',
                        type: 'post',
                        dataType: 'json',
                        data: {
                            roleId: roleId,
                            permissions: permissions
                        },
                        traditional: true,
                        cache: false,
                        success: function (res) {
                            if (res.code == "1") {
                                Vue.toastr.success(res.message);
                                $el.modal('hide');
                                self.$destroy();
                            }
                        }
                    });
                }
            }
        });

        // 创建的Vue对象应该被返回
        return permissionModal;
    };

    // 新增或修改窗口
    function createOrEditModal(model, isEdit) {
        var _modal = $('#modal').clone();
        var $el = _modal.modal({
            height: 300,
            maxHeight: 300
        });

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
                                self.$http.post('/api/role/saveOrUpdate', $.param(self._data)).then(function (res) {
                                        Vue.toastr.success(res.data.message);
                                        $el.modal('hide');
                                        vueRole.$dataTable.bootstrapTable('refresh');
                                        self.$destroy();
                                    },
                                    function (error) {
                                        Vue.toastr.error(error.responseText);
                                    }).catch(function () {
                                }).finally(function () {
                                    self.submitting = false;
                                });
                            }
                        });

                }
            }
        });

        // 创建的Vue对象应该被返回
        return vueModal;
    }
})();