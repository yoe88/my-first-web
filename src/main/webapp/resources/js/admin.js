'use strict'

async function changeRole(id) { //권한 변경할 회원 아이디
    const role = document.querySelector('input[name = role]:checked').value;

    const response = await fetch(`${id}`,{
        method: 'PUT',
        headers: {'Content-Type' : 'application/json'},
        body: JSON.stringify({'role' : role, 'type' : 'role'})
    });
    if(response.status === 200){
        const text = await response.text();
        if(text === '1') showAlert('success', '변경되었습니다.', true);
        else showAlert('danger', '다시 시도해주세요.', true);
    }else{
        alert('edit, Error');
    }
}

async function changeEnable(id) { //활동 변경할 회원 아이디
    const enable = document.querySelector('input[name = enable]:checked').value;

    const response = await fetch(`${id}`,{
        method: 'PUT',
        headers: {'Content-Type' : 'application/json'},
        body: JSON.stringify({'enable' : enable, 'type' : 'enable'})
    });
    if(response.status === 200){
        const text = await response.text();
        if(text === '1') showAlert('success', '변경되었습니다.', true);
        else showAlert('danger', '다시 시도해주세요.', true);
    }else{
        alert('edit, Error');
    }
}

async function toggleBoardPub(articleNo, button){ //글번호, 비/공개 버튼
    const value = button.value;
    const pub = value === '공개하기' ? 1 : 0;

    if(confirm((pub === 1 ? '공개' : '비공개') + ' 하시겠습니까?')){
        const response = await fetch(`${getRoot()}/boards/${articleNo}/edit/pub`, {
            method: 'PUT',
            headers: {'Content-Type' : 'application/json'},
            body: JSON.stringify({'pub': pub})
        });
        if(response.status === 200){
            const text = await response.text();
            if(text === 'true'){
                button.value = (pub === 1 ? '비공개하기' : '공개하기');
                showAlert('success', '변경되었습니다.', true);
            }
            else
                alert('다시 시도 해주세요.');
        }else{
            alert('disable, Error');
        }
    }
}

async function toggleGalleryPub(gno, button){ //글번호, 비/공개 버튼
    const value = button.value;
    const pub = value === '공개하기' ? 1 : 0;

    if(confirm((pub === 1 ? '공개' : '비공개') + ' 하시겠습니까?')){
        const response = await fetch(`${getRoot()}/galleries/${gno}/edit/pub`, {
            method: 'PUT',
            headers: {'Content-Type' : 'application/json'},
            body: JSON.stringify({'pub': pub})
        });
        if(response.status === 200){
            const text = await response.text();
            if(text === 'true'){
                button.value = (pub === 1 ? '비공개하기' : '공개하기');
                showAlert('success', '변경되었습니다.', true);
            }
            else
                alert('다시 시도 해주세요.');
        }else{
            alert('disable, Error');
        }
    }
}