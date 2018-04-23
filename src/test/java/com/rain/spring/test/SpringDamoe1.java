package com.rain.spring.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-demo.xml"})
public class SpringDamoe1 {

    @Autowired
    private SimpleTypeConverter simpleTypeConverter;

    @Test
    public void test1(){
        Integer s = simpleTypeConverter.convertIfNecessary(123, Integer.class);
        System.out.println(s);
    }
}
