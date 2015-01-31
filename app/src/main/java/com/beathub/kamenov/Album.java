package com.beathub.kamenov;

public class Album {

    private int album_id;
    private String albumName;
    private String artistName;

    public Album(int album_id, String albumName, String artistName){
        this.album_id = album_id;
        this.albumName = albumName;
        this.artistName = artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public int getAlbumId() {
        return album_id;
    }
}
