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
    private int countblack=12,countwhite=12;
    private int startRow, startCol,targetrow,targetcol; // משתני עזר לדעת מאיפה המטבע התחיל
    private int backgroundColor = Color.parseColor("#eeddd2");
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
        canvas.drawColor(backgroundColor);
        if(firstTime)
        {
            initBoard(canvas);
            initCoin(canvas);
            firstTime = false;
        }
        drawBoard(canvas);
        drawCoin(canvas);

    }
public boolean IsGameOver()
{
    return countwhite+countblack<=5;
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
        isWhiteTurn = !isWhiteTurn;
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
        targetrow = -1;
        targetcol = -1;

        // 1. מציאת המשבצת
        for (int i = 0; i < NUM_OF_SQUARES; i++) {
            for (int j = 0; j < NUM_OF_SQUARES; j++) {
                if (squares[i][j].didXandYInSquare(coin.x, coin.y)) {
                    targetrow = i;
                    targetcol = j;
                }
            }
        }

        // 2. בדיקה: האם אנחנו מחוץ ללוח?
        if (targetrow == -1 || targetcol == -1 ) {
            coin.x = coin.lastX;
            coin.y = coin.lastY;
        }
        else {
            // --- בלוק ELSE גדול: אנחנו בתוך הלוח ---
            boolean occupied = (coins[targetrow][targetcol] != null && coins[targetrow][targetcol] != coin);
            int rowDiff = targetrow - startRow;
            int colAbsDiff = Math.abs(targetcol - startCol);

            boolean legal = false;
            boolean isEatingMove = false;

            // בדיקה לתנועה רגילה (צעד אחד באלכסון)
            if (!occupied) {
                if ((coin.team == Coin.TEAM_WHITE || coin.GetisKing()) && rowDiff == 1 && colAbsDiff == 1) legal = true;
                if ((coin.team == Coin.TEAM_BLACK || coin.GetisKing()) && rowDiff == -1 && colAbsDiff == 1) legal = true;

                // בדיקת אכילה (דילוג של 2 משבצות)
                if (Math.abs(rowDiff) == 2 && colAbsDiff == 2)
                {
                    int midRow = (startRow + targetrow) / 2;
                    int midCol = (startCol + targetcol) / 2;

                    // בדיקה אם יש כלי של היריב באמצע
                    if (coins[midRow][midCol] != null && coins[midRow][midCol].team != coin.team) {

                        // כאן התיקון: בדיקת כיוון האכילה
                        if (coin.GetisKing()) {
                            // מלך יכול לאכול לכל הכיוונים
                            legal = true;
                            isEatingMove = true;
                        }
                        else if (coin.team == Coin.TEAM_WHITE && rowDiff == 2) {
                            // לבן אוכל רק "למטה" (שורות עולות)
                            legal = true;
                            isEatingMove = true;
                        }
                        else if (coin.team == Coin.TEAM_BLACK && rowDiff == -2) {
                            // שחור אוכל רק "למעלה" (שורות יורדות)
                            legal = true;
                            isEatingMove = true;
                        }

                        // אם המהלך נקבע כחוקי, נמחק את הכלי הנאכל
                        if (legal) {
                            if (coins[midRow][midCol].color==Color.WHITE)
                                countwhite--;
                            else
                                countblack--;
                            coins[midRow][midCol] = null;
                            if (IsGameOver()) {
                                if (countblack > countwhite)
                                    Toast.makeText(context, "black won", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(context, "white won", Toast.LENGTH_SHORT).show();
                                // כאן להוסיף כפתור משחק חדש
                            }

                        }
                    }
                }
            }

            // בדיקה סופית לביצוע המהלך (רק על משבצת חומה)
            if (squares[targetrow][targetcol].color == Color.parseColor("#C19A68") && !occupied && legal) {

                // בדיקת הכתרה למלך - אם לבן הגיע לשורה 7 או שחור לשורה 0
                if (coin.team == Coin.TEAM_WHITE && targetrow == 7) {
                    coin.setKing();
                }
                if (coin.team == Coin.TEAM_BLACK && targetrow == 0) {
                    coin.setKing();
                }

                // עדכון המערך
                coins[targetrow][targetcol] = coin;
                coins[startRow][startCol] = null;

                // עדכון גרפי
                coin.x = squares[targetrow][targetcol].x + w / 2;
                coin.y = squares[targetrow][targetcol].y + w / 2;

                // עדכון נתונים
                coin.row = targetrow;
                coin.col = targetcol;
                coin.lastX = coin.x;
                coin.lastY = coin.y;

                // החלפת תור
                switchTurn();
            }
            else {
                // מהלך לא חוקי - מחזירים למקום המקורי
                coin.x = coin.lastX;
                coin.y = coin.lastY;
            }
        }
    }
}