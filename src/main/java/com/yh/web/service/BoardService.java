package com.yh.web.service;

import com.yh.web.dto.board.Board;
import com.yh.web.dto.board.BoardDetail;
import com.yh.web.dto.board.BoardList;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public interface BoardService {
    int listNum = 10;  //게시글 개수 10개씩

    List<BoardList> getBoardList(String field, String query, long page);

    long getBoardListCount(String field, String query, long page);

    int findArticleNo(long articleNo);

    BoardDetail getBoardDetail(long articleNo, boolean isModify) throws UnsupportedEncodingException;

    int addBoard(Board board, MultipartFile mf);

    int modifyBoard(Board board, MultipartFile mf, boolean isDelete);

    int deleteBoard(long articleNo);

    int upRecommend(long articleNo, String userName);

    List<String> selectTitleLastFive();

    boolean updateBoardPubByArticleNo(long articleNo, Integer pub);

}
