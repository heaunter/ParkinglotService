package com.wswenyue.Test;

import com.wswenyue.utils.RandomNumber;

/**
 * Created by wswenyue on 2015/6/4.
 */
public class RandomNumberTest {
    public static void main(String[] args) {
        for(int i=1;i<20;i++){
            System.out.println(RandomNumber.getInstance().makeNumber(i));
//            int tag = i;
//            if(i>9){
//                tag = 9;
//            }
//            if(i<1){
//                tag = 1;
//            }
//           int num = (int) (Math.pow(10,tag)-1);
//            System.out.println(num);
        }
    }
}
