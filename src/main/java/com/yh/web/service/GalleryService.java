package com.yh.web.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public interface GalleryService {
    int listNum = 40;  //게시글 개수 10개씩

    boolean addGallery(Map<String, Object> model);

    Map<String, Object> getGalleryList(int page) throws UnsupportedEncodingException;

    Map<String, Object> getGalleryDetail(int gno) throws UnsupportedEncodingException;

    boolean updateGallery(Map<String, Object> model);

    boolean deleteGallery(long gno);

    boolean updateGalleryPub(long gno, Integer pub);

    boolean updateGalleriesPub(String allNo, String openNo);
}
