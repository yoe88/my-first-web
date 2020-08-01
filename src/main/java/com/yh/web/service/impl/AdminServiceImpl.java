package com.yh.web.service.impl;

import com.yh.web.Utils;
import com.yh.web.dao.AdminDao;
import com.yh.web.dto.board.BoardDetail;
import com.yh.web.dto.board.BoardList;
import com.yh.web.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    private final AdminDao adminDao;

    public AdminServiceImpl(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    @Override
    public Map<String,Object> getMembers(String field, String query, long page) {
        if(field.equals("id"))
            field = "M.ID";
        Map<String, Object> map = new HashMap<>();
        map.put("field", field);
        map.put("query", query);
        long start = 1 + (page-1) * listNum;
        long end = page * listNum;
        map.put("start",start);
        map.put("end",end);

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("list",adminDao.selectMembers(map));
        resultMap.put("count",adminDao.selectMembersCount(map));

        return resultMap;
    }

    @Override
    public Map<String, Object> getMember(String id) {
        Map<String, Object> member = adminDao.selectMemberById(id);
        if(member != null){
            String profileImage = (String) member.get("PROFILEIMAGE");
            if(profileImage != null){
                profileImage = Utils.urlEncode( profileImage);
            }
            member.put("PROFILEIMAGE",profileImage);
        }
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
    public List<BoardList> getBoardList(String field, String query, long page) {
        Map<String,Object> map = new HashMap<>();
        map.put("field",field);
        map.put("query",query);
        long start = 1 + (page-1) * listNum;
        long end = page * listNum;
        map.put("start",start);
        map.put("end",end);

        return adminDao.selectBoardList(map);

    }

    @Override
    public long getBoardListCount(String field, String query) {
        Map<String,Object> map = new HashMap<>();
        map.put("field",field);
        map.put("query",query);

        return adminDao.selectBoardListCount(map);
    }

    @Override
    public BoardDetail getBoardDetail(long articleNo) {
        BoardDetail boardDetail = adminDao.selectBoardDetailByArticleNo(articleNo);
        if(boardDetail != null){
            if(boardDetail.getProfileImage() != null){
                String profileImageName = Utils.urlEncode(boardDetail.getProfileImage());
                boardDetail.setProfileImage(profileImageName);
            }
            if(boardDetail.getFileName() != null) {
                String fileName = Utils.urlEncode(boardDetail.getFileName());
                boardDetail.setFileName(fileName);
                String encodeOriginalFileName = Utils.urlEncode(boardDetail.getOriginalFileName());
                boardDetail.setEncodeOriginalFileName(encodeOriginalFileName);
            }
        }
        return boardDetail;
    }

    /**
     * @param allNo_  모든 글번호
     * @param openNo_ 체크된 글번호
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
    public List<Map<String,String>> getGalleryList(long page) {
        Map<String, Long> map = new HashMap<>();
        long start = 1 + (page-1) * galleryListNum;
        long end = page * galleryListNum;
        map.put("start", start);
        map.put("end", end);

        List<Map<String,String>> list = adminDao.selectGalleryList(map);
        //URL Encode
        for (Map<String, String> gallery : list) {
            String encode = Utils.urlEncode(gallery.get("fileName"));
            gallery.put("fileName", encode);
        }
        return list;
    }

    @Override
    public long getGalleryListCount() {
        return adminDao.selectGalleryListCount();
    }

    @Override
    public Map<String, Object> getGalleryDetail(long gno) {
        Map<String, Object> galleryDetail = adminDao.selectGalleryDetail(gno);
        if(galleryDetail != null){
            List<Map<String,String>> fileList = (List<Map<String, String>>) galleryDetail.get("file");
            for (Map<String,String> file : fileList ){
                String fileName = Utils.urlEncode(file.get("fileName"));
                file.put("fileName", fileName);
                String originalFileName = Utils.urlEncode(file.get("originalFileName"));
                file.put("originalFileName", originalFileName);
            }
        }
        return galleryDetail;
    }
}
