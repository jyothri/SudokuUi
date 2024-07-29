package com.jkurapati.sudoku.ui;

import com.jkurapati.sudoku.engine.BoardChangeListener;
import com.jkurapati.sudoku.engine.BoardGenerator;
import com.jkurapati.sudoku.engine.Solver;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;

/**
 * Manages the window, and displays a pop up notification when the user completes the puzzle.
 */
public final class SudokuUi implements BoardChangeListener {
    //Size of the window
    private static final double WINDOW_Y = 732;
    private static final double WINDOW_X = 668;
    //distance between window and board
    private static final double BOARD_PADDING = 50;

    private static final double BOARD_X_AND_Y = 576;
    private static final Color WINDOW_BACKGROUND_COLOR = Color.rgb(0, 150, 136);
    private static final Color TILE_UPDATE__BACKGROUND_COLOR = Color.rgb(0, 150, 200);
    private static final Color BOARD_BACKGROUND_COLOR = Color.rgb(224, 242, 241);
    private static final String SUDOKU = "Sudoku";

    private final Stage stage;
    private final Group root;
    //This HashMap stores the Hash Values (a unique identifier which is automatically generated;
    // see java.lang.object in the documentation) of each TextField by their Coordinates. When a SudokuGame
    //is given to the updateUI method, we iterate through it by X and Y coordinates and assign the values to the
    //appropriate TextField therein. This means we don't need to hold a reference variable for every god damn
    //text field in this app; which would be awful.
    //The Key (<Key, Value> -> <Coordinates, Integer>) will be the HashCode of a given InputField for ease of lookup
    private final HashMap<Coordinates, SudokuTextField> textFieldCoordinates;
    private final SudokuEventHandler eventHandler;
    private BoardGenerator boardGenerator;
    private Solver solver;

    public void setSolver(Solver solver) {
        this.solver = solver;
    }

    public void setBoardGenerator(BoardGenerator boardGenerator) {
        this.boardGenerator = boardGenerator;
    }

    /**
     * Stage and Group are JavaFX specific classes for modifying the UI. Think of them as containers of various UI
     * components.
     * <p>
     * A HashMap is a data structure which stores key/value pairs. Rather than creating a member variable for every
     * SudokuTextField object (all 81 of them), I instead store these references within a HashMap, and I retrieve
     * them by using their X and Y Coordinates as a "key" (a unique value used to look something up).
     *
     * @param stage
     */

    SudokuUi(Stage stage) {
        this.stage = stage;
        this.root = new Group();
        this.textFieldCoordinates = new HashMap<>();
        this.eventHandler = new SudokuEventHandler(textFieldCoordinates);
    }

    void initializeUserInterface() {
        drawBackground(root);
        drawTitle(root);
        drawSudokuBoard(root);
        drawTextFields(root);
        drawGridLines(root);
        stage.show();
    }

    /**
     * 1. Draw each TextField based on x and y values.
     * 2. As each TextField is drawn, add its coordinates (x, y) based on Hash Value to the HashMap.
     *
     * @param root
     */
    private void drawTextFields(Group root) {
        //where to start drawing the numbers
        final int xOrigin = 50;
        final int yOrigin = 50;
        //how much to move the x or y value after each loop
        final int xAndYDelta = 64;

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                int xOffset = xOrigin + x * xAndYDelta;
                int yOffset = yOrigin + y * xAndYDelta;
                //Create tile
                SudokuTextField tile = new SudokuTextField(x, y);
                styleSudokuTile(tile, xOffset, yOffset);
                tile.setOnKeyPressed(eventHandler);
                textFieldCoordinates.put(new Coordinates(x, y), tile);
                root.getChildren().add(tile);
            }
        }
    }

    /**
     * Helper method for styling a sudoku tile number
     *
     * @param tile
     * @param x
     * @param y
     */
    private void styleSudokuTile(SudokuTextField tile, double x, double y) {
        Font numberFont = new Font(32);
        tile.setFont(numberFont);
        tile.setAlignment(Pos.CENTER);

        tile.setLayoutX(x);
        tile.setLayoutY(y);
        tile.setPrefHeight(64);
        tile.setPrefWidth(64);

        tile.setBackground(Background.EMPTY);
    }


    /**
     * In order to draw the various lines that make up the Sudoku grid, we use a starting x and y offset
     * value (remember, x grows positively from left to right, and y grows positively from top to bottom).
     * Each square is meant to be 64x64 units, so we add that number each time a
     *
     * @param root
     */
    private void drawGridLines(Group root) {
        //draw vertical lines starting at 114x and 114y:
        int xAndY = 114;
        for (int index = 0; index < 8; index++) {
            int thickness = (index + 1) % 3 == 0 ? 3 : 2;
            Rectangle verticalLine = getLine(xAndY + 64 * index, BOARD_PADDING, BOARD_X_AND_Y, thickness);
            Rectangle horizontalLine = getLine(BOARD_PADDING, xAndY + 64 * index, thickness, BOARD_X_AND_Y);
            root.getChildren().addAll(verticalLine, horizontalLine);
        }
    }

    private Rectangle getLine(double x, double y, double height, double width) {
        Rectangle line = new Rectangle();
        line.setX(x);
        line.setY(y);
        line.setHeight(height);
        line.setWidth(width);
        line.setFill(Color.BLACK);
        return line;
    }

    /**
     * Background of the primary window
     *
     * @param root
     */
    private void drawBackground(Group root) {
        Scene scene = new Scene(root, WINDOW_X, WINDOW_Y);
        scene.setFill(WINDOW_BACKGROUND_COLOR);
        stage.setScene(scene);
    }

    /**
     * Background of the actual sudoku board, offset from the window by BOARD_PADDING
     *
     * @param root
     */
    private void drawSudokuBoard(Group root) {
        Rectangle boardBackground = new Rectangle();
        boardBackground.setX(BOARD_PADDING);
        boardBackground.setY(BOARD_PADDING);
        boardBackground.setWidth(BOARD_X_AND_Y);
        boardBackground.setHeight(BOARD_X_AND_Y);
        boardBackground.setFill(BOARD_BACKGROUND_COLOR);
        root.getChildren().add(boardBackground);
    }

    private void drawTitle(Group root) {
        Text title = new Text(235, 690, SUDOKU);
        title.setFill(Color.WHITE);
        Font titleFont = new Font(43);
        title.setFont(titleFont);
        root.getChildren().add(title);
    }

    void drawSolveButton() {
        if (solver == null) {
            return;
        }
        Button solveButton = new Button("Solve it!");
        Font buttonFont = new Font(32);
        solveButton.setFont(buttonFont);
        solveButton.setAlignment(Pos.CENTER);

        solveButton.setLayoutX(480);
        solveButton.setLayoutY(658);

        solveButton.setPrefHeight(20);
        solveButton.setStyle("-fx-background-color: \n" + "        linear-gradient(#ffd65b, #e68400),\n" + "        linear-gradient(#ffef84, #f2ba44),\n" + "        linear-gradient(#ffea6a, #efaa22),\n" + "        linear-gradient(#ffe657 0%, #f8c202 50%, #eea10b 100%),\n" + "        linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0));\n" + "    -fx-background-radius: 30;\n" + "    -fx-background-insets: 0,1,2,3,0;\n" + "    -fx-text-fill: #654b00;\n" + "    -fx-font-weight: bold;\n" + "    -fx-font-size: 14px;\n" + "    -fx-padding: 10 20 10 20;");
        solveButton.setOnAction((unused) -> {
            System.out.println("Solving the board for user.");
            new Thread(() -> {
                solver.setBoardGenerator(this.boardGenerator);
                solver.setBoardChangeListener(this);
                solver.solve();
            }).start();
        });
        root.getChildren().add(solveButton);
    }

    void initializeBoard() {
        char[][] board = boardGenerator.getBoard();
        for (int xIndex = 0; xIndex < 9; xIndex++) {
            for (int yIndex = 0; yIndex < 9; yIndex++) {
                TextField tile = textFieldCoordinates.get(new Coordinates(xIndex, yIndex));
                String value = Character.toString(board[xIndex][yIndex]);
                if (value.equals(".")) {
                    value = "";
                }
                tile.setText(value);
                if (!value.equals("")) {
                    tile.setStyle("-fx-opacity: 0.8;");
                    tile.setDisable(true);
                }
            }
        }
    }

    @Override
    public void updateBoard(char[][] board, int x, int y) {
        TextField tile = textFieldCoordinates.get(new Coordinates(x, y));
        String value = Character.toString(board[x][y]);
        Platform.runLater(() -> {
            if (value.equals(".")) {
                tile.setText("");
                tile.setStyle("-fx-background-color: green;");
            } else {
                tile.setText(value);
                tile.setStyle("-fx-background-color: orange;");
            }
        });
    }

    public static final class SudokuUiBuilder {
        private Stage stage;
        private BoardGenerator boardGenerator;
        private Solver solver;

        public Solver getSolver() {
            return solver;
        }

        public static SudokuUiBuilder newSudokuUiBuilder(Stage stage) {
            SudokuUiBuilder sudokuUiBuilder = new SudokuUiBuilder();
            sudokuUiBuilder.setStage(stage);
            return sudokuUiBuilder;
        }

        public SudokuUi build() {
            SudokuUi sudokuUi = new SudokuUi(this.getStage());
            sudokuUi.initializeUserInterface();
            if (this.getSolver() != null) {
                sudokuUi.setSolver(this.solver);
                sudokuUi.drawSolveButton();
            }
            if (this.getBoardGenerator() != null) {
                sudokuUi.setBoardGenerator(this.getBoardGenerator());
                sudokuUi.initializeBoard();
            }
            return sudokuUi;
        }

        public Stage getStage() {
            return stage;
        }

        public void setStage(Stage stage) {
            this.stage = stage;
        }

        public BoardGenerator getBoardGenerator() {
            return boardGenerator;
        }

        public SudokuUiBuilder setBoardGenerator(BoardGenerator boardGenerator) {
            this.boardGenerator = boardGenerator;
            return this;
        }

        public SudokuUiBuilder setSolver(Solver solver) {
            this.solver = solver;
            return this;
        }
    }
}