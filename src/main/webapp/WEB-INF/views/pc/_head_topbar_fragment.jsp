<%@ page contentType="text/html;charset=UTF-8" %>
<div class="top-bar">
    <div class="header-con">
        <div class="header-top clearfix">
            <a class="logo fl" href="${ctx}/"><img src="${ctx}/static/pc/images/logo.png" alt="logo图片" /></a>
            <div class="search fl">
                <form action="#" data-brand="golist">
                    <div class="search-form clearfix">
                        <input class="search-txt" data-brand="__search">
                        <a href="${ctx}/list" class="icon icon-search fr" data-brand="__tosearch">搜索</a>
                        <a href="javascript:;" class="icon icon-del-sh fr disN" data-brand="__todel">删除</a>
                    </div>
                </form>
                <ul class="search-result disN" data-brand="search-history">
                    <li><a href="javascript:void(0);">没有数据</a></li>
                </ul>
            </div>
            <ul class="top-bar-nav clearfix">
                <li><a href="http://www.rocoinfo.com" class="alink a-mr5" target="_blank">关于我们</a>|</li>
                <li><a href="${ctx}/feedback" class="alink"><i class="icon icon-wishlist"><i class="icon icon-num" data-iszero="_count" data-wish="_count">0</i></i>心愿单 </a></li>
            </ul>
        </div>
    </div>
</div>