<!DOCTYPE html>
<html lang="en">
<head>
    <title>流量包详情</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="/taoshelf/static/css/bootstrap.css">
    <link rel="stylesheet" href="https://unpkg.com/element-ui@2.4.8/lib/theme-chalk/index.css">
    <link rel="stylesheet" href="/taoshelf/static/layui/css/layui.css">
    <style>
    </style>
    <script src="/taoshelf/static/js/jquery.js"></script>
    <script src="/taoshelf/static/js/bootstrap.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/vue@2.5.17/dist/vue.min.js"></script>
    <script src="https://unpkg.com/element-ui@2.4.8/lib/index.js"></script>
    <script src="/taoshelf/static/layui/layui.js" charset="utf-8"></script>
</head>
<body>
<nav class="navbar bg-dark navbar-expand-md navbar-dark">
    <a class="navbar-brand" href="/taoshelf/home">有梦循环上下架</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="collapsibleNavbar">
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link" href="/taoshelf/home">首页</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/taoshelf/task">任务管理</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/taoshelf/tasklog/tasklog" >日志信息</a>
            </li>
            <li class="nav-item">
                <a class="nav-link active" href="/taoshelf/flowPackage/flowPackage">流量包信息</a>
            </li>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown">
                    账户
                </a>
                <div class="dropdown-menu">
                    <a class="dropdown-item" href="#">${user.nick}</a>
                    <a class="dropdown-item" href="#">到期时间：${user.endTime}</a>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item" href="/taoshelf/recharge">账户充值</a>
                </div>
            </li>
        </ul>
    </div>
</nav>

<fieldset class="layui-elem-field layui-field-title" style="margin-top: 50px;">
  <legend>可用流量：${usableFlow}</legend>
  <legend>流量包详情</legend>
  </br>
  <legend><button id="getFlowPackage" data-method="confirmTrans" class="layui-btn layui-btn-radius">获取流量包</button></legend>
</fieldset> 

<!-- 表格 -->
<table class="layui-table" id="demo" lay-filter="test">
</table>
    

</body>

<script>
</script>

<script type="text/html" id="barDemo">
	{{# if(d.usageTime === undefined){ }}
  		<!-- <a href="/taoshelf/flowPackage/useFlowPackage?flowId={{d.flowId}}&nick=${user.nick}" class="layui-btn layui-btn-sm" lay-event="edit" style="color:white;margin:auto;width:100%;">使用</a>-->
		<button class="layui-btn layui-btn-sm" lay-submit="" lay-filter="add" style="color:white;margin:auto;width:100%;" onclick="usageFlowPackage('{{d.flowId}}','${user.nick}');">使用</button>
	{{# }else{ }}
		{{d.usageTime}}
	{{# } }}
</script>

<script>
	function usageFlowPackage(flowId,nick){
		$.get("/taoshelf/flowPackage/useFlowPackage",{
			flowId:flowId,
			nick:nick
		},
		function(data,status){
			var icon;
			if(data == 1){
				data = "流量包充值成功"
				icon = 1;
			}else{
				data = "流量包充值失败"
				icon = 2;
			}
			layui.use('layer', function(){
  				var layer = layui.layer;
  				layer.msg(data,{
  					icon:icon,
  					time:1500
  				}),function(){};
  				setTimeout(function(){
  					window.location.href="/taoshelf/flowPackage/flowPackage";
  				},"1500");
			});  
		});
	}
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
    ,where:{owner:'${user.nick}'} 
    ,cols: [[ 				//表头
      {field: 'flowId', title: '密钥', width:'20%', fixed: 'left'}
      ,{field: 'price', title: '价格',width:'10%',sort: true}
      ,{field: 'flowMax', title: '流量大小',width:'10%',sort: true}
      ,{field: 'getTime', title: '获取时间',width:'15%'} 
      ,{field: 'usageTime', title: '使用时间' ,templet: '#barDemo',width:'15%'} 
    ]]
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
	      ,owner:'${user.nick}' 
	    }
	    ,request:{
    		pageName:'current'
	    }
	  });
  });
  
  var active = {
  	notice : function(){
  		layer.prompt({
				formType: 3
			 	,value: ''
			 	,title: '请输入密钥'
				,area: ['800px', '350px'] //自定义文本域宽高}
				,btn:['确定','取消']
		 	}
		 	,function(flowPackageKey, index, elem){
		 		var nick = "${user.nick}";
		 		$.ajax({
				    url:'/taoshelf/flowPackage/getFlowPackageByKey',				
				    type:'GET', 		//GET或POST
				    async:false,    		//或false,是否异步
			  //    headers:'{'Content-Type':'application/json;charset=utf8','name':'bbb'}',			//请求头
				    data:{				//要发送到服务器的数据
				        flowId:flowPackageKey,
				        nick:nick
				    },
				    cache:true,			//浏览器是否缓存被请求页面,默认为true
				    contentType:'application/json;charset=utf8',		//发送数据到服务器时所使用的内容类型  默认值为：application/x-www-form-urlencoded
				    timeout:5000,    	//超时时间
				    dataType:'json',    //服务器响应的数据格式：json/xml/html/script/jsonp/text
				    beforeSend:function(xhr){					//发送请求前要执行的函数
				    },
				    success:function(data,textStatus,jqXHR){	//请求响应成功的函数的函数
				    	var icon;
						if(data == 1){
							data = "流量包已获取";
							icon = 1;
						}else{
							data = "无法获取流量包(可能原因：1.流量包已被使用2.密钥不正确)"
							icon = 2;
						}	
				    	layui.use('layer', function(){
			  				var layer = layui.layer;
			  				layer.msg(data,{
			  					icon:icon,
			  					time:2000
			  				}),function(){};
			  				//未获取流量包,结束
			  				if(icon == 2){
			  					return ;
			  				}
			  				//流量包获取成功,跳转
			  				setTimeout(function(){
			  					window.location.href="/taoshelf/flowPackage/flowPackage";
			  				},"1500");
						});
				    },
				    error:function(xhr,textStatus){				//请求失败要运行的函数。
				    },
				    complete(xhr,status){						//请求完成时,要执行的函数,(不管是请求失败还是请求成功)
				    }						
				});
		 		return false;
		 	});
    }
  }
  
  $("#getFlowPackage").on('click', function(){
  	active.notice();
  });
  
  
});
</script>
</html>



