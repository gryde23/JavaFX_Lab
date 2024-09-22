package com.example.jfx_lab.controllers;

import com.example.jfx_lab.HelloController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.Random;

public class secTaskController extends HelloController {
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
    private Canvas CanvasSecTask;
    private double x, y, width, height;
    private int shapeType;

    @FXML
    public void initialize() {
        drawRandomShape(CanvasSecTask.getGraphicsContext2D());
        CanvasSecTask.setFocusTraversable(true);
        CanvasSecTask.setOnKeyPressed(this::handleKeyPress);
    }
    @FXML
    private void onCanvasClicked(MouseEvent event) {
        // Установите фокус на Canvas, когда он щелкается
        CanvasSecTask.requestFocus();
    }

    private void drawRandomShape(GraphicsContext gc) {
        Random random = new Random();
        shapeType = random.nextInt(4); // 0 - линия, 1 - прямоугольник, 2 - эллипс, 3 - окружность

        x = random.nextInt(300);
        y = random.nextInt(300);
        width = random.nextInt(100) + 20; // Минимальная ширина 20
        height = random.nextInt(100) + 20; // Минимальная высота 20

        gc.clearRect(0, 0, CanvasSecTask.getWidth(), CanvasSecTask.getHeight()); // Очистка канваса
        drawShape(gc);
    }

    private void drawShape(GraphicsContext gc) {
        gc.clearRect(0, 0, CanvasSecTask.getWidth(), CanvasSecTask.getHeight()); // Очистка канваса

        switch (shapeType) {
            case 0: // Отрезок прямой
                double x1 = x;
                double y1 = y;
                double x2 = x + width;
                double y2 = y + height;
                gc.strokeLine(x1, y1, x2, y2);
                break;
            case 1: // Прямоугольник
                gc.strokeRect(x, y, width, height);
                drawDashedBorder(gc, x, y, width, height);
                break;
            case 2: // Эллипс
                gc.strokeOval(x, y, width, height);
                drawDashedBorder(gc, x, y, width, height);
                break;
            case 3: // Окружность
                double radius = width; // Используем ширину как радиус
                gc.strokeOval(x, y, radius, radius);
                drawDashedBorder(gc, x, y, radius, radius);
                break;
        }
    }

    private void drawDashedBorder(GraphicsContext gc, double x, double y, double width, double height) {
        gc.setLineDashes(5); // Устанавливаем длину пунктиров
        gc.setStroke(javafx.scene.paint.Color.rgb(0, 0, 0, 0.5)); // Полупрозрачный черный цвет
        gc.strokeRect(x, y, width, height); // Рисуем рамку
        gc.setLineDashes(null); // Сбрасываем стиль пунктиров
    }
    @FXML
    private void handleKeyPress(KeyEvent event) {
        GraphicsContext gc = CanvasSecTask.getGraphicsContext2D();

        switch (event.getCode()) {
            case UP:
                y -= 5; // Перемещение вверх
                break;
            case DOWN:
                y += 5; // Перемещение вниз
                break;
            case LEFT:
                x -= 5; // Перемещение влево
                event.consume();
                break;
            case RIGHT:
                x += 5; // Перемещение вправо
                break;
            case EQUALS:// Клавиша +
                height += 5; // Увеличение высоты
                break;
            case MINUS:// Клавиша -
                height = Math.max(0, height - 5); // Уменьшение высоты, не меньше 0
                break;
            case COMMA: // Клавиша <
                width -= 5; // Уменьшение ширины
                break;
            case PERIOD: // Клавиша >
                width += 5; // Увеличение ширины
                break;
        }
        drawShape(gc); // Перерисовка фигуры
    }
}
