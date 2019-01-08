package minesweeper.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BoardTest {

    private static Board board;

    @BeforeAll
    static void setUp() {
        board = new Board(Level.EASY);
    }

    @Test
    void shouldLoadNeighboursOfCell() {
        Cell cell = board.getAllCells().get(2);
        List<Cell> list = board.getNeighbours(cell);

        int expected = (int) list.stream().filter(Cell::hasBomb).count();

        assertThat(cell.getBombsAround(), is(expected));
    }

    @Test
    void shouldntAllowToLoadNeighboursOfCellWhichDoesntExist() {
        Cell cell = new Cell(0, 0, true);
        cell.incBombsAround();
        cell.incBombsAround();
        cell.incBombsAround();
        cell.incBombsAround();

        assertThrows(IllegalArgumentException.class, () -> board.getNeighbours(cell));
    }

    @Test
    void shouldLoadMatrixWithTenBombs() {
        List<Cell> cellslList = board.getAllCells();

        int expected = (int) cellslList.stream().filter(Cell::hasBomb).count();

        assertThat(10, is(expected));
    }
}