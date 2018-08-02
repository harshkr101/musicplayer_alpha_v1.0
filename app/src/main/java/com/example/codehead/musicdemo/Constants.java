package com.example.codehead.musicdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Constants {
    public interface ACTION {
        String MAIN_ACTION = "com.example.codehead.musicdemo.action.main";
        String INIT_ACTION = "com.example.codehead.musicdemo.action.init";
        String PREV_ACTION = "com.example.codehead.musicdemo.action.prev";
        String PLAY_ACTION = "com.example.codehead.musicdemo.action.play";
        String NEXT_ACTION = "com.example.codehead.musicdemo.action.next";
        String STARTFOREGROUND_ACTION = "com.example.codehead.musicdemo.action.startforeground";
        String STOPFOREGROUND_ACTION = "com.example.codehead.musicdemo.action.stopforeground";

    }

    public interface NOTIFICATION_ID {
        int FOREGROUND_SERVICE = 101;
    }

    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            bm = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.album, options);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }
}
