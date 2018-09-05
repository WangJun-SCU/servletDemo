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
