package com.example.puzzlefx.service.impl;

import com.example.puzzlefx.service.ImageService;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class ImageServiceImpl implements ImageService {
    @Override
    public List<BufferedImage> splitImage(BufferedImage image, int numOfRows, int numOfColumns, String path) {
        List<BufferedImage> subImages = new ArrayList<>();
        if (image == null || numOfRows <= 0 || numOfColumns <= 0 || numOfRows > image.getHeight() || numOfColumns > image.getWidth()) {
            throw new IllegalArgumentException("Invalid arguments provided");
        }
        int eWidth = image.getWidth() / numOfColumns;
        int eHeight = image.getHeight() / numOfRows;
        int x = 0;
        int y = 0;
        int count = numOfColumns * numOfRows;

        String absolutePath = createTilesDir(path);

        deleteFilesFromDirectory(absolutePath);

        for (int i = 0; i < numOfRows; i++) {
            x = 0;
            for (int j = 0; j < numOfColumns; j++) {
                try {
                    BufferedImage subImage = image.getSubimage(x, y, eWidth, eHeight);
                    File file = new File(absolutePath + "/piece" + count + ".png");
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    count--;
                    ImageIO.write(subImage, "png", file);
                    subImages.add(subImage);
                } catch (Exception e) {
                    throw new ImagingOpException("Can't create piece " + i + " " + j + " due to " + e.getMessage());
                }
                x += eWidth;
            }
            y += eHeight;
        }
        return subImages;
    }

    @Override
    public Image resizeImage(Image image, int newWidth, int newHeight) {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        int type = 0;
        type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(bufferedImage, 0, 0, newWidth, newHeight, null);
        g.dispose();
        return SwingFXUtils.toFXImage(resizedImage, null);
    }

    @Override
    public void uploadImage(Stage primaryStage, Button chooseImageButton, String path, String format) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            try {
                BufferedImage newImage = ImageIO.read(selectedFile);
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
                ImageIO.write(newImage, format, file);
            } catch (IOException ex) {
                throw new ImagingOpException("Can`t upload image");
            }
        }
    }

    public void deleteFilesFromDirectory(String path) {
        File directory = new File(path);
        File[] allFiles = directory.listFiles();
        if (allFiles != null) {
            for (File file : allFiles) {
                if (file.isFile()) {
                    file.delete();
                }
            }
        }
    }

    private String createTilesDir(String path) {
        String absolutePath = Paths.get(path).toAbsolutePath().toString();
        System.out.println(absolutePath);
        File tilesDir = new File(absolutePath);
        if (!tilesDir.exists()) {
            tilesDir.mkdirs();
        }
        return absolutePath;
    }
}
