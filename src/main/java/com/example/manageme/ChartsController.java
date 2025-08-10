package com.example.manageme;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class ChartsController implements Initializable{

    @FXML
    VBox root;
    @FXML
    protected Label LoggedUserLabel;
    @FXML
    protected TableView TabellaDeposito;
    protected DBConnection db;



    @FXML
    private BarChart<String, Number> TopSellChart;

    @FXML
    private CategoryAxis xAxisTopSellChart;

    @FXML
    private NumberAxis yAxisTopSellChart;



    @FXML
    private BarChart<String, Number> LowerSellChart;

    @FXML
    private CategoryAxis xAxisLowerSellChart;

    @FXML
    private NumberAxis yAxisLowerSellChart;



    protected void initialize(DBConnection db){
        LoggedUserLabel.setText("User: "+DBConnection.LoggedUser);
        this.db=db;
        topVenditeBarChart();
        lowerVenditeBarChart();
    }

    public void initialize(URL location, ResourceBundle resources) {}
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



    protected void topVenditeBarChart(){
        xAxisTopSellChart.setLabel("Prodotto");
        yAxisTopSellChart.setLabel("Vendite");
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        String query="SELECT p.Nome_prodotto,count(d.Data_Ora_Uscita) Venduto FROM deposito d JOIN prodotto p ON d.BarCode = p.BarCode GROUP BY p.Nome_prodotto ORDER BY Venduto DESC LIMIT 5;";
        try {
            ResultSet rs=db.getDeposito(query);
            while(rs.next()){
                series1.getData().add(new XYChart.Data<>(rs.getString(1), Integer.parseInt(rs.getString(2))));
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        // Aggiungi le serie al grafico
        TopSellChart.getData().addAll(series1);
        // Stile opzionale
        TopSellChart.setTitle("TOP 5 Prodotti più venduti");
        TopSellChart.setLegendVisible(false);
    }

    protected void lowerVenditeBarChart(){
        xAxisLowerSellChart.setLabel("Prodotto");
        yAxisLowerSellChart.setLabel("Vendite");
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        String query="SELECT p.Nome_prodotto,count(d.Data_Ora_Uscita) Venduto FROM deposito d JOIN prodotto p ON d.BarCode = p.BarCode GROUP BY p.Nome_prodotto ORDER BY Venduto ASC LIMIT 5;";
        try {
            ResultSet rs=db.getDeposito(query);
            while(rs.next()){
                series1.getData().add(new XYChart.Data<>(rs.getString(1), Integer.parseInt(rs.getString(2))));
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        // Aggiungi le serie al grafico
        LowerSellChart.getData().addAll(series1);
        // Stile opzionale
        LowerSellChart.setTitle("TOP 5 Prodotti meno venduti");
        LowerSellChart.setLegendVisible(false);
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

    @FXML
    private void changeToHome(){
        try{
            Stage stage = (Stage) root.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(WelcomeController.class.getResource("Home.fxml"));
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
