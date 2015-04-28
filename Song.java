package player;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;

/**
 * Created by azertify on 04/03/15.
 */
public class Song {

    private Path path;
    private StringProperty track;
    private StringProperty album;
    private StringProperty artist;
    private int length;

    /**
     * Constructor for song used when loading songs
     * @param path Path of song
     * @param track Track name
     * @param album Album name
     * @param artist Artist name
     * @param length Length of track
     */
    public Song(Path path, String track, String album, String artist, int length) {
        this.path = path;
        this.track = new SimpleStringProperty(track);
        this.album = new SimpleStringProperty(album);
        this.artist = new SimpleStringProperty(artist);
        this.length = length;
    }

    /**
     * Generates a song from a filename
     * @param filename
     */
    public Song(String filename) {
        this(new File(filename).toPath());
    }

    /**
     * Generates a song from a filename
     * @param path
     */
    public Song(Path path) {
        this.path = path;
        try {
            Mp3File mp3File = new Mp3File(path.toFile());
            track = new SimpleStringProperty(path.getFileName().toString());
            album = new SimpleStringProperty("Unknown Album");
            artist = new SimpleStringProperty("Unknown Artist");
            length = (int) mp3File.getLengthInSeconds();
            if (mp3File.hasId3v1Tag()) {
                ID3v1 tag = mp3File.getId3v1Tag();
                String tagTrack = tag.getTitle(), tagAlbum = tag.getAlbum(), tagArtist = tag.getArtist();
                if (tagTrack != null) track.setValue(tagTrack);
                else track.setValue(path.getFileName().toString());
                if (tagAlbum != null) album.setValue(tagAlbum);
                else album.setValue("Unknown Album");
                if (tagArtist != null) artist.setValue(tagArtist);
                else artist.setValue("Unknown Artist");
            } else if (mp3File.hasId3v2Tag()) {
                ID3v2 tag = mp3File.getId3v2Tag();
                String tagTrack = tag.getTitle(), tagAlbum = tag.getAlbum(), tagArtist = tag.getArtist();
                if (tagTrack != null) track.setValue(tagTrack);
                else track.setValue(path.getFileName().toString());
                if (tagAlbum != null) album.setValue(tagAlbum);
                else album.setValue("Unknown Album");
                if (tagArtist != null) artist.setValue(tagArtist);
                else artist.setValue("Unknown Artist");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Path getPath() {
        return path;
    }

    public String getURL() throws MalformedURLException {
        return path.toUri().toURL().toString();
    }

    public StringProperty getTrack() {
        return track;
    }

    public StringProperty getAlbum() {
        return album;
    }

    public StringProperty getArtist() {
        return artist;
    }

    public int getLength() {
        return length;
    }

    /**
     * Creates a song from an array of strings, when loading from a CSV file
     * @param parts Array of strings, representing each of the fields
     * @return Song made from these parts
     */
    public static Song fromString(String[] parts) {
        return new Song(
                new File(parts[0]).toPath(),
                parts[1],
                parts[2],
                parts[3],
                Integer.parseInt(parts[4])
        );
    }
}
