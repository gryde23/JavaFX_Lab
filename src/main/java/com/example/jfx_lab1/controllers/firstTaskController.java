package com.example.jfx_lab1.controllers;

import com.example.jfx_lab1.HelloController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class firstTaskController extends HelloController{
    @FXML
    private Button backButton;
    @FXML
    public void initialize(ActionEvent e) {
        backButton.setOnAction(event -> {
            try {
                swap(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
    @FXML
    AnchorPane paneForFirstTask;
    @FXML
    ToggleGroup imgButtonsFirstTask;

    @FXML
    protected void onAnchorPaneClick(MouseEvent mouseEvent) {
        if (imgButtonsFirstTask.getSelectedToggle() != null) {
            ImageView copiedImageView = ((ImageView) ((ToggleButton) imgButtonsFirstTask.getSelectedToggle()).getGraphic());
            ImageView imageForPane = new ImageView(copiedImageView.getImage());
            imageForPane.setX(mouseEvent.getX() - copiedImageView.getImage().getWidth() / 2);
            imageForPane.setY(mouseEvent.getY() - copiedImageView.getImage().getHeight() / 2);
            paneForFirstTask.getChildren().add(imageForPane);
        }
    }
    @FXML
    protected void onSaveButtonClicked() {
        // Создаем экземпляр FileChooser
        FileChooser fileChooser = new FileChooser();

        // Устанавливаем заголовок диалогового окна
        fileChooser.setTitle("Сохранить изображение");

        // Устанавливаем фильтры для файлов (например, только PNG)
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        // Показываем диалоговое окно и получаем выбранный файл
        File file = fileChooser.showSaveDialog((Stage) paneForFirstTask.getScene().getWindow());

        if (file != null) {
            // Создаем снимок содержимого AnchorPane
            WritableImage image = paneForFirstTask.snapshot(null, null);

            // Сохраняем изображение в файл
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException e) {
                e.printStackTrace(); // Обработка исключений
            }
        }
    }

}
