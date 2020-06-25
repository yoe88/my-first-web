package com.yh.web.service;

import com.yh.web.dto.BoardList;

import java.util.List;

public interface BoardService {
    List<BoardList> getBoardList(String field, String query, int page);
}
