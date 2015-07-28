package com.wswenyue.db.dao.impl;

import com.wswenyue.db.dao.PlaceDao;
import com.wswenyue.db.domain.Place;
import com.wswenyue.db.utils.JdbcUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by wswenyue on 2015/7/1.
 */
public class PlaceDaoImpl implements PlaceDao {
    //得到相应等级可用停车位
    @Override
    public List<Place> getAvailablePlaceByGrade(String grade) throws SQLException {
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        String sql = "select * from place where grade=? and isUsing='NO' ";
        return (List<Place>) qr.query(sql,grade,new BeanListHandler(Place.class));
    }

    @Override
    public String findAvailablePlac() throws SQLException {
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        String sql = "select count(*) from place where isUsing='NO'";
        Object[] temp = (Object[]) qr.query(sql,new ArrayHandler());
        return  temp[0].toString();
    }


    @Override
    public void updateStatus(String placeNum, String isUsing) throws SQLException {
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        String sql = "update place set isUsing=? where placeNum=?";
        Object params[] = {isUsing,placeNum};
        qr.update(sql,params);
    }

    @Override
    public Integer getUsedCount(String placeNum) throws SQLException {
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        String sql = "select usedCount from place where placeNum=?";
        return (Integer) qr.query(sql, placeNum, new ScalarHandler(1));
    }


    @Override
    public void updateUsedCount(String placeNum,String usedCount) throws SQLException {
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        String sql = "update place set usedCount=? where placeNum=?";
        Object params[] = {usedCount,placeNum};
        qr.update(sql,params);
    }

}
