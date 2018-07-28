package com.example.codehead.musicdemo;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.MediaController;


public class MusicController extends MediaController {
    Context context;
    public MusicController(Context context){
        super(context);
        this.context = context;
    }


    public void hide(){}


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(getContext(),LauncherActivity.class);
            context.startActivity(intent);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

}