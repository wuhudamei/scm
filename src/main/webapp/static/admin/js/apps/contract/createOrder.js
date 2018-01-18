var createOrEditProductVue
+(function () {

    createOrEditProductVue = new Vue({
        el: '#container',
        components: {
            'web-uploader': RocoVueComponents.WebUploaderComponent
        },
        validators: {
            numeric: function (val/*,rule*/) {
                // return  /^[-+]?[0-9]+$/.test(val) || (val === '')   //这个验证是只可以输入正整数
                return /^\d+(\.\d+)?$/;
            },
            overed: function (val, rule) {
                return val <= rule
            }
        },
        data: {
            // 分类列表
            categories: null,
            // 所有的区域供应商
            allSuppliers: null,
            //所有的门店
            allStores: null,
            // 编辑id
            productId: null,
            // 禁用按钮
            submiting: false,

            // 合同id
            contractId: '',
            // 合同编码
            contractCode: '',
            // 编辑时带来的ID
            editID: '',
            // 排除的skuid
            excludeSkuIdList: '',
            excludeSkuIdListArr: [],
            // sku之前的长度
            oldVal: '0',
            // sku新增之后的长度
            newVal: '',
            form: {
                // 主说明
                note: '',
                // 商品详情
                orderItemList: []
            },
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
            list: [],
            copy: '',
            change: '',
            //制单类型
            placeEnum:null,
            //选择其他费用说明--从字典表中加载
            otherFeeNoteList: null
        },
        created: function () {
            this.contractCode = this.$parseQueryString()['contractCode'];
            this.contractId = this.$parseQueryString()['contractId'];
            this.editID = this.$parseQueryString()['editID'];
            this.copy = this.$parseQueryString()['copy'];
            this.change = this.$parseQueryString()['change'];
            // 获取详情
            if (this.editID) {
                this.fetchOrderDetail();
                $('#order').addClass('active');
                $('#indentOrder').addClass('active');
            } else {
                $('#customer').addClass('active');
                $('#contractInfo').addClass('active');
            }
            // 分类列表
            this.fetchCategory();
            // 获取全部的区域供应商
            this.fetchAllStores();
        },
        ready: function () {
            this.findOtherFeeNoteList();

        },
        methods: {
            closeWin: function () {
                window.history.go(-1)
            },
            //选择其他费用说明--从字典表中加载
            findOtherFeeNoteList: function () {
                var self = this;
                self.$http.get('/api/system/dictionary/findByValue?dicValue=other_cost_type').then(function (res) {
                    if (res.data.code == 1) {
                        self.otherFeeNoteList = res.data.data;
                    }
                });
            },
            //动态增加其他费用
            addData: function (index) {
                var self = this;
                var uuid = 'mdni' + new Date().getTime();
                var obj = {
                    id: uuid,
                    otherFee: '',
                    otherFeeNoteList: self.otherFeeNoteList,
                    otherFeeNote: '',
                };
                self.form.orderItemList[index].otherFeesList.push(obj)
            },
            //动态删除其他费用
            removeData: function (index, ind) {
                var self = this;
                self.form.orderItemList[index].otherFeesList.splice(ind, 1);
            },
            // 获取商品信息
            fetchOrderDetail: function () {
                var self = this
                self.$http.get('/api/order/' + self.editID + "/detail?isEditDraftOrder=true").then(function (res) {
                    if (res.data.code == 1) {
                        res.data.data.orderItemList.forEach(function (orderItem) {
                            /*var otherNote = orderItem.otherFeeNote;
                            if (otherNote.length > 0) {
                                var otherFees = otherNote.substr(0, otherNote.length - 1).split('@');
                                orderItem.otherFeesList = [];
                                otherFees.forEach(function (obj) {
                                    var uuid = 'mdni' + new Date().getTime();
                                    var other = obj.split('=');
                                    orderItem.otherFeesList.push({
                                        id: uuid,
                                        otherFee: other[1],
                                        otherFeeNote: other[0]
                                    });
                                });
                            } else {
                                orderItem.otherFeesList = [];
                                var uuid = 'mdni' + new Date().getTime();
                                var obj = {
                                    id: uuid,
                                    otherFee: '',
                                    otherFeeNote: ''
                                };
                                orderItem.otherFeesList.push(obj)
                            }*/

                            orderItem.otherFeesList.forEach(function (fee) {
                                fee.otherFee = fee.feeValue;
                                fee.otherFeeNote = fee.feeType;
                                delete fee.feeValue;
                                delete fee.feeType;
                                delete fee.itemId;
                            });
                        });
                        self.form = $.extend({}, self.form, res.data.data);
                    }
                }).catch(function () {
                }).finally(function () {
                })
            },
            // 获取全部的门店
            fetchAllStores: function () {
                var self = this;
                self.$http.get('/api/product/store/all').then(function (res) {
                    if (res.data.code == 1) {
                        self.allStores = res.data.data;
                        if (self.productId != undefined) {
                            self.fetchProductDetail();
                        }
                    }
                }).catch(function () {
                }).finally(function () {
                })
            },
            // 分类列表
            fetchCategory: function () {
                var self = this
                self.$http.get('/api/catalog/findCatalogList').then(function (res) {
                    if (res.data.code == 1) {
                        self.categories = res.data.data
                    }
                }).catch(function () {
                }).finally(function () {
                })
            },
            quantityChange: function (index, quantity, stock) {
                if (quantity > stock) {
                    this.form.orderItemList[index].quantity = stock;
                }
            },
            remove: function (index) {
                this.form.orderItemList.splice(index, 1)
            },
            // 保存提交
            saveProduct: function (flag) {
                var self = this;
                var obj;
                var formData = {
                    'contractCode': self.contractCode,
                    'orderItemList': self.form.orderItemList,
                    'note': self.form.note,
                    'placeEnum': self.placeEnum
                };
                delete self.form.editTime;
                delete self.form.createTime;
                delete self.form.editor;
                delete self.form.creator;
                obj = formData;
                if (self.form.orderItemList.length <= 0) {
                    Vue.toastr.error('至少选择一种sku商品');
                    return false;
                }else{
                    var flag = false;
                    self.form.orderItemList.forEach(function (item) {
                        if(!item.quantity){
                            Vue.toastr.error('订货项中订货数量不能为空!');
                            flag = true;
                        }
                    });
                    if(flag){
                        return false;
                    }
                }


                obj.orderItemList.forEach(function (item) {

                    if (item.hasOtherFee == false) {
                        item.otherFee = 0;
                        item.otherFeeNote = "";
                        //给其他费用集合置为null
                        item.otherFeesList = [];
                    }

                    //var otherFees = 0;
                    //var otherFeeNotes = '';

                    //遍历费用集合,取出掉id
                    item.otherFeesList.forEach(function (element, index) {
                        //改名
                        element.feeValue = element.otherFee;
                        element.feeType = element.otherFeeNote;
                        //删除原属性
                        //将该对象中的id删除
                        delete element.id;
                        delete element.otherFee;
                        delete element.otherFeeNote;
                        delete element.otherFeeNoteList;

                        //otherFees = otherFees + Number(element.otherFee);
                       // otherFeeNotes += element.otherFeeNote + '=' + Number(element.otherFee) + '@';
                    })
                    //item.otherFee = otherFees
                   // item.otherFeeNote = otherFeeNotes
                    //delete  item.otherFeesList;
                });
                /*obj.orderItemList.forEach(function (item) {
                    if (item.hasOtherFee == false) {
                        item.otherFee = 0;
                        item.otherFeeNote = "";
                    }
                });*/
                if(self.copy === ''){
                    obj.id === null;//点击复制按钮，将产生新的单号，所以把传到后台的id设置为null
                    obj.placeEnum = 'NORMAL';
                }
                if(self.change === ''){
                    obj.id === null;//点击变更按钮，将产生新的单号，所以把传到后台的id设置为null
                    obj.placeEnum = 'CHANGE';
                }
                self.submiting = true;
                self.$http.post('/api/order/saveOrUpdate', JSON.stringify(obj)).then(function (res) {
                    self.submiting = false;
                    if (res.data.code == '1') {
                        Vue.toastr.success(res.data.message);
                        setTimeout(function () {
                            if (!self.editID) {
                                window.location.href = '/admin/contract';
                            } else {
                                window.location.href = '/admin/indentOrder/list';
                            }

                        }, 500)
                    } else {
                        Vue.toastr.error(res.data.message);
                    }
                });
            },
            // 选择品牌或者批次号
            selectBrands: function (type) {
                vendorBrands(type);
            },
            //选择复尺结果
            selectReviewResults: function (type, index) {
                vendorReviewResults(type, index);
            }
        },
        watch: {
            // 初始化datetimepicker
            'form.orderItemList': function () {
                this.$nextTick(function () {
                    for (var i = this.oldVal; i < this.form.orderItemList.length; i++) {
                        $(('#time' + i), this._$el).datetimepicker({
                            startDate: new Date()
                        });
                    }
                    this.excludeSkuIdListArr = [];
                    this.excludeSkuIdList = '';
                    for (var i = 0; i < this.form.orderItemList.length; i++) {
                        this.excludeSkuIdListArr.push(this.form.orderItemList[i].sku.id);
                        this.excludeSkuIdList = this.excludeSkuIdListArr.join(',');
                    }
                })
            }
        }
    });


    // 品牌||批次号弹窗 type = true 品牌 false 批次号
    function vendorBrands(type) {
        var $modal = $('#modalBrand').clone();
        $modal.modal({
            height: 500,
            maxHeight: 500
        });
        $modal.on('shown.bs.modal',
            function () {
                vueModal2 = new Vue({
                    el: $modal.get(0),
                    mixins: [RocoVueMixins.DataTableMixin],
                    data: {
                        form: {
                            keyword: '',
                            excludeSkuIdList: createOrEditProductVue.excludeSkuIdList,
                            catalogUrl: '',
                            convertUnit:'',
                            supplierId: '',
                            status: 'OPEN',
                            allSupplierId:'',
                            allStoreCode:'',
                            ProductStatusEnum:'LIST',
                            processStatus:'sku_shelf_shelves'
                        },
                        // 分类列表
                        categories: createOrEditProductVue.categories,
                        // 供应商列表
                        suppliers: null,
                        // 所有的区域供应商
                        allSuppliers: createOrEditProductVue.allSuppliers,
                        allStores: createOrEditProductVue.allStores,
                        allSupplierId: '',
                        allStoreCode: '',
                        type: type,
                        $dataTable: null,
                        selectedRows: {},
                        // 选中列
                        modalModel: null,
                        // 模式窗体模型
                        _$el: null,
                        // 自己的 el $对象
                    },
                    created: function () {

                    },
                    ready: function () {
                        this.drawTable();
                    },
                    methods: {
                        fetchAllSuppliers: function (val) {
                            var self = this
                            self.$http.get('/api/regionSupplier/filterByStoreCode?storeCode=' + val).then(function (res) {
                                if (res.data.code == 1) {
                                    self.allSuppliers = res.data.data
                                    if (self.productId != undefined) {
                                        self.fetchProductDetail()
                                    }
                                }
                            }).catch(function () {
                            }).finally(function () {
                            })
                        },
                        // 供应商列表
                        fetchSuppliers: function (id) {
                            var self = this
                            if(id == ""){
                                self.suppliers = [];
                                return ;
                            }
                            self.$http.get('/api/supplier/filterByRegionIdAndStatus?status=OPEN&regionSupplierId=' + id).then(function (res) {
                                if (res.data.code == 1) {
                                    self.suppliers = res.data.data
                                    if (self.productId != undefined) {
                                        // self.fetchProductDetail()
                                    }
                                }
                            }).catch(function () {
                            }).finally(function () {
                            })
                        },
                        choose: function () {
                            var obj = document.getElementsByName("product");
                            var order
                            createOrEditProductVue.oldVal = createOrEditProductVue.form.orderItemList.length
                            for (k in obj) {
                                if (obj[k].checked) {
                                    order = {
                                        otherFeesList: [],
                                        note: '',
                                        reviewSizeResult: '',
                                        installDate: '',
                                        installationLocation: '',
                                        quantity: '',
                                        otherFeeNote: '',
                                        otherFee: '',
                                        hasOtherFee: false,
                                        sku: JSON.parse(obj[k].getAttribute("sku"))
                                    };
                                    createOrEditProductVue.form.orderItemList.push(order)
                                }
                            }
                            createOrEditProductVue.newVal = createOrEditProductVue.form.orderItemList.length;
                            $modal.modal('hide');
                        },
                        query: function () {
                                var self=this;
                                self.form.allSupplierId=self.allSupplierId,
                                self.form.allStoreCode=self.allStoreCode,
                            this.$dataTable.bootstrapTable('selectPage', 1);
                        },
                        drawTable: function () {
                            var self = this;
                            self.$dataTable = $('#dataTableBrand', self._$el).bootstrapTable({
                                url: '/api/sku/adminSearch',
                                method: 'get',
                                dataType: 'json',
                                cache: false,
                                // 去缓存
                                pagination: true,
                                // 是否分页
                                sidePagination: 'server',
                                singleSelect: true,
                                // 服务端分页
                                queryParams: function (params) {
                                    // 将table 参数与搜索表单参数合并
                                    return _.extend({},
                                        params, self.form)
                                },
                                mobileResponsive: true,
                                undefinedText: '-',
                                // 空数据的默认显示字符
                                striped: true,
                                // 斑马线
                                maintainSelected: true,
                                // 维护checkbox选项
                                sortOrder: 'desc',
                                // 默认排序方式
                                columns: [
                                    {
                                        field: 'id',
                                        align: 'center',
                                        formatter: function (value, row, index) {
                                            var obj = JSON.stringify(row)
                                            var html = '<input type="checkbox" name="product" ' + "sku='" + obj + "' brandId='" + row.id + "'/>"
                                            return html;
                                        }
                                    },
                                    {
                                        field: 'code',
                                        title: 'sku编码',
                                        align: 'center',
                                        orderable: true
                                    },
                                    {
                                        field: 'name',
                                        title: 'sku名称',
                                        align: 'center',
                                        orderable: true
                                    },
                                    {
                                        field: 'attribute1',
                                        title: '属性值1',
                                        align: 'center',
                                        orderable: true
                                    },
                                    {
                                        field: 'attribute2',
                                        title: '属性值2',
                                        align: 'center',
                                        orderable: true
                                    },
                                    {
                                        field: 'attribute3',
                                        title: '属性值3',
                                        align: 'center',
                                        orderable: true
                                    },
                                    {
                                        field: 'stock',
                                        title: '库存量',
                                        align: 'center',
                                        orderable: true
                                    },
                                    {
                                        field: 'product',
                                        title: '型号',
                                        align: 'center',
                                        orderable: true,
                                        formatter:function (value, row, index) {
                                            var label='';
                                            if(value!=''&& value!=undefined){
                                                label=value.model;
                                            }
                                            return label;
                                        }
                                    },
                                    {
                                        field: 'product',
                                        title: '规格',
                                        align: 'center',
                                        orderable: true,
                                        formatter:function (value, row, index) {
                                            var label='';
                                            if(value!=''&& value!=undefined){
                                                label=value.spec;
                                            }
                                            return label;
                                        }
                                    }
                                ]
                            })
                            self.checkEventHandle('id');
                        }
                    },
                    watch: {
                        'allSupplierId': function (val) {
                            this.fetchSuppliers(val);
                        },
                        'allStoreCode': function (val) {
                            this.fetchAllSuppliers(val);
                        }
                    }
                });
            })
    }

    //选择复尺 结果列表
    function vendorReviewResults(type, index) {
        var $modal = $('#modalReviewResult').clone();
        $modal.modal({
            height: 500,
            maxHeight: 300
        });
        $modal.on('shown.bs.modal',
            function () {
                vueModal2 = new Vue({
                    el: $modal.get(0),
                    mixins: [RocoVueMixins.DataTableMixin],
                    data: {
                        contractId: '',
                        type: type,
                        $dataTable: null,
                        selectedRows: {},
                        // 选中列
                        modalModel: null,
                        // 模式窗体模型
                        _$el: null,
                        // 自己的 el $对象
                        catalogId:''
                    },
                    created: function () {
                        this.contractId = this.$parseQueryString()['contractId'];
                        this.contractCode = this.$parseQueryString()['contractCode'];
                    },
                    ready: function () {
                        this.getByCatalog();
                    },
                    methods: {
                        choose: function () {
                            var self = this;
                            var selections = self.$dataTable.bootstrapTable('getSelections')
                            var reviewSizeResult = "";
                            selections.forEach(function (obj) {
                                reviewSizeResult += obj.productName + " " + obj.location + " " + obj.model + " " + obj.specification + " " + obj.count + " " + obj.unit + " " + obj.remark ;
                            });
                            createOrEditProductVue.form.orderItemList[index].reviewSizeResult = reviewSizeResult;
                            $modal.modal('hide');
                        },
                        getByCatalog:function () {
                            var self = this;
                            console.log(createOrEditProductVue.form.orderItemList[index].sku.product.catalog.url);
                            self.$http.get('/api/catalog/getByUrl?catalogUrl=' + createOrEditProductVue.form.orderItemList[index].sku.product.catalog.url).then(function (res) {
                                self.catalogId = res.data.data.id;
                                this.drawTable();
                            });
                        },
                        drawTable: function () {
                            var self = this;
                            self.$dataTable = $('#dataTableReviewResult', self._$el).bootstrapTable({
                                url: '/api/reviewSizeResult/list?contractId=' + self.contractId + '&prodCatalogId=' + self.catalogId,
                                method: 'get',
                                dataType: 'json',
                                cache: false,
                                // 去缓存
                                pagination: false,
                                // 是否分页
                                sidePagination: 'server',
                                singleSelect: false,
                                mobileResponsive: true,
                                undefinedText: '-',
                                // 空数据的默认显示字符
                                striped: true,
                                // 斑马线
                                maintainSelected: true,
                                // 维护checkbox选项
                                sortOrder: 'desc',
                                // 默认排序方式
                                columns: [
                                    {
                                        checkbox: true,
                                        align: 'center',
                                        width: '5%'
                                    },
                                    {
                                        field: 'productName',
                                        title: '商品名称',
                                        width: '15%',
                                        align: 'center',
                                    },
                                    {
                                        field: 'location',
                                        title: '安装位置',
                                        width: '15%',
                                        align: 'center'
                                    },
                                    {
                                        field: 'model',
                                        title: '型号',
                                        width: '10%',
                                        align: 'center',
                                    },
                                    {
                                        field: 'specification',
                                        title: '规格',
                                        width: '10%',
                                        align: 'center'
                                    },
                                    {
                                        field: 'unit',
                                        title: '单位',
                                        width: '10%',
                                        align: 'center'
                                    }, {
                                        field: 'count',
                                        title: '数量',
                                        width: '15%',
                                        align: 'center'
                                    }, {
                                        field: 'remark',
                                        title: '备注',
                                        width: '15%',
                                        align: 'center'
                                    }
                                ]
                            });
                            console.log(self.$dataTable);
                            self.checkEventHandle('id');
                        }
                    }
                })
            })
    }
})()
