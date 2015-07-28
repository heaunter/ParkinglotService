package com.wswenyue.db.test;

import com.wswenyue.utils.DateCompute;
import org.junit.Test;

/**
 * Created by wswenyue on 2015/6/7.
 */
public class DateComputeTest {

    @Test
    public void test(){
        Integer s = DateCompute.Duration(" ");
        System.out.println(s);
    }
    @Test
    public void test1(){
        String s = DateCompute.GetDate();
        System.out.println(s);
    }
}
