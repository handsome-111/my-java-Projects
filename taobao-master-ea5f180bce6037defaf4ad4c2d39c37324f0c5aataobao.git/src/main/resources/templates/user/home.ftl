<!DOCTYPE html>
<html lang="en">
<head>
    <title>首页</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="/taoshelf/static/css/bootstrap.css">
    <style>
        th, td {
            white-space: nowrap;
            text-align: center;
        }
    </style>
    <script src="/taoshelf/static/js/jquery.js"></script>
    <script src="/taoshelf/static/js/bootstrap.js"></script>
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
                <a class="nav-link active" href="/taoshelf/home">首页</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/taoshelf/task">任务管理</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/taoshelf/tasklog/tasklog" >日志信息</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/taoshelf/flowPackage/flowPackage">流量包信息</a>
            </li>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown">
                    账户
                </a>
                <div class="dropdown-menu">
                    <a class="dropdown-item" href="#">${user.nick}</a>
                    <a class="dropdown-item" href="#">使用到期时间：${user.endTime}</a>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item" href="/taoshelf/recharge">账户充值</a>
                </div>
            </li>
        </ul>
    </div>
</nav>
<div class="container-fluid">
    <div class="row">
        <div class="col-lg-4 col-md-3 col-sm-1"></div>
        <div class="col-lg-4 col-md-6 col-sm-10" style="text-align: center">
            <img src="/taoshelf/static/img/logo.jpg" class="img-fluid">
            <!--<form action="/taoshelf/result" method="get">
                <div class="row">
                    <select name="type" class="col-md-2 col-sm-4 form-control" style="display: inline-block">
                        <option value="onsale">在售</option>
                        <option value="instock">库存</option>
                    </select>
                    <input name="q" type="text" class="col-md-10 col-sm-8 form-control" style="display: inline-block"
                           placeholder="请输入搜索关键字，为空则匹配所有">
                </div>
                <div class="row">
                    <div class="col-5"></div>
                    <input class="col-2 btn btn-outline-primary form-control" type="submit"
                           style="display: block;margin-top: 10px;"
                           value="搜索"/>
                    <div class="col-5"></div>
                </div>
            </form>-->
        </div>
        <div class="col-lg-4 col-md-3 col-sm-1"></div>
    </div>
    <div>
        <p>请注意应用授权使用到期时间，一旦到期将影响任务正常进行，请及时到服务市场续费</p>
        <p>服务使用到期时间：${user.endTime!}&nbsp;</p>
        <p>应用使用到期时间：${user.endDate1!}&nbsp;<a href="https://tb.cn/VdsUTMw" target="_blank">续费</a></p>
        <!-- <p>应用2使用到期时间：${user.endDate2!}&nbsp;<a href="https://tb.cn/QKThZNw" target="_blank">续费</a></p> -->
    </div>
    
    <style>
   		#start{
   			/*position: absolute;*/
   			width:300px;
		    margin-left: 50%;
		    transform: translateX(-50%) translateY(-50%);
  			border-radius: 5px;
		    background-color: #3498db;
		    border: none;
		    color: white;
		    padding: 15px 32px;
		    text-align: center;
		    text-decoration: none;
		    display: inline-block;
		    font-size: 16px;
		    cursor: pointer;
		    -webkit-transition-duration: 0.4s;
		    transition-duration: 0.4s;
		}
		#start:hover {
		    box-shadow: 0 12px 16px 0 rgba(0,0,0,0.24),0 17px 50px 0 rgba(0,0,0,0.19);
		}
    </style>
    <script>
    	function getNowFormatDate(date) {
		    var seperator1 = "-";
		    var seperator2 = ":";
		    var month = date.getMonth() + 1;
		    var strDate = date.getDate();
		    if (month >= 1 && month <= 9) {
		        month = "0" + month;
		    }
		    if (strDate >= 0 && strDate <= 9) {
		        strDate = "0" + strDate;
		    }
		    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
		            + " " + date.getHours() + seperator2 + date.getMinutes()
		            + seperator2 + date.getSeconds();
		    return currentdate;
		}
    
    	function start_click(){
    		//获取start
    		var nowDate = getNowFormatDate(new Date());
    		var startLong = new Date(nowDate.replace(new RegExp("-","gm"),"/")).getTime() + 60000;
    		var startDate = new Date();
    		startDate.setTime(startLong);
    		var start = getNowFormatDate(startDate);
    		
    		//获取end
    		var number = document.getElementById("val").value;
    		var endLong = new Date(nowDate.replace(new RegExp("-","gm"),"/")).getTime() + 60000 + (3600000 * number);
    		var endDate = new Date();
    		endDate.setTime(endLong);
    		var end = getNowFormatDate(endDate);
    		
    		if(number > 24){
    			alert("任务时间不得超过24小时");
    			return ;
    		}
    		
    		var radios = document.getElementsByName("rad[1]");
			for(var i = 0; i < radios.length; i++){
				if(radios[i].checked == true){
					if(radios[i].value == "仓库商品"){
						document.getElementById("start2").value=start;
						var form = document.getElementById("start_form2");
						form.submit();
						
					}else{
						document.getElementById("start_time").value=start;
						document.getElementById("end").value=end;
						var form = document.getElementById("start_form");
						form.submit();
					}
				}
			}
			
    	}
    </script>
	<button id="start" onclick="start_click();">开始循环任务</button>
	<!-- <span style="position: absolute;left: 55%;top: 40%;">(点击此处开始设置任务)</span> -->
</div>

<!-- 单选按钮 -->
<style>
label.radio {
  display: inline-block;
  height: 24px;
  width: 24px;
  position: relative;
  margin: 0 5px 5px 0;
  padding: 0;
  background-color: #cfcfcf;
  border-radius: 100%;
  vertical-align: top;
  box-shadow: 0 1px 15px rgba(0, 0, 0, 0.1) inset, 0 1px 4px rgba(0, 0, 0, 0.1) inset, 1px -1px 2px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: all 0.2s ease;
}
label.radio > span.pip {
  width: 16px;
  height: 16px;
  position: absolute;
  border-radius: 100%;
  background: blue;
  top: 4px;
  left: 4px;
  box-shadow: 0 2px 5px 1px rgba(0, 0, 0, 0.3), 0 0 1px rgba(255, 255, 255, 0.4) inset;
  background-image: linear-gradient(#ffffff 0, #e7e7e7 100%);
  transform: scale(0, 0);
  transition: all 0.2s ease;
}
label.radio.on {
  background-color: #05abe0;
}
label.radio.on > span.pip {
  transform: scale(1, 1);
}
label.radio.focus {
  outline: 0;
  box-shadow: 0 1px 15px rgba(0, 0, 0, 0.1) inset, 0 1px 4px rgba(0, 0, 0, 0.1) inset, 1px -1px 2px rgba(0, 0, 0, 0.1), 0 0 8px #52a8ec, 0 0 1px 1px rgba(0, 0, 0, 0.75) inset;
}
input[type=checkbox].replaced,
input[type=radio].replaced {
  position: absolute;
  left: -9999em;
}
.wrapper {
  width: 270px;
  /*margin: 30px auto;*/
  margin-left:43%;
}
.font_a{
  color: #666;
  text-align: center;
  width: 560px;
  margin: 0 auto;
  line-height: 1.2em;
}

</style>
<script src="/taoshelf/static/radio_style/js/prefixfree.min.js"></script>
<script src="/taoshelf/static/radio_style/js/modernizr.js"></script>
</head>
<div class="wrapper">
<span style="color:#666">任务类型:</span>
<input type="radio" id="rad7" name="rad[1]" value="在售商品" checked><span class="font_a">在售商品</span> 
<!--<input type="radio" id="rad8" name="rad[1]" value="库存商品"><span class="font_a">库存商品</span>-->
<div style="text-align:center;clear:both">
</div>
<script src='/taoshelf/static/radio_style/js/jquery.js'></script>
<script src="/taoshelf/static/radio_style/js/index.js"></script>

<!--number样式-->
<style type="text/css">
	.number{width:450px;margin:auto;color:#666;backgound-color:black;}
	.mui-numbox{ display: inline-block;}
	.minus{width: 30px; height: 30px; text-align: center;border: solid 1px #eaeaea; background: #f5f5f5; color: #9e9e9e;} 
	.numbox{border-top: solid 1px #eaeaea; border-bottom: solid 1px #eaeaea; height: 28px; width:60px; color: #333333; margin-left:-8px; text-align: center; background: transparent;}
	.numbox::textfield-decoration-container {}
	.numbox::-webkit-inner-spin-button {     -webkit-appearance: none;}
	.numbox::-webkit-outer-spin-button {     -webkit-appearance: none;	/* 有无看不出差别 */}
	.plus { margin-left: -8px;}
</style>
<div class="number">
				时间:
				<div class="mui-numbox">
					<button class="minus" type="button" onclick="opera('val', false);">-</button>
					<input class="numbox" type="number" id="val" value="1"/>
					<button class="minus plus" type="button" onclick="opera('val', true);">+</button>
					小时(系统默认限制最大值为24小时)
				</div>
<script type="text/javascript">
	function opera(x, y) {
		var rs = new Number(document.getElementById(x).value);
		if (y) {
			document.getElementById(x).value = rs + 1;
		} else if( rs >0) {
			document.getElementById(x).value = rs - 1;
		}
	}
</script>

	<form id="start_form" method="post" action="/taoshelf/add_task1" style="display:none;">
		<input type="text" name="type" value="在售商品">
		<input type="text" id="start_time" name="start">
		<input type="text" id="end" name="end">
	</form>
	
	<form id="start_form2" method="post" action="/taoshelf/add_task2" style="display:none;">
		<input type="text"  name="type" value="仓库商品"><input type="text" id="start2" name="start">
	</form>
</div> 
</body>
</html>



