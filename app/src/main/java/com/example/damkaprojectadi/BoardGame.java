package com.example.damkaprojectadi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class BoardGame extends View {
    private Coin[][] coins; // שינוי למערך דו-מימדי
    private Context context;
    private Square[][] squares;
    private Coin coin;
    private boolean firstTime;
    private final int NUM_OF_SQUARES = 8;
    private boolean isWhiteTurn = true;
    private int startRow, startCol; // משתני עזר לדעת מאיפה המטבע התחיל

    private float w;

    public BoardGame(Context context) {
        super(context);
        this.context = context;
        squares = new Square[NUM_OF_SQUARES][NUM_OF_SQUARES];
        coins = new Coin[NUM_OF_SQUARES][NUM_OF_SQUARES]; // אתחול המערך
        firstTime = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(firstTime)
        {
            initBoard(canvas);
            initCoin(canvas);
            firstTime = false;
        }
        drawBoard(canvas);
        drawCoin(canvas);
    }

    private void initBoard(Canvas canvas) {
        w = canvas.getWidth()/NUM_OF_SQUARES;
        float x = 0;
        float y = 600;
        float h = w;
        int color;

        for(int row=0; row< NUM_OF_SQUARES; row++)
        {
            for(int col=0; col< NUM_OF_SQUARES; col++)
            {
                if(row%2 == 0)
                {
                    if(col%2 == 0) color = Color.WHITE;
                    else color = Color.parseColor("#C19A68");
                }
                else
                {
                    if(col%2 == 0) color = Color.parseColor("#C19A68");
                    else color = Color.WHITE;
                }
                squares[row][col] = new Square(x,y,w,h,color);
                x = x+w;
            }
            y = y + h;
            x = 0;
        }
    }

    private void initCoin(Canvas canvas) {
        float r = w/3;
        for (int row=0; row<NUM_OF_SQUARES; row++)
        {
            for(int col=0; col<NUM_OF_SQUARES; col++)
            {
                if ((row + col) % 2 != 0)
                {
                    float x = col * w + w / 2;
                    float y = 600 + row * w + w / 2;
                    if (row < 3)
                    {
                        // השמה במערך הדו-מימדי
                        coins[row][col] = new Coin(x, y, r, Color.WHITE, Coin.TEAM_WHITE, row, col);
                    }
                    else if (row > 4)
                    {
                        // השמה במערך הדו-מימדי
                        coins[row][col] = new Coin(x, y, r, Color.BLACK, Coin.TEAM_BLACK, row, col);
                    }
                }
            }
        }
    }

    private void drawBoard(Canvas canvas) {
        for (int i=0; i< NUM_OF_SQUARES; i++)
        {
            for(int j=0; j< NUM_OF_SQUARES; j++)
            {
                squares[i][j].draw(canvas);
            }
        }
    }

    private void drawCoin(Canvas canvas) {
        // מעבר על המערך הדו-מימדי וציור רק איפה שיש מטבע
        for (int i = 0; i < NUM_OF_SQUARES; i++) {
            for (int j = 0; j < NUM_OF_SQUARES; j++) {
                if (coins[i][j] != null) {
                    coins[i][j].draw(canvas);
                }
            }
        }
    }

    private void switchTurn() {
        if (isWhiteTurn == true) {
            isWhiteTurn = false;
        } else {
            isWhiteTurn = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            // חישוב של המיקום במערך לפי הלחיצה
            int row = (int) ((y - 600) / w);
            int col = (int) (x / w);

            if (row >= 0 && row < 8 && col >= 0 && col < 8) {
                if (coins[row][col] != null) {
                    if ((isWhiteTurn && coins[row][col].team == Coin.TEAM_WHITE) || (!isWhiteTurn && coins[row][col].team == Coin.TEAM_BLACK)) {
                        coin = coins[row][col];
                        startRow = row; // שמירת המיקום המקורי
                        startCol = col;
                    }
                }
            }
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE)
        {
            if (coin != null)
            {
                coin.x = x;
                coin.y = y;
                invalidate();
            }
        }

        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            if (coin != null)
            {
                updateCoinAfterRelease();
                coin = null;
                invalidate();
            }
        }
        return true;
    }

    private void updateCoinAfterRelease() {
        int targetRow = -1;
        int targetCol = -1;

        // 1. מציאת המשבצת (הלולאה שלך)
        for (int i = 0; i < NUM_OF_SQUARES; i++) {
            for (int j = 0; j < NUM_OF_SQUARES; j++) {
                if (squares[i][j].didXandYInSquare(coin.x, coin.y)) {
                    targetRow = i;
                    targetCol = j;
                }
            }
        }

        // 2. בדיקה: האם אנחנו מחוץ ללוח?
        if (targetRow == -1 || targetCol == -1) {
            coin.x = coin.lastX;
            coin.y = coin.lastY;
        }
        else {
            // --- בלוק ELSE גדול: אנחנו בתוך הלוח ---

            boolean occupied = (coins[targetRow][targetCol] != null && coins[targetRow][targetCol] != coin);
            int rowDiff = targetRow - startRow;
            int colDiff = Math.abs(targetCol - startCol);

            boolean legal = false;
            if (coin.team == Coin.TEAM_WHITE) {
                if (rowDiff == 1 && colDiff == 1) legal = true;
            } else {
                if (rowDiff == -1 && colDiff == 1) legal = true;
            }

            // בדיקה סופית לביצוע המהלך
            if (squares[targetRow][targetCol].color == Color.parseColor("#C19A68") && !occupied && legal) {
                // עדכון המערך
                coins[targetRow][targetCol] = coin;
                coins[startRow][startCol] = null;

                // עדכון גרפי
                coin.x = squares[targetRow][targetCol].x + w / 2;
                coin.y = squares[targetRow][targetCol].y + w / 2;

                // עדכון נתונים
                coin.row = targetRow;
                coin.col = targetCol;
                coin.lastX = coin.x;
                coin.lastY = coin.y;

                // החלפת תור (קורה רק אם המהלך חוקי!)
                switchTurn();
            }
            else {
                // מהלך בתוך הלוח אבל לא חוקי - מחזירים למקום
                coin.x = coin.lastX;
                coin.y = coin.lastY;
            }
        }
    }
}