package com.wswenyue.db.dao;

import com.wswenyue.db.domain.SHT;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by wswenyue on 2015/6/30.
 */
public interface SHTDao {

    void add(SHT sht) throws SQLException;
    void addDatas(List<SHT> shts) throws SQLException;

    SHT findRecent() throws SQLException;
}
