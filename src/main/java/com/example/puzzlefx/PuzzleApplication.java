package com.example.puzzlefx;

import com.example.puzzlefx.service.ImageService;
import com.example.puzzlefx.service.impl.ImageServiceImpl;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class PuzzleApplication extends Application {
    private static final int RESIZED_IMAGE_WIDTH = 500;
    private static final int RESIZED_IMAGE_HEIGHT = 400;
    private static final int NUM_OF_COLUMNS = 4;
    private static final int NUM_OF_ROWS = 4;
    private static final String IMAGE_PATH = "src/main/resources/images/";
    private static final String TILES_PATH = "src/main/resources/images/tiles";
    private static final String DEFAULT_IMAGE_NAME = "default";
    private static final String IMAGE_FORMAT = "png";
    private static List<PuzzlePiece> puzzlePieces;
    protected static int numberOfPuzzles;
    protected static Text text;
    private Timeline timeline;
    private final double puzzlePieceWidth;
    private final double puzzlePieceHeight;
    private Group root;
    private List<ImageView> tiles = new ArrayList();

    {
        puzzlePieceWidth = RESIZED_IMAGE_WIDTH / NUM_OF_COLUMNS;
        puzzlePieceHeight = RESIZED_IMAGE_HEIGHT / NUM_OF_ROWS;
        numberOfPuzzles = NUM_OF_COLUMNS * NUM_OF_ROWS;
    }

    private final ImageService imageService;

    {
        imageService = new ImageServiceImpl();
    }

    private void init(Stage primaryStage) {
        root = new Group();
        primaryStage.setScene(new Scene(root));
        text = new Text();

        final Desk desk = Desk.createPuzzleDesk(NUM_OF_COLUMNS, NUM_OF_ROWS, puzzlePieceWidth, puzzlePieceHeight);

        makePuzzle(desk, primaryStage);
    }

    private void makePuzzle(Desk desk, Stage primaryStage) {

        File imageFile = new File(IMAGE_PATH, DEFAULT_IMAGE_NAME + "." + IMAGE_FORMAT);
        Image image = new Image(imageFile.toURI().toString());
        Image resizedImage = imageService.resizeImage(image, RESIZED_IMAGE_WIDTH, RESIZED_IMAGE_HEIGHT);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(resizedImage, null);
        imageService.splitImage(bufferedImage, NUM_OF_ROWS, NUM_OF_COLUMNS, TILES_PATH);
        puzzlePieces = new ArrayList<>();

        for (int col = 0; col < NUM_OF_COLUMNS; col++) {
            for (int row = 0; row < NUM_OF_ROWS; row++) {
                double x = col * puzzlePieceWidth;
                double y = row * puzzlePieceHeight;
                final PuzzlePiece puzzlePiece = new PuzzlePiece(resizedImage, x, y, desk.getWidth(), desk.getHeight(),
                        puzzlePieceWidth, puzzlePieceHeight);
                puzzlePieces.add(puzzlePiece);
            }
        }
        desk.getChildren().addAll(puzzlePieces);
        configButtons(desk, primaryStage, resizedImage);
    }

    private void configButtons(Desk desk, Stage primaryStage, Image resizedImage) {
        Button mixButton = new Button("Mix");
        onClickMixButtonAction(desk, puzzlePieces, mixButton);

        Button chooseImageButton = new Button("Choose Image");
        onClickChooseImageButtonAction(desk, primaryStage, chooseImageButton);

        Button solveButton = new Button("Solve");
        onClickSolveButtonAction(puzzlePieces, solveButton);
        solveButton.setTranslateX(desk.getDeskWidth() - solveButton.getWidth() - 220);

        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(15));
        buttonBox.getChildren().addAll(mixButton, chooseImageButton, solveButton, text);

        Text text = new Text("Original image:");
        text.setTranslateX(desk.getDeskWidth() + desk.getDeskWidth() / 2);
        text.setTranslateY(15);

        ImageView imageView = new ImageView(resizedImage);
        imageView.setTranslateX(desk.getDeskWidth() + 50);
        imageView.setTranslateY(25);

        VBox vb = new VBox(20);
        vb.getChildren().addAll(desk, buttonBox);
        root.getChildren().addAll(vb, text, imageView);
    }

    private void onClickChooseImageButtonAction(Desk desk, Stage primaryStage, Button chooseImageButton) {
        if (timeline != null) {
            timeline.stop();
        }
        chooseImageButton.setOnAction(e -> {
            String absoluteImagePath = new StringBuilder()
                    .append(Paths.get(IMAGE_PATH).toAbsolutePath())
                    .append(File.separator)
                    .append(DEFAULT_IMAGE_NAME)
                    .append(".")
                    .append(IMAGE_FORMAT).toString();
            System.out.println(absoluteImagePath);
            desk.getChildren().removeAll(puzzlePieces);
            root.getChildren().clear();
            imageService.uploadImage(primaryStage, chooseImageButton, absoluteImagePath, IMAGE_FORMAT);
            makePuzzle(desk, primaryStage);
            timeline.playFromStart();
        });
    }

    private void onClickSolveButtonAction(List<PuzzlePiece> puzzlePieces, Button solveButton) {
        solveButton.setOnAction(actionEvent -> {
            text.setText("AutoSolving not ready yet");
            text.setTranslateX(70);
        });
    }

    private void onClickMixButtonAction(Desk desk, List<PuzzlePiece> puzzlePieces, Button shuffleButton) {
        shuffleButton.setOnAction(actionEvent -> {
            root.getChildren().removeAll(tiles);
            if (timeline != null) {
                timeline.stop();
            }
            text.setText("Mixed!");
            text.setTranslateX(80);

            timeline = new Timeline();
            for (PuzzlePiece PuzzlePiece : puzzlePieces) {
                PuzzlePiece.setActive();
                PuzzlePiece.setVisible(true);

                double mixX = Math.random() *
                        (desk.getWidth() - puzzlePieceWidth + 30f) - PuzzlePiece.getX();
                double mixY = Math.random() *
                        (desk.getHeight() - puzzlePieceHeight + 30f) - PuzzlePiece.getY();

                timeline.getKeyFrames().add(
                        new KeyFrame(Duration.seconds(0.4),
                                new KeyValue(PuzzlePiece.translateXProperty(), mixX),
                                new KeyValue(PuzzlePiece.translateYProperty(), mixY)));
            }
            timeline.playFromStart();
        });
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        init(stage);
        stage.show();
    }
}
