package com.yh.board;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;

public class Test {
    public static void main(String[] args) {
        final int SIZE = 10_000_000; //천만
        final List<Integer> arrayList = new ArrayList<>(SIZE);
        final Set<Integer> hashSet = new HashSet<>(SIZE);

        // List Set 각각에 추가
        IntStream.range(0, SIZE).forEach(value -> {
            arrayList.add(value);
            hashSet.add(value);
        });

        // List Set 각각에 찾으려는 숫자가 있는지 확인한다. 둘 다 true 일 것이다.
        final int target = 9_000_000;
        //final int target = 11;
        //assertTrue(arrayList.contains(target));
        //assertTrue(arrayList.contains(target));

        // ArrayList 에서 숫자를 찾는데 얼마나 시간이 걸렸는지 확인해보자.
        Instant start = Instant.now();
        arrayList.contains(target);
        Instant end = Instant.now();
        long elapsedTime = Duration.between(start, end).toNanos();
        System.out.println("array list search time : " + elapsedTime);

        // HashSet 에서 숫자를 찾는데 얼마나 시간이 걸렸는지 확인해보자.
        start = Instant.now();
        hashSet.contains(target);
        end = Instant.now();
        elapsedTime = Duration.between(start, end).toNanos();
        System.out.println("hash set search time : " + elapsedTime);
    }


}
