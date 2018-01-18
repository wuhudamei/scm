/**
 * 用于检查一个对象是否包含在另外一个对象中
 * @method contains
 * @param {string} parentNode 父节点
 * @param {string} childNode 子节点
 */
function contains(parentNode, childNode) {
  if (parentNode.contains) {
    return parentNode != childNode && parentNode.contains(childNode);
  } else {
    return !!(parentNode.compareDocumentPosition(childNode) & 16);
  }
}
/**
 * 用于检查鼠标是否真正从外部移入或者移出对象的
 * @method checkHover
 * @param {string} e 当前的事件对象
 * @param {string} target 目标对象
 * @param {string} relatedTarget 属性代表的就是鼠标刚刚离开的那个节点，当触发mouseout事件时它代表的是鼠标移向的那个对象。
 * 由于MSIE不支持这个属性，不过它有代替的属性，分别是 fromElement和toElement
 */
function checkHover(e, target) {
  var rel = getEvent(e).relatedTarget,
    from = getEvent(e).fromElement,
    to = getEvent(e).toElement;
  if (getEvent(e).type == "mouseover") {
    return !contains(target, rel || from) && !((rel || from) === target);
  } else {
    return !contains(target, rel || to) && !((rel || to) === target);
  }
}
/**
 * 用于在MSIE或者FF下返回一个可用的event对象
 * @method getEvent
 * @param {string} e 当前的事件对象
 */
function getEvent(e) {
  return e || window.event;
}


//滚动条在Y轴上的滚动距离
getScrollTop = function(){
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
getScrollHeight = function(){
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
getWindowHeight = function(){
    var windowHeight = 0;
    if(document.compatMode == "CSS1Compat"){
        windowHeight = document.documentElement.clientHeight;
    }else{
        windowHeight = document.body.clientHeight;
    }
    return windowHeight;
}

// 解析查询参数
function parseQueryString(queryString) {
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
}