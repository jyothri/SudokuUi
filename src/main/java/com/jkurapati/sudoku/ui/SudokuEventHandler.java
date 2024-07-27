package com.jkurapati.sudoku.ui;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SudokuEventHandler implements EventHandler<KeyEvent> {
    private final Set<String> validCellValues = new HashSet<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9"));
    private static final String GAME_COMPLETE = "Congratulations, you have won! New Game?";
    private static final String ERROR = "An error has occurred.";
    private final HashMap<Coordinates, SudokuTextField> textFieldCoordinates;

    public SudokuEventHandler(HashMap<Coordinates, SudokuTextField> textFieldCoordinates) {
        this.textFieldCoordinates = textFieldCoordinates;
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            if (validCellValues.contains(event.getText())) {
                int value = Integer.parseInt(event.getText());
                handleInput(value, event.getSource());
            } else if (event.getCode() == KeyCode.BACK_SPACE) {
                handleInput(0, event.getSource());
            } else {
                ((TextField) event.getSource()).setText("");
            }
        }
        event.consume();
    }

    /**
     * @param value  expected to be an integer from 0-9, inclusive
     * @param source the textfield object that was clicked.
     */
    private void handleInput(int input, Object source) {
        int x = ((SudokuTextField) source).getX();
        int y = ((SudokuTextField) source).getY();
        String value = Integer.toString(input);
        ((TextField) source).setText(value);
        SudokuTextField tile = textFieldCoordinates.get(new Coordinates(x, y));
        tile.textProperty().setValue(value);
            //if game is complete, show dialog
//            if (gameData.getGameState() == GameState.COMPLETE) showDialog(GAME_COMPLETE);
    }

    public void showDialog(String message) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK);
        dialog.showAndWait();

        if (dialog.getResult() == ButtonType.OK) {
            onDialogClick();
        }
    }

    public void showError(String message) {
        Alert dialog = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        dialog.showAndWait();
    }

    public void onDialogClick() {

    }
}
