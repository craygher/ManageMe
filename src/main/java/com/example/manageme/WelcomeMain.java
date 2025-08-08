package com.example.manageme;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class WelcomeMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(WelcomeMain.class.getResource("welcome.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 580);
        stage.setResizable(false);
        stage.setTitle("ManageMe");
        stage.setScene(scene);
        stage.show();
        try {
            Connection db = DBConnection.getConnection();
        }catch(Exception e){
            System.out.println("Impossible to connect to database\n");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}