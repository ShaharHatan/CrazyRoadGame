package com.example.CrazyRoadGame;

import java.util.Random;

public class Rock {

    private int row;
    private int col;


    public Rock() {
        Random r = new Random();
        col = r.nextInt(3);
        row = 0;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setRowNextLevel() {
        row++;
    }
}
