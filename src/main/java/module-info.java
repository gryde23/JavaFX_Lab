module com.example.jfx_lab1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.jfx_lab1 to javafx.fxml;
    opens com.example.jfx_lab1.controllers to javafx.fxml;
    exports com.example.jfx_lab1;
    exports com.example.jfx_lab1.controllers;
}