package com.example.puzzlefx.service;

import com.example.puzzlefx.PuzzlePiece;

import java.util.List;

public interface PuzzleService {
    List<PuzzlePiece> solve();

    List<PuzzlePiece> shuffle();
}
