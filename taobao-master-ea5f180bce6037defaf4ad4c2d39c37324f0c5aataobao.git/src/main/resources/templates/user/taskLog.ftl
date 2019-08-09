<!DOCTYPE html>
<html lang="en">
<head>
    <title>日志信息</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="/taoshelf/static/css/bootstrap.css">
    <link rel="stylesheet" href="https://unpkg.com/element-ui@2.4.8/lib/theme-chalk/index.css">
    <link rel="stylesheet" href="/taoshelf/static/layui/css/layui.css">
    <style>
    </style>
    <script src="/taoshelf/static/js/jquery.js"></script>
    <script src="/taoshelf/static/js/bootstrap.js"></script>
    <script src="/taoshelf/static/js/vue.min.js"></script>
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
                <a class="nav-link active" href="/taoshelf/tasklog/tasklog" >日志信息</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/taoshelf/flowPackage/flowPackage">流量包信息</a>
            </li>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown">
                    账户
                </a>
                <div id="sessionUser" class="dropdown-menu">
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
  <legend>任务日志</legend>
</fieldset> 

<!-- 表格 -->
<table class="layui-table" id="demo" lay-filter="test">
</table>
</body>
<script th:inline="none">
	
/*var vm = new Vue({
	el:'#sessionUser',				//绑定的元素(Id),如#id,*
	data:{data:""},
	created: function () {		//初始化方法，每个 Vue 实例在被创建时都要经过一系列的初始化过程,this指向 vm 实例
		this.getUserSession();
	},
	methods:{
		timestampToTime : function (timestamp) {
	        var date = new Date(timestamp);
	        var Y = date.getFullYear() + '-';
	        var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
	        var D = date.getDate() + ' ';
	        var h = date.getHours() + ':';
	        var m = date.getMinutes() + ':';
	        var s = date.getSeconds();
	        console.log(timestamp);
	        return Y+M+D+h+m+s;
    	},
		//获取用户会话属性
		getUserSession : function (){
			var _this = this;		//获取VM对象
			$.ajax({
			    url:'/taoshelf/getUserSession',				
			    type:'GET', 		//GET或POST
			    async:false,    		//或false,是否异步
		  //    headers:'{'Content-Type':'application/json;charset=utf8','name':'bbb'}',			//请求头
			    cache:true,			//浏览器是否缓存被请求页面,默认为true
			    contentType:'application/json;charset=utf8',		//发送数据到服务器时所使用的内容类型  默认值为：application/x-www-form-urlencoded
			    timeout:5000,    	//超时时间
			    dataType:'json',    //服务器响应的数据格式：json/xml/html/script/jsonp/text
			    beforeSend:function(xhr){					//发送请求前要执行的函数
			    },
			    success:function(data,textStatus,jqXHR){	//请求响应成功的函数的函数
			    	_this.data = data;
			    	_this.data.user.endTime = _this.timestampToTime(data.user.endTime);
			    },
			    error:function(xhr,textStatus){				//请求失败要运行的函数。
			    },
			    complete(xhr,status){						//请求完成时,要执行的函数,(不管是请求失败还是请求成功)
		    }						
		});
		}
	}
});
	*/
	
layui.use('table', function(){
  var table = layui.table;
  //第一个实例
  table.render({
    elem: '#demo'
    ,url: '/taoshelf/tasklog/getTaskLogByTaskId' 	//数据接口
    ,page: true 			//开启分页
    ,autoSort: false		//禁止自动排序
    ,where:{taskId:'${taskId}'}
    ,cols: [[ 				//表头
      {field: 'tasklogId', title: '日志ID', width:'5%', sort: true, fixed: 'left'}
      ,{field: 'goodid', title: '宝贝ID',width:'10%'}
      ,{field: 'time', title: '日志时间' ,width:'10%',sort: true} 
      ,{field: 'title', title: '宝贝标题',width:'30%'}
      ,{field: 'msg', title: '日志信息',width:'30%'}
    ]]
    ,loading:true
    ,request:{
    	pageName:'current'
    }
    ,response:{
    	 statusName: 'code' //数据状态的字段名称，默认：code
	    ,statusCode: 200 //成功的状态码，默认：0
	    ,msgName: 'msg' //状态信息的字段名称，默认：msg
	    ,countName: 'count' //数据总数的字段名称，默认：count
	    ,dataName: 'data' //数据列表的字段名称，默认：data
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
	  	url: '/taoshelf/tasklog/getTaskLogByTaskId'
	    ,autoSort: false			//禁止自动排序
	    ,initSort: obj 				//记录初始排序，如果不设的话，将无法标记表头的排序状态。
	    ,where: { 					//请求参数（注意：这里面的参数可任意定义，并非下面固定的格式）
	      field: obj.field 			//排序字段
	      ,order: obj.type 			//排序方式
	    }
	    ,request:{
    		pageName:'current'
	    }
	  });
  });
});
</script>
</html>



