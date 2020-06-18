package com.yh.web.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Comment {
    private int ArticleNo;
    private int CommentNo;
    private LocalDateTime regDate;
    private String writer;
    private String content;
}
