package com.wswenyue.SocketClient;

import com.wswenyue.constant.Constant;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by wswenyue on 2015/5/24.
 */
public class GetSocket {

    private static boolean SocIsNull = true;
    private static Socket socket = null;

    public static Socket IsSOCNull(){
        if (SocIsNull){
            try {
                socket = new Socket(Constant.ARM_IP,Constant.ARM_PORT);
                // 在控制台打印实例化的结果
                System.out.println(socket);
                SocIsNull = false;
            } catch (IOException e) {
                System.out.println("Error:" + e);
            }
            return socket;
        }
        return socket;
    }
}
