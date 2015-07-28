package com.wswenyue.db.service;

import com.wswenyue.db.dao.impl.SHTDaoImpl;
import com.wswenyue.db.domain.SHT;
import com.wswenyue.utils.DateCompute;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by wswenyue on 2015/7/2.
 */
public class SHTManage {
    static SHTDaoImpl shtDao = new SHTDaoImpl();

    public static SHT GetRecentSHT(){
        SHT sht = null;
        try {
             sht = shtDao.findRecent();
            if (sht == null) {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sht;
    }

    public static Boolean Add(SHT sht){
        try {
            shtDao.add(sht);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static Boolean AddData(List<SHT> shts){
        if(shts!=null && shts.size()!=0){
            SHT newSHT = new SHT();
            int remainder = shts.size()%3;
            int num = shts.size()/3;
            int index;
            for (int i = 1; i <= num; i++) {
                index = i*3-1;
                String h = Average(shts.get(index).getH(),shts.get(index-1).getH(),shts.get(index-2).getH());
                String t = Average(shts.get(index).getT(),shts.get(index-1).getT(),shts.get(index-2).getT());
                String s = JudgeSmoke(shts.get(index).getS(),shts.get(index-1).getS(),shts.get(index-2).getS());
                newSHT.setH(h);
                newSHT.setT(t);
                newSHT.setS(s);
                newSHT.setTime(DateCompute.GetDate());
                Add(newSHT);
            }
            if(remainder == 1){
                newSHT.setH(shts.get(num*3).getH());
                newSHT.setT(shts.get(num*3).getH());
                newSHT.setS(shts.get(num*3).getH());
                newSHT.setTime(DateCompute.GetDate());
                Add(newSHT);
            }
            if(remainder == 2){
                String h = Average(shts.get(num*3).getH(),shts.get(num*3+1).getH());
                String t = Average(shts.get(num*3).getT(),shts.get(num*3+1).getT());
                String s = JudgeSmoke(shts.get(num*3).getS(),shts.get(num*3+1).getS());
                newSHT.setH(h);
                newSHT.setT(t);
                newSHT.setS(s);
                newSHT.setTime(DateCompute.GetDate());
                Add(newSHT);
            }
            return true;
        }
        return false;
    }
    public static boolean AddDatas(List<SHT> shts){
        if(shts!=null && shts.size()!=0) {
            try {
                shtDao.addDatas(shts);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public static String Average(String str1,String str2,String str3){
        int average =( Integer.parseInt(str1)+Integer.parseInt(str2)+Integer.parseInt(str3) )/3;
        return String.valueOf(average);
    }
    public static String Average(String str1,String str2){
        int average =( Integer.parseInt(str1)+Integer.parseInt(str2) )/2;
        return String.valueOf(average);
    }
    public static String JudgeSmoke(String str1,String str2,String str3) {
        if (str1.equals("1") || str2.equals("1") || str3.equals("1")) {
            return "1";
        } else {
            return "0";
        }
    }
    public static String JudgeSmoke(String str1,String str2) {
        if (str1.equals("1") || str2.equals("1") ) {
            return "1";
        } else {
            return "0";
        }
    }
}
