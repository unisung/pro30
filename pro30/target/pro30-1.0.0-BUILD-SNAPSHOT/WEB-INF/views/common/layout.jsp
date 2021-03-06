<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
 #container{
 width: 100%;
 margin: 0px auto;/* 전체를 브라우저의 중앙 배치 */
 text-align: center;
 border:0px solid #bcbcbc;
 }
 #header{
 padding: 5px;
 margin-bottom: 5px;
 border: 0px solid #bcbcbc;
 background-color: lightgreen;
 }
 #sidebar-left{
 width: 15%;
 height: 700px;
 padding: 5px;
 margin-right: 5px;
 margin-bottom: 5px;
 float: left;
 background-color: yellow;
 border: 0px solid #bcbcbc;
 font-size: 10px;
 }
 #content{
 width: 75%;
 padding: 5px;
 margin-right: 5px;
 float: left;
 border: 0px solid #bcbcbc;
 }
 #footer{
 clear: both;
 padding: 5px;
 border:0px solid #bcbcbc;
 background-color: lightblue;
 }
</style>
<title><tiles:insertAttribute name="title"/></title>
</head>
<body>
<div id="container">
     <div id="header">
     	<tiles:insertAttribute name="header"/>
     </div>
     <div id="sidebar-left" >
     	<tiles:insertAttribute name="side"/>
     </div>
      <div id="body">
     	<tiles:insertAttribute name="body"/>
     </div>
     <div id="footer" >
     	<tiles:insertAttribute name="footer"/>
     </div>
</div>

</body>
</html>