package com.yh.web.service;

import com.yh.web.dto.board.Board;
import com.yh.web.dto.board.BoardDetail;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface BoardService {
    Map<String,Object> getBoardList(String field, String query, int page);
    int getListNum();
    int searchArticleNo(int articleNo);
    BoardDetail getBoardDetail(int articleNo, boolean isModify);
    int getNextArticleNo();

    int getGrpNo(int articleNo);

    int addBoard(Board board, MultipartFile mf);

    int modifyBoard(Board board, MultipartFile mf, boolean isDelete);
}
