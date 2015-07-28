package com.wswenyue.protocol;

import com.wswenyue.db.domain.User;
import com.wswenyue.db.service.CardManageService;

/**
 * Created by wswenyue on 2015/6/4.
 */
public class AnalysisMsgOfCard {
    static User user = new User();

    public static String dowork(String msg){

        String result = "";

        if(msg.equalsIgnoreCase("MSG:gate1:1")){
            user.setUname("zhangsan");
            user.setPhone("18369906167");
            result = CardManageService.BrushCard(user);
        }
        if(msg.equalsIgnoreCase("MSG:gate1:2")){
            user.setUname("lisi");
            user.setPhone("18369935767");
            result = CardManageService.BrushCard(user);
        }
        if(msg.equalsIgnoreCase("MSG:gate1:3")){
            user.setUname("wangwu");
            user.setPhone("18369406787");
            result = CardManageService.BrushCard(user);
        }

        return result;
    }






}
