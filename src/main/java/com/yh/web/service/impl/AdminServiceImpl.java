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

    /**
     * @param field  필드 타입
     * @param query  검색 내용
     * @param page   페이지 위치
     * @return       회원 리스트
     */
    @Override
    public List<Map<String,Object>> getMembers(String field, String query, long page) {
        if(field.equals("id"))
            field = "M.ID";
        Map<String, Object> map = new HashMap<>();
        map.put("field", field);
        map.put("query", query);
        long start = 1 + (page-1) * listNum;
        long end = page * listNum;
        map.put("start",start);
        map.put("end",end);

        return adminDao.selectMembers(map);
    }

    /**
     * @param field  필드 타입
     * @param query  검색 값
     * @return       검색한 회원수
     */
    @Override
    public long getMembersCount(String field, String query) {
        if(field.equals("id"))
            field = "M.ID";
        Map<String, Object> map = new HashMap<>();
        map.put("field", field);
        map.put("query", query);

        return adminDao.selectMembersCount(map);
    }

    /**
     * @param id  사용자 아이디
     * @return      아이디에 해당하는 정보
     */
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

    /**             아이디에 해당하는 회원 권한 변경
     * @param id    사용자 아이디
     * @param role  바꿀 회원 권한
     */
    @Override
    public int updateMemberRole(String id, String role) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        if(role.equals("admin")) role = "ROLE_ADMIN";
        else if(role.equals("user")) role = "ROLE_USER";
        map.put("role", role);

        return adminDao.updateMemberRole(map);
    }

    /**               사용 가능 여부 변경하기
     * @param id         사용자 아이디
     * @param enable     1,0  사용 가능
     */
    @Override
    public int updateMemberEnable(String id, String enable) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("enable", enable);

        return adminDao.updateMemberEnable(map);
    }

    /**
     * @param field  필드 타입
     * @param query  검색 값
     * @param page   페이지 위치
     * @return      검색한 글 리스트
     */
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

    /**
     * @param field  필드 타입
     * @param query  검색 값
     * @return       검색한 글 갯수
     */
    @Override
    public long getBoardListCount(String field, String query) {
        Map<String,Object> map = new HashMap<>();
        map.put("field",field);
        map.put("query",query);

        return adminDao.selectBoardListCount(map);
    }

    /**
     * @param articleNo   글 번호
     * @return           번호에 해당 하는 글정보
     */
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

    /** 체크 된것은 공개처리   체크 안된건 비공개 처리
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


    /**
     * @param page  페이지 위치
     * @return      갤러리 리스트
     */
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

    /**
     * @return  갤러리 갯수
     */
    @Override
    public long getGalleryListCount() {
        return adminDao.selectGalleryListCount();
    }

    /**
     * @param gno  갤러리 번호
     * @return      번호에 해당하는 정보
     */
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
