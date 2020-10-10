package com.madhav.madhavassignment;

public interface Game {
    void decreaseHp(int val);
    int getHp();
    String getName();
    void heal();
    void dead();
}
