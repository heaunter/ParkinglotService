package com.wswenyue.SocketClient;

import com.wswenyue.protocol.AnalysisMsgOfCard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by wswenyue on 2015/5/24.
 */
public class ARMClient implements Runnable {

    private String msg = null;
    private static PrintStream ps = null;
    private BufferedReader br = null;
    private Socket socket = null;
    private String Message = null;

    public ARMClient(){
        socket = GetSocket.IsSOCNull();

        try {
            this.ps = new PrintStream(socket.getOutputStream());
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while ((Message = br.readLine())!= null){
                            System.out.println("ARM服务器回复的信息为：" + Message);
                            if(Message.equalsIgnoreCase("MSG:gate1:1") ||
                                    Message.equalsIgnoreCase("MSG:gate1:2")||
                                    Message.equalsIgnoreCase("MSG:gate1:3")){
                               String res = AnalysisMsgOfCard.dowork(Message);
                                ps.println(res);
                                System.out.println(res);
                                ps.flush();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ARMClient(String msg) {
        this.msg = msg;
    }

    @Override
    public void run() {
            if (msg != null) {
                ps.println(msg);
                ps.flush();
            }

    }
}


