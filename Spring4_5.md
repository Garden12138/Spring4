# Spring4

## 构建Spring Web应用程序

#### 了解Spring的SpringMVC框架

* 跟踪Spring MVC的请求：Spring将请求在调度Servlet，处理器映射，控制器以及视图解析器之间移动。
[![05fig01.jpg](https://i.loli.net/2018/03/26/5ab852f0cb58d.jpg)](https://i.loli.net/2018/03/26/5ab852f0cb58d.jpg)
  * DispatcherServlet：前端控制器，是一个单例的调度Servlet。任务是接收用户请求并将请求发送至相应的控制器（发送请求至控制器前先进入HandlerMapping确认具体控制器）。
  * HandlerMapping：处理器映射，任务是根据请求所携带的URL信息进行决策，确认具体的控制器。
  * Controller：控制器，处理请求信息（请求到达控制器后会卸下信息并等待控制器返回信息），完成业务逻辑（实际业务逻辑委托给服务对象处理），返回信息（即模型model，由请求携带）和视图名称（veiwname）至DispatcherServlet。
  * ViewResolver：视图解析器，将视图名匹配为一个特定的视图实现（即存放视图的web路径）。
  * View：视图，将模型渲染入视图并通过响应返回用户。

#### 搭建SpringMVC框架
