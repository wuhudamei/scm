var selected = null;
(function(window,document){
	selected = new Vue({
		el:'#selected',
		data:{
            title:'已选商品',//标题
            top:"top",//返回顶部
            isTop:false,//返回顶部是否显示
			wishList:[],//
            num:0,//心愿单数量
            productList:[],//返回的产品
            s:'/static/m/images/material/p-334x340.jpg',
            none:false
		},
		methods:{
            //获取产品数据
            getList:function() {
                var self = this;
                // 发送的查询条件
                var sendData = {
                    ids:self.wishList
                };
                self.$http.get(ctx + '/api/repository/Product/selected', {params:sendData}).then(function(res) {
                    if(res.data.success == true) {
                        var product = res.data.data||[];
                        var len=product.length;
                        for(var i=0;i<len;i++){
                            product[i].active=true;
                            self.productList.push(product[i])
                        }
                    }
                });
            },
            //获取cookie
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
                self.getList();
            },
            //选择是否添加至心愿单
            cookieL:function(item,id,active,index){
                // 是否带红心
                var self=this
                if(item.active==false){
                    //self.productList.$set(index,$.extend({}, self.productList[index], {active:true}));
                    Vue.set(self.productList[index],'active',true);
                    self.wishList.push(id)
                    self.num=self.wishList.length;
                    self.fly(event);
                }else{
                    //self.productList.$set(index,$.extend({}, self.productList[index], {active:false}));
                    Vue.set(self.productList[index],'active',false);
                    for(var i=0;i<self.wishList.length;i++){
                        if(self.wishList[i]==id){
                            self.wishList.splice(i,1);
                        }
                    }
                    self.num=self.wishList.length;
                }
                cookie.set('_wish',self.wishList,{path:'/'});
            },
            //飞入效果
            fly:function(event){
                var offset = $("#top").offset();
                var offset1 = document.body.clientWidth;
                var flyer = $('<div style="color:red;font-weight:600;z-index:99999">+1</div>');
                flyer.fly({
                    start: {
                        left: event.pageX,
                        top: event.pageY-document.body.scrollTop
                    },
                    end: {
                        left:document.body.clientWidth-20,
                        top: 10,
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
            // 路径跳转前先保存当前路径
            go:function () {
                Util.urlPush(window.location.href);
            },
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
            self.getCookie();         
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