package com.example.puzzlefx.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Puzzle {
    private List<PuzzlePiece> pieces;

    public void addPiece(PuzzlePiece piece) {
        pieces.add(piece);
    }

    public void removePiece(PuzzlePiece piece) {
        pieces.remove(piece);
    }

    public void shufflePieces() {
        Collections.shuffle(pieces);
    }

    public boolean checkSolution() {
        int size = pieces.size();
        for (int i = 0; i < size - 1; i++) {
            PuzzlePiece currentPiece = pieces.get(i);
            PuzzlePiece nextPiece = pieces.get(i + 1);
            if (!areAdjacent(currentPiece, nextPiece)) {
                return false;
            }
        }
        return true;
    }

    private boolean areAdjacent(PuzzlePiece piece1, PuzzlePiece piece2) {
        int xDiff = Math.abs(piece1.getCurrentX() - piece2.getCurrentX());
        int yDiff = Math.abs(piece1.getCurrentY() - piece2.getCurrentY());
        return (xDiff == 100 && yDiff == 0) || (xDiff == 0 && yDiff == 100);
    }
}
