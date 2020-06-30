package com.yh.web.dto.board;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Board {
    private int articleNo;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime regDate;
    private int recommend;
    private int hit;
    private int parent;
    private int grpNo;
    private boolean pub;
    private String ip;
}
