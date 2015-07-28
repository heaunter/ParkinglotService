package com.wswenyue.Test;

import com.wswenyue.db.domain.Place;
import com.wswenyue.parking.RecommendedPaking;

/**
 * Created by wswenyue on 2015/7/9.
 */
public class RecommendTest {
    @org.junit.Test
    public void Test(){
        Place place = RecommendedPaking.recommended("zwx");
        System.out.println(place.toString());
    }
}
