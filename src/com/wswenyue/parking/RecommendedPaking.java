package com.wswenyue.parking;

import com.wswenyue.constant.Constant;
import com.wswenyue.db.domain.Place;
import com.wswenyue.db.service.PlaceManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 车位推荐
 * Created by wswenyue on 2015/7/1.
 */
public class RecommendedPaking {

    public static Map<String,String> recommedMap = new HashMap<>();

    public static Place recommended(String unume) {
        //从数据库获取车位信息
        List<Place> places = PlaceManage.GetPlacesOfLevel();
        if(places!=null){
            //从车位信息中选择合适的车位
            //把选择好的车位信息反馈给用户
            Place place = places.get(0);
            String placeNum = place.getPlaceNum();
            System.out.println("推荐车位"+placeNum);
            if (!placeNum.equals(Constant.PARKING_OCCUPANCY_1) && !placeNum.equals(Constant.PARKING_OCCUPANCY_2)
                    && !placeNum.equals(Constant.PARKING_OCCUPANCY_3) && !placeNum.equals(Constant.PARKING_OCCUPANCY_4)){
                PlaceManage.ChangePlaceStatusToYes(place.getPlaceNum());
                System.out.println("推荐车位非传感器车位=======位号："+place.getPlaceNum());
                recommedMap.put(unume,placeNum);
            }
            return place;
        }else {
            return null;
        }
    }

}
