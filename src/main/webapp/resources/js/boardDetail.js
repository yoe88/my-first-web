'use strict'

window.addEventListener('load',()=> {
    loadComment();
})

//댓글창 보이기, 안 보이기
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

async function loadComment() { //댓글 리스트 불러오기
    const articleNo = document.querySelector('#articleNo').value; //글번호
    const totalCommentCount = document.querySelector('#total-comment-count'); //댓글 총개수
    let tempCount = 0;
    let list;
    try{
        const response = await fetch(`${getRoot()}/comment/${articleNo}`,{headers:{'Content-Type': 'application/json'}}) //요청
        const json = await  response.text();
        list = JSON.parse(json); //댓글 배열
    } catch (e) {
        alert('request error');
    }

    const ul = document.querySelector('#comment-list');
    ul.innerHTML = '';
    for(const comment of list){  //배열 하나씩 꺼내기
        const regDate_ = comment.regDate;
        const regDate = `${regDate_.year.toString().substring(2)}.${regDate_.monthValue}.${regDate_.dayOfMonth}. ${regDate_.hour}:${regDate_.minute}:${regDate_.second}`; //날짜+시간

        const li = `<li class="list-group-item p-2">
                        <div class="comment-info">
                            <img src="${getRoot()}/file/thumb/${comment.id}/${comment.profileImage == null ? "none" : comment.profileImage}/size?w=30&h=30" width="30" height="30"
                                 alt="프로필이미지" class="rounded-circle" width="30" height="30">
                            <span class="comment-writer text-muted">${comment.name}(${comment.id})</span>
                            <span class="mx-2" style="color: #e2d7df;">|</span>
                            <span class="comment-regdate text-muted">${regDate}</span>
        
                            <!-- 작성자 or 관리자 -->
                            <span class="float-right comment-option">
                                <div class="dropdown">
                                <!--Trigger-->
                                <a  type="button" data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false"><i class="fas fa-ellipsis-v"></i></a>
                                <!--Menu-->
                                <div class="dropdown-menu dropdown-menu-right">
                                    <a class="dropdown-item" href="#">삭제</a>
                                    <a class="dropdown-item" href="#">????</a>
                                </div>
                                </div>
                            </span>
                        </div>
                        <hr class="my-1">
                        <p class="comment-content my-1">${comment.content}</p>
                        <a class="small text-dark" href="javascript:;" data-cno="${comment.cno}" onclick="replyComment(this);">
                            <span class="reply-txt mr-1">답글</span><span class="reply-cnt font-weight-bold">${comment.count}</span>
                            <input type="radio" name="reply">
                        </a>
                    </li>`
        appendHtml(ul,li);
        tempCount++;
        tempCount += comment.count;
    }
    totalCommentCount.textContent = tempCount;
}

//답글 누르면 댓글이랑 작성박스 토글
function replyComment(a){
    const radio = a.children[2];
    if(radio.checked === true){
        radio.checked = false;
    }else{
        radio.checked = true;
    }

    const cno = a.attributes.getNamedItem('data-cno').value; //댓글번호
    toggleReplyComment(cno);
}


//댓글에 대한 답글 얻어오기
function getReplyComment(cno){ // 댓글번호
    return new Promise((resolve, reject) => {
        fetch(`${getRoot()}/comment/reply/${cno}`,{
            headers: {'Content-Type': 'application/json'}
        })
        .then(response => {
            if(response.status === 200) {
                resolve(response.text());
            }else{
                reject(new Error("Request is failed"));
            }
        });
    });
}


//답글을 누른곳에 대한 대댓글 리스트 추가
async function toggleReplyComment(cno){
    const radios = document.querySelectorAll('input[type=radio]');

    for (const element of radios) {
        const root = element.parentNode.parentNode; // //각댓글의 최상위 요소는  li
        if(element.checked === true){ //라디오 체크된곳만 진행하기
            //대댓글 목록 얻어오기
            const json = await getReplyComment(cno);
            const list = JSON.parse(json);
            //대댓글 리스트 작성
            const ul = document.createElement('ul');
            ul.classList.add('list-group', 'pl-3');
            ul.style.background = '#f3f0f0';
            const count = list.length; //대댓글 개수
            const currentCount = root.querySelector('.reply-cnt').textContent;
            root.querySelector('.reply-cnt').textContent = count;
            if(currentCount != count){
                const currentTotalCommentCount =  document.querySelector('#total-comment-count').textContent;
                document.querySelector('#total-comment-count').textContent = parseInt(currentTotalCommentCount) + (count-currentCount);
            }
            for(const c of list){
                if(c.pub === false){
                    const li = `<li class="list-group-item p-2"><p class="comment-content my-1">삭제된 댓글 입니다.</p></li>`;
                    appendHtml(ul,li);

                }else {
                    const regDate_ = c.regDate;
                    const regDate = `${regDate_.year.toString().substring(2)}.${regDate_.monthValue}.${regDate_.dayOfMonth}. ${regDate_.hour}:${regDate_.minute}:${regDate_.second}`; //날짜+시간
                    const li = `<li class="list-group-item p-2">
                                    <div class="comment-info">
                                        <img src="${getRoot()}/file/thumb/${c.id}/${c.profileImage == null ? "none" : c.profileImage}/size?w=25&h=25" width="25" height="25"
                                            alt="프로필이미지" class="rounded-circle" width="25" height="25">
                                        <span class="comment-writer text-muted">${c.name}(${c.id})</span>
                                        <span class="mx-2" style="color: #e2d7df;">|</span>
                                        <span class="comment-regdate text-muted">${regDate}</span>
                                        
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
                                    <p class="comment-content my-1">${c.content}</p>
                                 </li>`
                    appendHtml(ul, li);
                }
            }
            //만든 대댓글요소 추가
            root.appendChild(ul);
            //댓글입력창 추가
            const inputComment = document.createElement('div');
            inputComment.classList.add('container', 'mt-3', 'mb-2');
            //
            inputComment.innerHTML = `<div class="border rounded"> 
                        <textarea class="form-control no-resize" rows="1" placeholder="댓글입력" maxlength="200"
                                 onfocus="focusTextArea(this);" onkeyup="checkMaxLength(this);"></textarea>
                        <div class="p-2 clearfix hide">
                            <span class="currentLength">0</span><span class="maxLength text-muted">/200</span>
                            <button type="button" class="float-right btn btn-outline-danger btn-sm">댓글등록</button>
                        </div>
                    </div>`;
            root.appendChild(inputComment);
        }else{
            let len = root.children.length -1;
            while(root.children[len--].nodeName != 'A'){
                root.lastElementChild.remove();
            }
        }
    }
}

function focusTextArea(ta) {
    ta.rows = 3;
    ta.nextElementSibling.classList.remove('hide');
}
