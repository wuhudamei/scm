var vueModal;
+(function () {
    $('#applyManage').addClass('active');
    $('#catalogMenu').addClass('active');
    var vueIndex = new Vue({
        el: '#container',
        mixins: [RocoVueMixins.DataTableMixin],
        data: {
            // 面包屑
            breadcrumbs: [{
                path: '/',
                name: '主页'
            },
                {
                    path: '/',
                    name: '类目管理',
                    active: true //激活面包屑的
                }],
            // 查询表单
            form: {
                keyword: '',
                status: ''
            },
            $dataTable: null,
            selectedRows: {},
            //选中列
            modalModel: null,
            //模式窗体模型
            _$el: null,
            //自己的 el $对象
            _$dataTable: null //datatable $对象
        },
        created: function () {

        },
        ready: function () {
            this.drawTable();
        },
        methods: {
            query: function () {
                //将url初始化为空,表示不按照url分类
                this.form.parentId = null;
                //将分类显示还原
                this.breadcrumbs = [{
                    path: '/',
                    name: '主页'
                },
                    {
                        path: '/',
                        name: '分类管理',
                        active: true //激活面包屑的
                    }],
                    this.$dataTable.bootstrapTable('selectPage', 1);
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
                    url: '/api/catalog/list',
                    method: 'get',
                    dataType: 'json',
                    cache: false,
                    //去缓存
                    pagination: true,
                    //是否分页
                    pageSize: 25,
                    sidePagination: 'server',
                    //服务端分页
                    queryParams: function (params) {
                        // 将table 参数与搜索表单参数合并
                        return _.extend({},
                            params, self.form);
                    },
                    mobileResponsive: true,
                    undefinedText: '0',
                    //空数据的默认显示字符
                    striped: true,
                    //斑马线
                    maintainSelected: true,
                    //维护checkbox选项
                    sortOrder: 'desc',
                    //默认排序方式
                    columns: [
                        {
                            field: 'name',
                            title: '分类名称',
                            width: '26%',
                            orderable: false,
                            align: 'left',
                            formatter: function (value, row, index) {
                                var cataUrl = row.url;
                                var idArray = cataUrl.split("-");
                                var leftPad = ""
                                if (idArray.length > 2) {
                                    var marginLeft = (idArray.length - 2) * 40;
                                    leftPad = "<label style='margin-left:" + marginLeft + "px'></label>";
                                }
                                return ' <a data-handle="show-click" data-id="' + row.id + ' " data-name="' + row.name + '" data-code="' + row.code + ' " data-status="' + row.status + '" href="#">' + leftPad + value + '</a>';
                            }
                        },
                        {
                            field: 'seq',
                            title: '排序值',
                            width: '4%',
                            align: 'center',
                            orderable: false,
                        },
                        {
                            field: 'checkScale',
                            title: '是否复尺',
                            width: '10%',
                            orderable: false,
                            align: 'center',
                            formatter: function (data) {
                                return data == '1' ? '是' : '否';
                            }
                        },
                        {
                            field: 'status',
                            title: '使用状态',
                            width: '10%',
                            orderable: false,
                            align: 'center',
                            formatter: function (data) {
                                return data == 'OPEN' ? '启用' : '停用';
                            }
                        },
                        {
                            field: 'id',
                            title: '操作',
                            width: '10%',
                            orderable: false,
                            align: 'center',
                            formatter: function (value, row, index) {
                                if (hasEditCatalogPrivelege) {
                                    var dealLabel = "停 用";
                                    var newStatus = "LOCK";
                                    var dealClsName = "btn-danger";
                                    if (row.status == 'LOCK') {
                                        dealLabel = "启 用";
                                        newStatus = "OPEN";
                                        dealClsName = "btn-primary";
                                    }
                                    return ' <button data-handle="edit-click" data-id="' + row.id + '" data-name="' + row.name + '" data-code="' + row.code + '" data-status="' + row.status + '" class="m-r-xs btn btn-xs btn-primary" type="button">编 辑</button>'
                                        + ' <button data-handle="switch-click" data-id="' + row.id + '" data-status="' + newStatus + '" class="m-r-xs btn btn-xs ' + dealClsName + '"  type="button">' + dealLabel + '</button>';
                                } else {
                                    return '';
                                }
                            }
                        }]
                });
                //编辑
                self.$dataTable.on('click', '[data-handle="edit-click"]',
                    function (e) {
                        self.$http.get('/api/catalog/' + $(this).data().id).then(function (res) {
                            if (res.data.code == 1) {
                                //编辑
                                createOrEditModal(res.data.data, true);
                            }
                        }).catch(function () {

                        }).finally(function () {
                        });
                        e.stopPropagation();

                    });

                //点击分类名称,查询该分类下的所有分类
                self.$dataTable.on('click', '[data-handle="show-click"]',
                    function (e) {
                        //给form查询条件赋值
                        vueIndex.form = {
                            keyword: '',
                            status: '',
                            //将当前id作为父id,查询其下的所有子
                            parentId: $(this).data().id
                        },
                            //给面包层赋值
                            vueIndex.breadcrumbs = [{
                                path: '/',
                                name: '主页'
                            },
                                {
                                    path: '/',
                                    name: '分类管理',
                                    active: true //激活面包屑的
                                },
                                {
                                    path: '/',
                                    name: $(this).data().name,
                                    active: true //激活面包屑的
                                }
                            ];
                        //执行查询方法
                        vueIndex.$dataTable.bootstrapTable('selectPage', 1);
                        e.stopPropagation();

                    });

                //停用启用
                self.$dataTable.on('click', '[data-handle="switch-click"]',
                    function (e) {
                        var paramData = {
                            "id": $(this).data().id,
                            "status": $(this).data().status
                        };
                        vueIndex.$http.post('/api/catalog/changeStatus', paramData, {
                            emulateJSON: true
                        }).then(function (res) {
                                Vue.toastr.success(res.data.message);
                                self.query();
                            },
                            function (error) {
                                Vue.toastr.error(error.responseText);
                            });

                        e.stopPropagation();
                    });

                self.checkEventHandle('ordNo');
            },

            createBtnClickHandler: function (e) {
                //新增,给状态初始化:"status": "OPEN","parent.id": "0"(不可行)
                createOrEditModal({"status": "OPEN", "checkScale": 0}, false);
            },

        }
    });

    // 新增或修改窗口
    function createOrEditModal(model, isEdit) {
        var _modal = $('#modal').clone();
        var $el = _modal.modal({
            height: 550,
            maxHeight: 500,
        });
        //定义变量存储父id,如果model中有parent数据,意味着是修改,就赋值!
        var parentId = 0;
        if (model.parent != undefined) {
            parentId = model.parent.id;
        }
        $el.on('shown.bs.modal',
            function () {
                // 获取 node
                var el = $el.get(0);
                // 是否修改 boolean 化
                isEdit = !!isEdit;

                // 创建 Vue 对象编译节点
                vueModal = new Vue({
                    el: el,
                    http: {
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                    },
                    // 模式窗体必须引用 ModalMixin
                    mixins: [RocoVueMixins.ModalMixin],
                    components: {
                        'web-uploader': RocoVueComponents.WebUploaderComponent
                    },
                    $modal: $el,
                    //模式窗体 jQuery 对象
                    created: function () {
                        this.drawTable();
                        this.fetchConvertUnit();
                    },
                    ready: function () {

                        if (parentId != '0') {
                            this.findDomainInfoByCataLogId(model.id);
                        }
                        this.domainInfo(parentId);

                    },
                    validators: {
                        num: {
                            message: '请输入正确的损耗系数，例：（1或1.00）',
                            check: function (val) {
                                // return /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/.test(val);
                                // return /^0|[0-9]+(.[0-9]{1,2})?$/.test(val);
                                return /^([0-9]\d{0,9}|0)([.]?|(\.\d{1,2})?)$/.test(val);
                            }
                        }
                    },
                    data: {
                        isEdit: isEdit,
                        catalogList: [],
                        id: model.id,
                        parentUrl: null,
                        parentId: parentId,
                        name: model.name,
                        icon: model.icon,
                        url: model.url,
                        seq: model.seq,
                        status: model.status,
                        lossFactor: model.lossFactor == undefined ? '1' : model.lossFactor,
                        catalogType: model.catalogType == undefined ? '' : model.catalogType,
                        useDecimal: model.useDecimal == undefined ? '0' : model.useDecimal,
                        mealCategory: model.mealCategory == undefined ? '0' : model.mealCategory,
                        'parent.id': parentId,
                        checkScale: model.checkScale,
                        domainIfon: [],
                        domains: [],
                        device: '',
                        domainAndCatalog: '',
                        types: [{'id': 1, 'name': "主材", 'type': 'PRINCIPAL_MATERIAL'},
                            {'id': 2, 'name': "基、辅材", 'type': 'AUXILIARY_MATERIAL'},
                            {'id': 3, 'name': "软饰", 'type': 'SOFT_ACCESSORIES'},
                            {'id': 4, 'name': "家具", 'type': 'FURNITURE'},
                            {'id': 5, 'name': "家电", 'type': 'HOME_APPLIANCES'}],
                        statusOptions: [
                            {
                                text: '启用',
                                value: 'OPEN'
                            },
                            {
                                text: '停用',
                                value: 'LOCK'
                            }
                        ],
                        checkScaleOptions: [
                            {
                                text: '是',
                                value: 1
                            },
                            {
                                text: '否',
                                value: 0
                            }
                        ],
                        show: true,
                        showCataLog: false,
                        convertUnitList: null,
                        convertUnit: model.convertUnit
                    },
                    methods: {
                        //根据id查询所选择的功能区
                        findDomainInfoByCataLogId: function (parentId) {
                            var self = this;
                            self.$http.get('/api/domaininfo/finddomaininfocatalogbyid?parentId=' + parentId).then(function (res) {
                                if (res.data.code == 1) {
                                    var bb = [];
                                    var aa = res.data.data;
                                    for (var i = 0; i < aa.length; i++) {
                                        bb[i] = aa[i].domainInfoId;
                                    }
                                    self.domains = bb;
                                } else {
                                    self.domainAndCatalog = [];
                                }
                            }).catch(function () {
                            }).finally(function () {
                            });
                        },

                        //查询功能区
                        domainInfo: function (parentId) {
                            var self = this;
                            self.$http.get('/api/domaininfo/finddomaininfoname').then(function (res) {
                                if (res.data.code == 1) {
                                    self.domainIfon = res.data.data;
                                } else {
                                    self.domainIfon = [];
                                }
                            }).catch(function () {
                            }).finally(function () {
                            });
                        },
                        drawTable: function () {
                            var self = this;
                            vueIndex.$http.post('/api/catalog/findCatalogList', {
                                emulateJSON: true
                            }).then(function (res) {
                                    if (res.data.code != "1") {
                                    } else {
                                        //给数组赋值
                                        self.catalogList = res.data.data;
                                        var url = model.url;
                                        var urlSize = url.split('-');
                                        if (parentId != 0 && urlSize.length == 3) {
                                            self.show = false;
                                            self.showCataLog = true;
                                        }

                                    }
                                },
                                function (error) {
                                    Vue.toastr.error(error.responseText);
                                });

                        },
                        fetchConvertUnit: function () {
                            var self = this;
                            self.$http.get('/api/system/dictionary/findByValue?dicValue=convert_unit').then(function (res) {
                                if (res.data.code == 1) {
                                    self.convertUnitList = res.data.data;
                                }
                            }).catch(function () {
                            }).finally(function () {
                            });
                        },

                        save: function () {
                            //当父id不为0时,就去改变panrentUrl的初始值
                            if (this.parentId != 0 && this.parentId != undefined) {

                                //遍历复选框集合,如果父id等于当前选中parentId,当前对象即为所需要对象
                                for (var i = 0, ilength = this.catalogList.length; i < ilength; i++) {
                                    if (this.parentId === this.catalogList[i].id) {
                                        this.parentUrl = this.catalogList[i].url;
                                        break;
                                    }
                                }
                            }

                            var self = this;
                            self.$validate(true,
                                function () {
                                    if (self.$validation.valid) {
                                        self.device = self.domains.toString();
                                        self.submitting = true;
                                        var data = self._data;
                                        delete  data.convertUnitList;
                                        delete  data.catalogList;
                                        delete  data.statusOptions;
                                        delete  data.checkScaleOptions;
                                        if (data.catalogType == '0') {
                                            delete data.catalogType;
                                        }
                                        self.$http.post('/api/catalog/saveOrUpdate', $.param(data)).then(function (res) {
                                                if (res.data.code == '1') {
                                                    Vue.toastr.success(res.data.message);
                                                    $el.modal('hide');
                                                    vueIndex.$dataTable.bootstrapTable('refresh');
                                                    self.$destroy();
                                                } else {
                                                    Vue.toastr.error(res.data.message);
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
                    watch: {
                        'parentId': function (val) {
                            var self = this;
                            var id;
                            self.$http.post('/api/catalog/findCatalogParent?parentId='+val).then(function (res) {
                                if (res.data.code == "1") {
                                    //给数组赋值
                                    id = res.data.data;
                                    console.log(id);
                                    if (id == 0) {
                                        self.show = false;
                                        self.showCataLog = true;
                                    }else{
                                        self.show = true;
                                        self.showCataLog = false;
                                    }

                                }
                            })
                            // if (val != '0') {
                            //     self.show = false;
                            //     self.showCataLog = true;
                            // } else {
                            //     self.show = true;
                            //     self.showCataLog = false;
                            // }
                        }
                    }
                });

                // 创建的Vue对象应该被返回
                return vueModal;
            });
    }
})();