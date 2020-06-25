package com.yh.web.controller;

import com.yh.web.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

@Controller
@RequestMapping("/file")
public class FileController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String SE = File.separator;
    private final String uploadPath = "C:" + SE + "upload"; //업로드 고정 경로
    private final FileService fileService;

    public FileController(FileService fileService) {
        logger.info("FileController Init...");
        this.fileService = fileService;
    }

    @RequestMapping("/boards/{id}/{file:.+}")
    public void boardImage(@PathVariable("id") String id, @PathVariable("file") String fileName,
                           HttpServletResponse response) throws Exception {
        String filePath = uploadPath + SE + "board" + SE + id + SE + fileName;
        File file = new File(filePath);
        //fileService.download(response, file, fileName);
    }

    /**
     * id/이미지 이름 받아서 경로 지정 해주고 썸네일 메서드 호출
     */
    @GetMapping("/original/{id}/{imageFileName:.+}")
    public void originalImage(@PathVariable("id") String id, @PathVariable(value = "imageFileName") String imageFileName,
                              HttpServletResponse response) throws Exception {
        String filePath;
        if (imageFileName == null) {
            filePath = uploadPath + SE + FileService.getProfilePath() + SE + "user.png";
        } else {
            filePath = uploadPath + SE + FileService.getProfilePath() + SE + id + SE + imageFileName;
        }
        File file = new File(filePath);
        fileService.download(response, file, imageFileName, true, false);
    }

    /**
     * imageFileName이 none 인경우 인식할 수 있게 우회
     */
    /*@GetMapping("/thumb/{id}/none")
    public void profileImageHandler(@PathVariable("id") String id
            , @RequestParam("w") int width
            , @RequestParam("h") int height
            , HttpServletResponse response) throws Exception {
        logger.info("우회 프로필");
        profileImage(response, id, null, width, height);
    }*/

    /**
     * id/이미지 이름 받아서 경로 지정 해주고 썸네일 메서드 호출
     */
    @GetMapping("/thumb/{id}/{imageFileName:.+}")
    public ResponseEntity<String> printProfileImage(HttpServletResponse response
            , @PathVariable("id") String id
            , @PathVariable(value = "imageFileName") String imageFileName
            , @RequestParam("w") int width
            , @RequestParam("h") int height) throws Exception {
        //logger.info("id: {}, imageFile: {}, width {}, height {}", id, imageFileName, width, height);
        if (width > 1500 || height > 1500) {
            logger.info("접근 거부");
            return new ResponseEntity<String>("not allowed, Please check size", HttpStatus.FORBIDDEN);
        }
        String filePath;
        if (imageFileName.equals("none")) {
            filePath = uploadPath + SE + FileService.getProfilePath() + SE + "user.png";
        } else {
            filePath = uploadPath + SE + FileService.getProfilePath() + SE + id + SE + imageFileName;
        }
        File file = new File(filePath);
        fileService.toThumbnail(response, file, width, height);
        return new ResponseEntity<String>("OK", HttpStatus.OK);
    }
}