var detail = null;
(function(window,document){
	detail = new Vue({
		el:'#detail',
		data:{
			// 提示框
            title:'详情页',//标题
            top:"top",//返回顶部
            isTop:false,//返回顶部是否显示
            wishList:[],//
            num:0,//心愿单数量 
            loading:false,//加载图标显示
            active:false,//红心默认false
            backActive:false,//返回按钮状态
            //头部
            url:'',
            callback:null,
            none:false//心显示
		},
		methods:{		
            //获取cookie值
            getCookie:function(){
                var self=this;
                var href=window.location.href;
                var productID=href.match(/product\/(\S*)\/m/)[1]   
                var cookieWish=cookie.get('_wish');
                if(cookieWish){
                    self.wishList=cookieWish.split(",");                   
                    self.num=self.wishList.length;
                    if(self.num==0){
                        this.none=false;
                    }else{
                        this.none=true;
                    }
                    for(var i=0;i<self.wishList.length;i++){
                        if(self.wishList[i]==productID){
                            self.active=true;
                            break;
                        }
                    }
                }else{
                    self.wishList=[];
                }
            },
            //红心
            choose:function(id){
                // 是否带红心
                var self=this;
                if(self.active==false){
                    self.active=true;
                    self.wishList.push(id);
                    self.num=self.wishList.length;
                    self.fly(event);
                }else{
                    for(var i=0;i<self.wishList.length;i++){
                        if(self.wishList[i]==id){
                            self.wishList.splice(i,1);
                        }
                    }
                    self.active=false;
                    self.num=self.wishList.length;
                }
                cookie.set('_wish',self.wishList,{path:'/'});
            },
            //飞入效果
            fly:function(event){
                var offset = $("#fly").offset();
                var offset1 = document.body.clientWidth;
                var flyer = $('<div style="color:red;font-weight:600;z-index:99999">+1</div>');
                flyer.fly({
                    start: {
                        left: event.pageX,
                        top: event.pageY
                    },
                    end: {
                        left:offset.left,
                        top: offset.top+10,
                        width:0,
                        height:0
                    },
                    speed: 0.6, //越大越快，默认1.2
                    vertex_Rtop:100, //运动轨迹最高点top值，默认20
                    onEnd: function(){
                        this.destory();
                    }    
                });
            },
            //返回
            goBack:function () {
              // 如果有回调,则先执行回调
              this.backActive=true;
              this.callback && this.callback();
              window.location.href = Util.isEemty(this.url) ? Util.urlPop() : this.url;
          },
		},
		created:function(){
            this.getCookie();   
	    },
        ready:function() {
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
            "num":function(val){
              if(val==0){
                this.none=false;
              }else{
                this.none=true;
              }
            }
        }
	});
})(window,document);