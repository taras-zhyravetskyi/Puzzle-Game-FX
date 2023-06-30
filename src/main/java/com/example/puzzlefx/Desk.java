package com.example.puzzlefx;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class Desk extends Pane {
    private static final int LINE_MARGIN = 0;
    private static final double DESK_INCREASE_COEF = 1.1;
    private double deskWidth;
    private double deskHeight;
    public double getDeskWidth() {
        return deskWidth;
    }
    public double getCellHeight() {
        return deskHeight;
    }

    private Desk(double imageWidth, double imageHeight) {
        setStyle("-fx-background-color: #7e7878;");

        deskWidth = imageWidth * DESK_INCREASE_COEF;
        deskHeight = imageHeight * DESK_INCREASE_COEF;

        setPrefSize(deskWidth,deskHeight);
        setMaxSize(deskWidth, deskHeight);
        autosize();
    }

    public static Desk createPuzzleDesk(int numOfColumns, int numOfRows, double pieceWidth, double pieceHeight) {
        Desk desk = new Desk(numOfColumns * pieceWidth, numOfRows * pieceHeight);

        Path grid = new Path();
        grid.setStroke(Color.rgb(140, 40, 180));
        desk.getChildren().add(grid);

        //   Draw vertical lines
        for (int col = 0; col < numOfColumns - 1; col++) {
            grid.getElements().addAll(
                    new MoveTo(pieceWidth + pieceWidth * col, LINE_MARGIN),
                    new LineTo(pieceWidth + pieceWidth * col, pieceHeight * numOfRows - LINE_MARGIN)
            );
        }

        //   Draw horizontal lines
        for (int row = 0; row < numOfRows - 1; row++) {
            grid.getElements().addAll(
                    new MoveTo(LINE_MARGIN, pieceHeight + pieceHeight * row),
                    new LineTo(pieceWidth * numOfColumns - LINE_MARGIN, pieceHeight + pieceHeight * row)
            );
        }
        return desk;
    }
}
