package com.example.damkaprojectadi;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Coin extends Shape{
    float r;
    Paint p;
    private boolean isKing;
    int team; // 1=White, -1 = Brown
    int row, col;
    public static final int TEAM_BLACK= -1;
    public static final int TEAM_WHITE= 1;
    float lastX, lastY;  // to save the previous location

    public Coin(float x, float y, float r, int color, int team, int row, int col) {
        super(x,y,color);
        this.r = r;
        this.team=team;
        this.row=row;
        this.col=col;
        this.isKing = false;
        lastX = x;  // x previous location
        lastY = y;  // y previous location
        p = new Paint();
        p.setColor(color);
    }

    public void setKing ()
    {
        this.isKing = true;
    }
    public void draw(Canvas canvas)
    {
        if (isKing )
            canvas.drawRect(x-r,y-r,x+r,y+r,p);
        else
        canvas.drawCircle(x,y,r,p);
    }

    public boolean didUserTouchMe(float xu, float yu)
    {
        // xu & yu - the user touch location on the canvas
        // the function returns true if the user touch inside the coin, false otherwise
        if(Math.sqrt(Math.pow((x-xu),2) + Math.pow((y-yu),2)) < r)
            return true;
        return false;
    }

}
