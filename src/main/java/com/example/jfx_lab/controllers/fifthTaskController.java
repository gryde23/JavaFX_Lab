package com.example.jfx_lab.controllers;



import com.example.jfx_lab.HelloController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;



import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class fifthTaskController extends HelloController implements Initializable {
    @FXML
    Button backButton;
    @FXML
    private TableView<TableData> table;
    @FXML
    private TableColumn<TableData, String> languageColumn;
    @FXML
    private TableColumn<TableData, String> authorColumn;
    @FXML
    private TableColumn<TableData, String> yearColumn;
    @FXML
    TextField textFieldForLanguage;
    @FXML
    TextField textFieldForAuthor;
    @FXML
    TextField textFieldForYear;
    @FXML
    Button addButton;

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

    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupLanguageColumn(languageColumn, "language");
        setupLanguageColumn(authorColumn, "author");
        setupLanguageColumn(yearColumn, "year");
        table.setEditable(true);
        ObservableList<TableData> data = FXCollections.observableArrayList(
                new TableData("Си", "Деннис Ритчи", "1972"),
                new TableData("С++", "Бьерн Страуструп", "1983"),
                new TableData("Python", "Гвидо ван Россум", "1991"),
                new TableData("Java", "Джеймс Гослинг", "1995"),
                new TableData("JavaScript", "Брендон Айк", "1995"),
                new TableData("С#", "Андерс Хейлсберг", "2001"),
                new TableData("Scala", "Мартин Одерски", "2003")
        );
        languageColumn.setCellValueFactory(new PropertyValueFactory<>("language"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        table.setItems(data);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    @FXML
    private void onAddButtonClick() {
        if (!Objects.equals(textFieldForLanguage.getText(), "") && !Objects.equals(textFieldForAuthor.getText(), "") && !Objects.equals(textFieldForYear.getText(), "")) {
            TableData addedData = new TableData(textFieldForLanguage.getText(), textFieldForAuthor.getText(), textFieldForYear.getText());
            table.getItems().add(addedData);
        }
    }

    private void setupLanguageColumn(TableColumn<TableData, String> column, String name) {
        column.setCellValueFactory(new PropertyValueFactory<>(name));
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(event -> {
            TableData languageInfo = event.getRowValue();
            try {
                languageInfo
                        .getClass()
                        .getMethod("set" + name.substring(0, 1).toUpperCase() + name.substring(1), String.class)
                        .invoke(languageInfo, event.getNewValue());
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
