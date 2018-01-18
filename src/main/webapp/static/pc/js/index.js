// 首页

(function(window, document, $) {

    function Index() {
        this.template = Hogan.compile($('#indextemplate').html());
        this.$swiperList = $('[data-index="swiperlist"]');
    }

    // 初始化
    Index.prototype.init = function() {
        var self = this;
        self.overAndOut()
        self.fetchSwiperData();
    }

    // 获取轮播数据
    Index.prototype.fetchSwiperData = function() {
        var self = this;
        $.ajax({
            url: ctx + '/api/repository/sysBanner/banners?type=PC',
            dataType:'json',
            success:function(res) {
                if(res.code == 1) {
                    var html = self.template.render({
                        swiperList:res.data
                    })

                    self.$swiperList.html(html);
                    // 设置轮播
                    setTimeout(function() {
                        self.banner();
                    }, 300)
                }
            }
        })
    }

    // 处理图片鼠标滑过的效果
    Index.prototype.overAndOut = function() {
        // 鼠标划过
        $('[data-handle="over"]').on('mouseover', function(e){
            var self = $(this);
            var prev = self.prev();
            prev.removeClass('disN');
            e.stopPropagation();
        });

        // 图片鼠标划出
        $('[data-handle="out"]').on('mouseleave', function(e){
            var self = $(this);
            self.addClass('disN');
            e.stopPropagation();
        });
    }

    // 处理Banner，先取回对应的Banner数据，然后设置swiper
    Index.prototype.banner = function() {
        var mySwiper = new Swiper('.swiper-container', {
            loop: true,
            autoplay: 3000,
        });
        $('.swiper-button-prev').on('click', function(e) {
            e.stopPropagation();
            mySwiper.swipePrev();
        })
        $('.swiper-button-next').on('click', function(e) {
            e.stopPropagation();
            mySwiper.swipeNext();
        })
    }

    $(function() {
        // 处理分类品牌
        var template = Hogan.compile($('#brandtemplate').html());
        var b = new Brand('[data-index="brand"]', template)
        b.init();

        var index = new Index();
        index.init()
    })
})(window, document, jQuery)