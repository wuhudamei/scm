<%@ page contentType="text/html;charset=UTF-8" %>
<header flex="cross:center box:justify" class="bar bar-search ">
    <a href="javascript:void(0);" flex="cross:center" class="bar-item">
        <img style="width: .75rem;" src="${ctx}/static/m/images/logo.png" alt="logo">
    </a>
    <div class="item-search">
        <div class="search clearfix" id="search">
            <form action="#" data-brand="golist">
                <div flex="cross:center" class="search-input">
                    <!--
                        注意：
                        1. 当 input获取焦点的时候， search-icon-front 的 class 为 「 hidden 」 隐藏搜索图标
                        2. 当 input获取焦点的时侯， search-icon 的 class 为 「 show 」显示关闭图标
                    -->
                    <input type="search" data-brand="__search" placeholder="搜索：关键词">
                    <label class="search-icon-front show "><svg class="svg-search"><use xlink:href="#svg-search"/></svg></label>
                    <label class="search-icon hidden" data-brand="__todel"><i class="icon icon-close">icon</i></label>
                </div>
            </form>
        </div>
        <!--/.search -->
        <!--显示class为「 disB 」-->
        <div class="list-search-result disN" data-brand="search-history">

        </div>
        <!--/.list-search-result-->
    </div>
    <a href="${ctx}/ipad/feedback" class="bar-item" flex="cross:center">
        <svg class="svg-wish"><use xlink:href="#svg-wish"/></svg>
        <span>心愿单</span>
        <div class="badge _red disN" data-iszero="_count" data-wish="_count">0</div>
    </a>
</header>