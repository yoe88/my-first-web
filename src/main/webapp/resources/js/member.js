/*SSL 을 사용하지 않는 경우 GET 및 POST 는 동일합니다. SSL이 존재할 때 POST는 GET에 비해 더 안전하지만.
GET은 암호화되지 않은 데이터를 전송하지만 SSL을 사용하면 전송 될 HTTP 데이터가 암호화되므로 보안이 유지됩니다.*/
//중복확인을 거쳤지만 다시 아이디가 변경된경우
function resetId(){
    if(document.querySelector("#idCheck").value === 'Y'){
        alert('아이디가 변경되었습니다.\n중복확인을 다시 해주세요.');
        document.querySelector("#idCheck").value = 'N';
        document.querySelector(".error-id").textContent = '';
    }
}

//아이디체크
function checkId(){
    const id = document.querySelector("input[name=id]");
    const error = document.querySelector(".error-id");

    //아이디 유효성체크
    let regId = /^[a-z0-9]{4,12}$/;
    if(!regId.test(id.value)){
        error.textContent = '아이디는 공백제외 4~12자의 영문 소문자, 숫자만 사용 가능합니다.';
        error.style.color = 'red';
        id.focus();
        return;
    }

    //테스트///////////////////////////////////////////////////////
    //document.querySelector("#idCheck").value = 'Y';
    ///////////////////////////////////////////////////////////

    //아이디중복체크
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function(){
        if(this.readyState === 4){
            if(this.status === 200){
                const response = this.responseText;
                if(response === 'true'){
                    error.textContent = '중복된 아이디입니다.';
                    error.style.color = 'red';
                    document.querySelector("#idCheck").value = 'N';
                }else{
                    error.textContent = '사용 가능한 아이디입니다.';
                    error.style.color = 'blue';
                    document.querySelector("#idCheck").value = 'Y';
                }
            }else{
                alert('id, error');
            }
        }
    }
    xhttp.open("GET",getRoot() + "/member/checkid?id="+id.value.trim(),true);
    xhttp.send();
}



//중복확인을 거쳤지만 다시 이메일이 변경된경우 초기화
function resetEmail(err){
    if(document.querySelector("#emailCheck").value === 'Y'){
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


//이메일체크
function checkEmail(){
    const email = document.querySelector("input[name=email]");
    const error = document.querySelector(".error-email");

    //이메일 유효성체크
    let regEmail = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
    if(!regEmail.test(email.value)){
        error.textContent = '이메일형식이 올바르지 않습니다.';
        error.style.color = 'red';
        email.focus();
        return;
    }
        
    //이메일중복체크
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function(){
        if(xhttp.readyState === 4){
            if(xhttp.status === 200){
                const response = this.responseText;
                if(response === 'true'){
                    error.textContent = '중복된 이메일입니다.';
                    error.style.color = 'red';
                    document.querySelector("#emailCheck").value = 'N';
                    document.querySelector("#gCode").classList.add("hide");
                }else{
                    error.textContent = '사용 가능한 이메일입니다.';
                    error.style.color = 'blue';
                    document.querySelector("#emailCheck").value = 'Y';
                    document.querySelector("#gCode").classList.remove("hide");
                }
            }else{
                alert('email, error');
            }
        }
    }
    xhttp.open("GET",getRoot() +"/member/checkemail?email="+email.value.trim(),true);
    xhttp.send();
}

//인증코드 발급
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
                    document.querySelector(".error-email").textContent = "인증코드를 전송했습니다.";
                    document.querySelector(".error-email").style.color = 'green';
                    document.querySelector("#isNewCode").value = 'Y';
                    document.querySelector("#gCode").value = '인증번호 재전송';
                    showAlert('success','인증번호를 전송하였습니다.',true);
                }else{
                    alert('전송실패');
                }
            }else{
                 alert('code, error');
            }
        }
    }     
    xhttp.open("GET",getRoot() +"/createcode?email="+email.value.trim(),true);
    xhttp.send();
}


//인증번호 전송
function sendCode(){
    if(document.querySelector("#emailCheck").value === 'N'){
        alert('이메일을 확인하세요.');
        document.querySelector("input[name=email]").focus();
        return;
    }
    if(document.querySelector("#isNewCode").value === 'N'){
        alert('인증번호를 받아주세요.');
        return;
    }
    if(document.querySelector("#codeCheck").value === 'Y'){
        showAlert('success','인증이 되었습니다.',true);
        return;
    }

    const code = document.querySelector("#code");  //입력한 인증번호
    if(code.value.trim().length ===0) return;
    const errorCode = document.querySelector(".error-code");
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function(){
        if(xhttp.readyState === XMLHttpRequest.DONE){
            if(xhttp.status === 200){
                const response = this.responseText;
                if(response === '1'){
                    errorCode.textContent = "인증을 확인했습니다.";
                    errorCode.style.color = 'blue';
                    document.querySelector("#codeCheck").value = 'Y'
                }else if(response === '0'){
                    errorCode.textContent = "인증코드가 일치하지 않습니다.";
                    errorCode.style.color = 'red';
                    document.querySelector("#codeCheck").value = 'N'
                }else if(response === '2'){
                    alert('인증코드 에러\n이메일 인증을 다시해주세요');
                    resetEmail(2);
                }
            }else{
                alert('send, error');
            }
        }
    }     
    xhttp.open("GET",getRoot() +"/member/checkcode?code="+code.value.trim(),true);
    xhttp.send();
}


function addMember(form){
    //아이디 체크
    if(form.querySelector("#idCheck").value === 'N'){
        alert('아이디를 확인하세요.');
        form.querySelector("input[name=id]").focus();
        return false;
    }
    //비밀번호 체크
    const passwd = form.querySelectorAll("div[class=PASSWD] input");
    if(passwd[0].value.includes(' ') || passwd[0].value.length <4 || passwd[0].value.length >21){
        form.querySelector(".error-passwd").textContent = '비밀번호는 공백을 제외한 4~20자입니다.';
        passwd[0].focus();
        return false;
    }
    if(passwd[0].value !== passwd[1].value){
        alert('비밀번호가 일치하지 않습니다.');
        form.querySelector(".error-passwd").textContent = '비밀번호가 일치하지 않습니다.';
        passwd[1].focus();
        return false;
    }else{
        form.querySelector(".error-passwd").textContent = '';
    }
    //닉네임 체크
    const name = form.querySelector("input[name=name]");
    const regName = /^[0-9a-zA-Z가-힣]{1,8}$/; // 특수문자제외
    if(name.value.length === 0){
        form.querySelector(".error-name").textContent = '닉네임을 입력하세요.';
        name.focus();
        return false;
    }
    if(!regName.test(name.value)){
        form.querySelector(".error-name").textContent = '닉네임은 공백 특수문자를 제외한 1~8자입니다.';
        return false;
    }else{
        form.querySelector(".error-name").textContent = '';
    }
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

function errTextInit(){
    let err = document.querySelectorAll("span[class *=error-]");
    err.forEach(function(element){
        element.textContent = '';
    });
}
function validateLogin(form){
    //4~12  //4~20
    if(form.id.value.length === 0){
        document.querySelector('.error-id').textContent = '아이디를 입력해주세요.';
        return false;
    }
    if(form.passwd.value.length === 0){
        document.querySelector('.error-pw').textContent = '비밀번호를 입력해주세요.';
        return false;
    }
    return true;
}