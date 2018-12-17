package minesweeper.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

class GameTest {

    @Mock
    private Board board;
    @Mock
    private Cell cell1;
    @Mock
    private Cell cell2;
    @Mock
    private Cell cell3;
    @Mock
    private Cell cell4;

    private Game game;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        game = new Game(board, Level.EASY);

        when(board.getNeighbours(cell1)).thenReturn(Arrays.asList(cell2, cell3, cell4));
        when(board.getAllCells()).thenReturn(Arrays.asList(cell1, cell2, cell3, cell4));
        when(cell1.getState()).thenReturn(State.CLOSED);
        when(cell2.getState()).thenReturn(State.CLOSED);
        when(cell3.getState()).thenReturn(State.CLOSED);
        when(cell4.getState()).thenReturn(State.CLOSED);
    }

    @Test
    void shouldAllowToMarkCell() {
        when(cell1.getState()).thenReturn(State.FLAGGED);
        game.markCell(cell1);

        verify(cell1, times(1)).mark();
        assertThat(game.getBombsCounter(), is(9));
    }

    @Test
    void shouldOpenAllCellsAroundZero() {
        when(cell1.getBombsAround()).thenReturn(0);
        game.openCell(cell1);

        verify(cell1, times(1)).open();
        verify(cell2, times(1)).open();
        verify(cell3, times(1)).open();
        verify(cell4, times(1)).open();
    }

    @Test
    void shouldOpenBombedCellAndCellsAroundWithBombAndWrongMarkedBomb() {
        when(cell1.ifHasBomb()).thenReturn(true);
        when(cell2.ifHasBomb()).thenReturn(true);
        when(cell3.getState()).thenReturn(State.FLAGGED);

        game.openCell(cell1);

        verify(cell1, times(1)).setState(State.BOMBED);
        verify(cell2, times(1)).setState(State.BOMB);
        verify(cell3, times(1)).setState(State.NOBOMB);
        verify(cell4, never()).setState(any(State.class));
    }
}