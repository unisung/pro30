package com.myspring.pro30.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

//인터셉터는 Controller로 전달되는 request를 가로채서
//viewName을 만들어서 Controller로 보냄
public class ViewNameInterceptor extends HandlerInterceptorAdapter {

	//request가 Controller로 가기 전에 처리
	@Override
	public boolean preHandle(HttpServletRequest request, 
			                                 HttpServletResponse response, Object handler)
			throws Exception {
		try {
			String viewName=getViewName(request);
			request.setAttribute("viewName", viewName);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return true;
	}

//request객체로 부터 viewName을 만드는 메소드
		private String getViewName(HttpServletRequest request) throws Exception {
			String contextPath = request.getContextPath();
			String uri = (String) request.getAttribute("javax.servlet.include.request_uri");
			if (uri == null || uri.trim().equals("")) {
				uri = request.getRequestURI();
			}

			int begin = 0;
			if (!((contextPath == null) || ("".equals(contextPath)))) {
				begin = contextPath.length();
			}

			int end;
			if (uri.indexOf(";") != -1) {
				end = uri.indexOf(";");
			} else if (uri.indexOf("?") != -1) {
				end = uri.indexOf("?");
			} else {
				end = uri.length();
			}

			String viewName = uri.substring(begin, end);
			if (viewName.indexOf(".") != -1) {
				viewName = viewName.substring(0, viewName.lastIndexOf("."));
			}
			//action-servlet.xml의 "/WEB-INF/views/" 부분은 그대로 두고 이부분을 수정
			if (viewName.lastIndexOf("/") != -1) {
				viewName = viewName.substring(viewName.lastIndexOf("/",1), viewName.length());
			}
			return viewName;
		}
}
