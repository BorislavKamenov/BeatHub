package com.beathub.kamenov;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Borislav on 14.1.2015 г..
 */
public class PlaylistsAdapter extends ArrayAdapter<Playlist> {
    private int layout_id;
    private Context context;
    private ArrayList<Playlist> playlists;

    PlaylistsAdapter(Context context, int layout_id, ArrayList<Playlist> playlists) {
        super(context, layout_id, playlists);
        this.context = context;
        this.playlists = playlists;
        this.layout_id = layout_id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = convertView;
        if (view == null) {
            ViewHolder holder = new ViewHolder();
            view = inflater.inflate(layout_id, parent, false);
            holder.playlistName = (TextView) view.findViewById(R.id.playlist_name);
            holder.countOfSongs = (TextView) view.findViewById(R.id.count_of_songs_in_playlist);
            view.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        Playlist playlist = playlists.get(position);
        holder.playlistName.setText(playlist.getName());
        holder.countOfSongs.setText(playlist.getCountOfSongs());
        return view;
    }

    class ViewHolder {
        private TextView playlistName;
        private TextView countOfSongs;
    }
}
