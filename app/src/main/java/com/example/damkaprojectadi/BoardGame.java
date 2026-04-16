package com.example.damkaprojectadi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.graphics.Paint;

import java.util.ArrayList;

public class BoardGame extends View {
    private Coin[][] coins;
    private Context context;
    private Square[][] squares;
    private Coin coin;
    private boolean firstTime;
    private final int NUM_OF_SQUARES = 8;
    private boolean isWhiteTurn = true;
    private int countblack=12,countwhite=12;
    private int startRow, startCol,targetrow,targetcol;
    private int backgroundColor = Color.parseColor("#eeddd2");
    private float w;
    private Bitmap homeButton;
    private int homeButtonX, homeBttonY; // מיקום הכפתור
    private int homeButtonSize =200; // גודל הכפתור
    private  GameActivity gameActivity;


    public BoardGame(Context context) {
        super(context);
        this.context = context;
        squares = new Square[NUM_OF_SQUARES][NUM_OF_SQUARES];
        coins = new Coin[NUM_OF_SQUARES][NUM_OF_SQUARES]; // אתחול המערך
        firstTime = true;

        homeButton = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow);
        homeButton = Bitmap.createScaledBitmap(homeButton, homeButtonSize, homeButtonSize, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(backgroundColor);
        // הוספת הכותרת
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK); // צבע הטקסט
        textPaint.setTextSize(100);      // גודל הטקסט
        //textPaint.setFakeBoldText(true); // הדגשה (Bold)
        textPaint.setTextAlign(Paint.Align.CENTER); // מרכזי את הטקסט לפי ה-X שניתן לו

        // חישוב המיקום: אמצע רוחב המסך, וגובה של 250 פיקסלים מלמעלה
        float xPos = canvas.getWidth() / 2;
        float yPos = 250;

        canvas.drawText("DAMKA", xPos, yPos, textPaint);
        if(firstTime)
        {
            initBoard(canvas);
            initCoin(canvas);
            firstTime = false;
        }
        drawBoard(canvas);
        drawCoin(canvas);

        homeButtonX = canvas.getWidth() - homeButtonSize - 50;
        homeBttonY = 50;

        if (homeButton != null) {
            canvas.drawBitmap(homeButton, homeButtonX, homeBttonY, null);
        }
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

            if (x>= homeButtonX && x<= homeButtonX + homeButtonSize &&
                    y>= homeBttonY && y<= homeBttonY + homeButtonSize){
                Intent i =new Intent(((GameActivity)context), MainActivity.class);
            }

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
                ((GameActivity)context).fbModule.setPositionInFirebase(new Position(startRow,startCol, coin.row,coin.col));
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

    public void setPositionReceiveFromFirebase(Position position) {

    }
}