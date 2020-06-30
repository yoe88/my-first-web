/*function changeFile(thisFile) { //파일버튼
    const acceptedImageTypes = ['image/gif', 'image/jpeg', 'image/png', 'image/bmp', 'image/tiff']; //이미지 파일 종류
    const thumbImg = document.querySelector('.thumb-img'); //썸네일
    const file = thisFile.files[0];//fileList[0]; 첫번째파일객체
    const fileName = document.querySelector(".custom-file-label"); //화면상에 보이는 파일이름

    if (thisFile.value.length === 0) {  // 파일이름
        thisFile.value = '';  //파일초기화
        fileName.textContent = '파일을 선택해 주세요.';
        thumbImg.src = '';
        thumbImg.style.display = 'none';
        return;
    }
    if (file.size > 10485760) {  //10485760 10mb;
        //alert('파일 사이즈는 최대 10mb입니다.');
        showAlert('danger','파일 사이즈는 최대 10mb입니다.',true);
        thisFile.value = '';  //파일초기화
        fileName.textContent = '파일을 선택해 주세요.';
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
}*/