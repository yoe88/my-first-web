<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"/>
<sec:authentication property="principal.username" var="username" />
<sec:authentication property="principal.profileImage" var="profileImage" />

<main class="container my-3">
    <section class="row">
        <div class="col-md-6 my-2 p-0 px-2">
            <div class="card h-100">
                <div class="card-header">프로필사진 / 닉네임</div>
                <div class="card-body">
                    <img class="rounded-circle" src="${contextPath}/file/thumb/${username}/${profileImage}" alt="" width="60" height="60">
                    <div class="d-inline-block ml-5">
                        <strong>닉네임</strong><span class="ml-2">${m.name}</span>
                    </div>
                </div>
                <div class="card-footer bg-white">
                    <button class="btn btn-outline-primary">수정하기</button>
                </div>
            </div>
        </div>
        <div class="col-md-6 my-2 p-0 px-2">
            <div class="card h-100">
                <div class="card-header">비밀번호 / 이메일</div>
                <div class="card-body">
                    <div>
                        <strong class="w-25 d-inline-block">비밀번호</strong><span class="ml-2">암호화 되어있습니다.</span><br><br>
                    </div>
                    <div>
                        <strong class="w-25 d-inline-block">이메일</strong><span class="ml-2">${m.email}</span>
                    </div>
                </div>
                <div class="card-footer bg-white">
                    <button class="btn btn-outline-primary">수정하기</button>
                </div>
            </div>
        </div>
        <div class="col-md-6 my-2 p-0 px-2">
            <div class="card h-100">
                <div class="card-header">주소</div>
                <div class="card-body">
                    <div>
                        <strong class="w-25 d-inline-block">우편번호</strong><span class="ml-2">${m.zonecode}</span><br><br>
                    </div>
                    <div>
                        <p><strong class="w-25 d-inline-block">도로명</strong><span class="ml-2">${m.address}</span></p>
                    </div>
                    <div>
                        <strong class="w-25 d-inline-block">주소</strong><span class="ml-2">${m.detailAddress}</span>
                    </div>
                </div>
                <div class="card-footer bg-white">
                    <button class="btn btn-outline-primary">수정하기</button>
                </div>
            </div>
        </div>
    </section>

</main>