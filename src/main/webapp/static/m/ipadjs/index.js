// 首页

(function(window, document, $) {

    function Index() {
        this.template = Hogan.compile($('#indextemplate').html());
        this.$swiperList = $('[data-index="swiperlist"]');
    }

    // 初始化
    Index.prototype.init = function() {
        var self = this;
        self.fetchSwiperData();
    }

    // 获取轮播数据
    Index.prototype.fetchSwiperData = function() {
        var self = this;
        $.ajax({
            url: ctx + '/api/repository/sysBanner/banners?type=PAD',
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

    // 处理Banner，先取回对应的Banner数据，然后设置swiper
    Index.prototype.banner = function() {
        var swiper = new Swiper('.swiper-container1', {
            pagination: '.swiper-pagination',
            paginationClickable: true,
            autoplay: 2000
        });
    }

    $(function() {
        // 处理分类品牌
        var template = Hogan.compile($('#brandtemplate').html());
        var b = new Brand(null, template)
        b.init();

        var index = new Index();
        index.init()
    })
})(window, document, jQuery)