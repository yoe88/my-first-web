<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"/>
<main>
	<section class="container">
		<div>
			<h1 class="h1-title">* 자유게시판 *</h1>
		</div>

		<!--검색창-->
		<div class="text-right my-3 float-right">
			<form action="">
				<div class="input-group">
					<div class="input-group-prepend">
						<select class="form-control" name="f">
							<option value="title">제목</option>
							<option value="writer">작성자</option>
						</select>
					</div>
					<input type="text" class="form-control" name="q">
					<div class="input-group-append">
						<button type="submit" class="form-control btn-purple"><i class="fa fa-search"></i></button>
					</div>
				</div>
			</form>
		</div>

		<!--게시글목록-->
		<div class="table-responsive">
			<table class="table tbl-board">
				<thead>
				<tr class="text-center">
					<th class="">번호</th>
					<th class="">제목</th>
					<th class="">글쓴이</th>
					<th class="">작성일</th>
					<th class="">추천</th>
					<th class="">조회</th>
				</tr>
				</thead>
				<tbody>
				<tr class="text-center">
					<td>19922</td>
					<td class="text-left tbl-title"><a href="">abcdefghijklmnopqrstuvwxyz</a></td>
					<td class=""><span></span></td>
					<td>20.03.22</td>
					<td>15012</td>
					<td>203</td>
				</tr>
				<c:if test="${list != null}">
					<c:forEach items="${list}" var="b">
						<tr class="text-center">
							<td>${b.articleNo}</td>
							<td class="text-left tbl-title"><a href="">${b.title}</a></td>
							<td class="">${b.writer}</td>
							<td>20.03.22</td>
							<td>15012</td>
							<td>203</td>
						</tr>
					</c:forEach>
				</c:if>
				</tbody>
			</table>
		</div>
	</section>

	<section class="container my-3 text-center">
		<div class="float-left">
			<a href="${contextPath}/boards/new"><button class="btn btn-outline-danger">글쓰기</button></a>
		</div>
		<nav class="pager">
			<ul class="pagination" style="align-items: center;">
				<li class="page-item"><a class="page-link" href="#"><i class="fas fa-angle-double-left"></i></a></li>
				<li class="page-item"><a class="page-link" href="#"><i class="fas fa-angle-left"></i></a></li>
				<li class="page-item"><a class="page-link" href="#">1</a></li>
				<li class="page-item"><a class="page-link" href="#">2</a></li>
				<li class="page-item"><a class="page-link" href="#">3</a></li>
				<li class="page-item"><a class="page-link" href="#"><i class="fas fa-angle-right"></i></a></li>
				<li class="page-item"><a class="page-link" href="#"><i class="fas fa-angle-double-right"></i></a></li>
			</ul>
		</nav>
	</section>
</main>
<%--<script src="<c:url value="/resources/js/commons.js" />"></script>--%>
<script src="<c:url value="/resources/js/boards.js" />"></script>