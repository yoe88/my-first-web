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
    <link rel="stylesheet" href="../css/mystyle.css"> -->
    <link rel="shortcut icon" href="<c:url value="/resources/images/favicon.ico" />">
    <link rel="icon" href="<c:url value="/resources/images/favicon.ico" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/mystyle.css" />">

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

                <div class="PASSWD">
                    <div class="input-group my-1">
                        <div class="input-group-prepend">
                            <span class="input-group-text"><i class="fas fa-lock"></i></span>
                        </div>
                        <input type="password" class="form-control" placeholder="비밀번호" name="passwd" autocomplete="new-password">
                    </div>
                    <div class="input-group my-1">
                        <div class="input-group-prepend">
                            <span class="input-group-text"><i class="fas fa-key"></i></span>
                        </div>
                        <input type="password" class="form-control" placeholder="비밀번호 확인" autocomplete="new-password">
                    </div>
                    <span class="error-passwd small"></span>
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
                            <button type="button" class="input-group-text check-email" onclick="checkEmail();">중복확인</button>
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
                        
                        <input type="text" id="zonecode" name="zonecode" class="form-control d-inline" placeholder="우편번호" autocomplete="off" readonly>

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
                    <button class="form-control bg-purple bg-purple-hover">가입하기</button>
                </div>
            </form>
            <a href="${contextPath}/index" class="btn btn-outline-danger float-right">돌아가기</a>
   
        </section>


    <!-- <script src="../js/member.js"></script> -->
    <script src="<c:url value="/resources/js/commons.js" />"></script>
    <script src="<c:url value="/resources/js/member.js" />"></script>
    <!-- 다음 API -->
    <script>
    // 우편번호 찾기 찾기 화면을 넣을 element
    var element_wrap = document.getElementById('wrap');

    function foldDaumPostcode() {
        // iframe을 넣은 element를 안보이게 한다.
        element_wrap.style.display = 'none';
    }

    function execDaumPostcode() {
        let currentWidth = document.querySelector(".register").offsetWidth;
        console.dir(document.querySelector(".register"));

        console.log(currentWidth);
        // 현재 scroll 위치를 저장해놓는다.
        var currentScroll = Math.max(document.body.scrollTop, document.documentElement.scrollTop);
        new daum.Postcode({
            oncomplete: function(data) {
                // 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var addr = ''; // 주소 변수
                var extraAddr = ''; // 참고항목 변수

                //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    addr = data.roadAddress;
                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    addr = data.jibunAddress;
                }

                // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
                if(data.userSelectedType === 'R'){
                    // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                    // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                    if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                        extraAddr += data.bname;
                    }
                    // 건물명이 있고, 공동주택일 경우 추가한다.
                    if(data.buildingName !== '' && data.apartment === 'Y'){
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                    if(extraAddr !== ''){
                        extraAddr = ' (' + extraAddr + ')';
                    }
                    // 조합된 참고항목을 해당 필드에 넣는다.
                    document.getElementById("extraAddress").value = extraAddr;
                
                } else {
                    document.getElementById("extraAddress").value = '';
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('zonecode').value = data.zonecode;
                document.getElementById("address").value = addr;
                // 커서를 상세주소 필드로 이동한다.
                document.getElementById("detailAddress").focus();

                // iframe을 넣은 element를 안보이게 한다.
                // (autoClose:false 기능을 이용한다면, 아래 코드를 제거해야 화면에서 사라지지 않는다.)
                element_wrap.style.display = 'none';

                // 우편번호 찾기 화면이 보이기 이전으로 scroll 위치를 되돌린다.
                document.body.scrollTop = currentScroll;
            },
            // 우편번호 찾기 화면 크기가 조정되었을때 실행할 코드를 작성하는 부분. iframe을 넣은 element의 높이값을 조정한다.
            onresize : function(size) {
                if(400 < size.height)
                    element_wrap.style.height = 400 + 'px';
                if(currentWidth < size.width)
                    element_wrap.style.width = currentWidth  +'px';
                else
                    element_wrap.style.width = 500 +'px';
            },
            width : '100%',
            height : '100%'
        }).embed(element_wrap);

        // iframe을 넣은 element를 보이게 한다.
        element_wrap.style.display = 'block';
    }
    </script>
</body>
</html>