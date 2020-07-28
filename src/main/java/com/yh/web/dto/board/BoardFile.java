package com.yh.web.dto.board;

import lombok.Data;

@Data
public class BoardFile {
    private long articleNo;
    private String fileName;
    private long fileSize;
    private String originalFileName;
}
