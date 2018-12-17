package minesweeper.model;

import javafx.scene.image.Image;
import minesweeper.controller.Controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CellListener implements PropertyChangeListener {

    private Controller controller;

    public CellListener(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        Cell cell = (Cell) evt.getSource();
        String cellState = evt.getPropertyName();
        State state = State.valueOf(cellState);

        controller.getImageView()[cell.getX()][cell.getY()].setImage((Image) state.image);
    }
}
