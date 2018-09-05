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
