package com.yh.web.dao;

import com.yh.web.dto.board.Board;
import com.yh.web.dto.board.BoardDetail;
import com.yh.web.dto.board.BoardFile;
import com.yh.web.dto.board.BoardList;

import java.util.List;
import java.util.Map;

public interface BoardDao {
    List<BoardList> selectBoardList( Map<String,Object> map);
    int selectBoardListCount( Map<String,Object> map);
    int selectBoardCountByArticleNo(int articleNo);

    BoardDetail selectBoardDetailByArticleNo(int articleNo);
    int selectNextSequence();

    int selectGrpNoByArticleNo(int articleNo);

    int insertBoard(Board board);

    int insertBoardFile(BoardFile boardFile);

    void updateBoardHitByArticleNo(int articleNo);

    int updateBoard(Board board);

    String selectBoardFileNameByArticleNo(int articleNo);

    int updateBoardFile(BoardFile boardFile);

    int deleteBoardFileByArticleNo(int articleNo);

    int updateBoardPubByArticleNo(int articleNo);

    int deleteBoardByArticleNo(int articleNo);

    boolean isAlreadyExistsID(Map<String, Object> map);

    int insertRecommend(Map<String, Object> map);

    List<String> selectTitleLastFive();

    int selectChildCount(int articleNo);
}
