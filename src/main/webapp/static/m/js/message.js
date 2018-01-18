var message = null;
(function(window,document){
	message = new Vue({
		el:'#message',
		data:{
            title:'留言',//
			// 提示框
            top:"top",// 
            confirm:{
                text:'',
                cancalShow:false,
                okFun:null,
                cancelFun:null,
                show:false
            },
            name:'',//姓名
            phone:'',//搜索关键字
            email:'',//邮箱
            company:'',//搜索关键字返回列表
            content:'',//留言
            department:'',//部门
            other:'',//其他
            subDepartment:'',//提交时候的部门信息
            nameError:false,//姓名不对显示提示
            phoneError:false,//电话不对显示提示
            companyError:false,//公司为空时显示提示
            emailError:false,//邮箱错误显示提示
            wishList:[],//添加心愿单cookie的id
            num:0,//心愿单数量
            likeList:[],//猜你喜欢
            selectedList:[],//选中宝贝
            selectedShow:false,//中意的宝贝是否显示
            selectedMoreShow:false,//中意宝贝更多显示
            s:'/static/m/images/material/p-334x340.jpg',
            btnState:false,//确定按钮颜色变化
            none:false
		},
		methods:{
            // 检查数据是否填写完整 完整返回 true 否则 false
            check: function() {
                var self = this;
                var allParam=true;
                //判断姓名
                if(self.name== '') {
                    self.nameError = true;
                    allParam=false;
                }else{
                    self.nameError = false;
                }
                //判断电话
                if(self.phone== ''||!(/^(((0\d{2,3}\-)?\d{7,8}(\-\d{3})?)|(1[35784]\d{9}))$/.test(self.phone))) {
                    self.phoneError = true;
                    allParam=false;
                }else{
                    self.phoneError = false;
                }
                //判断邮箱
                if(self.email!=''){
                    if(!(/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/.test(self.email))) {
                        self.emailError = true;
                        allParam=false;
                    }else{
                        self.emailError = false;
                    }
                }else{
                   self.emailError = false; 
                }
                //判断公司
                if(self.company== '') {
                    self.companyError = true;
                    allParam=false;
                }else{
                    self.companyError = false;
                }
                if(allParam){
                    return true;
                }else{
                    return false;
                }
                
            },		
            // 提交
            submit:function() {
                var self=this;
                if(self.check()){
                    self.subDepartment=(self.department == '其它' )? self.other : self.department;
                    var sendDate={
                       name:self.name,
                       phone:self.phone,
                       email:self.email,
                       companyName:self.company,
                       subDepartment:self.subDepartment,
                       leaveMsg:self.content,
                       other:self.other,
                       department:self.department
                    }
                    var needProductList = '';
                    self.wishList.forEach(function(item, idx){
                        needProductList += 'needProductList['+idx+'].productId='+item+ '&';
                    })
                    needProductList = needProductList.substring(0, needProductList.length-1);
                    sendData="name="+self.name+"&phone="+self.phone+"&email="+self.email+"&companyName="+self.company;
                    sendData+="&department="+self.subDepartment;
                    sendData+="&leaveMsg="+self.content+"&"+needProductList;
                    self.$http.post(ctx + '/api/repository/Massage/saveMassage?'+sendData).then(function(res) {
                        if (res.data.success == true) {
                            self.confirm.show=true;
                            self.confirm.text='您的留言已提交成功,我们会尽快与您联系';
                            cookie.set('_message',JSON.stringify(sendDate));
                            self.confirm.okFun=function(){
                                window.location.href=ctx+"/m"
                            }  
                        }else{
                            self.confirm.show=true;
                            self.confirm.text=res.data.message;
                        }
                    })
                }
            },
            //重置
            reset:function(){
                var self=this;
                //内容清空
                self.name='';
                self.phone='';
                self.email='';
                self.company='';
                self.content='';
                self.other='';
                self.department='';
                //状态重置
                self.nameError=false;
                self.phoneError=false;
                self.companyError=false;
                self.emailError=false;

                var sendDate={
                       name:'',
                       phone:'',
                       email:'',
                       companyName:'',
                       subDepartment:'',
                       leaveMsg:'',
                       other:'',
                       department:''
                }
                cookie.set('_message',JSON.stringify(sendDate))
            },
            //推荐
            like:function(){
                var self=this;
                var sendData={
                   pageIndex:0,
                   pageSize:7,
                   excludeIds:self.wishList 
                }
                self.$http.get(ctx + '/api/repository/Product/recommend',{params:sendData}).then(function(res) {
                    if(res.data.success == true){
                        if(res.data.data.rows.length<=4){
                           self.likeList=res.data.data.rows; 
                        }else{
                           for(var i=0;i<4;i++){
                            res.data.data.rows[i].active=false;
                            self.likeList.push(res.data.data.rows[i])
                           }
                        }
                    }else{
                    }
                })
            },
            //中意的宝贝
            selectedProducts:function(){
                var self = this;
                var sendData={
                   ids:self.wishList 
                }
                self.$http.get(ctx + '/api/repository/Product/selected',{params:sendData}).then(function(res) {
                if (res.data.success == true) {
                    var arr=res.data.data||[];
                    var len=arr.length;
                    if(len>7){
                        for(var i=0;i<7;i++){
                            arr[i].active=true;
                            self.selectedList.push(arr[i]);                           
                        }
                        self.selectedMoreShow=true;
                    }else{
                        for(var i=0;i<len;i++){
                            arr[i].active=true;
                            self.selectedList.push(arr[i]);
                        }
                        self.selectedMoreShow=false;
                    }
                    self.$nextTick(function() {
                        self.initSwiper();
                    });
                }
              })
            },
            //重新加载
            initSwiper: function() {
                var mySwiper = new Swiper ('.swiper-container-love', {
                    slidesPerView: 2.9,
                    paginationClickable: true,
                    spaceBetween : 10,
                    slidesOffsetBefore : 10,
                    slidesOffsetAfter : 10
                })
            },
            //推荐添加至心愿单
            cookieL:function(item,id,active,index){
                // 是否带红心
                var self=this
                if(item.active==false){
                    //self.likeList.$set(index,$.extend({}, self.likeList[index], {active:true}));
                    Vue.set(self.likeList[index],'active',true);
                    self.wishList.push(id)
                    self.num=self.wishList.length;
                    self.fly(event);
                }else{
                    //self.likeList.$set(index,$.extend({}, self.likeList[index], {active:false}));
                    Vue.set(self.likeList[index],'active',false);
                    for(var i=0;i<self.wishList.length;i++){
                        if(self.wishList[i]==id){
                            self.wishList.splice(i,1);
                        }
                    }
                    self.num=self.wishList.length;
                }
                cookie.set('_wish',self.wishList,{path:'/'});
            },
            //中意的宝贝添加删除
            cookieR:function(item,id,active,index){
                // 是否带红心
                var self=this
                if(item.active==false){
                    //self.selectedList.$set(index,$.extend({}, self.selectedList[index], {active:true}));
                    Vue.set(self.selectedList[index],'active',true);
                    self.wishList.push(id)
                    self.num=self.wishList.length;
                    self.fly(event);
                }else{
                    //self.selectedList.$set(index,$.extend({}, self.selectedList[index], {active:false}));
                    Vue.set(self.selectedList[index],'active',false);
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
                var cookieWish=cookie.get('_wish');
                var _message=cookie.get('_message');
                if(cookieWish){
                    self.wishList=cookieWish.split(",");
                    self.num=self.wishList.length;
                    if(self.num==0){
                        this.none=false;
                    }else{
                        this.none=true;
                    }
                    self.selectedShow=true;
                    self.selectedProducts();//
                }else{
                    self.selectedShow=false; 
                }
                //留言信息回显
                if(_message){
                    _message=JSON.parse(_message);
                    self.name=_message.name;
                    self.phone=_message.phone;
                    self.email=_message.email;
                    self.company=_message.companyName;
                    self.content=_message.leaveMsg;
                    self.department=_message.department;
                    if(_message.department=="其它"){
                        self.other=_message.other;
                    }
                }  
            },
            // 路径跳转前先保存当前路径
            go:function () {
                Util.urlPush(window.location.href);
            },
            inputVal:function(){
                if(this.name==''||this.phone==''||this.company==''){
                    this.btnState=false;
                }else{
                    this.btnState=true; 
                }
            },
            phoneEp:function(){
                var self=this;
                if(self.phone== ''||!(/^(((0\d{2,3}\-)?\d{7,8}(\-\d{3})?)|(1[35784]\d{9}))$/.test(self.phone))) {
                    self.phoneError = true;
                    allParam=false;
                }else{
                    self.phoneError = false;
                }
            },
            emailEp:function(){
                var self=this;
                if(self.email!=''){
                    if(!(/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/.test(self.email))) {
                        self.emailError = true;
                        allParam=false;
                    }else{
                        self.emailError = false;
                    }
                }else{
                   self.emailError = false; 
                }
            }
		},
		created:function(){
            this.getCookie();
	    },
        ready:function(){
            var self=this;
            self.like();// 
        },
        watch:{
            name:function(val){
                this.inputVal();
                if(val!=''){
                    this.nameError = false;
                }
            },
            phone:function(val){
                this.inputVal();
                if((/^(((0\d{2,3}\-)?\d{7,8}(\-\d{3})?)|(1[35784]\d{9}))$/.test(val))) {
                    this.phoneError =false;
                }              
            },
            email:function(val){
                this.inputVal();
                if((/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/.test(val))) {
                    this.emailError = false;
                }
            },
            company:function(val){
                this.inputVal();
                if(val!=''){
                    this.companyError = false;
                }
            },
            content:function(val){
                this.inputVal();
            },
            department:function(val){
                this.inputVal();
            },
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