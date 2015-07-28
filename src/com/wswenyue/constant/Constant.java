package com.wswenyue.constant;

/**
 * Created by wswenyue on 2015/5/24.
 */
public class Constant {
    /** 服务器的端口号*/
    public static final int SERVER_PORT = 8989;
    /**
     * 以下是ARMserver配置
     * */
    public static final String OPEN_IDENTIFER = "OPEN";	//打开
    public static final String OPEN02_IDENTIFER = "OPEN2";	//打开
    public static final String OPEN03_IDENTIFER = "OPEN3";	//打开
    public static final String ARM_IP = "192.168.3.199";
    public static final int ARM_PORT = 8080;
    /**
     * 价格控制类型
     * */
    public static final String PHONE_TYPE = "phone";
    public static final String CARD_TYPE = "card";
    /**
     * 手机端发送过来的命令定义
     * */
    public static final String Client_Login = "LOGIN";
    public static final String Client_Register = "REGISTER";
    public static final String Client_Reset = "RESET";
    public static final String Client_Pay_ZFB = "PAY_ZFB";
    public static final String Client_Pay_CZK = "PAY_CZK";
    public static final String Client_CMD = "CMD";
    /**
     * 回送给手机端的指令
     * */
    public static final String Server_Login_Succeed = "LOGIN_SUCCEED";
    public static final String Server_Login_Fail  = "LOGIN_FAIL";

    public static final String Server_Register_Succeed = "REGISTER_SUCCEED";
    public static final String Server_Register_Fail = "REGISTER_FAIL";

    public static final String Server_Rsset_Succeed = "RESET_SUCCEED";
    public static final String Server_Rsset_Fail = "RESET_FAIL";

    public static final String Server_Pay_Succeed = "PAY_SUCCEED";
    public static final String Server_Pay_Fail = "PAY_FAIL";

    //异常指令
    public static final String Server_Unknown = "UNKNOWN_CODE";
    public static final String Server_CMD_Execution_Succeed = "CMD_EXECUTION_SUCCEED";
    public static final String Server_CMD_Repuat = "CMD_REPUAT";//指令重复执行

    /**
     * 权限管理
     *
     * */
    public static final String Authority_Permission_denied= "PERMISSION_DENIED";//权限不够
    public static final String Authority_Not_Allowed= "NOT_ALLOWED";//权限不够
    public static final String Authority_Area_Outside= "AREA_OUTSIDE";//区域之外

    /**
     * 监控端发送过来的命令定义
     * */
    public static final String MONITOR = "M";

    /**
     * 四个传感器代表的车位定义
     * */
    public static final String PARKING_OCCUPANCY_1 = "26";
    public static final String PARKING_OCCUPANCY_2 = "27";
    public static final String PARKING_OCCUPANCY_3 = "2";
    public static final String PARKING_OCCUPANCY_4 = "1";//
    /**车位传感器数据校正常量*/
    public static final int CORRECTION_COUNT = 5;
    /**车俩通行检查常量*/
    public static final int CHANNEL_WIDTH = 70;//进口宽度
    public static final int GENERAL_SENSITIVITY = CHANNEL_WIDTH /4;//通行灵敏度，当检测值计算超过灵敏度则认为车俩通过
}
