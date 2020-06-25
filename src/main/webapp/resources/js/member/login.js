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