# Spring4

## 渲染Web视图-常用视图解析器

#### 理解视图解析器
* 视图解析器实现基本原理：ViewResolver接口实现解析视图名任务并返回View接口。View接口任务就是接受模型以及servlet的request和response对象，并将输出结果渲染到response中。
```
public interface ViewResolver{
  View resolverViewName(String viewName,Locale locale) throw Exception;
}
```
```
public interface View{
  String getContentType();
  void render(Map<String,?> model,HttpServletRequest request,HttpServletResponse) throw Exception;
}
```
* Spring4支持的视图解析器

| 视图解析器 | 描述 |
| -------- | ----- |
| BeanNameViewResolver | 将视图解析为Spring应用上下文的bean定义，视图名与bean id 相匹配 |
| ContentNegotiatingViewResolver | 将视图解析任务委托给另一个能够产生客户端需要的内容类型的视图解析器 |
| FreeMarkerViewResolver | 将视图解析为[FreeMark模板](http://freemarker.foofun.cn/)定义，视图名与HTML名相匹配 |
| InternalResourceViewResolver | 将视图解析为Web应用的内部资源（一般为JSP）定义，视图名与JSP名相匹配 |
| JasperReportsViewResolver | 将视图解析为[JasperReports](https://www.yiibai.com/jasper_reports/)定义 ,视图名与报表视图配置上的bean id 相匹配|
| ResourceBundleViewResolver | 将视图解析为ResourceBundle定义，通过属性文件解析视图，视图名与JSP名相匹配 |
| TilesViewResolver | 将视图解析为Tiles定义，视图名与tile id 相匹配 |
| UrlBasedViewResolver | 将视图解析为物理路径的视图定义 （一般为静态页面），视图名与物理路径视图名相匹配 |
| VelocityLayoutViewResolver | 将视图解析为[Velocity布局](https://www.oschina.net/question/12_4580)定义，从不同的Velocity模板中组合页面 |
| VelocityViewResolver | 将视图解析为Velocity模板定义，已被Velocity布局代替 |
| XmlViewResolver | 将视图解析为XML里的bean定义，视图名与bean id 相匹配 |
| XsltViewResolver | 将视图解析为[XSLT](http://www.codejava.net/frameworks/spring/spring-mvc-xstlview-and-xsltviewresolver-example)转换后的结果定义 |
| ThymeleafViewResolver | 将视图解析为Thymeleaf定义，视图名与HTML名相匹配 |

#### 常用视图解析器
