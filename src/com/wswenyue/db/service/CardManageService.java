package com.wswenyue.db.service;

import com.wswenyue.constant.Constant;
import com.wswenyue.db.domain.Card;
import com.wswenyue.db.domain.Place;
import com.wswenyue.db.domain.User;
import com.wswenyue.parking.RecommendedPaking;
import com.wswenyue.protocol.MonitoringDataProcessing;
import com.wswenyue.utils.DateCompute;
import com.wswenyue.utils.Judge;

/**
 * 卡用户的管理（卡就是管理员用户）
 * 开卡
 * 判断刷卡权限
 * <p>
 * Created by wswenyue on 2015/6/7.
 */
public class CardManageService {


    /**
     * 刷卡操作
     */
    public static String BrushCard(User user) {
        StringBuffer sb = new StringBuffer();
        String tag = JudegEnter(user);
        if (tag.equals("IN")) {
            if (MonitoringDataProcessing.Switch_Is_Effective) {
                if (!MonitoringDataProcessing.Switch_Is_Opened) {
                    Card card = BasicCardService.GetCard(user);
                    if (card != null) {
                        //取出cname和balance
                        User u = BasicUserService.GetUser(user.getPhone());
                        if (u.getBalance() != null && u.getBalance() > 0) {
                            if (     //插入startTime
                                    BasicCardService.PutCardStartTime(u.getUid())
                                            //count数加1
                                            && BasicCardService.SetCardCount(u.getUid())) {

                                //推荐车位
                                Place place = RecommendedPaking.recommended(user.getUname());

                                sb.append(card.getCname()).append("#0#")
                                        .append(u.getBalance()).append("#0#00:00:00#")
                                        .append(place.getPlaceNum()).append("#")
                                        .append(PlaceManage.GetPlacesNum()).append("#");
                                MonitoringDataProcessing.Switch_Is_Opened = true;

                            } else {
                                System.out.println("数据库连接异常");
                            }

                        } else {
                            System.out.println(u.getUname() + "您的余额不足，请充值。");
                        }

                    } else {
                        System.out.println("对不起，您的账户不存在！！！");
                    }
                } else {
                    System.out.println("闸门已打开，请尽快通过！！！");
                }
            } else {
                System.out.println("请进入闸门区域后再操作！！！");
            }
        }
        if (tag.equals("OUT")) {
            if (MonitoringDataProcessing.Switch_Is_Effective) {
                if (!MonitoringDataProcessing.Switch_Is_Opened) {

                    Card card = BasicCardService.GetCard(user);
                    if (card != null) {
                        //取出cname和balance
                        User u = BasicUserService.GetUser(user.getPhone());

                        if (u.getBalance() != null && u.getBalance() < 0) {
                            System.out.println(u.getUname() + "您的余额不足，请充值。");
                        }
                        if (!BasicCardService.SetCardCount(u.getUid())) {
                            System.out.println("插入计数出错");
                        }

                        //计算时长
                        Integer s = BusinessService.ComputeCardDuration(u);
                        Integer money = BusinessService.ComputDeductions(s, Constant.CARD_TYPE);

                        System.out.println("扣费：" + money);

                        //获取余额
                        int balance = u.getBalance() - money;
                        sb.append(card.getCname()).append("#").append(money).append("#")
                                .append(balance).append("#1#")
                                .append(DateCompute.FormatTime(s)).append("# # #");

//                            System.out.println(sb.toString());

                        //更新余额
                        BasicUserService.UpdateBalance(u.getPhone(), balance);
                        BasicCardService.SetCardStartTime(u.getUid());
                        MonitoringDataProcessing.Switch_Is_Opened = true;
                        //把车位置为可用
                        String rplaceNum = RecommendedPaking.recommedMap.get(u.getUname());
                        if(rplaceNum != null && !rplaceNum.equals("")){
                            PlaceManage.ChangePlaceStatusToNo(rplaceNum);
                            RecommendedPaking.recommedMap.remove(u.getUname());
                        }


                    } else {
                        System.out.println("对不起，您的账户不存在！！！");
                    }

                } else {
                    System.out.println("闸门已打开，请尽快通过！！！");
                }
            } else {
                System.out.println("请进入闸门区域后再操作！！！");
            }

        }

        System.out.println(sb.toString());
        return sb.toString();
    }


    /**
     * 判断card用户进入或离开
     *
     * @param user 奇数返回true
     *             偶数返回false
     * @return 查询出错 返回""
     */
    public static String JudegEnter(User user) {
        String tag = "";
        Integer uid = BasicUserService.GetUid(user);
        Integer count = BasicCardService.GetCardCount(uid);
        if (count != null) {
            if (Judge.isOdd(count)) {
                tag = "IN";
            } else {
                tag = "OUT";
            }
        }
        return tag;
    }

}
