(function(){
	//返回上一页
  var goBack = Vue.extend({
      template:'#goback',
      props:{
          title:'',
          url:'',
          callback:null,
          num:0,
          none:false
      },
      data:function(){
        return {
          isShow: true,
          currUser:null,
        }
      },
      methods:{
          goBack:function () {
              // 如果有回调,则先执行回调
              this.callback && this.callback();
              window.location.href = Util.isEemty(this.url) ? Util.urlPop() : this.url;
          },
          go:function(){
            //跳转前保存路径
            Util.urlPush(window.location.href);
          }
      },
      created:function(){
        
      },
      watch:{
        
      }
  });

  var Confirm = Vue.extend({
    template: '#confirm',
    data: {
      ongoing: false // 防止连击
    },
    props: {
      confirm: {
        type: Object,
      }
    },
    methods: {
      // 取消
      cancel: function() {
        this.$nextTick(function() {
          if (this.ongoing) return false;
          this.ongoing = true;
          this.confirm.show = false;
          // 执行取消回调 并传入内容
          this.confirm.cancelFun && this.confirm.cancelFun();
        });
      },
      // 确定
      ok: function() {
        this.$nextTick(function() {
          if (this.ongoing) return false;
          this.ongoing = true;
          this.confirm.show = false;
          // 执行确认回调 并传入内容
          this.confirm.okFun && this.confirm.okFun();
        });
      }
    },
    watch: {
      // 弹窗显示恢复可点击效果
      'confirm.show': function(val) {
        if (val) {
          this.ongoing = false;
        }
      }
    }
  });

  var leftBar = Vue.extend({
      template:'#leftBar',
      props:{
          title:'',
          url:'',
          callback:null,
          cata:'',
          isShow:false,
          leftBtn:false
      },
      data:function(){
        return {
          //isShow: false,//左侧显示
          sure:false,//确定按钮颜色变化
          currUser:null,
          key:'',//搜索关键字
          searchshow:false,//
          searchList:[],//搜索关键字返回列表
          currTab: 'classify', // 当前激活的Tab页
          searchList:'',//搜索框返回列表
          brandLetter:[],//品牌字母
          brandList:[],//获取的品牌列表
          searchBrand:[],//选择需要查询的品牌
          searchBrandList:[],//最终的查询的品牌
          focusState:false,//input获取焦点显示关闭的按钮
          initList:[],//
          confirm:{
              text:'',
              cancalShow:false,
              okFun:null,
              cancelFun:null,
              show:false
          },
          
        }
      },
      methods:{
          //获取品牌
          getBrand:function(){
            var self = this;
            self.$http.get(ctx+'/api/repository/Brand/queryBrandForWeb?containCatalog=false').then(function(res) {
              if (res.data.code == "1") {
                //品牌/字母初始化
                self.initList=res.data.data||[]
                self.initList.forEach(function(val,index){
                  var obj={
                      letter:val.letter,
                      href:"#_"+val.letter,
                      brand:[]
                    }
                    val.brand.forEach(function(value){
                      var objBrand={
                        id:value.id,
                        en:value.en,
                        active:false,
                        name:value.name
                      }
                      obj.brand.push(objBrand)
                    })
                    self.brandLetter.push(obj)                      
                })
              }
            })
          },
          //
          tab:function(type) {
            this.currTab = type;
          },
          //选择品牌最多20()
          chooseBrand:function(index,itemID,listID){
              var self = this;         
              self.brandLetter.map(function(val,i){
                if(val.letter==itemID){
                  self.brandLetter[i].brand.map(function(v,iLitter){
                    if(v.id==listID){
                      v.active=!v.active;
                      if(self.searchBrandList.length>=20){
                        v.active=false       
                      }else{
                        self.searchBrandList.push(v.id)
                      } 
                      //去重
                      Array.prototype.unique = function(){
                        var res = [];
                        var json = {};
                        for(var i = 0; i < this.length; i++){
                          if(!json[this[i]]){
                            res.push(this[i]);
                            json[this[i]] = 1;
                          }
                        }
                        return res;
                      }
                      self.searchBrandList=self.searchBrandList.unique() 
                      if(v.active==false){
                        self.searchBrandList.map(function(val,ii){
                          if(val==v.id){
                            self.searchBrandList.splice(ii,1); 
                          }
                        })                      
                      }                                                    
                    }
                  })                 
                }
              })
              if(self.searchBrandList.length>=20){
                self.confirm.show=true;
                self.confirm.text="亲，最多只可以选择20个品牌呦！"
              }
          },
          //选择品牌确定
          go:function(){
            var self=this;
            if(self.searchBrandList=='') return false;
            window.location.href="/m/brandlist?ids="+self.searchBrandList.toString();
          },
          //重置
          reset:function(){
            var self=this;
            self.searchBrandList=[];
            self.brandLetter=[]
            self.initList.forEach(function(val,index){
                  var obj={
                      letter:val.letter,
                      href:"#_"+val.letter,
                      brand:[]
                    }
                    val.brand.forEach(function(value){
                      var objBrand={
                        id:value.id,
                        en:value.en,
                        active:false,
                        name:value.name
                      }
                      obj.brand.push(objBrand)
                    })
                    self.brandLetter.push(obj)                      
                })
          },
          choose:function(id) {
               window.location.href="/product/"+id+"/m"; 
          },
          searchKey:function(){
              window.location.href=ctx+"/m/brandlist?keyword="+this.key;
          },
          clear:function(){
            this.key='';
          }
      },
      created:function(){
        this.getBrand();
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
          },
          searchBrandList:function(val) {
            if(val.length>0){
              this.sure=true;
            }else{
              this.sure=false;
            }
          }
      }
  });
  //返回顶部
  var goTop = Vue.extend({
      template:'#goTop',
      props:{
          top:'top'
      },
      data:function(){
        return {
        }
      },
      methods:{
        scrollTop:function(){
          $('body,html').animate({scrollTop:0},1000);
        }
        // scrollTop: function(key) {
        //         var offset=$('#' + key).offset();
        //         $.fn.scrollTo =function(options){
        //             var defaults = {
        //                 toT : 0,    //滚动目标位置
        //                 durTime : 500,  //过渡动画时间
        //                 delay : 30,     //定时器时间
        //                 callback:null   //回调函数
        //             };
        //             var opts = $.extend(defaults,options),
        //                 timer = null,
        //                 _this = this,
        //                 curTop = _this.scrollTop(),//滚动条当前的位置
        //                 subTop = opts.toT - curTop,    //滚动条目标位置和当前位置的差值
        //                 index = 0,
        //                 dur = Math.round(opts.durTime / opts.delay),
        //                 smoothScroll = function(t){
        //                     index++;
        //                     var per = Math.round(subTop/dur);
        //                     if(index >= dur){
        //                         _this.scrollTop(t);
        //                         window.clearInterval(timer);
        //                         if(opts.callback && typeof opts.callback == 'function'){
        //                             opts.callback();
        //                         }
        //                         return;
        //                     }else{
        //                         _this.scrollTop(curTop + index*per);
        //                     }
        //                 };
        //             timer = window.setInterval(function(){
        //                 smoothScroll(opts.toT);
        //             }, opts.delay);
        //             return _this;
        //         };
        //         $("body").scrollTo({toT:offset.top});
        //     }, 
      },
      created:function(){
      }
  });
  Vue.component('go-top', goTop);
  Vue.component('left-bar', leftBar);
  Vue.component('confirm-window', Confirm);
  Vue.component('go-back', goBack);
})()