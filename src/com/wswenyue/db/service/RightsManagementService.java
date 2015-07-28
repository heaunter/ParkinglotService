package com.wswenyue.db.service;

import com.wswenyue.db.domain.User;

/**
 * Created by wswenyue on 2015/6/6.
 * 权限管理
 */
public class RightsManagementService {

    /**
     * 检查手机用户是否有进入的权限
     */
    public static boolean CheckEnterRight(User user) {

        if(BasicUserService.CheckAlreadyLogin(user)
                && BasicUserService.CheckBalance(user)){
            return true;
        }
        return false;
    }


    /**检查是否有打开2、3号门的权限
     * 1、判断用户是否已登录
     * 2、获取uid
     * 3、拿到status，看是否已经进入
     * 进入则有权限打开2、3号门
     * 否则没有
     */
    public static boolean CheckOpen(User user) {
        if (BasicUserService.CheckAlreadyLogin(user)) {
            Integer uid = BasicUserService.GetUid(user);
            if (uid != null) {
                Integer status = BasicAccountService.AccountStatus(uid);
                if (status == 1) {
                    return true;
                }
            }
        }
        return false;
    }


}
