<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="ko">

<head>
    <title>로그인</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!--부트스트랩 설정-->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
    <!-- 아이콘  설정-->
    <script src="https://kit.fontawesome.com/9766199556.js" crossorigin="anonymous"></script>

    <!-- 내가만든 스타일 -->
    <!-- <link rel="shortcut icon" href="./favicon.ico">
    <link rel="icon" href="./favicon.ico">
    <link rel="stylesheet" href="../css/myStyle.css"> -->
   	<link rel="shortcut icon" href="<c:url value="/resources/images/favicon.ico" />">
    <link rel="icon" href="<c:url value="/resources/images/favicon.ico" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/myStyle.css" />">
</head>

<body>
    <div class="login">
        <div class="container p-4 bg-white col-md-7" style="border-radius: 1.25rem;">
            <form action="loginProcess" method="post" onsubmit="return validateLogin(this);">
                <fieldset>
                    <legend class="text-center">Login</legend>
                    <div>
                        <input class="form-control my-1" type="text" placeholder="아이디" name="id" autocomplete="off" value="${id}"
                            autofocus onkeydown="errTextInit();">
                            <!--minlen4 maxlen12 required-->
                        <span class="error-id small"></span>
                    </div>

                    <div>
                        <input class="form-control my-1" type="password" placeholder="비밀번호" name="password"
                          autocomplete="off" onkeydown="errTextInit();">
                            <!--minlen4 maxlen20 required-->
                        <span class="error-pw  small">${errMsg}</span>
                    </div>
                    
                    <div class="custom-control custom-checkbox ">
                        <input type="checkbox" id="jb-checkbox" name="remember-me" class="custom-control-input">
                        <label class="custom-control-label" for="jb-checkbox">로그인 상태 유지</label>
                    </div>
                    
                    <div>
                        <button class="form-control bg-purple bg-purple-hover">로그인</button>
                    </div>

                    <div class=" small mt-2">
                        <div class="float-left">
                            <a class="text-muted" href="register"><i class="fas fa-user-lock pr-1"></i>아이디</a>
                            <a class="text-muted" href="register"><i class="fas fa-circle pr-1"></i>비밀번호찾기</a>
                        </div>
                        <div class="float-right">
                            <a class="text-muted" href="register"><i class="fas fa-user-plus pr-1"></i>회원가입</a>
                        </div>
                    </div>

                </fieldset>
            </form>
            <a href="${contextPath}/index" class="btn btn-outline-danger float-right my-2">돌아가기</a>
        </div>
    </div>
    <!-- <script src="../js/member.js"></script> -->
    <script src="<c:url value="/resources/js/member.js" />"></script>
</body>
</html>