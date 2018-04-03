# Spring4

## Spring MVC的高级技术

#### SpringMVC配置方案

* 使用AbstractAnnotationConfigDispatcherServletInitializer搭建SpringMVC环境（Servlet3.0）
  * 原理：Servlet3.0环境中，容器会在类路径查找实现javax.servlet.ServletContainerInitializer接口的类，若发现则用它来实现配置Servlet容器。Spring提供了该接口的实现SpringServletContainerInitializer，该实现会查找实现WebApplicationInitializer的类，如AbstractAnnotationConfigDispatcherServletInitializer，利用这些实现类进行servlet环境的配置。

```
package com.web.spring4.config;
import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration.Dynamic;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import com.web.spring4.filter.ExtraFilter;
/**
 * Web应用初始类
 * @author Garden
 * 2018年3月25日
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{
	/*加载ContextLoaderListener 数据层组件（bean，数据源等）*/
	@Override
	protected Class<?>[] getRootConfigClasses() {
		// TODO Auto-generated method stub
		return new Class<?>[] {RootConfig.class};
	}
	/*加载DispatcherServlet web组件（bean，映射处理器，控制器，视图解析器）*/
	@Override
	protected Class<?>[] getServletConfigClasses() {
		// TODO Auto-generated method stub
		return new Class<?>[] {WebConfig.class};
	}
	/*将DispatcherServlet映射到'/'*/
	@Override
	protected String[] getServletMappings() {
		// TODO Auto-generated method stub
		return new String[] {"/"};
//		return new String[] {"/mvc/*"};
	}
	/*定义过滤器并将过滤器映射至DispatcherServlet*/
//	@Override
//	protected Filter[] getServletFilters(){
//		return new Filter[]{new ExtraFilter()};
//		
//	}
	/*基于Servlet3.0的multipart请求解析器具体配置*/
	@Override
	protected void customizeRegistration(Dynamic registration){
		registration.setMultipartConfig(
				new MultipartConfigElement("",2097152,4194304,0));
		/*参数：存放文件临时路径，上传文件的文件大小，请求的最大容量，最大内存大小*/
	}
}
```
```
package com.web.spring4.config;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
/**
 * 数据层组件配置类
 * @author Garden
 * 2018年3月25日
 */
@Configuration
@ComponentScan(basePackages={"com.web.spring4.dao","com.web.spring4.service"},
               excludeFilters={@Filter(type=FilterType.ANNOTATION,value=EnableWebMvc.class)})  
public class RootConfig {
}
```
```
package com.web.spring4.config;
import java.io.IOException;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring3.SpringTemplateEngine;
import org.thymeleaf.spring3.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;
/**
 * web组件配置类
 * @author Garden
 * 2018年3月25日
 */
@Configuration
@EnableWebMvc    /*启用Spring MVC*/
@Import(ControllerConfig.class)    /*启动组件扫描，主要扫描控制器以及其他组件*/
public class WebConfig extends WebMvcConfigurerAdapter{    /*继承抽象类，实现配置默认servlet拦截方法*/
	@Bean
	public ViewResolver viewResolver(){    /*配置JSP视图解析器*/
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/jsp/");
		resolver.setSuffix(".jsp");
		resolver.setExposeContextBeansAsAttributes(true);
		resolver.setViewClass(org.springframework.web.servlet
				.view.JstlView.class);    /*设置返回View为JSTLView*/
		return resolver;
	}
//	@Bean
//	public ViewResolver viewResolver(){    /*配置Tiles视图解析器*/
//		return new TilesViewResolver();
//	}
//	@Bean
//	public TilesConfigurer tilesConfigurer(){    /*配置解析tiles定义*/
//		TilesConfigurer tiles = new TilesConfigurer();
//		tiles.setDefinitions(new String[]{
//				"WEB-INF/layout/tiles.xml"
//		});
//		tiles.setCheckRefresh(true);
//		return tiles;
//	}
//	  @Bean
//	  public ViewResolver viewResolver(
//			  SpringTemplateEngine templateEngine) {    /*配置Thymeleaf视图解析器*/
//	    ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
//	    viewResolver.setTemplateEngine(templateEngine);
//	    viewResolver.setCharacterEncoding("utf-8");
//	    return viewResolver;
//	  }
//	  @Bean
//	  public SpringTemplateEngine templateEngine(
//			  TemplateResolver templateResolver) {    /*配置模板引擎*/
//	    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//	    templateEngine.setTemplateResolver(templateResolver);
//	    return templateEngine;
//	  }
//	  @Bean
//	  public TemplateResolver templateResolver() {    /*配置模板解析器*/
//		  TemplateResolver templateResolver =
//				new ServletContextTemplateResolver();
//	    templateResolver.setPrefix("/WEB-INF/html/");
//	    templateResolver.setSuffix(".html");
//	    templateResolver.setTemplateMode("HTML5");
//	    templateResolver.setCharacterEncoding("utf-8");
//	    return templateResolver;
//	  }
	@Override
	public void configureDefaultServletHandling(
			DefaultServletHandlerConfigurer configurer) {    /*配置静态资源的处理，
			                                                                                                         将静态资源的请求转移至默认servlet*/
		configurer.enable();
	}
	@Bean
	public MessageSource messageSource(){    /*国际化信息配置*/
		ReloadableResourceBundleMessageSource messageSource =
				new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:/language");
		messageSource.setCacheSeconds(10);
		return messageSource;
	}
	@Bean
	public MultipartResolver multipartResolver() throws IOException{    /*基于Servlet3.0的multipart请求解析器配置*/
		return new StandardServletMultipartResolver();
	}
//	@Bean
//	public MultipartResolver multipartResolver() throws IOException{   /*基于Jakarta Commons FileUpload的multipart请求解析器配置*/
//		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
//		multipartResolver.setUploadTempDir(new FileSystemResource(""));
//		multipartResolver.setMaxUploadSize(2097152);
//		multipartResolver.setMaxInMemorySize(0);
//		return multipartResolver;
//	}
}
```
```
package com.web.spring4.listener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
/**
 * 额外监听器
 * @author Garden
 * 2018年4月3日
 */
public class ExtraListener implements HttpSessionListener {
    public ExtraListener() {
        // TODO Auto-generated constructor stub
    }
    public void sessionCreated(HttpSessionEvent se)  {
         // TODO Auto-generated method stub
        String sessionId = se.getSession().getId();
        System.out.println("建立了会话，会话ID为："+sessionId);
    }

    public void sessionDestroyed(HttpSessionEvent se)  {
         // TODO Auto-generated method stub
        String sessionId = se.getSession().getId();
        System.out.println("关闭了会话，会话ID为："+sessionId);
    }
}
```
```
package com.web.spring4.config;
import javax.servlet.Registration.Dynamic;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.springframework.web.WebApplicationInitializer;
import com.web.spring4.listener.ExtraListener;
/**
 * 监听器初始化类
 * @author Garden
 * 2018年4月2日
 */
public class ListenerInitialzer implements WebApplicationInitializer{
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		// TODO Auto-generated method stub
		servletContext.addListener(ExtraListener.class);
	}
}
```
```
package com.web.spring4.filter;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
/**
 * 额外过滤器
 * @author Garden
 * 2018年4月3日
 */
@WebFilter("/ExtraFilter")
public class ExtraFilter implements Filter {
    public ExtraFilter() {
        // TODO Auto-generated constructor stub
    }
	public void destroy() {
		// TODO Auto-generated method stub
	}
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		System.out.println(":-----------:");
		System.out.println("There is ExtraFilter-doFilter");
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		chain.doFilter(httpRequest, response);
	}
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
		System.out.println(":-----------:");
		System.out.println("There is ExtraFilter-init");
	}
}
```
```
package com.web.spring4.config;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.springframework.web.WebApplicationInitializer;
import com.web.spring4.filter.ExtraFilter;
/**
 * Filter初始化类
 * @author Garden
 * 2018年4月2日
 */
public class FilterInitialzer implements WebApplicationInitializer{
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		// TODO Auto-generated method stub
		Dynamic filter = servletContext.addFilter("extraFilter", ExtraFilter.class);
		//filter.addMappingForServletNames(null, false, "extraServlet");
		filter.addMappingForUrlPatterns(null, false, "/extra/*");
	}
}
```
```
package com.web.spring4.servlet;
/**
 * 额外Servlet
 * @author Garden
 * 2018年4月3日
 */
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/ExtraServlet")
public class ExtraServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public ExtraServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("There is ExtraServlet");
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
```
```
package com.web.spring4.config;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;
import org.springframework.web.WebApplicationInitializer;
import com.web.spring4.servlet.ExtraServlet;
/**
 * Servlet初始化类
 * @author Garden
 * 2018年4月2日
 */
public class ServletInitializer implements WebApplicationInitializer{
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		// TODO Auto-generated method stub
		Dynamic servlet = servletContext
				.addServlet("extraServlet",ExtraServlet.class );
		servlet.addMapping("/extra/*");
	}
}
```

* 使用web.xml搭建SpringMVC环境（Servlet3.0以前）

```
<?xml version="1.0" encoding="UTF-8"?>
<web-app
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns="http://java.sun.com/xml/ns/javaee"
    xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
    id="WebApp_ID"
    version="3.0">
    <!--基于Java配置
    <context-param>
        <param-name>contextClass</param-name>
        <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
    </context-param>
    -->

    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:SpringDataSource.xml,classpath:config.xml</param-value>
        <!-- <param-value>com.web.spring4.config.RootConfig</param-value> -->
    </context-param>
    <context-param>
        <param-name>spring.profiles.default</param-name>
        <param-value>dev</param-value>
    </context-param>
    <context-param>
        <param-name>spring.profiles.active</param-name>
        <param-value>pro</param-value>
    </context-param>
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
    <servlet>
        <servlet-name>springMVC</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!--
        <init-param>
            <param-name>contextClass</param-name>
            <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
        </init-param>
         -->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:SpringMVC.xml,classpath:config.xml</param-value>
            <!-- <param-value>com.web.spring4.config.WebConfig</param-value> -->
        </init-param>
        <init-param>
            <param-name>spring.profiles.default</param-name>
            <param-value>dev</param-value>
        </init-param>
        <init-param>
            <param-name>spring.profiles.active</param-name>
            <param-value>pro</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>springMVC</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
    <filter>
        <filter-name>UrlRewriteFilter</filter-name>
        <filter-class>org.tuckey.web.filters.urlrewrite.UrlRewriteFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>UrlRewriteFilter</filter-name>
        <url-pattern>/*</url-pattern>
        <dispatcher>REQUEST</dispatcher>
        <dispatcher>FORWARD</dispatcher>
    </filter-mapping>
    <filter>
        <filter-name>encodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>UTF-8</param-value>
        </init-param>
        <init-param>
            <param-name>forceEncoding</param-name>
            <param-value>true</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>encodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
    <filter>
        <filter-name>hibernateFilter</filter-name>
        <filter-class>org.springframework.orm.hibernate4.support.OpenSessionInViewFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>hibernateFilter</filter-name>
        <url-pattern>*.do</url-pattern>
    </filter-mapping>
</web-app>
```

#### 处理文件上传

#### 控制器处理异常

#### 跨重定向参数传递
