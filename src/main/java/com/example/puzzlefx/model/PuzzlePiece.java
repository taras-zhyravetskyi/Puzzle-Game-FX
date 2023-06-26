package com.example.puzzlefx.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PuzzlePiece {
    private String imagePath;
    private int currentX;
    private int currentY;
}
