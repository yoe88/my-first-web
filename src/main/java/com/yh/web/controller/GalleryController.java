package com.yh.web.controller;

import com.yh.web.Utils;
import com.yh.web.dto.Gallery;
import com.yh.web.service.GalleryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@PreAuthorize("hasAnyRole('ROLE_USER')")
@RequestMapping("/galleries")
@Controller
public class GalleryController {

    private final GalleryService galleryService;

    public GalleryController(GalleryService galleryService) {
        this.galleryService = galleryService;
    }


    /**
     * @param p_ 페이지
     * @return   갤러리 인덱스
     */
    @PreAuthorize("permitAll()")
    @GetMapping(path = "")
    public ModelAndView index(@RequestParam(name = "p",required = false,defaultValue = "1") String p_) throws UnsupportedEncodingException {
        ModelAndView mav = new ModelAndView("/gallery/index");
        mav.addObject("page_title", "갤러리");

        int page;
        try{  //page 문자열이 숫자로 변환이 안되거나 음수일 경우 1로 초기화
            page = Integer.parseInt(p_);
            if(page < 1) page = 1;
        } catch (NumberFormatException e){
            page = 1;
        }

        Map<String, Object> resultMap =  galleryService.getGalleryList(page);
        List<Map<String, String>> list = (List<Map<String, String>>) resultMap.get("list");
        int listTotalCount = (int) resultMap.get("count");

        int pageMaxNum =  (int) Math.ceil((listTotalCount/(double)galleryService.listNum)); 	//82개일경우 3
        pageMaxNum = (pageMaxNum ==0) ? 1 : pageMaxNum;

        mav.addObject("p",page);
        mav.addObject("list",list);
        mav.addObject("listTotalCount",listTotalCount);
        mav.addObject("pageMaxNum",pageMaxNum);
        return mav;
    }

    /**
     * @return 업로드 페이지
     */
    @GetMapping(path = "/new")
    public ModelAndView newGallery(){
        ModelAndView mav = new ModelAndView("/gallery/form");
        mav.addObject("page_title", "업로드");
        mav.addObject("action","new");
        return mav;
    }


    /**
     * @param principal  사용자
     * @param title     제목
     * @param files     이미지 파일들
     * @return          성공시 인덱스 페이지
     */
    @PostMapping(path = "/new")
    public ModelAndView newGallery(Principal principal, HttpServletRequest request
            , @RequestParam("title") String title
            , @RequestParam("file") List<MultipartFile> files) {
        ModelAndView mav = new ModelAndView();

        String id = principal.getName();
        String ip = request.getRemoteAddr();

        Gallery gallery = new Gallery();
        gallery.setTitle(title);
        gallery.setWriter(id);
        gallery.setIp(ip);

        Map<String, Object> model = new HashMap<>();
        model.put("gallery",gallery);
        model.put("files",files);

        boolean result = galleryService.addGallery(model);
        if(result){
            mav.setViewName("redirect: "+ Utils.getRoot() + "/galleries");
        }
        else{
            Utils.redirectErrorPage(mav, "업로드를 실패하였습니다.\\n다시 시도해주세요.", "/galleries");
        }
        return mav;
    }

    /**
     * @param gno_  갤러리 번호
     * @return      상세 페이지
     */
    @GetMapping(path = "/{gno}")
    public ModelAndView detailGallery(@PathVariable("gno") String gno_
                                    ,HttpServletRequest request) throws UnsupportedEncodingException {
        ModelAndView mav = new ModelAndView();
        int gno;

        try {
            gno = Integer.parseInt(gno_);
        }catch (NumberFormatException e){
            Utils.redirectErrorPage(mav, "올바른 접근이 아닙니다.", "/galleries");
            return mav;
        }

        Map<String, Object> model = galleryService.getGalleryDetail(gno);
        if(model == null){
            Utils.redirectErrorPage(mav, "올바른 접근이 아닙니다.", "/galleries");
            return mav;
        }else{
            mav.addObject("page_title", model.get("title"));
            mav.setViewName("/gallery/detail");
            mav.addObject("model",model);
            mav.addObject("qs", Utils.getPreQS(request));
        }
        return mav;
    }

    /**
     * @param gno_       갤러리 번호
     * @param principal  유저
     * @return           갤러리 수정 페이지
     */
    @GetMapping(path = "/{gno}/edit")
    public ModelAndView editGalleryForm(@PathVariable("gno") String gno_
                                        ,Principal principal) throws UnsupportedEncodingException {
        ModelAndView mav = new ModelAndView();
        int gno;

        try { //숫자로 변환이 안되는 번호
            gno = Integer.parseInt(gno_);
        }catch (NumberFormatException e){
            Utils.redirectErrorPage(mav, "올바른 접근이 아닙니다.", "/galleries");
            return mav;
        }

        Map<String, Object> galleryDetail = galleryService.getGalleryDetail(gno);
        if(galleryDetail == null){  // 존재하지 않는 경우
            Utils.redirectErrorPage(mav, "올바른 접근이 아닙니다.", "/galleries");
        }else{
            //글을 올린 아이디와 수정 하려는 사람의 아이디가 일치하는 경우에만
            if (galleryDetail.get("writer").equals(principal.getName())){
                mav.addObject("page_title", "갤러리 수정");
                mav.setViewName("/gallery/form");
                mav.addObject("model",galleryDetail);
                mav.addObject("action",gno + "/edit");
            }else{
                Utils.redirectErrorPage(mav, "올바른 접근이 아닙니다.", "/galleries");
            }
        }
        return mav;
    }


    /**
     * @param gno               갤러리 번호
     * @param title             제목
     * @param deleteNo          삭제할 파일 번호
     * @param deleteFileName    삭제할 파일 이름
     * @param files             새로 들어온 파일
     * @return                  성공시 글번호에 해당하는 상세 페이지 실패시 리스트 페이지
     */
    @PostMapping(path = "/{gno}/edit")
    public ModelAndView editGalleryForm(@PathVariable("gno") long gno
                                        ,@RequestParam("title") String title
                                        ,@RequestParam(value = "deleteNo" ,required = false) List<Integer> deleteNo
                                        ,@RequestParam(value = "deleteFileName", required = false) List<String> deleteFileName
                                        ,@RequestParam(value = "file", required = false) List<MultipartFile> files){

        Map<String, Object> model = new HashMap<>();
        model.put("gno",gno);
        model.put("title",title);
        model.put("deleteNo",deleteNo);
        model.put("deleteFileName",deleteFileName);
        model.put("files",files);

        ModelAndView mav = new ModelAndView();
        boolean result =  galleryService.updateGallery(model);
        if(result){
            mav.setViewName("redirect: "+ Utils.getRoot() + "/galleries/" + gno);
        }else{
            Utils.redirectErrorPage(mav, "수정을 실패하였습니다.\\n다시 시도해주세요.", "/galleries");
        }

        return mav;
    }

    /**
     * @param gno  갤러리 번호
     * @return
     */
    @DeleteMapping(path = "{gno}")
    public ModelAndView deleteGallery(@PathVariable("gno") long gno){

        boolean result = galleryService.deleteGallery(gno);
        ModelAndView mav = new ModelAndView();
        if(result){
            mav.setViewName("redirect: "+ Utils.getRoot() + "/galleries");
        }else{
            Utils.redirectErrorPage(mav, "삭제를 실패하였습니다.\\n다시 시도해주세요.", "/galleries/" + gno);
        }
        return mav;
    }

    /**  갤러리 pub 수정하는곳
     * @param gno    갤러리 번호
     * @param param  pub가 담겨 있는 param 
     * @return  성공시 true
     */
    @PutMapping(path = "{gno}/edit/pub")
    public ResponseEntity<Boolean> updateGalleryPub(@PathVariable("gno") long gno
                                                    ,@RequestBody Map<String,Integer> param){
        boolean result = galleryService.updateGalleryPub(gno, param.get("pub"));
        return ResponseEntity.ok(result);
    }
}
