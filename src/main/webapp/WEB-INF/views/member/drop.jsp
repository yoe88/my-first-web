<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"/>
<sec:authentication property="principal.username" var="username" />
<sec:authentication property="principal.profileImage" var="profileImage" />
<main class="container my-3">
    <h4>${page_title}</h4>

    <section class="my-3">
        <div class="justify-content-center">
            <div class="row justify-content-md-center">
                <input type="password" class="form-control col-md-7 mb-3"  autocomplete="new-password" placeholder="비밀번호">
                <input type="password" class="form-control col-md-7"       autocomplete="new-password" placeholder="비밀번호 확인">
            </div>
        </div>

        <div class="text-center mt-3">
            <button type="button" id="apply" class="btn btn-success mr-3" onclick="dropMember();">탈퇴하기</button>
            <a href="${contextPath}/member/me" class="btn btn-danger mx-3">취소</a>
        </div>
    </section>
</main>
<script src="<c:url value="/resources/js/commons.js" />"></script>
<script src="<c:url value="/resources/js/member/modify.js" />"></script>

