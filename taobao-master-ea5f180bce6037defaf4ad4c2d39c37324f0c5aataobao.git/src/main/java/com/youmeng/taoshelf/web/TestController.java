package com.youmeng.taoshelf.web;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.FenxiaoCooperationGetRequest;
import com.taobao.api.request.FenxiaoDistributorProductsGetRequest;
import com.taobao.api.request.FenxiaoDistributorsGetRequest;
import com.taobao.api.response.FenxiaoCooperationGetResponse;
import com.taobao.api.response.FenxiaoDistributorProductsGetResponse;
import com.taobao.api.response.FenxiaoDistributorsGetResponse;
import com.youmeng.taoshelf.entity.Task;
import com.youmeng.taoshelf.entity.User;

@RestController
public class TestController {
    @Resource(name = "client1")
	private TaobaoClient client ;
    @Autowired
    private RestTemplate restTemplate;
	public static final String url = "http://gw.api.taobao.com/router/rest";
	static final String appkey = "12322527" ;
	static final String secret = "db6e35b4a58543e9f48b34419e278377";
	@Autowired
    private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private RestTemplate template;
	@RequestMapping("/test")
	public String test(){
    	stringRedisTemplate.opsForValue().decrement("bbb");
    	System.out.println(stringRedisTemplate.opsForValue().get("bbb"));
		stringRedisTemplate.opsForValue().set("bbb", "2");
    	stringRedisTemplate.opsForValue().decrement("bbb");
    	System.out.println(stringRedisTemplate.opsForValue().get("bbb"));
    	stringRedisTemplate.opsForValue().decrement("bbb");
    	System.out.println(stringRedisTemplate.opsForValue().get("bbb"));
    	stringRedisTemplate.opsForValue().decrement("bbb");
    	System.out.println(stringRedisTemplate.opsForValue().get("bbb"));
    	return "GG";
	}
	/**
	 * 获取所有的供应商
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/test2")
	public String test2() throws Exception {
		//TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		FenxiaoCooperationGetRequest  req = new FenxiaoCooperationGetRequest();
		req.setStatus("NORMAL");
		req.setTradeType("AGENT");
		req.setPageNo(1L);
		req.setPageSize(20L);
		FenxiaoCooperationGetResponse rsp = client.execute(req, "61028234da83401a90b8e6bd821b54610e82df99dfabd0e1137562299");
		System.out.println(rsp.getBody());
		File file = new File("/test.txt");
		PrintStream print = new PrintStream(file);
		print.println(rsp.getBody());
		print.close();
		
		
		FenxiaoDistributorsGetRequest req2 = new FenxiaoDistributorsGetRequest();
		JSONArray cooperations = JSON.parseObject(rsp.getBody()).getJSONObject("fenxiao_cooperation_get_response").getJSONObject("cooperations").getJSONArray("cooperation");
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i < cooperations.size();i++){
			JSONObject cooperation = (JSONObject) cooperations.get(i);
			sb.append(cooperation.get("supplier_nick"));
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		System.out.println("所有的昵称：" + sb.toString());
		req2.setNicks(sb.toString());
		FenxiaoDistributorsGetResponse rsp2 = client.execute(req2, "61028234da83401a90b8e6bd821b54610e82df99dfabd0e1137562299");
		//System.out.println(rsp2.getBody());
		File file2 = new File("/test2.txt");
		PrintStream print2 = new PrintStream(file2);
		print2.println(rsp2.getBody());
		print2.close();
		return rsp.getBody();
	}
	@RequestMapping("/test3")
	public String test3(@RequestParam String nick) throws Exception{
		/**
		 * 获取所有的供应商
		 */
		FenxiaoCooperationGetRequest  req = new FenxiaoCooperationGetRequest();
		req.setStatus("NORMAL");
		req.setTradeType("AGENT");
		req.setPageNo(1L);
		req.setPageSize(5L);
		FenxiaoCooperationGetResponse rsp = client.execute(req, "61028234da83401a90b8e6bd821b54610e82df99dfabd0e1137562299");
		System.out.println(rsp.getBody());
		File file = new File("/test.txt");
		PrintStream print = new PrintStream(file);
		print.println(rsp.getBody());
		print.close();
		JSONArray cooperations = JSON.parseObject(rsp.getBody()).getJSONObject("fenxiao_cooperation_get_response").getJSONObject("cooperations").getJSONArray("cooperation");
		System.out.println("供应商总数:" + JSON.parseObject(rsp.getBody()).getJSONObject("fenxiao_cooperation_get_response").getInteger("total_results"));
		List<String> nicks = new ArrayList<String>();
		for(int i = 0;i < cooperations.size();i++){
			JSONObject cooperation = (JSONObject) cooperations.get(i);
			nicks.add((String) cooperation.get("supplier_nick"));
		}
		System.out.println("所有的供应商:" + nicks);
		
		
		System.out.println("发起测试的供应商:" + nicks.get(0));
		FenxiaoDistributorProductsGetRequest req2 = new FenxiaoDistributorProductsGetRequest();
		req2.setPageNo(1L);
		req2.setPageSize(20L);
		req2.setTradeType("AGENT");
		req2.setSupplierNick(nicks.get(0));
		//req.setSupplierNick("tc服饰有限公司");
		//req.setSupplierNick(nick);
		FenxiaoDistributorProductsGetResponse rsp2 = client.execute(req2, "61028234da83401a90b8e6bd821b54610e82df99dfabd0e1137562299");
		//System.out.println(rsp.getBody());
		File file2 = new File("/test3.txt");
		PrintStream print2 = new PrintStream(file2);
		print2.println(rsp2.getBody());
		print2.close();
		
		return rsp2.getBody();
	}
	@RequestMapping("/test4")
	public String test4(@RequestParam String nick) throws Exception{
		String str ="163shoe,tc服饰有限公司,new_shine,深圳丰尚美,东泰通达运动专营店,卉卉美美数码专营店,索芙特天诺专卖店,夏琦供应商,立鑫供应商,亚旺贸易供应商,大汉酵素品牌商,欧蒽艺品牌商,诗碧旗舰店,鸣人童装旗舰店供应商,康纤旗舰店供应商,池莲旗舰店供应商1,nusun宠物用品旗舰店供应商";
		FenxiaoDistributorProductsGetRequest req = new FenxiaoDistributorProductsGetRequest();
		req.setPageNo(1L);
		req.setPageSize(20L);
		req.setTradeType("DEALER");
		req.setSupplierNick(nick);
		FenxiaoDistributorProductsGetResponse rsp = client.execute(req, "61028234da83401a90b8e6bd821b54610e82df99dfabd0e1137562299");
		System.out.println(rsp.getBody());
		File file = new File("/test4.txt");
		PrintStream print = new PrintStream(file);
		print.println(rsp.getBody());
		print.close();
		
		return rsp.getBody();
	}
	@RequestMapping("/test5")
	public String test5(){
		//MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
		HashMap<String,Object> params = new HashMap<String,Object>();
		User user = new User();
		user.setEndTime(new Date());
		user.setNick("qwewqew");
		Task task = new Task();
		task.setId("12312312");
		task.setEndTime(new Date());
		task.setStartTime(new Date());
		task.setType("qq");
		params.put("task", task);
		params.put("user", user);
		/**
		 * 定义头信息
		 */
		HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        /**
         * 转换成JSON
         */
        String jsonString = JSONObject.toJSONString(params);
        HttpEntity<String> formEntity = new HttpEntity<String>(jsonString.toString(), headers);
        
	    String response = restTemplate.postForObject("http://localhost:81/taotask/start", formEntity, String.class);
	    return response;
	}
}
