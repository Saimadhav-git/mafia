package com.madhav.madhavassignment;

public class Healer implements Game{
    private int Hp=800;
    private String name="healer";

    @Override
    public void decreaseHp(int val) {
        this.Hp-=val;
    }
    public void dead()
    {
        this.Hp=0;
    }
    @Override
    public int getHp() {
        return this.Hp;
    }
    @Override
    public String getName() {
        return name;
    }
    public void heal()
    {
        this.Hp+=500;
    }
}
