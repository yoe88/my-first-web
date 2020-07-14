package com.yh.web.service.impl;

import com.yh.web.dao.AdminDao;
import com.yh.web.dto.board.BoardDetail;
import com.yh.web.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    private final AdminDao adminDao;

    public AdminServiceImpl(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    @Override
    public Map<String,Object> getMembers(String field, String query, int page) {
        if(field.equals("id"))
            field = "M.ID";
        Map<String, Object> map = new HashMap<>();
        map.put("field", field);
        map.put("query", query);
        int start = 1 + (page-1) * listNum;
        int end = page * listNum;
        map.put("start",start);
        map.put("end",end);

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("list",adminDao.selectMembers(map));
        resultMap.put("count",adminDao.selectMembersCount(map));

        return resultMap;
    }

    @Override
    public Map<String, Object> getMember(String id){
        return adminDao.selectMemberById(id);
    }

    @Override
    public int updateMemberRole(String id, String role) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        if(role.equals("admin")) role = "ROLE_ADMIN";
        else if(role.equals("user")) role = "ROLE_USER";
        map.put("role", role);

        return adminDao.updateMemberRole(map);
    }

    @Override
    public int updateMemberEnable(String id, String enable) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("enable", enable);

        return adminDao.updateMemberEnable(map);
    }

    @Override
    public Map<String, Object> getBoardList(String field, String query, int page) {
        Map<String,Object> map = new HashMap<>();
        map.put("field",field);
        map.put("query",query);
        int start = 1 + (page-1) * listNum;
        int end = page * listNum;
        map.put("start",start);
        map.put("end",end);

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("list",adminDao.selectBoardList(map));
        resultMap.put("count",adminDao.selectBoardListCount(map));

        return resultMap;
    }

    @Override
    public BoardDetail getBoardDetail(int articleNo) {
        return adminDao.selectBoardDetailByArticleNo(articleNo);
    }

    @Override
    public int updateBoardPub(int articleNo, int pub) {
        Map<String, Integer> map = new HashMap<>();
        map.put("articleNo", articleNo);
        map.put("pub", pub);

        return adminDao.updateBoardPub(map);
    }
}
