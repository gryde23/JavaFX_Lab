module com.example.jfx_lab {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.swing;
    requires static lombok;
    requires javafx.media;

    opens com.example.jfx_lab to javafx.fxml;
    opens com.example.jfx_lab.controllers.fourthTask to javafx.fxml;
    opens com.example.jfx_lab.controllers to javafx.fxml;
    exports com.example.jfx_lab;
    exports com.example.jfx_lab.controllers;
    exports com.example.jfx_lab.controllers.fourthTask;
}