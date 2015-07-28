package com.wswenyue.protocol;

import com.wswenyue.SocketClient.ARMClient;
import com.wswenyue.constant.Constant;
import com.wswenyue.db.domain.SHT;
import com.wswenyue.db.service.PlaceManage;
import com.wswenyue.db.service.SHTManage;
import com.wswenyue.parking.MPPlayer;
import com.wswenyue.utils.DateCompute;

import java.util.ArrayList;
import java.util.List;

/**
 * 监控数据处理
 * 烟雾 smoke 、湿度 humidity、温度 temperature、车距 vehideDistance
 * 停车位占用检测 parkingOccupancy
 * Created by wswenyue on 2015/6/29.
 */
public class MonitoringDataProcessing {
    private static int Occupancy1Recorder = 0;
    private static int Occupancy2Recorder = 0;
    private static int Occupancy3Recorder = 0;
    private static int Occupancy4Recorder = 0;
    private static int Occupancy1Recorder_NoCar = 0;
    private static int Occupancy2Recorder_NoCar = 0;
    private static int Occupancy3Recorder_NoCar = 0;
    private static int Occupancy4Recorder_NoCar = 0;
    private static boolean IsChange_1 = false;
    private static boolean IsChange_2 = false;
    private static boolean IsChange_3 = false;
    private static boolean IsChange_4 = false;
    private static boolean IsChange_1_NoCar = false;
    private static boolean IsChange_2_NoCar = false;
    private static boolean IsChange_3_NoCar = false;
    private static boolean IsChange_4_NoCar = false;

    private static List<SHT> shts = new ArrayList<>();
    private static String S = null;
    private static String H = null;
    private static String T = null;
    private static int SHTS_COUNT = 3;

    /**开关是否有效的状态*/
    public static boolean Switch_Is_Effective = false;
    /**开关是否被打开了*/
    public static boolean Switch_Is_Opened = false;

    /**超声波模块的状态：激发态EXCITED 正常态NORMAL*/
    private static String Ultrasonic_1_Staus = "NORMAL";
    private static String Ultrasonic_2_Staus = "NORMAL";
    /**记录超声波模块被激发的先后顺序：0表示初始化状态，1表示模块一最先被触发，2表示模块二最先被触发*/
    private static int Is_First_Excited = 0;
    /**判断是否修改激发态的标志量；修改车出入后多少秒可用*/
    private static boolean IsReceive = true;
    private static long STOP_TIME = 4000;



    /**
     * 烟雾 smoke、湿度 humidity、温度 temperature、
     * 车距 vehideDisrance、停车位占用检测 parkingOccupancy
     */
    public static void ReceiveData(String smoke, String humidity,
                                   String temperature, String vehideDistance1, String vehideDistance2,
                                   String parkingOccupancy1, String parkingOccupancy2,
                                   String parkingOccupancy3, String parkingOccupancy4) {
        if (smoke != null && humidity != null && temperature != null
                && !smoke.equals("") && !humidity.equals("") && !temperature.equals("")) {
            SHTRecorder(smoke, humidity, temperature);
        }
        if (vehideDistance1 != null && vehideDistance2 != null
                && !vehideDistance1.equals("") && !vehideDistance2.equals("")) {
            VehicleDistanceRecorder(vehideDistance1, vehideDistance2);
        }
        if (parkingOccupancy1 != null && parkingOccupancy2 != null && parkingOccupancy3 != null && parkingOccupancy4 != null
                && !parkingOccupancy1.equals("") && !parkingOccupancy2.equals("") && !parkingOccupancy3.equals("") && !parkingOccupancy4.equals("")) {
            ParkingOccupancyRecorder(parkingOccupancy1, parkingOccupancy2, parkingOccupancy3, parkingOccupancy4);
        }

    }

    /**
     * 烟雾、温湿度处理
     */
    public static void SHTRecorder(String smoke, String humidity, String temperature) {

        /**判断是否有烟雾，有烟雾启动报警音乐*/
        if(smoke.equals("1") && MPPlayer.PLAYER_STATUS){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("有烟雾===启动报警");
                    MPPlayer.PlayMp3();
                }
            }).start();
        }

        if (SHTS_COUNT < 1) {
            SHTManage.AddDatas(shts);
            shts.clear();
            SHTS_COUNT = 3;
            //TODO  给ARM发送温湿度
            System.out.println("给ARM发送温湿度:"+"e"+temperature+"#"+humidity+"#");
            Thread t = new Thread(new ARMClient("e"+temperature+"#"+humidity+"#"));
            t.start();
        }
        if (S != null) {
            if (!T.equals(temperature) || !H.equals(humidity) || !S.equals(smoke)) {
                SHT sht = new SHT(smoke,humidity,temperature,DateCompute.GetDate());
                shts.add(sht);
                T=temperature;
                H=humidity;
                S=smoke;
                SHTS_COUNT--;

            }
        }
        if (T == null) {
            SHT sht = new SHT(smoke,humidity,temperature,DateCompute.GetDate());
            shts.add(sht);
            T=temperature;
            H=humidity;
            S=smoke;
            SHTS_COUNT--;
        }
    }

    /**
     * 门禁车俩进出管理
     * 车距处理
     */
    public static void VehicleDistanceRecorder(String vehideDistance1, String vehideDistance2) {
//        System.out.println("1车距为：" + vehideDistance1);
//        System.out.println("2车距为：" + vehideDistance2);
        if(IsReceive && JudgePass(vehideDistance1) && !JudgePass(vehideDistance2)){
            //超声波模块1被激发
            Ultrasonic_1_Staus = "EXCITED";
            Ultrasonic_2_Staus = "NORMAL";
        }
        if(IsReceive && JudgePass(vehideDistance2) && !JudgePass(vehideDistance1)){
            //超声波模块2被激发
            Ultrasonic_2_Staus = "EXCITED";
            Ultrasonic_1_Staus = "NORMAL";
        }
        if(JudgePass(vehideDistance2) && JudgePass(vehideDistance1)){
            //非法入侵
            Ultrasonic_1_Staus = "NORMAL";
            Ultrasonic_2_Staus = "NORMAL";
            Is_First_Excited = 0;
            System.out.println("非法入侵");
        }
        if(Is_First_Excited==0){
            if(Ultrasonic_1_Staus.equals("EXCITED")){
                Is_First_Excited =1;
            }else if(Ultrasonic_2_Staus.equals("EXCITED")){
                Is_First_Excited =2;
            }
        }
        /**进车*/
        if(Is_First_Excited == 1){
            //让开关有效
            Switch_Is_Effective = true;
            //先后两个模块都触发则完成此次通行
            if(Ultrasonic_2_Staus.equals("EXCITED")){
                Is_First_Excited = 0;
                // 落杠指令发送
                if(SendCloseMsgToArm()){
                    System.out.println("发送落杠指令成功");
                    Switch_Is_Effective = false;
                    Switch_Is_Opened = false;
                }
                Ultrasonic_1_Staus = "NORMAL";
                Ultrasonic_2_Staus = "NORMAL";
                IsReceive = false;
                Change_IsReceiveToTrue();
            }
        }
        /**出车*/
        if(Is_First_Excited == 2){
            //让开关有效
            Switch_Is_Effective = true;
            //先后两个模块都触发则完成此次通行
            if(Ultrasonic_1_Staus.equals("EXCITED")){
                Is_First_Excited = 0;
                // 落杠指令发送
                if(SendCloseMsgToArm()){
                    System.out.println("发送落杠指令成功");
                    Switch_Is_Effective = false;
                    Switch_Is_Opened = false;
                }
                Ultrasonic_1_Staus = "NORMAL";
                Ultrasonic_2_Staus = "NORMAL";
                IsReceive = false;
                Change_IsReceiveToTrue();
            }
        }

    }


    /**
     * 停车位处理
     */
    public static void ParkingOccupancyRecorder(String parkingOccupancy1, String parkingOccupancy2, String parkingOccupancy3, String parkingOccupancy4) {
        if (Occupancy1Recorder > Constant.CORRECTION_COUNT && !IsChange_1) {
            System.out.println("车位" + Constant.PARKING_OCCUPANCY_1 + "已有车");
            PlaceManage.ChangePlaceStatusToYes(Constant.PARKING_OCCUPANCY_1);
            IsChange_1 = true;
            IsChange_1_NoCar = false;
        }
        if (Occupancy2Recorder > Constant.CORRECTION_COUNT && !IsChange_2) {
            System.out.println("车位" + Constant.PARKING_OCCUPANCY_2 + "已有车");
            PlaceManage.ChangePlaceStatusToYes(Constant.PARKING_OCCUPANCY_2);
            IsChange_2 = true;
            IsChange_2_NoCar = false;
        }
        if (Occupancy3Recorder > Constant.CORRECTION_COUNT && !IsChange_3) {
            System.out.println("车位" + Constant.PARKING_OCCUPANCY_3 + "已有车");
            PlaceManage.ChangePlaceStatusToYes(Constant.PARKING_OCCUPANCY_3);
            IsChange_3 = true;
            IsChange_3_NoCar = false;
        }
        if (Occupancy4Recorder > Constant.CORRECTION_COUNT && !IsChange_4) {
            System.out.println("车位" + Constant.PARKING_OCCUPANCY_4 + "已有车");
            PlaceManage.ChangePlaceStatusToYes(Constant.PARKING_OCCUPANCY_4);
            IsChange_4 = true;
            IsChange_4_NoCar = false;
        }
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        if (Occupancy1Recorder_NoCar > Constant.CORRECTION_COUNT && !IsChange_1_NoCar) {
            System.out.println("车位" + Constant.PARKING_OCCUPANCY_1 + "无停车，可以使用");
            PlaceManage.ChangePlaceStatusToNo(Constant.PARKING_OCCUPANCY_1);
            IsChange_1_NoCar = true;
            IsChange_1 = false;
        }
        if (Occupancy2Recorder_NoCar > Constant.CORRECTION_COUNT && !IsChange_2_NoCar) {
            System.out.println("车位" + Constant.PARKING_OCCUPANCY_2 + "无停车，可以使用");
            PlaceManage.ChangePlaceStatusToNo(Constant.PARKING_OCCUPANCY_2);
            IsChange_2_NoCar = true;
            IsChange_2 = false;
        }
        if (Occupancy3Recorder_NoCar > Constant.CORRECTION_COUNT && !IsChange_3_NoCar) {
            System.out.println("车位" + Constant.PARKING_OCCUPANCY_3 + "无停车，可以使用");
            PlaceManage.ChangePlaceStatusToNo(Constant.PARKING_OCCUPANCY_3);
            IsChange_3_NoCar = true;
            IsChange_3 = false;
        }
        if (Occupancy4Recorder_NoCar > Constant.CORRECTION_COUNT && !IsChange_4_NoCar) {
            System.out.println("车位" + Constant.PARKING_OCCUPANCY_4 + "无停车，可以使用");
            PlaceManage.ChangePlaceStatusToNo(Constant.PARKING_OCCUPANCY_4);
            IsChange_4_NoCar = true;
            IsChange_4 = false;
        }

        if (parkingOccupancy1.equals("1")) {
            Occupancy1Recorder++;
        } else {
            Occupancy1Recorder = 0;
        }

        if (parkingOccupancy2.equals("1")) {
            Occupancy2Recorder++;
        } else {
            Occupancy2Recorder = 0;
        }

        if (parkingOccupancy3.equals("1")) {
            Occupancy3Recorder++;
        } else {
            Occupancy3Recorder = 0;
        }

        if (parkingOccupancy4.equals("1")) {
            Occupancy4Recorder++;
        } else {
            Occupancy4Recorder = 0;
        }
        //==========================================
        if (parkingOccupancy1.equals("0")) {
            Occupancy1Recorder_NoCar++;
        } else {
            Occupancy1Recorder_NoCar = 0;
        }

        if (parkingOccupancy2.equals("0")) {
            Occupancy2Recorder_NoCar++;
        } else {
            Occupancy2Recorder_NoCar = 0;
        }

        if (parkingOccupancy3.equals("0")) {
            Occupancy3Recorder_NoCar++;
        } else {
            Occupancy3Recorder_NoCar = 0;
        }

        if (parkingOccupancy4.equals("0")) {
            Occupancy4Recorder_NoCar++;
        } else {
            Occupancy4Recorder_NoCar = 0;
        }


    }

    /**
     * 间距计算
     */
    public static int Spacing(String str) {
        int x = Integer.parseInt(str);

        if (x > Constant.CHANNEL_WIDTH) {
            return x - Constant.CHANNEL_WIDTH;
        } else {
            return Constant.CHANNEL_WIDTH - x;
        }
    }

    public static boolean JudgePass(String str) {
        if (Spacing(str) > Constant.GENERAL_SENSITIVITY) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * TODO 发送落杠指令
     * */
    private static boolean SendCloseMsgToArm(){
        try {
            Thread t = new Thread(new ARMClient("s"));
            t.start();
            System.out.println("发送落杠指令--------------");
            return true;
        }catch (Exception e){
            System.out.println("发送落杠指令失败：" + e);
        }
     return false;
    }

    /**
     * 启动新线程修改IsReceive的值为true让其接受数据
     * */
    private static void Change_IsReceiveToTrue(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(STOP_TIME);
                    IsReceive = true;
                    System.out.println("修改IsReceive状态为true");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
