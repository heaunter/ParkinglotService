package com.wswenyue.db.test;

import com.wswenyue.db.dao.impl.AccountDaoImpl;
import com.wswenyue.db.domain.Account;
import com.wswenyue.db.service.BasicAccountService;
import org.junit.Test;

import java.sql.SQLException;

/**
 * Created by wswenyue on 2015/6/7.
 */
public class AccountTest {

    @Test
     public void test(){
        BasicAccountService.PutAccountStartTime(1);
    }
    @Test
    public void test1(){
        AccountDaoImpl accountDao = new AccountDaoImpl();
        try {
            Account account = accountDao.find(4);
            System.out.println(account.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("错误");
        }
    }
}
