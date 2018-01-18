<%@ page contentType="text/html;charset=UTF-8" %>
<header flex="cross:center box:justify" class="bar bar-search ">
    <a href="javascript:history.go(-1);" flex="cross:center" class="bar-item">
        <svg class="svg-arrow _left"><use xlink:href="#svg-arrow"/></svg>
        返回
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
                    <label class="search-icon hidden" data-brand="__todel"><i class="icon icon-close" data-brand="clearsearchtext">icon</i></label>
                </div>
            </form>
        </div>
        <!--/.search -->
        <!--显示class为「 disB 」-->
        <div class="list-search-result disB" data-brand="search-history">

        </div>
        <!--/.list-search-result-->
    </div>
    <a href="${ctx}/ipad/feedback" flex="cross:center" class="bar-item">
        <svg class="svg-wish"><use xlink:href="#svg-wish"/></svg>
        <span>心愿单</span>
        <div class="badge _red disN" data-iszero="_count" data-wish="_count">0</div>
    </a>
</header>