package com.madhav.madhavassignment;

public class Detective implements Game{
    private int Hp=800;
    private String name="detective";

    @Override
    public void decreaseHp(int val) {
        this.Hp-=val;
    }
    public void heal()
    {
        this.Hp+=500;
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
}
