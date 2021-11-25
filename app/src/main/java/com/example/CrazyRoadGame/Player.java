package com.example.CrazyRoadGame;


public class Player {

    private int pos;
    private int numOfLife;
    private int score;


    public Player(){
        pos=2;
        numOfLife=3;
        score=0;
    }

    public int getNumOfLife() {
        return numOfLife;
    }


    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void hitRock() {
        --numOfLife;
    }

    public void hitCoin(int coinVal){
        score=+coinVal;
    }

    public int getScore() {
        return score;
    }

}
