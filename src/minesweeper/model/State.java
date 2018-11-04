package minesweeper.model;

public enum State {
    ZERO,
    NUM1,
    NUM2,
    NUM3,
    NUM4,
    NUM5,
    NUM6,
    NUM7,
    NUM8,
    BOMB,

    OPENED,
    CLOSED,
    FLAGGED,
    BOMBED,
    NOBOMB,
    INFORM;

    public Object image;

    State nextNumberState() {
        return State.values()[this.ordinal() + 1];
    }

    public int getNumber() {
        int nr = ordinal();
        if (nr >= State.ZERO.ordinal() && nr <= State.NUM8.ordinal()) {
            return nr;
        }
        return -1;
    }
}
