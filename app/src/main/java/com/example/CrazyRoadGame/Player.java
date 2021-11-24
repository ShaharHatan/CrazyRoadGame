package com.example.CrazyRoadGame;


public class Player {

    private int pos;
    private int numOfLife;


    public Player(){
        pos=2;
        numOfLife=3;
    }

    public int getNumOfLife() {
        return numOfLife;
    }

    public void setLifeAfterHit() {
        --numOfLife;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

}
