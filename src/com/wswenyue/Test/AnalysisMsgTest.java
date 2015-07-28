package com.wswenyue.Test;

import com.wswenyue.protocol.AnalysisMsgOfCard;

/**
 * Created by wswenyue on 2015/6/4.
 */
public class AnalysisMsgTest {
    @org.junit.Test
    public void testDowork() {
        System.out.println("开始了");
        for (int i = 1; i < 4; i++) {
            System.out.println("i:"+i);
            String res = AnalysisMsgOfCard.dowork("MSG:gate1:" + i);
            System.out.println(res);
//            String res1 = AnalysisMsg.dowork("MSG:gate1:" + i);
//            System.out.println(res1);
        }
    }
}
