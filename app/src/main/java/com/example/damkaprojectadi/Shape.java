package com.example.damkaprojectadi;

import android.graphics.Canvas;

public abstract class Shape {
    protected float  x,y;
    protected int color;

    public Shape(float x, float y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }
    public void setX(float x){
        this.x = x;
    }

    public void setY(float y){
        this.y = y;
    }

    public abstract void draw(Canvas canvas);

}
