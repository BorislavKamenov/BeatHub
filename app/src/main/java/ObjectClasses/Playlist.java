package ObjectClasses;

public class Playlist {

    private int playlistId;
    private String name;

    public Playlist(String name, int playlistId) {
        this.name = name;
        this.playlistId = playlistId;
    }

//    Playlist(String name, int countOfSongs){
//        this.name = name;
//        this.countOfSongs = countOfSongs;
//    }

    public String getName() {
        return name;
    }

    public int getPlaylistId() {
        return playlistId;
    }
}
