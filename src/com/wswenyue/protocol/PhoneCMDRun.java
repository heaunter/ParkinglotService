package com.wswenyue.protocol;

import com.wswenyue.constant.Constant;
import com.wswenyue.db.domain.Account;
import com.wswenyue.db.domain.User;
import com.wswenyue.db.service.BasicAccountService;
import com.wswenyue.db.service.BasicUserService;
import com.wswenyue.db.service.BusinessService;
import com.wswenyue.db.service.PlaceManage;
import com.wswenyue.parking.RecommendedPaking;
import com.wswenyue.utils.DateCompute;

/**
 * Created by wswenyue on 2015/6/29.
 */
public class PhoneCMDRun {

    public static String doworkForPhone(String uname, String cmd) {

        StringBuffer sb = new StringBuffer();
        String result = "";
        //得到user
        User user = BasicUserService.GetUserByName(uname);
        Account account = BasicAccountService.GetAccountByuid(user.getUid());
        String openWay = BasicAccountService.JudegEnter(account.getAstatus());
        if (account == null || openWay.equals("")) {
            //说明此用户不存在
            result = Constant.Authority_Permission_denied;
        } else {
            if (cmd.equals(Constant.OPEN_IDENTIFER)) {
                if (user.getUserflag() == 1) {

                    if (openWay.equals("IN")) {
                        if (MonitoringDataProcessing.Switch_Is_Effective) {
                            if(!MonitoringDataProcessing.Switch_Is_Opened){
                                System.out.println("手机用户进入");
                                //查数据库获取余额，卡名
                                if (user.getBalance() != null && user.getBalance() > 0) {
                                    sb.append(account.getAname()).append("#01#0#")
                                            .append(user.getBalance()).append("#0#00:00:00#")
                                            .append(RecommendedPaking.recommended(uname).getPlaceNum()).append("#")
                                            .append(PlaceManage.GetPlacesNum()).append("#");

                                    result = sb.toString();
                                    System.out.println(sb.toString());

                                    //将开门次数加1
                                    BasicAccountService.PutAccountAstatus(user.getUid(), account.getAstatus() + 1);
                                    BasicAccountService.PutAccountStartTime(user.getUid());
                                    MonitoringDataProcessing.Switch_Is_Opened = true;
                                }
                            }else {
                                System.out.println("闸门已打开，请尽快通过！！！");
                                result = Constant.Server_CMD_Repuat;
                            }
                        } else {
                            System.out.println("请进入闸门区域后再操作！！！");
                            result = Constant.Authority_Area_Outside;
                        }
                    }
                    if (openWay.equals("OUT")) {
                        if (MonitoringDataProcessing.Switch_Is_Effective) {
                            if(!MonitoringDataProcessing.Switch_Is_Opened){
                                System.out.println("手机用户离开");
                                //计算时长
                                Integer s = BusinessService.ComputeDuration(user);
                                //计算扣费
                                Integer money = BusinessService.ComputDeductions(s, Constant.PHONE_TYPE);
                                System.out.println("扣费：" + money);

                                //获取余额
                                int balance = user.getBalance() - money;

                                sb.append(account.getAname()).append("#01#").append(money).append("#")
                                        .append(balance).append("#1#")
                                        .append(DateCompute.FormatTime(s)).append("# # #");
                                //将开门次数加1
                                BasicAccountService.PutAccountAstatus(user.getUid(), account.getAstatus() + 1);

                                result = sb.toString();
                                System.out.println(sb.toString());

                                //更新余额
                                BasicUserService.UpdateBalance(user.getPhone(), balance);
                                //设置时间为空
                                BasicAccountService.SetAccountStartTime(user.getUid());
                                MonitoringDataProcessing.Switch_Is_Opened = true;
                                //把车位置为可用
                                String rplaceNum = RecommendedPaking.recommedMap.get(uname);
                                if(rplaceNum != null && !rplaceNum.equals("")){
                                    PlaceManage.ChangePlaceStatusToNo(rplaceNum);
                                    RecommendedPaking.recommedMap.remove(uname);
                                }

                            }else {
                                System.out.println("闸门已打开，请尽快通过！！！");
                                result = Constant.Server_CMD_Repuat;
                            }
                        } else {
                            System.out.println("请进入闸门区域后再操作！！！");
                            result = Constant.Authority_Area_Outside;
                        }

                    }
                } else {
                    //没有登录
                    result = Constant.Authority_Permission_denied;
                }

            } else if (cmd.equalsIgnoreCase(Constant.OPEN02_IDENTIFER)) {
                if (user.getUserflag() == 1) {
                    if (openWay.equals("OUT")) {
                        //打开2号门
                        System.out.println("二号门被打开");
                        sb.append(account.getAname()).append("#02#0#")
                                .append(user.getBalance()).append("#0#00:00:00#");
                        result = sb.toString();
                    } else {
                        //1号门未打开
                        result = Constant.Authority_Not_Allowed;
                    }
                } else {
                    //没有登录
                    result = Constant.Authority_Permission_denied;
                }

            } else if (cmd.equalsIgnoreCase(Constant.OPEN03_IDENTIFER)) {
                if (user.getUserflag() == 1) {
                    if (openWay.equals("OUT")) {
                        //打开3号门
                        System.out.println("三号门被打开");
                        sb.append(account.getAname()).append("#03#0#")
                                .append(user.getBalance()).append("#0#00:00:00#");
                        result = sb.toString();
                    } else {
                        //1号门未打开
                        result = Constant.Authority_Not_Allowed;
                        System.out.println("打开3Error：1号门未打开");
                    }
                } else {
                    //没有登录
                    result = Constant.Authority_Permission_denied;
                    System.out.println("打开3Error没有登录");
                }
            }
        }

        return result;
    }
}
