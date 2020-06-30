package com.yh.web.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CommentList {
    private int cno;
    private String profileImage;
    private String name;
    private String id;
    private String content;
    private LocalDateTime regDate;
    private boolean pub;
    private int count;
}
