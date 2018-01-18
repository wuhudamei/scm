(function(window, document, $){
    function List(b) {
        // 元素
        this.tip = $('[data-list="tip"]'); // 显示搜索内容
        this.price = $('[data-list="price"]'); // 价格区间
        this.priceSort = $('[data-list="pricesort"]'); // 价格排序
        this.proList = $('[data-list="prolist"]'); // 商品列表
        this.template = Hogan.compile($('#listtemplate').html()); // Hogan模板
        this.$choosePrice = $('[data-list="chooseprice"]'); // 选择价格
        this.$chooseInterval = $('[data-list="chooseinterval"]'); // 选择价格区间
        this.$fakeAttention = $('[data-list="fakeattention"]'); // 关注度
        this.$marketPrice = $('[data-list="market_price"]'); // 价格
        this.$noProTip = $('[data-list="noprotip"]'); // 没有查询到商品的时候的提示语
        this.$filterBar = $('[data-list="filterbar"]'); // 过滤条
        // 数据
        this.loadingTemp = null;
        this.bottom = null;

        this.keyword = ''; // 关键字查找
        this.catalogId = ''; // 分类ID
        this.brandIds = []; // 品牌列表

        this.startPrice = 0, // 开始价格
        this.endPrice = 999999999, // 结束价格
        this.orderColumn = 'fake_attention'; // 排序列 关注度： fake_attention 价格:market_price
        this.orderSort = 'DESC'; // 价格排序 升：ASC 降：DESC


        this.timer = null; // 滚动加载计数

        this.offset = 0; // 当前偏移量
        this.limit = 8; // 每页数据条数
        this.total = 1; // 总条数

        this.isReLoad = true; // 是否重新加载整个页面的数据

        this.Brand = b;

        // 加载推荐数据使用
        this.pageIndex = 0;
        this.pageSize = 8;
        this.pageTotal = 1;
        this.initWishData = [];

        this.isRecommending = false; // 是否正在推荐

        this.init();
    }

    // 初始化工作
    List.prototype.init = function() {
        var self = this;
        self.loadingTemp = self.template.render({loading:true});
        self.bottom = self.template.render({bottom:true});

        self.handerQuery();
        self.loading();
        self.event();
    }

    // 处理查询参数 及回显状态
    List.prototype.handerQuery = function() {
        var self = this;
        var query = parseQueryString();

        self.keyword = query.keyword;
        self.catalogId = query.cata;
        self.brandIds = (query.brands && query.brands.split(',')) || null;

        // 搜索关键字
        if(self.keyword) {
            self.tip.html(self.keyword);
            self.Brand.setSearchText(self.keyword);
        }

        if(self.brandIds) {
            // 提示
            self.tip.html('品牌');
            self.Brand.init(self.brandIds);
        }
        else {
            self.Brand.init();
        }

        self.initWishData = self.Brand.getFromCookie()

        if(self.catalogId) {
            self.Brand.setCataCurrent(self.catalogId);

            // 提示
            self.tip.html(self.Brand.__CATA[self.catalogId + ''].name)
        }

        // 加载 数据
        self.fetchData();
    }

    // 获取数据
    List.prototype.fetchData = function() {
        var self = this;
        $.ajax({
            url:ctx + '/api/repository/Product/weblist',
            dataType:'json',
            data: {
                keyword: self.keyword, // 关键字
                catalogId: self.catalogId, // 分类ID
                brandIds: self.brandIds, // 品牌列表
                startPrice: self.startPrice, // 开始价格
                endPrice: self.endPrice, // 结束价格
                offset: self.offset, // 偏移量
                limit: self.limit, // 限制量
                orderColumn: self.orderColumn, // 排序列
                orderSort: self.orderSort // 升降序
            },
            beforeSend:function(res) {
                self.proList.find('[data-list="loading"]').remove();
                self.proList.append(self.loadingTemp);
            },
            success:function(res) {
                if(res.code == 1) {
                    // 取得总条数
                    self.total = res.data.total;

                    var index = 0;
                    var _html = self.template.render({
                        list:res.data.rows,
                        isWish:function() {
                            return self.Brand.isWishById(this.id) ? 'icon-follow' : 'icon-unfollow'
                        },
                        isFour:function() {
                            ++index;
                            return index % 4 == 0 ? 'last':''
                        }
                    })

                    var appendHtml = $(_html);

                    // 删除加载显示
                    self.proList.find('[data-list="loading"]').remove();

                    // 处理图片延迟加载
                    self.Brand.imgLazy(appendHtml)
                    // 图片第一屏时图片直接加载进来
                    // if(self.offset == 0) {
                    //     appendHtml.find("img").trigger("sporty")
                    // }

                    if(self.isReLoad) {
                        self.proList.find('[data-list="bottom"]').remove();

                        self.proList.html(''); // 把原来数据清空
                        self.isReLoad = false; // 取消重新加载

                        self.pageIndex = 0;

                        // 如果首次加载没有数据，则加载推荐数据
                        if(self.offset == 0 && res.data.rows.length == 0) {
                            //self.$filterBar.addClass('disN');
                            self.isRecommending = true;
                            self.$noProTip.removeClass('disN');
                            self.loadRecommendData();
                        }
                        else {
                            self.$noProTip.addClass('disN');
                        }
                    }


                    self.proList.append(appendHtml);

                    // 判断是否到底部
                    if(self.total <= (self.offset + self.limit) && res.data.rows.length != 0) {
                        self.proList.append(self.bottom);
                    }
                }
            }
        })
    }

    // 滚动加载
    List.prototype.loading = function() {
        var self = this;
        // 监听内容滚动区域
        $(window).on('scroll', function() {
            clearTimeout(self.timer);
            // 快到底部100px内时加载
            self.timer = setTimeout(function () {
                if((getScrollTop() + getWindowHeight()) >= (getScrollHeight() - 10)) {
                    // 正常列表查询
                    if(self.offset < self.total) {
                        self.offset = self.offset + self.limit;
                        self.fetchData();
                    }
                    // 推荐列表查询
                    else if(self.offset == 0 && self.pageIndex < self.pageTotal) {
                        self.pageIndex++;
                        self.loadRecommendData();
                    }
                }
            }, 500);
        })
    }

    // 加载推荐数据
    List.prototype.loadRecommendData = function() {
        var self = this;
        $.ajax({
            url:ctx + '/api/repository/Product/recommend',
            dataType:'json',
            data:{
                pageIndex:self.pageIndex,
                pageSize:self.pageSize,
                excludeIds:self.initWishData
            },
            beforeSend:function(res) {
                self.proList.find('[data-list="loading"]').remove();
                self.proList.append(self.loadingTemp);
            },
            success:function(res) {
                if(res.code == 1) {
                    // 保存一下总页数 主要是作为滚动加载的比较使用
                    self.pageTotal = res.data.total;

                    var index = 0;
                    var _html = self.template.render({
                        list:res.data.rows,
                        isWish:function() {
                            return self.Brand.isWishById(this.id) ? 'icon-follow' : 'icon-unfollow'
                        },
                        isFour:function() {
                            ++index;
                            return index % 4 == 0 ? 'last':''
                        }
                    })

                    var appendHtml = $(_html);

                    // 删除加载显示
                    self.proList.find('[data-list="loading"]').remove();

                    // 处理图片延迟加载
                    self.Brand.imgLazy(appendHtml)
                    // 图片第一屏时图片直接加载进来
                    // if(self.pageIndex == 0) {
                    //     appendHtml.find("img").trigger("sporty")
                    // }

                    self.proList.append(appendHtml);

                    // 判断是否到底部
                    if(self.pageTotal <= self.pageIndex) {
                        self.proList.append(self.bottom);
                    }
                }
            }
        })
    }

    // 根据条件重新加载
    List.prototype.reLoad = function() {
        var self = this;
        self.isReLoad= true; // 重新加载
        self.isRecommending = false;

        self.offset = 0;
        self.fetchData();
    }

    // 事件监听
    List.prototype.event = function() {
        var self = this;

        // 是否添加取消心愿单事件监听
        self.proList.on('click', '[data-list="wish"]', function(e) {
            // console.log("clientX:" + e.clientX,"clientY:" + e.clientY);
            var item = $(this);

            // 是否带红心
            // 未带红心 则添加
            if(item.hasClass('icon-unfollow')) {
                self.Brand.setToCookie(item.data("id"));
                item.removeClass('icon-unfollow').addClass('icon-follow')
                // console.log(this.getBoundingClientRect());
                self.Brand.startFly(this, function() {
                    // console.log('test');
                }, e)
            }
            // 已带红心 则取消
            else {
                self.Brand.delFromCookie(item.data("id"));
                item.removeClass('icon-follow').addClass('icon-unfollow')
            }
        });

        // 单击价格区间
        self.$choosePrice.on('click', function(e) {
            if (self.$chooseInterval.hasClass('disN'))
                self.$chooseInterval.removeClass('disN')
            else self.$chooseInterval.addClass('disN');
            e.stopPropagation();
        })

        // 选择价格区间
        self.$chooseInterval.on('click', 'a', function(e) {
            var item = $(this);
            self.$chooseInterval.find('a').removeClass('active');
            item.addClass('active');

            self.startPrice = item.data('start'); // 开始价格
            self.endPrice = item.data('end'); // 结束价格

            self.$choosePrice.find('label').text(item.data('text'));

            self.reLoad();

            self.$chooseInterval.addClass('disN');
            e.stopPropagation();
        })

        // 点击关注度
        self.$fakeAttention.on('click', function(e) {
            // 如果品牌过滤已经打开则关闭
            if(!self.$chooseInterval.hasClass('disN')) self.$chooseInterval.addClass('disN')
            var item = $(this);
            // 排序的列表定义为关注度
            self.orderColumn= 'fake_attention';
            var order = item.hasClass('ico-follow-des');

            if(item.hasClass('ico-follow-des')) {
                item.removeClass('ico-follow-des').addClass('ico-follow-asc')
            }
            else {
                item.removeClass('ico-follow-asc').addClass('ico-follow-des')
            }

            self.orderSort = order ? 'ASC':'DESC';

            self.reLoad();
            e.stopPropagation();
        })

        // 点击价格
        self.$marketPrice.on('click', function(e) {
            // 如果品牌过滤已经打开则关闭
            if(!self.$chooseInterval.hasClass('disN')) self.$chooseInterval.addClass('disN')
            var item = $(this);
            // 排序的列表定义为价格
            self.orderColumn= 'market_price';
            var order = item.hasClass('ico-sort-des');

            if(item.hasClass('ico-sort-des')) {
                item.removeClass('ico-sort-des').addClass('ico-sort-asc')
            }
            else {
                item.removeClass('ico-sort-asc').addClass('ico-sort-des')
            }

            self.orderSort = order ? 'ASC':'DESC';

            self.reLoad();
            e.stopPropagation();
        })
    }

    $(function() {
        var template = Hogan.compile($('#brandtemplate').html());
        var b = new Brand('[data-index="brand"]', template)
        // b.init();

        var list = new List(b);

    })
})(window, document, jQuery)