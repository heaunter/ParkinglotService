package com.wswenyue.db.service;

import com.wswenyue.db.dao.impl.AccountDaoImpl;
import com.wswenyue.db.domain.Account;
import com.wswenyue.db.domain.User;
import com.wswenyue.utils.DateCompute;
import com.wswenyue.utils.Judge;

import java.sql.SQLException;

/**
 * 判断该卡是否存在
 * 获取账户状态：0/1 0：表示出来，1表示进入 0为默认
 * 获取进来时间或开始时间
 * 添加当前时间
 * Created by wswenyue on 2015/6/6.
 */
public class BasicAccountService {
    static AccountDaoImpl accountDao = new AccountDaoImpl();

    /**
     * 判断该卡是否存在
     *
     * @param account
     */
    public static boolean FindAccount(Account account) {
        try {
            if (accountDao.find(account.getUid()) != null) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断该卡是否存在
     *
     * @param uid
     */
    public static boolean FindAccount(Integer uid) {
        try {
            if (accountDao.find(uid) != null) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 通过uid得到该卡
     *
     * @param uid
     * @return 查不到或查询出错返回 null
     */
    public static Account GetAccountByuid(Integer uid) {
        try {
            Account account = accountDao.find(uid);
            if (account != null) {
                return account;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过uname得到该卡
     *
     * @param uname
     * @return 查不到或查询出错返回 null
     */
    public static Account GetAccountByUname(String uname) {
        try {
            Integer uid =  BasicUserService.GetUidByUname(uname);
            Account account = accountDao.find(uid);
            if (account != null) {
                return account;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取账户状态：0/1 0：表示出来，1表示进入 0为默认
     *
     * @param account
     * @return 查询失败返回 NULL
     */
    public static Integer AccountStatus(Account account) {
        Integer status = null;
        if (FindAccount(account)) {
            try {
                status = accountDao.find(account.getUid()).getAstatus();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return status;
    }

    /**
     * 获取账户状态：门被打开的次数
     *
     * @param uid
     * @return 查询失败返回 NULL
     */
    public static Integer AccountStatus(Integer uid) {
        Integer status = null;
        if (FindAccount(uid)) {
            try {
                status = accountDao.find(uid).getAstatus();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return status;
    }

    /**
     * 获取账户状态：门被打开的次数
     * @param uname
     * @return 查询失败返回 NULL
     */
    public static Integer GetAccountStatus(String uname) {
        return GetAccountByUname(uname).getAstatus();
    }

    /**
     * 获取进来时间或开始时间
     *
     * @param account
     * @return 查询失败返回：空字符串
     */
    public static String GetAccountStartTime(Account account) {
        String startTime = "";
        if (FindAccount(account)) {
            try {
                startTime = accountDao.find(account.getUid()).getStartTime();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return startTime;
    }

    /**
     * 获取进来时间或开始时间
     *
     * @param user
     * @return 查询失败返回：空字符串
     */
    public static String GetAccountStartTime(User user) {
        Integer uid = BasicUserService.FindUser(user.getPhone());
        String startTime = "";
        if (uid != null && FindAccount(uid)) {
            try {
                startTime = accountDao.find(uid).getStartTime();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return startTime;
    }

    /**
     * 更新开始时间
     *
     * @param uid
     * @return 查询失败返回：空字符串
     */
    public static boolean PutAccountStartTime(Integer uid) {
        if (FindAccount(uid)) {
            try {
                String startTime = DateCompute.GetDate();
                if (!"".equals(startTime)) {
                    accountDao.updateStartTime(uid, startTime);
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 更新打开次数
     *
     * @param uid
     * @return 查询失败返回：空字符串
     */
    public static boolean PutAccountAstatus(Integer uid, Integer status) {
        if (FindAccount(uid)) {
            try {
                accountDao.updateAstatus(uid, status);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 判断Account用户进入或离开
     * 奇数返回true
     * 偶数返回false
     *
     * @param uname
     * @return 查询出错 返回""
     */
    public static String JudegEnter(String uname) {
        String tag = "";
        Account account = GetAccountByUname(uname);
        if (account != null) {
            if (account.getAstatus() != null) {
                if (Judge.isOdd(account.getAstatus())) {
                    tag = "IN";
                } else {
                    tag = "OUT";
                }
            }
        }
        return tag;
    }

    /**
     * 判断Account用户进入或离开
     * 奇数返回true
     * 偶数返回false
     *
     * @param astatus
     * @return 查询出错 返回""
     */
    public static String JudegEnter(Integer astatus) {
        String tag = "";
        if (astatus != null) {
            if (Judge.isOdd(astatus)) {
                tag = "IN";
            } else {
                tag = "OUT";
            }
        }
        return tag;
    }
    /**
     * 设置开始时间为空
     *
     * @param uid
     * @return 失败返回：false
     */
    public static boolean SetAccountStartTime(Integer uid) {
        if (FindAccount(uid)) {
            String startTime = "";
            try {
                accountDao.updateStartTime(uid, startTime);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}
