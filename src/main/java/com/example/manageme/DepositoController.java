package com.example.manageme;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Optional;

public class DepositoController {

    @FXML
    VBox root;

    @FXML
    TextField BCFieldAdd;
    @FXML
    TextField QNTadd;
    @FXML
    TextField BCFieldRemove;
    @FXML
    TextField QNTremove;

    @FXML
    protected Text reportAdd;
    @FXML
    protected Text reportRemove;


    @FXML
    TextField BCProdAdd;
    @FXML
    TextField NPProdAdd;
    @FXML
    TextField BCProdRem;

    @FXML
    Text reportAddProd;
    @FXML
    Text reportRemProd;


    @FXML
    protected TableView TabellaDeposito;

    @FXML
    protected Label LoggedUserLabel;

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
    protected void sendAdd(){
        String barcode= BCFieldAdd.getText();
        int quantita= Integer.parseInt(QNTadd.getText());

        if(barcode.isEmpty()||quantita<=0){
            reportAdd.setText("Impossibile Inviare: Riempi Correttamente tutti i Campi!");
            return;
        }
        String query="INSERT INTO deposito(Nome_Utente,Barcode,Data_Ora_inserimento) VALUES (?,?,?) ";
        for(int i=0;i<quantita;i++) {
            try (PreparedStatement stmt = DBConnection.conn.prepareStatement(query)) {

                stmt.setString(1, DBConnection.LoggedUser);
                stmt.setString(2, barcode);
                stmt.setTimestamp(3, Timestamp.valueOf(java.time.LocalDateTime.now()));

                int righeInserite = stmt.executeUpdate();

                if (righeInserite > 0) {
                    System.out.println("Prodotto inserito con successo!");
                    reportAdd.setText("Prodotto inserito con successo!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        updateTable("SELECT d.id,d.nome_utente,p.Nome_Prodotto,d.barcode, count(d.barcode) Quantità,d.Data_Ora_Inserimento,d.Data_Ora_Uscita FROM deposito d JOIN prodotto p ON p.BarCode = d.BarCode WHERE d.Data_Ora_Uscita IS NULL\n" +
                "GROUP BY d.BarCode;");
    }
    @FXML
    protected void sendRem(){
        String barcode= BCFieldRemove.getText();
        int quantita= Integer.parseInt(QNTremove.getText());

        if(barcode.isEmpty()||quantita<0){
            reportRemove.setText("Impossibile Inviare: Riempi Correttamente tutti i Campi!");
            return;
        }
        String query = "DELETE FROM deposito WHERE Barcode = ? ORDER BY Data_Ora_inserimento DESC LIMIT 1";
        for(int i=0;i<quantita;i++) {
            try (PreparedStatement stmt = DBConnection.conn.prepareStatement(query)) {

                stmt.setString(1, barcode);

                int righeDel = stmt.executeUpdate();

                if (righeDel > 0) {
                    System.out.println("Prodotto eliminato con successo!");
                    reportRemove.setText("Prodotto eliminato con successo!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        updateTable("SELECT d.id,d.nome_utente,p.Nome_Prodotto,d.barcode, count(d.barcode) Quantità,d.Data_Ora_Inserimento,d.Data_Ora_Uscita FROM deposito d JOIN prodotto p ON p.BarCode = d.BarCode WHERE d.Data_Ora_Uscita IS NULL\n" +
                "GROUP BY d.BarCode;");
    }


    @FXML
    protected void sendAddProd(){
        String barcode= BCProdAdd.getText();
        String nome_prodotto= NPProdAdd.getText();
        if(barcode.isEmpty()){
            reportAddProd.setText("Impossibile Inviare: Riempi Correttamente tutti i Campi!");
            return;
        }
        String query="INSERT INTO prodotto(Nome_Prodotto,Barcode) VALUES (?,?) ";

            try (PreparedStatement stmt = DBConnection.conn.prepareStatement(query)) {

                stmt.setString(1, nome_prodotto);
                stmt.setString(2, barcode);

                int righeInserite = stmt.executeUpdate();

                if (righeInserite > 0) {
                    System.out.println("Prodotto inserito con successo!");
                    reportAddProd.setText("Prodotto inserito con successo!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        updateTable("SELECT * from prodotto");

    }

    @FXML
    protected void sendRemProd(){
        String barcode= BCProdRem.getText();

        if(barcode.isEmpty()){
            reportRemProd.setText("Impossibile Inviare: Riempi Correttamente tutti i Campi!");
            return;
        }
        String query0 = "DELETE FROM deposito WHERE Barcode = ?";

        try (PreparedStatement stmt = DBConnection.conn.prepareStatement(query0)) {

            stmt.setString(1, barcode);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query = "DELETE FROM prodotto WHERE Barcode = ?";

            try (PreparedStatement stmt = DBConnection.conn.prepareStatement(query)) {

                stmt.setString(1, barcode);

                int righeDel = stmt.executeUpdate();

                if (righeDel > 0) {
                    System.out.println("Prodotto eliminato con successo!");
                    reportRemProd.setText("Prodotto eliminato con successo!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        updateTable("SELECT * from prodotto");
    }



    @FXML
    private void changeToCharts(){
        try{
            Stage stage = (Stage) root.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(WelcomeController.class.getResource("Charts.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 900, 580);
            ChartsController chartsController = fxmlLoader.getController();
            chartsController.initialize(db);
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
