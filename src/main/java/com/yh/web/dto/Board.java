package com.yh.web.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Board {
    int articleNo;
    String title;
    String content;
    String writer;
    LocalDateTime regDate;
    int recommend;
    int hit;
    int parent;
    int grpNo;
    boolean pub;
    String ip;
}
