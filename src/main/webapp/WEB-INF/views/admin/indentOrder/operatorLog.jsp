<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<title>已对账</title>
<meta name="keywords" content="">
<meta name="description" content="">
<style>
</style>
</head>
<body id="app" class="fixed-sidebar full-height-layout gray-bg">
<div id="container" class="wrapper wrapper-content" v-cloak>
    <div id="breadcrumb">
        <bread-crumb :crumbs="breadcrumbs"></bread-crumb>
    </div>
    <!-- ibox start -->
    <div class="ibox">
        <div class="ibox-content">
            <div>
                <div class="row">
                    <form id="searchForm" @submit.prevent="query">

                        <div class="col-sm-2">
                            <div class="form-group" style="line-height: 34px">
                                    <input
                                            v-model="form.keyword"
                                            id="keyword"
                                            name="keyword"
                                            type="text"
                                            placeholder="订单编号/项目编号" class="form-control"/>
                                </div>
                        </div>

                        <div class="col-md-1" >
                            <div class="form-group" >
                                <button id="searchBtn" type="submit"
                                        class="btn btn-block btn-outline btn-default" alt="搜索"
                                        title="搜索">
                                    <i class="fa fa-search"></i>
                                </button>
                            </div>
                        </div>

                    </form>
                </div>
                <div>
                    <table v-el:dataTable id="dataTable" width="100%"
                           class="table table-striped table-bordered table-hover">
                    </table>
                </div>
    </div>
    <!-- ibox end -->
</div>


</div>
<script src="/static/admin/js/apps/indentOrder/operatorLog.js"></script>
</body>
</html>