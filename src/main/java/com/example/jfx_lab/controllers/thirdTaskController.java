package com.example.jfx_lab.controllers;

import com.example.jfx_lab.HelloController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

public class thirdTaskController extends HelloController implements Initializable {

    @FXML
    ToggleGroup thirdTaskGroup;
    @FXML
    ColorPicker colorPickerForContour;
    @FXML
    ColorPicker colorPickerForFill;
    @FXML
    TextField textFieldForWidthOfContour;
    @FXML
    ChoiceBox<String> typeOfContour;
    @FXML
    AnchorPane paneForThirdTask;
    @FXML
    private ToggleButton lineButton;
    @FXML
    private ToggleButton circleButton;
    @FXML
    private ToggleButton ellipseButton;
    @FXML
    private ToggleButton rectangleButton;
    @FXML
    TextField heightForImageToSave;
    @FXML
    TextField widthForImageToSave;
    @FXML
    Button buttonForSave;
    @FXML
    Button buttonForHelp;

    Tooltip tooltipForHelp = new Tooltip("""
            Это приложение выполняет функцию графического редактора,
             \
            настройте изображение \
            в правой панели и щелкните лкм по левой панели,
             после добавления на панель фигуры, вы можете изменять \
            ее размеры\s
            (клавиши "<" ">" "+" "-" и менять ее расположение на клавиши стрелок\s""");
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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Arrays.asList("Dotted", "Solid", "Dashed").forEach(value -> typeOfContour.getItems().add(value));
        paneForThirdTask.setFocusTraversable(true);
        paneForThirdTask.setOnKeyPressed(this::handleKeyPress);
    }
    private List<Shape> shapes = new ArrayList<>();
    private Shape selectedShape = null;
    private List<Shape> shapesOutLines = new ArrayList<>();
    private Shape selectedShapeOutline = null;
    @FXML
    protected void onPaneClick(MouseEvent event){
        if (event.getButton() == MouseButton.SECONDARY) {
            paneForThirdTask.requestFocus();
            double x = event.getX();
            double y = event.getY();
            if (!Objects.equals(textFieldForWidthOfContour.getText(), "") && !Objects.equals(typeOfContour.getValue(), null)
                    && thirdTaskGroup.getSelectedToggle() != null) {
                int contourWidth = Integer.parseInt(textFieldForWidthOfContour.getText());
                ToggleButton selectedButton = (ToggleButton) thirdTaskGroup.getSelectedToggle();
                Color contourColor = colorPickerForContour.getValue();
                Color figureColor = colorPickerForFill.getValue();
                String contourType = typeOfContour.getValue();

                if (selectedButton != null) {
                    if (selectedButton == lineButton) {
                        drawLine(x, y, contourWidth, contourType, contourColor, figureColor);
                    } else if (selectedButton == circleButton) {
                        drawCircle(x, y, contourWidth, contourType, contourColor, figureColor);
                    } else if (selectedButton == ellipseButton) {
                        drawEllipse(x, y, contourWidth, contourType, contourColor, figureColor);
                    } else if (selectedButton == rectangleButton) {
                        drawRectangle(x, y, contourWidth, contourType, contourColor, figureColor);
                    }
                }
            }
        } else if (event.getButton() == MouseButton.PRIMARY) {
            double x = event.getX();
            double y = event.getY();
            selectShapeAt(x, y);
        }
    }
    private void selectShapeAt(double x, double y) {
        for (Shape shape : shapes) {
            if (shape.contains(x, y)) {
                if (selectedShape != null) {
                    // Сброс цвета предыдущей выбранной фигуры
                    resetShapeColor(selectedShape);
                }
                selectedShape = shape;
                for (Shape outline: shapesOutLines){
                    if (outline.contains(x,y)){
                        selectedShapeOutline = outline;
                    }
                }
                shape.setStroke(Color.RED); // Изменяем цвет выбранной фигуры
                System.out.println("Selected shape: " + shape);
                break;
            }
        }
    }
    private void resetShapeColor(Shape shape) {
        if (shape instanceof Line) {
            shape.setStroke(colorPickerForFill.getValue()); // Вернуть цвет линии
        } else if (shape instanceof Circle) {
            shape.setStroke(Color.TRANSPARENT); // Вернуть цвет круга
        } else if (shape instanceof Ellipse) {
            shape.setStroke(Color.TRANSPARENT); // Вернуть цвет эллипса
        } else if (shape instanceof Rectangle) {
            shape.setStroke(Color.TRANSPARENT); // Вернуть цвет прямоугольника
        }
    }
    private void handleShapeClick(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            Shape clickedShape = (Shape) event.getSource();
            selectShapeAt(clickedShape.getLayoutX(), clickedShape.getLayoutY());
        }
    }
    private void drawLine(double x, double y, int contourWidth, String contourType, Color contourColor, Color figureColor) {
        // Создаем прямоугольник, который будет обрамлять линию
        Rectangle rectangle = new Rectangle(x - 25, y - 25, 50, 50);
        rectangle.setStrokeWidth(contourWidth);
        rectangle.setStroke(contourColor);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setOpacity(0.5);
        shapesOutLines.add(rectangle);
        paneForThirdTask.getChildren().add(rectangle);
        rectangle.setOnMouseClicked(this::handleShapeClick);

        // Установка стиля контура
        if ("Dashed".equalsIgnoreCase(contourType)) {
            rectangle.getStrokeDashArray().addAll(10d, 5d);
        } else if ("Dotted".equalsIgnoreCase(contourType)) {
            rectangle.getStrokeDashArray().addAll(1d, 5d);
        } else {
            rectangle.getStrokeDashArray().clear(); // Сплошная линия
        }
        // Создаем линию, которая будет диагональю прямоугольника
        Line line = new Line(x - 25, y - 25, x + 25, y + 25);
        line.setStrokeWidth(2);
        line.setStroke(figureColor); // Цвет линии
        shapes.add(line);
        paneForThirdTask.getChildren().add(line);
    }

    private void drawCircle(double x, double y, int contourWidth, String contourType, Color contourColor, Color figureColor) {
        Circle circle = new Circle(x, y, 25);
        circle.setStrokeWidth(2);
        circle.setStroke(figureColor);
        circle.setFill(figureColor);
        shapes.add(circle);
        paneForThirdTask.getChildren().add(circle);
        circle.setOnMouseClicked(this::handleShapeClick);

        // Создаем контур вокруг круга
        Circle circleOutline = new Circle(x, y, 25);
        // Установка стиля контура
        if ("Dashed".equalsIgnoreCase(contourType)) {
            circleOutline.getStrokeDashArray().addAll(10d, 5d); // Пример для пунктирной линии
        } else if ("Dotted".equalsIgnoreCase(contourType)) {
            circleOutline.getStrokeDashArray().addAll(1d, 5d); // Пример для точечной линии
        } else {
            circleOutline.getStrokeDashArray().clear(); // Сплошная линия
        }
        circleOutline.setStrokeWidth(contourWidth);
        circleOutline.setStroke(contourColor);
        circleOutline.setFill(Color.TRANSPARENT);
        circleOutline.setOpacity(0.5);
        shapesOutLines.add(circleOutline);
        paneForThirdTask.getChildren().add(circleOutline);

    }

    private void drawEllipse(double x, double y, int contourWidth, String contourType, Color contourColor, Color figureColor) {
        Ellipse ellipse = new Ellipse(x, y, 50, 25);
        ellipse.setStrokeWidth(2);
        ellipse.setStroke(figureColor);
        ellipse.setFill(figureColor);
        shapes.add(ellipse);
        paneForThirdTask.getChildren().add(ellipse);
        ellipse.setOnMouseClicked(this::handleShapeClick);
        // Создаем контур вокруг эллипса
        Ellipse ellipseOutline = new Ellipse(x, y, 50, 25);
        // Установка стиля контура
        if ("Dashed".equalsIgnoreCase(contourType)) {
            ellipseOutline.getStrokeDashArray().addAll(10d, 5d); // Пример для пунктирной линии
        } else if ("Dotted".equalsIgnoreCase(contourType)) {
            ellipseOutline.getStrokeDashArray().addAll(1d, 5d); // Пример для точечной линии
        } else {
            ellipseOutline.getStrokeDashArray().clear(); // Сплошная линия
        }
        ellipseOutline.setStrokeWidth(contourWidth);
        ellipseOutline.setStroke(contourColor);
        ellipseOutline.setFill(javafx.scene.paint.Color.TRANSPARENT);
        ellipseOutline.setOpacity(0.5);
        shapesOutLines.add(ellipseOutline);
        paneForThirdTask.getChildren().add(ellipseOutline);
    }

    private void drawRectangle(double x, double y, int contourWidth, String contourType, Color contourColor, Color figureColor) {
        Rectangle rectangle = new Rectangle(x - 50, y - 25, 100, 50);
        rectangle.setStrokeWidth(2);
        rectangle.setStroke(figureColor);
        rectangle.setFill(figureColor);
        shapes.add(rectangle);
        paneForThirdTask.getChildren().add(rectangle);
        rectangle.setOnMouseClicked(this::handleShapeClick);
        // Создаем контур вокруг прямоугольника
        Rectangle rectangleOutline = new Rectangle(x - 50, y - 25, 100, 50);
        if ("Dashed".equalsIgnoreCase(contourType)) {
            rectangleOutline.getStrokeDashArray().addAll(10d, 5d);
        } else if ("Dotted".equalsIgnoreCase(contourType)) {
            rectangleOutline.getStrokeDashArray().addAll(1d, 5d);
        } else {
            rectangleOutline.getStrokeDashArray().clear(); // Сплошная линия
        }
        rectangleOutline.setStrokeWidth(contourWidth);
        rectangleOutline.setStroke(contourColor);
        rectangleOutline.setFill(javafx.scene.paint.Color.TRANSPARENT);
        rectangleOutline.setOpacity(0.5);
        shapesOutLines.add(rectangleOutline);
        paneForThirdTask.getChildren().add(rectangleOutline);
    }
    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (selectedShape != null && selectedShapeOutline != null) { // Проверяем, выбрана ли фигура
            switch (event.getCode()) {
                case UP:
                    selectedShape.setLayoutY(selectedShape.getLayoutY() - 5);
                    selectedShapeOutline.setLayoutY(selectedShapeOutline.getLayoutY() - 5);
                    event.consume();// Перемещение вверх
                    break;
                case DOWN:
                    selectedShape.setLayoutY(selectedShape.getLayoutY() + 5);
                    selectedShapeOutline.setLayoutY(selectedShapeOutline.getLayoutY() + 5);
                    event.consume();// Перемещение вниз
                    break;
                case LEFT:
                    selectedShape.setLayoutX(selectedShape.getLayoutX() - 5); // Перемещение влево
                    selectedShapeOutline.setLayoutX(selectedShapeOutline.getLayoutX() - 5);
                    event.consume();
                    break;
                case RIGHT:
                    selectedShape.setLayoutX(selectedShape.getLayoutX() + 5);
                    selectedShapeOutline.setLayoutX(selectedShapeOutline.getLayoutX() + 5);
                    event.consume();// Перемещение вправо
                    break;
                case EQUALS: // Клавиша +
                    if (selectedShape instanceof Rectangle) {
                        Rectangle rect = (Rectangle) selectedShape;
                        rect.setHeight(rect.getHeight() + 5);
                        Rectangle rectOutLine = (Rectangle) selectedShapeOutline;
                        rectOutLine.setHeight(rectOutLine.getHeight() + 5);// Увеличение высоты
                    } else if (selectedShape instanceof Circle) {
                        Circle circle = (Circle) selectedShape;
                        circle.setRadius(circle.getRadius() + 5);
                        Circle circleOutLine = (Circle) selectedShapeOutline;
                        circleOutLine.setRadius(circleOutLine.getRadius() + 5);// Увеличение радиуса
                    } else if (selectedShape instanceof Ellipse) {
                        Ellipse ellipse = (Ellipse) selectedShape;
                        ellipse.setRadiusY(ellipse.getRadiusY() + 5);
                        Ellipse ellipseOutline = (Ellipse) selectedShapeOutline;
                        ellipseOutline.setRadiusY(ellipseOutline.getRadiusY() + 5);// Увеличение высоты эллипса
                    } else if (selectedShape instanceof Line) {
                        Line line = (Line) selectedShape;
                        line.setEndY(line.getEndY() + 5);
                        Rectangle lineOutline = (Rectangle) selectedShapeOutline;
                        lineOutline.setHeight(lineOutline.getHeight() + 5);
                    }
                    break;
                case MINUS: // Клавиша -
                    if (selectedShape instanceof Rectangle) {
                        Rectangle rect = (Rectangle) selectedShape;
                        rect.setHeight(Math.max(0, rect.getHeight() - 5));
                        Rectangle rectOutline = (Rectangle) selectedShapeOutline;
                        rectOutline.setHeight(Math.max(0, rectOutline.getHeight() - 5)); // Уменьшение высоты, не меньше 0
                    } else if (selectedShape instanceof Circle) {
                        Circle circle = (Circle) selectedShape;
                        circle.setRadius(Math.max(0, circle.getRadius() - 5));
                        Circle circleOutline = (Circle) selectedShapeOutline;
                        circleOutline.setRadius(Math.max(0, circleOutline.getRadius() - 5));// Уменьшение радиуса, не меньше 0
                    } else if (selectedShape instanceof Ellipse) {
                        Ellipse ellipse = (Ellipse) selectedShape;
                        ellipse.setRadiusY(Math.max(0, ellipse.getRadiusY() - 5));
                        Ellipse ellipseOutline = (Ellipse) selectedShapeOutline;
                        ellipseOutline.setRadiusY(Math.max(0, ellipseOutline.getRadiusY() - 5));
                    } else if (selectedShape instanceof Line) {
                        Line line = (Line) selectedShape;
                        line.setEndY(Math.max(0, line.getEndY() - 5));
                        Rectangle lineOutline = (Rectangle) selectedShapeOutline;
                        lineOutline.setHeight(Math.max(0, lineOutline.getHeight() - 5));
                    }
                    break;
                case COMMA: // Клавиша <
                    if (selectedShape instanceof Rectangle) {
                        Rectangle rect = (Rectangle) selectedShape;
                        rect.setWidth(Math.max(0, rect.getWidth() - 5));
                        Rectangle rectOutline = (Rectangle) selectedShapeOutline;
                        rectOutline.setWidth(Math.max(0, rectOutline.getWidth() - 5));// Уменьшение ширины
                    } else if (selectedShape instanceof Ellipse) {
                        Ellipse ellipse = (Ellipse) selectedShape;
                        ellipse.setRadiusX(Math.max(0, ellipse.getRadiusX() - 5));
                        Ellipse ellipseOutline = (Ellipse) selectedShapeOutline;
                        ellipseOutline.setRadiusX(Math.max(0, ellipseOutline.getRadiusX() - 5));
                    } else if (selectedShape instanceof Line) {
                        Line line = (Line) selectedShape;
                        line.setEndX(Math.max(0, line.getEndX() - 5));
                        Rectangle lineOutline = (Rectangle) selectedShapeOutline;
                        lineOutline.setWidth(Math.max(0, lineOutline.getWidth() - 5));
                    }
                    break;
                case PERIOD: // Клавиша >
                    if (selectedShape instanceof Rectangle) {
                        Rectangle rect = (Rectangle) selectedShape;
                        rect.setWidth(rect.getWidth() + 5);
                        Rectangle rectOutline = (Rectangle) selectedShapeOutline;
                        rectOutline.setWidth(rectOutline.getWidth() + 5);// Увеличение ширины
                    } else if (selectedShape instanceof Ellipse) {
                        Ellipse ellipse = (Ellipse) selectedShape;
                        ellipse.setRadiusX(Math.max(0, ellipse.getRadiusX() + 5));
                        Ellipse ellipseOutline = (Ellipse) selectedShapeOutline;
                        ellipseOutline.setRadiusX(ellipseOutline.getRadiusX() + 5);
                    } else if (selectedShape instanceof Line) {
                        Line line = (Line) selectedShape;
                        line.setEndX(line.getEndX() + 5);
                        Rectangle lineOutline = (Rectangle) selectedShapeOutline;
                        lineOutline.setWidth(lineOutline.getWidth() + 5);
                    }
                    break;
            }
        }
    }
    @FXML
    protected void onSaveButtonClick() {
        int height = Integer.parseInt(heightForImageToSave.getText());
        int width = Integer.parseInt(widthForImageToSave.getText());

        // Создайте FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить изображение");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG файлы", "*.png"),
                new FileChooser.ExtensionFilter("JPEG файлы", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("Все файлы", "*.*")
        );

        // Откройте диалог выбора файла
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            // Убедитесь, что файл имеет правильное расширение
            String filePath = file.getAbsolutePath();
            if (!filePath.endsWith(".png") && !filePath.endsWith(".jpg") && !filePath.endsWith(".jpeg")) {
                filePath += ".png"; // Добавьте расширение по умолчанию
            }

            // Создайте WritableImage с нужным разрешением
            WritableImage image = new WritableImage((int) paneForThirdTask.getWidth(),(int) paneForThirdTask.getHeight());
            paneForThirdTask.snapshot(null, image);

            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

            // Измените разрешение изображения
            BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(bufferedImage, 0, 0, width, height, null);
            g.dispose();

            // Сохраните измененное изображение в файл
            try {
                ImageIO.write(resizedImage, "png", new File(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void onHelpButtonPressed(MouseEvent mouseEvent) {
        tooltipForHelp.show(buttonForHelp, mouseEvent.getScreenX(), mouseEvent.getScreenY());
    }

    @FXML
    protected void onHelpButtonReleased() {
        tooltipForHelp.hide();
    }
}
