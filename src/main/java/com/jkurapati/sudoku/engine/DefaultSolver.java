package com.jkurapati.sudoku.engine;

public class DefaultSolver implements Solver {
    private final BoardGenerator generator;
    private final BoardChangeListener listener;
    private final char[] posb = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};

    public DefaultSolver(BoardGenerator generator, BoardChangeListener boardChangeListener) {
        this.generator = generator;
        this.listener = boardChangeListener;
    }

    @Override
    public boolean solve() {
        return backtrack(generator.getBoard(), 0, 0);
    }

    private boolean backtrack(char[][] board, int x, int y) {
//         System.out.printf("Backtracking at [%d,%d]\n",x,y);
        int[] unf = getNextUnfilled(board, x, y);
//         System.out.printf("Unfilled entry at [%d,%d]\n",unf[0],unf[1]);
        if (unf[0] == -1) {
            return true;
        }
        for (char p : posb) {
            if (isValid(board, unf[0], unf[1], p)) {
                board[unf[0]][unf[1]] = p;
                notifyListener(board, unf[0], unf[1]);
                if (backtrack(board, unf[0], unf[1])) {
//                    System.out.printf("Finalized value %c at [%d,%d]\n", p, unf[0], unf[1]);
                    return true;
                }
            }
        }
        notifyListener(board, unf[0], unf[1]);
        board[unf[0]][unf[1]] = '.';
        return false;
    }

    private void notifyListener(char[][] board, int x, int y) {
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        listener.updateBoard(board, x, y);
    }

    private int[] getNextUnfilled(char[][] board, int x, int y) {
        while (x < 9 && y < 9) {
            char t = board[x][y];
            if (t == '.') return new int[]{x, y};
            y++;
            x += y / 9;
            y = y % 9;
        }
        return new int[]{-1, -1};
    }

    private boolean isValid(char[][] board, int x, int y, char target) {
        char t = target;
        for (int i = 0; i < 9; i++) {
            if (board[x][i] == t || board[i][y] == t) {
                return false;
            }
        }
        // 4,4
        // i=3
        // i<6
        for (int i = ((int) (x / 3) * 3); i < ((int) (x / 3) * 3 + 3); i++) {
            for (int j = ((int) (y / 3) * 3); j < ((int) (y / 3) * 3 + 3); j++) {
                if (board[i][j] == t) {
                    return false;
                }
            }
        }
        return true;
    }
}
