package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RefreshScope注解使这个bean可刷新
@RestController
@RefreshScope
class ProjectNameRestController {

	@Value("${configuration.projectName}")
	private String projectName; 

	//Spring会把Config Server中的配置值，作为enviroment
	/*
	 * @Autowired public
	 * ProjectNameRestController(@Value("${configuration.projectName}") String pn) {
	 * // <2> this.projectName = pn; }
	 */

	@RequestMapping("/project-name")
	String projectName() {
		return this.projectName;
	}
}
