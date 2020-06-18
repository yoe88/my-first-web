package com.yh.web.dto;

import lombok.Data;

@Data
public class BoardFile {
    int no;
    int articleNo;
    String fileName;
    int fileSize;
    String originFileName;
}
