package com.yh.web.service;

import com.yh.web.dto.board.BoardDetail;
import com.yh.web.dto.board.BoardList;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public interface AdminService {
    int listNum = 10;  //리스트 10개씩
    int galleryListNum = 40;

    Map<String,Object> getMembers(String field, String query, long page);

    Map<String, Object> getMember(String id);

    int updateMemberRole(String id, String role);

    int updateMemberEnable(String id, String enable);

    List<BoardList> getBoardList(String field, String query, long page);

    long getBoardListCount(String field, String query);

    BoardDetail getBoardDetail(long articleNo);

    boolean updateBoardAllPub(String allNo, String openNo);

    List<Map<String,String>> getGalleryList(long page) ;

    long getGalleryListCount();

    Map<String, Object> getGalleryDetail(long gno);
}
