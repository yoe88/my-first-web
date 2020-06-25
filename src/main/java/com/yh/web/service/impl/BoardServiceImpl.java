package com.yh.web.service.impl;

import com.yh.web.dao.BoardDao;
import com.yh.web.dto.BoardList;
import com.yh.web.service.BoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BoardServiceImpl implements BoardService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    final BoardDao boardDao;

    public BoardServiceImpl(BoardDao boardDao) {
        logger.info("BoardServiceImpl Init...");
        this.boardDao = boardDao;
    }

    @Transactional(readOnly = true)
    @Override
    public List<BoardList> getBoardList(String field, String query, int page) {
        Map<String,Object> map = new HashMap<>();
        if(field.equals("writer"))
            field = "b.writer";
        map.put("field",field);
        map.put("query",query);
        int start = 1 + (page-1)*10;
        int end = page * 10;
        map.put("start",start);
        map.put("end",end);
        return boardDao.selectBoardList(map);
    }
}
