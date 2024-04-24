package com.example.csit228f2_2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegisterController implements Sayopable{

    @FXML
    private TextField tfusername;

    @FXML
    private TextField tfpassword;

    @FXML
    private TextField tfconfirmPassword;

    @FXML
    private Button registerButton;

    Window window;

    @FXML
    public void showLoginStage() throws IOException {
        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.close();

        Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("User Login");
//        stage.getIcons().add(new Image("image/banana.png"));
        stage.show();
    }

    @FXML
    public void register() throws SQLException {
        Connection connection = MySQLConnection.getConnection();
        connection.setAutoCommit(false);

        String username = tfusername.getText();
        String password = tfpassword.getText();
        String confirmPass = tfconfirmPassword.getText();


        if(password.equals(confirmPass)) {
            try (Connection c = MySQLConnection.getConnection();
                 PreparedStatement statement = c.prepareStatement(
                         "INSERT INTO users (username, password) VALUES (?, ?)")) {

                statement.setString(1, username);
                statement.setString(2, password);
                int rowsInserted = statement.executeUpdate();

                if (rowsInserted > 0) {
                    connection.commit();
                    System.out.println("inserted is himo'ed");

                    // IF SUCCESS ANG REGISTER POP UP MESSAGE
                    showConfirmationMessage("REJISTER ZUCCESSFULLEY", "ZUCCESS");

                    //NISUD NA SA LOGIN PAGE AFTER REG
                    showLoginStage();
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        } else {
            showInformationMessage("Dapat pariha ang password ug confirm password", "PASSWORD NOT HTE SAME");
            tfconfirmPassword.requestFocus();
        }

    }

    @Override
    public void showErrorMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void showInformationMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void showConfirmationMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
