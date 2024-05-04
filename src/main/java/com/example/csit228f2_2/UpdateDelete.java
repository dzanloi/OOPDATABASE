package com.example.csit228f2_2;

import com.mysql.cj.log.Log;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class UpdateDelete implements Sayopable{
    public Text userLabel;
    @FXML
    Button deleteButton;

    @FXML
    Button addTaskbtn;

    @FXML
    private TableView<Task> taskTableView;
    @FXML
    private TableColumn<Task, String> titleColumn;
    @FXML
    private TableColumn<Task, String> dateColumn;
    @FXML
    private TableColumn<Task, String> statusColumn;

    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));

        populateTableView();
    }

    private void populateTableView() {
        String username = userLabel.getText();
        setUsername(username);
        List<Task> tasks = fetchDataFromDatabase();
        taskTableView.getItems().clear();
        taskTableView.getItems().addAll(tasks);
    }

    //TOFIX;
    private List<Task> fetchDataFromDatabase() {
        List<Task> tasks = new ArrayList<>();
        try (Connection connection = MySQLConnection.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM task");
            while (resultSet.next()) {
                Task task = new Task(
                        resultSet.getString("tasktitle"),
                        resultSet.getString("taskdeadline")
                );
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }
    @FXML
    public void deleteAccount() throws SQLException {
        showInformationMessage("ARE YOU SURE", "SURE KA IDELETE NIMO NI ACC?");

        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "DELETE FROM users WHERE userid = ?")) {
            c.setAutoCommit(false);
            int userIdtoDel = LoginController.userid;
            statement.setInt(1, userIdtoDel);

            int rowsDel = statement.executeUpdate();

            if(rowsDel > 0) {
                c.commit();
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

    @FXML
    public void addTask() throws IOException {
        Stage stage = new Stage();

        Parent root = FXMLLoader.load(getClass().getResource("Task.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Add Task");
        stage.show();
    }
}
