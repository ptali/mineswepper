package minesweeper.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Board {

    private final int SIZE_X;
    private final int SIZE_Y;
    private Cell[][] matrix;
    private final Random random;
    private int numberOfBombs;
    private int bombCounter;
    private List<Cell> allCells;

    Board(Level level) {
        this(level.getSIZE_X(), level.getSIZE_Y());
        this.bombCounter = 0;
        this.numberOfBombs = level.getBOMBS_COUNT();

        loadBoard();
        loadNumberOfBombsAround();
    }

    private Board(int x, int y) {
        this.random = new Random();
        matrix = new Cell[x][y];
        SIZE_X = x;
        SIZE_Y = y;
    }

    private void loadBoard() {
        int rndX;
        int rndY;
        allCells = new ArrayList<>();

        while (bombCounter < numberOfBombs) {
            rndX = random.nextInt(SIZE_X);
            rndY = random.nextInt(SIZE_Y);
            if (matrix[rndX][rndY] == null) {
                matrix[rndX][rndY] = new Cell(rndX, rndY, true);
                bombCounter++;
            }
        }

        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                if (matrix[x][y] == null) {
                    matrix[x][y] = new Cell(x, y, false);
                }
                allCells.add(matrix[x][y]);
            }
        }
    }

    private void loadNumberOfBombsAround() {
        for (Cell cell : allCells) {
            loadNeighbours(cell);
        }
    }

    private boolean isInRange(int x, int y) {
        return x >= 0 && y >= 0 &&
                x < SIZE_X && y < SIZE_Y;
    }

    private void loadNeighbours(Cell cell) {
        int x = cell.getX();
        int y = cell.getY();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (isInRange(x + i, y + j)) {
                    if (matrix[x + i][y + j].ifHasBomb()) {
                        cell.incBombsAround();
                    }
                }
            }
        }
    }

    List<Cell> getNeighbours(Cell cell) {
        List<Cell> neighbours = new ArrayList<>();
        int x = cell.getX();
        int y = cell.getY();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if ((x + i) >= 0 && (y + j) >= 0 && (x + i) < SIZE_X && (y + j) < SIZE_Y) {
                    neighbours.add(matrix[x + i][y + j]);
                }
            }
        }
        return Collections.unmodifiableList(neighbours);
    }

    public Cell[][] getMatrix() {
        return matrix;
    }

    List<Cell> getAllCells() {
        return Collections.unmodifiableList(allCells);
    }
}
