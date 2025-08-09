package com.example.manageme;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Optional;

public class HomeController {

    @FXML
    VBox root;

    @FXML
    protected Label LoggedUserLabel;

    @FXML
    protected TableView TabellaDeposito;


    protected DBConnection db;


    protected void initialize(DBConnection db){
        LoggedUserLabel.setText("User: "+DBConnection.LoggedUser);
        this.db=db;
        String Query = "SELECT d.id,d.nome_utente,p.Nome_Prodotto,d.barcode, count(d.barcode) Quantità,d.Data_Ora_Inserimento,d.Data_Ora_Uscita FROM deposito d JOIN prodotto p ON p.BarCode = d.BarCode WHERE d.Data_Ora_Uscita IS NULL\n" +
                "GROUP BY d.BarCode;";
        updateTable(Query);
    }
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

    protected void updateTable(String Query){
        ResultSet rs=null;
        TabellaDeposito.getColumns().clear();


        try{
            ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
            rs=db.getDeposito(Query);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 0; i < columnCount; i++) {
                final int colIndex = i;
                TableColumn<ObservableList<String>, String> column = new TableColumn<>(metaData.getColumnName(i + 1));
                column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(colIndex)));
                TabellaDeposito.getColumns().add(column);
            }

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }
            TabellaDeposito.setItems(data);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    protected void getDeposito(){
        String Query = "SELECT d.id,d.nome_utente,p.Nome_Prodotto,d.barcode, count(d.barcode) Quantità,d.Data_Ora_Inserimento,d.Data_Ora_Uscita FROM deposito d JOIN prodotto p ON p.BarCode = d.BarCode WHERE d.Data_Ora_Uscita IS NULL\n" +
                "GROUP BY d.BarCode;";
        updateTable(Query);
    }
    @FXML
    protected void getProdotto(){
        String Query = "SELECT * FROM prodotto";
        updateTable(Query);
    }
    @FXML
    protected void getUtente(){
        String Query = "SELECT u.Nome_Utente, u.Data_Iscrizione, u.id_persona, p.nome, p.cognome  FROM utente u JOIN persona p ON u.id_persona = p.id";
        updateTable(Query);
    }
    @FXML
    protected void getPersona(){
        String Query= "SELECT * FROM persona";
        updateTable(Query);
    }

    @FXML
    private void changeToAddRemove(){
        try{
            Stage stage = (Stage) root.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(WelcomeController.class.getResource("AddRemove.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 900, 580);
            DepositoController depositoController = fxmlLoader.getController();
            depositoController.initialize(db);
            stage.setResizable(false);
            stage.setTitle("ManageMe");
            stage.setScene(scene);
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void changeToCharts(){
        try{
            Stage stage = (Stage) root.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(WelcomeController.class.getResource("Charts.fxml"));
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

    @FXML
    private void changeToUsers(){
        try{
            Stage stage = (Stage) root.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(WelcomeController.class.getResource("Users.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 900, 580);
            UsersController usersController = fxmlLoader.getController();
            usersController.initialize(db);
            stage.setResizable(false);
            stage.setTitle("ManageMe");
            stage.setScene(scene);
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
