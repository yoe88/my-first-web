package com.yh.web.controller.admin;

import com.yh.web.Utils;
import com.yh.web.service.AdminService;
import com.yh.web.service.GalleryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller("admin.galleryController")
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class GalleryController {

    private final AdminService adminService;
    private final GalleryService galleryService;
    public GalleryController(AdminService adminService, GalleryService galleryService) {
        this.adminService = adminService;
        this.galleryService = galleryService;
    }

    /**
     * @param p_ 페이지
     * @return   갤러리 인덱스
     */
    @GetMapping("/galleries")
    public ModelAndView index(@RequestParam(name = "p",required = false,defaultValue = "1") String p_) throws UnsupportedEncodingException {
        ModelAndView mav = new ModelAndView("/admin/gallery/galleryList");
        mav.addObject("page_title", "갤러리");

        int page;
        try{  //page 문자열이 숫자로 변환이 안되거나 음수일 경우 1로 초기화
            page = Integer.parseInt(p_);
            if(page < 1) page = 1;
        } catch (NumberFormatException e){
            page = 1;
        }

        Map<String, Object> resultMap =  adminService.getGalleryList(page);
        List<Map<String, String>> list = (List<Map<String, String>>) resultMap.get("list");
        long listTotalCount = (long) resultMap.get("count");

        int pageMaxNum =  (int) Math.ceil((listTotalCount/(double)AdminService.galleryListNum)); 	//82개일경우 3
        pageMaxNum = (pageMaxNum ==0) ? 1 : pageMaxNum;

        mav.addObject("p",page);
        mav.addObject("list",list);
        mav.addObject("listTotalCount",listTotalCount);
        mav.addObject("pageMaxNum",pageMaxNum);
        return mav;
    }

    /**         체크된 갤러리 번호는 공개처리 체크 안된건 비공개처리
     * allNo    현재 페이지 모든 갤러리 번호
     * openNo   체크된 갤러리 번호
     */
    @PutMapping("/galleries")
    public ResponseEntity<Boolean> updateGalleriesPub(@RequestBody Map<String,String> param){

        String allNo = param.get("allNo");     //모든 글 번호
        String openNo = param.get("openNo");   //체크된 글 번호

        boolean result = galleryService.updateGalleriesPub(allNo,openNo);
        return ResponseEntity.ok(result);
    }

    /**
     * @param gno_  갤러리 번호
     * @return      상세 페이지
     */
    @GetMapping(path = "/galleries/{gno}")
    public ModelAndView detailGallery(@PathVariable("gno") String gno_
            , HttpServletRequest request) throws UnsupportedEncodingException {
        ModelAndView mav = new ModelAndView();
        int gno;

        try {
            gno = Integer.parseInt(gno_);
        }catch (NumberFormatException e){
            Utils.redirectErrorPage(mav, "올바른 접근이 아닙니다.", "/galleries");
            return mav;
        }

        Map<String, Object> model = adminService.getGalleryDetail(gno);
        if(model == null){
            Utils.redirectErrorPage(mav, "올바른 접근이 아닙니다.", "/galleries");
            return mav;
        }else{
            mav.addObject("page_title", model.get("title"));
            mav.setViewName("/admin/gallery/detail");
            mav.addObject("model",model);
            mav.addObject("qs", Utils.getPreQS(request));
        }
        return mav;
    }
}

