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
            //console.log('펄스');
            
            //console.dir(li.lastElementChild);
            /* if(li.lastElementChild.classList.contains('comment-write-box'))
                li.lastElementChild.remove(); */
            let len = root.children.length -1;
            /* for(let i = 4; i<len; i++){
                li.lastElementChild.remove();
            } */
            while(root.children[len--].nodeName != 'A'){
                root.lastElementChild.remove();
            }
        }
    });
}     



function test(){  
    const listCmt = new Array();
    const xhttp = new XMLHttpRequest();
    xhttp.open('GET',getRoot() + '/boards/1/reply/1',true);
    xhttp.send();
    xhttp.onreadystatechange = function(){
        if(this.readyState == 4){
            if(this.status == 200){
                listCmt.push(1);
                listCmt.push(2);
                listCmt.push(3);
                console.dir(listCmt);
            }else{
                alert('replyComment, error');
            }
        }
    }
    console.dir(listCmt);
    return listCmt;
}
//상세보기페이지