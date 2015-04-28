package player;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import player.csv.CsvFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

/**
 * Created by azertify on 04/03/15.
 */
public class Library {
    private ObservableList<Song> songs;
    private int index;
    private boolean shuffle;

    /**
     * Constructor for Library, initialises the fields
     */
    public Library() {
        songs = FXCollections.observableArrayList();
        index = 0;
        shuffle = false;
    }

    /**
     * Reads files recursively to find all music files
     * @param file The home music directory, which contains all music files
     */
    public void readFiles(File file) {
        if (file.isDirectory()) {
            readFiles(file.toPath());
        }
    }

    private void readFiles(Path folder) {
        try {
            Files.walk(folder)
                    .filter(Library::isMp3File)
                    .forEach(p -> addSong(p));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static boolean isMp3File(Path path) {
        if (Files.isRegularFile(path) && path.toString().endsWith(".mp3")) {
            return true;
        } else {
            return false;
        }
    }

    private void addSong(Path path) {
        songs.add(new Song(path));
    }

    public int getIndex() {
        return index;
    }

    /**
     * Sets index of current song
     * @param index Index of current song
     */
    public void setIndex(int index) {
        if (index > 0 && index < songs.size()) {
            this.index = index;
        }
    }

    /**
     * Chooses index of next song
     */
    public void nextSong() {
        if (shuffle) {
            Random random = new Random();
            setIndex(random.nextInt(songs.size()));
        } else {
            setIndex(index + 1 % songs.size());
        }
    }

    /**
     * Gets the Song object at the specified index
     * @param index Index of song
     * @return Song object
     */
    public Song getSong(int index) {
        if (index >= 0 && index < songs.size()) {
            return songs.get(index);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * All of the songs
     * @return List of all songs
     */
    public ObservableList<Song> getSongs() {
        return songs;
    }

    /**
     * Saves the library to a file
     * @param file File to save
     */
    public void saveLibrary(File file) {
        CsvFile csv = new CsvFile(file);
        for (Song song : songs) {
            csv.add(new String[]{
                    song.getPath().toString(),
                    song.getTrack().getValue(),
                    song.getAlbum().getValue(),
                    song.getArtist().getValue(),
                    String.valueOf(song.getLength()),
            });
        }
        csv.close();
    }

    /**
     * Loads library from file
     * @param file File to load
     * @return True if successful, false if file does not exist, or if file is wrong
     */
    public boolean loadLibrary(File file) {
        songs.clear();
        if (!file.exists()) {
            return false;
        }
        CsvFile csv = new CsvFile(file);
        csv.getStream().forEach(s -> {
            songs.add(Song.fromString(s));
        });
        if (!checkSongs()) {
            return false;
        }
        return true;
    }

    /**
     * Ensures songs in list exist
     * @return True if all songs in list, false otherwise
     */
    private boolean checkSongs() {
        for (Song song : songs) {
            if (!song.getPath().toFile().exists()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Toggles shuffle
     */
    public void toggleShuffle() {
        shuffle = !shuffle;
    }
}
