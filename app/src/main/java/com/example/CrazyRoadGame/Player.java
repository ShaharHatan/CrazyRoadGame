package com.example.CrazyRoadGame;


public class Player {
    public enum POS{LEFT,CENTER,RIGHT}

    private POS pos;
    private int numOfLife;


    public Player(){
        pos=POS.CENTER;
        numOfLife=3;
    }

    public int getNumOfLife() {
        return numOfLife;
    }

    public void setLifeAfterHit() {
        --numOfLife;
    }

    public POS getPos() {
        return pos;
    }

    public void setPos(POS pos) {
        this.pos = pos;
    }

}
