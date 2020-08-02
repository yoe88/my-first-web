package com.yh.web.service.impl;

import com.yh.web.Utils;
import com.yh.web.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;

@Slf4j
@Service
public class FileServiceImpl implements FileService {
    /**
     * 원본 이미지를 썸네일로 만들어 출력
     * @param file   파일
     * @param width  너비
     * @param height 높이
     */
    public void toThumbnail(HttpServletResponse response, File file, int width, int height) throws IOException {
        //원본 이미지의 BufferedImage 객체를 생성
        BufferedImage oImg = ImageIO.read(file);
        ByteArrayOutputStream imageOutputStream = new ByteArrayOutputStream();
        //원본 이미지의 너비 높이 저장
        int oWidth = oImg.getWidth();
        int oHeight = oImg.getHeight();
        // 원본 너비를 기준으로 하여 만들고자하는 썸네일의 비율로 높이를 계산한다.
        int nWidth = oWidth;
        int nHeight = (oWidth * height) / width;
        // 계산된 높이가 원본보다 높다면 crop이 안되므로
        // 원본 높이를 기준으로 썸네일의 비율로 너비를 계산한다.
        if (nHeight > oHeight) {
            nWidth = (oHeight * width) / height;
            nHeight = oHeight;
        }
        // 계산된 크기로 원본 이미지를 가운데에서 crop 한다.
        BufferedImage cropImg = Scalr.crop(oImg, (oWidth - nWidth) / 2, (oHeight - nHeight) / 2, nWidth, nHeight);
        // crop된 이미지로 썸네일을 생성한다.
        BufferedImage thumImg = Scalr.resize(cropImg, width, height);
        //만든 썸네일을 스트림에 저장
        ImageIO.write(thumImg, "png", imageOutputStream);

        byte[] imgByte = imageOutputStream.toByteArray();
        response.setHeader("Cache-Control", "no-cache");               //인코더 해야 한글 이름으로도 다운로드 받을수 있다.
        response.addHeader("Content-disposition", "inline; fileName=" + Utils.urlEncode(file.getName()) );
        response.setContentType("image/png");

        OutputStream out = response.getOutputStream();
        out.write(imgByte);
        out.close();
    }

    /**
     * @param response       응답객체
     * @param file           파일
     * @param originalFileName 원본 파일명
     * @param isImage       이미지인지 
     * @param isDownload    다운로드할것인지
     */
    public void download(HttpServletResponse response
            , File file
            , String originalFileName
            , boolean isImage
            , boolean isDownload) throws IOException {
        if(originalFileName == null)
            originalFileName = file.getName();
        response.setHeader("Cache-Control", "no-cache");
        if(isDownload){  //다운로드 일 경우 attachment 아니면 inline
            response.addHeader("Content-disposition", "attachment; fileName=" + Utils.urlEncode(originalFileName));
        }else{
            response.addHeader("Content-disposition", "inline; fileName=" + Utils.urlEncode(originalFileName));
        }
        if (isImage) { //이미지 일 경우 ContentType 이미지로
            String fileName = file.getName();
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
            response.setContentType(String.format("image/%s", fileExtension));
        }
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

    /**
     * @param mf         단일 파일
     * @param folderPath 파일이 들어갈 폴더 경로
     * @return 파일이 성공적으로 업로드 되었으면 파일이름, 실패 했으면 null
     */
    public String upload(MultipartFile mf, String folderPath) {
        log.info(folderPath);
        File folderFile = new File(folderPath);
        if (!folderFile.exists())
            folderFile.mkdirs();
        String originalFileName = mf.getOriginalFilename(); // 원본 파일 명
        String safeFile = System.currentTimeMillis() + originalFileName; // 수정된 파일이름
        String safeFilePath = folderPath + SE + safeFile; // 파일경로
        try {
            mf.transferTo(new File(safeFilePath));
            return safeFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param filePath  삭제할 파일 경로
     * @return  삭제가 정상적으로 됐으면  true 실패 했으면 false
     */
    public boolean deleteFile(String filePath) {

        try {
            File file = new File(filePath);
            if (file.exists())          //파일이 존재하는경우 삭제시도
                return file.delete();
            else return true;          //존재하지 않는경우 true
        } catch (Exception e) {        //시도하다가 에러난경우 false
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param folderPath  삭제할 폴더 경로
     * @return  성공 했으면 true
     */
    @Override
    public boolean deleteFolder (String folderPath){
        try {
            File file = new File(folderPath);
            if(file.exists()) {
                File[] fList = file.listFiles();  //폴더 안에 있는 파일목록
                if (fList != null) {                //비어 있지 않다면 파일 전부 제거
                    for (File f : fList) {
                        f.delete();
                    }
                }
                return file.delete();           //그러고 나서 폴더 삭제 시도
            }else
                return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
}
