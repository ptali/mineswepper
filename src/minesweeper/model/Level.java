package minesweeper.model;

public enum Level {
    EASY(8, 8, 10),
    MEDIUM(16, 16, 40),
    HARD(31, 16, 90);

    private final int SIZE_X;
    private final int SIZE_Y;
    private final int BOMBS_COUNT;

    Level(int x, int y, int bombs) {
        this.SIZE_X = x;
        this.SIZE_Y = y;
        this.BOMBS_COUNT = bombs;
    }

    public int getSIZE_X() {
        return SIZE_X;
    }

    public int getSIZE_Y() {
        return SIZE_Y;
    }

    public int getBOMBS_COUNT() {
        return BOMBS_COUNT;
    }
}
