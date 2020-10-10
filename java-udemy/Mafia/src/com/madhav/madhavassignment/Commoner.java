package com.madhav.madhavassignment;

public class Commoner implements Game{
    private int Hp=1000;
    private String name="commoner";

    @Override
    public void decreaseHp(int val) {
        this.Hp-=val;
    }
    public void dead()
    {
        this.Hp=0;
    }
    public void heal()
    {
        this.Hp+=500;
    }
    @Override
    public int getHp() {
        return this.Hp;
    }
    @Override
    public String getName() {
        return name;
    }
}
