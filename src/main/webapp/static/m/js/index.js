var index = null;
(function(window,document){
	index = new Vue({
		el:'#index',
		data:{
			// 提示框
            confirm:{
                text:'',
                cancalShow:true,
                okFun:null,
                cancelFun:null,
                show:false
            },
            top:"top",//返回顶部
            isTop:false,//返回顶部是否显示
            leftShow:false,
            key:'',//搜索关键字
            searchshow:false,//
            searchList:[],//搜索关键字返回列表
            bannerList:[],//banner图片
            wishList:[],//
            num:0,//心愿单数量
            isShow:false,//左侧导航显示
            focusState:false,//input获取焦点显示关闭的按钮
            none:false,
            leftBtn:true,//左侧按钮首页不出现
		},
		methods:{
            // 点击搜索列表的某条数据
            choose:function(id) {
               window.location.href="/product/"+id+"/m" 
            },
            getBanner:function(){
                var self = this;
                self.$http.post(ctx + '/api/repository/sysBanner/banners?type=WAP').then(function(res) {
                if (res.data.success == true) {
                    self.bannerList=res.data.data;
                    self.$nextTick(function() {
                        self.initSwiper();
                    });
                }
              })
            },
            initSwiper: function() {
                var mySwiper = new Swiper ('.swiper-container', {
                    direction: 'horizontal',
                    loop: true,
                    autoplay: 2000,
                    // 如果需要分页器
                    pagination: '.swiper-pagination',
                    paginationClickable: true,
                })
            },
            // 路径跳转前先保存当前路径
            go:function () {
                Util.urlPush(window.location.href);
            },
            searchKey:function(){
                window.location.href=ctx+"/m/brandlist?keyword="+this.key;
            },
            //获取cookie值
            getCookie:function(){
                var self=this;
                var cookieWish=cookie.get('_wish');
                if(cookieWish){
                    self.wishList=cookieWish.split(",");
                    self.num=self.wishList.length;
                    if(self.num==0){
                        this.none=false;
                    }else{
                        this.none=true;
                    }
                }else{
                    
                }
            },
            isS:function(){
                var self=this;
                self.isShow=!self.isShow;
            },
            clear:function(){
                this.key='';
            }
		},
		created:function(){
            this.getBanner();
            this.getCookie();
	    },
        ready:function(){
            var self = this;
            // 监听内容滚动区域
            document.addEventListener('scroll', function () {
                if(Util.getScrollTop()>100){
                    self.isTop=true;
                }else{
                    self.isTop=false;
                }
            });
        },
        watch:{
            key:function(val){
              var self = this;
              var send={
                keyword:val
              }
              if(val==''){
                self.focusState=false;
              }else{
                self.focusState=true;
              }
              if(val==''){
                return self.searchList=[];
              }
              self.$http.post(ctx + '/api/repository/Product/searchLike?keyword='+val).then(function(res) {
                if (res.data.success == true) {
                    self.searchList=[];              
                    self.searchList = res.data.data;
                    if(self.searchList.length>0){
                        self.searchshow=true;
                    }else{
                       self.searchshow=false;
                    }      
                }
              })
            }
        }
	});
})(window,document);