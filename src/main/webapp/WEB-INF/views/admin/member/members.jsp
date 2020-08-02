<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"/>
<main class="container mb-5" id="admin-members">

    <!--검색창-->
    <div class="text-right my-3 float-right">
        <form action=""> <!-- 여기선 공백 검색 허용 -->
            <div class="input-group">
                <div class="input-group-prepend">
                    <select class="form-control" name="f">
                        <option value="id" ${f == "id" ? "selected" : ""}>아이디</option>
                        <option value="name" ${f == "name" ? "selected" : ""}>닉네임</option>
                    </select>
                </div>
                <input type="text" class="form-control" name="q" id="q" value="${param.q}">
                <div class="input-group-append">
                    <button type="submit" class="form-control btn-purple"><i class="fa fa-search"></i></button>
                </div>
            </div>
        </form>
    </div>

    <table class="table font-pretty-night">
        <thead>
            <tr>
                <th>아이디</th>
                <th>닉네임</th>
                <th>가입일</th>
                <th>회원등급</th>
            </tr>
        </thead>
        <tbody>
        <c:if test="${list != null}">
            <c:forEach items="${list}" var="m">
                <tr>
                    <td><a href="members/${m.id}">${m.id}</a></td>
                    <td>${m.name}</td>
                    <td class="text-center"><fmt:formatDate value="${m.regDate}" pattern="yyyy.MM.dd" /></td>
                    <td class="text-center">${m.roleName}</td>
                </tr>
            </c:forEach>
        </c:if>
        <c:if test="${listTotalCount == 0}">
            <tr class="text-center"><td colspan="4">검색된 결과가 없습니다.</td></tr>
        </c:if>
        </tbody>
    </table>
    <div class="clearfix">
        <h5 class="float-right"><span class="current-page-num">${p}</span>/<span class="font-weight-normal">${pageMaxNum}</span></h5>
    </div>
    <section class="container my-2 text-center clearfix">
        <c:set var="startNum" value="${p-2}"/>
        <nav class="pager">
            <ul class="pagination">
                <c:if test="${p != 1}">
                    <li class="page-item"><a class="page-link" href="?f=${f}&q=${param.q}&p=1"><i class="fas fa-angle-double-left"></i></a></li>
                    <li class="page-item"><a class="page-link" href="?f=${f}&q=${param.q}&p=${p-1}"><i class="fas fa-angle-left"></i></a></li>
                </c:if>
                <c:forEach var="i" begin="0" end="4">
                    <c:set var="currentPageNum" value="${startNum + i}"/>
                    <c:if test="${currentPageNum > 0 and currentPageNum <= pageMaxNum}">
                        <li class="page-item ${currentPageNum == p ? "active" : ""}">
                            <a class="page-link" href="<c:if test="${currentPageNum == p}">javascript:;</c:if>
								<c:if test="${currentPageNum != p}">?f=${f}&q=${param.q}&p=${currentPageNum}</c:if> ">${currentPageNum}
                            </a>
                        </li>
                    </c:if>
                </c:forEach>
                <c:if test="${p < pageMaxNum}">
                    <li class="page-item"><a class="page-link" href="?f=${f}&q=${param.q}&p=${p+1}"><i class="fas fa-angle-right"></i></a></li>
                    <li class="page-item"><a class="page-link" href="?f=${f}&q=${param.q}&p=${pageMaxNum}"><i class="fas fa-angle-double-right"></i></a></li>
                </c:if>
            </ul>
        </nav>
    </section>
</main>
