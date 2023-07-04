module com.example.puzzlefx {
    requires javafx.controls;
    requires javafx.web;
    requires java.desktop;
    requires javafx.swing;

    opens com.example.puzzlefx to javafx.fxml;
    exports com.example.puzzlefx;
}