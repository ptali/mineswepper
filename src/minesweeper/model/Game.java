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
        this(new Board(Level.EASY), Level.EASY);
    }

    Game(Board board, Level level) {
        this.board = board;
        this.level = level;
        gameState = GameState.CLOSED;
        sizeX = level.getSIZE_X();
        sizeY = level.getSIZE_Y();
        numberOfBombs = level.getBOMBS_COUNT();
        bombsCounter = numberOfBombs;
    }

    public Game(Level level) {
        this(new Board(level), level);
    }

    public void start() {
        gameState = GameState.PLAY;
    }

    public void markCell(Cell cell) {
        cell.mark();
        if (cell.isFlagged()) {
            reduceBombsCounter();
        } else if (cell.isClosed()) {
            increaseBombsCounter();
        }
    }

    public void openCell(Cell cell) {
        if (cell.isClosed() || cell.isInformed()) {
            if (cell.hasBomb()) {
                openBombs(cell);
                return;
            }
            if (cell.getBombsAround() == 0) {
                openCellsAroundZero(cell);
                return;
            }
            cell.open();
            openedCells++;
        }
    }

    private void openBombs(Cell cell) {
        cell.open();
        for (Cell around : board.getAllCells()) {
            if (around == cell) {
                continue;
            }
            if (around.hasBomb()) {
                around.setState(State.BOMB);
            } else {
                if (around.isFlagged() || around.isInformed()) {
                    around.setState(State.NOBOMB);
                }
            }
        }
        gameState = GameState.BOMB;
    }

    private void openCellsAroundZero(Cell cell) {
        cell.open();
        openedCells++;
        for (Cell around : board.getNeighbours(cell)) {
            openCell(around);
        }
    }

    public void checkWinn() {
        int size = sizeX * sizeY;
        if (openedCells == (size - numberOfBombs)) {
            gameState = GameState.WIN;
            markLastCells();
        }
    }

    private void markLastCells() {
        for (Cell cell : board.getAllCells()) {
            if (cell.isClosed()) {
                cell.mark();
            }
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

    public boolean isStarted() {
        return !gameState.equals(GameState.CLOSED);
    }

    public boolean isFinished() {
        return isBombed() || isWin();
    }

    public boolean isBombed() {
        return gameState.equals(GameState.BOMB);
    }

    public boolean isWin() {
        return gameState.equals(GameState.WIN);
    }

    public Level getLevel() {
        return level;
    }

    public Board getBoard() {
        return board;
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
