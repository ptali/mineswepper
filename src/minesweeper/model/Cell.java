package minesweeper.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

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
        setState(State.ZERO);
        while (state.getNumber() < bombsAround) {
            setState(state.nextNumberState());
        }
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
