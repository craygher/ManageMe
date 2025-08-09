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
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.Optional;

public class UsersController {

    @FXML
    VBox root;
    @FXML
    protected Label LoggedUserLabel;
    @FXML
    protected TableView TabellaDeposito;
    protected DBConnection db;


    @FXML
    TextField NAddPerso;
    @FXML
    TextField CAddPers;
    @FXML
    Text reportAddPers;


    @FXML
    TextField IDRemPers;
    @FXML
    Text reportRemPers;


    @FXML
    TextField PassAddUt;
    @FXML
    TextField NUAddUt;
    @FXML
    TextField id_associato;
    @FXML
    Text reportAddUt;


    @FXML
    TextField IDRemUt;
    @FXML
    Text reportRemUt;



    protected void initialize(DBConnection db){
        LoggedUserLabel.setText("User: "+DBConnection.LoggedUser);
        this.db=db;
        String Query = "SELECT u.Nome_utente, u.Data_iscrizione,p.nome,p.cognome FROM Utente u JOIN Persona p ON u.id_persona = p.id";
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
    protected void sendAddPers(){
        String nome= NAddPerso.getText();
        String cognome= NAddPerso.getText();

        if(nome.isEmpty()||cognome.isEmpty()){
            reportAddPers.setText("Impossibile Inviare: Riempi Correttamente tutti i Campi!");
            return;
        }
        String query="INSERT INTO Persona(Nome,Cognome) VALUES (?,?) ";
        try (PreparedStatement stmt = DBConnection.conn.prepareStatement(query)) {

            stmt.setString(1, nome);
            stmt.setString(2, cognome);

            int righeInserite = stmt.executeUpdate();

            if (righeInserite > 0) {
                System.out.println("Persona inserita con successo!");
                reportAddPers.setText("Persona inserita con successo!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        updateTable("SELECT * FROM persona");
    }

    @FXML
    protected void sendRemPers(){
        String id_rem= IDRemPers.getText();

        if(id_rem.isEmpty()){
            reportRemPers.setText("Impossibile Inviare: Riempi Correttamente tutti i Campi!");
            return;
        }
        String query = "DELETE FROM persona WHERE id = ?";

            try (PreparedStatement stmt = DBConnection.conn.prepareStatement(query)) {

                stmt.setString(1, id_rem);

                int righeDel = stmt.executeUpdate();

                if (righeDel > 0) {
                    System.out.println("Persona eliminata con successo!");
                    reportRemPers.setText("Persona eliminata con successo!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        updateTable("SELECT * FROM persona");
    }

    @FXML
    protected void sendAddUt(){
        String nome_utente= NUAddUt.getText();
        String password= PassAddUt.getText();
        String id_ass= id_associato.getText();

        if(nome_utente.isEmpty()||password.isEmpty()||id_ass.isEmpty()){
            reportAddUt.setText("Impossibile Inviare: Riempi Correttamente tutti i Campi!");
            return;
        }

        String HashPass= BCrypt.hashpw(password, BCrypt.gensalt());


        String query="INSERT INTO Utente(Nome_utente,Password,Data_Iscrizione,id_persona) VALUES (?,?,?,?) ";
        try (PreparedStatement stmt = DBConnection.conn.prepareStatement(query)) {

            stmt.setString(1, nome_utente);
            stmt.setString(2, HashPass);
            stmt.setTimestamp(3, Timestamp.valueOf(java.time.LocalDateTime.now()));
            stmt.setString(4, id_ass);

            int righeInserite = stmt.executeUpdate();

            if (righeInserite > 0) {
                System.out.println("Utente inserito con successo!");
                reportAddUt.setText("Utente inserito con successo!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        updateTable("SELECT u.Nome_utente, u.Data_iscrizione,p.nome,p.cognome FROM Utente u JOIN Persona p ON u.id_persona = p.id");
    }

    @FXML
    protected void sendRemUt(){
        int id_rem= Integer.parseInt(IDRemUt.getText());

        if(id_rem<0){
            reportRemUt.setText("Impossibile Inviare: Riempi Correttamente tutti i Campi!");
            return;
        }
        String query = "DELETE FROM Utente WHERE id_persona = ?";

        try (PreparedStatement stmt = DBConnection.conn.prepareStatement(query)) {

            stmt.setInt(1, id_rem);

            int righeDel = stmt.executeUpdate();

            if (righeDel > 0) {
                System.out.println("Utente eliminato con successo!");
                reportRemUt.setText("Utente eliminato con successo!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateTable("SELECT u.Nome_utente, u.Data_iscrizione,p.nome,p.cognome FROM Utente u JOIN Persona p ON u.id_persona = p.id");
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

    @FXML
    private void changeToAddRem(){
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

}
