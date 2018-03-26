package com.web.spring4.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * web组件配置类
 * @author Garden
 * 2018年3月25日
 */
@Configuration
@EnableWebMvc    /*启用Spring MVC*/
@Import(ControllerConfig.class)    /*启动组件扫描，
                                                                            主要扫描控制器以及其他组件*/
public class WebConfig extends WebMvcConfigurerAdapter{    /*继承抽象类，
                                                                                                                              实现配置默认servlet拦截方法*/
	@Bean
	public ViewResolver viewResolver(){    /*配置JSP视图解析器*/
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/jsp/");
		resolver.setSuffix(".jsp");
		resolver.setExposeContextBeansAsAttributes(true);
		return resolver;
	}
	
	@Override
	public void configureDefaultServletHandling(
			DefaultServletHandlerConfigurer configurer) {    /*配置静态资源的处理，
			                                                                                                         将静态资源的请求转移至默认servlet*/
		configurer.enable();
	}
}
