<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"/>
<sec:authentication property="principal.username" var="username" />
<sec:authentication property="principal.profileImage" var="profileImage" />

<main class="container">
    <section class="row">
        <div class="col-md-6 my-2 p-0 px-2">
            <div class="card h-100">
                <div class="card-header">프로필사진 / 닉네임</div>
                <div class="card-body">
                    <a target="_blank" href="${contextPath}/file/original/${username}/${profileImage}" style="text-decoration: none">
                        <img class="border rounded-circle" src="${contextPath}/file/thumb/${username}/${profileImage}?w=60&h=60" alt="프로필사진" width="60" height="60">
                    </a>
                    <div class="d-inline-block ml-5">
                        <strong>닉네임</strong><span class="ml-2">${m.name}</span>
                    </div>
                </div>
                <div class="card-footer bg-white">
                    <a href="edit/profile" class="btn btn-outline-primary">수정하기</a>
                </div>
            </div>
        </div>
        <div class="col-md-6 my-2 p-0 px-2">
            <div class="card h-100">
                <div class="card-header">아이디 / 이메일</div>
                <div class="card-body">
                    <div>
                        <strong class="w-25 d-inline-block">아이디</strong><span class="ml-2">${m.id}</span><br><br>
                    </div>
                    <div>
                        <strong class="w-25 d-inline-block">이메일</strong><span class="ml-2">${m.email}</span>
                    </div>
                </div>
                <%--<div class="card-footer bg-white">
                    <a href="edit/info" class="btn btn-outline-primary">수정하기</a>
                </div>--%>
            </div>
        </div>
        <div class="col-md-6 my-2 p-0 px-2">
            <div class="card h-100">
                <div class="card-header">비밀번호</div>
                <div class="card-body">
                    <div>
                        <strong class="w-25 d-inline-block">비밀번호</strong><span class="ml-2">암호화 되어 있습니다.</span><br><br>
                    </div>
                </div>
                <div class="card-footer bg-white">
                    <a href="edit/password" class="btn btn-outline-primary">수정하기</a>
                </div>
            </div>
        </div>
        <div class="col-md-6 my-2 p-0 px-2">
            <div class="card h-100">
                <div class="card-header">주소</div>
                <div class="card-body">
                    <div>
                        <strong class="w-25 d-inline-block">우편번호</strong><span class="ml-2">${m.zoneCode}</span><br><br>
                    </div>
                    <div>
                        <p><strong class="w-25 d-inline-block">도로명</strong><span class="ml-2">${m.address}</span></p>
                    </div>
                    <div>
                        <strong class="w-25 d-inline-block">주소</strong><span class="ml-2">${m.detailAddress}</span>
                    </div>
                </div>
                <div class="card-footer bg-white">
                    <a href="edit/address" class="btn btn-outline-primary">수정하기</a>
                </div>
            </div>
        </div>
    </section>

</main>