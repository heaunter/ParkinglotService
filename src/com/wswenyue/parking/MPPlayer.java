package com.wswenyue.parking;

import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.FileInputStream;


public class MPPlayer {

    private String filename;
    private Player player;
    private static String path = System.getProperty("user.dir") + "\\bg.mp3";
    private static MPPlayer mpPlayer = new MPPlayer(path);
    /**
     * 播放器的状态，true表示准备好可以播放，false表示没有准备好
     */
    public static boolean PLAYER_STATUS = true;

    public static void PlayMp3() {
        if (mpPlayer == null) {
            mpPlayer = new MPPlayer(path);
        }
        mpPlayer.play();
    }

    private MPPlayer(String filename) {

        this.filename = filename;

    }

    private void play() {
        try {
            PLAYER_STATUS = false;
            BufferedInputStream buffer = new BufferedInputStream(
                    new FileInputStream(filename));
            player = new Player(buffer);
            player.play();
        } catch (Exception e) {
            System.out.println(e);
        }
        PLAYER_STATUS = true;
    }

//    public static void main(String[] args) {
//
//        String path = System.getProperty("user.dir");
//        MPPlayer mp = new MPPlayer(path+"\\bg.mp3");
//
//        mp.play();
//
//    }

}