package com.yh.web.dao;

import com.yh.web.dto.board.Board;
import com.yh.web.dto.board.BoardDetail;
import com.yh.web.dto.board.BoardFile;
import com.yh.web.dto.board.BoardList;

import java.util.List;
import java.util.Map;

public interface BoardDao {
    List<BoardList> selectBoardList( Map<String,Object> map);
    long selectBoardListCount( Map<String,Object> map);
    int findBoardByArticleNo(long articleNo);

    BoardDetail selectBoardDetailByArticleNo(long articleNo);
    long selectNextSequence();

    long selectGrpNoByArticleNo(long articleNo);

    int insertBoard(Board board);

    int insertBoardFile(BoardFile boardFile);

    void updateBoardHitByArticleNo(long articleNo);

    int updateBoard(Board board);

    String selectBoardFileNameByArticleNo(long articleNo);

    int updateBoardFile(BoardFile boardFile);

    int deleteBoardFileByArticleNo(long articleNo);

    int updateBoardPubByArticleNo(Map<String, Object> map);

    int deleteBoardByArticleNo(long articleNo);

    boolean isAlreadyExistsID(Map<String, Object> map);

    int insertRecommend(Map<String, Object> map);

    List<String> selectTitleLastFive();

    long selectChildCount(long articleNo);
}
