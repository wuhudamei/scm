<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta content="telephone=no" name="format-detection"/>
    <meta content="email=no" name="format-detection" />
    <title>500错误</title>
    <meta name="keywords" content="">
    <meta name="description" content="">

    <link href="${ctx}/static/hplus/css/bootstrap.min.css?v=ea38b0f7" rel="stylesheet">
    <link href="${ctx}/static/hplus/font-awesome/css/font-awesome.css?v=ea38b0f7" rel="stylesheet">

    <link href="${ctx}/static/hplus/css/animate.css?v=ea38b0f7" rel="stylesheet">
    <link href="${ctx}/static/hplus/css/style.css?v=ea38b0f7" rel="stylesheet">

</head>

<body class="gray-bg">
    <div class="middle-box text-center animated fadeInDown">
        <h1>500</h1>
        <h3 class="font-bold">服务器内部错误</h3>

        <div class="error-desc">
            服务器好像出错了...
            <br/>您可以返回主页看看
            <br/><a href="${ctx}/" class="btn btn-orange m-t">主页</a>
        </div>
    </div>
</body>

</html>