# Puzzle-Game-FX

This is the PuzzleFX project, which allows you to play a puzzle game based on JavaFX.

## Project Description

Puzzle FX is a game where you can solve puzzles by dragging and dropping puzzle pieces into their correct positions to restore the original image. 

## Running the Project

To run the Puzzle FX application, please follow these steps:

1. Ensure you have Java JDK (version 17 or above) installed on your machine.
2. Clone this repository to your local machine.
3. Run app in your JDK

## Automatic Solving Algorithm

The PuzzleFX's automatic solving algorithm operates by analyzing and comparing the color of puzzle piece edges. The algorithm begins with the top-left piece, identified as having the greatest color difference on its top and left edges, then sequentially places the remaining pieces based on the minimal color difference with adjacent pieces. Ultimately, the algorithm stitches the correctly placed pieces together to form the final solved image

Enjoy playing Puzzle FX and have fun solving puzzles!
