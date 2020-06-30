package com.yh.web.dto.board;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardList {
    private String articleNo;        //글번호
    private String title;            //제목
    private String writer;           //작성자(닉네임)
    private LocalDateTime regDate;   //등록일
    private int recommend;           //추천수
    private int hit;                 //조회수
    private int cmt;                 //댓글 개수
    private int lv;                  //레벨
}
