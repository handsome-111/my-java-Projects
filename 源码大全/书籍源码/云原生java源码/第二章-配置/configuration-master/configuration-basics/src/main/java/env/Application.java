package env;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

// <1>@PropertySource注解就像是一个“属性占位符”的快捷方式，通过一个.properties文件来配置PropertySource
@Configuration
@PropertySource("some.properties")
public class Application {

	private final Log log = LogFactory.getLog(getClass());

	public static void main(String[] args) throws Throwable {
		new AnnotationConfigApplicationContext(Application.class);
	}

	// <2>	需要将PropertySourcesPlaceholderConfigurer注册成一个“静态的”Bean，因为他是BeanFactoryPostProcessor的一个实现
	//必须在Spring bean初始化生命周期的早期阶段进行调用
	@Bean
	static PropertySourcesPlaceholderConfigurer pspc() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	// <3>	可以使用@Value注解来修饰字段(但是尽量不要这样做，会使得测试失败)
	@Value("${configuration.projectName}")
	private String fieldValue;

	// <4>	也可以使用@Value注解来修饰构造函数参数
	@Autowired
	Application(@Value("${configuration.projectName}") String pn) {
		log.info("Application constructor: " + pn);
	}

	// <5>	也可以使用setter方法
	@Value("${configuration.projectName}")
	void setProjectName(String projectName) {
		log.info("setProjectName: " + projectName);
	}

	// <6>	也可以注入Spring Environment对象并手动解析配置项目
	@Autowired
	void setEnvironment(Environment env) {
		log.info("setEnvironment: " + env.getProperty("configuration.projectName"));
	}

	// <7>
	@Bean
	InitializingBean both(Environment env, @Value("${configuration.projectName}") String projectName) {
		return () -> {
			log.info("@Bean with both dependencies (projectName): " + projectName);
			log.info("@Bean with both dependencies (env): " + env.getProperty("configuration.projectName"));
		};
	}

	@PostConstruct
	void afterPropertiesSet() throws Throwable {
		log.info("fieldValue: " + this.fieldValue);
	}
}
