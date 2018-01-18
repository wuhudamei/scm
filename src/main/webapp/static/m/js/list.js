var list = null;
(function(window,document){
	list = new Vue({
		el:'#list',
		data:{
            title:'',//标题
            top:"top",//返回顶部
            isTop:false,//返回顶部是否显示
            param:null, // 浏览器参数
            cata:'',//左导航当前分类
            brandshow:false,//品牌选择框显示
            priceshow:false,//价格选择显示
            attentionShow:false,//关注度显示
            brandLetter:[],//品牌字母
            brand:[],//品牌列表
            price:[{
                startPrice:0,
                endPrice:100,
                active:false,
                text:'0-100'
            },{
                startPrice:101,
                endPrice:200,
                active:false,
                text:'101-200'
            },{
                startPrice:201,
                endPrice:500,
                active:false,
                text:'201-500'
            },{
                startPrice:501,
                endPrice:1000,
                active:false,
                text:'501-1000'
            },{
                startPrice:1001,
                endPrice:99999999,
                active:false,
                text:'1000以上'
            },{
                startPrice:0,
                endPrice:99999999,
                active:false,
                text:'全部'
            }],
            leftShow:false,
            keyword:'',//搜索关键字
            searchshow:false,//
            startPrice:'',//开始价格
            endPrice:'',//结束价格
            offset:0,//偏移量
            limit:8,//限制量
            total:0,//总条数
            catalogId:'',//分类ID
            orderSort:'DESC',//升降序
            sortShow:false,//升降序显示
            sort:true,
            orderColumn:'fake_attention',//排序列关注度
            brandIds:'',//品牌列表
            productList:[],//返回产品列表
            productListLeft:[],//返回产品列表左页面
            productListRight:[],//返回产品列表右页面
            wishList:[],//
            num:0,//心愿单数量
            loading:false,//加载图标显示
            emptyList:[],//
            notFound:false,//没有找到显示
            currentList:"default",//初始加载默认的搜索列表，如果没有加载推荐列表“recommend”
            s:'/static/m/images/material/p-334x340.jpg',
            //推荐查询参数
            pageIndex:0,//页数
            pageSize:8,//每次取的个数
            recommendTotal:0,//返回数据的总页数
            none:false,
            end:false//加载到底部
		},
		methods:{
            //价格和品牌显示/关注度、价格排序
            show:function(val){
                var self=this;
                switch (val){
                    case "brand"://品牌
                    self.priceshow=false;
                    self.attentionShow=false;
                    self.sortShow=false;
                    self.brandshow=!self.brandshow;
                    break;
                    case "price"://价格
                    self.brandshow=false;
                    self.attentionShow=false;
                    self.sortShow=false;
                    self.priceshow=!self.priceshow;
                    break;
                    case "attention"://关注度
                    self.priceshow=false;
                    self.brandshow=false;
                    self.sortShow=false;
                    self.attentionShow=true;
                    self.orderColumn="fake_attention";
                    self.orderSort= self.orderSort == 'ASC' ? 'DESC' : 'ASC';
                    self.productList=[];
                    self.offset=0;//偏移量
                    self.limit=8;//限制量
                    self.total=0;//总条数
                    self.productListLeft=[];//返回产品列表左页面
                    self.productListRight=[];//返回产品列表右页面

                    self.pageIndex=0;//推荐复位
                    self.pageSize=8;//推荐复位
                    self.recommendTotal=0;//推荐复位
                    self.end=false;//加载到底部取消
                    self.getList();
                    break;
                    case "sort"://升降序
                    self.priceshow=false;
                    self.brandshow=false;
                    self.orderColumn="market_price";
                    self.productList=[];
                    self.attentionShow=false;
                    self.offset=0;//偏移量
                    self.limit=8;//限制量
                    self.total=0;//总条数
                    self.productListLeft=[];//返回产品列表左页面
                    self.productListRight=[];//返回产品列表右页面
                    if(self.sortShow){
                        self.orderSort="DESC";
                        self.sortShow=false;
                    }else{
                        self.orderSort="ASC";
                        self.sortShow=true;
                    }
                    self.pageIndex=0;//推荐复位
                    self.pageSize=8;//推荐复位
                    self.recommendTotal=0;//推荐复位
                    self.end=false;//加载到底部取消
                    self.getList();
                }
            },
            //获取品牌列表
            brandList:function(){
                var self = this;
                self.param = Util.parseQueryString();
                self.cata=self.param.cata;
                self.$http.post(ctx+'/api/repository/Brand/queryBrandForWeb?containCatalog=false&catalogId='+self.cata).then(function(res) {
               if (res.data.success == true) {
                    //品牌字母初始化
                    var arr=res.data.data||[];
                    res.data.data.forEach(function(val,index){
                        var obj={
                            letter:val.letter,
                            active:index==0?true:false,
                            brand:val.brand
                        }
                        self.brandLetter.push(obj)
                    })
                    //品牌初始化
                    self.brandLetter[0].brand.forEach(function(val,index){
                        var obj={
                            id:val.id,
                            active:false,
                            en:val.en,
                            name:val.name
                        }
                        self.brand.push(obj)
                    })
                }
              })
            },
            //品牌字母选择
            chooseletter:function(index,val){
                var self = this;
                self.brandLetter.map(function(v,i){
                    if(i == index){
                        v.active = true;
                        self.brand=[]
                        v.brand.forEach(function(val,index){
                        var obj={
                            id:val.id,
                            active:false,
                            en:val.en,
                            name:val.name
                        }
                        self.brand.push(obj)
                    })
                    }else{
                        v.active = false;
                    }
                })
            },
            //品牌选择
            chooseBrand:function(index,id){
                var self = this;
                self.brand.map(function(v,i){
                    if(i == index){
                        v.active = true;
                        self.brandIds=id;
                        self.productList=[];
                        self.offset=0;//偏移量
                        self.limit=8;//限制量
                        self.total=0;//总条数
                        self.productListLeft=[];//返回产品列表左页面
                        self.productListRight=[];//返回产品列表右页面
                        self.brandshow=false;//关闭选择项

                        self.pageIndex=0;//推荐复位
                        self.pageSize=8;//推荐复位
                        self.recommendTotal=0;//推荐复位
                        self.end=false;//加载到底部取消
                        self.getList();
                    }else{
                        v.active = false;
                    }
                })
            },
            choosePrice:function(index,startPrice,endPrice){
                var self = this;
                self.price.map(function(v,i){
                    if(i == index){
                        v.active = true;
                        self.startPrice=startPrice;
                        self.endPrice=endPrice;
                        self.productList=[];
                        self.offset=0;//偏移量
                        self.limit=8;//限制量
                        self.total=0;//总条数
                        self.productListLeft=[];//返回产品列表左页面
                        self.productListRight=[];//返回产品列表右页面
                        self.priceshow=false;//关闭选择项

                        self.pageIndex=0;//推荐复位
                        self.pageSize=8;//推荐复位
                        self.recommendTotal=0;//推荐复位
                        self.end=false;//加载到底部取消
                        self.getList();
                    }else{
                        v.active = false;
                    }
                })
            },
            titleList:function(){
                var self=this;
                switch (self.param.cata){
                    case "1":
                    return self.title="生活电器";
                    break;
                    case "2":
                    return self.title="厨房电器";
                    break;
                    case "3":
                    return self.title="个护电器";
                    break;
                    case "4":
                    return self.title="家居生活";
                    break;
                    case "5":
                    return self.title="厨房用品";
                    break;
                    case "6":
                    return self.title="商旅箱包";
                    break;
                    case "7":
                    return self.title="运动户外";
                    break;
                    case "8":
                    return self.title="电子数码";
                    break;
                    case "9":
                    return self.title="汽车用品";
                    break;
                    case "10":
                    return self.title="母婴儿童";
                    break;
                    case "11":
                    return self.title="时尚精品";
                    break;
                    case "12":
                    return self.title="食品卡券";
                    break;
                    case "13":
                    return self.title="虚拟商品";
                    break;
                }
            },
            //获取产品数据
            getList:function() {
                var self = this;
                // 发送的查询条件
                var arr=[];
                arr.push(self.brandIds);//转成数组
                var sendData = {
                    limit:self.limit,
                    offset:self.offset,
                    keyword:self.keyword,
                    startPrice:self.startPrice,
                    endPrice:self.endPrice,
                    catalogId:self.param.cata,//分类id
                    brandIds:arr,
                    orderColumn:self.orderColumn,
                    orderSort:self.orderSort
                };
                if(self.total < self.offset){
                    self.end=true;
                    return false;
                }
                self.loading=true;
                self.$http.get(ctx + '/api/repository/Product/weblist', {params:sendData}).then(function(res) {
                    if(res.data.code == 1) {
                        self.productList = res.data.data.rows;
                        if(self.productList.length>0){
                            self.notFound=false;
                            self.currentList="default";
                            //初始化数据把id转成字符串，加上active=false
                        for(var n=0;n<self.productList.length;n++){
                            self.productList[n].id=self.productList[n].id.toString();
                            self.productList[n].active=false;
                        }
                        //获取cookie如果有active为TRUE
                        var len=self.wishList.length;
                        if(len>0){
                            for(var j=0;j<len;j++){
                                for(var m=0;m<self.productList.length;m++){
                                    if(self.wishList[j]==self.productList[m].id){
                                        self.productList[m].active=true;
                                    }
                                }
                            }
                        }
                        //左右列分配
                        for(var i=0;i<self.productList.length;i++){
                            if(i%2===0){
                                self.productListLeft.push(self.productList[i])
                            }else{
                                self.productListRight.push(self.productList[i])
                            }
                        }
                        // 处理当前页是总页数
                        self.total = res.data.data.total;
                        self.offset += self.limit;
                        self.$nextTick(function() {
                            self.loading=false;
                        });

                        }else{
                            //找不到给推荐
                            self.notFound=true;
                            self.currentList="recommend";
                            self.end=false;//加载到底部取消
                            self.recommend();
                        }

                    }
                });
            },
            cookieL:function(item,id,active,index){
                // 是否带红心
                var self=this
                if(item.active==false){
                    ///console.log(self.productListLeft[0].active)
                    self.productListLeft.$set(index,$.extend({}, self.productListLeft[index], {active:true}));
                    //console.log(self.productListLeft[0].active)
                    self.wishList.push(id)
                    self.num=self.wishList.length;
                    self.fly(event);
                }else{
                    self.productListLeft.$set(index,$.extend({}, self.productListLeft[index], {active:false}));
                    //self.productListLeft[index].active=false;
                    for(var i=0;i<self.wishList.length;i++){
                        if(self.wishList[i]==id){
                            self.wishList.splice(i,1);
                        }
                    }
                    self.num=self.wishList.length;
                }
                cookie.set('_wish',self.wishList,{path:'/'});
            },
            cookieR:function(item,id,active,index){
                // 是否带红心.根据active的状态存储
                var self=this
                if(item.active==false){
                    self.productListRight.$set(index,$.extend({}, self.productListRight[index], {active:true}));
                    self.wishList.push(id);
                    self.num=self.wishList.length;

                    self.fly(event);
                }else{
                    self.productListRight.$set(index,$.extend({}, self.productListRight[index], {active:false}));
                    //self.productListRight[index].active=false;
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
            //获取cookie值
            getCookie:function(){
                var self=this;
                self.param = Util.parseQueryString();
                self.cata=self.param.cata;
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
                    self.wishList=[];
                }
                self.$nextTick(function() {
                    self.getList();
                });
            },
            // 路径跳转前先保存当前路径
            go:function () {
                Util.urlPush(window.location.href);
            },
            //推荐
            recommend:function() {
                var self = this;
                // 发送的查询条件
                var sendData = {
                    pageIndex:self.pageIndex,
                    pageSize:self.pageSize,
                    excludeIds:self.wishList
                };
                if(self.recommendTotal < self.pageIndex){
                    self.end=true;//
                    return false;
                }
                self.loading=true;
                self.$http.get(ctx + '/api/repository/Product/recommend', {params:sendData}).then(function(res) {
                    if(res.data.success == true) {
                        self.productList = res.data.data.rows;
                        for(var n=0;n<self.productList.length;n++){
                            self.productList[n].id=self.productList[n].id.toString();
                            self.productList[n].active=false;
                        }
                        //获取cookie如果有active为TRUE
                        var len=self.wishList.length;
                        if(len>0){
                            for(var j=0;j<len;j++){
                                for(var m=0;m<self.productList.length;m++){
                                    if(self.wishList[j]==self.productList[m].id){
                                        self.productList[m].active=true;
                                    }
                                }
                            }
                        }
                        //左右列分配
                        for(var i=0;i<self.productList.length;i++){
                            if(i%2===0){
                                self.productListLeft.push(self.productList[i])
                            }else{
                                self.productListRight.push(self.productList[i])
                            }
                        }

                        // 处理当前页是总页数
                        self.recommendTotal = res.data.data.total;
                        self.pageIndex++;
                        self.$nextTick(function() {
                            self.loading=false;
                        });
                    }
                });
            },
            loadMore:function(){
                var self = this;
                switch (self.currentList){
                    case "default":
                    self.getList();
                    break;
                    case "recommend":
                    self.recommend();
                    break;
                }
            }
		},
		created:function(){
            var self=this;
           this.brandList();
           this.getCookie();
	    },
        ready:function() {
            var self = this;
            // 监听内容滚动区域
            document.addEventListener('scroll', function () {
                clearTimeout(self.timer);
                self.timer = setTimeout(function () {
                    if((Util.getScrollTop() + Util.getWindowHeight()) >= (Util.getScrollHeight() - 100)){
                        self.loadMore();
                    }
                }, 500);
                if(Util.getScrollTop()>100){
                    self.isTop=true;
                }else{
                    self.isTop=false;
                }
            });

            self.titleList();
            //self.getCookie();
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