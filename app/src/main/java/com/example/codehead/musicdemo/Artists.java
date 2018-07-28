package com.example.codehead.musicdemo;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class Artists extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Song> artistList;
    private RelativeLayout artistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artistnamelist);
        listView=findViewById(R.id.artist_list);
        artistview = findViewById(R.id.artistview);
        testtime();
        artistList =new ArrayList<>();
        getSongList();
        Collections.sort(artistList, new Comparator<Song>(){
            public int compare(Song a, Song b){
                return a.getArtist().compareTo(b.getArtist());
            }
        });
        ArtistsAdapter artistsAdapter=new ArtistsAdapter(this, artistList);
        listView.setAdapter(artistsAdapter);
    }
    public void getSongList(){
        //query external audio
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        //iterate over results if valid
        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                artistList.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }
        musicCursor.close();
    }

    private void testtime() {
        Date d=new Date();
        SimpleDateFormat sdf= new SimpleDateFormat("HH");
        String currentDateTimeString = sdf.format(d);

        int time1 = Integer.parseInt(currentDateTimeString);

        if(time1 < 7)
            dark();
        else if(time1 > 7 && time1 < 19) {
            sun();
        }
        else if(time1 > 19) {
            dark();
        }
        else
            dark();

    }

    private void sun()
    {

        artistview.setBackgroundResource(R.drawable.back2);
    }
    private void dark()
    {
        artistview.setBackgroundResource(R.drawable.artistsblack);
    }


}