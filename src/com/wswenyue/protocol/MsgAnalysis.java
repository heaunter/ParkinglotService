package com.wswenyue.protocol;

import com.wswenyue.SocketClient.ARMClient;
import com.wswenyue.constant.Constant;
import com.wswenyue.db.domain.User;
import com.wswenyue.db.service.BusinessService;
import com.wswenyue.db.service.UserManagementService;

/**
 * Created by wswenyue on 2015/6/12.
 */
public class MsgAnalysis {

    static BusinessService businessService = new BusinessService();

    public static String Analysis(String msg) {
        String result = "";
        String[] str = msg.split("#");
        if (Constant.Client_Login.equals(str[0])) {
            String uname = str[1];
            String upasswd = str[2];
            if (UserManagementService.Login(uname, upasswd)) {
                result = Constant.Server_Login_Succeed;
            } else {
                result = Constant.Server_Login_Fail;
            }
        }
        //监控端指令
        if (Constant.MONITOR.equals(str[0])) {
            //TODO 执行相关分析
            MonitoringDataProcessing.ReceiveData(str[1],str[2],str[3],str[4],str[5],str[6],str[7],str[8],str[9]);
//            System.out.println(System.currentTimeMillis());
//            System.out.println("是否有烟雾"+str[1]);
//            System.out.println("室内湿度"+str[2]);
//            System.out.println("室内温度"+str[3]);
//            System.out.println("车距"+str[4]);
//            System.out.println("车距"+str[5]);
//            System.out.println("车位是否占用"+str[6]+":"+str[7]+":"+str[8]+":"+str[9]);
        }
        if (Constant.Client_Register.equals(str[0])) {
            System.out.println(str[1] + "===" + str[2] + "==" + str[3] + "===" + str[4]);
            User user = new User();
            user.setUname(str[1]);
            user.setPassword(str[2]);
            user.setEmail(str[3]);
            user.setPhone(str[4]);
            if (UserManagementService.Regiester(user)) {
                result = Constant.Server_Register_Succeed;
            } else {
                result = Constant.Server_Register_Fail;
            }
        }
        if (Constant.Client_Reset.equals(str[0])) {
            String newPasswd = str[1];
            User user = new User();
            user.setEmail(str[2]);
            user.setPhone(str[3]);
            if (UserManagementService.ChangePassword(user, newPasswd)) {
                result = Constant.Server_Rsset_Succeed;
            } else {
                result = Constant.Server_Rsset_Fail;
            }
        }
        if (Constant.Client_Pay_ZFB.equals(str[0])) {
            //支付宝充值
            String uname = str[1];
            String money = str[2];
            String Passwd = str[3];
            if(Passwd.equals("admin")){
                int temp = Integer.parseInt(money);
                if(businessService.Recharge(uname,temp)){
                    result = Constant.Server_Pay_Succeed;
                }else {
                    result = Constant.Server_Pay_Fail;
                }
            }else {
                result = Constant.Server_Pay_Fail;
            }

        }
        if (Constant.Client_Pay_CZK.equals(str[0])) {
            //充值卡充值
            String uname = str[1];
            String CardNum = str[2];
            String Passwd = str[3];
            if(Passwd.equals("admin")&&CardNum.equals("money")){
                if(businessService.Recharge(uname)){
                    result = Constant.Server_Pay_Succeed;
                }else {
                    result = Constant.Server_Pay_Fail;
                }
            }else {
                result = Constant.Server_Pay_Fail;
            }

        }


        if (Constant.Client_CMD.equals(str[0])) {
            // 处理命令
            String uname = str[1];
            String cmd = str[2];
            System.out.println(uname+"---"+cmd);
            String res = PhoneCMDRun.doworkForPhone(uname, cmd);

            //System.out.println(res);

            if (res.equals("")) {
                result = Constant.Server_Unknown;
            }else if(res.equals(Constant.Authority_Not_Allowed)||res.equals(Constant.Authority_Permission_denied)){
                result = res;
            }else {
                //TODO ARM
                System.out.println("给ARM发送指令："+res);
                Thread t = new Thread(new ARMClient(res));
                t.start();
                result = Constant.Server_CMD_Execution_Succeed;
            }
        }
        return result;
    }




}
