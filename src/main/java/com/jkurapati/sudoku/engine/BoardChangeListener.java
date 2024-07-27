package com.jkurapati.sudoku.engine;

public interface BoardChangeListener {
    void updateBoard(char[][] board, int x, int y);
}
