package com.yh.web.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardList {
    int articleNo;           //글번호
    String title;            //제목
    String writer;           //작성자
    LocalDateTime regDate;   //등록일
    int recommend;           //추천수
    int hit;                 //조회수
    int cmt;                 //댓글 개수
    int lv;                  //레벨
}
