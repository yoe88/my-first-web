'use strict'
//엔터키 막기
notAllowEnter();

/*SSL 을 사용하지 않는 경우 GET 및 POST 는 동일합니다. SSL이 존재할 때 POST는 GET에 비해 더 안전하지만.
GET은 암호화되지 않은 데이터를 전송하지만 SSL을 사용하면 전송 될 HTTP 데이터가 암호화되므로 보안이 유지됩니다.*/
//아이디체크
function checkId(){
    const id = document.querySelector("input[name=id]");
    const error = document.querySelector(".error-id");

    //아이디 유효성체크
    const regId = /^[a-z0-9]{4,12}$/;
    if(!regId.test(id.value)){
        error.textContent = '아이디는 공백제외 4~12자의 영문 소문자, 숫자만 사용 가능합니다.';
        error.style.color = 'red';
        id.focus();
        return;
    }

    //아이디중복체크
    fetch('checkid?id='+id.value.trim(),{
        headers:{'Content-Type': 'text/plain'}
    })
    .then(response => {
        if(response.status === 200){
            error.textContent = '사용 가능한 아이디입니다.';
            error.style.color = 'blue';
            document.querySelector("#idCheck").value = 'Y';
        } else if(response.status === 409){
            error.textContent = '중복된 아이디입니다.';
            error.style.color = 'red';
            document.querySelector("#idCheck").value = 'N';
        } else{
            alert('id, server error');
        }
    });
}
//중복확인을 거쳤지만 다시 아이디가 변경된경우
function resetId(){
    if(document.querySelector("#idCheck").value === 'Y'){
        if(functionKey.includes(event.keyCode)) return;
        alert('아이디가 변경되었습니다.\n중복확인을 다시 해주세요.');
        document.querySelector("#idCheck").value = 'N';
        document.querySelector(".error-id").textContent = '';
    }
}

//비밀번호체크
function validatePassword(password){
    return !(password.value.includes(' ') || password.value.length < 4 || password.value.length > 20);
}

//닉네임 체크
function validateName(name, errText){  //닉네임 input, 에러 input
    const regName = /^[0-9a-zA-Z가-힣]{1,8}$/; // 특수문자제외
    if(name.value.length === 0){
        errText.textContent = '닉네임을 입력하세요.';
        name.focus();
        return false;
    }
    if(!regName.test(name.value)){
        errText.textContent = '닉네임은 공백 특수문자를 제외한 1~8자입니다.';
        name.focus();
        return false;
    }else{
        errText.textContent = '';
    }
    return true;
}

//이메일체크
function validateEmail(){
    const email = document.querySelector("input[name=email]");
    const error = document.querySelector(".error-email");

    //이메일 유효성체크
    const regEmail = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
    if(!regEmail.test(email.value)){
        error.textContent = '이메일형식이 올바르지 않습니다.';
        error.style.color = 'red';
        email.focus();
        return false;
    }

    //이메일중복체크
    fetch('checkemail?email=' + email.value.trim(),{
        headers:{'Content-Type': 'text/plain'}
    }).then(response =>{
        if(response.status === 200){
            error.textContent = '사용 가능한 이메일입니다.';
            error.style.color = 'blue';
            document.querySelector("#emailCheck").value = 'Y';
            document.querySelector("#gCode").classList.remove("hide");
        }else if(response.status === 409){
            error.textContent = '중복된 이메일입니다.';
            error.style.color = 'red';
            document.querySelector("#emailCheck").value = 'N';
            document.querySelector("#gCode").classList.add("hide");
        }else {
            alert('email, server error');
        }
    })
}
//중복확인을 거쳤지만 다시 이메일이 변경된경우 초기화
function resetEmail(err){
    if(document.querySelector("#emailCheck").value === 'Y'){
        if(functionKey.includes(event.keyCode)) return;
        if(err!==2)
            alert('이메일이 변경되었습니다.\n 중복확인을 다시 해주세요.');
        document.querySelector("#emailCheck").value = 'N';
        document.querySelector(".error-email").textContent = '';
        document.querySelector("#codeCheck").value = 'N';
        document.querySelector(".error-code").textContent = '';
        document.querySelector("#gCode").classList.add("hide");
        document.querySelector("#isNewCode").value = 'N';
        document.querySelector("#gCode").value = '인증번호 받기';
    }
}

//인증번호 발급  인증번호 받기 버튼
function getCode(){
    if(document.querySelector("#emailCheck").value === 'N'){
        alert('이메일을 확인하세요.');
        document.querySelector("input[name=email]").focus();
        return;
    }
    if(document.querySelector("#codeCheck").value === 'Y'){
        document.querySelector("#codeCheck").value = 'N';
    }
    const email = document.querySelector("input[name=email]");
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function(){
        if(xhttp.readyState === XMLHttpRequest.OPENED){ //1
            showLoading();  //로딩창 켜기
        }
        if(xhttp.readyState === 4){
            if(xhttp.status === 200){
                const response = this.responseText;
                if(response === '1'){
                    closeLoading(); //로딩창 끄기
                    document.querySelector(".error-email").textContent = "인증번호를 전송했습니다.";
                    document.querySelector(".error-email").style.color = 'green';
                    document.querySelector("#isNewCode").value = 'Y';
                    document.querySelector("#gCode").value = '인증번호 재전송';
                    showAlert('success','인증번호를 전송하였습니다.',true);
                }else{
                    alert('전송실패');
                }
            }else{
                closeLoading();
                alert('code, error');
            }
        }
    }
    xhttp.open("GET","createcode?email="+email.value.trim(),true);
    xhttp.setRequestHeader('Content-Type', 'text/plain');
    xhttp.send();
}

//인증번호 전송  인증번호 확인버튼
function sendCode(){
    if(document.querySelector("#isNewCode").value === 'N'){
        alert('인증번호를 받아주세요.');
        return;
    }
    if(document.querySelector("#codeCheck").value === 'Y'){
        showAlert('success','이미 인증이 되었습니다.',true);
        return;
    }

    const code = document.querySelector("#code");  //입력한 인증번호
    if(code.value.trim().length ===0) return;
    const regCode = /^[0-9a-zA-Z가-힣]{1,8}$/; // 특수문자제외
    if(!regCode.test(code.value)) {
        alert('인증번호 형식이 옳지 않습니다.');
        return;
    }
    const errorCode = document.querySelector(".error-code");

    fetch('checkcode?code='+ code.value.trim(),{
        //headers:{'Content-Type': 'text/plain'}
    })
    .then(response => response.text())
    .then(text => {
        if(text === '1'){
            errorCode.textContent = "인증을 확인했습니다.";
            errorCode.style.color = 'blue';
            document.querySelector("#codeCheck").value = 'Y'
        }else if(text === '0'){
            errorCode.textContent = "인증번호가 일치하지 않습니다.";
            errorCode.style.color = 'red';
            document.querySelector("#codeCheck").value = 'N'
        }else{
            alert('인증번호 에러\n이메일 인증을 다시해주세요');
            resetEmail(2);
        }
    });
}


function addMember(form){
    if(functionKey.includes(event.keyCode)) return false;
    //아이디 체크
    if(form.querySelector("#idCheck").value === 'N'){
        alert('아이디를 확인하세요.');
        form.querySelector("input[name=id]").focus();
        return false;
    }
    //비밀번호 체크
    const password = form.querySelectorAll("input[type=password]");
    if(!validatePassword(password[0])){
        form.querySelector(".error-pw").textContent = '비밀번호는 공백을 제외한 4~20자입니다.';
        password[0].focus();
        return false;
    }
    if(password[0].value !== password[1].value){
        form.querySelector(".error-pw").textContent = '비밀번호가 일치하지 않습니다.';
        password[1].focus();
        return false;
    }else{
        form.querySelector(".error-pw").textContent = '';
    }
    //닉네임 체크
    const name = form.querySelector("input[name=name]");
    const nameErr = form.querySelector(".error-name");
    if(!validateName(name,nameErr)) return false;

    //이메일 체크
    if(form.querySelector("#emailCheck").value === 'N'){
        alert('이메일을 확인하세요.');
        form.querySelector("input[name=email]").focus();
        return false;
    }
    if(form.querySelector("#isNewCode").value === 'N'){
        alert('인증번호를 받아주세요.');
        return false;
    }
    if(form.querySelector("#codeCheck").value === 'N'){
        alert('인증을 확인해주세요.');
        form.querySelector("#code").focus();
        return false;
    }
    return true;
}