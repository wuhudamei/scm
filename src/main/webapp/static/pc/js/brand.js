
(function(window, document, $) {

    function Brand(selector, template) {
        this.selector = $(selector);
        this.template = template; // 各分类品牌模板
        this.selectsBrand = []; // 当前已选品牌
        this.data = null;
        this.brandContainer = $('[data-brand="container"]'); // 所有的分类
        this.wish = $('[data-wish="_count"]'); // 心愿数
        this.isZero = $('[data-iszero="_count"]'); // 右上角的心愿 其实这个跟心愿数是一个 主要是为了控制为0的时候不显示
        this.search = $('[data-brand="__search"]'); // 搜索框
        this.tosearch = $('[data-brand="__tosearch"]'); // 去搜索
        this.toDel = $('[data-brand="__todel"]'); // 删除
        this.$goList = $('[data-brand="golist"]'); // 搜索框表单 Form
        this.searchHistory = $('[data-brand="search-history"]'); // 搜索下拉列表

        this.fly = $('#_fly'); // 飞心

        this.__WISHLIST = "__WISHLIST"; // 心愿单在cookie中的键值
        this.wishList = []; // 选择的商品列表

        // 分类对象
        this.__CATA = {
            "1":{
                id:"1",
                name:"生活电器"
            },
            "2":{
                id:"2",
                name:"厨电"
            },
            "3":{
                id:"3",
                name:"个护"
            },
            "4":{
                id:"4",
                name:"家居"
            },
            "5":{
                id:"5",
                name:"厨具"
            },
            "6":{
                id:"6",
                name:"商旅"
            },
            "7":{
                id:"7",
                name:"户外"
            },
            "8":{
                id:"8",
                name:"电子"
            },
            "9":{
                id:"9",
                name:"汽车"
            },
            "10":{
                id:"10",
                name:"母婴"
            },
            "11":{
                id:"11",
                name:"精品"
            },
            "12":{
                id:"12",
                name:"卡券"
            },
            "13":{
                id:"13",
                name:"虚拟"
            }
        }
    }

    //把已选择的品牌ID列表传进来
    Brand.prototype.init = function(selects) {
        this.selectsBrand = selects || [];
        this.initWishList();
        this.fetchAllBrand();
    }

    // 请求后台数据
    Brand.prototype.fetchAllBrand = function() {
        var self = this;
        $.ajax({
            url:ctx + '/api/repository/Brand/queryBrandForWeb?containCatalog=true&a=b',
            dataType:'json',
            success:function(res) {
                if(res.code == 1) {
                    self.data = res.data;
                    self.createTemplate();
                }
            }
        })
    }

    // 组织数据根据模板生成 html 代码
    Brand.prototype.createTemplate = function() {
        var self = this;
        // 判断该品牌是否是选择状态
        function isChoose(id) {
            for(var i = 0; i < self.selectsBrand.length; i++) {
                if(id == self.selectsBrand[i]) return true;
            }
            return false;
        }
        // 组装品牌html代码
        var brandHtml = self.template.render({
            brand:self.data,
            isChooseBorder:function() {
                return isChoose(this.id) ? 'current' : ''
            },
            isChooseDel:function() {
                return isChoose(this.id) ? 'icon-del' : ''
            }
        })

        self.selector.html(brandHtml)
        self.event();
    }

    // 根据关键字动态搜索对应的商品
    Brand.prototype.dynamicSearch = function(val) {
        var self = this;
        $.ajax({
            url: ctx + '/api/repository/Product/searchLike',
            data:{
                keyword:val
            },
            dataType:'json',
            beforeSend:function() {
                self.searchHistory.addClass('disN');
            },
            success:function(res) {
                if(res.code == 1) {
                    var historyHtml = self.template.render({
                        searchHistory:res.data
                    })

                    self.searchHistory.html(historyHtml);
                    // 显示出来
                    self.searchHistory.removeClass('disN');
                }
            }
        })
    }

    // 根据分类ID设置分类的下标选择状态
    Brand.prototype.setCataCurrent = function(id) {
        if(!id) return;
        this.brandContainer.find('[data-cata="' + id + '"]').closest('li').addClass('current');
    }

    // 事件处理
    Brand.prototype.event = function() {
        var self = this;
        // 字母鼠标滑过
        self.selector.on('mouseover', '[data-handle="_letterover"]', function(e) {
            e.stopPropagation();

            var _item = $(this);

            var _brandList = $('[data-b="' + _item.data('brand') + '"]')
            var _classify = _item.data("classify"); // 分类ID

            var closestBrand = _item.closest('[data-brand="_' + _classify + '"]');

            closestBrand.find('[data-handle="_letterover"]').removeClass("current");
            _item.addClass('current');

            closestBrand.find('[data-brands="_mouseout"]').addClass('disN');

            _brandList.removeClass('disN')
            return false;
        })

        // 鼠标移出品牌区域则消失
        $('[data-brand="brandmouseout"]').on('mouseout', function(e) {
            e.stopPropagation();
            if(checkHover(e,this)){
                self.selector.find('[data-brands="all"]').addClass('disN');
            }
            return false;
        })

        // 滑过分类鼠标事件
        self.brandContainer.on('mouseover', '[data-brand="cata"]', function(e) {
            var _item = $(this);
            // 分类ID
            var classify = _item.data("cata");
            // 先隐藏所有的分类下标状态
            // self.brandContainer.find('li').removeClass("current");
            // 标示当前分类
            // _item.closest('li').addClass("current");
            // 隐藏所有的品牌
            self.selector.find('[data-brands="all"]').addClass('disN');
            // 显示当前分类下的品牌
            var currentCata = self.selector.find('[data-brand="_' + classify + '"]');
            currentCata.removeClass('disN');

            // 显示第一个字母及对应的品牌
            // data-handle="_letterover"
            // data-brands="_mouseout"
            currentCata.find('[data-handle="_letterover"]').removeClass('current');
            currentCata.find('[data-brands="_mouseout"]').addClass('disN')
            currentCata.find('[data-handle="_letterover"]:first').addClass('current');
            currentCata.find('[data-brands="_mouseout"]:first').removeClass('disN')

            e.stopPropagation();
            e.preventDefault();
            return false
        })

        // 监听搜索框内容变化
        var timer = null;
        self.search.on('input', function(e) {
            clearTimeout(timer);
            var item = $(this);
            timer = setTimeout(function() {
                // 1. 变更查询内容
                var val = item.val();
                self.tosearch.attr('href', ctx + '/list?keyword=' + val);
                // 2. 动态匹配查询
                // 如果是空数据，则直接隐藏
                if($.trim(val) === '') {
                    self.searchHistory.addClass('disN');

                    self.tosearch.removeClass('disN');
                    self.toDel.addClass('disN');
                    return;
                }
                else {
                    self.tosearch.addClass('disN');
                    self.toDel.removeClass('disN');
                }

                self.dynamicSearch(val);
            }, 500)
            e.stopPropagation();
        })

        // 点击清除搜索框数据
        self.toDel.on('click', function(e) {
            self.search.val('');
            self.tosearch.removeClass('disN');
            self.toDel.addClass('disN');
            self.searchHistory.addClass('disN');
            e.stopPropagation();
        })

        // 搜索框获得焦点显示X号
        // self.search.on('focus', function(e) {
        //     self.tosearch.addClass('disN');
        //     self.toDel.removeClass('disN');
        // })
        // // 搜索框失去焦点显示搜索图
        // self.search.on('blur', function(e) {
        //     self.tosearch.removeClass('disN');
        //     self.toDel.addClass('disN');
        // })

        // 输入搜索内容按键盘搜索跳转到列表页
        self.$goList.on('submit', function(e) {
            window.location.href = ctx + '/list?keyword=' + self.search.val()

            e.stopPropagation();
            return false;
        })
    }

    // 心愿单数量更新
    Brand.prototype.updateWishCount = function(count) {
        this.wish.html(count);
    }

    // 开始飞
    Brand.prototype.startFly = function(item, callback, e) {
        var position = {};
        if(typeof getBoundingClientRect === 'function') {
            position = item.getBoundingClientRect()
        }
        else {
            position = {
                left:e.clientX,
                top:e.clientY
            }
        }
        this.flyTo($(window).width() - position.left, $(window).height() - position.top, callback)
    }

    // 处理数字飞向心愿
    Brand.prototype.flyTo = function(right, bottom, callback) {
        var self = this;
        // self.fly.velocity("finish");
        self.fly.velocity("stop");
        self.fly.css('right', right).css('bottom', bottom);

        var _offset = self.wish.offset()
        //console.log(_offset);
        //right:$(window).width() - _offset.left,
        //_offset.top
        self.fly.velocity({
            right:60,
            bottom: 250
        },{
            duration:2000,
            begin: function(el) {
                self.fly.show()
            },
            complete:function(el) {
                callback && callback();
                self.fly.hide()
                self.fly.velocity("finish");
            }
        });
    }

    // item 内的图片添加延迟加载
    Brand.prototype.imgLazy = function(item) {
        // 处理图片延迟加载
        item.find('img').lazyload({
            placeholder : ctx + "/static/pc/images/pro/load-img.jpg",
            effect : "fadeIn",
            threshold : 200
            // event : "sporty"
        });
    }

    // 从cookie取回所选择的商品列表，并设置心愿数
    Brand.prototype.initWishList = function() {
        var self = this;
        var __wishList = cookie.get(self.__WISHLIST);
        // 如果已经有了，则取出来做相应的设置
        if(__wishList) {
            self.wishList = JSON.parse(__wishList);
            self.wish.html(self.wishList.length);

            if(self.wishList.length == 0) {
                self.isZero.addClass('disN');
            }
            else {
                self.isZero.removeClass('disN');
            }
        }
        // 如果是没有则直接置0
        else {
            self.wish.html(0);
            self.isZero.addClass('disN');
        }
    }

    // 把该商品添加到cookie并设置数量
    Brand.prototype.setToCookie = function(id) {
        var self = this;
        self.wishList.push(id + '');
        setTimeout(function() {
            self.wish.html(self.wishList.length);
            if(self.wishList.length == 0) {
                self.isZero.addClass('disN');
            }
            else {
                self.isZero.removeClass('disN');
            }
        }, 2000)

        cookie.set(self.__WISHLIST, JSON.stringify(self.wishList), {path:'/'});
    }

    // 把该商品从cookie删除并设置数量
    Brand.prototype.delFromCookie = function(id) {
        var self = this;
        self.wishList = _.difference(self.wishList, [id + '']);
        self.wish.html(self.wishList.length);

        if(self.wishList.length == 0) {
            self.isZero.addClass('disN');
        }

        cookie.set(self.__WISHLIST, JSON.stringify(self.wishList), {path:'/'});
    }

    // 返回心愿单数据 如果有参数则按照参数长度返回，没有则全部返回
    Brand.prototype.getFromCookie = function(l) {
        var _arr = l ? this.wishList.slice(0, l) : this.wishList;
        return _arr
    }

    // 根据该商品ID检查是否是心愿商品 如果是则返回true 否则返回 false
    Brand.prototype.isWishById = function(id) {
        var self = this;
        var l = self.wishList.length;
        return l > _.difference(self.wishList, [id + '']).length ? true : false
    }

    // 删除所有的心愿单数据
    Brand.prototype.removeWish = function() {
        this.wishList = [];
        cookie.set(this.__WISHLIST, '', {path:'/'});
    }

    // 设置文本框内容
    Brand.prototype.setSearchText = function(val) {
        this.search.val(val);
    }

    window.Brand = Brand || {};

    // $(function() {
    //     var template = Hogan.compile($('#brandtemplate').html());
    //     var b = new Brand('[data-index="brand"]', template)
    //     b.init(['123']);
    // })
})(window, document, jQuery)