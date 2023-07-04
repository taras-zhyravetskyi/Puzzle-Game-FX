package com.example.puzzlefx.Model;

import java.awt.image.BufferedImage;

public class PuzzlePieceForAutoSolving {
    BufferedImage image;
    RGB leftEdge;
    RGB rightEdge;
    RGB topEdge;
    RGB bottomEdge;

    public PuzzlePieceForAutoSolving() {
    }

    public PuzzlePieceForAutoSolving(BufferedImage image, RGB leftEdge, RGB rightEdge, RGB topEdge, RGB bottomEdge) {
        this.image = image;
        this.leftEdge = leftEdge;
        this.rightEdge = rightEdge;
        this.topEdge = topEdge;
        this.bottomEdge = bottomEdge;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public RGB getLeftEdge() {
        return leftEdge;
    }

    public void setLeftEdge(RGB leftEdge) {
        this.leftEdge = leftEdge;
    }

    public RGB getRightEdge() {
        return rightEdge;
    }

    public void setRightEdge(RGB rightEdge) {
        this.rightEdge = rightEdge;
    }

    public RGB getTopEdge() {
        return topEdge;
    }

    public void setTopEdge(RGB topEdge) {
        this.topEdge = topEdge;
    }

    public RGB getBottomEdge() {
        return bottomEdge;
    }

    public void setBottomEdge(RGB bottomEdge) {
        this.bottomEdge = bottomEdge;
    }
}
