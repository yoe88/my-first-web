function getRoot(){
    return "/web";
}
//텍스트로 된 태그를 추가할때
function appendHtml(parent, str) {
    const div = document.createElement('div');
    div.innerHTML = str;
    while (div.children.length > 0) {
        parent.appendChild(div.children[0]);
    }
}

//로딩창 보이기
function showLoading(){
    //화면의 높이와 너비를 구함
    const height = window.innerHeight;
    const width = window.innerWidth;
    //화면에 출력할 배경을 설정하고 추가
    const body_ = '<div id = "background"></div>';
    appendHtml(document.body, body_);

    const body = document.querySelector("#background");
    //body 넓이를 전체로 채움
    body.style.width = width + 'px';
    body.style.height = height + 'px';

    //로딩이미지 추가
    let load = '<div class="spinner-border text-danger" style="z-index:100; width:3rem; height:3rem;"></div>';
    appendHtml(body, load);
}
//로딩창 닫기
function closeLoading(){
    document.querySelector("#background").remove();
}
//경고창 보이기
let setTimeoutAlert = null;
function showAlert(color, text, autoOff) {
    if(document.querySelector("div[role=alert]") != null){
        document.querySelector("div[role=alert]").remove();
        clearTimeout(setTimeoutAlert);
    }
    let alert = `<div class="btn btn-outline-${color} d-inline-block" role="alert">
                    <strong>${text}<i class="fas fa-exclamation mx-2" style="font-size: 1.25rem;"></i></strong>
                </div>`;
    appendHtml(document.body, alert);
    if(autoOff === true){
        setTimeoutAlert = setTimeout(function () {
            closeAlert();
        }, 1500);
    }
}

//경고창 닫기
function closeAlert() {
    if(document.querySelector("div[role=alert]") != null)
        document.querySelector("div[role=alert]").remove();
}