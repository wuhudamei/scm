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
        this.$loading = $('[data-list="loading"]'); // loading提示
        this.$brand = $('[data-list="brand"]'); // 品牌过滤
        this.$brandContainer = $('[data-list="brandcontainer"]'); // 品牌显示区域
        this.$filterBar = $('[data-list="filterbar"]'); // 过滤条
        this.$bottom = $('[data-list="bottom"]'); // 已经到底了，向上翻翻吧

        // 数据
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

        // 如果有分类
        if(self.catalogId) {
            self.Brand.setCataCurrent(self.catalogId);

            // 提示
            self.tip.html(self.Brand.__CATA[self.catalogId + ''].name)

            // 根据分类显示该分类下的品牌数据
            self.fetchBrandData();
        }
        // 如果不是分类过来的则直接删除品牌过滤
        else {
            self.$brand.remove();
        }

        // 加载 数据
        self.fetchData();
    }

    // 根据分类获取品牌数据
    List.prototype.fetchBrandData = function() {
        var self = this;
        $.ajax({
            url:ctx + '/api/repository/Brand/queryBrandForWeb?containCatalog=false&catalogId=' + self.catalogId,
            dataType:'json',
            success:function(res) {
                if(res.code == 1) {
                    // 计算宽度
                    for(var i = 0; i < res.data.length; i++) {
                        res.data[i].witdh = (res.data[i].brand.length * 1.5); // 动态设置显示的宽度
                        res.data[i].isFirst = (i == 0 ? '':'disN'); // 默认显示第一个字母的品牌
                        res.data[i].isActive = (i == 0 ? '_active':''); // 默认激活第一个字母
                    }
                    // 生成模板内容
                    var html = self.template.render({
                        brand:{
                            letter:res.data
                        }
                    })

                    self.$brandContainer.html(html);
                }
            }
        })
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
                self.$loading.removeClass('disN');
            },
            success:function(res) {
                if(res.code == 1) {
                    // 取得总条数
                    self.total = res.data.total;

                    var _html = self.template.render({
                        listContainer:{
                            list:res.data.rows,
                        },
                        isWish:function() {
                            return self.Brand.isWishById(this.id) ? '_active' : ''
                        }
                    })

                    var appendHtml = $(_html);

                    // 删除加载显示
                    self.$loading.addClass('disN');

                    // 删除占位符
                    self.proList.find('.list-block-placeholder').remove();

                    // 处理图片延迟加载
                    self.Brand.imgLazy(appendHtml)
                    // 图片第一屏时图片直接加载进来
                    // if(self.offset == 0) {
                    //     appendHtml.find("img").trigger("sporty")
                    // }

                    if(self.isReLoad) {

                        // 如果重新加载则把到底部的标志隐藏
                        self.$bottom.addClass('disN');

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
                        self.$bottom.removeClass('disN');
                    }
                }
            }
        })
    }

    // 滚动加载
    List.prototype.loading = function() {
        var self = this;
        // 监听内容滚动区域
        document.addEventListener('scroll', function () {

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
        });
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
                self.$loading.removeClass('disN');
            },
            success:function(res) {
                if(res.code == 1) {
                    // 保存一下总页数 主要是作为滚动加载的比较使用
                    self.pageTotal = res.data.total;

                    var _html = self.template.render({
                        listContainer:{
                            list:res.data.rows,
                        },
                        isWish:function() {
                            return self.Brand.isWishById(this.id) ? '_active' : ''
                        }
                    })

                    var appendHtml = $(_html);

                    // 删除加载显示
                    self.$loading.addClass('disN');

                    // 删除占位符
                    self.proList.find('.list-block-placeholder').remove();

                    // 处理图片延迟加载
                    self.Brand.imgLazy(appendHtml)

                    // 图片第一屏时图片直接加载进来
                    // if(self.pageIndex == 0) {
                    //     appendHtml.find("img").trigger("sporty")
                    // }

                    self.proList.append(appendHtml);

                    // 判断是否到底部
                    if(self.pageTotal <= self.pageIndex) {
                        self.$bottom.removeClass('disN');
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
            var currItem = item.closest('[data-recommend="item"]');

            // 是否带红心
            // 未带红心 则添加
            if(!currItem.hasClass('_active')) {
                self.Brand.setToCookie(item.data("id"));
                currItem.addClass('_active')
                self.Brand.startFly(this, function() {
                    // console.log('test');
                })
            }
            // 已带红心 则取消
            else {
                self.Brand.delFromCookie(item.data("id"));
                currItem.removeClass('_active')
            }
        });

        // 单击价格区间
        self.$choosePrice.on('click', function(e) {
            // 如果品牌选择区已经打开，则关闭
            if(!self.$brandContainer.hasClass('hidden')) self.$brandContainer.addClass('hidden')

            if (self.$chooseInterval.hasClass('disN'))
                self.$chooseInterval.removeClass('disN')
            else self.$chooseInterval.addClass('disN');
            e.stopPropagation();
        })

        // 选择价格区间
        self.$chooseInterval.on('click', 'div', function(e) {
            var item = $(this);
            self.$chooseInterval.find('div').removeClass('_active');
            item.addClass('_active');

            self.startPrice = item.data('start'); // 开始价格
            self.endPrice = item.data('end'); // 结束价格

            self.$choosePrice.find('label').text(item.data('text'));

            self.reLoad();

            self.$chooseInterval.addClass('disN');

            // 如果需要根据价格区间过滤则高亮显示
            if(item.data('text') === '价格区间') {
                self.$choosePrice.removeClass('_active');
            }
            else {
                self.$choosePrice.addClass('_active');
            }

            e.stopPropagation();
        })

        // 点击关注度
        self.$fakeAttention.on('click', function(e) {
            // 如果品牌选择区已经打开，则关闭
            if(!self.$brandContainer.hasClass('hidden')) self.$brandContainer.addClass('hidden')
            // 如果价格区间过滤已经打开，则关闭
            if(!self.$chooseInterval.hasClass('disN')) self.$chooseInterval.addClass('disN')

            var item = $(this);
            // 点击关注度则高亮显示
            item.addClass('_active');
            // 如果价格已是高亮则取消
            if(self.$marketPrice.hasClass('_active')) self.$marketPrice.removeClass('_active');

            // 排序的列表定义为关注度
            self.orderColumn= 'fake_attention';
            // var order = item.hasClass('ico-follow-des');

            // if(item.hasClass('ico-follow-des')) {
            //     item.removeClass('ico-follow-des').addClass('ico-follow-asc')
            // }
            // else {
            //     item.removeClass('ico-follow-asc').addClass('ico-follow-des')
            // }

            // self.orderSort = order ? 'ASC':'DESC';

            self.orderSort = self.orderSort == 'ASC' ? 'DESC' : 'ASC';

            self.reLoad();
            e.stopPropagation();
        })

        // 点击品牌过滤
        self.$brand.on('click', function(e) {
            // 如果价格区间过滤已经打开，则关闭
            if(!self.$chooseInterval.hasClass('disN')) self.$chooseInterval.addClass('disN')

            var item = $(this);
            if(self.$brandContainer.hasClass('hidden'))
                self.$brandContainer.removeClass('hidden');
            else
                self.$brandContainer.addClass('hidden');
            e.stopPropagation();
        })

        // 点击字母
        self.$brandContainer.on('click', '[data-list="swap"]', function(e) {
            var item = $(this);
            // 如果当前就是选择状态则直接返回
            if(item.hasClass('_active')) return;

            var _letter = item.data('letter');
            // 清除其它字母的选择状态，变当前字母为选择状态
            self.$brandContainer.find('[data-list="swap"]').removeClass('_active');
            item.addClass('_active');

            // 隐藏所有字母对应的品牌
            self.$brandContainer.find('[data-list="allbrand"]').addClass('disN');
            // 显示当前点击字母对应的品牌
            self.$brandContainer.find('[data-letter="_' + _letter + '"]').removeClass('disN');

            e.stopPropagation();
        })

        // 点击品牌 进行查询
        self.$brandContainer.on('click', '[data-brand="allitem"]', function(e) {
            var item = $(this);
            if(item.hasClass('_active')) {
                item.removeClass('_active');
                self.brandIds = [];
                // 如果没有根据品牌过滤则不需要高亮显示了
                self.$brand.removeClass('_active');
            }
            else {
                // 如果点击的不是已选的品牌，则需要先把其它已选的品牌的选择状态去掉
                self.$brandContainer.find('[data-brand="allitem"]').removeClass('_active');
                // 给当前品牌添加选择状态
                item.addClass('_active');
                self.brandIds = [item.data('id')];
                // 如果需要根据品牌过滤则高亮显示
                self.$brand.addClass('_active');
            }

            self.$brandContainer.addClass('hidden');

            self.reLoad();
            e.stopPropagation();
        })

        // 点击价格
        self.$marketPrice.on('click', function(e) {
                        // 如果品牌选择区已经打开，则关闭
            if(!self.$brandContainer.hasClass('hidden')) self.$brandContainer.addClass('hidden')
            // 如果价格区间过滤已经打开，则关闭
            if(!self.$chooseInterval.hasClass('disN')) self.$chooseInterval.addClass('disN')

            var item = $(this);
            // 如果点击价格则高亮显示
            item.addClass('_active');
            // 如果关注度已经高亮则取消
            if(self.$fakeAttention.hasClass('_active')) self.$fakeAttention.removeClass('_active');
            // 排序的列表定义为价格
            self.orderColumn= 'market_price';

            var order = item.hasClass('_asc'); // 是否升序 有_asc 为升

            if(order) {
                item.removeClass('_asc')
            }
            else {
                item.addClass('_asc')
            }

            self.orderSort = !order ? 'ASC':'DESC';

            self.reLoad();
            e.stopPropagation();
        })
    }

    $(function() {
        var template = Hogan.compile($('#brandtemplate').html());
        var b = new Brand(null, template)
        // b.init();

        var list = new List(b);

    })
})(window, document, jQuery)