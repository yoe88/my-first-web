package com.yh.web.service;

import com.yh.web.dto.board.Board;
import com.yh.web.dto.board.BoardDetail;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface BoardService {
    int listNum = 10;  //게시글 개수 10개씩

    Map<String,Object> getBoardList(String field, String query, int page);

    int searchArticleNo(int articleNo);
    BoardDetail getBoardDetail(int articleNo, boolean isModify);
    int getNextArticleNo();

    int getGrpNo(int articleNo);

    int addBoard(Board board, MultipartFile mf);

    int modifyBoard(Board board, MultipartFile mf, boolean isDelete);

    int deleteBoard(int articleNo);

    int upRecommend(int articleNo, String userName);
}
