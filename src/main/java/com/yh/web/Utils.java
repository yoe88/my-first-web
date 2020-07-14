package com.yh.web;

import java.util.Random;

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
}
