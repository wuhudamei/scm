+(function () {
    $('#accountManage').addClass('active');
    $('#userMenu').addClass('active');

    var vueUser = new Vue({
        el: '#container',
        mixins: [DaMeiVueMixins.DataTableMixin],
        data: {
            breadcrumbs: [{
                path: '/admin',
                name: '主页'
            }, {
                path: '/',
                name: '用户管理',
                active: true
            }],
            form: {
                keyword: '',
                acctType: '',
                storeCode: ''
            },
            storeList: [],
            regionSupplierList: [],
            $dataTable: null,
            selectedRows: {},
            modalModel: null,
            _$el: null,
            _$dataTable: null
        },
        created: function () {
        },
        ready: function () {
            this.initData();
            this.drawTable();
        },
        methods: {
            query: function () {
                this.$dataTable.bootstrapTable('selectPage', 1);
            },

            initData: function () {
                var self = this;
                this.$http.get('/api/product/store/all').then(function (resp) {
                    self.storeList = resp.data.data;
                });
            },

            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/api/user/list',
                    method: 'get',
                    dataType: 'json',
                    cache: false,
                    pagination: true,
                    sidePagination: 'server',
                    queryParams: function (params) {
                        return _.extend({}, params, self.form);
                    },
                    mobileResponsive: true,
                    undefinedText: '',
                    striped: true,
                    maintainSelected: true,
                    sortOrder: 'ASC',
                    columns: [{
                        field: 'username',
                        title: '用户名',
                        defaultContent: '',
                        width: '15%',
                        orderable: false
                    },
                        {
                            field: 'name',
                            title: '姓名',
                            defaultContent: '',
                            width: '12%'
                        },
                        {
                            field: 'mobile',
                            title: '手机',
                            defaultContent: '',
                            width: '12%'
                        },
                        /*{
                         field: 'position',
                         title: '岗位',
                         defaultContent: '-',
                         width: '10%'
                         },*/
                        {
                            field: 'acctType',
                            title: '账户类型',
                            defaultContent: '',
                            width: '10%',
                            formatter: function (value, row, index) {
                                var label = "";
                                switch (value) {
                                    case "ADMIN":
                                        label = "管理员";
                                        break;
                                    case "REGION_SUPPLIER":
                                        label = "区域供应商";
                                        break;
                                    case "PROD_SUPPLIER":
                                        label = "商品供应商";
                                        break;
                                    case "STORE":
                                        label = "门店管理员";
                                        break;
                                    case "FINANCE":
                                        label = "财务人员";
                                        break;
                                    case "MATERIAL_CLERK":
                                        label = "材料部下单员";
                                        break;
                                    case "MATERIAL_MANAGER":
                                        label = "材料部主管";
                                        break;
                                    case "CHAIRMAN_FINANCE":
                                        label = "董事长财务";
                                        break;
                                    default:
                                        label = "待设置";
                                }
                                return label;
                            }
                        },
                        {
                            field: 'storeName',
                            title: '门店名称',
                            defaultContent: '',
                            width: '10%'
                        },
                        {
                            field: 'supplierName',
                            title: '供应商名称',
                            defaultContent: '',
                            width: '20%'
                        },
                        {
                            field: 'id',
                            title: '操作',
                            className: 'td_center',
                            orderable: false,
                            width: '15%',
                            formatter: function (value, row, index) {
                                if (DaMeiUtils.hasPermission('user:edit')) {
                                    var html = '<button style="margin-left:10px;" data-handle="user-edit" data-id="' + value + '"  data-username="' + row.username + '" data-name="' + row.name + '" data-mobile="' + row.mobile + '" data-accttype="' + row.acctType + '" data-status="' + row.status + '" data-position="' + row.position + '" data-supplierid="' + row.supplierId + '" data-storecode="' + row.storeCode + '"  class="m-r-xs btn btn-xs btn-primary" type="button">编辑</button>';
                                }
                                return html;
                            }
                        }]
                });

                //编辑
                self.$dataTable.on('click', '[data-handle="user-edit"]',
                    function (e) {
                        var model = $(this).data();
                        model.username = model.username;
                        model.acctType = model.accttype === 'undefined' ? '' : model.accttype;
                        model.position = model.position === 'undefined' ? '' : model.position;
                        model.supplierId = model.supplierid === 'undefined' ? '' : model.supplierid;
                        model.regionId = '';
                        model.storeCode = model.storecode === 'undefined' ? '' : model.storecode;
                        model.storeArray = self.storeList;
                        model.regionSupplierArray = [];
                        model.supplierArray = '';
                        if (model.acctType == 'REGION_SUPPLIER') {
                            model.regionId = model.supplierId;
                            self.$http.get('/api/regionSupplier/getByStoreCode/' + model.storeCode).then(function (resp) {
                                model.regionSupplierArray = resp.data.data;
                                createOrEditModal(model, true);
                            });

                        } else if (model.acctType == 'PROD_SUPPLIER') {
                            self.$http.get('/api/supplier/getBySupplierId?supplierId=' + model.supplierId).then(function (resp) {
                                model.regionId = resp.data.data.regionSupplierId;
                                model.regionSupplierArray = resp.data.data.regionSupplierList;
                                model.supplierArray = resp.data.data.supplierList;
                                createOrEditModal(model, true);
                            });
                        } else {
                            createOrEditModal(model, true);
                        }

                        e.stopPropagation();
                    });

                //重置密码
                self.$dataTable.on('click', '[data-handle="user-resetpwd"]',
                    function (e) {
                        var id = $(this).data('id');
                        swal({
                                title: '重置密码',
                                text: '确定重置密码吗？',
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
                                vueUser.$http.post('/api/user/resetpwd/' + id).then(function (res) {
                                    if (res.data.code == 1) {
                                        Vue.toastr.success(res.data.message);
                                    }
                                }).catch(function () {

                                }).finally(function () {
                                    swal.close();
                                });
                            });
                        e.stopPropagation();
                    });

                //设置角色
                self.$dataTable.on('click', '[data-handle="user-roles"]',
                    function (e) {
                        var userId = $(this).data('id');
                        vueUser.$http.get('/api/user/role/' + userId).then(function (resp) {
                            setRoleModal(userId, resp.data)
                        });
                        e.stopPropagation();
                    });

                //改变状态
                self.$dataTable.on('click', '[data-handle="user-switch"]',
                    function (e) {
                        var data = $(this).data();
                        swal({
                                title: '改变状态',
                                text: '确定改变状态吗？',
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
                                vueUser.$http.get('/api/user/' + data.id + '/switch?status=' + data.status).then(function (res) {
                                    if (res.data.code == 1) {
                                        Vue.toastr.success(res.data.message);
                                        vueUser.$dataTable.bootstrapTable('selectPage', 1);
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
                createOrEditModal({
                    storeArray: this.storeList,
                    regionSupplierArray: this.regionSupplierList,
                    regionId: '',
                    supplierArray: [],
                    supplierId: '',
                    status: 'OPEN',
                    acctType: ''
                }, false);
            },
            initAccount: function (e) {
                var self = this;
                swal({
                        title: '同步认证中心用户',
                        text: '确定同步用户账号吗？',
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
                        self.$http.get('/api/user/initUserAccount').then(function (res) {
                            if (res.data.code == 1) {
                                Vue.toastr.success(res.data.message);
                                self.$dataTable.bootstrapTable('selectPage', 1);
                            } else {
                                Vue.toastr.error(res.data.message);
                            }

                        }).catch(function () {

                        }).finally(function () {
                            swal.close();
                        });
                    });

                e.stopPropagation();
            }
        }
    });
    //end of vueUser

    // 新增或修改窗
    function createOrEditModal(model, isEdit) {
        var _modal = $('#modal').clone();
        var $el = _modal.modal({
            height: 500,
            maxHeight: 500
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
            mixins: [DaMeiVueMixins.ModalMixin],
            $modal: $el,
            created: function () {
            },
            data: model,
            methods: {

                //根据所选门店查询区域供应商
                chooseStore: function (ele) {
                    var self = this;
                    //self.regionSupplierArray = [];
                    if (self.acctType != 'ADMIN' && self.acctType != 'FINANCE' && self.storeCode) {
                        self.$http.get('/api/regionSupplier/filterByStoreCode?storeCode=' + self.storeCode).then(function (resp) {
                            self.regionSupplierArray = resp.data.data;
                        });
                    }
                    ele.stopPropagation();
                },

                //根据所选门店查询商品供应商
                choolseRegionSupply: function (ele) {
                    var self = this;
                    if (this.acctType == 'PROD_SUPPLIER') {
                        this.$http.get('/api/supplier/filterByRegionIdAndStatus?regionSupplierId=' + self.regionId).then(function (resp) {
                            self.supplierArray = resp.data.data;
                        });
                    }
                    ele.stopPropagation();
                },

                save: function () {
                    var self = this;
                    var validateFlag = true;
                    var validateMessage = '';
                    if (self.acctType != 'ADMIN' && self.acctType != 'CHAIRMAN_FINANCE') {
                        if (self.storeCode == '') {
                            validateMessage = '请选择门店！';
                            validateFlag = false;
                        }
                    }
                    if (self.acctType == 'REGION_SUPPLIER') {
                        if (self.regionId == '') {
                            validateMessage = '请选择区域供应商！';
                            validateFlag = false;
                        }
                    } else if (self.acctType == 'PROD_SUPPLIER') {
                        if (self.supplierId == '') {
                            validateMessage = '请选择商品供应商！';
                            validateFlag = false;
                        }
                    }

                    if (!validateFlag) {
                        Vue.toastr.warning(validateMessage);
                        return;
                    }

                    self.$validate(true,
                        function () {
                            if (self.$validation.valid) {
                                self.submitting = true;
                                if (self.acctType == 'REGION_SUPPLIER') {
                                    self.supplierId = self.regionId;
                                }

                                self.$http.post('/api/user/saveUser', $.param(self._data)).then(function (res) {
                                        Vue.toastr.success(res.data.message);
                                        $el.modal('hide');
                                        vueUser.$dataTable.bootstrapTable('refresh');
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
        return vueModal;
    }

    //设置角色窗
    function setRoleModal(userId, allRoleList) {
        // 选择中的角色
        var selectedRoles = {};
        if (allRoleList) {
            $(allRoleList).each(function (index, roleItem) {
                if (roleItem.checked == true) {
                    selectedRoles[roleItem.name] = roleItem.id;
                }
            })
        }

        var _modal = $('#setRoleModal').clone();
        var $el = _modal.modal({
            height: 300,
            maxHeight: 300
        });

        var el = $el.get(0);

        var vueModal = new Vue({
            el: el,
            http: {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            },
            mixins: [DaMeiVueMixins.ModalMixin],
            $modal: $el,
            created: function () {
            },
            data: {
                roles: allRoleList
            },
            methods: {
                //点击勾选角色触发
                checkSub: function (role, e) {
                    var checked = e.target.checked;
                    if (checked) {
                        selectedRoles[role.name] = role.id;
                    } else {
                        selectedRoles[role.name] = null;
                    }
                },

                saveRoles: function () {
                    var self = this;
                    var checkedRoleIds = [];
                    for (var key in selectedRoles) {
                        if (selectedRoles[key]) {
                            checkedRoleIds.push(selectedRoles[key]);
                        }
                    }

                    $.ajax({
                        url: ctx + '/api/user/setRole',
                        type: 'post',
                        dataType: 'json',
                        data: {
                            userId: userId,
                            roles: checkedRoleIds
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

        return vueModal;
    }
})();