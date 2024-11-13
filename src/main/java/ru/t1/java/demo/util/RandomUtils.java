package ru.t1.java.demo.util;

public class RandomUtils {

    public static Long randomLong() {
        return Math.abs(Long.parseLong(String.valueOf(Math.random() * 1000000000)));
    }
}
