package com.beathub.kamenov;

public class Playlist {

    private String name;
    private int countOfSongs;

    public Playlist(String name){
        this.name = name;
    }

//    Playlist(String name, int countOfSongs){
//        this.name = name;
//        this.countOfSongs = countOfSongs;
//    }

    public String getName() {
        return name;
    }

    public int getCountOfSongs() {
        return countOfSongs;
    }
}
