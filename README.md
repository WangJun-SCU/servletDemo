# Servlet学习笔记

工作以来就一直在用各种web框架，比如SpringMVC，Spring Cloud这些，向外提供接口都很方便，但是一直在想它的内部是怎样对外暴露服务的，正好之前学习java基础的时候也没好好看servlet这块，抽个时间学习总结一下，也能更好的理解掌握现在的web框架。

## 1. servlet简介

servlet的全称是server applet，服务端小程序，是运行在服务器端用java编写的程序，用于处理及响应客户端的需求，动态生成web内容。

Servlet是个特殊的java类，这个类必须继承HtppServlet，每个Servlet可以响应客户端的请求。Servlet提供不同的方法用于响应客户端请求：

- doGet：用于响应客户端的get请求； 
- doPost：用于响应客户端的post请求；
- doPut：用于响应客户端的put请求；
- doDelete：用于响应客户端的delete请求；

大部分时候，Servlet对所有请求的响应都是完全一样的，这个时候，我们就可以采用重写service()方法来代替上面的几个方法。

`void service(HttpServletRequest req, HttpServletResponse resp)`

一般情况下，在MVC应用中，Servlet扮演控制器的角色：

- Model：对应JavaBean；
- View：对应JSP页面；
- Controller：对应Servlet；

所以，在SpringMVC中对外暴露接口的本质就是一个servlet，它的名字是：org.springframework.web.servlet.DispatcherServlet。

## 2. servlet实例

大概了解了servlet之后，我们通过新建一个工程来使用原始的servlet对外暴露服务。

### 2.1 新建工程

使用eclipse新建一个动态web项目，

![](..\..\assets\servletDemo1.png)

项目建好后目录结构应该是这样的：

```
│  .classpath
│  .project
│
├─.settings
│      .jsdtscope
│      org.eclipse.jdt.core.prefs
│      org.eclipse.wst.common.component
│      org.eclipse.wst.common.project.facet.core.xml
│      org.eclipse.wst.jsdt.ui.superType.container
│      org.eclipse.wst.jsdt.ui.superType.name
│
├─build
│  └─classes
├─src
└─WebContent
    ├─META-INF
    │      MANIFEST.MF
    │
    └─WEB-INF
        └─lib
```

### 2.2 新建servlet

![](../../assets/servletDemo2.png)

![](../../assets/servletDemo3.png)

新建好后的java代码如下：

```java
package com.servletDemo.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MyServlet
 */
@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

```

### 2.3 编译打包

![](../../assets/servletDemo4.png)

将导出的war包丢进tomcat的webapps下面，重启tomcat。访问：http://localhost:8080/servletDemo/MyServlet，就可以看到返回结果。没错，就是这个简单！

![](../../assets/servletDemo5.png)

### 2.4 配置Servlet的两种方式

使用servlet有两种配置方式：

- Servlet类上使用@WebServlet注解进行配置
- 在web.xml文件中配置

**1）@WebServlet注解**

在上面的例子中就是使用WebServlet注解的方式配置的，`@WebServlet("/MyServlet")`的意思是如果请求是`/MyServlet`，则由`MyServlet`类的实例提供服务。`@WebServlet`注解还有很多其他属性，如下：

| 属性              | 类型           | 是否必须 | 说明                                      |
| ----------------- | -------------- | -------- | ----------------------------------------- |
| asyncSupported    | boolean        | 否       | 指定Servlet是否支持异步操作模式           |
| displayName       | String         | 否       | 指定Servlet显示名称                       |
| initParams        | WebInitParam[] | 否       | 配置初始化参数                            |
| loadOnStartup     | int            | 否       | 标记容器是否在应用启动时就加载这个Servlet |
| name              | String         | 否       | 指定Servlet名称                           |
| urlPatterns/value | String[]       | 否       | 这两个属性作用相同，指定Servlet处理的url  |

比如上面的类注解可以添加一些其他属性：

```java
@WebServlet(  
    name="servletdemo",   
    urlPatterns={"/MyServlet"},   
    loadOnStartup=1 
)  
public class MyServlet extends HttpServlet {
    
}
```

**2）web.xml配置**

除了注解的方式，还可以通过web.xml配置，在WEB-INF下新建`web.xml`，进行servlet配置：

```xml
<!DOCTYPE web-app PUBLIC
 "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
 "http://java.sun.com/dtd/web-app_2_3.dtd" >

<web-app>
  <display-name>My web app</display-name>
  
  <servlet>
      <servlet-name>servletdemo</servlet-name>
      <servlet-class>
         com.servletDemo.servlet.MyServlet
      </servlet-class>
      <!-- 表示启动容器时初始化此Servelt,调用init方法 -->
      <load-on-startup>1</load-on-startup>
   </servlet>

   <servlet-mapping>
      <servlet-name>servletdemo</servlet-name>  <!-- 需要和上面的servlet-name保持一致 -->
      <url-pattern>/MyServlet</url-pattern> <!-- url的匹配规则-->
   </servlet-mapping>
</web-app>

```

**注意：**配置了web.xml就需要把@WebServlet注解删掉，否则启动tomcat容器的时候会报错两个servlet指向同一个URL。

再重新编译打包丢进容器，重启并访问：http://localhost:8080/servletDemo/MyServlet。结果是一样的：

![](../../assets/servletDemo5.png)

### 2.5 一个Servlet处理多个URL请求

当浏览器发送了一次请求到服务器时，servlet容器会根据请求的url-pattern找到对应的Servlet类，执行对应的doPost或doGet方法，再将响应信息返回给浏览器，这种情况下，一个具体的Servlet类只能处理对应的web.xml中配置的url-pattern请求，一个Servlet类，一对配置信息。 但是我们知道现在大部分的javaweb框架都是可以编写一个java类而可以处理多个url请求的。那么使用原生的servlet如何做到呢？

有两种方法。一是根据请求的地址，截取其中的具体方法名，然后使用if-else判断匹配，再执行具体的方法。二是根据截取出来的方法名，使用反射机制，来执行具体的方法。 

具体解决方案可以参考：https://blog.csdn.net/codeMas/article/details/80696777。

## 2. Filter介绍

Filter可以认为是Servlet的一种“加强版”。它主要是对用户请求进行预处理，也可以对HttpServletResponse进行后续处理，是个典型的处理链。使用Filter的完整流程一般是：Filter对用户请求进行预处理，接着请求交给Servlet进行处理并生成响应，最后Filter再对服务器响应进行后续处理。

Filter可以拦截多个请求或响应，一个请求或响应也可以被多个Filter拦截。

创建Filter类需要实现`javax.servlet.Filter`接口，该接口定义了如下三个方法：

- void init(FilterConfig config)
- void destory()
- void doFilter(ServletRequest request, ServletResponse respone, FilterChain chain)

**Filter实例：**

1）编写Filter类

```java
package com.servletDemo.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class MyFilter implements Filter {
	public MyFilter() {
		
	}

	public void destroy() {
		System.out.println("进入Filter的destroy方法...时间：" + new Date());
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("进入Filter的doFilter方法...时间：" + new Date());
		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
		System.out.println("进入Filter的init方法...时间：" + new Date());
	}

}

```

2）配置web.xml

和servlet的配置一样，也需要配置url匹配策略，添加了Filter的web.xml如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:web="http://xmlns.jcp.org/xml/ns/javaee">
	<display-name>My web app</display-name>

	<filter>
		<filter-name>MyFilter</filter-name>
		<filter-class>com.servletDemo.servlet.MyFilter</filter-class>
	</filter>
	<filter-mapping>
		<filter-name>MyFilter</filter-name>
		<url-pattern>/MyServlet</url-pattern>
	</filter-mapping>

	<servlet>
		<servlet-name>servletdemo</servlet-name>
		<servlet-class>
			com.servletDemo.servlet.MyServlet
		</servlet-class>
		<load-on-startup>1</load-on-startup>
	</servlet>
	<servlet-mapping>
		<servlet-name>servletdemo</servlet-name>
		<url-pattern>/MyServlet</url-pattern>
	</servlet-mapping>
</web-app>
```

将工程部署上去，访问：

http://localhost:8080/servletDemo/MyServlet，

从控制台打印的日志可以看出，http请求先经过了filter再到servlet。日志打印顺序：

```shell
......
进入Filter的init方法...时间：Wed Sep 05 16:54:08 CST 2018
九月 05, 2018 4:54:08 下午 org.apache.catalina.startup.HostConfig deployWAR
信息: Deployment of web application archive D:\software\apache-tomcat-7.0.90\webapps\servletDemo.war has finished in 74 ms
......
信息: Deployment of web application directory D:\software\apache-tomcat-7.0.90\webapps\ROOT has finished in 56 ms
九月 05, 2018 4:54:12 下午 org.apache.coyote.AbstractProtocol start
信息: Starting ProtocolHandler ["http-apr-8080"]
九月 05, 2018 4:54:12 下午 org.apache.coyote.AbstractProtocol start
信息: Starting ProtocolHandler ["ajp-apr-8009"]
九月 05, 2018 4:54:12 下午 org.apache.catalina.startup.Catalina start
信息: Server startup in 9575 ms
进入Filter的doFilter方法...时间：Wed Sep 05 16:54:15 CST 2018
进入servlet的doget方法...时间：Wed Sep 05 16:54:15 CST 2018
```



## 3. Listener介绍

当web应用在web容器中运行时，web应用内部会不断的发生各种事件：web应用被启动、web应用被停止、用户session开始、用户session结束、用户请求到达等，通常来这些web事件对开发者是透明的，但是Servlet API提供了大量的监听器来监听web应用的内部事件，从而允许当web内部事件发生时回调事件监听器内的方法。使用Listener需要两步（和Servlet、Filter一样）：

- 定义Listener实现类；
- 通过注解或web.xml配置。

常用的web事件监听器接口有如下几个：

- ServletContextListener：用于监听web应用的启动和关闭；
- ServletContextAttributeListener：用于监听ServletContext范围（applocation）内属性的改变；
- ServletRequestListener：用于监听用户请求；
- ServletRequestAttributeListener：用于监听ServletRequest范围（request）内属性的改变；
- HttpSessionListener：用于监听用户session的开始和结束；
- HttpSessionAttributeListener：用于监听HttpSession范围（session）内属性的改变；

**Listener实例**

1）编写Listenter类，此处以监听web容器的启动和关闭作为例子，其他监听类型类似。

```java
package com.servletDemo.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class MyListener implements ServletContextListener {

    public MyListener() {
    	
    }

    public void contextDestroyed(ServletContextEvent paramServletContextEvent)  { 
    	System.out.println("web应用关闭了...");
    }

    public void contextInitialized(ServletContextEvent paramServletContextEvent)  { 
    	System.out.println("web应用启动了...");
    }
	
}

```

2）配置web.xml

listener的配置比较简单，只需要配置一个节点就可以了。

```xml
<listener>
    <listener-class>
        com.servletDemo.servlet.MyListener
    </listener-class>
</listener>
```

重启部署到tomcat，容器启动的时候会打印日志：

```shell
......
九月 05, 2018 5:13:37 下午 org.apache.catalina.startup.HostConfig deployWAR
信息: Deploying web application archive D:\software\apache-tomcat-7.0.90\webapps\servletDemo.war
web应用启动了...
进入Filter的init方法...时间：Wed Sep 05 17:13:38 CST 2018
九月 05, 2018 5:13:38 下午 org.apache.catalina.startup.HostConfig deployWAR
信息: Deployment of web application archive D:\software\apache-tomcat-7.0.90\webapps\servletDemo.war has finished in 127 ms
......
信息: Starting ProtocolHandler ["http-apr-8080"]
九月 05, 2018 5:13:42 下午 org.apache.coyote.AbstractProtocol start
信息: Starting ProtocolHandler ["ajp-apr-8009"]
九月 05, 2018 5:13:42 下午 org.apache.catalina.startup.Catalina start
信息: Server startup in 10147 ms
```



> 参考：
>
> https://baike.baidu.com/item/servlet/477555?fr=aladdin
>
> https://www.cnblogs.com/xdp-gacl/p/3760336.html
>
> https://blog.csdn.net/codeMas/article/details/80696777
>
> https://www.cnblogs.com/whgk/p/6399262.html
>
> Servlet工作原理解析：https://www.ibm.com/developerworks/cn/java/j-lo-servlet/
>
> http://www.runoob.com/servlet/servlet-writing-filters.html

