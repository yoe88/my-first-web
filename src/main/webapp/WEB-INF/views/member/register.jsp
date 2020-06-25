<%@ page contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="ko">

<head>
    <title>회원가입</title>
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

    <!-- 다음 주소 API -->
    <script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
</head>

<body>
    <div class="background-overlay"></div>
        <section class="register col-md-7 bg-white px-0 py-1 p-md-3 mt-md-5 rounded-lg clearfix ">
            <h2 class="">회원가입</h2>
            
            <form action="new" method="POST" onsubmit="return addMember(this);">
                <input type="hidden" id="idCheck" value="N"> <%--아이디중복확인 --%>
                <input type="hidden" id="emailCheck" value="N">  <%--이메일중복확인 --%>
                <input type="hidden" id="isNewCode" value="N">   <%--인증번호를 받았는지--%>
                <input type="hidden" id="codeCheck" value="N">    <%--이메일인증확인 --%>

                <span class="error-">*필수사항</span>
                <div class="ID">
                    <div class="input-group my-1">
                        <div class="input-group-prepend">
                            <span class="input-group-text"><i class="fas fa-address-card"></i></span>
                        </div>
                        <input type="text" class="form-control" placeholder="아이디 (공백제외 4~12자의 영문 소문자, 숫자)" name="id" autocomplete="off" autofocus onkeyup="resetId();">
                        <div class="input-group-append">
                            <button type="button" class="input-group-text check-id" onclick="checkId();">중복확인</button>
                        </div>
                    </div>
                    <span class="error-id small"></span>
                </div>

                <div class="PASSWORD">
                    <div class="input-group my-1">
                        <div class="input-group-prepend">
                            <span class="input-group-text"><i class="fas fa-lock"></i></span>
                        </div>
                        <input type="password" class="form-control" placeholder="비밀번호" name="password" autocomplete="new-password">
                    </div>
                    <div class="input-group my-1">
                        <div class="input-group-prepend">
                            <span class="input-group-text"><i class="fas fa-key"></i></span>
                        </div>
                        <input type="password" class="form-control" placeholder="비밀번호 확인" autocomplete="new-password">
                    </div>
                    <span class="error-pw small"></span>
                </div>

                <div class="NAME">
                    <div class="input-group my-1">
                        <div class="input-group-prepend">
                            <span class="input-group-text"><i class="fas fa-id-badge"></i></span>
                        </div>
                        <input type="text" class="form-control" placeholder="닉네임 (공백 특수문자를 제외한 1~8글자)" name="name" autocomplete="off">
                    </div>
                    <span class="error-name small"></span>
                </div>

                <div class="EMAIL">
                    <div class="input-group my-1">
                        <div class="input-group-prepend">
                            <span class="input-group-text"><i class="fas fa-envelope"></i></span>
                        </div>
                        <input type="email" class="form-control" placeholder="이메일" name="email" onkeydown="resetEmail();">
                        <div class="input-group-append">
                            <button type="button" class="input-group-text check-email" onclick="validateEmail();">중복확인</button>
                        </div>
                    </div>
                    <span class="error-email small"></span>
                    <div class="float-right">
                        <input type="button" id="gCode" class="btn btn-outline-primary btn-sm mb-1 hide" onclick="getCode();" value="인증번호 받기">
                    </div>

                    <div class="input-group my-1">
                        <input type="text" class="form-control" placeholder="인증번호" id="code">

                        <div class="input-group-append">
                            <button type="button" class="input-group-text" onclick="sendCode();">인증번호 확인</button>
                        </div>
                    </div>
                    <span class="error-code small"></span>
                </div>
                <span class="">*선택사항</span>
                <div class="daum-address">
                    <div class="input-group my-1">
                        <div class="input-group-prepend">
                            <span class="input-group-text"><i class="fas fa-home"></i></span>
                        </div>
                        
                        <input type="text" id="zonecode" name="zoneCode" class="form-control d-inline" placeholder="우편번호" autocomplete="off" readonly>

                        <div class="input-group-append">
                            <button type="button" onclick="execDaumPostcode()" class="input-group-text">우편번호 찾기</button><br>
                        </div>
                    </div>

                    <input type="text" id="address" name="address" placeholder="주소" class="form-control float-left" autocomplete="off" readonly style="width: 50%;">
                    <input type="text" id="extraAddress" name="extraAddress" placeholder="참고항목" class="form-control float-left" autocomplete="off" readonly style="width: 50%;">
                    <input type="text" id="detailAddress" name="detailAddress" placeholder="상세주소" class="form-control d-inline ">

                    <div id="wrap" style="display:none;border:1px solid;width:500px;height:300px;margin:5px 0;position:relative">
                        <img src="//t1.daumcdn.net/postcode/resource/images/close.png" id="btnFoldWrap" style="cursor:pointer;position:absolute;right:0px;top:-1px;z-index:1" onclick="foldDaumPostcode()" alt="접기 버튼">
                    </div>
                </div>
                <div class="my-3">
                    <input type="hidden">
                    <button class="form-control bg-purple bg-purple-hover">가입하기</button>
                </div>
            </form>
            <a href="${contextPath}/index" class="btn btn-outline-danger float-right">돌아가기</a>
   
        </section>


    <!-- <script src="../js/member.js"></script> -->
    <script src="<c:url value="/resources/js/commons.js" />"></script>
    <script src="<c:url value="/resources/js/member/DaumAddressAPI.js" />"></script>
    <script src="<c:url value="/resources/js/member/register.js" />"></script>
</body>
</html>