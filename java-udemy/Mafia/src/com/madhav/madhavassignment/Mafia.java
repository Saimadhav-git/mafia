package com.madhav.madhavassignment;

public class Mafia implements Game{
    private int Hp=2500;
    private String name="mafia";
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
