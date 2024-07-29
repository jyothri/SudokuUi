package com.jkurapati.sudoku.app;

import com.jkurapati.sudoku.engine.DefaultSolver;
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
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        SudokuUi.SudokuUiBuilder.newSudokuUiBuilder(primaryStage)
                .setBoardGenerator(new SudokuBoard())
                .setSolver(new DefaultSolver())
                .build();
        System.out.println("Started UI");
    }
}