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
import javafx.stage.Stage;
import javafx.stage.Window;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Sayopable{
    @FXML
    public TextField tfusername;

    @FXML
    public TextField tfpassword;

    @FXML
    private Button loginButton;

    public static int userid;

    public static String username;
    public static String password;
    Window window;

    public void login() throws SQLException {
        Connection connection = MySQLConnection.getConnection();
        connection.setAutoCommit(false);

        username = tfusername.getText();
        password = tfpassword.getText();

        //TODO FOR TOMORROW: Read from my database: carreonjavadb and read the username and password then log in if kit an in there.

        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "SELECT * FROM users WHERE username = ? AND password = ?")) {

            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                userid = resultSet.getInt("userid");
                connection.commit();

                //REDIRECT SA DASHBOARD AFTER LOGIN
                showUD();
            } else {
                showErrorMessage("LOGGE'NT IN", "Logge'nt in");
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void showRegisterStage() throws IOException {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();

        Parent root = FXMLLoader.load(getClass().getResource("RegisterPage.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Registration");
//        stage.getIcons().add(new Image("image/grapes.png"));
        stage.show();
    }

    public void showUD() throws IOException {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();

        FXMLLoader root = new FXMLLoader(getClass().getResource("UD.fxml"));
        Scene scene = new Scene(root.load());

        UpdateDelete updateDelete = root.getController();
        updateDelete.setUsername(username);

        stage.setScene(scene);
        stage.setTitle("CRUD Portfolio");
        stage.show();
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
        alert.showAndWait();
    }
}
