package com.yh.web.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private int cno;
    private int articleNo;
    private String content;
    private String writer;
    private LocalDateTime regDate;
    private int parent;
    private String ip;
    private boolean pub;
}
