package com.yh.web.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Gallery {
    private long gno;
    private String title;
    private String writer;
    private LocalDateTime regDate;
    private String ip;
}
