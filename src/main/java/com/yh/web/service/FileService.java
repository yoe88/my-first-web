package com.yh.web.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public interface FileService{
    String SE = File.separator;
    String uploadPath = "C:" + SE + "upload"; //기본 저장위치
    String profilePath = "profile";     //사용자 프로필 폴더 이름
    String defaultProfile = "user.png";  //기본 이미지
    String boardPath = "board";          //게시판 폴더 이름
    
    void toThumbnail(HttpServletResponse response, File file, int width, int height) throws IOException;

    void download(HttpServletResponse response, File file, String originFileName
            , boolean isImage, boolean isDownload) throws IOException;

    String upload(MultipartFile mf, String folderName);
    boolean deleteFile(String folderName, String fileName);
}
