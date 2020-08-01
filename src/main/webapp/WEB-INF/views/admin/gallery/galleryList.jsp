<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<style>

</style>
<main class="mb-5 px-3 clearfix" id="gallery-index">
    <%--<section class="px-3 mb-3">
        <a href="galleries/new" class="btn btn-purple">이미지 올리기</a>
    </section>--%>
    <c:if test="${listTotalCount != 0}">
    <section class="px-3 mb-3">
        <button class="btn btn-purple" onclick="updateGalleriesPub();">일괄 공개</button>
        <button class="ml-5 btn btn-outline-purple" onclick="checkedGalleriesPub();">전체 선택</button>
        <button class="btn btn-outline-danger" onclick="unCheckedGalleriesPub();">전체 해제</button>
    </section>
    </c:if>

    <c:if test="${not empty list}">
    <c:set var="allNo" value=""/>
    <section id="gallery-img">
        <ul>
            <c:forEach items="${list}" var="g">
                <c:set var="allNo" value="${allNo} ${g.gno}" />
                <li>
                    <div class="responsive-gallery">
                        <div class="gallery">
                            <span class="check-box ${g.pub == '1' ? "checked" : ""}" data-gno="${g.gno}" onclick="toggleGalleryChecked(this);"></span>
                            <a href="galleries/${g.gno}">
                                <c:if test="${g.cnt != '1'}">
                                    <span aria-label="슬라이드" class="multiple"></span>
                                </c:if>
                                <img src="${contextPath}/file/thumb/gallery/${g.gno}/${g.fileName}/size?w=250&h=250" alt="picture">
                            </a>
                        </div>
                    </div>
                </li>
            </c:forEach>
            <input type="hidden" name="allNo" value="${allNo}">
        </ul>
    </section>
    </c:if>

    <c:if test="${empty list}">
        <section class="container" id="empty">
            <div class="text-center">
                <img src=" <c:url value="/resources/images/first.png"/>">
                <h1 class="font-Jua">첫번째 글의 주인공이 되어보세요!</h1>
            </div>
        </section>
    </c:if>

    <section class="container my-2 text-center">
        <c:set var="startNum" value="${p-2}"/>
        <nav class="pager">
            <ul class="pagination">
                <c:if test="${p != 1}">
                    <li class="page-item"><a class="page-link" href="?p=1"><i class="fas fa-angle-double-left"></i></a></li>
                    <li class="page-item"><a class="page-link" href="?p=${p-1}"><i class="fas fa-angle-left"></i></a></li>
                </c:if>
                <c:forEach var="i" begin="0" end="4">
                    <c:set var="currentPageNum" value="${startNum + i}"/>
                    <c:if test="${currentPageNum > 0 and currentPageNum <= pageMaxNum}">
                        <li class="page-item ${currentPageNum == p ? "active" : ""}">
                            <a class="page-link" href="<c:if test="${currentPageNum == p}">javascript:;</c:if>
								<c:if test="${currentPageNum != p}">?p=${currentPageNum}</c:if> ">${currentPageNum}
                            </a>
                        </li>
                    </c:if>
                </c:forEach>
                <c:if test="${p < pageMaxNum}">
                    <li class="page-item"><a class="page-link" href="?p=${p+1}"><i class="fas fa-angle-right"></i></a></li>
                    <li class="page-item"><a class="page-link" href="?p=${pageMaxNum}"><i class="fas fa-angle-double-right"></i></a></li>
                </c:if>
            </ul>
        </nav>
    </section>
</main>
<script src="<c:url value="/resources/js/commons.js"/>"></script>
<script src="<c:url value="/resources/js/gallery.js"/>"></script>
