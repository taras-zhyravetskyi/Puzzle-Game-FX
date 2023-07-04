package com.example.puzzlefx.Model;

import com.example.puzzlefx.PuzzleApplication;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class PuzzlePiece extends Parent {
    private static final double ERROR_IN_LOCATION = 10;
    private static int piecesInPlaceCounter;
    public final double pieceWidth;
    public final double pieceHeight;
    private final double x;
    private final double y;
    private final double deskWidth;
    private final double deskHeight;
    private double startDragX;
    private double startDragY;
    private Shape pieceShape;
    private Shape pieceClip;
    private Point2D dragAnchor;

    static {
        piecesInPlaceCounter = 0;
    }

    public PuzzlePiece(Image image, double x, double y, double deskWidth,
                       double deskHeight, double pieceWidth, double pieceHeight) {
        this.x = x;
        this.y = y;
        this.deskWidth = deskWidth;
        this.deskHeight = deskHeight;
        this.pieceWidth = pieceWidth;
        this.pieceHeight = pieceHeight;

        configureClip();
        configureShape();
        configImage(image);
    }

    public void configureClip() {
        pieceClip = createPiece();
        pieceClip.setFill(Color.WHITE);
        pieceClip.setStroke(null);
    }

    public void configureShape() {
        pieceShape = createPiece();
        pieceShape.setFill(null);
        pieceShape.setStroke(Color.BLACK);
    }

    private void configImage(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setClip(pieceClip);
        setFocusTraversable(true);
        getChildren().addAll(imageView, pieceShape);
        setCache(true);
        setInactive();

        setUpMouseEvents();
    }


    private Shape createPiece() {
        Shape shape = new Rectangle(pieceWidth, pieceHeight);
        shape.setTranslateX(x);
        shape.setTranslateY(y);
        return shape;
    }

    private void setUpMouseEvents() {
        setOnMousePressed(me -> {
            toFront();
            startDragX = getTranslateX();
            startDragY = getTranslateY();
            dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());
        });

        setOnMouseReleased(me -> {
            if (-ERROR_IN_LOCATION <  getTranslateX()  && getTranslateX() < ERROR_IN_LOCATION &&
                -ERROR_IN_LOCATION < getTranslateY() && getTranslateY() < ERROR_IN_LOCATION &&
                getRotate() == 0) {
                setTranslateX(0);
                setTranslateY(0);
                setInactive();
                piecesInPlaceCounter++;
                if (PuzzleApplication.numberOfPuzzles == piecesInPlaceCounter) {
                    piecesInPlaceCounter = 0;
                    PuzzleApplication.text.setText("You win!!");
                }
            }
        });

        setOnScroll(m -> {
            setRotate(getRotate() + 90);
            if (getRotate() >= 360) {
                setRotate(0);
            }
        });

        setOnMouseDragged(me -> {
            double newTranslateX = startDragX + me.getSceneX() - dragAnchor.getX();
            double newTranslateY = startDragY + me.getSceneY() - dragAnchor.getY();

            double minTranslateX = -45f - x;
            double maxTranslateX = (deskWidth - pieceWidth + 50f) - x;
            double minTranslateY = -30f - y;
            double maxTranslateY = (deskHeight - pieceHeight + 70f) - y;

            newTranslateX = Math.max(minTranslateX, Math.min(newTranslateX, maxTranslateX));
            newTranslateY = Math.max(minTranslateY, Math.min(newTranslateY, maxTranslateY));

            setTranslateX(newTranslateX);
            setTranslateY(newTranslateY);
        });
    }

    public void setActive() {
        setDisable(false);
        setEffect(new DropShadow());
        toFront();
    }

    public void setInactive() {
        setEffect(null);
        setDisable(true);
        toBack();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
