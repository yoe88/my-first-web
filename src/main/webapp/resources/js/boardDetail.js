'use strict'

window.addEventListener('load',async()=> {
    await loadComment(1);
})
//글자수 세기
function checkMaxLength(text){
    const currentLen = text.nextElementSibling.children[0];
    currentLen.textContent = text.value.length;
}

//댓글창 토글
function toggleViewComment(){
    const commentView = document.querySelector('#comment-view');
    commentView.classList.toggle('hide');
}

//댓글 리스트 불러오기
async function loadComment(page) {//페이지 번호
    const articleNo = document.querySelector('#articleNo').value; //글번호
    const totalCommentCount = document.querySelector('#total-comment-count'); //화면에 표시 할 댓글 총개수
    let map;  //서버에서 응답한 map
    try{
        const response = await fetch(`${getRoot()}/comment/${articleNo}?p=${page}`,{headers:{'Content-Type': 'application/json'}}) //요청
        map = await response.json();
    } catch (e) {
        alert('request error');
    }
    if(document.querySelector('#more-comment') !== null) //더보기 버튼이 존재하면 삭제하기
        document.querySelector('#more-comment').remove();

    const list = map.list;   //댓글 리스트
    const count = map.count; //답글 제외한 댓글 개수
    totalCommentCount.textContent = map.totalCount; //댓글 총 개수

    const ul = document.querySelector('#comment-list');
    const pno = document.createAttribute("data-pno");
    pno.value = page;
    ul.attributes.setNamedItem(pno);  //ul에 몇 페이지까지 펼쳐져 있는지 작성..

    const lastLI = ul.lastElementChild;  //ul에 마지막 li를 가져오기
    const lastSeq = (lastLI === null ? 0 : lastLI.attributes.getNamedItem('data-seq').value);
    if(lastSeq === count) return; //마지막 li번호가 count랑 같으면 끝내기

    const seq = 1 + (page-1)*10; //시작번호 설정

    const userName = document.querySelector('#user-name').value;  //로그인한 유저 아이디 string
    const isAdmin = document.querySelector('#admin').value;       //admin 권한을 가졌는지 true/false string형

    list.forEach((comment, i) => {
        if((seq+i) <= lastSeq  ) return;

        const regDate_ = comment.regDate;
        const regDate = `${regDate_.year.toString().substring(2)
                         }.${regDate_.monthValue < 10 ? `0${regDate_.monthValue}` : regDate_.monthValue
                         }.${regDate_.dayOfMonth < 10 ? `0${regDate_.dayOfMonth}` : regDate_.dayOfMonth
                         }. ${regDate_.hour < 10 ? `0${regDate_.hour}` : regDate_.hour
                         }:${regDate_.minute < 10 ? `0${regDate_.minute}` : regDate_.minute
                         }:${regDate_.second < 10 ? `0${regDate_.second}` : regDate_.second}`; //날짜+시간

        const li = `<li class="list-group-item p-2" data-cno="${comment.cno}" data-seq="${seq + i}">
                       <div class="comment-info">
                            <img src="${getRoot()}/file/thumb/profile/${comment.id}/${comment.profileImage == null ? "none" : comment.profileImage}/size?w=30&h=30" width="30" height="30"
                                 alt="프로필이미지" class="rounded-circle" width="30" height="30">
                            <span class="comment-writer text-muted">${comment.name}(${comment.id})</span>
                            <span class="mx-2" style="color: #e2d7df;">|</span>
                            <span class="comment-regdate text-muted">${regDate}</span>
        
                            ${(userName === comment.id || isAdmin === 'true') && comment.pub === true ?
                                `<!-- 작성자 or 관리자 -->
                                <span class="float-right comment-option">
                                <div class="dropdown">
                                <!--Trigger-->
                                <a  type="button" data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false"><i class="fas fa-ellipsis-v"></i></a>
                                <!--Menu-->
                                <div class="dropdown-menu dropdown-menu-right">
                                <a class="dropdown-item" onclick="deleteComment(this);" >삭제</a>
                                <a class="dropdown-item" href="#">????</a>
                                </div>
                                </div>
                                </span>` : ''
                            }
                        </div>
                        <hr class="my-1">
                        <p class="comment-content my-1" style="white-space: pre-wrap;">${comment.pub === true ? comment.content : '<i class="fas fa-exclamation-circle mr-1" style="color: #fbbd0d"></i><span style="color: #d0d0d0">삭제된 댓글 입니다.</span>'}</p>
                        <a class="small text-dark" onclick="showReplyComment(this);" style="cursor: pointer">
                            <span class="reply-txt mr-1">답글</span><span class="reply-cnt font-weight-bold">${comment.count}</span>
                            <input type="radio" name="reply">
                        </a>
                    </li>`
        appendHtml(ul,li);
    })
    if(count > page * 10){  //더 불러올 댓글이 존재하면 더보기 버튼 추가
        const li = `<li class="list-group-item p-0" id="more-comment" data-pno="${(page+1)}" style="background-color: #dabcf1">
                        <button type="button" class="btn btn-sm w-100" onclick="loadComment(${(page+1)})">더보기</button>
                    </li>`
        appendHtml(ul,li);
    }
}

//답글 누르면 댓글이랑 작성박스 토글
async function showReplyComment(a){
    const radio = a.children[2];
    if(radio.checked === true){
        radio.checked = false;
    }else{
        radio.checked = true;
    }
    await toggleReplyComment();
}


//댓글에 대한 답글 얻어오기
function loadReplyComment(cno, page){ // 댓글번호, 페이지 번호
    return new Promise((resolve, reject) => {
        fetch(`${getRoot()}/comment/reply/${cno}?p=${page}`,{
            headers: {'Content-Type': 'application/json'}
        })
        .then(response => {
            if(response.status === 200) {
                resolve(response.json());
            }else{
                reject(new Error("Request is failed"));
            }
        });
    });
}


//답글을 누른곳에 대한 대댓글 리스트 토글
async function toggleReplyComment(){
    const radios = document.querySelectorAll('input[type=radio]'); //라디오 버튼 전부 가져오기

    for (const element of radios) {
        const root = element.parentNode.parentNode; // //각댓글의 최상위 요소는  li
        if(element.checked === true){ //라디오 체크된곳만 진행하기
            const cno = root.attributes.getNamedItem('data-cno').value; //댓글번호
            const ul = document.createElement('ul');
            ul.classList.add('list-group', 'pl-2', 'pt-2');
            ul.style.backgroundColor = 'rgb(255, 244, 238)';
            root.appendChild(ul);
            await appendReplyComment(cno, 1);
        }else{
            let len = root.children.length -1;
            while(root.children[len--].nodeName !== 'A'){
                root.lastElementChild.remove();
            }
        }
    }
}

//댓글번호에 해당하는 페이지 가져와 추가하기
async function appendReplyComment(cno, page){ //ul, 댓글번호, 페이지
    const root = document.querySelector(`li[data-cno='${cno}']`); //댓글번호에 해당하는 최상위 li태그
    const ul = root.lastElementChild; //댓글아래에 추가할 대댓글 리스트
    
    const pno = document.createAttribute("data-pno"); //펼쳐진 페이지 번호
    pno.value = page;
    ul.attributes.setNamedItem(pno);  //ul에 몇 페이지까지 펼쳐져 있는지 작성..

    const userName = document.querySelector('#user-name').value;  //로그인한 유저 아이디 string
    const isAdmin = document.querySelector('#admin').value;       //admin 권한을 가진 유저인지 true/false string형
    
    if(ul.querySelector('#reply-comment-writeBox') !== null){ //댓글 작성창 제거
        ul.querySelector('#reply-comment-writeBox').remove();
    }

    if(ul.querySelector('#more-reply-comment') !== null) { //더보기 버튼이 존재하면 삭제하기
        ul.lastElementChild.remove();
    }

    //대댓글 리스트 얻어오기
    const map = await loadReplyComment(cno, page);
    const list = map.list;
    const count = map.count; //대댓글 개수

    const lastLI = ul.lastElementChild;  //ul에 마지막 li를 가져오기
    const lastSeq = (lastLI === null ? 0 : lastLI.attributes.getNamedItem('data-seq').value);
    //if(lastSeq === count) return; //마지막 li번호가 count랑 같으면 끝내기

    const seq = 1 + (page-1)*10; //시작번호 설정

    //대댓글 리스트 작성
    const currentCount = root.querySelector('.reply-cnt').textContent;
    root.querySelector('.reply-cnt').textContent = count; //대댓글 개수 갱신
    if(currentCount !== count){ //변동사항이 있으면 전체 댓글개수도 갱신
        const currentTotalCommentCount =  document.querySelector('#total-comment-count').textContent;
        document.querySelector('#total-comment-count').textContent = (Number(currentTotalCommentCount) + (count-currentCount)).toString();
    }
    //for(const c of list){
    list.forEach( (c, i) =>{
        if(seq+i <= lastSeq) return;
        const regDate_ = c.regDate;
        const regDate = `${regDate_.year.toString().substring(2)}.${regDate_.monthValue}.${regDate_.dayOfMonth}. ${regDate_.hour}:${regDate_.minute}:${regDate_.second}`; //날짜+시간
        const li = `<li class="list-group-item p-2" data-cno="${c.cno}" data-seq="${seq + i}">
                                <div class="comment-info">
                                    <img src="${getRoot()}/file/thumb/profile/${c.id}/${c.profileImage == null ? "none" : c.profileImage}/size?w=25&h=25" width="25" height="25"
                                        alt="프로필이미지" class="rounded-circle" width="25" height="25">
                                    <span class="comment-writer text-muted">${c.name}(${c.id})</span>
                                    <span class="mx-2" style="color: #e2d7df;">|</span>
                                    <span class="comment-regdate text-muted">${regDate}</span>
                                    
                                    ${(userName === c.id || isAdmin === 'true') && c.pub === true ?
                                        `<!-- 작성자 or 관리자 -->
                                        <span class="float-right comment-option">
                                        <div class="dropdown">
                                        <!--Trigger-->
                                        <a  type="button" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false"><i class="fas fa-ellipsis-v"></i></a>
                                        <!--Menu-->
                                        <div class="dropdown-menu dropdown-menu-right">
                                        <a class="dropdown-item" onclick="deleteComment(this);">삭제</a>
                                        <a class="dropdown-item" href="#">????</a>
                                        </div>
                                        </div>
                                        </span>` : ''
                                    }   
                                </div>
                                <hr class="my-1">
                                <p class="comment-content my-1" style="white-space: pre-wrap">${c.pub === true ? c.content : '<i class="fas fa-exclamation-circle mr-1" style="color: #fbbd0d"></i><span style="color: #d0d0d0">삭제된 댓글 입니다.</span>'}</p>
                             </li>`
        appendHtml(ul, li);
    });
    if(count > page * 10){  //더 불러올 댓글이 존재하면 더보기 버튼 추가
        const li = `<li class="list-group-item p-0" id="more-reply-comment" data-pno="${(page+1)}" style="background-color: #e8dcf9;">
                        <button type="button" class="btn btn-sm w-100" onclick="appendReplyComment(${cno},${Number(page) +1})" >더보기</button>
                    </li>`;
        appendHtml(ul,li);
    }
    //만든 대댓글요소 추가
    //div.appendChild(ul);

    //댓글입력창 추가
    const writeBox =
        `<div class="mt-3 mb-2 bg-white" id="reply-comment-writeBox">
                <div class="border rounded"> 
                    <textarea class="form-control no-resize" rows="1" placeholder="댓글입력" maxlength="200"
                             onfocus="focusTextArea(this);" onkeyup="checkMaxLength(this);"></textarea>
                    <div class="p-2 clearfix hide">
                        <span class="currentLength">0</span><span class="maxLength text-muted">/200</span>
                        <button type="button" class="float-right btn btn-outline-success btn-sm" data-cno="${cno}" onclick="addComment(this);">댓글등록</button>
                        <button type="button" class="float-right btn btn-outline-danger btn-sm mr-3" onclick="cancelComment(this);">취소</button>
                    </div>
                </div>
            </div>`;
    appendHtml(ul,writeBox);

    //return div;
}


async function addComment(button) { //댓글추가하기   댓글등록 버튼
    const textArea = button.parentElement.previousElementSibling;
    //비어 있는 문자일 경우 경고창
    if(textArea.value.trim().length === 0){
        showAlert('danger','내용을 입력해주세요.',true);
        return;
    }
    const articleNo = document.querySelector('#ano').value;  //글번호
    const cno = button.attributes.getNamedItem('data-cno').value;  // 답글 쓰기 위한 댓글 번호
    const content = textArea.value.trim();
    const comment = {
        articleNo: articleNo,
        content: content,
        parent: cno
    }
    const response = await fetch(`${getRoot()}/comment`,{  // 댓글 서버 전송
                                    method: 'POST',
                                    headers: {
                                        'Content-Type': 'application/json'
                                    },
                                    body: JSON.stringify(comment)
                                });
    const responseText = await response.text();  //응답코드
    if(responseText === '1'){
        if(cno === '0'){         // 게시글에 대한 댓글일경우 전체 새로고침? => no
            blurTextArea(textArea);
            const ul = document.querySelector('#comment-list');
            //현재 페이지 번호 가져와서 그 페이지만 새로고침
            const pno = ul.attributes.getNamedItem('data-pno').value;
            
            //const scrollTop = Math.max(document.body.scrollTop, document.documentElement.scrollTop); //현재 스크롤 위치
            //ul.innerHTML = '';  //댓글리스트 초기화
            /*(async function () {
                for(let i=1; i<=pno; i++) {
                    await loadComment(i)
                }
            }()).then(() => window.scrollTo(0,scrollTop))*/
            loadComment(Number(pno));
        }else{                      //댓글에 대한 답글일경우 부분 새로고침
            /*let div = button;
            for(; !div.classList.contains('reply-comment') ; div = div.parentElement);
            div.remove();*/
            let ul = button;
            for(; !(ul.nodeName === 'UL') ; ul = ul.parentElement);
            const page = ul.attributes.getNamedItem('data-pno').value;

            document.querySelector('#reply-comment-writeBox').remove();

            appendReplyComment(cno,page);
        }
    }
}

//댓글입력창 펼치기
function focusTextArea(ta) {
    ta.rows = 3;
    ta.nextElementSibling.classList.remove('hide');
}

//댓글입력창 닫기
function blurTextArea(ta) {
    ta.value = '';
    ta.rows = 1;
    ta.nextElementSibling.classList.add('hide');
    ta.nextElementSibling.firstElementChild.textContent = '0'; //span
}

function cancelComment(button) { //취소버튼
    const ta = button.parentElement.previousElementSibling; //textarea
    blurTextArea(ta);
}

function deleteComment(a) { //삭제 버튼
    let li = a;
    for(;(li.nodeName !== 'LI'); li= li.parentElement);
    const cno = li.attributes.getNamedItem('data-cno').value;  //댓글 번호 꺼내기

    fetch(`${getRoot()}/comment/${cno}`,{ // 요청
        method: 'DELETE',
        headers: {'Content-Type': 'text/plain'}
    })
    .then(response => response.text())
    .then(text => {
        if(text === '1'){ // 서버 처리가 완료 된경우
            const content = li.querySelector('.comment-content');
            //삭제된 메시지로 바꾸기
            content.innerHTML = '<i class="fas fa-exclamation-circle mr-1" style="color: #fbbd0d"></i><span style="color: #d0d0d0">삭제된 댓글 입니다.</span>';
        }
    })
}

//추천수
function upRecommend() {
    const articleNo = document.querySelector('#articleNo').value;
    fetch(`${getRoot()}/boards/${articleNo}/recommend`)
    .then(response => response.text())
    .then(text => {
        if(text === '1'){
            const recommendCount = document.querySelector('#recommend-count');
            const number = Number(recommendCount.textContent) + 1;
            recommendCount.textContent = number.toString()
        }else{
            showAlert('info','이미 추천하였습니다.', true);
        }
    });

}

async function deleteBoard(articleNo) { //글번호
    if(!confirm('정말 삭제하시겠습니까?\n삭제하면 복구가 불가능합니다.')) return;
    const response = await fetch(`${getRoot()}/boards/${articleNo}`, {
        method: 'DELETE'
    });
    if(response.status === 200){
        const text = await response.text();
        if(text === '44')
            alert('답변이 달린 게시글은 삭제할 수 없습니다.');
        else if(text === '1')
            location.href = `${getRoot()}/boards`;
        else
            alert('다시 시도 해주세요.');
    }else{
        alert('delete, Error');
    }
}

async function disableBoard(articleNo) {
    if(confirm('비공개 하시겠습니까?')){
        const response = await fetch(articleNo, {
            method: 'PUT'
        });
        if(response.status === 200){
            const text = await response.text();
            if(text === '1')
                location.href = `${getRoot()}/boards`;
            else
                alert('다시 시도 해주세요.');
        }else{
            alert('disable, Error');
        }
    }
}
