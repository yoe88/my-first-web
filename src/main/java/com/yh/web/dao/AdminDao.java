package com.yh.web.dao;

import com.yh.web.dto.board.BoardDetail;
import com.yh.web.dto.board.BoardList;

import java.util.List;
import java.util.Map;

public interface AdminDao {
    List<Object> selectMembers(Map<String, Object> map);

    int selectMembersCount(Map<String, Object> map);

    Map<String, Object> selectMemberById(String id);

    int updateMemberRole(Map<String, String> map);

    int updateMemberEnable(Map<String, String> map);

    List<BoardList> selectBoardList(Map<String, Object> map);

    int selectBoardListCount(Map<String, Object> map);

    BoardDetail selectBoardDetailByArticleNo(int articleNo);

    int updateBoardPub(Map<String, Integer> map);
}
