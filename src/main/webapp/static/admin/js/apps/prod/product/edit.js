var createOrEditProductVue;
+(function () {
    $('#applyManage').addClass('active');
    $('#addProductApply').addClass('active');
    UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
    UE.Editor.prototype.getActionUrl = function (action) {
        if (action == 'uploadimage' || action == 'uploadscrawl' || action == 'uploadimage') {
            return '/api/repository/Product/uploads/';
        } else if (action == 'uploadvideo') {
            return 'http://a.b.com/video.php';
        } else {
            return this._bkGetActionUrl.call(this, action);
        }
    }

    createOrEditProductVue = new Vue({
        el: '#container',
        components: {
            'web-uploader': DaMeiVueComponents.WebUploaderComponent
        },
        validators: { // `numeric` and `url` custom validator is local registration
            url: function (val) {
                if (!val) {
                    return true;
                } else {
                    return /^(http\:\/\/|https\:\/\/)(.{4,})$/.test(val)
                }
            },
            specvalidate: function (val) {
                if (createOrEditProductVue && createOrEditProductVue.catelog && createOrEditProductVue.catelog.convertUnit == 'square_meter_turn') {
                    return (/(\d+)\\*(\d+)\\*(\d+)\\*.(\d+)/.test(val) || (val == ''));
                }else {
                    return true;
                }
            }
        },
        data: {
            isShowSku: true,
            // 分类列表
            categories: null,
            // 品牌列表，显示品牌用
            brands: null,
            // 选中的品牌名称
            brandName: '',
            // 供应商列表
            suppliers: null,
            // 所有的区域供应商
            allSuppliers: null,
            //门店
            allStores: null,
            //计量单位
            measureList: null,
            // 编辑id
            productId: null,
            //禁用按钮
            submiting: false,
            // 主图预览
            showMainImgUrls: [],

            allOrganization: null,
            form: {
                // 商品主图
                mainImgUrl: '',
                subImgUrls: [],
                // 商品编码
                code: '',
                // 名称
                name: '',
                //供应商
                supplier: {
                    id: ''
                },
                // 门店
                allStore: {
                    code: ''
                },
                // 区域供应商
                allSupplier: {
                    id: ''
                },
                // 型号
                model: '',
                //规格
                spec: '',
                //计量单位
                measureUnit: {
                    id: ''
                },
                // 分类
                catalog: {
                    url: ''
                },
                declare: {
                    code: ''
                },
                // 品牌
                brand: {
                    id: ''
                },
                // 市场价
                marketPrice: '',
                //供货价
                supplyPrice: '',
                // sku描述
                skuDesc: '',
                //库存数
                stock: '',
                //推荐值,值越大越靠前,0表示不推荐
                recommend: '',
                //商品比价连接url,例如：连接到京东
                compareLinkUrl: '',
                // 详情
                detail: '',
                // 启用/停用状态
                status: '',
                productImages: [],
                skuMeta: {
                    attribute1Name: '',
                    attribute2Name: '',
                    attribute3Name: ''
                },
                hasSku: false, // 是否设置sku
            },
            ueditor: null,
            webUploaderMain: {
                sku: 'forSku',
                type: 'main',
                formData: {
                    type: 'PRODUCT'
                },
                accept: {
                    title: 'Images',
                    extendsions: 'gif,jpg,jpeg,bmp,png',
                    mimeTypes: 'image/*' //'image/jpg,image/png,image/gif,image/jpeg,image/bmp'
                },
                thumb: {
                    width: 40,
                    height: 40,
                    // 图片质量，只有type为`image/jpeg`的时候才有效。
                    quality: 70,
                    // 是否允许放大，如果想要生成小图的时候不失真，此选项应该设置为false.
                    allowMagnify: false,
                    // 是否允许裁剪。
                    crop: true,
                    // 为空的话则保留原有图片格式。
                    // 否则强制转换成指定的类型。
                    type: "image/jpeg"
                },
                server: ctx + '/api/upload',
                //上传图片路径
                fileNumLimit: 8,
                fileSizeLimit: 50000 * 1024,
                fileSingleSizeLimit: 5000 * 1024
            },

            attribute1: '',
            attribute2: '',
            attribute3: '',
            skus: [],
            showSkuBtn: true,
            skuId: '',
            catelog: '',
            specDisabled: true
        },
        methods: {
            setSku: function () {
                this.form.hasSku = !this.form.hasSku;
            },
            setPrice: function (id) {
                this.skuId = id;
                priceModal(id)
            },
            fetchCatelog: function () {
                var self = this;
                self.$http.get('/api/catalog/getByUrl?catalogUrl=' + self.form.catalog.url).then(function (res) {
                    if (res.data.code == 1) {
                        self.catelog = res.data.data;
                        self.specDisabled = false
                    }
                }).catch(function () {

                }).finally(function () {

                });
            },
            // 动态生成sku
            createSku: function () {
                if (this.attribute1 == '' && this.attribute2 == '' && this.attribute3 == '') {
                    return
                }
                this.showSkuBtn = !this.showSkuBtn
                var attr1 = [], attr2 = [], attr3 = [];
                this.attribute1 = this.attribute1.replace('，', ',')
                this.attribute2 = this.attribute2.replace('，', ',')
                this.attribute3 = this.attribute3.replace('，', ',')
                var attr1 = this.attribute1.split(',');
                var attr2 = this.attribute2.split(',');
                var attr3 = this.attribute3.split(',');
                var arr = new Array(attr3, attr2, attr1)

                function combine(arr) {
                    var r = [];
                    (function f(t, a, n) {
                        if (n == 0) {
                            return r.push(t);
                        }
                        for (var i = 0; i < a[n - 1].length; i++) {
                            f(t.concat(a[n - 1][i]), a, n - 1);
                        }
                    })([], arr, arr.length);
                    return r;
                }

                var res = combine(arr);
                for (var i = 0; i < res.length; i++) {
                    var obj = {
                        code: '',
                        stock: '',
                        supplyPrice: '',
                        storePrice: '',
                        salePrice: '',
                        status: "OPEN",
                        attribute1: res[i][0],
                        attribute2: res[i][1],
                        attribute3: res[i][2],
                        productImages: []
                    }
                    this.skus.push(obj)
                }
            },
            clear: function () {
                this.skus = []
                this.showSkuBtn = !this.showSkuBtn
            },
            closeWin: function () {
                window.history.go(-1);
            },
            //获取品牌列表
            fetchBrand: function () {
                var self = this;
                self.$http.get('/api/brand/list').then(function (res) {
                    if (res.data.code == 1) {
                        self.brands = res.data.data.rows;
                        if (self.productId) {
                            self.fetchProductDetail();
                        }
                    }
                }).catch(function () {

                }).finally(function () {

                });
            },
            // 保存提交
            saveProduct: function (flag) {
                var self = this;
                self.form.detail = self.ueditor.getContent();
                if (self.form.detail && self.form.detail.length > 3000) {
                    Vue.toastr.error('商品详情不能超过3000字符');
                    return;
                }
                self.$validate(true)
                if (!self.$validation.valid) {
                    return false
                }

                if (self.skus.length < 1) {
                    Vue.toastr.error('至少需要生成一个SKU！！！');
                    return false;
                }

                self.submiting = true;
                var formData = {
                    'name': self.form.name,
                    'model': self.form.model,
                    'spec': self.form.spec,
                    'measureUnit': {
                        id: self.form.measureUnit.id,
                    },
                    'supplier': {
                        id: self.form.supplier.id
                    },
                    'catalog': {
                        url: self.form.catalog.url
                    },
                    'brand': {
                        id: self.form.brand.id
                    },
                    'detail': self.form.detail,
                    'skus': self.skus,
                    'hasSku': self.form.hasSku,
                    productImages: self.form.productImages,
                    skuMeta: self.form.skuMeta,
                }
                if (self.form.hasSku == false) {
                    delete formData.skuMeta;
                    delete formData.skus;
                }
                if (self.productId) {
                    formData['id'] = self.productId
                }

                self.$http.post('/api/product/edit', formData).then(function (res) {
                    self.submiting = false;
                    if (res.data.code == '1') {
                        Vue.toastr.success(res.data.message);
                        setTimeout(function () {
                            window.location.href = '/admin/product';
                        }, 500)

                    } else {
                        Vue.toastr.error(res.data.message);
                    }
                })
            },
            // 分类列表
            fetchCategory: function () {
                var self = this;
                self.$http.get('/api/catalog/findCatalogList').then(function (res) {
                    if (res.data.code == 1) {
                        self.categories = res.data.data;
                    }
                }).catch(function () {

                }).finally(function () {

                });
            },
            // 计量单位
            fetchMeasureUnit: function () {
                var self = this;
                self.$http.get('/api/dict/measure/all').then(function (res) {
                    if (res.data.code == 1) {
                        self.measureList = res.data.data;
                    }
                }).catch(function () {

                }).finally(function () {

                });
            },
            //供应商列表
            fetchSuppliers: function (id) {
                var self = this;
                self.$http.get('/api/supplier/filterByRegionIdAndStatus?status=OPEN&regionSupplierId=' + id).then(function (res) {
                    if (res.data.code == 1) {
                        self.suppliers = res.data.data;
                        if (self.form.supplier.id == '') {
                            self.form.supplier.id = self.suppliers[0].id;
                        }
                    }
                }).catch(function () {

                }).finally(function () {

                });
            },
            // 获取全部的区域供应商
            fetchAllSuppliers: function (val) {
                var self = this;
                self.$http.get('/api/regionSupplier/filterByStoreCode?storeCode=' + val).then(function (res) {
                    if (res.data.code == 1) {
                        self.allSuppliers = res.data.data;
                        if (self.form.allSupplier.id == '') {
                            self.form.allSupplier.id = self.allSuppliers[0].id;
                        }
                    }
                }).catch(function () {

                }).finally(function () {

                });
            },

            fetchAllStores: function () {
                var self = this;
                self.$http.get('/api/product/store/all').then(function (res) {
                    if (res.data.code == 1) {
                        self.allStores = res.data.data;
                        if (self.form.allStore.code == '') {
                            self.form.allStore.code = self.allStores[0].code;
                        }
                    }
                }).catch(function () {

                }).finally(function () {

                });
            },
            // 获取商品信息
            fetchProductDetail: function () {
                var self = this;
                self.isShowSku = false;
                self.$http.get('/api/product/detail/' + self.productId).then(function (res) {
                    if (res.data.code == 1) {
                        self.form = $.extend({}, self.form, res.data.data);
                        self.$nextTick(function () {
                            // self.$activateValidator()
                            self.buildUeditor()
                            // self.$resetValidation()
                            // self.$validate(true)
                        })
                        self.skus = res.data.data.skus || [];
                        console.log(res.data.data.supplier);
                        self.form.allSupplier.id = res.data.data.supplier.regionSupplier.id;
                        self.form.allStore.code = res.data.data.supplier.regionSupplier.store.code;
                        // 获取所有品牌然后通过id获取name
                        self.fetchAllSuppliers(self.form.allStore.code);

                        Array.prototype.unique3 = function () {
                            var res = [];
                            var json = {};
                            for (var i = 0; i < this.length; i++) {
                                if (!json[this[i]]) {
                                    res.push(this[i]);
                                    json[this[i]] = 1;
                                }
                            }
                            return res;
                        }

                        var attr1 = [], attr2 = [], attr3 = [];
                        if (self.skus.length != 0) {
                            this.showSkuBtn = false
                            self.skus.forEach(function (element) {
                                attr1.push(element.attribute1);
                                attr2.push(element.attribute2);
                                attr3.push(element.attribute3);
                            });
                        }

                        self.attribute1 = attr1.unique3().join(",")
                        self.attribute2 = attr2.unique3().join(",")
                        self.attribute3 = attr3.unique3().join(",")

                        if (self.form.loadableMainImgUrl) {
                            self.showMainImgUrls.push(self.form.loadableMainImgUrl);
                        }
                        self.$nextTick(function () {
                            // self.$activateValidator()
                            self.buildUeditor()
                            // self.$resetValidation()
                            // self.$validate(true)
                        })
                        //后台不需要这2个值
                        // delete self.form.loadableMainImgUrl;
                        // delete self.form.loadableSubImgUrls;
                        var ilength = self.brands.length;
                        for (var i = 0; i < ilength; i++) {
                            if (self.form.brand.id === self.brands[i].id) {
                                self.brandName = self.brands[i].brandName;
                                break;
                            }
                        }


                    }
                }).catch(function () {

                }).finally(function () {

                });
            },
            // ueditor 初始化
            buildUeditor: function () {
                var self = this;
                self.ueditor = UE.getEditor(self.$els.ueditor, {
                    toolbars: [
                        [
                            // 'anchor', //锚点
                            'undo', //撤销
                            'redo', //重做
                            'bold', //加粗
                            'indent', //首行缩进
                            // 'snapscreen', //截图
                            'italic', //斜体
                            'underline', //下划线
                            'strikethrough', //删除线
                            // 'subscript', //下标
                            // 'fontborder', //字符边框
                            // 'superscript', //上标
                            'formatmatch', //格式刷
                            'source', //源代码
                            'blockquote', //引用
                            'pasteplain', //纯文本粘贴模式
                            'selectall', //全选
                            // 'print', //打印
                            // 'preview', //预览
                            // 'horizontal', //分隔线
                            // 'removeformat', //清除格式
                            'time', //时间
                            'date', //日期
                            'unlink', //取消链接
                            // 'insertrow', //前插入行
                            // 'insertcol', //前插入列
                            // 'mergeright', //右合并单元格
                            // 'mergedown', //下合并单元格
                            // 'deleterow', //删除行
                            // 'deletecol', //删除列
                            // 'splittorows', //拆分成行
                            // 'splittocols', //拆分成列
                            // 'splittocells', //完全拆分单元格
                            // 'deletecaption', //删除表格标题
                            // 'inserttitle', //插入标题
                            // 'mergecells', //合并多个单元格
                            // 'deletetable', //删除表格
                            // 'cleardoc', //清空文档
                            // 'insertparagraphbeforetable', //"表格前插入行"
                            // 'insertcode', //代码语言
                            'fontfamily', //字体
                            'fontsize', //字号
                            // 'paragraph', //段落格式
                            // 'simpleupload', //单图上传
                            // 'insertimage', //多图上传
                            // 'edittable', //表格属性
                            // 'edittd', //单元格属性
                            'link', //超链接
                            'emotion', //表情
                            'spechars', //特殊字符
                            'searchreplace', //查询替换
                            'map', //Baidu地图
                            'gmap', //Google地图
                            'insertvideo', //视频
                            'help', //帮助
                            'justifyleft', //居左对齐
                            'justifyright', //居右对齐
                            'justifycenter', //居中对齐
                            'justifyjustify', //两端对齐
                            'forecolor', //字体颜色
                            'backcolor', //背景色
                            // 'insertorderedlist', //有序列表
                            // 'insertunorderedlist', //无序列表
                            // 'fullscreen', //全屏
                            // 'directionalityltr', //从左向右输入
                            // 'directionalityrtl', //从右向左输入
                            // 'rowspacingtop', //段前距
                            // 'rowspacingbottom', //段后距
                            // 'pagebreak', //分页
                            // 'insertframe', //插入Iframe
                            // 'imagenone', //默认
                            // 'imageleft', //左浮动
                            // 'imageright', //右浮动
                            // 'attachment', //附件
                            // 'imagecenter', //居中
                            // 'wordimage', //图片转存
                            // 'lineheight', //行间距
                            // 'edittip ', //编辑提示
                            // 'customstyle', //自定义标题
                            // 'autotypeset', //自动排版
                            // 'webapp', //百度应用
                            // 'touppercase', //字母大写
                            // 'tolowercase', //字母小写
                            // 'background', //背景
                            // 'template', //模板
                            // 'scrawl', //涂鸦
                            // 'music', //音乐
                            // 'inserttable', //插入表格
                            // 'drafts', // 从草稿箱加载
                            // 'charts', // 图表
                        ]
                    ],
                    height: 465,
                    autoHeightEnabled: false
                });
                self.ueditor.on('ready',
                    function () {
                        if (self.form.detail) {
                            self.ueditor.setContent(self.form.detail);
                        }
                    });
            },
            // 选择品牌或者批次号
            selectBrands: function (type) {
                vendorBrands(type);
            },
            // 删除图片
            deleteImg: function (url, index, type) {
                var self = this;

                self.$http.get('/api/upload/delete', {
                    params: {
                        path: url
                    }
                }).then(function (res) {
                    if (res.data.code == 1) {
                        self.form.productImages.splice(index, 1)

                    }
                }).finally(function () {
                })
            },
            deleteSkuImg: function (url, index, ind) {
                var self = this;

                self.$http.get('/api/upload/delete', {
                    params: {
                        path: url
                    }
                }).then(function (res) {
                    if (res.data.code == 1) {
                        this.skus[index].productImages.splice(ind, 1);

                    }
                }).finally(function () {
                })
            },
            // 自动填充sku信息
            skuChange: function (val, type) {
                this.skus.forEach(function (obj) {
                    if (obj[type] == '') {
                        obj[type] = val
                    }
                })
            }
        },

        created: function () {

            var self = this;
            this.productId = this.$parseQueryString()['productId'];
            // 分类列表
            this.fetchCategory();
            //获取全部门店
            this.fetchAllStores();
            // 计量单位
            this.fetchMeasureUnit()
            this.fetchSuppliers(self.form.allSupplier.id);
            // 获取全部的区域供应商
            this.fetchAllSuppliers(self.form.allStore.code);
            this.fetchBrand();
        },
        ready: function () {
            if (!this.productId) {
                this.$nextTick(function () {
                    this.buildUeditor()
                })
            }

        },
        watch: {
            "form.hasSku": function (val) {
                if (val == false) {

                }
            },
            "form.allSupplier.id": function (val) {
                this.fetchSuppliers(val)
            },
            "form.allStore.code": function (val) {
                this.fetchAllSuppliers(val);
            },
            'form.catalog.url': function (val) {
                var self = this;
                if (val) {
                    self.fetchCatelog();
                }
            },

        },
        // 图片上传成功
        events: {
            'webupload-upload-success-main': function (file, res) {
                if (res.code == 1) {
                    this.$toastr.success('上传成功');

                    var obj = {
                        imagePath: res.data.path,
                        fullPath: res.data.fullPath
                    }
                    if (this.form.productImages == undefined || this.form.productImages == '') {
                        Vue.set(this.skus, 'productImages', [])
                    }
                    this.form.productImages.push(obj);

                } else {
                    this.$toastr.error(res.message);
                }
            },
            'webupload-upload-success-forSku': function (ind, res) {
                if (res.code == 1) {
                    var obj = {
                        imagePath: res.data.path,
                        fullPath: res.data.fullPath
                    }
                    if (this.skus[ind].productImages == undefined || this.skus[ind].productImages == '') {
                        Vue.set(this.skus[ind], 'productImages', [])
                    }
                    this.skus[ind].productImages.push(obj)
                    this.$toastr.success('上传成功');
                } else {
                    this.$toastr.error(res.message);
                }
            }
        }
    });
    //品牌||批次号弹窗 type = true 品牌 false 批次号
    function vendorBrands(type) {
        var getUrl, arr;
        if (type) {
            getUrl = '/api/brand/list?status=' + "OPEN" + '';
            arr = [{
                checkbox: true,
                align: 'center',
                width: '5%'
            },
                {
                    field: 'code',
                    title: '品牌编码',
                    width: '10%',
                    orderable: true
                },
                {
                    field: 'brandName',
                    title: '中文名称',
                    width: '10%',
                    orderable: true
                },
                {
                    field: 'pinyinInitial',
                    title: '中文首字母',
                    width: '10%',
                    orderable: true
                }
            ]
        } else {
            arr = [{
                checkbox: true,
                align: 'center',
                width: '5%'
            },
                {
                    field: 'code',
                    title: '申请批次号',
                    width: '15%',
                    orderable: true
                },
                {
                    field: 'org',
                    title: '申报机构',
                    width: '10%',
                    orderable: false,
                    formatter: function (value, row, index) {
                        //遍历机构集合,匹配当前orgId,取出名字
                        if (createOrEditProductVue.allOrganization != null && createOrEditProductVue.allOrganization != undefined) {
                            for (var i = 0; i < createOrEditProductVue.allOrganization.length; i++) {
                                if (createOrEditProductVue.allOrganization[i].id == value.id) {
                                    return createOrEditProductVue.allOrganization[i].name;
                                }
                            }
                        }
                    }
                },
                {
                    field: 'declareDate',
                    title: '提报日期',
                    width: '10%',
                    orderable: false
                },
                {
                    field: 'user.name',
                    title: '申请人',
                    width: '10%',
                    orderable: false
                }
            ]
            getUrl = '/api/apply/declaration/declarationFormList'
        }
        var $modal = $('#modalBrand').clone();
        $modal.modal({
            height: 480,
            maxHeight: 500
        });
        $modal.on('shown.bs.modal',
            function () {
                vueModal2 = new Vue({
                    el: $modal.get(0),
                    mixins: [DaMeiVueMixins.DataTableMixin],
                    data: {
                        form: {
                            keyword: '',
                            declareCode: null,
                            orgId: null,
                            declareStartDate: null,
                            declareEndDate: null
                        },
                        //机构组织
                        orgnizations: createOrEditProductVue.allOrganization,
                        type: type,
                        $dataTable: null,
                        selectedRows: {},
                        //选中列
                        modalModel: null,
                        //模式窗体模型
                        _$el: null,
                        //自己的 el $对象
                    },
                    created: function () {
                    },
                    ready: function () {
                        this.drawTable();
                        if (!type) {
                            this.activeDatepicker()
                        }
                    },
                    methods: {
                        choose: function () {

                            for (id in this.selectedRows) {
                                if (type) {
                                    createOrEditProductVue.form.brand.id = id;
                                    createOrEditProductVue.brandName = this.selectedRows[id].brandName;
                                } else {
                                    createOrEditProductVue.form.declare.code = this.selectedRows[id].code;
                                }
                            }

                            $modal.modal('hide');
                        },
                        // 日历初始化
                        activeDatepicker: function () {
                            var self = this;
                            $('#declareStartDate', self._$el).datetimepicker({});
                            $('#declareEndDate', self._$el).datetimepicker({});
                        },
                        query: function () {
                            this.$dataTable.bootstrapTable('selectPage', 1);
                        },
                        drawTable: function () {
                            var self = this;
                            self.$dataTable = $('#dataTableBrand', self._$el).bootstrapTable({
                                url: getUrl,
                                method: 'get',
                                dataType: 'json',
                                cache: false,
                                //去缓存
                                pagination: true,
                                //是否分页
                                sidePagination: 'server',
                                singleSelect: true,
                                //服务端分页
                                queryParams: function (params) {
                                    // 将table 参数与搜索表单参数合并
                                    return _.extend({},
                                        params, self.form);
                                },
                                mobileResponsive: true,
                                undefinedText: '-',
                                //空数据的默认显示字符
                                striped: true,
                                //斑马线
                                maintainSelected: true,
                                //维护checkbox选项
                                sortOrder: 'desc',
                                //默认排序方式
                                columns: arr,
                                onLoadSuccess: function () {
                                    var array = [];
                                    if (type) {
                                        if (createOrEditProductVue.form.brand && createOrEditProductVue.form.brand.id != '') {
                                            array.push(parseInt(createOrEditProductVue.form.brand.id, 10));
                                        }
                                        self.$dataTable.bootstrapTable('checkBy', {
                                            field: 'id',
                                            values: array
                                        });
                                    } else {
                                        if (createOrEditProductVue.form.declare && createOrEditProductVue.form.declare.code != '') {
                                            array.push(createOrEditProductVue.form.declare.code);
                                        }
                                        self.$dataTable.bootstrapTable('checkBy', {
                                            field: 'code',
                                            values: array
                                        });
                                    }


                                }
                            });
                            self.checkEventHandle('id');
                        }
                    }
                });
            });
    }

    // 设置价格
    function priceModal(id) {
        var $modal = $('#priceModal').clone();
        $modal.modal({
            height: 600,
            maxHeight: 500,
            maxHeight: 600,
            width: 600,
        });
        $modal.on('shown.bs.modal',
            function () {
                vueModal2 = new Vue({
                    el: $modal.get(0),
                    mixins: [DaMeiVueMixins.DataTableMixin],
                    data: {
                        form: {
                            keyword: '',
                            declareCode: null,
                            orgId: null,
                            declareStartDate: null,
                            declareEndDate: null
                        },
                        //机构组织
                        orgnizations: createOrEditProductVue.allOrganization,
                        id: id,
                        current: 'aConfig', // 当前tab
                        aKeyword: '', // a关键字
                        bKeyword: '', // b关键字
                        cKeyword: '', // c关键字
                        $aDataTable: null, // A表格
                        $bDataTable: null, // B表格
                        $cDataTable: null, // C表格
                        $dataTable: null,
                        selectedRows: {},
                        //选中列
                        modalModel: null,
                        //模式窗体模型
                        _$el: null,
                        aControl: 0, // 是否可以新增 0 不可以 1 可以
                        bControl: 0, // 是否可以新增 0 不可以 1 可以
                        cControl: 0, // 是否可以新增 0 不可以 1 可以
                        aShow: 0, // 是否可以显示tab 0 不可以 1 可以
                        bShow: 0, // 是否可以显示tab 0 不可以 1 可以
                        cShow: 0, // 是否可以显示tab 0 不可以 1 可以
                        //自己的 el $对象
                    },
                    created: function () {

                    },
                    ready: function () {
                        // 0 表示只能查看 1 表示可以查看、新增、编辑
                        switch (acctType) {
                            // 管理员
                            case 'ADMIN':
                                this.aTable(1);
                                this.bTable(1);
                                this.cTable(1);
                                this.aControl = 1;
                                this.bControl = 1;
                                this.cControl = 1;
                                this.aShow = 1;
                                this.bShow = 1;
                                this.cShow = 1;
                                break;
                            // 区域
                            case 'REGION_SUPPLIER':
                                this.aTable(0);
                                this.bTable(1);
                                this.aControl = 0;
                                this.bControl = 1;
                                this.cControl = 0;
                                this.aShow = 1;
                                this.bShow = 1;
                                this.cShow = 0;
                                break;
                            // 商品
                            case 'PROD_SUPPLIER':
                                this.aTable(1);
                                this.aControl = 1;
                                this.bControl = 0;
                                this.cControl = 0;
                                this.aShow = 1;
                                this.bShow = 0;
                                this.cShow = 0;
                                break;
                            // 门店
                            case 'STORE':
                                this.current = 'bConfig'
                                this.bTable(0);
                                this.cTable(1);
                                this.aControl = 0;
                                this.bControl = 0;
                                this.cControl = 1;
                                this.aShow = 0;
                                this.bShow = 1;
                                this.cShow = 1;
                                break;
                        }
                    },
                    methods: {
                        createBtnClickHandler: function (priceType) {
                            console.log(9)
                            createOrEditModal({priceType: priceType}, false);
                        },
                        // tab切换
                        tab: function (config) {
                            this.current = config
                        },
                        // A表格
                        aTable: function (control) {
                            var self = this
                            self.$aDataTable = $('#aTable', self._$el).bootstrapTable({
                                url: '/api/product/price/list',
                                method: 'get',
                                dataType: 'json',
                                cache: false,
                                pagination: true,
                                sidePagination: 'server',
                                queryParams: function (params) {
                                    return _.extend({}, params, {priceType: 'SUPPLY', skuId: id})
                                },
                                mobileResponsive: true,
                                undefinedText: '',
                                striped: true,
                                maintainSelected: true,
                                sortOrder: 'ASC',
                                columns: [
                                    {
                                        field: 'priceStartDate',
                                        title: '时间',
                                        align: 'center'
                                    },
                                    {
                                        field: 'price',
                                        title: '价格',
                                        align: 'center',
                                        formatter: function (value) {

                                            return "¥" + value;
                                        }
                                    },
                                    {
                                        field: 'editor',
                                        title: '操作人',
                                        align: 'center',
                                        formatter: function (value) {
                                            return value.name
                                        }
                                    },
                                    {
                                        field: 'editTime',
                                        title: '操作时间',
                                        align: 'center'
                                    },
                                    {
                                        field: 'id',
                                        title: '操作',
                                        className: 'td_center',
                                        orderable: false,
                                        align: 'center',
                                        formatter: function (value, row, index) {
                                            var html = ''
                                            if (row.editable == true && control == '1') {
                                                html += '<button style="margin-left:10px;"'
                                                html += 'data-handle="aEdit"'
                                                html += 'data-priceStartDate="' + row.priceStartDate + '"'
                                                html += 'data-price="' + row.price + '"';
                                                html += 'data-priceType="' + row.priceType + '"'
                                                html += 'data-id="' + row.id + '"';
                                                html += 'class="m-r-xs btn btn-xs btn-primary" type="button">编辑</button>'
                                                html += '<button style="margin-left:10px;" data-handle="delete" data-id="' + value + '" class="m-r-xs btn btn-xs btn-primary" type="button">删除</button>'
                                            }
                                            return html
                                        }
                                    }]
                            })
                            // 编辑
                            self.$aDataTable.on('click', '[data-handle="aEdit"]',
                                function (e) {
                                    var model = $(this).data()
                                    model.priceStartDate = model.pricestartdate
                                    model.priceType = model.pricetype
                                    createOrEditModal(model, true);
                                    e.stopPropagation()
                                })

                            // 删除
                            self.$aDataTable.on('click', '[data-handle="delete"]',
                                function (e) {
                                    var id = $(this).data('id')
                                    swal({
                                            title: '',
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
                                            vueModal2.$http.post('/api/product/price/' + id + '/del').then(function (res) {
                                                if (res.data.code == 1) {
                                                    Vue.toastr.success(res.data.message)
                                                    self.$aDataTable.bootstrapTable('refresh')
                                                }
                                            }).catch(function () {
                                            }).finally(function () {
                                                swal.close()
                                            })
                                        })
                                    e.stopPropagation()
                                })
                        },
                        // B表格
                        bTable: function (control) {
                            var self = this
                            self.$bDataTable = $('#bTable', self._$el).bootstrapTable({
                                url: '/api/product/price/list',
                                method: 'get',
                                dataType: 'json',
                                cache: false,
                                pagination: true,
                                sidePagination: 'server',
                                queryParams: function (params) {
                                    return _.extend({}, params, {priceType: 'STORE', skuId: id})
                                },
                                mobileResponsive: true,
                                undefinedText: '',
                                striped: true,
                                maintainSelected: true,
                                sortOrder: 'ASC',
                                columns: [
                                    {
                                        field: 'priceStartDate',
                                        title: '时间',
                                        align: 'center'
                                    },
                                    {
                                        field: 'price',
                                        title: '价格',
                                        align: 'center',
                                        formatter: function (value) {

                                            return "¥" + value;
                                        }
                                    },
                                    {
                                        field: 'editor',
                                        title: '操作人',
                                        align: 'center',
                                        formatter: function (value) {
                                            return value.name
                                        }
                                    },
                                    {
                                        field: 'editTime',
                                        title: '操作时间',
                                        align: 'center'
                                    },
                                    {
                                        field: 'id',
                                        title: '操作',
                                        className: 'td_center',
                                        orderable: false,
                                        align: 'center',
                                        formatter: function (value, row, index) {
                                            var html = ''
                                            if (row.editable == true && control == '1') {
                                                html += '<button style="margin-left:10px;"'
                                                html += 'data-handle="bEdit"'
                                                html += 'data-priceStartDate="' + row.priceStartDate + '"'
                                                html += 'data-price="' + row.price + '"';
                                                html += 'data-priceType="' + row.priceType + '"'
                                                html += 'data-id="' + row.id + '"';
                                                html += 'class="m-r-xs btn btn-xs btn-primary" type="button">编辑</button>'
                                                html += '<button style="margin-left:10px;" data-handle="delete" data-id="' + value + '" class="m-r-xs btn btn-xs btn-primary" type="button">删除</button>'
                                            }
                                            return html
                                        }
                                    }]
                            })
                            // 编辑
                            self.$bDataTable.on('click', '[data-handle="bEdit"]',
                                function (e) {
                                    var model = $(this).data()
                                    model.priceStartDate = model.pricestartdate
                                    model.priceType = model.pricetype
                                    createOrEditModal(model, true)
                                    e.stopPropagation()
                                })

                            // 删除
                            self.$bDataTable.on('click', '[data-handle="delete"]',
                                function (e) {
                                    var id = $(this).data('id')
                                    swal({
                                            title: '',
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
                                            vueModal2.$http.post('/api/product/price/' + id + '/del').then(function (res) {
                                                if (res.data.code == 1) {
                                                    Vue.toastr.success(res.data.message)
                                                    self.$bDataTable.bootstrapTable('selectPage', 1)
                                                }
                                            }).catch(function () {
                                            }).finally(function () {
                                                swal.close()
                                            })
                                        })
                                    e.stopPropagation()
                                })
                        },
                        // C表格
                        cTable: function (control) {
                            var self = this
                            self.$cDataTable = $('#cTable', self._$el).bootstrapTable({
                                url: '/api/product/price/list',
                                method: 'get',
                                dataType: 'json',
                                cache: false,
                                pagination: true,
                                sidePagination: 'server',
                                queryParams: function (params) {
                                    return _.extend({}, params, {priceType: 'SALE', skuId: id})
                                },
                                mobileResponsive: true,
                                undefinedText: '',
                                striped: true,
                                maintainSelected: true,
                                sortOrder: 'ASC',
                                columns: [
                                    {
                                        field: 'priceStartDate',
                                        title: '时间',
                                        align: 'center'
                                    },
                                    {
                                        field: 'price',
                                        title: '价格',
                                        align: 'center',
                                        formatter: function (value) {

                                            return "¥" + value;
                                        }
                                    },
                                    {
                                        field: 'editor',
                                        title: '操作人',
                                        align: 'center',
                                        formatter: function (value) {
                                            return value.name
                                        }
                                    },
                                    {
                                        field: 'editTime',
                                        title: '操作时间',
                                        align: 'center'
                                    },
                                    {
                                        field: 'id',
                                        title: '操作',
                                        className: 'td_center',
                                        orderable: false,
                                        align: 'center',
                                        formatter: function (value, row, index) {
                                            var html = ''
                                            if (row.editable == true && control == '1') {
                                                html += '<button style="margin-left:10px;"'
                                                html += 'data-handle="cEdit"'
                                                html += 'data-priceStartDate="' + row.priceStartDate + '"'
                                                html += 'data-price="' + row.price + '"';
                                                html += 'data-id="' + row.id + '"';
                                                html += 'data-priceType="' + row.priceType + '"'
                                                html += 'class="m-r-xs btn btn-xs btn-primary" type="button">编辑</button>'
                                                html += '<button style="margin-left:10px;" data-handle="delete" data-id="' + value + '" class="m-r-xs btn btn-xs btn-primary" type="button">删除</button>'
                                            }
                                            return html
                                        }
                                    }]
                            })
                            // 编辑
                            self.$cDataTable.on('click', '[data-handle="cEdit"]',
                                function (e) {
                                    var model = $(this).data()
                                    model.priceStartDate = model.pricestartdate
                                    model.priceType = model.pricetype
                                    createOrEditModal(model, true)
                                    e.stopPropagation()
                                })

                            // 删除
                            self.$cDataTable.on('click', '[data-handle="delete"]',
                                function (e) {
                                    var id = $(this).data('id')
                                    swal({
                                            title: '',
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
                                            vueModal2.$http.post('/api/product/price/' + id + '/del').then(function (res) {
                                                if (res.data.code == 1) {
                                                    Vue.toastr.success(res.data.message)
                                                    self.$cDataTable.bootstrapTable('selectPage', 1)
                                                }
                                            }).catch(function () {
                                            }).finally(function () {
                                                swal.close()
                                            })
                                        })
                                    e.stopPropagation()
                                })
                        },
                    }
                });
            });
    }


    //  新增／编辑价格
    function createOrEditModal(model, isEdit) {
        var _modal = $('#contractModal').clone();
        var $el = _modal.modal({
            maxHeight: 600,
            width: 400,
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
                    $modal: $el,
                    data: {
                        id: model.id,
                        priceStartDate: model.priceStartDate,
                        price: model.price,
                        skuId: createOrEditProductVue.skuId,
                        priceType: model.priceType,
                        type: '价格'
                    },
                    ready: function () {
                        $('#timepick', this._$el).datetimepicker({
                            startDate: new Date()
                        });
                    },
                    created: function () {
                        switch (this.priceType) {
                            case "SUPPLY":
                                this.type = "供货价";
                                break;
                            case "STORE":
                                this.type = "门店价";
                                break;
                            case "SALE":
                                this.type = "销售价";
                                break;
                        }
                    },
                    methods: {
                        savePrice: function () {
                            var self = this;
                            console.log(0)
                            self.$validate(true,
                                function () {
                                    if (self.$validation.valid) {
                                        self.submitting = true;
                                        delete self._data.type;
                                        self.$http.post('/api/product/price/saveOrUpdate', $.param(self._data)).then(function (res) {
                                                if (res.data.code === '1') {
                                                    Vue.toastr.success(res.data.message);
                                                    $el.modal('hide');
                                                    if (vueModal2.current == 'aConfig') {
                                                        vueModal2.$aDataTable.bootstrapTable('refresh')
                                                    } else if (vueModal2.current == 'bConfig') {
                                                        vueModal2.$bDataTable.bootstrapTable('refresh')
                                                    } else {
                                                        vueModal2.$cDataTable.bootstrapTable('refresh')
                                                    }
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