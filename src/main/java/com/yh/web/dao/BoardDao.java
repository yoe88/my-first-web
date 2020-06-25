package com.yh.web.dao;

import com.yh.web.dto.BoardList;

import java.util.List;
import java.util.Map;

public interface BoardDao {
    List<BoardList> selectBoardList(Map map);
}
