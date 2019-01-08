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
        if(hasBomb()){
            setState(State.BOMBED);
        }
        else {
            setState(State.values()[bombsAround]);
        }
    }

    void mark() {
        if (isOpen()) {
            return;
        }
        if (isFlagged()) {
            setState(State.INFORM);
            return;
        } else if (isInformed()) {
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

    boolean isClosed(){
        return state.equals(State.CLOSED);
    }

    boolean isFlagged(){
        return state.equals(State.FLAGGED);
    }

    boolean isInformed(){
        return state.equals(State.INFORM);
    }

    private boolean isOpen() {
        return isOpen;
    }

    boolean hasBomb() {
        return hasBomb;
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
}
