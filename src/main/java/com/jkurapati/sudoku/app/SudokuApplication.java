package com.jkurapati.sudoku.app;

import com.jkurapati.sudoku.engine.DefaultSolver;
import com.jkurapati.sudoku.engine.Solver;
import com.jkurapati.sudoku.engine.SudokuBoard;
import com.jkurapati.sudoku.ui.SudokuUi;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class is the Root Container (the thing which attends to all of the primary objects which must communicate when
 * the program is running).
 */
public class SudokuApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        SudokuUi uiImpl = new SudokuUi(primaryStage, new SudokuBoard());
        System.out.println("Started UI");
        Solver solver = new DefaultSolver(new SudokuBoard(), uiImpl);
        new Thread(() -> {
            solver.solve();
        }).start();
        System.out.println("Started solver");
    }

    public static void main(String[] args) {
        launch(args);
    }
}