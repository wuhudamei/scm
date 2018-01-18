(function(window, document, $) {
    function Detail(b) {
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
        self.initSwiper();
        self.event();
    }

    // 初始化轮播
    Detail.prototype.initSwiper = function() {
        var swiper = new Swiper('.swiper-container-details', {
            pagination: '.swiper-pagination',
            paginationClickable: true,
            autoplay: 2000
        });
    }

    // 根据是否心愿单设置显示的状态
    Detail.prototype.setText = function(isWish) {
        var self = this;
        if(isWish) {
            self.$add.addClass('_active')
        }
        else {
            self.$add.removeClass('_active')
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
                self.Brand.delFromCookie(self.id + '');
            }
            else {
                self.Brand.setToCookie(self.id + '');
                self.Brand.startFly(this, function() {
                    // console.log('test');
                })
            }
            // 设置文本
            self.setText(!flag);

            e.stopPropagation();
        })
    }

    $(function() {
        // var template = Hogan.compile($('#brandtemplate').html());
        var b = new Brand(null, null)
        b.init();

        var detail = new Detail(b);
    })
})(window, document, jQuery)