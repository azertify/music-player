package player;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import player.controllers.*;

import java.io.File;
import java.io.IOException;

/**
 * Created by azertify on 04/03/15.
 */
public class Main extends Application {
    private Stage stage;
    private BorderPane layout;
    private Library library;
    private Player player;

    /**
     * Starts the GUI
     * @param stage The primary stage which the application runs on
     */
    public void start(Stage stage) {
        library = new Library();

        File musicDir = new File(new File(System.getProperty("user.home")), "Music");

        if (!library.loadLibrary(new File(musicDir, "music.csv"))) {
            library.readFiles(musicDir);
            library.saveLibrary(new File(musicDir, "music.csv"));
        }
        player = Player.getInstance();
        player.setLibrary(library);

        this.stage = stage;
        this.stage.setTitle("Music Player");

        initRootLayout();
        showMusicPlayer();
    }

    /**
     * Main method, starts application
     * @param args Command line args
     */
    public static void main(String[] args) {
        Application.launch();
    }

    public Library getLibrary() {
        return library;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Initialises the root layout, which interfaces are attached to
     */
    private void initRootLayout() {
        try {
            layout = FXMLLoader.load(getClass().getResource("../RootLayout.fxml"));

            Scene scene = new Scene(layout);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialises the music player view
     */
    private void showMusicPlayer()
    {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../MusicOverview.fxml"));
            AnchorPane musicOverview = loader.load();

            layout.setCenter(musicOverview);

            MusicOverviewController controller = loader.getController();
            controller.setMain(this);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}