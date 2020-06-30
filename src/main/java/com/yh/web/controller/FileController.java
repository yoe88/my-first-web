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
    private final String uploadPath = FileService.uploadPath; //"C:" + SE + "upload"; //업로드 고정 경로
    private final FileService fileService;

    public FileController(FileService fileService) {
        log.info("FileController Init...");
        this.fileService = fileService;
    }

    @RequestMapping("/boards/{articleNo}/{file:.+}")
    public void boardImage(@PathVariable("articleNo") String articleNo
                          ,@PathVariable("file") String fileName
                          ,@RequestParam("fName") String originalFileName
                          ,HttpServletResponse response) throws Exception {
        String filePath = uploadPath + SE + FileService.boardPath + SE + articleNo + SE + fileName;
        File file = new File(filePath);
        fileService.download(response, file, originalFileName,false, true);
    }

    /**
     * id/이미지 이름 받아서 경로 지정 해주고 썸네일 메서드 호출
     */
    @GetMapping("/original/{id}/{imageFileName:.+}")
    public void originalImage(@PathVariable("id") String id, @PathVariable(value = "imageFileName") String imageFileName,
                              HttpServletResponse response) throws Exception {
        String filePath;
        if (imageFileName == null) {
            filePath = uploadPath + SE + FileService.profilePath + SE + FileService.defaultProfile;
        } else {
            filePath = uploadPath + SE + FileService.profilePath + SE + id + SE + imageFileName;
        }
        File file = new File(filePath);
        fileService.download(response, file, imageFileName, true, false);
    }


    /**
     * id/이미지 이름 받아서 경로 지정 해주고 썸네일 메서드 호출
     */
    @GetMapping("/thumb/{id}/{imageFileName:.+}/size")
    public ResponseEntity<String> printProfileImage(HttpServletResponse response
            , @PathVariable("id") String id
            , @PathVariable(value = "imageFileName") String imageFileName
            , @RequestParam("w") int width
            , @RequestParam("h") int height) throws Exception {
        //logger.info("id: {}, imageFile: {}, width {}, height {}", id, imageFileName, width, height);
        if (width > 1500 || height > 1500) {
            log.info("접근 거부");
            return new ResponseEntity<>("not allowed, Please check size", HttpStatus.FORBIDDEN);
        }
        String filePath;
        if (imageFileName.equals("none")) {
            filePath = uploadPath + SE + FileService.profilePath + SE + FileService.defaultProfile;
        } else {
            filePath = uploadPath + SE + FileService.profilePath + SE + id + SE + imageFileName;
        }
        File file = new File(filePath);
        fileService.toThumbnail(response, file, width, height);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}