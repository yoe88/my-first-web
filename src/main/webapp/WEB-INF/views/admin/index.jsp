<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"/>
<main class="container mb-5" id="admin-index">
	<div class="row">
		<div class="col i-1 rounded">
			<a href="admin/members">
				<i class="fas fa-users" style="color: #7d5757"></i>
				<h2>회원관리</h2>
			</a>
		</div>
		<div class="col i-2 rounded">
			<a href="admin/boards">
				<i class="far fa-clipboard" style="color: #8f37d2"></i>
				<h2>게시글관리</h2>
			</a>
		</div>
		<div class="col i-3 rounded">
			<a href="admin/galleries">
				<i class="fas fa-images" style="color: #654dc7"></i>
				<h2>갤러리관리</h2>
			</a>
		</div>
	</div>
</main>
