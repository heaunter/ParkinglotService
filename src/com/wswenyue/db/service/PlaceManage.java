package com.wswenyue.db.service;

import com.wswenyue.db.dao.impl.PlaceDaoImpl;
import com.wswenyue.db.domain.Place;

import java.sql.SQLException;
import java.util.List;

/**
 * 车位管理
 * Created by wswenyue on 2015/7/1.
 */
public class PlaceManage {

    static PlaceDaoImpl placeDao = new PlaceDaoImpl();


    /**推荐等级车位*/
    public static List<Place> GetPlacesOfLevel(){
        try {
            List<Place> nearly = placeDao.getAvailablePlaceByGrade("nearly");
            if(nearly!=null && nearly.size()!=0){
                return nearly;
            }else{
                List<Place> medium = placeDao.getAvailablePlaceByGrade("medium");
                if(medium!=null && medium.size()!=0) {
                    return medium;
                }else {
                    List<Place> far = placeDao.getAvailablePlaceByGrade("far");
                    if(far!=null && far.size()!=0) {
                        return far;
                    }else {
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**得到可用停车位数*/
    public static String GetPlacesNum(){
        String pNum = "";
        try {
            pNum = placeDao.findAvailablePlac();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pNum;
    }
    /** 把停车位的状态修改成已停车*/
    public static Boolean ChangePlaceStatusToYes(String placeNum){
        if(placeNum==null || placeNum.equals("")){
            return false;
        }
        try {
            placeDao.updateStatus(placeNum,"YES");
            //停车位使用次数加一
            UpdatePlaceUsedCount(placeNum);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    /** 把停车位的状态修改成无车、可用*/
    public static Boolean ChangePlaceStatusToNo(String placeNum){
        if(placeNum==null || placeNum.equals("")){
            return false;
        }
        try {
            placeDao.updateStatus(placeNum,"NO");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    /** 把停车位的使用次数加一*/
    public static Boolean UpdatePlaceUsedCount(String placeNum){
        if(placeNum==null || placeNum.equals("")){
            return false;
        }
        try {
            Integer count = placeDao.getUsedCount(placeNum);
            if(count!=null){
                Integer num = count + 1;
                placeDao.updateUsedCount(placeNum, String.valueOf(num));
                return true;
            }else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
