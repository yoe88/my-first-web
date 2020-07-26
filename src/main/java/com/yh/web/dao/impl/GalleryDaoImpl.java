package com.yh.web.dao.impl;

import com.yh.web.dao.GalleryDao;
import com.yh.web.dto.Gallery;
import com.yh.web.dto.GalleryFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class GalleryDaoImpl implements GalleryDao {

    private final String MAPPER = "mapper.gallery.";
    private final SqlSession sqlSession;

    public GalleryDaoImpl(SqlSession sqlSession) {
        log.info("GalleryDaoImpl Init...");
        this.sqlSession = sqlSession;
    }


    @Override
    public void insertGallery(Gallery gallery) {
        sqlSession.insert(MAPPER + "insertGallery", gallery);
    }

    @Override
    public long selectNextSequence() {
        return sqlSession.selectOne(MAPPER + "selectNextSequence");
    }

    @Override
    public void insertGalleryFile(GalleryFile galleryFile) {
        sqlSession.insert(MAPPER + "insertGalleryFile", galleryFile);
    }

    @Override
    public List<Map<String, String>> selectGalleryList(Map<String, Integer> map) {
        return sqlSession.selectList(MAPPER + "selectGalleryList", map);
    }

    @Override
    public int selectGalleryListCount() {
        return sqlSession.selectOne(MAPPER + "selectGalleryListCount");
    }

    @Override
    public Map<String, Object> selectGalleryDetail(int gno) {
        return sqlSession.selectOne(MAPPER + "selectGalleryDetail", gno);
    }

    @Override
    public void updateGallery(Map<String, Object> model) {
        sqlSession.update(MAPPER + "updateGallery", model);
    }

    @Override
    public void deleteGalleryFile(List<Integer> deleteNo) {
        sqlSession.delete(MAPPER + "deleteGalleryFile", deleteNo);
    }

    @Override
    public int deleteGallery(long gno) {
        return sqlSession.delete(MAPPER + "deleteGallery", gno);
    }

    @Override
    public int updateGalleryPub(Map<String, Object> map) {
        return sqlSession.update(MAPPER + "updateGalleryPub", map);
    }

    @Override
    public void updateGalleryOpenPub(List<String> openNo) {
        sqlSession.update(MAPPER + "updateGalleryOpenPub", openNo);
    }

    @Override
    public void updateGalleryClosePub(List<String> closeNo) {
        sqlSession.update(MAPPER + "updateGalleryClosePub", closeNo);
    }
}
