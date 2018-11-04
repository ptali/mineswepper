package minesweeper.model;

public class Game {
    private final Board board;
    private GameState gameState;
    private final Level level;
    private int openedCells;
    private final int sizeX;
    private final int sizeY;
    private final int numberOfBombs;
    private int timeCounter;
    private int bombsCounter;

    public Game() {
        this(Level.EASY);
    }

    public Game(Level level) {
        this.level = level;
        gameState = GameState.CLOSED;
        sizeX = level.getSIZE_X();
        sizeY = level.getSIZE_Y();
        board = new Board(level);
        numberOfBombs = level.getBOMBS_COUNT();
        bombsCounter = numberOfBombs;
    }

    public void start() {
        gameState = GameState.PLAY;
    }

    public void markCell(Cell cell) {
        cell.mark();
        if (cell.getState().equals(State.FLAGGED)) {
            reduceBombsCounter();
        } else if (cell.getState().equals(State.CLOSED)) {
            increaseBombsCounter();
        }
        checkWinn();
    }

    public void openCell(Cell cell) {
        switch (cell.getState()) {
            case OPENED:
                break;
            case FLAGGED:
                break;
            case INFORM:
            case CLOSED:
                if (cell.ifHasBomb()) {
                    openBombs(cell);
                    break;
                }
                if (cell.getBombsAround() == 0) {
                    openCellsAroundZero(cell);
                    break;
                }
                cell.open();
                openedCells++;
        }
        checkWinn();
    }

    private void openCellsAroundZero(Cell cell) {
        cell.open();
        openedCells++;
        for (Cell around : board.getNeighbours(cell)) {
            openCell(around);
        }
    }

    private void openBombs(Cell cell) {
        cell.setState(State.BOMBED);
        for (Cell around : board.getAllCells()) {
            if (around == cell) {
                continue;
            }
            if (around.ifHasBomb()) {
                around.setState(State.BOMB);
            } else {
                if (around.getState() == State.FLAGGED || around.getState() == State.INFORM) {
                    around.setState(State.NOBOMB);
                }
            }
        }
        gameState = GameState.BOMB;
    }

    private void setFlaggedToLastClosedCells() {
        for (Cell cell : board.getAllCells()) {
            if (cell.getState() == State.CLOSED) {
                cell.mark();
            }
        }
    }

    private void checkWinn() {
        int size = sizeX * sizeY;
        if (openedCells == (size - numberOfBombs)) {
            gameState = GameState.WIN;
            setFlaggedToLastClosedCells();
        }
    }

    private void reduceBombsCounter() {
        if (bombsCounter == 0) {
            return;
        }
        bombsCounter--;
    }

    private void increaseBombsCounter() {
        if (bombsCounter == numberOfBombs) {
            return;
        }
        bombsCounter++;
    }

    public void addSecond() {
        timeCounter++;
    }

    public Level getLevel() {
        return level;
    }

    public Board getBoard() {
        return board;
    }

    public GameState getState() {
        return gameState;
    }

    public int getTime() {
        return timeCounter;
    }

    public int getBombsCounter() {
        return bombsCounter;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }
}
