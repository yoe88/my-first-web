<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<c:set var="contextPath"  value="${pageContext.request.contextPath}"/>
<sec:authorize access="isAuthenticated()">
    <sec:authentication property="principal.username" var="username" />
    <sec:authentication property="principal.profileImage" var="profileImage" />
</sec:authorize>
<header>
        <nav class="navbar navbar-expand-md bg-purple navbar-dark mb-3 mb-md-5">
            <div class="container py-2">
                <a class="navbar-brand font-weight-bolder" href="${contextPath}/index" style="font-size: 150%;">YH</a>
                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="collapsibleNavbar">
                    <ul class="nav navbar-nav">
                        <li class="nav-item"><a class="nav-link" href="${contextPath}/boards"><i class="fas fa-clipboard pr-1"></i>게시판</a></li>
                        <li class="nav-item"><a class="nav-link" href="${contextPath}/galleries"><i class="fas fa-images pr-1"></i>포토갤러리</a></li>
                        <sec:authorize access="hasRole('ROLE_ADMIN')">
                        	<li class="nav-item"><a class="nav-link" href="${contextPath}/admin"><i class="fas fa-crown pr-1"></i>관리자</a></li>
                        </sec:authorize>
                    </ul>
                    <ul class="navbar-nav  ml-auto align-items-md-center">
                    	<sec:authorize access="isAnonymous()">
	                        <li class="nav-item"><a class="nav-link" href="${contextPath}/member/login"><i class="fas fa-sign-in-alt pr-1"></i>로그인</a></li>
	                        <li class="nav-item"><a class="nav-link" href="${contextPath}/member/register"><i class="fas fa-user-plus pr-1"></i>회원가입</a></li>
                        </sec:authorize>
                        <sec:authorize access="isAuthenticated()">
                            <li class="nav-item"><a class="d-md-none nav-link" href="${contextPath}/member/me"><i class="fas fa-user pr-1"></i>내정보</a></li>
                            <li class="nav-item"><a class="d-md-none nav-link" href="${contextPath}/logout"><i class="fas fa-sign-out-alt pr-1"></i>로그아웃</a></li>
                        	<li class="nav-item">
	                        	<div class="dropdown">
								    <!-- Trigger --> 
                                    <img class="ml-1 rounded-circle d-none d-md-block" width="50" height="50" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"
								     src="${contextPath}/file/thumb/${username}/${profileImage}?w=50&h=50" alt="profile" style="cursor: pointer;" />
								    <!-- Menu -->
								    <div class="dropdown-menu dropdown-menu-right">
								        <a class="dropdown-item" href="${contextPath}/member/me"><i class="fas fa-user pr-1"></i>내정보</a>
								        <a class="dropdown-item" href="${contextPath}/logout"><i class="fas fa-sign-out-alt pr-1"></i>로그아웃</a>
								    </div>
								</div>
							</li>
                        </sec:authorize>
                    </ul>
                </div>
            </div>
        </nav>
    </header>