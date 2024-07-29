package com.jkurapati.sudoku.engine;

public interface Solver {
    boolean solve();

    void setBoardGenerator(BoardGenerator boardGenerator);

    void setBoardChangeListener(BoardChangeListener listener);
}
