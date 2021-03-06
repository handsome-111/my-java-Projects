/*$.ajax({
    url:'/getPerson',				
    type:'GET', 		//GET或POST
    async:true,    		//或false,是否异步
//	    headers:'{'Content-Type':'application/json;charset=utf8','name':'bbb'}',			//请求头
    data:{				//要发送到服务器的数据
        name:'llc',
        age:22
    },
    cache:true,			//浏览器是否缓存被请求页面,默认为true
    contentType:'application/json;charset=utf8',		//发送数据到服务器时所使用的内容类型  默认值为：application/x-www-form-urlencoded
    timeout:5000,    	//超时时间
    dataType:'json',    //服务器响应的数据格式：json/xml/html/script/jsonp/text
    beforeSend:function(xhr){					//发送请求前要执行的函数
    },
    success:function(data,textStatus,jqXHR){	//请求响应成功的函数的函数		
    	
    },
    error:function(xhr,textStatus){				//请求失败要运行的函数。
    },
    complete(xhr,status){						//请求完成时,要执行的函数,(不管是请求失败还是请求成功)
    }						
});
*/

/**
 * VUE具体API：https://cn.vuejs.org/v2/api
 */
var vm = new Vue({
	el:'#person',				//绑定的元素(Id),如#id,*
	data:{data:""},				//初始化“”
	created: function () {		//初始化方法，每个 Vue 实例在被创建时都要经过一系列的初始化过程,this指向 vm 实例
	},
	methods:{					//对象的方法,通过this可以获取该实例的属性,跟JAVA一样,通过vm.method1()来调用
		showData : function(){
			$.ajax({
			    url:'/getPerson',				
			    type:'GET', 		//GET或POST
			    async:true,    		//或false,是否异步
//				    headers:'{'Content-Type':'application/json;charset=utf8','name':'bbb'}',			//请求头
			    data:{				//要发送到服务器的数据
			        name:'llc',
			        age:22
			    },
			    cache:true,			//浏览器是否缓存被请求页面,默认为true
			    contentType:'application/json;charset=utf8',		//发送数据到服务器时所使用的内容类型  默认值为：application/x-www-form-urlencoded
			    timeout:5000,    	//超时时间
			    dataType:'json',    //服务器响应的数据格式：json/xml/html/script/jsonp/text
			    beforeSend:function(xhr){					//发送请求前要执行的函数
			    },
			    success:function(data,textStatus,jqXHR){	//请求响应成功的函数的函数		
			    	vm.data =  data;						//赋值给data
			    },
			    error:function(xhr,textStatus){				//请求失败要运行的函数。
			    },
			    complete(xhr,status){						//请求完成时,要执行的函数,(不管是请求失败还是请求成功)
			    }						
			});
		},
		method2 : function(){}								
	},
});



