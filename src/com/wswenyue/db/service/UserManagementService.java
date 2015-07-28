package com.wswenyue.db.service;

import com.wswenyue.db.dao.impl.AccountDaoImpl;
import com.wswenyue.db.dao.impl.UserDaoImpl;
import com.wswenyue.db.domain.Account;
import com.wswenyue.db.domain.User;
import com.wswenyue.utils.RandomNumber;

import java.sql.SQLException;

/**
 * 用户登录、注册、修改密码
 * Created by wswenyue on 2015/6/6.
 */
public class UserManagementService {
    static UserDaoImpl userDao = new UserDaoImpl();

    /**
     * 登录管理
     * 需要修改用户登录状态 ：userflag
     * 0：表示未登录，1：表示已登录，2表示已注销
     *
     * */


    /**
     * 登录
     *
     * @param user
     * @return 登录成功返回true，失败返回false
     */
    public static boolean Login(User user) {
        if (BasicUserService.FindUser(user)) {
            try {
                userDao.updateUserflag(user.getPhone(), 1);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 登录
     *
     * @param uname
     * @param passwd
     * @return 登录成功返回true，失败返回false
     */
    public static boolean Login(String uname, String passwd) {
        // 密码验证 account验证
        User user = BasicUserService.GetUserByName(uname);
        Account account =BasicAccountService.GetAccountByuid(user.getUid());
        if (user != null && account != null && passwd.equals(user.getPassword())) {
            try {
                userDao.updateUserflag(user.getPhone(), 1);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 登出
     *
     * @param user
     * @return 登出成功返回true，失败返回false
     */
    public static boolean Logout(User user) {
        if (BasicUserService.FindUser(user)) {
            try {
                userDao.updateUserflag(user.getPhone(), 2);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * 注册管理
     * 规定的是手机号唯一 ，也就说如果手机号不一样，就是新用户就予以注册
     * 注册成功则绑定账户，账户名为3位随机值
     *
     * @param user
     * @return 注册成功返回true ，失败返回false
     */
    public static boolean Regiester(User user) {
        if (!BasicUserService.FindUser(user)) {
            UserDaoImpl ud = new UserDaoImpl();
            AccountDaoImpl ad = new AccountDaoImpl();
            Account account = new Account();
            try {
                //添加用户
                ud.add(user);
                Integer uid = ud.find(user.getPhone()).getUid();
                account.setAname("p" + RandomNumber.getInstance().makeNumber(3));
                account.setUid(uid);
                //绑定账户
                ad.add(account);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 修改密码
     *
     * @param user NewPasswd
     * @return 修改密码成功返回true ，失败返回false
     */
    public static boolean ChangePassword(User user, String NewPasswd) {
        if (BasicUserService.FindUser(user)) {
            try {
                userDao.updatePasswd(user.getPhone(), NewPasswd);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return false;
    }


}
