package com.yh.board;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        LocalDateTime dateTime1 = LocalDateTime.of(2016,9,2,9,30,50);
        LocalDate date2 = LocalDate.of(2016,9,1);

        System.out.println(ChronoUnit.DAYS.between(dateTime1.toLocalDate(),date2));



    }
}
