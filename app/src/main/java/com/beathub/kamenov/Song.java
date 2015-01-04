package com.beathub.kamenov;

import android.graphics.Bitmap;

public class Song {
	
	private long id;
	private String title;
	private String artist;
    private Bitmap artCover;
	
//	public Song(long songID, String songTitle, String songArtist){
//
//        this.id = songID;
//        this.title = songTitle;
//        this.artist = songArtist;
//	}

    public Song(long songID, Bitmap artCover, String songTitle, String songArtist){

        this.id = songID;
        this.artCover = artCover;
        this.title = songTitle;
        this.artist = songArtist;
    }
	
	public long getID(){return id;}
	public String getTitle(){return title;}
	public String getArtist(){return artist;}
    public Bitmap getArtCover() {
        return artCover;
    }
}
