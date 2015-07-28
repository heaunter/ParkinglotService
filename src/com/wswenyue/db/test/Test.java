package com.wswenyue.db.test;

import com.wswenyue.db.dao.impl.PlaceDaoImpl;
import com.wswenyue.db.dao.impl.SHTDaoImpl;
import com.wswenyue.db.dao.impl.UserDaoImpl;
import com.wswenyue.db.domain.Place;
import com.wswenyue.db.domain.SHT;
import com.wswenyue.db.domain.User;
import com.wswenyue.db.service.CardManageService;
import com.wswenyue.db.service.PlaceManage;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by wswenyue on 2015/6/7.
 */
public class Test {

    @org.junit.Test
    public void test(){
        User user = new User();
        user.setUname("老罗");
        user.setPassword("12345");
        user.setEmail("lisi@sina.com");
        user.setPhone("18369905767");
        String sb = CardManageService.BrushCard(user);
        System.out.println(sb);
    }
    @org.junit.Test
    public void test1(){
        UserDaoImpl userDao = new UserDaoImpl();
        User user = null;
        try {
            user = userDao.findUserByName("lisi");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(user.toString());
    }
//    @org.junit.Test
//    public void test2(){
//        SHT sht = new SHT("1","37","20","2222222");
//        SHTDaoImpl shtDao = new SHTDaoImpl();
//        try {
//            shtDao.add(sht);
//            System.out.println("添加数据成功");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
    @org.junit.Test
    public void test3(){
        SHTDaoImpl shtDao = new SHTDaoImpl();
        try {
            SHT sht = shtDao.findRecent();
            System.out.println("最近的数据：" + sht.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error-------");
        }
    }
    @org.junit.Test
    public void test4(){
        PlaceDaoImpl placeDao = new PlaceDaoImpl();
        try {
            String p = placeDao.findAvailablePlac();
            System.out.println("可用的车位：" + p);
            System.out.println("+++++++++++");
            List<Place> list = placeDao.getAvailablePlaceByGrade("nearly");
            System.out.println("-------------------");
            for (Place place : list) {
                System.out.println("该等级可用的车位信息：");
                System.out.println(place.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error-------");
        }
    }
//    @org.junit.Test
//    public void test5(){
//        PlaceDaoImpl placeDao = new PlaceDaoImpl();
//        Place place = new Place(2,"2","YES","nearly",1);
//        Place place1 = new Place(3,"3","YES","nearly",3);
//        try {
//            placeDao.updateStatus(place);
//            placeDao.updateStatus(place1);
//            placeDao.updateStatus("1","YES");
//            placeDao.updateUsedCount(place1);
//            placeDao.updateUsedCount(place);
//            placeDao.updateUsedCount("4","4");
//            System.out.println("更改成功");
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("Error-------");
//        }
//    }
    @org.junit.Test
    public void test6(){
        PlaceDaoImpl placeDao = new PlaceDaoImpl();
        try {
            Integer placenum = placeDao.getUsedCount("2");
            System.out.println("使用次数："+placenum);
            PlaceManage.UpdatePlaceUsedCount("2");
            System.out.println("更新之后的次数：" + placeDao.getUsedCount("2"));
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error-------");
        }
    }
}
