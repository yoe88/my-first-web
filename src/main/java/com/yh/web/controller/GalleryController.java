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
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/galleries")
@Controller
public class GalleryController {

    private final GalleryService galleryService;

    public GalleryController(GalleryService galleryService) {
        this.galleryService = galleryService;
    }

    /**
     * @param p_ 페이지
     * @return   갤러리 리스트
     */
    @PreAuthorize("permitAll()")
    @GetMapping(path = "")
    public ModelAndView galleryList(@RequestParam(name = "p",required = false,defaultValue = "1") String p_) {
        ModelAndView mav = new ModelAndView();

        long page;
        try{  //p_ 문자열이 숫자로 변환이 안되거나 1보다 작을 경우 1로 초기화
            page = Long.parseLong(p_);
            if(page < 1) page = 1;
        } catch (NumberFormatException e){
            page = 1;
        }

        long listTotalCount = galleryService.getGalleryListCount();                             //갤러리 총 개수
        long pageMaxNum =  (long) Math.ceil((listTotalCount/(double)galleryService.listNum)); 	//82개일경우 3
        pageMaxNum = (pageMaxNum == 0) ? 1 : pageMaxNum;
        if(page > pageMaxNum){											        //param p가 페이지 끝 번호보다 큰경우
            Utils.redirectErrorPage(mav,"존재하지 않는 페이지입니다.\\n확인 후 다시 시도하시기 바랍니다.","/galleries");
            return mav;
        }

        List<Map<String, String>> list = galleryService.getGalleryList(page);

        mav.setViewName("/gallery/index");
        mav.addObject("page_title", "갤러리");
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
     * @param principal 사용자
     * @param title     제목
     * @param files     이미지 파일들
     * @return          성공시 리스트 페이지
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
            mav.setViewName("redirect:/galleries");
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
                                    ,HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        long gno;

        try {
            gno = Long.parseLong(gno_);
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
                                        ,Principal principal) {
        ModelAndView mav = new ModelAndView();
        long gno;

        try { //숫자로 변환이 안되는 번호
            gno = Long.parseLong(gno_);
        }catch (NumberFormatException e){
            Utils.redirectErrorPage(mav, "올바른 접근이 아닙니다.", "/galleries");
            return mav;
        }

        Map<String, Object> galleryDetail = galleryService.getGalleryDetail(gno);
        //존재 하지 않거나 작성자와 로그인한 아이디가 불일치 하는 경우
        if(galleryDetail == null || !galleryDetail.get("writer").equals(principal.getName()) ){
            Utils.redirectErrorPage(mav, "올바른 접근이 아닙니다.", "/galleries");
        }else{
            mav.setViewName("/gallery/form");
            mav.addObject("page_title", "갤러리 수정");
            mav.addObject("model",galleryDetail);
            mav.addObject("action",gno + "/edit");
        }
        return mav;
    }


    /**
     * @param gno               갤러리 번호
     * @param title             제목
     * @param deleteNo          삭제할 파일 번호
     * @param deleteFileName    삭제할 파일 이름
     * @param files             추가 할 파일
     * @return                  성공시 번호에 해당하는 상세 페이지 실패시 리스트 페이지
     */
    @PostMapping(path = "/{gno}/edit")
    public ModelAndView modifyGallery(@PathVariable("gno") long gno
                                        ,@RequestParam("title") String title
                                        ,@RequestParam(value = "deleteNo" ,required = false) List<Long> deleteNo
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
            mav.setViewName("redirect:/galleries/" + gno);
        }else{
            Utils.redirectErrorPage(mav, "수정을 실패하였습니다.\\n다시 시도해주세요.", "/galleries");
        }

        return mav;
    }

    /**
     * @param gno  갤러리 번호
     * @return     삭제 성공시 리스트 페이지
     */
    @DeleteMapping(path = "{gno}")
    public ModelAndView deleteGallery(@PathVariable("gno") long gno){

        boolean result = galleryService.deleteGallery(gno);
        ModelAndView mav = new ModelAndView();
        if(result){
            mav.setViewName("redirect:/galleries");
        }else{
            Utils.redirectErrorPage(mav, "삭제를 실패하였습니다.\\n다시 시도해주세요.", "/galleries/" + gno);
        }
        return mav;
    }

    /**  갤러리 pub 수정하는곳
     * @param gno    갤러리 번호
     * @param param  pub 담겨 있는 param
     * @return  성공시 true
     */
    @PutMapping(path = "{gno}/edit/pub")
    public ResponseEntity<Boolean> updateGalleryPub(@PathVariable("gno") long gno
                                                    ,@RequestBody Map<String,Integer> param){
        boolean result = galleryService.updateGalleryPub(gno, param.get("pub"));
        return ResponseEntity.ok(result);
    }
}
