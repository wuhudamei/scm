(function(window, document, $) {
    function Detail(b) {
        this.$smallList = $('[data-detail="imgList"]'); // 小图列表
        this.$bigImg = $('[data-detail="bigImg"]'); // 大图
        this.$add = $('[data-detail="add"]'); // 添加 取消心愿单

        this.id = ''; // 商品ID
        this.Brand = b;

        this.init();
    }

    // 初始化
    Detail.prototype.init = function() {
        var self = this;
        self.id = self.$add.data('id');

        self.isWish();
        self.event();
    }

    // 根据是否心愿单设置显示的文本
    Detail.prototype.setText = function(isWish) {
        var self = this;
        if(isWish) {
            self.$add.addClass('btn-white');
            self.$add.html('取消心愿单');
        }
        else {
            self.$add.addClass('btn-red');
            self.$add.html('添加至心愿单');
            
        }
    }

    // 判断该商品是否已是心愿商品
    Detail.prototype.isWish = function() {
        var self = this;
        // 判断是否是心愿商品
        var flag = self.Brand.isWishById(self.id + '');
        // 如果是
        self.setText(flag);
    }

    // 事件
    Detail.prototype.event = function() {
        var self = this;

        // 点击添加取消心愿单
        self.$add.on('click', function(e) {
            var flag = self.Brand.isWishById(self.id + '');
            if(flag) {
                self.Brand.delFromCookie(self.id);
                self.$add.removeClass('btn-white');
                self.$add.addClass('btn-red');
            }
            else {
                self.Brand.setToCookie(self.id);
                self.$add.removeClass('btn-red');
                self.$add.addClass('btn-white');

                self.Brand.startFly(this, function() {
                    // console.log('test');
                }, e)
            }
            // 设置文本
            self.setText(!flag);

            e.stopPropagation();
        })

        // 鼠标滑过小图显示对应的大图
        self.$smallList.on('mouseover', 'img', function(e) {
            var item = $(this);
            // 把小图的路径设置到大图上
            self.$bigImg.attr('src', item.attr('src'))
            // 小图高亮
            self.$smallList.find('li').removeClass('active');
            item.closest('li').addClass('active');

            e.stopPropagation();
        })
    }

    $(function() {
        var template = Hogan.compile($('#brandtemplate').html());
        var b = new Brand('[data-index="brand"]', template)
        b.init();

        var detail = new Detail(b);
    })
})(window, document, jQuery)