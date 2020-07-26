<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<style>

</style>
<main class="mb-5 px-3 clearfix" id="gallery-index">
    <section class="px-3 mb-3">
        <a href="galleries/new" class="btn btn-purple">이미지 올리기</a>
    </section>

    <c:if test="${not empty list}">
    <section id="gallery-img">
        <ul>
            <c:forEach items="${list}" var="g">
                <li>
                    <div class="responsive-gallery">
                        <div class="gallery">
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
        <nav class="pager">
            <ul class="pagination">
                <c:if test="${p != 1}">
                    <li class="page-item"><a class="page-link" href="?p=1"><i class="fas fa-angle-double-left"></i></a></li>
                    <li class="page-item"><a class="page-link" href="?p=${p-1}"><i class="fas fa-angle-left"></i></a></li>
                </c:if>
                <c:forEach begin="${p-2 > 0 ? p-2 : 1}" end="${p+2}" varStatus="st">
                    <c:if test="${st.current <= pageMaxNum}">
                        <li class="page-item ${p==st.current ? "active" : ""}">
                            <a class="page-link" href="<c:if test="${st.current == p}">javascript:;</c:if>
								<c:if test="${st.current != p}">?p=${st.current}</c:if> ">${st.current}
                            </a>
                        </li>
                    </c:if>
                </c:forEach>
                <c:if test="${p != pageMaxNum and pageMaxNum !=0}">
                    <li class="page-item"><a class="page-link" href="?p=${p+1}"><i class="fas fa-angle-right"></i></a></li>
                    <li class="page-item"><a class="page-link" href="?p=${pageMaxNum}"><i class="fas fa-angle-double-right"></i></a></li>
                </c:if>
            </ul>
        </nav>
    </section>
</main>
