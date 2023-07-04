package com.example.puzzlefx.service;

import com.example.puzzlefx.Model.PuzzlePieceForAutoSolving;
import com.example.puzzlefx.Model.RGB;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class AutoSolvingAlgorithm {
    public BufferedImage solvePuzzle(String path, int imageWidth, int imageHeight) {
        List<PuzzlePieceForAutoSolving> puzzlePieces = readDirectory(path);
        int numberOfColumns = imageWidth /  puzzlePieces.get(0).getImage().getWidth();
        int numberOfRows = imageHeight / puzzlePieces.get(0).getImage().getHeight();

        PuzzlePieceForAutoSolving[][] puzzle = new PuzzlePieceForAutoSolving[numberOfRows][numberOfColumns];
        PuzzlePieceForAutoSolving currentPiece = findTopLeftPiece(puzzlePieces);
        puzzlePieces.remove(currentPiece);
        puzzle[0][0] = currentPiece;

        puzzle = findOtherPieces(numberOfColumns, numberOfRows, puzzlePieces, currentPiece, puzzle);
        return mergeImages(puzzle);
    }

    private BufferedImage mergeImages(PuzzlePieceForAutoSolving[][] puzzlePieces) {
        int rows = puzzlePieces.length;
        int columns = puzzlePieces[0].length;

        int pieceWidth = puzzlePieces[0][0].getImage().getWidth();
        int pieceHeight = puzzlePieces[0][0].getImage().getHeight();

        int totalWidth = pieceWidth * columns;
        int totalHeight = pieceHeight * rows;

        BufferedImage mergedImage = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = mergedImage.createGraphics();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                BufferedImage piece = puzzlePieces[i][j].getImage();
                graphics.drawImage(piece, j * pieceWidth, i * pieceHeight, null);
            }
        }
        graphics.dispose();
        return mergedImage;
    }

    private List<PuzzlePieceForAutoSolving> readDirectory(String path) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        List<PuzzlePieceForAutoSolving> puzzlePieces = new ArrayList<>();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                try {
                    BufferedImage img = ImageIO.read(file);
                    RGB leftEdge = getEdgeColor(img, Edge.LEFT);
                    RGB rightEdge = getEdgeColor(img, Edge.RIGHT);
                    RGB topEdge = getEdgeColor(img, Edge.TOP);
                    RGB bottomEdge = getEdgeColor(img, Edge.BOTTOM);

                    PuzzlePieceForAutoSolving puzzlePiece = new PuzzlePieceForAutoSolving(img, leftEdge, rightEdge, topEdge, bottomEdge);
                    puzzlePieces.add(puzzlePiece);
                } catch (IOException e) {
                    throw new ImagingOpException("Can`t read piece " + file.getPath());
                }
            }
        }
        return puzzlePieces;
    }

    RGB getEdgeColor(BufferedImage image, Edge edge) {
        int width = image.getWidth();
        int height = image.getHeight();
        RGB rgb = new RGB();

        int[] range;
        int x = 0;
        int y = 0;

        switch (edge) {
            case LEFT:
                range = IntStream.range(0, height).toArray();
                x = 0;
                break;
            case RIGHT:
                range = IntStream.range(0, height).toArray();
                x = width - 1;
                break;
            case TOP:
                range = IntStream.range(0, width).toArray();
                y = 0;
                break;
            case BOTTOM:
                range = IntStream.range(0, width).toArray();
                y = height - 1;
                break;
            default:
                throw new IllegalArgumentException("Invalid edge: " + edge);
        }
        for (int i : range) {
            int dx = edge == Edge.LEFT || edge == Edge.RIGHT ? x : i;
            int dy = edge == Edge.TOP || edge == Edge.BOTTOM ? y : i;
            int color = image.getRGB(dx, dy);
            int red = (color >> 16) & 0xff;
            int green = (color >> 8) & 0xff;
            int blue = color & 0xff;
            rgb.addColor(red, green, blue);
        };
        return rgb;
    }

    int calculateRGBDiff(RGB rgb1, RGB rgb2) {
        int totalDiff = 0;
        for (int i = 0; i < rgb1.getRed().size(); i++) {
            int diffRed = Math.abs(rgb1.getRed().get(i) - rgb2.getRed().get(i));
            int diffGreen = Math.abs(rgb1.getGreen().get(i) - rgb2.getGreen().get(i));
            int diffBlue = Math.abs(rgb1.getBlue().get(i) - rgb2.getBlue().get(i));
            totalDiff += diffRed + diffGreen + diffBlue;
        }
        return totalDiff;
    }

    PuzzlePieceForAutoSolving findTopLeftPiece(List<PuzzlePieceForAutoSolving> pieces) {
        PuzzlePieceForAutoSolving topLeftPiece = null;
        double maxDiff = 0;
        List<Double> difs = new ArrayList<>();

        for (PuzzlePieceForAutoSolving piece : pieces) {
            int diffTop = pieces.stream().filter(p -> p != piece)
                    .mapToInt(p -> calculateRGBDiff(piece.getTopEdge(), p.getBottomEdge()))
                    .min().orElse(Integer.MAX_VALUE);
            int diffLeft = pieces.stream().filter(p -> p != piece)
                    .mapToInt(p -> calculateRGBDiff(piece.getLeftEdge(), p.getRightEdge()))
                    .min().orElse(Integer.MAX_VALUE);

            double totalDiff = diffTop + diffLeft;
            difs.add(totalDiff);
            if (totalDiff > maxDiff) {
                maxDiff = totalDiff;
                topLeftPiece = piece;
            }
        }
        return topLeftPiece;
    }

    PuzzlePieceForAutoSolving[][] findOtherPieces(int numberOfColumns, int numberOfRows,
                    List<PuzzlePieceForAutoSolving> puzzlePieces, PuzzlePieceForAutoSolving currentPiece,
                    PuzzlePieceForAutoSolving[][] puzzle) {
        // виконуємо для кожного місця в матриці пазла

        for (int j = 1; j < numberOfColumns; j++) {
            int minDif = Integer.MAX_VALUE;
            int pieceIndex = 0;
            for (int k = 0; k < puzzlePieces.size(); k++) {
                PuzzlePieceForAutoSolving piece = puzzlePieces.get(k);
                int currentDif = calculateRGBDiff(puzzle[0][j - 1].getRightEdge(), piece.getLeftEdge());
                if (currentDif < minDif) {
                    minDif = currentDif;
                    pieceIndex = k;
                }
            }
            puzzle[0][j] = puzzlePieces.remove(pieceIndex);
        }

        for (int i = 1; i < numberOfRows; i++) {
            int minDif = Integer.MAX_VALUE;
            int pieceIndex = 0;
            for (int k = 0; k < puzzlePieces.size(); k++) {
                PuzzlePieceForAutoSolving piece = puzzlePieces.get(k);
                int currentDif = calculateRGBDiff(puzzle[i - 1][0].getBottomEdge(), piece.getTopEdge());
                if (currentDif < minDif) {
                        minDif = currentDif;
                        pieceIndex = k;
                }
            }
            puzzle[i][0] =  puzzlePieces.remove(pieceIndex);
        }
        for (int i = 1; i < numberOfRows; i++) {
            for (int j = 1; j < numberOfColumns; j++) {
                int minDif = Integer.MAX_VALUE;
                int pieceIndex = 0;
                for (int k = 0; k < puzzlePieces.size(); k++) {
                    PuzzlePieceForAutoSolving piece = puzzlePieces.get(k);
                    int currentDif = calculateRGBDiff(puzzle[i - 1][j].getBottomEdge(), piece.getTopEdge()) +
                            + calculateRGBDiff(puzzle[i][j - 1].getRightEdge(), piece.getLeftEdge());
                    if (currentDif < minDif) {
                        minDif = currentDif;
                        pieceIndex = k;
                    }
                }
                puzzle[i][j] = puzzlePieces.remove(pieceIndex);
            }
        }
        return puzzle;
    }

    enum Edge {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM
    }
}
