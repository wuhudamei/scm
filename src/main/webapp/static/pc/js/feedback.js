(function(window, document, $) {
    function Feedback(b) {
        this.template = Hogan.compile($('#feedbacktemplate').html()); // 模板
        this.$form = $('[data-feedback="form"]');

        this.$ok = $('[data-feedbak="ok"]'); // 提交
        this.$reset = $('[data-feedbak="reset"]'); // 重置
        this.$tipBtn = $('[data-feedback="tipbtn"]'); // 提示信息确定按钮
        this.$choosePro = $('[data-feedback="choosepro"]'); // 已选商品轮播
        this.$swiperPre = $('[data-feedback="swiperpre"]'); // 已选商品 向右轮播按钮
        this.$swiperNext = $('[data-feedback="swipernext"]'); // 已选商品 向右轮播按钮
        this.$recommendPro = $('[data-feedback="recommendpro"]'); // 推荐商品
        this.$selectedPro = $('[data-feedback="selectedpro"]'); // 已选商品大区域
        this.$tipForNoPro = $('[data-feedback="noprotip"]'); // 没有商品时展示
        this.$tipForHasPro = $('[data-feedback="hasprotip"]'); // 有商品时展示

        this.Brand = b;
        this.mySwiper = null; // 轮播对象
        this.isSuccess = false;
        this.mobileVal=false;//手机号填写正确
        this.nameVal=false;//姓名填写不为空
        this.companyVal=false;//公司填写不为空
        this.init();
    }

    // 初始化数据
    Feedback.prototype.init = function() {
        this.echoPersonInfo();
        this.fetchChoosePro();
        this.fetchRecommend();
        this.event();
    }

    // 如果有个人信息，则直接回显
    Feedback.prototype.echoPersonInfo = function() {
        var self = this;
        var personInfo = cookie.get('__personinfo');

        if(personInfo) {
            personInfo = personInfo && JSON.parse(personInfo) ;

            _.forIn(personInfo, function(value, key) {
                self.$form.find('[name="' + key + '"]').val(value);
                // 如果所在部门为其它则把其它显示出来
                if(key === 'part' && value === '其它') {
                    self.$form.find('[data-feedback="otherreason"]').removeClass('disN');
                }
            })
        }
    }

    // 获取已选商品
    Feedback.prototype.fetchChoosePro = function() {
        var self = this;

        // 如果没有选择心愿单
        var _wish = self.Brand.getFromCookie();
        if(_wish.length == 0) {
            self.handlerSwiper();
            self.$tipForNoPro.removeClass('disN');
            return;
        }
        else {
            self.$selectedPro.removeClass('disN');
            self.$tipForHasPro.removeClass('disN');
        }

        $.ajax({
            url:ctx + '/api/repository/Product/selected',
            dataType:'json',
            data:{
                ids:self.Brand.getFromCookie(_wish.length > 8 ? 7:8)
            },
            success:function(res) {
                if(res.code == 1) {
                    var chooseProHtml = self.template.render({
                        choose:{
                            list:res.data,
                            morethan:_wish.length > 8 ? true : false
                        }
                    })

                    // 如果不满4个商品则不显示向右按钮
                    if(res.data && res.data.length <= 4) {
                        self.$swiperNext.addClass('disN');
                        self.$swiperPre.addClass('disN');
                    }
                    self.$choosePro.html(chooseProHtml);
                    // self.$choosePro.html('');

                    // 图片延迟加载
                    self.Brand.imgLazy(self.$choosePro);

                    setTimeout(function() {
                        self.handlerSwiper();
                    }, 300)
                }
            }
        })
    }

    // 轮播已选商品
    Feedback.prototype.handlerSwiper = function() {
        var self = this;
        self.mySwiper = new Swiper('.swiper-container', {
            slidesPerView: 4,
            noSwiping : true // 设置不可以滑动 只能通过点击滑动
        });
        self.$swiperPre.on('click', function(e) {
            e.stopPropagation();
            // mySwiper.swipePrev();
            self.mySwiper.swipeTo(0, 500, false)
            self.$swiperPre.addClass('disN');
            var _arr = self.$choosePro.find('.swiper-slide');
            if(_arr.length >= 5)
                self.$swiperNext.removeClass('disN');
        })
        self.$swiperNext.on('click', function(e) {
            e.stopPropagation();
            // mySwiper.swipeNext();
            var _arr = self.$choosePro.find('.swiper-slide');
            var toPosition = 5;
            if(_arr.length > 6) {
                toPosition = 5;
            }
            else if(_arr.length === 5) {
                toPosition = 4;
            }

            self.mySwiper.swipeTo(toPosition, 500, false)
            self.$swiperNext.addClass('disN');
            self.$swiperPre.removeClass('disN');
        })
    }

    // 获取推荐商品
    Feedback.prototype.fetchRecommend = function() {
        var self = this;

        $.ajax({
            url:ctx + '/api/repository/Product/recommend',
            data: {
                pageIndex:0,
                pageSize:7,
                excludeIds:self.Brand.getFromCookie()
            },
            dataType:'json',
            success:function(res) {
                if(res.code == 1) {

                    var index = 0;
                    var _html = self.template.render({
                        recommend:{
                            list:res.data.rows,
                            isWish:function() {
                                return self.Brand.isWishById(this.id) ? 'icon-follow' : 'icon-unfollow'
                            }
                        },
                        isFour:function() {
                            ++index;
                            return index % 4 == 0 ? 'last':''
                        }
                    })

                    self.$recommendPro.append(_html);

                    // 图片延迟加载
                    self.Brand.imgLazy(self.$recommendPro);

                }
            }
        })
    }

    // 添加取消心愿
    Feedback.prototype.addOrCancelWish = function(item, e) {
        var self = this;
        // 是否带红心
        // 未带红心 则添加
        if($(item).hasClass('icon-unfollow')) {
            self.Brand.setToCookie($(item).data("id"));
            $(item).removeClass('icon-unfollow').addClass('icon-follow')
            // console.log(this.getBoundingClientRect());
            self.Brand.startFly(item, function() {
                // console.log('test');
            }, e)
        }
        // 已带红心 则取消
        else {
            self.Brand.delFromCookie($(item).data("id"));
            $(item).removeClass('icon-follow').addClass('icon-unfollow')
        }
    }

    // 事件监听
    Feedback.prototype.event = function() {
        var self = this;
        $(document).keyup(function(event){
            if(event.keyCode == 8){
                var nameval=$("#name").val();
                var mobileval=($("#mobile").val());
                var companynameval=($("#companyname").val());

                if(nameval==''){
                    self.nameVal=false;
                }else{
                    self.nameVal=true;
                }
                if(!(/^(((0\d{2,3}\-)?\d{7,8}(\-\d{3})?)|(1[35784]\d{9}))$/.test(mobileval))){
                    self.mobileVal=false;
                }else{
                    self.mobileVal=true;
                }
                if(companynameval==''){
                    self.companyVal=false;
                }else{
                    self.companyVal=true;
                }
                if(self.mobileVal&&self.nameVal&&self.companyVal){
                    self.$ok.removeClass('_gray').removeClass('btn-disabled').addClass('_white');
                }else{
                    self.$ok.removeClass('_white').addClass('btn-disabled').addClass('_gray');
                }
            }
        });
        //  监听所有部门变化，如果是其它的时候，需要显示自由输入框
        self.$form.on('change', '[name="part"]', function(e) {
            var item = $(this);
            var val = item.val();

            if(val === '其它') {
                self.$form.find('[data-feedback="otherreason"]').removeClass('disN');
            }
            else {
                self.$form.find('[data-feedback="otherreason"]').addClass('disN');
            }

            e.stopPropagation();
        })
        //电话输入失焦
        self.$form.on('blur', '[name="mobile"]', function(e) {
            var item = $(this);
            var val = item.val();
            if(!(/^(((0\d{2,3}\-)?\d{7,8}(\-\d{3})?)|(1[35784]\d{9}))$/.test(item.val()))){
                self.$form.find('#mobile').addClass('error');
                self.$form.find('#mobiletip').removeClass('disN').addClass('disB');
                self.mobileVal=false;
            }else{
                self.$form.find('#mobiletip').removeClass('disB').addClass('disN');
                self.$form.find('#mobile').removeClass('error');
                self.mobileVal=true;
            }
            e.stopPropagation();
        })

        var emailReg = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;


        // 监听表单，内容有变则可点击提交
        self.$form.on('input', 'input', function(e) {
            var item = $(this);

            var name = item.attr('name');
            if(item.val() !== '') {
                //self.$ok.removeClass('_gray').removeClass('btn-disabled').addClass('_white');

                // 如果是电话，输入正确的电话号码时需要把提示去掉
                if(name == 'mobile') {
                    if((/^(((0\d{2,3}\-)?\d{7,8}(\-\d{3})?)|(1[35784]\d{9}))$/.test(item.val()))){
                        self.$form.find('#' + name).closest('.item-input-box').removeClass('item-error');
                        self.$form.find('#' + name + 'tip').addClass('disN');
                        self.mobileVal=true;
                    }else{
                        self.mobileVal=false;
                    }
                    //return;
                }
                else if(name == 'email') {
                    if(emailReg.test(item.val())){
                        self.$form.find('#' + name).closest('.item-input-box').removeClass('item-error');
                        self.$form.find('#' + name + 'tip').addClass('disN');
                    }
                    //return;
                }else if(name == 'name'){
                    if(item.val()!=''){
                        self.nameVal=true;
                    }else{
                        self.nameVal=false;
                    }
                }else if(name == 'companyname'){
                    if(item.val()!=''){
                        self.companyVal=true;
                    }else{
                        self.companyVal=false;
                    }
                }

                self.$form.find('#' + name).closest('.item-input-box').removeClass('item-error');
                self.$form.find('#' + name + 'tip').addClass('disN');
            }
            if(self.mobileVal&&self.nameVal&&self.companyVal){
                self.$ok.removeClass('_gray').removeClass('btn-disabled').addClass('_white');
            }else{
                self.$ok.removeClass('_white').addClass('btn-disabled').addClass('_gray');
            }
            e.stopPropagation();
        })

        // 提交
        self.$ok.on('click', function(e) {
            var formData = self.$form.serialize();
            // 转化对象
            formData = parseQueryString(formData);

            // 校验数据
            var flag = false;
            // 姓名
            if($.trim(formData.name) === '') {
                self.$form.find('#name').focus();
                self.$form.find('#name').addClass('error');
                self.$form.find('#nametip').removeClass('disN').addClass('disB');
                flag = true;
            }
            else {
                self.$form.find('#name').removeClass('error');
                self.$form.find('#nametip').addClass('disN');
            }
            // 电话
            if($.trim(formData.mobile) === '' || !(/^(((0\d{2,3}\-)?\d{7,8}(\-\d{3})?)|(1[35784]\d{9}))$/.test(formData.mobile))) {
                if(!flag) self.$form.find('#mobile').focus();
                self.$form.find('#mobile').addClass('error');
                self.$form.find('#mobiletip').removeClass('disN').addClass('disB');
                flag = true;
            }
            else {
                self.$form.find('#mobile').removeClass('error');
                self.$form.find('#mobiletip').addClass('disN');
            }
            // 邮箱
            if($.trim(formData.email) !== '' && !emailReg.test(formData.email)) {
                if(!flag) self.$form.find('#email').focus();
                self.$form.find('#email').addClass('error');
                self.$form.find('#emailtip').removeClass('disN').addClass('disB');
                flag = true;
            }
            else {
                self.$form.find('#email').removeClass('error');
                self.$form.find('#emailtip').addClass('disN');
            }
            // 公司名称
            if($.trim(formData.companyname) === '') {
                if(!flag) self.$form.find('#companyname').focus();
                self.$form.find('#companyname').addClass('error');
                self.$form.find('#companynametip').removeClass('disN').addClass('disB');
                flag = true;
            }
            else {
                self.$form.find('#companyname').removeClass('error');
                self.$form.find('#companynametip').addClass('disN');
            }

            if(flag) return;


            // 保存到cookie
            cookie.set('__personinfo', JSON.stringify(formData));

            var wishList = self.Brand.getFromCookie();

            var needProductList = '';
            _.forEach(wishList, function(item, idx) {
                needProductList += 'needProductList['+idx+'].productId='+item+ '&';
            })
            needProductList = needProductList.substring(0, needProductList.length-1);

            $.ajax({
                url:ctx + '/api/repository/Massage/saveMassage',
                dataType:'json',
                type:'post',
                data: $.param({
                    name:formData.name,
                    phone:formData.mobile,
                    email:formData.email,
                    companyName:formData.companyname,
                    department:formData.part == '其它' ? formData.otherreason : formData.part,
                    leaveMsg:formData.leaveword,
                })+"&"+needProductList,
                success:function(res) {
                    $('[data-feedback="msg"]').css('display', 'block');
                    if(res.code == 1) {
                        cookie.remove(['__personinfo']); // 删除保存的个人填写的数据
                        self.Brand.removeWish(); // 删除所有心愿单数据
                        $('[data-feedback="tip"]').html('您的留言已提交成功，我们会尽快与您取得联系。');
                        self.isSuccess = true;
                    }
                    else {
                        $('[data-feedback="tip"]').html('网络不太给力，留言提交失败，请重新提交。');
                        self.isSuccess = false;
                    }
                }
            })
            e.stopPropagation();
        })

        // 重置
        self.$reset.on('click', function(e) {
            self.$form[0].reset();
            self.$form.find('[data-feedback="otherreason"]').addClass('disN');
            self.$ok.removeClass('_white').addClass('_gray').addClass('btn-disabled');
            self.$form.find('#name').removeClass('error');
            self.$form.find('#nametip').removeClass('disB').addClass('disN');
            self.$form.find('#mobile').removeClass('error');
            self.$form.find('#mobiletip').removeClass('disB').addClass('disN');
            self.$form.find('#companyname').removeClass('error');
            self.$form.find('#companynametip').removeClass('disB').addClass('disN');
            self.$form.find('#email').removeClass('error');
            self.$form.find('#emailtip').removeClass('disB').addClass('disN');

            self.nameVal=false;
            self.mobileVal=false;
            self.companyVal=false;
        })

        // 消息提示确定按钮，点击确定隐藏提示框
        self.$tipBtn.on('click', function(e) {
            $('[data-feedback="msg"]').css('display', 'none');
            if(self.isSuccess) window.location.href = ctx + '/'
            e.stopPropagation();
        })

        // 监听已选商品部分是否添加取消心愿
        self.$choosePro.on('click', '[data-list="wish"]', function(e) {
            var item = $(this);
            // 从已选列表移除
            // if(item.hasClass('icon-follow')) {
            //     var index = self.$choosePro.find('.swiper-slide').index(item.closest('.swiper-slide'));

            //     self.mySwiper.removeSlide(index);

            //     // 如果只剩三个，则把左右隐藏掉
            //     var _arr = self.$choosePro.find('.swiper-slide');
            //     if(_arr.length > 1 && _arr.length < 5) {
            //         self.$swiperPre.addClass('disN');
            //         self.$swiperNext.addClass('disN');
            //     }
            //     if(_arr.length == 1) {
            //         self.$selectedPro.addClass('disN');
            //     }
            // }

            self.addOrCancelWish(this, e);

            e.stopPropagation();
        })

        // 监听推荐商品部分是否添加取消心愿
        self.$recommendPro.on('click', '[data-list="wish"]', function(e) {
            self.addOrCancelWish(this, e);
            e.stopPropagation();
        })
    }

    $(function() {
        var template = Hogan.compile($('#brandtemplate').html());
        var b = new Brand('[data-index="brand"]', template)
        b.init();

        var feedback = new Feedback(b);
    })
})(window, document, jQuery)