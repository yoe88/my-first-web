package com.yh.web.controller;

import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;

@Controller
public class FileController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String SE = File.separator;
    private final String path = "C:" + SE + "images";

    public FileController() {
       logger.info("파일 초기화");
    }

    @RequestMapping("/file/boards/{id}/{file:.+}")
    public void boardImage(@PathVariable("id") String id,@PathVariable("file") String fileName,
                         HttpServletResponse response)throws Exception {
        String filePath = path + SE + "board" + SE + id + SE + fileName;
        File file = new File(filePath);
        download(response, file, fileName);
    }

    /**
     * imageFile이 null 인경우 인식할 수 있게 우회
     */
    @GetMapping("/file/thumb/{id}/") //
    public void profileImageHandler(@PathVariable("id") String id, HttpServletResponse response) throws Exception {
        logger.info("우회 프로필");
        profileImage(id,null,response);
    }

    /**
     *
     */
    @GetMapping("/file/thumb/{id}/{imageFile:.+}")
    public void profileImage(@PathVariable("id") String id, @PathVariable(value = "imageFile") String imageFileName,
                         HttpServletResponse response)throws Exception {
        logger.info("id: {}, imageFile: {}", id, imageFileName);
        String filePath;
        if(imageFileName == null){
            filePath = path + SE + "profile" + SE + "user.png";
        }else{
            filePath = path + SE + "profile" + SE + id + SE + imageFileName;
        }
        File file = new File(filePath);
        toThumbnail(response, file,50,50);
    }

    /**
     * @param file   파일
     * @param width  가로
     * @param height 길이
     */
    public void toThumbnail(HttpServletResponse response , File file , int width, int height) throws IOException {
        ByteArrayOutputStream imageOutputStream = new ByteArrayOutputStream();

        BufferedImage image = Thumbnails.of(file).size(width, height).outputFormat("png").asBufferedImage();
        ImageIO.write(image,"png",imageOutputStream);

        byte[] imgByte = imageOutputStream.toByteArray();

        response.setHeader("Cache-Control", "no-cache");                    //인코더 해줘야 한글 이름으로도 다운로드 받을수 있다.
        response.addHeader("Content-disposition", "attachment; fileName=" + URLEncoder.encode(file.getName(),"UTF-8"));

        OutputStream out = response.getOutputStream();
        out.write(imgByte);
        out.close();
    }

    /**
     * @param response              응답객체
     * @param file                  파일
     * @param originFileName     원본 파일명
     */
    private void download(HttpServletResponse response,
                          File file,
                          String originFileName) throws IOException {
        logger.info("다운로드 시작");

        response.setHeader("Cache-Control", "no-cache");
        response.addHeader("Content-disposition", "attachment; fileName=" + URLEncoder.encode(originFileName,"UTF-8"));

        OutputStream out = response.getOutputStream();
        FileInputStream in = new FileInputStream(file);
        byte[] buffer = new byte[1024 * 8];
        while (true) {
            int count = in.read(buffer); // 버퍼에 읽어들인 문자개수
            if (count == -1) // 버퍼의 마지막에 도달했는지 체크
                break;
            out.write(buffer, 0, count);
        }
        in.close();
        out.close();
    }
}