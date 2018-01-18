(function(window, document, $) {
    function Follow(b) {
        this.template = Hogan.compile($('#followtemplate').html()); // 模板
        this.$proList = $('[data-list="prolist"]'); // 商品展示区

        this.Brand = b;
        this.loadingTemp = null;

        this.init();
    }

    // 初始化
    Follow.prototype.init = function() {
        var self = this;
        self.loadingTemp = self.template.render({loading:true});

        self.fetchData();
        self.event()
    }

    // 商品数据加载
    Follow.prototype.fetchData = function() {
        var self = this;
        $.ajax({
            url:ctx + '/api/repository/Product/selected',
            dataType:'json',
            data:{
                ids:self.Brand.getFromCookie()
            },
            beforeSend:function(res) {
                self.$proList.append(self.loadingTemp);
            },
            success:function(res) {
                if(res.code == 1) {
                    // console.log(res.data);

                    var _html = self.template.render({
                        listContainer:{
                            list:res.data
                        }
                    })

                    // 删除加载显示
                    // self.$proList.find('[data-list="loading"]').remove();


                    self.$proList.html(_html);

                    // 处理图片延迟加载
                    self.Brand.imgLazy(self.$proList);
                    // 图片第一屏时图片直接加载进来
                    // self.$proList.find("img").trigger("sporty")
                }
            }
        })
    }

    // 事件监听
    Follow.prototype.event = function() {
        var self = this;
        self.$proList.on('click', '[data-list="wish"]', function(e) {
            // console.log("clientX:" + e.clientX,"clientY:" + e.clientY);
            var item = $(this);
            var currItem = item.closest('[data-recommend="item"]');

            // 是否带红心
            // 未带红心 则添加
            if(!currItem.hasClass('_active')) {
                self.Brand.setToCookie(item.data("id"));
                currItem.addClass('_active')
                // console.log(this.getBoundingClientRect());
                self.Brand.startFly(this, function() {
                    console.log('test');
                })
            }
            // 已带红心 则取消
            else {
                self.Brand.delFromCookie(item.data("id"));
                currItem.removeClass('_active')
            }
        });
    }

    $(function() {
        var template = Hogan.compile($('#brandtemplate').html());
        var b = new Brand(null, template)
        b.init();

        var follow = new Follow(b);
    })
})(window, document, jQuery)