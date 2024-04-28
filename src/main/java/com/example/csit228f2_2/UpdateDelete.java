package com.example.csit228f2_2;

import com.mysql.cj.log.Log;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class UpdateDelete implements Sayopable{
    public Text userLabel;
    @FXML
    Button deleteButton;



    @FXML
    public void deleteAccount() throws SQLException {
        showInformationMessage("ARE YOU SURE", "SURE KA IDELETE NIMO NI ACC?");


        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "DELETE FROM users WHERE userid = ?")) {

            int userIdtoDel = LoginController.userid;
            statement.setInt(1, userIdtoDel);

            int rowsDel = statement.executeUpdate();

            if(rowsDel > 0) {
                showLoginStage();
                showConfirmationMessage("DELETE", "YES DELETED NAHAHAH");
            }

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void logout() {
        showConfirmationMessage("Logout?", "Logout ka?");
        LoginController.username = null;
        LoginController.userid = 0;
        LoginController.password = null;

        try {
            showLoginStage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showErrorMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void showInformationMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void showConfirmationMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            alert.showAndWait();
        } else {
            return;
        }
    }

    @FXML
    public void showLoginStage() throws IOException {
        Stage stage = (Stage) deleteButton.getScene().getWindow();
        stage.close();

        Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("User Login");
//        stage.getIcons().add(new Image("image/banana.png"));
        stage.show();
    }

    public void setUsername(String username) {
        userLabel.setText(username.toUpperCase());
    }


}
