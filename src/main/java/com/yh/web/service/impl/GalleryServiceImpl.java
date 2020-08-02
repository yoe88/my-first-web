package com.yh.web.service.impl;

import com.yh.web.Utils;
import com.yh.web.dao.GalleryDao;
import com.yh.web.dto.Gallery;
import com.yh.web.dto.GalleryFile;
import com.yh.web.service.FileService;
import com.yh.web.service.GalleryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


@Slf4j
@Service
public class GalleryServiceImpl implements GalleryService {

    private final GalleryDao galleryDao;
    private final FileService fileService;

    public GalleryServiceImpl(GalleryDao galleryDao, FileService fileService) {
        this.galleryDao = galleryDao;
        this.fileService = fileService;
    }

    /**
     * @param page  페이지 숫자
     * @return      페이지에 해당하는 갤러리 리스트
     */
    @Override
    public List<Map<String,String>> getGalleryList(long page) {
        Map<String, Long> map = new HashMap<>();
        long start = 1 + (page-1) * listNum;
        long end = page * listNum;
        map.put("start", start);
        map.put("end", end);

        List<Map<String,String>> list = galleryDao.selectGalleryList(map);
        //파일 이름 순회 하며 URL Encode 작업
        for (Map<String, String> gallery : list) {
            String encodeFileName = Utils.urlEncode(gallery.get("fileName"));
            gallery.put("fileName", encodeFileName);
        }

        return list;
    }

    /**
     * @return  갤러리 갯수
     */
    @Override
    public long getGalleryListCount() {
        return galleryDao.selectGalleryListCount();
    }

    /**
     * @param model  갤러리 정보
     * @return    성공시 true 실패 false
     */
    @Transactional
    @Override
    public boolean addGallery(Map<String, Object> model)  {
        long gno = 0;

        try{
            //다음 시퀀스 번호 얻기
            gno = galleryDao.selectNextSequence();
            Gallery gallery = (Gallery) model.get("gallery");
            gallery.setGno(gno);

            //갤러리 테이블 삽입
            galleryDao.insertGallery(gallery);

            //파일 관련 작업
            List<MultipartFile> files = (List<MultipartFile>) model.get("files");

            if(files.size() == 0) {
                throw new NullPointerException("첨부된 파일이 없음");
            }

            String folderPath = FileService.galleryPath + SE + gno;
            for(MultipartFile mf : files){
                if(mf.getSize() == 0) continue;

                String safeFileName = fileService.upload(mf, folderPath);
                if(safeFileName == null){
                    throw new RuntimeException("파일 업로드 실패!");
                }

                GalleryFile galleryFile = new GalleryFile();
                galleryFile.setGno(gno);
                galleryFile.setFileName(safeFileName);
                galleryFile.setFileSize(mf.getSize());
                galleryFile.setOriginalFileName(mf.getOriginalFilename());
                galleryDao.insertGalleryFile(galleryFile);
            }

            return true;
        } catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if(gno != 0){
                String folderPath = FileService.galleryPath + SE + gno;
                fileService.deleteFolder(folderPath);
            }
            return false;
        }
    }

    /**
     * @param gno  갤러리 번호
     * @return    번호에 해당하는 갤러리 정보
     */
    @Override
    public Map<String, Object> getGalleryDetail(long gno) {
        Map<String, Object> galleryDetail = galleryDao.selectGalleryDetail(gno);
        if(galleryDetail != null) {
            List<Map<String, String>> fileList = (List<Map<String, String>>) galleryDetail.get("file");
            for (Map<String, String> file : fileList) {
                String encodeFileName = Utils.urlEncode(file.get("fileName"));
                file.put("fileName", encodeFileName);
                String encodeOriginalFileName = Utils.urlEncode(file.get("originalFileName"));
                file.put("originalFileName", encodeOriginalFileName);
            }
        }

        return galleryDetail;
    }

    /**
     * 갤러리 테이블 수정, 갤러리 파일 테이블(삭제, 추가)  파일 (삭제, 추가)  총 5가지
     * @param model 갤러리 (번호,제목) 삭제할 (파일번호, 파일이름), 추가할 파일
     */
    @Transactional
    @Override
    public boolean updateGallery(Map<String, Object> model) {

        try{
            // 글 번호
            long gno = (long) model.get("gno");

            List<Long> deleteNo = (List<Long>) model.get("deleteNo");
            if(deleteNo != null){   //삭제 할 파일이 존재 하는 경우
                //갤러리 파일 테이블 삭제
                galleryDao.deleteGalleryFile(deleteNo);

                //파일 삭제
                List<String> deleteFileNameList = (List<String>) model.get("deleteFileName");
                for(String fileName : deleteFileNameList){
                    String filePath = FileService.galleryPath + SE + gno + SE + fileName;
                    fileService.deleteFile(filePath);
                }
            }

            //갤러리 테이블 수정
            Map<String, Object> gallery = new HashMap<>();
            gallery.put("gno", gno);
            gallery.put("title", model.get("title"));
            galleryDao.updateGallery(gallery);

            List<MultipartFile> files = (List<MultipartFile>) model.get("files");
            if(files.size() != 0){  //첨부된 파일이 있는 경우
                for(MultipartFile mf : files){
                    if(mf.getSize() == 0) continue;

                    //업로드 하고
                    String folderPath = FileService.galleryPath + SE + gno;
                    String safeFileName = fileService.upload(mf, folderPath);

                    //갤러리 파일 테이블 추가
                    GalleryFile galleryFile = new GalleryFile();
                    galleryFile.setGno(gno);
                    galleryFile.setFileName(safeFileName);
                    galleryFile.setFileSize(mf.getSize());
                    galleryFile.setOriginalFileName(mf.getOriginalFilename());
                    galleryDao.insertGalleryFile(galleryFile);
                }
            }

            return true;
        }catch (Exception e){
            log.info("Gallery Update error");
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    /**     번호를 가지고 갤러리 테이블 삭제 하면 연쇄적으로 갤러리 파일 테이블도 삭제 된다.
     *      그리고 번호 폴더를 통째로 삭제 진행
     * @param gno  삭제 할 갤러리 번호
     */
    @Transactional
    @Override
    public boolean deleteGallery(long gno) {

        try{
            String folderPath = FileService.galleryPath + SE + gno;
            boolean deleteResult = fileService.deleteFolder(folderPath);  //폴더 삭제
            if(!deleteResult){
                throw new RuntimeException("폴더 삭제 실패");
            }

            int result = galleryDao.deleteGallery(gno);
            if(result == 0){
                throw new RuntimeException("갤러리 삭제 실패");
            }

            return true;
        }catch (Exception e){
            log.info("Gallery Update error");
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    /**
     * @param gno  갤러리 번호
     * @param pub  번호에 해당하는 갤러리 공개 수정
     */
    @Override
    public boolean updateGalleryPub(long gno, Integer pub) {
        Map<String, Object> map = new HashMap<>();
        map.put("gno",gno);
        map.put("pub",pub);

        int result = galleryDao.updateGalleryPub(map);
        return result == 1;
    }

    //************************************** 관리자 서비스 ********************************
    /**  체크된건 공개  체크 안된건 비공개 처리
    * @param allNo_    모든 갤러리 번호
    * @param openNo_   체크된 갤러리 번호
    */
    @Transactional
    @Override
    public boolean updateGalleriesPub(String allNo_, String openNo_) {
        try{
            List<String> closeNo = new ArrayList<>(Arrays.asList(allNo_.split(" ")));
            List<String> openNo = new ArrayList<>(Arrays.asList(openNo_.split(" ")));
            closeNo.removeAll(openNo);

            log.info("오픈{}",openNo.toString());
            if(openNo.size() != 0){
                galleryDao.updateGalleryOpenPub(openNo);
            }
            log.info("닫기{}",closeNo.toString());
            if(closeNo.size() != 0){
                galleryDao.updateGalleryClosePub(closeNo);
            }
            return true;
        } catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }
}
