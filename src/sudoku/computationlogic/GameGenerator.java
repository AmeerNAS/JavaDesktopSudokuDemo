package sudoku.computationlogic;

import sudoku.problemdomain.Coordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static sudoku.problemdomain.SudokuGame.GRID_BOUNDRY;

public class GameGenerator {
    public static int[][] getNewGameGrid() {
        return unsolveGame(getSolvedGame());
    }

    private static int[][] unsolveGame(int[][] solvedGame) {
        Random random = new Random(System.currentTimeMillis());

        boolean solvable = false;
        int[][] solvableArray = new int[GRID_BOUNDRY][GRID_BOUNDRY];

        while(solvable == false) {
            SudokuUtilities.copySudokuArrayValues(solvedGame, solvableArray);

            int index = 0;

            while (index < 40) {
                int xCoordinates = random.nextInt(GRID_BOUNDRY);
                int yCoordinates = random.nextInt(GRID_BOUNDRY);

                if (solvableArray[xCoordinates][yCoordinates] !=0) {
                    solvableArray[xCoordinates][yCoordinates] =0;
                    index++;
                }
            }

            int[][] toBeSolved = new int[GRID_BOUNDRY][GRID_BOUNDRY];
            SudokuUtilities.copySudokuArrayValues(solvableArray, toBeSolved);

            solvable = SudokuSolver.puzzleIsSolvable(toBeSolved);


        }

        return solvableArray;
    }

    private static int[][] getSolvedGame() {
        Random random = new Random(System.currentTimeMillis());
        int[][] newGrid = new int[GRID_BOUNDRY][GRID_BOUNDRY];

        for (int value = 1; value < GRID_BOUNDRY; value++) {
            int allocations = 0;
            int interupt = 0;

            List<Coordinates> allocTracker = new ArrayList<>();

            int attempts = 0;

            while (allocations < GRID_BOUNDRY) {
                if (interupt > 200) {
                    allocTracker.forEach(coord -> {
                        newGrid[coord.getX()][coord.getY()] = 0;
                    });

                    interupt = 0;
                    allocations = 0;
                    allocTracker.clear();
                    attempts++;

                    if (attempts > 500) {
                        clearArray(newGrid);
                        attempts = 0;
                        value = 1;
                    }
                }

                int xCoordinate = random.nextInt(GRID_BOUNDRY);
                int yCoordinate = random.nextInt(GRID_BOUNDRY);

                if (newGrid[xCoordinate][yCoordinate] ==0){
                    newGrid[xCoordinate][yCoordinate] = value;

                    if (GameLogic.sudokuIsInvalid(newGrid)) {
                        newGrid[xCoordinate][yCoordinate] =0;
                        interupt++;
                    } else {
                        allocTracker.add(new Coordinates(xCoordinate, yCoordinate));
                        allocations++;
                    }
                }

            }

        }

        return newGrid;
    }

    private static void clearArray(int[][] newGrid) {
        for (int xIndex = 0; xIndex < GRID_BOUNDRY; xIndex++) {
            for (int yIndex = 0; yIndex < GRID_BOUNDRY; yIndex++) {
                newGrid[xIndex][yIndex] = 0;
            }
        }
    }
}
