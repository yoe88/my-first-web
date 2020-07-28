<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<sec:authentication property="principal.username" var="username" />
<sec:authorize access="hasRole('ROLE_ADMIN')" var="isAdmin" />

<main class="container" id="gallery-detail">
    <table class="table">
        <tr>
            <td>
                <h4 class="d-inline">${model.title}</h4><a href="${contextPath}/admin/galleries${qs}" class="btn btn-outline-danger float-right mt-1">목록</a>
            </td>
        </tr>

        <tr>
            <td>
                <div>
                    <c:forEach items="${model.file}" var="f">
                        <%--원본으로? 썸네일로?--%>
                        <div class="text-center mb-3">
                            <a target="_blank" href="${contextPath}/file/original/gallery/${model.gno}/${f.fileName}?o=${f.originalFileName}" style="text-decoration: none">
                                <img src="${contextPath}/file/original/gallery/${model.gno}/${f.fileName}" alt="picture" style="max-width: 100%">
                            </a>
                        </div>
                    </c:forEach>
                </div>
            </td>
        </tr>
    </table>
    <c:if test="${isAdmin || username == model.writer}"> <%--관리자거나 글쓴이--%>
    <section class="container d-flex justify-content-around mb-3">
        <sec:authorize access="hasRole('ROLE_ADMIN')"> <%--관리자 이상만 비공개 처리 가능--%>
            <input type="button" value="${model.pub ? "비공개하기" : "공개하기"}" class="btn btn-outline-purple" onclick="toggleGalleryPub('${model.gno}',this);">
        </sec:authorize>
        <c:if test="${username == model.writer}">        <%--글쓴이만--%>
            <a href="${contextPath}/galleries/${model.gno}/edit" class="btn btn-outline-success">수정하기</a>
        </c:if>
        <input type="button" value="삭제하기" class="btn btn-outline-danger" onclick="deleteGallery('${model.gno}');">
    </section>
    </c:if>
</main>
<script src="<c:url value="/resources/js/commons.js" />"></script>
<script src="<c:url value="/resources/js/gallery.js" />"></script>
<script src="<c:url value="/resources/js/admin.js" />"></script>
