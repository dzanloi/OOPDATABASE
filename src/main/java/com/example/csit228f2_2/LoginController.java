package com.example.csit228f2_2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController {
    public static List<User> users;
    @FXML
    private TextField tfusername;

    @FXML
    private TextField tfpassword;

    @FXML
    private Button loginButton;

    Window window;

    public void login(ActionEvent actionEvent) throws SQLException {
        Connection connection = MySQLConnection.getConnection();
        connection.setAutoCommit(false);

        String username = tfusername.getText();
        String password = tfpassword.getText();

        for (User user : users) {
            if (username.equals(user.username) && password.equals(user.password)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
                try {
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.close();

                    Scene scene = new Scene(loader.load());
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        connection.commit();
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
}
