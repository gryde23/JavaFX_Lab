package com.example.jfx_lab.controllers;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class mainMenuController {
    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Button button3;
    @FXML
    private Button button4;
    @FXML
    private Button button5;
    @FXML
    private Button button6;
    @FXML
    private Button button7;
    @FXML
    private Button button8;

    @FXML
    public void initialize() {
        button1.setOnAction(event -> loadScene("/FXMLS/firstTask.fxml"));
        button2.setOnAction(event -> loadScene("/FXMLS/secTask.fxml"));
        button3.setOnAction(event -> loadScene("/FXMLS/thirdTask.fxml"));
        button4.setOnAction(event -> loadScene("/FXMLS/fourthTask.fxml"));
        button5.setOnAction(event -> loadScene("Scene5.fxml"));
        button6.setOnAction(event -> loadScene("Scene6.fxml"));
        button7.setOnAction(event -> loadScene("Scene7.fxml"));
        button8.setOnAction(event -> loadScene("Scene8.fxml"));
    }
    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) button1.getScene().getWindow(); // Используем любую кнопку для получения окна
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
