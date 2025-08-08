package com.example.manageme;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class WelcomeController {

    @FXML
    VBox root;
    @FXML
    TextField NomeUtenteField;
    @FXML
    PasswordField PasswordField;

    DBConnection db = new DBConnection();



    @FXML
    protected void onExitButtonClick(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Attenzione");
        alert.setHeaderText("Chiusura Programma");
        alert.setContentText("Sicuro di voler chiudere il programma?");
        Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("Si");
        okButton.setStyle(String.format("-fx-background-color: #ff7b5a;"));
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent()){
            if (result.get() == ButtonType.OK) {
                System.exit(0);
            }
        }
    }

    @FXML
    protected void onInvioClick(){
        String nomeUtente = NomeUtenteField.getText();
        String password = PasswordField.getText();

        if(nomeUtente.equals("") || password.equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("");
            alert.setContentText("Nome utente o Password Vuoti");
            alert.showAndWait();
            return;
        }

        try{
            db.login(nomeUtente, password);
            changeToHomeScene();
        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void changeToHomeScene() {
        try{
            Stage stage = (Stage) root.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(WelcomeController.class.getResource("/com/example/manageme/Home.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 900, 580);
            HomeController homeController = fxmlLoader.getController();
            homeController.initialize(db);
            stage.setResizable(false);
            stage.setTitle("ManageMe");
            stage.setScene(scene);
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }

    }



}