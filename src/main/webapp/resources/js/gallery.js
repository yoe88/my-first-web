'use strict'
//이미지 파일 유효성 검사 통과 하면 true  실패 false
function validateImageFile(inputFile) {
    const acceptedImageTypes = ['image/gif', 'image/jpeg', 'image/png', 'image/bmp', 'image/tiff']; //이미지 파일 종류
    const file = inputFile.files[0];
    if (file.size > 10485760) {  //10485760 10mb;
        //alert('파일 사이즈는 최대 10mb입니다.');
        showAlert('danger','파일 사이즈는 최대 10mb입니다.',false);
        return false;
    }
    //이미지가 아닌경우
    if(!acceptedImageTypes.includes(file.type)) {
        showAlert('secondary','gif, jpeg, png, bmp, tiff 같은 이미지를 올려주세요.', false);
        return false;
    }

    return true;
}

//파일 버튼 만들고 이벤트 발생
function uploadImg() {
    const fileList = document.querySelector('#thumb-list');
    if(fileList.childElementCount >= 5){
        alert('업로드 가능한 파일 개수는 최대 5개입니다.');
        return;
    }

    //시퀀스 받기
    let sequence = Number(document.querySelector('#sequence').value) + 1;
    const file = document.createElement('input');
    file.classList.add('hide');
    file.type = 'file';
    file.name = 'file';
    file.id = 'f' + sequence;
    file.accept = 'image/*';
    file.onchange = changeFile;
    file.click();
}

//만든 파일버튼에 파일을 선택한 경우
//히든으로 파일 올리고 썸네일도 추가 생성
async function changeFile(e) {
    const inputFile = e.target;
    //유효성 검사
    if(!validateImageFile(inputFile)) return;  
    
    const form = document.querySelector('form');
    const id = inputFile.id;
    const file = inputFile.files[0];
    const thumbList = document.querySelector('#thumb-list'); //썸네일 리스트
    //시퀀스 값 올리기
    document.querySelector('#sequence').value = Number(inputFile.id.substring(1));

    //폼에 파일 추가
    form.appendChild(inputFile);

    const dataURI = await returnThumbnail(file,  150, 150);
    const img = `
                <div class="img-box mr-2">
                    <i class="far fa-window-close text-danger pointer" data-id="${id}" onclick="removeFileAndThumbnail(this)"></i>
                    <img src="${dataURI}" alt="thumbnail" class="border rounded-circle" width="60" height="60">
                </div>
                `;
    appendHtml(thumbList,img);
}

//클라이언트에서 만든 파일과 썸네일 제거
function removeFileAndThumbnail(icon) {
    const id = icon.attributes.getNamedItem('data-id').value;
    const file = document.querySelector(`#${id}`);
    file.remove();                  //input file 제거
    icon.parentElement.remove();   //썸네일 박스 제거
}
//서버에서 만든 썸네일 제거 하고 delete True 표시 위한 input 생성 (파일번호, 파일이름)
function removeThumbnail(icon) {
    const no = icon.attributes.getNamedItem('data-no').value;   //파일 번호
    const src = icon.nextElementSibling.src;
    //src를 /로 나눠 인덱스 고정 번호로 파일 이름을 얻어야 하는데 왠지 배열 길이가 고정일거 같지 않음..  그래서 뒤에서 2번째를 꺼내려고 함
    const split = src.split('/');
    split.pop();                    //마지막 하나(size) 없애고
    const fileName = split.pop();   //파일 이름
    
    icon.parentElement.remove();   //썸네일 박스 제거

    //파일 번호 input
    const inputNo = document.createElement('input');
    inputNo.type = 'hidden';
    inputNo.name = 'deleteNo';
    inputNo.value = no;

    //파일 이름 input
    const inputFileName = document.createElement('input');
    inputFileName.type = 'hidden';
    inputFileName.name = 'deleteFileName';
    inputFileName.value = decodeURI(fileName);

    const form = document.querySelector('form');
    form.appendChild(inputNo);
    form.appendChild(inputFileName);
}

//폼 유효성 검사
function validateGallery(form) {
    const title = form.querySelector('input[name=title]').value;
    if(title.length === 0 || title.length >26) {
        showAlert('danger', '제목은 최대 26글자 입니다.', true);
        return false;
    }

    const fileList = document.querySelector('#thumb-list');
    if(fileList.childElementCount === 0){
        showAlert('danger','이미지를 최소 하나 올려주세요.',true);
        return false;
    }
    if(fileList.childElementCount > 5){
        alert('업로드 가능한 파일 개수는 최대 5개입니다.');
        return false;
    }

    return true;
}

//폼과 input 생성 하여 추가 하고 서브밋
function deleteGallery(gno) {
    if(!confirm('정말 삭제하시겠습니까?\n삭제하면 복구가 불가능합니다.')) return;

    const form = document.createElement('form');
    form.action = `${getRoot()}/galleries/${gno}`;
    form.method = 'POST';

    const input = document.createElement('input');
    input.type = 'hidden';
    input.name = '_method';
    input.value = 'delete';
    form.appendChild(input);

    document.body.appendChild(form);
    form.submit();
}

async function disableGallery(gno) {
    if(!confirm('비공개 하시겠습니까?')) return;

    const response = await fetch(`${gno}/edit/pub`,{
        method : 'PUT',
        //post는 되는데 put으로 보내면 서버에서 받지를 못한다;; json으로 하자..
        //headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        headers: { 'Content-Type': 'application/json' },
        body : JSON.stringify({pub:0})
    });
    if(response.status === 200){
        const text = await response.text();
        if(text === 'true')
            location.href = `${getRoot()}/galleries`;
        else
            alert('다시 시도 해주세요.');
    }else{
        alert('disable, Error');
    }
}

// 관리자 갤러리 리스트 체크박스 토글
function toggleGalleryChecked(thisSpan) {
    thisSpan.classList.toggle('checked');
}

//관리자 갤러리 리스트 공개 일괄 변경
async function updateGalleriesPub() {
    const allNo = document.querySelector('input[name=allNo]').value.trim();
    console.log(`'${allNo}'`);

    const openNoArray = [];
    const checked = document.querySelectorAll('span.checked');
    checked.forEach((i)=>{
        openNoArray.push(i.attributes.getNamedItem('data-gno').value);
    })

    const openNo = openNoArray.join(" ");
    console.log(`'${openNo}'`);

    const response = await fetch('',{
        method: 'PUT',
        headers: {'Content-Type' : 'application/json'},
        body: JSON.stringify({"allNo" : allNo, "openNo" : openNo})
    })
    if(response.status === 200){
        const text = await response.text();
        if(text === 'true'){
            showAlert('success','변경 되었습니다.',true);
        }else{
            showAlert('danger','다시 시도해주세요.',true);
        }
    }else{
        alert('Error')
    }
}

function checkedGalleriesPub() {
    const checkBox = document.querySelectorAll('.check-box');

    checkBox.forEach(i =>{
        i.classList.add('checked');
    })
}

function unCheckedGalleriesPub() {
    const checkBox = document.querySelectorAll('.check-box');

    checkBox.forEach(i =>{
        i.classList.remove('checked');
    })
}