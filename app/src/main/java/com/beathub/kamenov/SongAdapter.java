package com.beathub.kamenov;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SongAdapter extends BaseAdapter {
	
	//song_simple_row list and layout
    private Context c;
	private ArrayList<Song> songs;
	
	//constructor
	public SongAdapter(Context c, ArrayList<Song> songs){
        this.c = c;
		this.songs = songs;
	}

	@Override
	public int getCount() {
		return songs.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//map to song_simple_row layout
        LayoutInflater inflater = LayoutInflater.from(c);

        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.song_simple_row, parent, false);

		//get title and artist views
        ImageView albumArt = (ImageView) view.findViewById(R.id.artcover);
		TextView songTitle = (TextView)view.findViewById(R.id.song_title);
		TextView artistArtist = (TextView)view.findViewById(R.id.song_artist);

		//get song_simple_row using position
		Song currentSong = songs.get(position);

		//get title and artist strings
        albumArt.setImageBitmap(currentSong.getArtCover());
        songTitle.setText(currentSong.getTitle());
        artistArtist.setText(currentSong.getArtist());
		//set position as tag
        view.setTag(position);

		return view;
	}

}
