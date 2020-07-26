package com.yh.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

@Slf4j
public class Utils {
    public static String getRoot(){
        return "/web";
    }

    //출처 https://ktko.tistory.com/entry
    /**
     * @return 랜덤코드 6자리
     */
    public static String createRandomCode() {
        int certCharLength = 6;
        final char[] characterTable = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
                'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

        Random random = new Random(System.currentTimeMillis());
        int tablelength = characterTable.length;
        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < certCharLength; i++) {
            buf.append(characterTable[random.nextInt(tablelength)]);
        }
        return buf.toString();
    }

    /**  전달 받은 query String 추가하기*/
    public static String getPreQS(HttpServletRequest request) {
        String referer = request.getHeader("REFERER"); //쿼리스트링 값 넘겨주기
        if(referer == null) {
            return null;
        }

        String qs = "";
        int index = referer.lastIndexOf("?");
        if(index != -1){
            qs = referer.substring(referer.indexOf("?"));
        }
        return qs;
    }

    public static void redirectErrorPage(ModelAndView mav, String msg, String redirect ){
        mav.setViewName("/empty/commons/error/errorMessage");
        mav.addObject("msg",msg);
        mav.addObject("redirect",redirect);
    }
}
