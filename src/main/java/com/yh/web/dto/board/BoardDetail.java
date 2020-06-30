package com.yh.web.dto.board;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardDetail {

    private int articleNo;
    private String title;
    private String content;
    private String id;
    private String name;
    private LocalDateTime regDate;
    private int recommend;
    private int hit;
    private String fileName;
    private String originalFileName;
    private String profileImage;

}
