'use strict'
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
    if(form.password.value.length === 0){
        document.querySelector('.error-pw').textContent = '비밀번호를 입력해주세요.';
        return false;
    }
    return true;
}

async function findID(){
    const result = document.querySelector('#search-ID-Result');
    const email = document.querySelector('#input-email').value.trim();

    if(email.length === 0){
        result.style.color = '#FF0000';
        result.textContent = '이메일을 입력해주세요.';
        return;
    }

    const response = await fetch(`findId?email=${email}`,{
        headers : {'Content-Type' : 'text/plain'}
    });
    const responseText = await response.text();
    if(responseText.length === 0){ //검색된 아이디가 없는경우
        result.style.color = 'rgb(255, 98, 98)';
        result.textContent = '해당하는 회원이 존재하지 않습니다.';
    }else{ //해당하는 아이디가 존재하는 경우
        result.style.color = 'rgb(106, 104, 228)';
        result.innerHTML = `해당 이메일과 일치하는 아이디는 <span style="font-size: 1.4rem;">${responseText}</span> 입니다.`;
    }
}

async function findMember() {
    const result = document.querySelector('#search-Member-Result');
    const info = document.querySelectorAll('#searchMember input');
    const id = info[0].value.trim();
    const email = info[1].value.trim();

    if(id.length === 0 || email.length === 0){
        result.style.color = '#FF0000';
        result.textContent = '아이디와 이메일을 입력해주세요.';
        return;
    }
    const response = await fetch(`findMember?id=${id}&email=${email}`,{
        headers : {'Content-Type' : 'text/plain'}
    });
    const responseText = await response.text();
    if(responseText === '0'){ //검색된 정보가 없는경우
        result.style.color = 'rgb(255, 98, 98)';
        result.textContent = '해당하는 회원이 존재하지 않습니다.';
    }else{ //해당하는 회원이 존재하는 경우
        result.style.color = 'rgb(106, 104, 228)';
        result.innerHTML = `버튼을 누르면 이메일로 임시 비밀번호가 발급됩니다. <button class="btn btn-outline-danger" onclick="getNewPassword();">발급하기</button>`;
    }
}

$('#searchID').on('hidden.bs.modal', function () {
    const result = document.querySelector('#search-ID-Result');
    const email = document.querySelector('#input-email');
    result.textContent = '';
    email.value = '';
})

$('#searchMember').on('hidden.bs.modal', function () {
    const result = document.querySelector('#search-Member-Result');
    const info = document.querySelectorAll('#searchMember input');
    result.textContent = '';
    info[0].value = '';
    info[1].value = '';
})

async function getNewPassword() {
    const info = document.querySelectorAll('#searchMember input');
    const id = info[0].value.trim();
    const email = info[1].value.trim();

    showLoading();
    const response =  await fetch(`newPassword?id=${id}&email=${email}`,{
        headers: {'Content-Type' : 'text/plain'}
    });
    closeLoading();
    $('#searchMember').modal('hide');
    const responseText = await response.text();
    if(responseText === 'OK'){
        showAlert('success','메일을 발송했습니다.', false);
    }else{
        alert('다시 시도해주세요.');
    }
}

