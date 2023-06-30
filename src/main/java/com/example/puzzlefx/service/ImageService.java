package com.example.puzzlefx.service;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.awt.image.BufferedImage;
import java.util.List;

public interface ImageService {
    List<BufferedImage> splitImage(BufferedImage image, int numOfRows, int numOfColumns, String path);

    Image resizeImage(Image image, int newWidth, int newHeight);

    void uploadImage(Stage primaryStage, Button chooseImageButton, String path, String format);
}
