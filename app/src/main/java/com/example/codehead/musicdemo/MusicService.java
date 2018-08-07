package com.example.codehead.musicdemo;


import java.util.ArrayList;
import java.util.Random;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;


public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    //media player
    private MediaPlayer player;
    //song list
    private ArrayList<Song> songs;
    //current position
    private int songPosn;
    //binder
    private final IBinder musicBind = new MusicBinder();
    //title of current song
    private String songTitle="";
    //artist of current Song
    private String songArtist="";
    //notification id
    private static final int NOTIFY_ID=1;
    //shuffle flag and random
    private boolean shuffle=false;
    private Random rand;

    //notification stuff
    private RemoteViews bigViews;
    private static final String TAG = "NotificationService";
    private boolean isPlaying =true;

    public void onCreate(){
        //create the service
        super.onCreate();
        //initialize position
        songPosn=0;
        //random
        rand=new Random();
        //create player
        player = new MediaPlayer();
        //initialize
        initMusicPlayer();
    }

    public void initMusicPlayer(){
        //set player properties
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //set listeners
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    //getter methods for song and artist
    protected String getSongArtist() {
        return songArtist;
    }
    protected String getSongTitle() {
        return songTitle;
    }

    //pass song list
    public void setList(ArrayList<Song> theSongs){
        songs=theSongs;
    }

    //binder
    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    //activity will bind to service
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    //release resources when unbind
    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }

    //play a song
    public void playSong(){
        //play
        player.reset();
        //get song
        Song playSong = songs.get(songPosn);
        //get title
        songTitle=playSong.getTitle();
        //get Artist
        songArtist=playSong.getArtist();
        //get id
        long currSong = playSong.getID();
        //set uri
        Uri trackUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currSong);
        //set the data source
        try{
            player.setDataSource(getApplicationContext(), trackUri);
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        player.prepareAsync();
    }

    //set the song
    public void setSong(int songIndex){
        songPosn=songIndex;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //check if playback has reached the end of a track
        if(player.getCurrentPosition()>0){
            mp.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.v("MUSIC PLAYER", "Playback Error");
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //start playback
        mp.start();
        //build notification
        Intent serviceIntent = new Intent(this, MusicService.class);
        serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startService(serviceIntent);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action=intent.getAction();
        if(action!=null){
        switch (action) {
            case Constants.ACTION.STARTFOREGROUND_ACTION:
                showNotification();
                break;

            case Constants.ACTION.PREV_ACTION:
                    Log.i(TAG, "Clicked Previous");
                    playPrev();
                break;

            case Constants.ACTION.PLAY_ACTION:
                Log.i(TAG, "Clicked Play");
                if (isPlaying) {
                    pausePlayer();
                    bigViews.setImageViewResource(R.id.status_bar_play, R.drawable.play_notification);
                    isPlaying = false;
                } else {
                    go();
                    bigViews.setImageViewResource(R.id.status_bar_play, R.drawable.pause_notification);
                    isPlaying = true;
                }
                break;
            case Constants.ACTION.NEXT_ACTION:
                playNext();
                Log.i(TAG, "Clicked Next");
                break;

            case Constants.ACTION.STOPFOREGROUND_ACTION:
                Log.i(TAG, "Received Stop Foreground Intent");
                stopForeground(true);
                stopSelf();
                break;
        }}
        return START_STICKY;
    }
  private void showNotification() {
    // Using RemoteViews to bind custom layouts into Notification
      bigViews = new RemoteViews(getPackageName(), R.layout.status_bar_expanded);

      //intents for notification
      Intent notificationIntent = new Intent(this, SongActivity.class);
      notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
      notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

      Intent previousIntent = new Intent(this, MusicService.class);
      previousIntent.setAction(Constants.ACTION.PREV_ACTION);
      PendingIntent ppreviousIntent = PendingIntent.getService(this, 0, previousIntent, 0);

      Intent playIntent = new Intent(this, MusicService.class);
      playIntent.setAction(Constants.ACTION.PLAY_ACTION);
      PendingIntent pplayIntent = PendingIntent.getService(this, 0, playIntent, 0);

      Intent nextIntent = new Intent(this, MusicService.class);
      nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
      PendingIntent pnextIntent = PendingIntent.getService(this, 0, nextIntent, 0);

      Intent closeIntent = new Intent(this, MusicService.class);
      closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
      PendingIntent pcloseIntent = PendingIntent.getService(this, 0, closeIntent, 0);

      bigViews.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
      bigViews.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
      bigViews.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);
      bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);
      bigViews.setImageViewResource(R.id.status_bar_play, R.drawable.pause_notification);

        //set current song and artist name
      bigViews.setTextViewText(R.id.status_bar_track_name, songTitle);
      bigViews.setTextViewText(R.id.status_bar_artist_name, songArtist);

      //build notification
      Notification notification = new Notification.Builder(this).build();
      notification.bigContentView = bigViews;
      notification.flags = Notification.FLAG_ONGOING_EVENT;
      notification.icon = R.drawable.ic_launcher_foreground;
      notification.contentIntent = pendingIntent;
      startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);
  }

    //playback methods
    public int getPosn(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }

    public boolean isPng(){
        return player.isPlaying();
    }

    public void pausePlayer(){
        player.pause();
    }

    public void seek(int posn){
        player.seekTo(posn);
    }

    public void go(){
        player.start();
    }

    //skip to previous track
    public void playPrev(){
        songPosn--;
        if(songPosn<0) songPosn=songs.size()-1;
        playSong();
    }

    //skip to next
    public void playNext(){
        if(shuffle){
            int newSong = songPosn;
            while(newSong==songPosn){
                newSong=rand.nextInt(songs.size());
            }
            songPosn=newSong;
        }
        else{
            songPosn++;
            if(songPosn>=songs.size())
                songPosn=0;
        }
        playSong();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    //toggle shuffle
    public void setShuffle(){
        if(shuffle){
            shuffle=false;}
         else{
             shuffle=true;
        }
    }

}