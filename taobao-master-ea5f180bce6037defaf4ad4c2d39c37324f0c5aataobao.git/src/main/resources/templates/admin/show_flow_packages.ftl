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
  <legend style="padding-top:25px;">流量包管理</legend>
</fieldset>

<table class="layui-table" id="demo" lay-filter="test">
</table>

<script type="text/html" id="barDemo">
  <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail">查看</a>
  <a class="layui-btn layui-btn-xs" lay-event="edit" style="color:white;">编辑</a>
  <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del" style="color:white;">删除</a>
</script>

<script th:inline="none">
layui.use('table', function(){
  var table = layui.table;
  //第一个实例
  table.render({
    elem: '#demo'
    ,url: '/taoshelf/flowPackage/getFlowPackage' 	//数据接口
    ,page: true 			//开启分页
    ,autoSort: false		//禁止自动排序
    ,cols: [[ 				//表头
      {type: 'checkbox', fixed: 'left'}
      ,{field: 'flowId', title: '密钥', width:'20%', fixed: 'left'}
      ,{field: 'price', title: '价格',width:'10%',sort: true}
      ,{field: 'flowMax', title: '流量大小',width:'10%',sort: true}
      ,{field: 'usageTime', title: '使用时间' ,width:'10%'} 
      ,{field: 'owner', title:'所属用户',width:'10%'}
      ,{fixed: 'right', width: 165, align:'center', toolbar: '#barDemo'}
    ]]
    ,toolbar: 'default' //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
    ,loading:true
    ,request:{
    	pageName:'current'
    }
    ,response:{
    	 statusName: 'code' //数据状态的字段名称，默认：code
	    ,statusCode: 200 	//成功的状态码，默认：0
	    ,msgName: 'msg' 	//状态信息的字段名称，默认：msg
	    ,countName: 'count' //数据总数的字段名称，默认：count
	    ,dataName: 'data' 	//数据列表的字段名称，默认：data
    } 
    ,done: function(res, curr, count){
	    //如果是异步请求数据方式，res即为你接口返回的信息。
	    //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
  	} 
  });
  
  table.on('sort(test)', function(obj){ 		//注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
	 
	  //尽管我们的 table 自带排序功能，但并没有请求服务端。
	  //有些时候，你可能需要根据当前排序的字段，重新向服务端发送请求，从而实现服务端排序，如：
	  table.reload('demo', {
	  	url: '/taoshelf/flowPackage/getFlowPackage'
	    ,autoSort: false			//禁止自动排序
	    ,initSort: obj 				//记录初始排序，如果不设的话，将无法标记表头的排序状态。
	    ,where: { 					//请求参数（注意：这里面的参数可任意定义，并非下面固定的格式）
	      field: obj.field 			//排序字段
	      ,order: obj.type 			//排序方式
	      ,owner:obj.owner 
	    }
	    ,request:{
    		pageName:'current'
	    }
	  });
  });
  
  //监听头工具栏事件
  table.on('toolbar(test)', function(obj){
    var checkStatus = table.checkStatus(obj.config.id),data = checkStatus.data; //获取选中的数据
    switch(obj.event){
      case 'add':
        window.location.href="/taoshelf/8088/createFlowPackage";
      	break;
      case 'update':
        if(data.length === 0){
          layer.msg('请选择一行');
        } else if(data.length > 1){
          layer.msg('只能同时编辑一个');
        } else {
          layer.alert('编辑 [id]：'+ checkStatus.data[0].id);
        }
      break;
      case 'delete':
        if(data.length === 0){
          layer.msg('请选择一行');
        } else {
          	$.post("/taoshelf/8088/batchDeleteFlowPackage",{flowIds:data},
				function(data,status){
					layui.use('layer', function(){
		  				var layer = layui.layer;
		  				layer.msg(data,{
		  					icon:1,
		  					time:1500
		  				}),function(){};
					},json);  
				});
          layer.msg('删除' + data[0].flowId);
        }
      break;
    };
  });
});
</script>
</html>





