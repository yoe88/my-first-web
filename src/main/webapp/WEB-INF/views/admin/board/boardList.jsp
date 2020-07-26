<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"/>
<main id="board-list">
    <section class="container">
        <div>
            <h1 class="h1-title">관리자 게시판</h1>
        </div>

        <!--검색창-->
        <div class="text-right my-3 float-right">
            <form action="" onsubmit="return notAllowEmpty(this);">
                <div class="input-group">
                    <div class="input-group-prepend">
                        <select class="form-control" name="f">
                            <option value="title" ${f == "title" ? "selected" : ""}>제목</option>
                            <option value="writer" ${f == "writer" ? "selected" : ""}>작성자</option>
                        </select>
                    </div>
                    <input type="text" class="form-control" name="q" id="q" value="${param.q}">
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
                    <th>번호</th>
                    <th>제목</th>
                    <th>작성자</th>
                    <th>작성일</th>
                    <th>추천</th>
                    <th>조회</th>
                    <th>공개</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${list != null}">
                    <c:set var="allNo" value=""/>
                    <c:forEach items="${list}" var="b" varStatus="st">
                        <c:set var="allNo" value="${allNo} ${b.articleNo}" />
                        <tr class="text-center">
                            <td>${b.articleNo}</td>
                            <td class="text-left tbl-title">
                                <a href="${contextPath}/admin/boards/${b.articleNo}" style="margin-left: ${b.lv * 14}px;">
                                    <c:if test="${b.lv != 0}"><span class="icon-reply"></span></c:if>${b.title}</a>
                                <c:if test="${b.cmt !=0}">
                                    <span class="reply-count">${b.cmt}</span>
                                </c:if>
                            </td>
                            <td>${b.writer}</td>
                            <td>
                                <fmt:parseDate value="${b.regDate}" pattern="yyyy-MM-dd'T'HH:mm" var="dateTime" type="both"/>
                                <c:if test="${isNow[st.index]}">
                                    <fmt:formatDate pattern="HH:mm" value="${dateTime}" />
                                </c:if>
                                <c:if test="${not isNow[st.index]}">
                                    <fmt:formatDate pattern="yy.MM.dd" value="${dateTime}" />
                                </c:if>
                            </td>
                            <td>${b.recommend}</td>
                            <td>${b.hit}</td>
                            <td><input type="checkbox" name="no" value="${b.articleNo}" ${b.pub ? "checked" : ""} ></td>
                        </tr>
                    </c:forEach>
                    <input type="hidden" name="allNo" value="${allNo}">
                </c:if>
                <c:if test="${listTotalCount == 0}">
                    <tr class="text-center"><td colspan="7">검색된 결과가 없습니다.</td></tr>
                </c:if>
                </tbody>
            </table>
            <c:if test="${listTotalCount != 0}">
                <input type="button" class="btn btn-outline-dark float-right" value="일괄공개" onclick="updateBoardsPub()">
            </c:if>
        </div>
        <div class="clearfix">
            <h5 class="float-right"><span class="current-page-num">${p}</span>/<span class="font-weight-normal">${pageMaxNum}</span></h5>
        </div>
    </section>

    <section class="container my-2 text-center clearfix">

        <nav class="pager">
            <ul class="pagination">
                <c:if test="${p != 1}">
                    <li class="page-item"><a class="page-link" href="?f=${f}&q=${param.q}&p=1"><i class="fas fa-angle-double-left"></i></a></li>
                    <li class="page-item"><a class="page-link" href="?f=${f}&q=${param.q}&p=${p-1}"><i class="fas fa-angle-left"></i></a></li>
                </c:if>
                <c:forEach begin="${p-2 > 0 ? p-2 : 1}" end="${p+2}" varStatus="st">
                    <c:if test="${st.current <= pageMaxNum}">
                        <li class="page-item ${p==st.current ? "active" : ""}">
                            <a class="page-link" href="<c:if test="${st.current == p}">javascript:;</c:if>
													<c:if test="${st.current != p}">?f=${f}&q=${param.q}&p=${st.current}</c:if> ">${st.current}</a>
                        </li>
                    </c:if>
                </c:forEach>
                <c:if test="${p != pageMaxNum and pageMaxNum !=0}">
                    <li class="page-item"><a class="page-link" href="?f=${f}&q=${param.q}&p=${p+1}"><i class="fas fa-angle-right"></i></a></li>
                    <li class="page-item"><a class="page-link" href="?f=${f}&q=${param.q}&p=${pageMaxNum}"><i class="fas fa-angle-double-right"></i></a></li>
                </c:if>
            </ul>
        </nav>
    </section>
</main>
<script src="<c:url value="/resources/js/commons.js"/>"></script>
<script src="<c:url value="/resources/js/board.js"/>"></script>