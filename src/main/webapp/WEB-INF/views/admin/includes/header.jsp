<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<script type="text/javascript">
	 var acctType = '<shiro:principal property="acctType"/>';
</script>
<!-- polyfill end-->
<script id="userScript">
    function null2Empty(val){
        if(val == 'null' || val == null)
            return '';
        return val;
    }
    window.DaMeiUser = {
        id: null2Empty('<shiro:principal property="id"/>'),
        name: null2Empty('<shiro:principal property="name" />'),
        supplierId: null2Empty('<shiro:principal property="supplierId" />'),
        storeCode: null2Empty('<shiro:principal property="storeCode" />'),
        roleNameList: null2Empty('<shiro:principal property="roleNameList" />'),
        permissionList: DaMeiUtils.permissionsFormat(null2Empty('<shiro:principal property="permissionList" />'))
    }
</script>
<div class="header fixed row border-bottom">
  <header class="navbar navbar-static-top"
          role="navigation" style="margin-bottom: 0">
    <div class="navbar-header">
      <div class="navbar-brand">
        <img src="/static/admin/img/logo.png" alt="rocoinfo,大诚若谷,logo">
      </div>

      <a id="toggleMenuMD"
         class="hidden-xs navbar-minimalize minimalize-styl-2 btn btn-primary" href="javascript:;">
        <i class="fa fa-bars"></i>
      </a>
      <a id="toggleMenuXS"
          href="javascript:;"
          data-clicked="false"
          class="hidden-sm hidden-lg hidden-md navbar-minimalize minimalize-styl-2 btn btn-primary pull-right">
        <i class="fa fa-bars"></i>
      </a>

       <ul class="nav navbar-top-links navbar-right">
       <!-- 	 
       <li>
          <a href="/admin/changePassword">
            	修改密码
          </a>
        </li> -->
       
        <li class="dropdown hidden-xs">
          <a href="/logout/admin" class="right-sidebar-toggle" aria-expanded="false">
            <i class="fa fa fa-sign-out"></i> 退出
          </a>
        </li>
         
      </ul> 
    </div>
  </header>
</div>

