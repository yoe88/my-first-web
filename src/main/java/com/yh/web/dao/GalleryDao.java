package com.yh.web.dao;

import com.yh.web.dto.Gallery;
import com.yh.web.dto.GalleryFile;
import com.yh.web.dto.board.BoardDetail;
import com.yh.web.dto.board.BoardList;

import java.util.List;
import java.util.Map;

public interface GalleryDao {

    void insertGallery(Gallery gallery);

    long selectNextSequence();

    void insertGalleryFile(GalleryFile galleryFile);

    List<Map<String, String>> selectGalleryList(Map<String, Integer> map);

    int selectGalleryListCount();

    Map<String, Object> selectGalleryDetail(int gno);

    void updateGallery(Map<String, Object> gallery);

    void deleteGalleryFile(List<Integer> deleteNo);

    int deleteGallery(long gno);

    int updateGalleryPub(Map<String, Object> map);

    void updateGalleryOpenPub(List<String> openNo);

    void updateGalleryClosePub(List<String> closeNo);
}
