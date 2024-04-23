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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegisterController{

    @FXML
    private TextField tfusername;

    @FXML
    private TextField tfpassword;

    @FXML
    private TextField tfconfirmPassword;

    @FXML
    private Button registerButton;

    Window window;

    public void showLoginStage(MouseEvent mouseEvent) throws IOException {
        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.close();

        Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("User Login");
        stage.getIcons().add(new Image("image/banana.png"));
        stage.show();
    }

    public void register(ActionEvent actionEvent) {
        String username = tfusername.getText();
        String password = tfpassword.getText();

        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "INSERT INTO users (username, password) VALUES (?, ?)")) {

                statement.setString(1, username);
                    statement.setString(2, password);
                    int rowsInserted = statement.executeUpdate();

                    if(rowsInserted > 0)
                        System.out.println("inserted is himo'ed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
