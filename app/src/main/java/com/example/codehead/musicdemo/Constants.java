package com.example.codehead.musicdemo;
//this class contains constants for notification
public class Constants {
    public interface ACTION {
        String MAIN_ACTION = "com.example.codehead.musicdemo.action.main";
        String PREV_ACTION = "com.example.codehead.musicdemo.action.prev";
        String PLAY_ACTION = "com.example.codehead.musicdemo.action.play";
        String NEXT_ACTION = "com.example.codehead.musicdemo.action.next";
        String STARTFOREGROUND_ACTION = "com.example.codehead.musicdemo.action.startforeground";
        String STOPFOREGROUND_ACTION = "com.example.codehead.musicdemo.action.stopforeground";
    }
    public interface NOTIFICATION_ID {
        int FOREGROUND_SERVICE = 101;
    }
}
