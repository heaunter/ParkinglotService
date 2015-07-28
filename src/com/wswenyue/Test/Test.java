package com.wswenyue.Test;

import com.wswenyue.constant.Constant;
import com.wswenyue.db.domain.Account;
import com.wswenyue.db.domain.User;
import com.wswenyue.db.service.BasicAccountService;
import com.wswenyue.db.service.BasicUserService;
import com.wswenyue.protocol.MsgAnalysis;
import com.wswenyue.protocol.PhoneCMDRun;
import com.wswenyue.utils.DateCompute;

/**
 * Created by wswenyue on 2015/6/4.
 */
public class Test {

    @org.junit.Test
    public void test() {
        String res = DateCompute.FormatTime(200);
        System.out.println(res);
    }

    @org.junit.Test
    public void test1() {
        String res = MsgAnalysis.Analysis("REGISTER#succeed#succeed#succeed@163.com#123456");
        System.out.println(res);
    }

    @org.junit.Test
    public void test2() {
        String res = PhoneCMDRun.doworkForPhone("lisi", Constant.OPEN_IDENTIFER);
        System.out.println(res);
    }

    //    User user = BasicUserService.GetUserByName(uname);
//    Account account = BasicAccountService.GetAccountByuid(user.getUid());
//    String openWay = BasicAccountService.JudegEnter(account.getAstatus());
    @org.junit.Test
    public void test3() {
        User user = BasicUserService.GetUserByName("yaya");
//        System.out.println(user.toString());
        Account account = BasicAccountService.GetAccountByuid(user.getUid());
        System.out.println(account.toString());
    }
}
