module com.example.demo3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens com.theme to javafx.fxml;
    opens com.browser.Controller to javafx.fxml;
    //opens main to javafx.fxml;
    exports com.browser;
    //exports com.theme;
    //exports com.browser.Controller to javafx.fxml;
}