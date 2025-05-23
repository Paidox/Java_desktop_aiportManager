package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application
{
    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/main/window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 610, 600);

        stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/images/logo.jpg"))));
        stage.setTitle("Flight Management");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

