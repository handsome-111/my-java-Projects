package com.youmeng.taoshelf.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.youmeng.common.base.taotask.entity.FlowPackage;
import com.youmeng.taoshelf.entity.Admin;
import com.youmeng.taoshelf.entity.Card;
import com.youmeng.taoshelf.entity.Log;
import com.youmeng.taoshelf.entity.PageInfo;
import com.youmeng.taoshelf.entity.Task;
import com.youmeng.taoshelf.entity.User;
import com.youmeng.taoshelf.service.AdminService;
import com.youmeng.taoshelf.service.CardService;
import com.youmeng.taoshelf.service.FlowPackageService;
import com.youmeng.taoshelf.service.LogService;
import com.youmeng.taoshelf.service.TaskService;
import com.youmeng.taoshelf.service.UserService;

@Controller
public class AdminController {
	@Autowired
	private FlowPackageService flowPackageService;
    @Resource
    private AdminService adminService;

    @Resource
    private UserService userService;

    @Resource
    private CardService cardService;

    @Resource
    private LogService logService;

    @Resource
    private TaskService taskService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/8088/login")
    public ModelAndView login() {
        return new ModelAndView("/admin/login");
    }

    @RequestMapping("/8088/admin")
    public ModelAndView admin(@RequestParam(defaultValue = "1") int page_no,
                              @RequestParam(defaultValue = "15") int page_size,HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/admin/admin");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.findAdminByName(authentication.getName());
        Page<Task> taskPage = taskService.getAllTasksByStatus("正在执行", page_no - 1, page_size);
        modelAndView.addObject("task_total", taskPage.getTotalElements());
        modelAndView.addObject("page_size", page_size);
        modelAndView.addObject("page_no", page_no);
        List<Task> tasks = taskPage.getContent();
        for (Task task : tasks) {
            if (task.getStatus().equals("正在执行任务")) {
                String count = redisTemplate.opsForValue().get(task.getId());
                if (task.getEndTime() == null) {
                    task.setStatus(task.getStatus() + count + "/" + task.getNum());
                } else {
                    task.setStatus(task.getStatus() + "(第" + count + "次上下架)");
                }
            }
        }
        modelAndView.addObject("tasks", tasks);
        modelAndView.addObject("admin", admin);
        return modelAndView;
    }

    @RequestMapping("/8088/stop_task")
    @ResponseBody
    public String stopTask(@RequestParam String id) {
    	String number = redisTemplate.opsForValue().get(id);
        return taskService.stopTaskById(id,number);
    }

    @RequestMapping("/8088/user")
    public ModelAndView user(@RequestParam(defaultValue = "") String s,
                             @RequestParam(defaultValue = "1") int page_no,
                             @RequestParam(defaultValue = "15") int page_size) {
        ModelAndView modelAndView = new ModelAndView("/admin/user");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.findAdminByName(authentication.getName());
        modelAndView.addObject("admin", admin);
        Page<User> users = userService.getAllUsers(page_size, page_no - 1, s);
        List<User> userList = users.getContent();
        modelAndView.addObject("users", userList);
        modelAndView.addObject("card_total", users.getTotalElements());
        modelAndView.addObject("page_size", page_size);
        modelAndView.addObject("page_no", page_no);
        return modelAndView;
    }

    @PostMapping("/8088/del_user")
    @ResponseBody
    public String delUser(@RequestParam String nick) {
        Boolean aBoolean = userService.deleteUserByNick(nick);
        if (aBoolean) {
            return "success";
        } else {
            return "error";
        }
    }

    @GetMapping("/8088/card_new")
    public ModelAndView cardNew(@RequestParam(defaultValue = "1") int page_no,
                                @RequestParam(defaultValue = "15") int page_size) {
        ModelAndView modelAndView = new ModelAndView("/admin/card_new");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.findAdminByName(authentication.getName());
        modelAndView.addObject("admin", admin);
        Page<Card> cards = cardService.getAllNewCard(page_size, page_no - 1);
        List<Card> cardList = cards.getContent();
        modelAndView.addObject("card_total", cards.getTotalElements());
        modelAndView.addObject("page_size", page_size);
        modelAndView.addObject("page_no", page_no);
        modelAndView.addObject("cards", cardList);
        return modelAndView;
    }

    @GetMapping("/8088/card_old")
    public ModelAndView cardOld(@RequestParam(defaultValue = "1") int page_no,
                                @RequestParam(defaultValue = "15") int page_size) {
        ModelAndView modelAndView = new ModelAndView("/admin/card_old");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.findAdminByName(authentication.getName());
        modelAndView.addObject("admin", admin);
        Page<Card> cards = cardService.getAllOldCard(page_size, page_no - 1);
        List<Card> cardList = cards.getContent();
        modelAndView.addObject("card_total", cards.getTotalElements());
        modelAndView.addObject("page_size", page_size);
        modelAndView.addObject("page_no", page_no);
        modelAndView.addObject("cards", cardList);
        return modelAndView;
    }

    @PostMapping("/8088/import_cards")
    public String importCards(@RequestParam MultipartFile file, @RequestParam int day) throws IOException {
        if (!file.isEmpty()) {
            List<Card> cards = new ArrayList<>();
            InputStream inputStream = file.getInputStream();
            LineIterator lineIterator = IOUtils.lineIterator(inputStream, "utf-8");
            while (lineIterator.hasNext()) {
                Card card = new Card(lineIterator.next().trim());
                card.setDay(day);
                cards.add(card);
            }
            cardService.importCards(cards);
        }
        return "redirect:/8088/card_new";
    }

    @PostMapping("/8088/delete_cards")
    @ResponseBody
    public String deleteCards(@RequestParam String data) {
        JSONArray array = JSONArray.parseArray(data);
        ArrayList<String> list = new ArrayList<>();
        for (Object anArray : array) {
            String item = (String) anArray;
            list.add(item);
        }
        String[] ids = (String[]) list.toArray(new String[0]);
        return String.valueOf(cardService.deleteCardById(ids));
    }

    @RequestMapping("/8088/user_log")
    public ModelAndView userLog(
            @RequestParam String nick,
            @RequestParam(defaultValue = "1") int page_no,
            @RequestParam(defaultValue = "15") int page_size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.findAdminByName(authentication.getName());
        ModelAndView modelAndView = new ModelAndView("/admin/user_log");
        modelAndView.addObject("admin", admin);
        User user = userService.getUserByNick(nick);
        modelAndView.addObject("nick", nick);
        Page<Log> logPage = logService.getLogsByUser(user, page_no - 1, page_size);
        modelAndView.addObject("log_total", logPage.getTotalElements());
        modelAndView.addObject("page_size", page_size);
        modelAndView.addObject("page_no", page_no);
        List<Log> logs = logPage.getContent();
        modelAndView.addObject("logs", logs);
        return modelAndView;
    }

    @RequestMapping("/8088/password")
    public ModelAndView password() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.findAdminByName(authentication.getName());
        ModelAndView modelAndView = new ModelAndView("/admin/password");
        modelAndView.addObject("admin", admin);
        return modelAndView;
    }

    @PostMapping("/8088/do_password_mod")
    public String doPasswordMod(RedirectAttributes attributes,
                                @RequestParam String oldPassword,
                                @RequestParam String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.findAdminByName(authentication.getName());
        if (passwordEncoder.matches(oldPassword, admin.getPassword())) {
            admin.setPassword(passwordEncoder.encode(newPassword));
            if (adminService.saveAdmin(admin)) {
                attributes.addFlashAttribute("info", new PageInfo("success", "密码修改成功"));
            } else {
                attributes.addFlashAttribute("info", new PageInfo("error", "密码修改失败"));
            }
        } else {
            attributes.addFlashAttribute("info", new PageInfo("error", "旧密码输入不正确"));
        }
        return "redirect:/8088/password";
    }
    /**
     * 流量包管理
     * @return
     */
    @GetMapping("/8088/createFlowPackage")
    public ModelAndView createFlowPackage(){
    	 ModelAndView modelAndView = new ModelAndView("/admin/create_flow_package");
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         Admin admin = adminService.findAdminByName(authentication.getName());
         modelAndView.addObject("admin", admin);
         return modelAndView;
    }
    
    /**
     * 查看流量包
     * @return
     */
    @GetMapping("/8088/showFlowPackages")
    public ModelAndView showFlowPackages(){
    	ModelAndView modelAndView = new ModelAndView("/admin/show_flow_packages");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.findAdminByName(authentication.getName());
        modelAndView.addObject("admin", admin);
        return modelAndView;
    }
    
    /**
     * 批量创建流量包
     * @param flowMax	流量大小
     * @param price		价格
     * @param count		总流量包数量
     * @param nick		发送给指定用户
     * @return
     */
    @PostMapping("/8088/batchCreateFlowPackage")
    @ResponseBody
	public String batchCreateFlowPackage(@RequestParam long flowMax,@RequestParam double price
			,@RequestParam long count
			,@RequestParam(required=false) String nick){
    	if(nick != null && nick != ""){
    		User user = userService.getUserByNick(nick);
    		if(user == null){
    			return "该用户不存在,流量包创建失败";
    		}
    	}
		List<FlowPackage> list = new ArrayList<FlowPackage>();
		for(int i = 0; i < count;i++){
			FlowPackage flowPackage = new FlowPackage();
			flowPackage.setCreateTime(new Date());
			flowPackage.setFlowMax(flowMax);
			flowPackage.setPrice(price);
			flowPackage.setOwner(nick);
			if(nick != null){
				flowPackage.setGetTime(new Date());
			}
			list.add(flowPackage);
		}
		
		boolean flag = flowPackageService.saveBatch(list);
		if(flag == true){
			return "流量包创建完成";
		}
		return "流量包创建异常";
	}
    @PostMapping("/8088/batchDeleteFlowPackage")
    @ResponseBody
    public String batchDeleteFlowPackage(@RequestParam(value="flowIds[]") String...flowIds){
    	List list = Arrays.asList(flowIds);
    	boolean flag = flowPackageService.removeByIds(list);
    	if(flag){
    		return "流量包删除成功";
    	}
    	return "流量包异常";
    }
}
