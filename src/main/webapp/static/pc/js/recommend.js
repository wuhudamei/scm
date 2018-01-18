(function(window, document, $) {
    function Recommend(b) {
        this.template = Hogan.compile($('#recommendtemplate').html()); // 模板
        this.$proList = $('[data-list="prolist"]'); // 商品展示区

        // 数据
        this.Brand = b;
        this.loadingTemp = null;
        this.bottom = null;

        this.pageIndex = 0;
        this.pageSize = 12;
        this.pageTotal = 1;

        this.initWishData = this.Brand.getFromCookie()

        this.init();
    }

    // 初始化
    Recommend.prototype.init = function() {
        var self = this;
        self.loadingTemp = self.template.render({loading:true});
        self.bottom = self.template.render({bottom:true});

        self.fetchData();
        self.loading();
        self.event()
    }

    // 商品数据加载
    Recommend.prototype.fetchData = function() {
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
                self.$proList.append(self.loadingTemp);
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
                    self.$proList.find('[data-list="loading"]').remove();

                    // 处理图片延迟加载
                    self.Brand.imgLazy(appendHtml)
                    // 图片第一屏时图片直接加载进来
                    // if(self.offset == 0) {
                    //     appendHtml.find("img").trigger("sporty")
                    // }
                    self.$proList.append(appendHtml);

                    // 判断是否到底部
                    if(self.pageTotal <= self.pageIndex) {
                        self.$proList.append(self.bottom);
                    }
                }
            }
        })
    }

    // 滚动加载
    Recommend.prototype.loading = function() {
        var self = this;
        // 监听内容滚动区域
        $(window).on('scroll', function() {
            clearTimeout(self.timer);
            // 快到底部100px内时加载
            self.timer = setTimeout(function () {
                if((getScrollTop() + getWindowHeight()) >= (getScrollHeight() - 100) && self.pageIndex < self.pageTotal){
                    self.pageIndex++;
                    self.fetchData();
                }
            }, 500);
        })
    }

    // 事件监听
    Recommend.prototype.event = function() {
        var self = this;
        self.$proList.on('click', '[data-list="wish"]', function(e) {
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
    }



    $(function() {
        var template = Hogan.compile($('#brandtemplate').html());
        var b = new Brand('[data-index="brand"]', template)
        b.init();

        var recommend = new Recommend(b);
    })
})(window, document, jQuery)