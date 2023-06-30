module com.example.puzzlefx {
    requires javafx.controls;
    requires javafx.fxml;
        requires javafx.web;
            
        requires org.controlsfx.controls;
            requires com.dlsc.formsfx;
            requires net.synedra.validatorfx;
            requires org.kordamp.ikonli.javafx;
            requires org.kordamp.bootstrapfx.core;
            requires com.almasb.fxgl.all;
    requires java.desktop;
    requires javafx.swing;

    opens com.example.puzzlefx to javafx.fxml;
    exports com.example.puzzlefx;
}