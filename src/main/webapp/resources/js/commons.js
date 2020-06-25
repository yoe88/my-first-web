'use strict'
function getRoot(){
    return "/web";
}

//엔터키 방지
document.addEventListener('keydown', function(event) {
    if (event.keyCode === 13) {
        event.preventDefault();
    };
}, true);
//특수키
const functionKey = [9,13,16,17,18,19,20,27,33,34,35,36,37,38,39,40,44
    ,45,46,91,93,112,113,114,115,116,117,118,119,120,121,122,123,144,145
];

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
    const alert = `<div class="btn btn-outline-${color} d-inline-block" role="alert">
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

//이미지 파일, 썸네일 들어갈 img, 너비, 높이
function makeThumbnail(file, thumbImg, width,height) {
    //썸네일 제작
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = function () {
        const oImage = new Image(); //drawImage 메서드에 넣기 위해 이미지 객체화
        oImage.src = reader.result; //data-uri를 이미지 객체에 주입
        oImage.onload = function () {
            //리사이즈를 위한 캔버스
            const canvas = document.createElement('canvas');
            const canvasContext = canvas.getContext("2d");

            //캔버스 크기 설정
            canvas.width = width;
            canvas.height = height;

            //원본 이미지의 너비 높이 저장
            const oWidth = oImage.width; //원본 너비
            const oHeight = oImage.height; //원본 높이

            // 원본 너비를 기준으로 하여 만들고자하는 썸네일의 비율로 높이를 계산한다.
            let nWidth = oWidth;
            let nHeight = (height / width) * oWidth;
            // 계산된 높이가 원본보다 높으면
            // 원본 높이를 기준으로 썸네일의 비율로 너비를 계산한다.
            if (nHeight > oHeight) {
                nWidth = (width / height) * oHeight;
                nHeight = oHeight;
            }

            //계산된 기준점으로 캔버스에 그리기
            canvasContext.drawImage(this, (oWidth - nWidth) / 2, (oHeight - nHeight) / 2, nWidth, nHeight, 0, 0, width, height);

            //캔버스에 그린 이미지를 다시 data-uri 형태로 변환
            const dataURI = canvas.toDataURL("image/jpeg");

            //썸네일 이미지 보여주기
            thumbImg.src = dataURI;
            thumbImg.style.display = 'block';
        };
    };
}