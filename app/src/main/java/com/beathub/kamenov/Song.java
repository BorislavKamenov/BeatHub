package com.beathub.kamenov;

public class Song {
	
	private long id;
    private String path;
	private String title;
	private String artist;
    private long duration;
	
	public Song(long id, String path, String songTitle, String songArtist, long duration){

        this.id = id;
        this.path = path;
        this.title = songTitle;
        this.artist = songArtist;
        this.duration = duration;
	}

    public long getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public long getDuration() {
        return duration;
    }
}
