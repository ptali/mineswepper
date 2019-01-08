package minesweeper.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Board {

    private final int SIZE_X;
    private final int SIZE_Y;
    private Cell[][] matrix;
    private Random random;
    private int numberOfBombs;
    private int bombCounter;
    private List<Cell> allCells;

    Board(Level level) {
        this(level.getSIZE_X(), level.getSIZE_Y());
        this.bombCounter = 0;
        this.numberOfBombs = level.getBOMBS_COUNT();

        loadBoard();
        loadNeighbours();
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

    private void loadNeighbours() {
        for (Cell cell : allCells) {
            loadNumberOfBombsAround(cell);
        }
    }

    private void loadNumberOfBombsAround(Cell cell) {
        int x = cell.getX();
        int y = cell.getY();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (isInRange(x + i, y + j)) {
                    if (matrix[x + i][y + j].hasBomb()) {
                        cell.incBombsAround();
                    }
                }
            }
        }
    }

    private boolean isInRange(int x, int y) {
        return x >= 0 && y >= 0 &&
                x < SIZE_X && y < SIZE_Y;
    }

    List<Cell> getNeighbours(Cell cell) {
        List<Cell> neighbours = new ArrayList<>();
        int x = cell.getX();
        int y = cell.getY();

        if (!matrix[x][y].equals(cell)) {
            throw new IllegalArgumentException("Can't find this cell in the matrix.");
        }

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (isInRange(x + i, y + j)) {
                    neighbours.add(matrix[x + i][y + j]);
                }
            }
        }
        return Collections.unmodifiableList(neighbours);
    }

    public Cell getCell(int x, int y) {
        if (isInRange(x, y)) {
            return matrix[x][y];
        }
        return null;
    }

    List<Cell> getAllCells() {
        return Collections.unmodifiableList(allCells);
    }
}
