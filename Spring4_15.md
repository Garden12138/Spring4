# Spring4
## Spring集成远程服务

#### 了解远程服务
* 远程调用：是客户端应用和服务端之间的会话。
[![远程调用.png](https://i.loli.net/2018/05/18/5afee70b15d25.png)](https://i.loli.net/2018/05/18/5afee70b15d25.png)
* 远程过程调用（remote procedure call）：RPC调用是执行流从一个应用传递给另一个应用，理论上另一个应用部署在跨网络的一台远程机器上。
* Spring通过多次远程调用技术支持RPC
```
RPC模型            适用场景
RMI                不考虑网络限制（防火墙），发布/访问基于Java服务
Hessian|Burlap     考虑网络限制（防火墙），通过HTTP发布/访问基于Java服务。Hessian基于二进制，Burlap基于XML
Http Invoker       考虑网络限制（防火墙），并希望使用基于XML或专有的序列化机制实现Java序列化时，发布/访问基于Spring的服务
JAX-RPC|JAX-WS     发布/访问平台独立的，基于SOAP的WEB服务
```
  * 工作原理：

  [![Spring将服务装配至客户端应用.png](https://i.loli.net/2018/05/19/5afff0aaae476.png)](https://i.loli.net/2018/05/19/5afff0aaae476.png)

  [![Spring将管理的Bean发布为远程服务.png](https://i.loli.net/2018/05/19/5afff21d696e1.png)](https://i.loli.net/2018/05/19/5afff21d696e1.png)

#### 发布和访问RMI服务（Spring配置）
* 服务端发布RMI服务
  * 原理：

  [![服务端发布RMI服务.png](https://i.loli.net/2018/05/19/5b001c23e9083.png)](https://i.loli.net/2018/05/19/5b001c23e9083.png)

  * 配置：
  ```
  //配置RMI服务导出器
  @Bean
  public RmiServiceExporter rmiServiceExporter(SpitterService spitterService){
    RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
    rmiServiceExporter.setService(spitterService);/*注入具体服务Bean*/
    rmiServiceExporter.setServiceName("SpitterService");/*设置服务名称*/
    rmiServiceExporter.setServiceInterface(SpitterService.class);/*设置服务接口*/
    rmiServiceExporter.setRegistryHost("rmi.spitter.com");/*设置服务注册表Host*/
    rmiServiceExporter.setRegistryPort("1199");/*设置服务注册表端口号*/
    return rmiServiceExporter;
  }
  ```

* 客户端访问RMI服务
  * 原理：

  [![客户端访问RMI服务.png](https://i.loli.net/2018/05/20/5b010af711206.png)](https://i.loli.net/2018/05/20/5b010af711206.png)

  * 配置:
  ```
  //配置RMI服务代理
  @Bean
  public RmiProxyFactoryBean spitterService(){
    RmiProxyFactoryBean rmiProxy = new RmiProxyFactoryBean();
    rmiProxy.setServiceUrl("rmi://localhost/SpitterService");/*设置服务URL*/
    rmiProxy.setServiceInterface(SpitterService.class);/*设置服务接口（与服务端一致）*/
    return rmiProxy;
  }
  ```
  ```
  //注入服务
  @Autowired
  private SpitterService SpitterService;
  ```
  
#### 发布和访问Hessian和Burlap服务

#### 发布和访问Spring HttpInvoker服务

#### 发布和访问Web服务
