package minesweeper.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

class CellTest {

    private static Cell cell;

    @BeforeAll
    static void setUp() {
        cell = new Cell(0, 0, false);
    }

    @AfterEach
    void cleanUp() {
        cell.setState(State.CLOSED);
    }

    @Test
    void shouldAllowToMarkedCellWithAFlag() {
        cell.mark();

        State expected = State.FLAGGED;

        assertThat(cell.getState(), is(expected));
    }

    @Test
    void shouldAllowToMarkedCellWithAQuestionMark() {
        cell.mark();
        cell.mark();

        State expected = State.INFORM;

        assertThat(cell.getState(), is(expected));
    }

    @Test
    void shouldAllowToUndoMarkCell() {
        cell.mark();
        cell.mark();
        cell.mark();

        State expected = State.CLOSED;

        assertThat(cell.getState(), is(expected));
    }

    @Test
    void shouldntAllowToMarkCellWhenIsOpen() {
        cell.open();
        cell.mark();

        assertThat(cell.getState(), is(not(State.FLAGGED)));
    }

    @Test
    void shouldStoreNumberOfBombsAround() {
        cell.incBombsAround();
        cell.open();

        State expected = State.NUM1;

        assertThat(cell.getState(), is(expected));
    }
}