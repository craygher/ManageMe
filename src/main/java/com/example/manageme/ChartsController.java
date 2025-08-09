package com.example.manageme;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;


    protected void initialize(DBConnection db){
        LoggedUserLabel.setText("User: "+DBConnection.LoggedUser);
        this.db=db;
    }

    public void initialize(URL location, ResourceBundle resources) {

        // Configura gli assi
        xAxis.setLabel("Prodotto");
        yAxis.setLabel("Vendite");

        // Crea le serie di dati
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("");
        series1.getData().add(new XYChart.Data<>("Gennaio", 200));
        series1.getData().add(new XYChart.Data<>("Febbraio", 500));
        series1.getData().add(new XYChart.Data<>("Marzo", 300));
        series1.getData().add(new XYChart.Data<>("Aprile", 600));

        /*XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("2024");
        series2.getData().add(new XYChart.Data<>("Gennaio", 400));
        series2.getData().add(new XYChart.Data<>("Febbraio", 200));
        series2.getData().add(new XYChart.Data<>("Marzo", 800));
        series2.getData().add(new XYChart.Data<>("Aprile", 500));*/

        // Aggiungi le serie al grafico
        TopSellChart.getData().addAll(series1);

        // Stile opzionale
        TopSellChart.setTitle("TOP 5 Prodotti più venduti");
        TopSellChart.setLegendVisible(true);
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


}
