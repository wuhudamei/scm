(function() {
    var Util = window.Util = {};

    //把querystring 转换为对象
    Util.parseQueryString = function(queryString) {
        var params = {};
        var parts = queryString && queryString.split('&') || window.location.search.substr(1).split('\x26');

        for (var i = 0; i < parts.length; i++) {
            var keyValuePair = parts[i].split('=');
            var key = decodeURIComponent(keyValuePair[0]);
            var value = keyValuePair[1] ?
                decodeURIComponent(keyValuePair[1].replace(/\+/g, ' ')) :
                keyValuePair[1];

            switch (typeof(params[key])) {
                case 'undefined':
                    params[key] = value;
                    break; //first
                case 'array':
                    params[key].push(value);
                    break; //third or more
                default:
                    params[key] = [params[key], value]; // second
            }
        }
        return params;
    };

    // 至少包含url对象
    Util.parseObject = function(url, obj) {
        var ori = url;
        var search = '';
        if(url.indexOf('?') > 0) {
            search = url.substr(url.indexOf('?') + 1);
            ori = url.substr(0, url.indexOf('?'));
        }

        var o = obj;
        if(search && obj) {
            var searchObj = Util.parseQueryString(search);
            for(var key in obj) {
                searchObj[key] = obj[key];
            }
            o = searchObj;
        }
        var s = '';
        for(var key in o) {
            s += key + "=" + o[key] + "&";
        }

        return ori + "?" + s.substr(0, s.length - 1);

    };

    /*
     * formatNumber(data,type)
     * 功能：金额按千位逗号分割
     * 参数：data，需要格式化的金额或积分.
     * 参数：type,判断格式化后的金额是否需要小数位(如果为true,末尾带两位小数).
     * 返回：返回格式化后的数值字符串.
     */
    Util.formatNumber = function (data, type) {
        if (/[^0-9\.]/.test(data))
            return "0";
        if (data == null || data == "")
            return "0";
        data = data.toString().replace(/^(\d*)$/, "$1.");
        data = (data + "00").replace(/(\d*\.\d\d)\d*/, "$1");
        data = data.replace(".", ",");
        var re = /(\d)(\d{3},)/;
        while (re.test(data))
            data = data.replace(re, "$1,$2");
        data = data.replace(/,(\d\d)$/, ".$1");
        if (type == 0) {// 不带小数位(默认是有小数位)
            var a = data.split(".");
            if (a[1] == "00") {
                data = a[0];
            }
        }
        return data;
    }

    /*
     * 格式化时间
     * now:事件对象
     * fmt:时间格式 yyyy-MM-dd HH:mm:ss
     */
    Util.formatDate = function (now, fmt) {
        // $.formatDate(new Date(),'yyyy-MM-dd hh:mm:ss');
        var o = {
            "M+": now.getMonth() + 1, //月份
            "d+": now.getDate(), //日
            "h+": now.getHours(), //小时
            "m+": now.getMinutes(), //分
            "s+": now.getSeconds(), //秒
            "q+": Math.floor((now.getMonth() + 3) / 3), //季度
            "S": now.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (now.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }


    // 根据键值对设置会话数据
    /* 上页url键:preUrl
     * 用户类型:type
     *
     */
    Util.setStorge = function(key, value) {
        sessionStorage.setItem(key, value);
    };
    // 根据键值获取会话数据
    Util.getStorge = function(key) {
        return sessionStorage.getItem(key);
    };
    // 返回最后一个路径
    Util.urlPop = function() {
        var urlStr = Util.getStorge("history_url");
        if(Util.isEemty(urlStr) || urlStr == "[]") return '/m';
        var urlArr = $.parseJSON(urlStr);
        var backUrl = urlArr.pop();
        Util.setStorge('history_url', JSON.stringify(urlArr));
        return backUrl;
    };
     /**
     * 判断是否为空
     * 为空返回true 否则返回false
     */
    Util.isEemty = function (val) {
        if(val == undefined || val == null || val == 'undefined' || val == 'null' || val == '') {
            return true;
        }
        return false;
    }
    // 把路径添加到回退列表里面
    Util.urlPush = function(url) {
        var urlStr = Util.getStorge("history_url");
        var urlArr = [];
        if(urlStr) {
            urlArr = $.parseJSON(urlStr);
        }

        urlArr.push(url ? url : window.location.href);

        Util.setStorge('history_url', JSON.stringify(urlArr));
    };

    /**
     * 删除  sessionStorge 数据
     * @param key 根据键删除对应的数据,如果为空,则删除所有的sessionStorge数据
     */
    Util.clearStorge = function (key) {
        if(key) {
            sessionStorage.removeItem(key);
        }
        else {
            sessionStorage.clear();
        }
    }

    Util.setLocalStorge = function(key, value) {
        localStorage.setItem(key, value);
    };
    Util.getLocalStorge = function(key) {
        return localStorage.getItem(key);
    };

    /**
     * 删除  localStorage 数据
     * @param key 根据键删除对应的数据,如果为空,则删除所有的localStorage数据
     */
    Util.clearLocalStorge = function (key) {
        if(key) {
            localStorage.removeItem(key);
        }
        else localStorage.clear();
    }

    Util.getLastDay = function (month) {
        return new Date(new Date().getFullYear(),month,0).getDate();
    }
    //近index个月   index =year为当年
    Util.getTime = function (index) {
        var obj ={};
        var thisYear = new Date().getFullYear();
        var thisMonth = new Date().getMonth()+1;
        var m = null;
        var y = null;
        var d = null;
        if(index == 'year'){
            obj.startTime =  moment(thisYear.toString()+'0101').format('YYYY-MM-DD HH:mm');
            obj.endTime = moment((thisYear+1).toString()+'0101').format('YYYY-MM-DD HH:mm');
        }else{
            if(thisMonth < index){
                m = 12 - (index - thisMonth) + 1;
                y = thisYear-1;
            }else{
                m = thisMonth - index +1;
                y = thisYear;
            }
            d = this.getLastDay(thisMonth)
            m =(m<10 ? "0"+m:m);


            var st =(y.toString()+ m.toString() + '01');
            obj.startTime = moment(st).format('YYYY-MM-DD HH:mm');
            if(thisMonth == 12){          //如果当月为12月，处理方式改变
                thisYear = thisYear + 1;
                thisMonth = '01'; 
            }else{
                thisMonth = thisMonth +1;
                thisMonth =(thisMonth<10 ? "0"+thisMonth:thisMonth);
            }
            var endt = (thisYear.toString()  + thisMonth.toString() + '01');
            obj.endTime = moment(endt).format('YYYY-MM-DD HH:mm');
        }
        return obj;
    }
    // // 取得接口路径
    // Util.getUrl = function(key) {
    //     var url = "";
    //     // var baseUrl = window.location.origin;
    //     var baseUrl = "http://192.168.3.65:9933";
    //     switch(key) {
    //         case 'indexbanner': // 首页Banner
    //             url = "/Index/GetIndexBanners.do?jsoncallback=?";
    //             break;
    //         case 'indexflash': // 首页资讯
    //             url = "/Index/GetIndexNewsMessage.do?jsoncallback=?";
    //             break;

    //     }
    //     return baseUrl + url;
    // };

    //滚动条在Y轴上的滚动距离
    Util.getScrollTop = function(){
        var scrollTop = 0, bodyScrollTop = 0, documentScrollTop = 0;
        if(document.body){
            bodyScrollTop = document.body.scrollTop;
        }
        if(document.documentElement){
            documentScrollTop = document.documentElement.scrollTop;
        }
        scrollTop = (bodyScrollTop - documentScrollTop > 0) ? bodyScrollTop : documentScrollTop;
        return scrollTop;
    }
    //文档的总高度
    Util.getScrollHeight = function(){
        var scrollHeight = 0, bodyScrollHeight = 0, documentScrollHeight = 0;
        if(document.body){
            bodyScrollHeight = document.body.scrollHeight;
        }
        if(document.documentElement){
            documentScrollHeight = document.documentElement.scrollHeight;
        }
        scrollHeight = (bodyScrollHeight - documentScrollHeight > 0) ? bodyScrollHeight : documentScrollHeight;
        return scrollHeight;
    }
    //浏览器视口的高度
    Util.getWindowHeight = function(){
        var windowHeight = 0;
        if(document.compatMode == "CSS1Compat"){
            windowHeight = document.documentElement.clientHeight;
        }else{
            windowHeight = document.body.clientHeight;
        }
        return windowHeight;
    }

})();