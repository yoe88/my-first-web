package com.yh.web.controller;

import com.yh.web.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

@Slf4j
@Controller
@RequestMapping("/file")
public class FileController {
    private final String SE = File.separator;
    private final FileService fileService;

    public FileController(FileService fileService) {
        log.info("FileController Init...");
        this.fileService = fileService;
    }

    @RequestMapping("/boards/{articleNo}/{file:.+}")
    public ResponseEntity<String> boardFile (@PathVariable("articleNo") String articleNo
                          ,@PathVariable("file") String fileName
                          ,@RequestParam("fName") String originalFileName
                          ,HttpServletResponse response) throws Exception {
        String filePath = FileService.boardPath + SE + articleNo + SE + fileName;
        File file = new File(filePath);
        if(!file.exists())
            return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
        fileService.download(response, file, originalFileName,false, true);
        return ResponseEntity.ok("200");
    }

    /**
     * id/이미지 이름 받아서 경로 지정 해주고 썸네일 메서드 호출
     */
    @GetMapping("/original/{type}/{identify}/{imageFileName:.+}")
    public ResponseEntity<String> originalImage(HttpServletResponse response
            , @PathVariable("type") String type
            , @PathVariable("identify") String identify
            , @PathVariable(value = "imageFileName") String imageFileName
            , @RequestParam(name = "o", required = false) String originalFileName) throws Exception {
        String filePath;

        if(type.equals("profile")){
            if (imageFileName == null) {
                filePath = FileService.defaultProfile;
            } else {
                filePath = FileService.profilePath + SE + identify + SE + imageFileName;
            }
            File file = new File(filePath);
            if(!file.exists())
                return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
            fileService.download(response, file, imageFileName, true, false);
        }else if(type.equals("gallery")){
            filePath = FileService.galleryPath + SE + identify + SE + imageFileName;

            File file = new File(filePath);
            if(!file.exists())
                return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
            fileService.download(response, file, originalFileName, true, false);
        }
        return ResponseEntity.ok("200");
    }


    /**
     * /타입/식별자/이미지 이름 받아서 경로 지정 해주고 썸네일 메서드 호출
     */
    @GetMapping("/thumb/{type}/{identify}/{imageFileName:.+}/size")
    public ResponseEntity<String> printImage(HttpServletResponse response
            , @PathVariable("type") String type
            , @PathVariable("identify") String identify  //아이디, 글번호
            , @PathVariable(value = "imageFileName") String imageFileName
            , @RequestParam("w") int width
            , @RequestParam("h") int height) throws Exception {
        if (width > 500 || height > 500) {
            return new ResponseEntity<>("not allowed, Please check size", HttpStatus.FORBIDDEN);
        }
        String filePath;
        if(type.equals("profile")){
            if (imageFileName.equals("none")) {
                filePath = FileService.defaultProfile;
                log.info("기본프로필 {}", filePath);
            } else {
                filePath = FileService.profilePath + SE + identify + SE + imageFileName;
            }
            File file = new File(filePath);
            if(!file.exists())
                return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
            fileService.toThumbnail(response, file, width, height);
            return new ResponseEntity<>("OK", HttpStatus.OK);
        }else if(type.equals("gallery")){
            filePath = FileService.galleryPath + SE + identify + SE + imageFileName;

            File file = new File(filePath);
            if(!file.exists())
                return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
            fileService.toThumbnail(response, file, width, height);
            return ResponseEntity.ok("ok");
        }

        return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
    }

}