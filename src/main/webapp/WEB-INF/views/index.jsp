<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>    
<div class="container">
	<h1>
		Hello world!  
	</h1>
	<sec:authorize access="isAuthenticated()">일반사용자</sec:authorize>
	<sec:authorize access="hasRole('ROLE_ADMIN')">관리자</sec:authorize>
	<a href="para">나의 권한정보</a>
	<a href="admin">관리자페이지</a>
	
	
	<P>  The time on the server is ${serverTime}. </P><br><br>
	<a href="logout">로그아웃</a>
</div>
