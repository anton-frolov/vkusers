package com.example.vkusers.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class DateTimeUtilTest {


    @Test
    void fromVkString() {
        System.out.println(DateTimeUtil.fromVkString("02.05.2019"));
        System.out.println(DateTimeUtil.fromVkString("21.7.1994"));
        System.out.println(DateTimeUtil.fromVkString("2.12.2019"));
        System.out.println(DateTimeUtil.fromVkString("1.1.1901"));
        System.out.println(DateTimeUtil.fromVkString("9.4"));

    }
}