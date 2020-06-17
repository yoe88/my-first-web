<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <%-- 타일즈를 사용하기 위한 태그라이브러리 --%>      
 <%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>     
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
     <%-- 부트스트랩 설정 --%>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
    
    <%-- 아이콘  설정--%>
    <script src="https://kit.fontawesome.com/9766199556.js" crossorigin="anonymous"></script>
    
    <%-- 내가만든 스타일 --%>
    <%-- <link rel="shortcut icon" href="./favicon.ico">
    <link rel="icon" href="./favicon.ico">
    <link rel="stylesheet" href="../css/mystyle.css"> --%>
    <link rel="shortcut icon" href="<c:url value="/resources/images/favicon.ico" />">
    <link rel="icon" href="<c:url value="/resources/images/favicon.ico" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/mystyle.css" />">
    
    <title><tiles:insertAttribute name="title" /></title>
</head>
<body>
    <tiles:insertAttribute name="header"/>
    <tiles:insertAttribute name="body"/>
    <tiles:insertAttribute name="footer"/>  
</body>
</html>