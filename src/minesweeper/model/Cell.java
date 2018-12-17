package minesweeper.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

public class Cell {

    private final int x;
    private final int y;
    private final boolean hasBomb;
    private boolean isOpen;
    private int bombsAround;
    private State state;
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

    Cell(int x, int y, boolean hasBomb) {
        this.x = x;
        this.y = y;
        this.hasBomb = hasBomb;
        this.state = State.CLOSED;
        this.isOpen = false;
    }

    void open() {
        isOpen = true;
        setState(State.values()[bombsAround]);
    }

    void mark() {
        if (isOpen()) {
            return;
        }
        if (state.equals(State.FLAGGED)) {
            setState(State.INFORM);
            return;
        } else if (state.equals(State.INFORM)) {
            setState(State.CLOSED);
            return;
        }
        setState(State.FLAGGED);
    }

    void incBombsAround() {
        bombsAround++;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener(listener);
    }

    void setState(State newState) {
        changes.firePropertyChange(newState.name(), state, newState);
        this.state = newState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return x == cell.x &&
                y == cell.y &&
                hasBomb == cell.hasBomb &&
                bombsAround == cell.bombsAround &&
                state == cell.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, hasBomb, bombsAround, state);
    }

    public State getState() {
        return state;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    int getBombsAround() {
        return bombsAround;
    }

    boolean ifHasBomb() {
        return hasBomb;
    }

    private boolean isOpen() {
        return isOpen;
    }
}
