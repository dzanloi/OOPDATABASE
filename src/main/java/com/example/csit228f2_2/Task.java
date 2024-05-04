package com.example.csit228f2_2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public class Task implements Sayopable{

    public Button createTaskButton;
    public Button backButton;
    @FXML
    private TextField taskTitleField;

    @FXML
    private DatePicker deadlinePicker;

    @FXML
    public void backButtonOnAction() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void createTask() throws SQLException {
        Connection connection = MySQLConnection.getConnection();
        connection.setAutoCommit(false);

        String taskTitle = taskTitleField.getText();
        LocalDate deadline = deadlinePicker.getValue();

        if(taskTitle.isEmpty() || deadline == null) {
            showErrorMessage("No Task", "Please input all fields");
            return;
        } else {
            try (Connection c = MySQLConnection.getConnection()) {
                // Insert into the task table
                try (PreparedStatement statement = c.prepareStatement(
                        "INSERT INTO task (tasktitle, taskdeadline) VALUES (?, ?)",
                        PreparedStatement.RETURN_GENERATED_KEYS)) {

                    statement.setString(1, taskTitle);
                    statement.setString(2, String.valueOf(Date.valueOf(deadline)));
                    int rowsInserted = statement.executeUpdate();

                    if (rowsInserted > 0) {
                        // Get the generated task ID
                        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                int taskId = generatedKeys.getInt(1);

                                // Insert into the user_task table
                                try (PreparedStatement userTaskStatement = c.prepareStatement(
                                        "INSERT INTO user_task (userid, taskid) VALUES (?, ?)")) {

                                    // Use your actual user ID here
                                    int userId = LoginController.userid;
                                    userTaskStatement.setInt(1, userId);
                                    userTaskStatement.setInt(2, taskId);
                                    int userTaskRowsInserted = userTaskStatement.executeUpdate();

                                    if (userTaskRowsInserted > 0) {
                                        connection.commit();
                                        System.out.println("Task and User_Task inserted successfully");

                                        // IF SUCCESS ANG REGISTER POP UP MESSAGE
                                        showConfirmationMessage("ZUCCESS", "Task Kreated");

                                        //CLOSE IF NAIIMO
                                        Stage stage = (Stage) backButton.getScene().getWindow();
                                        stage.close();
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
}
