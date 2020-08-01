package com.yh.web.service;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface GalleryService {
    int listNum = 40;  //리스트 갯수 40개씩
    String SE = File.separator;

    List<Map<String,String>> getGalleryList(long page);

    long getGalleryListCount();

    boolean addGallery(Map<String, Object> model);

    Map<String, Object> getGalleryDetail(long gno);

    boolean updateGallery(Map<String, Object> model);

    boolean deleteGallery(long gno);

    boolean updateGalleryPub(long gno, Integer pub);

    boolean updateGalleriesPub(String allNo, String openNo);

}
