// JavaScript Document

function goTop()
{
	$("#floating_window")
    $(window).scroll(function(e) {
        //若滚动条离顶部大于100元素
        if($(window).scrollTop()>100)
            $("#floating_window").fadeIn("slow");//以1秒的间隔渐显id=gotop的元素
        else
            $("#floating_window").fadeOut("slow");//以1秒的间隔渐隐id=gotop的元素
    });
};
$(function(){
	$("#floating_window").hide();
    //点击回到顶部的元素
    $("#gotop").click(function(e) {
            //以1秒的间隔返回顶部
            $('body,html').animate({scrollTop:0},1000);
    });

    goTop();//实现回到顶部元素的渐显与渐隐
});