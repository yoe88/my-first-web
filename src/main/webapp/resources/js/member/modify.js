'use strict'
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

function fileTrigger() {
    const fileButton = document.querySelector("#input-file");
    fileButton.click();
}
function changeName(){
    if(functionKey.includes(event.keyCode)) return;
    document.querySelector("#apply").classList.remove('disabled');
}

let singleImageFile = null;
function changeImageFile(fileButton) {
    const filePath = fileButton.value;
    if (filePath.length === 0) return;
    const file = fileButton.files[0];
    if(file.size > 10485760 ){
        alert('최대 허용 파일 사이즈는 10mb입니다.');
        return;
    }
    const acceptedImageTypes = ['image/gif', 'image/jpeg', 'image/png', 'image/bmp', 'image/tiff']; //허용가능한 파일 확장자
    if(!acceptedImageTypes.includes(file.type)) {
        alert('gif, jpeg, png, bmp, tiff 같은 이미지를 올려주세요.');
        return;
    }
    document.querySelector("#delete-button").classList.remove('disabled');  //삭제버튼
    document.querySelector("#isDelete").value = 'false';
    singleImageFile = file;
    const thumbImg = document.querySelector("#profile-image");
    makeThumbnail(file, thumbImg, 160, 160);

    document.querySelector("#delete-button").classList.add('btn-outline-danger');
    document.querySelector("#delete-button").classList.remove('disabled','btn-outline-secondary');
    document.querySelector("#apply").classList.remove('disabled');
}
function deleteProfileImage(button){
    if(button.classList.contains('disabled')) return;  //삭제버튼 비활성화일 경우 리턴
    singleImageFile = null;
    document.querySelector("#isDelete").value = 'true';
    const img = document.querySelector("#profile-image");
    img.src = getRoot() +'/file/thumb/profile/anonymous/none/size?w=160&h=160';
    document.querySelector("#input-file").value = '';

    button.classList.remove('btn-outline-danger');
    button.classList.add('disabled','btn-outline-secondary');
    document.querySelector("#apply").classList.remove('disabled');
}

//프로필사진 / 닉네임 수정
function modifyProfile(button){
    if(button.classList.contains('disabled')) return; //적용버튼 비활성화일 경우 리턴
    const formData = new FormData();
    const name = document.querySelector("#name");
    const nameErr = document.querySelector(".error-name");
    if(!validateName(name,nameErr)) return;

    let isDelete = document.querySelector("#isDelete").value;

    formData.append('name',name.value);
    formData.append("isDelete",isDelete);
    if(singleImageFile !== null){
        formData.append('image', singleImageFile);
    }

    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if(xhttp.readyState === 4){
            if(xhttp.status === 200){
                let response = this.response;
                if(response === "1")
                    location.href = getRoot() + '/member/me'
                else{
                    alert('fail..')
                }
            }
        }
    }
    xhttp.open("POST", getRoot()+ '/member/edit/profile',true);
    //xhttp.setRequestHeader("Content-type","multipart/form-data");
    xhttp.send(formData);
}

//비밀번호체크
function validatePassword(password){
    return !(password.value.includes(' ') || password.value.length < 4 || password.value.length > 20);
}

//현재 비밀번호, 새로운 비밀번호, 새로운 비밀번호 확인
function modifyPassword (passwordArray) {
    if(passwordArray.length === 0) return true;
    for(let i=0; i<2; i++){
        if(!validatePassword(passwordArray[i])){
            passwordArray[i].value = '';
            passwordArray[i].focus();
            alert('비밀번호는 공백을 제외한 4~20자입니다.');
            return false;
        }
    }
    if(passwordArray[0].value === passwordArray[1].value) {
        alert('현재 비밀번호와 새 비밀번호가 일치합니다.');
        passwordArray[1].value = '';
        passwordArray[1].focus();
        return false;
    }
    if(passwordArray[1].value !== passwordArray[2].value){
        alert('새 비밀번호 확인이 일치 하지 않습니다.');
        passwordArray[2].focus();
        return false;
    }
    let response = 0;
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = ()=>{
        if(xhttp.readyState === 4){
            if(xhttp.status === 200) {
                response = xhttp.responseText;
            }else{
                alert('password err');
            }
        }
    }
    xhttp.open("GET",getRoot() +"/member/checkpassword?password="+passwordArray[0].value.trim(),false);
    xhttp.send();
    if(response === '1'){
        return true;
    }
    else {
        alert('비밀번호를 정확하게 입력해 주세요.');
        return false;
    }
}

function deleteAddress(){
    const addressArray = document.querySelectorAll(".form-control");
    for(let address of addressArray)
        address.value = '';
    document.querySelector('#apply').classList.remove('disabled');
}

function changeAddress() {
    if(functionKey.includes(event.keyCode)) return;
    document.querySelector('#apply').classList.remove('disabled');
}

function modifyMember(form){
    if(form.querySelector('#apply').classList.contains('disabled')) return; //비활성화일 경우 리턴

    const passwordArray = form.querySelectorAll("input[type=password]");
    if(!modifyPassword(passwordArray)) return;

    form.submit();
}

async function dropMember() {
    const passwordArray = document.querySelectorAll('input[type=password]');

    if(passwordArray[0].value !== passwordArray[1].value) {
        showAlert('info', '비밀번호 확인이 일치하지 않습니다.', true);
        return;
    }
    const rText = await fetch('checkpassword?password='+passwordArray[0].value.trim()).then(r => r.text());
    if(rText === '0'){
        showAlert('danger', '비밀번호가 일치하지 않습니다.', true);
        return;
    }else{
        if(confirm('정말 탈퇴하시겠습니까?')){
            const rText = await fetch('drop',{
                method: 'PUT'
            }).then(r => r.text());
            if(rText === '1'){
                location.href = `${getRoot()}/index`;
            }
        }
    }


}