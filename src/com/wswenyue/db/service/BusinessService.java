package com.wswenyue.db.service;

import com.wswenyue.db.dao.impl.UserDaoImpl;
import com.wswenyue.db.domain.User;
import com.wswenyue.utils.DateCompute;

import java.sql.SQLException;

/**
 * 管理充值、扣费
 * Created by wswenyue on 2015/6/6.
 */
public class BusinessService {
    UserDaoImpl userDao = new UserDaoImpl();

    /**
     * 扣费
     * 查询并计算相应的金额，然后扣费
     *
     * @param user AccountType(账户类型：phone，card)
     * @return 成功返回true 失败返回false
     */
    public boolean Deductions(User user, String AccountType) {
        if (BasicUserService.FindUser(user)) {
            Integer originalMoney = BasicUserService.InquireBalance(user);//原始金额
            Integer duration = ComputeDuration(user);
            Integer money = ComputDeductions(duration, AccountType);
            if (originalMoney != null && originalMoney > 0 && money != null) {
                try {
                    int temp = originalMoney - money;
                    userDao.updateBalance(user.getPhone(), temp);
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 充值
     *
     * @param user money
     * @return 成功返回true 失败返回false
     */
    public boolean Recharge(User user, int money) {
        if (BasicUserService.FindUser(user)) {
            //原始金额
            Integer originalMoney = BasicUserService.InquireBalance(user);
            if (originalMoney != null) {
                try {
                    userDao.updateBalance(user.getPhone(), originalMoney + money);
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 充值
     *
     * @param uname money
     * @return 成功返回true 失败返回false
     */
    public boolean Recharge(String uname, int money) {
        User user = BasicUserService.GetUserByName(uname);
        if (user != null) {
            //原始金额
            Integer originalMoney = user.getBalance();
            if (originalMoney != null) {
                try {
                    userDao.updateBalance(user.getPhone(), originalMoney + money);
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 充值
     *
     * @param uname
     * @return 成功返回true 失败返回false
     */
    public boolean Recharge(String uname) {
        User user = BasicUserService.GetUserByName(uname);
        if (user != null) {
            try {
                userDao.updateBalance(user.getPhone(), 999);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 计算Account时长 按秒计算
     */
    public static Integer ComputeDuration(User user) {
        String startTime = BasicAccountService.GetAccountStartTime(user);
        Integer duration = DateCompute.Duration(startTime);
        return duration;
    }

    /**
     * 计算Card时长 按秒计算
     */
    public static Integer ComputeCardDuration(User user) {
        String startTime = BasicCardService.GetCardStartTime(user);
        Integer duration = DateCompute.Duration(startTime);
        return duration;
    }

    /**
     * 计算扣费
     */
    public static Integer ComputDeductions(Integer duration, String AccountType) {
        Integer price = PriceService.GetPrice(AccountType);
        if (duration != null && price != null) {
            return duration * price;
        }
        return null;
    }
}
