package com.yh.web.dto;

import lombok.Data;

@Data
public class GalleryFile {
    private long no;
    private long gno;
    private String fileName;
    private long fileSize;
    private String originalFileName;
}
