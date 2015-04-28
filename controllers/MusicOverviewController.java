package player.controllers;

import player.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Creates the main controllers from FXML files, and adds data from a Library object
 * @author Samuel Lord
 * @version 2014.11.04
 */
public class MusicOverviewController
{
    @FXML private TableView<Song> table;
    @FXML private TableColumn<Song, String> trackColumn;
    @FXML private TableColumn<Song, String> albumColumn;
    @FXML private TableColumn<Song, String> artistColumn;
    @FXML private Label nowPlaying;

    private Main main;

    /**
     * Uses lambda functions to pull data from Song objects into the cells.
     */
    @FXML
    private void initialize()
    {
        trackColumn.setCellValueFactory(data -> data.getValue().getTrack());
        albumColumn.setCellValueFactory(data -> data.getValue().getAlbum());
        artistColumn.setCellValueFactory(data -> data.getValue().getArtist());
    }

    /**
     * Play button pressed
     */
    @FXML
    private void handlePlaySong()
    {
        int selected = table.getSelectionModel().getSelectedIndex();
        main.getLibrary().setIndex(selected);
        main.getPlayer().play();
    }

    /**
     * Stop button pressed
     */
    @FXML
    private void handleStopSong()
    {
        main.getPlayer().stop();
    }

    /**
     * Shuffle toggle button pressed
     */
    @FXML
    private void handleShuffle()
    {
        main.getLibrary().toggleShuffle();
    }

    /**
     * Next song button pressed
     */
    @FXML
    private void handleNext()
    {
        main.getLibrary().nextSong();
        main.getPlayer().play();
    }

    /**
     * Pause toggle button pressed
     */
    @FXML
    private void handlePause() {
        main.getPlayer().pause();
    }

    /**
     * Sets the table up with data from the Library object
     * @param main
     */
    public void setMain(Main main)
    {
        this.main = main;
        table.setItems(main.getLibrary().getSongs());
        nowPlaying.textProperty().bind(main.getPlayer().getNowPlaying());
    }
}