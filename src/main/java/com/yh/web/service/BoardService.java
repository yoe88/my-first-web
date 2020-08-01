package com.yh.web.service;

import com.yh.web.dto.board.Board;
import com.yh.web.dto.board.BoardDetail;
import com.yh.web.dto.board.BoardList;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface BoardService {
    int listNum = 10;  //리스트 개수 10개씩
    String SE = File.separator;

    List<BoardList> getBoardList(String field, String query, long page);

    long getBoardListCount(String field, String query);

    int findArticleNo(long articleNo);

    BoardDetail getBoardDetail(long articleNo, boolean isModify);

    int addBoard(Board board, MultipartFile mf);

    int modifyBoard(Board board, MultipartFile mf, boolean isDelete);

    int deleteBoard(long articleNo);

    int upRecommend(long articleNo, String userName);

    List<String> selectTitleLastFive();

    boolean updateBoardPubByArticleNo(long articleNo, Integer pub);

}
