package player;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.MalformedURLException;

/**
 * Created by azertify on 04/03/15.
 */
public class Player {
    private static Player ourInstance = new Player();

    public static Player getInstance() {
        return ourInstance;
    }

    private Library library;
    private MediaPlayer player;
    private StringProperty nowPlaying = new SimpleStringProperty("Now Playing");

    private Player() {
    }

    /**
     * Sets the library which the player uses
     * @param library Library to use
     */
    public void setLibrary(Library library) {
        if (library.getSongs().size() < 1) {
            throw new RuntimeException("Library has no songs!");
        }
        this.library = library;
    }

    /**
     * Plays track specified by index in library
     * Should resume if paused
     */
    public void play() {
        if (library == null) {
            return;
        }
        Song song = library.getSong(library.getIndex());
        nowPlaying.setValue(song.getTrack().getValue()
                + " - " + song.getAlbum().getValue()
                + " - " + song.getArtist().getValue());
        try {
            Media media = new Media(song.getURL());
            if (player != null && player.getMedia().equals(media)) {
                player.play();
            } else {
                if (player != null) {
                    player.stop();
                }
                player = new MediaPlayer(media);
                player.setOnEndOfMedia(() -> { // awww sick lambda bro
                    library.nextSong();
                    play();
                });
                player.play();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pause current track
     */
    public void pause() {
        player.pause();
    }

    /**
     * Stop playing
     */
    public void stop() {
        player.stop();
    }

    /**
     * Returns a string of the currently playing track
     * @return Now playing string
     */
    public StringProperty getNowPlaying() {
        return nowPlaying;
    }
}
