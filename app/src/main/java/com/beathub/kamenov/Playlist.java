package com.beathub.kamenov;

public class Playlist {

    private String name;
    private int Id;

    public Playlist(String name) {
        this.name = name;
        //countOfSongs = 0;
    }

//    Playlist(String name, int countOfSongs){
//        this.name = name;
//        this.countOfSongs = countOfSongs;
//    }

    public String getName() {
        return name;
    }

}
