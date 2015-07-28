package com.wswenyue.db.dao.impl;

import com.wswenyue.db.dao.SHTDao;
import com.wswenyue.db.domain.SHT;
import com.wswenyue.db.utils.JdbcUtils;
import com.wswenyue.utils.DateCompute;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by wswenyue on 2015/6/30.
 */
public class SHTDaoImpl implements SHTDao {
    @Override
    public void add(SHT sht) throws SQLException {
        if(sht.getTime()==null || sht.getTime().equals("")){
            sht.setTime(DateCompute.GetDate());
        }
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        String sql = "insert into sht(S,H,T,time) values(?,?,?,?)";
        Object params[] = {sht.getS(),sht.getH(),sht.getT(),sht.getTime()};
        qr.update(sql, params);
    }

    @Override
    public void addDatas(List<SHT> shts) throws SQLException {
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        String sql = "insert into sht(S,H,T,time) values(?,?,?,?)";
        Object params[][] = new Object[shts.size()][];
        for (int i = 0; i < shts.size(); i++) {
            params[i] = new Object[]{shts.get(i).getS(),shts.get(i).getH(),shts.get(i).getT(),shts.get(i).getTime()};
        }
        qr.batch(sql, params);
    }

    @Override
    public SHT findRecent() throws SQLException {
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        String sql = "select * from sht where eid=(select max(eid) from sht)";
        return (SHT) qr.query(sql,new BeanHandler(SHT.class));
    }

}
