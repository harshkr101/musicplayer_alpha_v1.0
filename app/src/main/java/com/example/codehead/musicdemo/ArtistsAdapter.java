package com.example.codehead.musicdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ArtistsAdapter extends BaseAdapter{

private ArrayList<Song> artistlist;
private LayoutInflater inflater;

public ArtistsAdapter(Context c,ArrayList<Song> songs){
    artistlist=songs;
    inflater=LayoutInflater.from(c);
}
    @Override
    public int getCount() {
        return artistlist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") LinearLayout linearLayout = (LinearLayout)inflater.inflate(R.layout.artists_name, parent, false);
        TextView artistView = (TextView)linearLayout.findViewById(R.id.artist_textview);
        //get song using position
        Song currSong = artistlist.get(position);
        //get title and artist strings
        String artist=currSong.getArtist();
        if(artist.equals("<unknown>")){
            artist="Not available";
        }
        artistView.setText(artist);
        //set position as tag
        linearLayout.setTag(position);
        return linearLayout;
    }
}
