'use strict'
//게시판 작성페이지
function validateBoard(form){
    const title = form.title;
    const content = form.content;
    if(title.value.length === 0 ){
        alert('제목을 입력하세요.');
        return false;
    }
    if(title.value.length > 26 ){
        alert('제목의 글자수는 최대 26자입니다.');
        return false;
    }
    if(content.value.length === 0 ){
        alert('내용을 입력하세요.');
        return false;
    }
    /* if(stringByteLength > 300){
        alert('최대 입력값을 초과하였습니다.');
        return false;
    } */
    return true;
}
function checkMaxByte(text){ //textArea
    let len = checkByte(text.value);
    document.querySelector(".currentByte").textContent = len.b;
    if(len.b > 300){
        text.value= text.value.substring(0,len.i -1);
        len = checkByte(text.value);
        document.querySelector(".currentByte").textContent = len.b
        alert('최대 입력값을 초과하였습니다.');
    }
}
function checkByte(s,b,i,c){
    for(b=i=0;c=s.charCodeAt(i++);){
        b+=c>>11?3:c>>7?2:1; //2048로나눴을때 몫이 있으면 3바이트 다시 128이랑비교해서 몫이 있으면 2바이트 없으면1바이트
        if(b > 300){
            return{
                b:b,  //바이트
                i:i   //글자수
            };
        }
    }
    return{
        b:b
    };
};
function changeFile(thisFile) { //실제 입력된 파일
    const acceptedImageTypes = ['image/gif', 'image/jpeg', 'image/png', 'image/bmp', 'image/tiff']; //허용가능한 파일 확장자
    const thumbImg = document.querySelector('.thumb-img'); //썸네일
    const file = thisFile.files[0];//fileList[0]; 첫번째파일객체
    const fileName = document.querySelector(".custom-file-label"); //화면상에 보이는 파일이름

    if (file.value.length === 0) {  // name?
        fileName.textContent = 'Choose file';
        thumbImg.src = '';
        thumbImg.style.display = 'none';
        return;
    }
    if (file.size > 10485760) {  //10485760 10mb;
        alert('파일 사이즈는 최대 10mb입니다.');
        thisFile.value = '';  //파일초기화
        fileName.textContent = 'Choose file';
        thumbImg.src = '';
        thumbImg.style.display = 'none';
        return;
    }
    fileName.textContent = file.name; //파일이름 변경

    //첨부파일이 이미지일경우
    if (acceptedImageTypes.includes(file.type)) {
        makeThumbnail(file, thumbImg, 50, 50);
    } else {
        thumbImg.src = '';
        thumbImg.style.display = 'none';
    }
}

function cancle() {
    if (confirm("변경사항이 저장되지 않을 수 있습니다."))
        history.go(-1);
}
//게시판 작성페이지


//상세보기페이지
//댓글창 보이기.안보이기
function toggleComment(){
    const commentView = document.querySelector('#comment-view');
    commentView.classList.toggle('hide');
}
//글자수 세기
function checkMaxLength(text){
    const currentLen = text.nextElementSibling.children[0];
    const len = text.value.length;
    currentLen.textContent = len;        
}

//답글 누르면 댓글이랑 작성박스 토글
function replyComment(a){
    const radio = a.children[2];
    if(radio.checked == true){
        radio.checked = false;
    }else{
        radio.checked = true;
    }
    toggleReplyComment();
}

//댓글에 대한 답글 얻어오기
function getReplyComment(){  
    let list;
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function(){
        if(this.readyState == 4){
            if(this.status == 200){
                list = JSON.parse(this.response); //댓글목록
            }else{
                alert('replyComment, error');
            }
        }
    }
    xhttp.open('GET',getRoot() + '/boards/1/reply/1',false);
    xhttp.send();
    return list;
}


//답글을 누른곳에 대한 대댓글 추가
function toggleReplyComment(){
    const radios = document.querySelectorAll('input[type=radio]');
    const div = document.createElement('div');
    div.classList.add('container', 'mt-3', 'mb-2', 'comment-write-box');
    div.innerHTML = `<div class="border rounded">
                        <textarea class="form-control no-resize" rows="4" placeholder="댓글입력" maxlength="200" onkeyup="checkMaxLength(this);"></textarea>
                        <div class="p-2 clearfix">
                            <span class="currentLength">0</span><span class="maxLength text-muted">/200</span>
                            <button type="button" class="float-right btn btn-outline-danger btn-sm">댓글등록</button>
                        </div>
                    </div>`;
    radios.forEach(element => {
        const root = element.parentNode.parentNode;
        if(element.checked == true){ 
            //console.log('트루');
           //각댓글의 최상위 요소는  li
            //대댓글 목록얻어오기
            const list = getReplyComment();
            console.dir(list);
             //대댓글 리스트 목록작성
            const ul = document.createElement('ul');
            ul.classList.add('list-group', 'px-3');
            const count = list.length; //대댓글 개수
            const currentCount = root.querySelector('.reply-cnt').textContent;
            root.querySelector('.reply-cnt').textContent = count;
            if(currentCount != count){
                const currentTotalCommentCount =  document.querySelector('#total-comment-count').textContent;
                document.querySelector('#total-comment-count').textContent = parseInt(currentTotalCommentCount) + (count-currentCount); 
            }
            list.forEach(function(cmt) {
                const writer = cmt.writer;
                const content = cmt.content;
                const regdate_ = cmt.regdate;               
                const regdate = `${regdate_.year}.${regdate_.monthValue}.${regdate_.dayOfMonth}. ${regdate_.hour}:${regdate_.minute}:${regdate_.second}`; //날짜+시간
                const li = document.createElement('li');
                li.classList.add('list-group-item', 'p-2');
                li.innerHTML = `<div>
                                    <span class="comment-writer text-muted">${writer}</span>
                                    <span class="mx-2" style="color: #e2d7df;">|</span>
                                    <span class="comment-regdate text-muted">${regdate}</span>
                                    
                                    <!-- 작성자 or 관리자 -->
                                    <span class="float-right comment-option">
                                        <div class="dropdown">
                                        <!--Trigger--> 
                                        <a  type="button" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false"><i class="fas fa-ellipsis-v text-muted"></i></a>         
                                        <!--Menu-->
                                        <div class="dropdown-menu dropdown-menu-right">
                                            <a class="dropdown-item" href="#">삭제</a>
                                            <a class="dropdown-item" href="#">????</a>
                                        </div>
                                        </div>
                                    </span>            
                                </div>
                                <hr class="my-1">
                                <p class="comment-content my-1">${content}</p>`;
                ul.appendChild(li);
            });
            //만든 대댓글요소 추가
            root.appendChild(ul);
            //댓글입력창 추가
            root.appendChild(div);
        }else{
            let len = root.children.length -1;
            while(root.children[len--].nodeName != 'A'){
                root.lastElementChild.remove();
            }
        }
    });
}     

//상세보기페이지