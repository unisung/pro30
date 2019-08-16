<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt"  uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	 request.setCharacterEncoding("UTF-8");
%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>헤더</title>
</head>
<body>
<table border="0" width="100%">
<tr>
  <td>
  <a href="${contextPath}/main.do">
  	<img src="${contextPath}/resources/image/duke_swing.gif"/>
  </a>
  </td>
  <td>
  <h1><font size=30>스프링페이지</font> </h1>
  </td>
  <td>
   <!-- request,session,application -->
  <c:choose>
  <c:when test="${isLogOn==true && member !=null}">
  	<h3>환영합니다.${member.name }님!</h3>
  	<a href="${contextPath}/member/logout.do"><h3>로그아웃</h3></a>
  </c:when>
  <c:otherwise>
  	<a href="${contextPath}/member/loginForm.do"><h3>로그인</h3></a>
  </c:otherwise>  
  </c:choose>
  </td>
</tr>
</table>
</body>
</html>