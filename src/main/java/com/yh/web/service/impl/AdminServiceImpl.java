package com.yh.web.service.impl;

import com.yh.web.dao.AdminDao;
import com.yh.web.dto.board.BoardDetail;
import com.yh.web.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.awt.image.ImageProducer;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    private final AdminDao adminDao;

    public AdminServiceImpl(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    @Override
    public Map<String,Object> getMembers(String field, String query, int page) {
        if(field.equals("id"))
            field = "M.ID";
        Map<String, Object> map = new HashMap<>();
        map.put("field", field);
        map.put("query", query);
        int start = 1 + (page-1) * listNum;
        int end = page * listNum;
        map.put("start",start);
        map.put("end",end);

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("list",adminDao.selectMembers(map));
        resultMap.put("count",adminDao.selectMembersCount(map));

        return resultMap;
    }

    @Override
    public Map<String, Object> getMember(String id) throws UnsupportedEncodingException {
        Map<String, Object> member = adminDao.selectMemberById(id);
        String profileImage = (String) member.get("PROFILEIMAGE");
        if(profileImage != null){
            profileImage = URLEncoder.encode( profileImage,"UTF-8").replace("+","%20");
        }
        member.put("PROFILEIMAGE",profileImage);
        return member;
    }

    @Override
    public int updateMemberRole(String id, String role) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        if(role.equals("admin")) role = "ROLE_ADMIN";
        else if(role.equals("user")) role = "ROLE_USER";
        map.put("role", role);

        return adminDao.updateMemberRole(map);
    }

    @Override
    public int updateMemberEnable(String id, String enable) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("enable", enable);

        return adminDao.updateMemberEnable(map);
    }

    @Override
    public Map<String, Object> getBoardList(String field, String query, int page) {
        Map<String,Object> map = new HashMap<>();
        map.put("field",field);
        map.put("query",query);
        int start = 1 + (page-1) * listNum;
        int end = page * listNum;
        map.put("start",start);
        map.put("end",end);

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("list",adminDao.selectBoardList(map));
        resultMap.put("count",adminDao.selectBoardListCount(map));

        return resultMap;
    }

    @Override
    public BoardDetail getBoardDetail(int articleNo) throws UnsupportedEncodingException {
        BoardDetail boardDetail = adminDao.selectBoardDetailByArticleNo(articleNo);
        if(boardDetail.getProfileImage() != null){
            String profileImageName = URLEncoder.encode(boardDetail.getProfileImage(), "UTF-8").replace("+", "%20");
            boardDetail.setProfileImage(profileImageName);
        }
        if(boardDetail.getFileName() != null) {
            String fileName = URLEncoder.encode(boardDetail.getFileName(), "UTF-8").replace("+", "%20");
            boardDetail.setFileName(fileName);
            String encodeOriginalFileName = URLEncoder.encode(boardDetail.getOriginalFileName(), "UTF-8").replace("+", "%20");
            boardDetail.setEncodeOriginalFileName(encodeOriginalFileName);
        }
        return boardDetail;
    }

    @Override
    public int updateBoardPub(int articleNo, int pub) {
        Map<String, Integer> map = new HashMap<>();
        map.put("articleNo", articleNo);
        map.put("pub", pub);

        return adminDao.updateBoardPub(map);
    }

    /**
     * @param allNo_  모든 글번호
     * @param openNo_ 체크된 글번호
     * @return
     */
    @Transactional
    @Override
    public boolean updateBoardAllPub(String allNo_, String openNo_) {
        try{
            List<String> closeNo = new ArrayList<>(Arrays.asList(allNo_.split(" ")));
            List<String> openNo = new ArrayList<>(Arrays.asList(openNo_.split(" ")));
            closeNo.removeAll(openNo);

            log.info("오픈{}",openNo.toString());
            if(openNo.size() != 0){
                adminDao.updateBoardOpenPub(openNo);
            }
            log.info("닫기{}",closeNo.toString());
            if(closeNo.size() != 0){
                adminDao.updateBoardClosePub(closeNo);
            }
            return true;
        } catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }


    @Override
    public Map<String, Object> getGalleryList(int page) throws UnsupportedEncodingException {
        Map<String, Integer> map = new HashMap<>();
        int start = 1 + (page-1) * galleryListNum;
        int end = page * galleryListNum;
        map.put("start", start);
        map.put("end", end);

        Map<String,Object> resultMap = new HashMap<>();
        List<Map<String,String>> list = adminDao.selectGalleryList(map);
        //URL Encode
        for (Map<String, String> gallery : list) {
            String encode = URLEncoder.encode(gallery.get("fileName"), "UTF-8").replace("+", "%20");
            gallery.put("fileName", encode);
        }
        resultMap.put("list",list);
        resultMap.put("count",adminDao.selectGalleryListCount());

        return resultMap;
    }

    @Override
    public Map<String, Object> getGalleryDetail(int gno) throws UnsupportedEncodingException {
        Map<String, Object> galleryDetail = adminDao.selectGalleryDetail(gno);
        List<Map<String,String>> fileList = (List<Map<String, String>>) galleryDetail.get("file");
        for (Map<String,String> file : fileList ){
            String fileName = URLEncoder.encode(file.get("fileName"), "UTF-8").replace("+", "%20");
            file.put("fileName", fileName);
            String originalFileName = URLEncoder.encode(file.get("originalFileName"), "UTF-8").replace("+", "%20");
            file.put("originalFileName", originalFileName);
        }
        return galleryDetail;
    }
}
