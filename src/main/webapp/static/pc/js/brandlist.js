(function(window, document, $) {
    function BrandList(b) {
        this.Brand = b;
        this.template = Hogan.compile($('#brandlisttemplate').html()); // Hogan渲染模板
        this.$letters = $('[data-brandlist="letters"]'); // 字母导航
        this.$chooseBrand = $('[data-brandlist="choosebrand"]'); // 选择的品牌容器
        this.$brandContainer = $('[data-brandlist="brandcontainer"]'); // 显示所有品牌的外层元素
        this.$msgOk = $('[data-brandlist="msgok"]'); // 提示消息确定
        this.$dynamic = $('[data-brandlist="dynamic"]'); // 控制是否固定
        this.init();

        // 数据
        this.chooseList = []; // 选择的品牌数据列表
    }

    // 初始化数据
    BrandList.prototype.init = function() {
        var self = this;

        self.Brand.init();
        self.fetchData();
        self.event();
    }

    // 事件绑定
    BrandList.prototype.event = function() {
        var self = this;
        // 导航字母点击事件 点击添加背景色
        self.$letters.on('click', 'a', function(e) {
            var item = $(this);
            if(!item.hasClass('current')) {
                // 先清除其它的北京
                self.$letters.find('a').removeClass('current');
                item.addClass('current');
            }


            window.scrollTo(0, $('#letter-' + item.data('letter')).offset().top - 250);

            e.stopPropagation();
        })

        // 品牌容器下面的选择 选择品牌
        self.$brandContainer.on('click', 'a', function(e) {
            var item = $(this);

            // 如果是已经选择的品牌则直接返回
            if(item.hasClass('current')) return;

            // 判断是否已经有20个了
            if(self.chooseList.length == 20) {
                $('[data-brandlist="tip"]').css('display', 'block');
                return;
            }

            // 添加背景色
            item.addClass('current');
            var o = {
                id:item.data('id'),
                name:item.data('name')
            }
            // 数据添加到列表中
            self.chooseList.push(o);
            // 在选取的品牌中显示
            var chooseBrandHtml = self.template.render({
                choosebrand:o
            })
            self.$chooseBrand.find('ul').append(chooseBrandHtml)

            // 如果选择未展示出来则展示出来
            if(self.$chooseBrand.hasClass('disN')) self.$chooseBrand.removeClass('disN');

            e.stopPropagation();
        })

        // 提示消息确定
        self.$msgOk.on('click', function(e) {
            $('[data-brandlist="tip"]').css('display', 'none');
            e.stopPropagation();
        })

        // 从选择区取消该品牌
        self.$chooseBrand.on('click', '[data-brandlist="del"]', function(e) {
            var item = $(this);
            var id = item.data('id');
            // 从列表中删除该数据
            for(var i = 0; i < self.chooseList.length; i++) {
                if(id === self.chooseList[i].id) {
                    self.chooseList.splice(i, i+1);
                    break;
                }
            }

            // 移除该元素
            item.closest('li').remove();
            // 选择状态的颜色去掉
            self.$brandContainer.find('#' + id).removeClass('current');

            // 如果选择区没有品牌了，则隐藏掉
            if(self.chooseList.length === 0) {
                self.$chooseBrand.addClass('disN');
            }

            e.stopPropagation();
        })

        // 确定
        self.$chooseBrand.on('click', '[data-brandlist="ok"]', function(e) {

            var _ids = '';
            _.forEach(self.chooseList, function(item, ind) {
                _ids += item.id + ','
            })
            window.location.href = ctx + '/list?brands=' + _ids.slice(0, _ids.length - 1)

            e.stopPropagation();
        })

        // 重置
        self.$chooseBrand.on('click', '[data-brandlist="reset"]', function(e) {
            // 把品牌区选择的状态去掉
            for(var i = 0; i < self.chooseList.length; i++) {
                self.$brandContainer.find('#' + self.chooseList[i].id).removeClass('current');
            }

            self.chooseList = [];
            self.$chooseBrand.addClass('disN');
            self.$chooseBrand.find('ul').html('');
            e.stopPropagation();
        })


        $(window).on('scroll', function(e) {
            // 滚动条高度
            var scroH = $(this).scrollTop();

            //滚动条的滑动距离大于等于定位元素距离浏览器顶部的距离，就固定，反之就不固定
            if (scroH > 100) {
                self.$dynamic.addClass('fixed')
            }
            else {
                self.$dynamic.removeClass('fixed')
            }

            $('[data-view="letter"]').each(function(index, ele) {
                var _item = $(ele);
                var _letter = _item.data('letter');
                var top = _item.offset().top;

                var poor = top - scroH;

                if(250 < poor && poor < 500) {
                    // 先清除其它的北京
                    self.$letters.find('a').removeClass('current');
                    self.$letters.find('#___' + _letter).addClass('current');
                    return false;
                }
            })
        })
    }

    // 获取页面展示的品牌数据
    BrandList.prototype.fetchData = function() {
        var self = this;

        $.ajax({
            url: ctx + '/api/repository/Brand/queryBrandForWeb?containCatalog=false',
            dataType:'json',
            success:function(res) {
                if(res.code == 1) {
                    var letterHtml = self.template.render({
                        letters:res.data
                    })
                    // 字母导航
                    self.$letters.append(letterHtml);

                    // 组装一下按页面展示结构需要的数据
                    var _list = [];
                    for(var i = 0; i < res.data.length; i++) {
                        var _letter = res.data[i];
                        var o = {};
                        o.letter = _letter.letter;
                        var _tlist = [];
                        for(var j = 0; j < _letter.brand.length; j+=5) {
                            _tlist.push({
                                col:_letter.brand.slice(j, j + 5)
                            })
                        }
                        o.row = _tlist;

                        _list.push(o);
                    }
                    // 生成 品牌对应的html
                    var brandHtml = self.template.render({
                        brands:_list
                    })

                    self.$brandContainer.html(brandHtml);
                }
            }
        })
    }

    $(function() {
        var template = Hogan.compile($('#brandtemplate').html());
        var b = new Brand('[data-index="brand"]', template)

        var brandList = new BrandList(b);
    })
})(window, document, jQuery)