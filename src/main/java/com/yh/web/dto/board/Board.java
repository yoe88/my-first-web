package com.yh.web.dto.board;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Board {
    private long articleNo;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime regDate;
    private long recommend;
    private long hit;
    private long parent;
    private long grpNo;
    private boolean pub;
    private String ip;
}
