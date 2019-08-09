<!DOCTYPE html>
<html lang="en">
<head>
    <title>有梦管理后台</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${request.contextPath}/static/css/bootstrap.css">
    <link rel="stylesheet" href="https://unpkg.com/element-ui@2.4.8/lib/theme-chalk/index.css">
    <link rel="stylesheet" href="/taoshelf/static/layui/css/layui.css">
    <script src="${request.contextPath}/static/js/jquery.js"></script>
    <script src="${request.contextPath}/static/js/bootstrap.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/vue@2.5.17/dist/vue.min.js"></script>
    <script src="https://unpkg.com/element-ui@2.4.8/lib/index.js"></script>
    <script src="/taoshelf/static/layui/layui.js" charset="utf-8"></script>
    <script src="/taoshelf/static/js/jquery-form.js" charset="utf-8"></script>
    <style>
		input::-webkit-outer-spin-button,
		input::-webkit-inner-spin-button {
		    -webkit-appearance: none;
		}
    	input[type="number"] {
		    -moz-appearance: textfield;
		}
    </style>
</head>
<body>
<nav class="navbar bg-dark navbar-expand-md navbar-dark">
    <a class="navbar-brand" href="#">有梦管理后台</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="collapsibleNavbar">
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link" href="/taoshelf/8088/admin">运行监控</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/taoshelf/8088/user">用户管理</a>
            </li>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown">
                    卡密管理
                </a>
                <div class="dropdown-menu">
                    <a class="dropdown-item" href="/taoshelf/8088/card_new">未使用卡密</a>
                    <a class="dropdown-item" href="/taoshelf/8088/card_old">已使用卡密</a>
                </div>
            </li>
            <li class="nav-item dropdown active">
                <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown">流量包管理</a>
                <div class="dropdown-menu">
                    <a class="dropdown-item" href="/taoshelf/8088/showFlowPackages">查看流量包</a>
                    <a class="dropdown-item" href="/taoshelf/8088/createFlowPackage">创建流量包</a>
                </div>
            </li>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" data-toggle="dropdown">${admin.username}</a>
                <div class="dropdown-menu">
                    <a class="dropdown-item" href="/taoshelf/8088/password">修改密码</a>
                    <a class="dropdown-item" href="/taoshelf/8088/logout">安全退出</a>
                </div>
            </li>
        </ul>
    </div>
</nav>

<fieldset class="" style="width:200px;margin:0 auto;">
  <legend style="padding-top:25px;">创建流量包</legend>
</fieldset>

<form id="myForm" class="layui-form layui-form-pane" action="/taoshelf/8088/batchCreateFlowPackage" method="post">
	<div class="layui-form-item" style="width:310px;margin:0 auto;">
    	<label class="layui-form-label">价格</label>
    	<div class="layui-input-block" style="width:200px;">
      		<input type="number" name="price" placeholder="单位：元" autocomplete="off" required lay-verify="required" class="layui-input">
    	</div>
  	</div>
  	
  	<div class="layui-form-item" style="width:310px;margin:0 auto;">
    	<label class="layui-form-label" >流量大小</label>
    	<div class="layui-input-block" style="width:200px;">
      		<input type="number" name="flowMax" autocomplete="off" placeholder="单个流量包大小" required lay-verify="required" class="layui-input">
    	</div>
  	</div>
  	
  	<div class="layui-form-item" style="width:310px;margin:0 auto;">
    	<label class="layui-form-label">流量包数量</label>
    	<div class="layui-input-block" style="width:200px;">
      		<input type="number" name="count" autocomplete="off" placeholder="批量生成流量包" required lay-verify="required" class="layui-input">
    	</div>
  	</div>
  	<div class="layui-form-item" style="width:310px;margin:0 auto;">
    	<label class="layui-form-label">用户昵称</label>
    	<div class="layui-input-block" style="width:200px;">
      		<input type="text" name="nick" autocomplete="off" placeholder="将生成的流量包发送给用户" class="layui-input">
    	</div>
  	</div>
  	<div class="layui-form-item" style="width:310px;margin:0 auto;">
		<button class="layui-btn" lay-filter="formDemo" lay-submit="" style="width:310px;">创建</button>
	</div>
</form>
	
<script>
	layui.use('form', function(){
  	var form = layui.form;
  	//监听提交
  	form.on('submit(formDemo)', function(data){
    	$.post("/taoshelf/8088/batchCreateFlowPackage",data.field,
		function(data,status){
			layui.use('layer', function(){
  				var layer = layui.layer;
  				layer.msg(data,{
  					icon:1,
  					time:1500
  				}),function(){};
			});  
		});
    	return false;	//阻止表单跳转。如果需要表单跳转，去掉这段即可。
	  });
	});
</script>

</html>





