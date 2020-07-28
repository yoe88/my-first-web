package com.yh.web.service;

import com.yh.web.dto.board.BoardDetail;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public interface AdminService {
    int listNum = 10;  //회원수 10행씩
    int galleryListNum = 40;
    Map<String,Object> getMembers(String field, String query, long page);
    Map<String, Object> getMember(String id) throws UnsupportedEncodingException;

    int updateMemberRole(String id, String role);

    int updateMemberEnable(String id, String enable);

    Map<String, Object> getBoardList(String field, String query, long page);

    BoardDetail getBoardDetail(long articleNo) throws UnsupportedEncodingException;

    boolean updateBoardAllPub(String allNo, String openNo);

    Map<String, Object> getGalleryList(long page) throws UnsupportedEncodingException;

    Map<String, Object> getGalleryDetail(long gno) throws UnsupportedEncodingException;
}
