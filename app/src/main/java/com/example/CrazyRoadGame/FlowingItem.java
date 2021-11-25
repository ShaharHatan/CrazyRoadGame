package com.example.CrazyRoadGame;

import java.util.Random;

public class FlowingItem {
 public enum ITEM_TYPE {ROCK,COIN};

    private int row;
    private final int COL;
    private final ITEM_TYPE ITEM_TYPE;
    //val=0 rock    ,   val=1 coin


    public FlowingItem(ITEM_TYPE item_type) {
        Random r = new Random();
        COL = r.nextInt(5);
        row = -1;
        this.ITEM_TYPE = item_type;
    }

    public int getRow() {
        return row;
    }

    public int getCOL() {
        return COL;
    }

    public void setRowNextLevel() {
        row++;
    }

    public ITEM_TYPE getITEM_TYPE() {
        return ITEM_TYPE;
    }
}
