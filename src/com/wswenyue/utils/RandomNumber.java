package com.wswenyue.utils;

import java.util.Random;

/**
 * Created by wswenyue on 2015/6/4.
 */
public class RandomNumber {
    private RandomNumber(){}
    private static final RandomNumber instance = new RandomNumber();
    public static RandomNumber getInstance(){
        return instance;
    }
    public String makeNumber(int digit){

        int temp;

        if(digit < 0){
            digit = 1;
        }
        StringBuffer res = new StringBuffer();
        Random rm = new Random();
        while (true){
            temp = rm.nextInt(10);
            if(temp!=0){
                res.append(temp);
                break;
            }
        }
        int i = 0;
        while (i < digit-1){
            res.append(rm.nextInt(10));
            i++;
        }
        return res.toString();
    }
}
