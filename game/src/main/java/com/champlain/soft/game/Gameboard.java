package com.champlain.soft.game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Gameboard extends Application {
    // 🔹 Grid constants
    private static final int ROWS = 10;
    private static final int COLS = 10;

    // Scene constants
    private static final int SCENE_WIDTH  = 800;
    private static final int SCENE_HEIGHT = 800;


    enum CellType {
        GRASS, PLAYER, PRINCESS, BOMB, WALL
    }

    // 🔹 Use "matrix" instead of "map"
    private CellType[][] matrix = new CellType[ROWS][COLS];

    private Image imgGrass;
    private Image imgWall;
    private Image imgPlayer;
    private Image imgPrincess;
    private Image imgBomb;

    @Override
    public void start(Stage stage) {

        imgGrass    = new Image(getClass().getResourceAsStream("/images/grass.png"));
        imgWall     = new Image(getClass().getResourceAsStream("/images/wall.png"));
        imgPlayer   = new Image(getClass().getResourceAsStream("/images/gardian.png"));
        imgPrincess = new Image(getClass().getResourceAsStream("/images/princess.png"));
        imgBomb     = new Image(getClass().getResourceAsStream("/images/bomb.png"));

        initMatrix();

        GridPane grid = new GridPane();
        drawBoard(grid);

        BorderPane root = new BorderPane();
        root.setCenter(grid);


        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

        stage.setTitle("Rescue the Princess");
        stage.setScene(scene);
        stage.show();
    }



    private void initMatrix() {
        //fill everything with grass first
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                matrix[r][c] = CellType.GRASS;
            }
        }

        //walls on the perimeter
        for (int r = 0; r < ROWS; r++) {
            matrix[r][0] = CellType.WALL;
            matrix[r][COLS - 1] = CellType.WALL;
        }
        for (int c = 0; c < COLS; c++) {
            matrix[0][c] = CellType.WALL;
            matrix[ROWS - 1][c] = CellType.WALL;
        }

        //player always at [1][1]
        matrix[1][1] = CellType.PLAYER;

        //collect free inner cells (not player)
        List<int[]> freeCells = new ArrayList<>();
        for (int r = 1; r < ROWS - 1; r++) {
            for (int c = 1; c < COLS - 1; c++) {
                if (r == 1 && c == 1) continue;
                freeCells.add(new int[]{r, c});
            }
        }
        Collections.shuffle(freeCells);

        //place princess and bombs randomly
        matrix[freeCells.get(0)[0]][freeCells.get(0)[1]] = CellType.PRINCESS;
        matrix[freeCells.get(1)[0]][freeCells.get(1)[1]] = CellType.BOMB;
        matrix[freeCells.get(2)[0]][freeCells.get(2)[1]] = CellType.BOMB;
        matrix[freeCells.get(3)[0]][freeCells.get(3)[1]] = CellType.BOMB;
        matrix[freeCells.get(4)[0]][freeCells.get(4)[1]] = CellType.BOMB;
        matrix[freeCells.get(5)[0]][freeCells.get(5)[1]] = CellType.BOMB;
    }


    private void drawBoard(GridPane grid) {
        grid.getChildren().clear();

        double cellSize = SCENE_WIDTH / COLS;


        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {

                StackPane cell = new StackPane();
                cell.setPrefSize(cellSize, cellSize);

                ImageView bg = new ImageView(imgGrass);
                bg.setFitWidth(cellSize);
                bg.setFitHeight(cellSize);
                cell.getChildren().add(bg);

                Image fg = null;
                if      (matrix[row][col] == CellType.WALL)
                    fg = imgWall;
                else if (matrix[row][col] == CellType.PLAYER)
                    fg = imgPlayer;
                else if (matrix[row][col] == CellType.PRINCESS)
                    fg = imgPrincess;
                else if (matrix[row][col] == CellType.BOMB)
                    fg = imgBomb;

                if (fg != null) {
                    ImageView fgView = new ImageView(fg);
                    fgView.setFitWidth(cellSize);
                    fgView.setFitHeight(cellSize);
                    cell.getChildren().add(fgView);
                }

                grid.add(cell, col, row);
            }
        }
    }


}