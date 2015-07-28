package com.wswenyue.db.test;

import com.wswenyue.constant.Constant;
import com.wswenyue.db.domain.User;
import com.wswenyue.db.service.BusinessService;
import org.junit.Test;

public class BusinessServiceTest {

    User user = new User();
    BusinessService businessService = new BusinessService();

    public void init() {
        user.setUname("张三");
        user.setPassword("1234");
        user.setEmail("zhangsan@163.com");
        user.setPhone("18369956765");

    }

    @Test
    public void test1() {
        init();
        boolean b =businessService.Deductions(user, Constant.PHONE_TYPE);
        System.out.println(b);
    }

    @Test
    public void test2() {
        init();
        boolean b = businessService.Recharge(user, 89898);
        System.out.println(b);
    }
    @Test
    public void test3() {
        init();
    }

}
