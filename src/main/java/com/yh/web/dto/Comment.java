package com.yh.web.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    int cno;
    int articleNo;
    String content;
    String writer;
    LocalDateTime regDate;
    int parent;
    String ip;
    boolean pub;
}
