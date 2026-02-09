package com.example.damkaprojectadi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class BoardGame extends View {
    private ArrayList<Coin> coins;
    private Context context;
    private Square[][] squares;
    private Coin coin;
    private Coin coin1, coin2, coin3, coin4, coin5,coin6,coin7,coin8;
    private boolean firstTime;
    private final int NUM_OF_SQUARES = 8;
    private boolean isWhiteTurn= true;

    private float w;

    public BoardGame(Context context) {
        super(context);
        this.context = context;
        squares = new Square[NUM_OF_SQUARES][NUM_OF_SQUARES];
        firstTime = true;
        coins = new ArrayList<>();
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

        for(int row=0; row< NUM_OF_SQUARES; row++) //שורה
        {
            for(int col=0; col< NUM_OF_SQUARES; col++) //עמודה
            {
                if(row%2 == 0) // Even line
                {
                    if(col%2 == 0)
                        color = Color.WHITE;
                    else
                        color = Color.parseColor("#C19A68");
                }
                else
                {   // Odd line
                    if(col%2 == 0)
                        color = Color.parseColor("#C19A68");
                    else
                        color = Color.WHITE;
                }
                squares[row][col] = new Square(x,y,w,h,color);

                x = x+w;
            }
            y = y + h;
            x = 0;
        }
    }

    private void initCoin(Canvas canvas) {
        // set the coin location only once, at the beginning
        float w = canvas.getWidth()/NUM_OF_SQUARES;
        float r = w/3;
        for (int row=0; row<NUM_OF_SQUARES; row++)
        {
            for(int col=0; col<NUM_OF_SQUARES; col++)
            {
                if ((row + col) % 2 !=0)
                {
                    float x = col * w + w /2;
                    float y = 600 + row * w + w/2;
                    if (row < 3 )
                    {
                        coins.add(new Coin(x,y, r, Color.WHITE, Coin.TEAM_WHITE, row, col));
                    }
                    else if (row > 4)
                    {
                        coins.add(new Coin(x,y, r, Color.BLACK, Coin.TEAM_BLACK, row, col));
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
        for (Coin c : coins)
        {
            c.draw(canvas);
        }

    }


    private void switchTurn ()
    {
        if (isWhiteTurn==true)
        {
            isWhiteTurn = false;
        }
        else
        {
            isWhiteTurn = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y= event.getY();

        if (event.getAction()== MotionEvent.ACTION_DOWN)
        {
            for (Coin c : coins)
            {
                if (c.didUserTouchMe(x,y)){
                    if((isWhiteTurn && c.team== Coin.TEAM_WHITE) ||(!isWhiteTurn && c.team == Coin.TEAM_BLACK))
                    {
                        coin = c;
                    }
                }
            }
        }

        if(event.getAction() == MotionEvent.ACTION_MOVE)
        {
            if (coin != null)
            {
                coin.x = x;
                coin.y = y;
                invalidate();
            }
        }

        if(event.getAction() == MotionEvent.ACTION_UP)
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
        int targetRow = -1, targetCol = -1;

        //  מציאת המשבצת עליה הונח המטבע
        for (int i = 0; i < NUM_OF_SQUARES; i++) { // i מייצג שורה
            for (int j = 0; j < NUM_OF_SQUARES; j++) { // j מייצג עמודה
                if (squares[i][j].didXandYInSquare(coin.x, coin.y)) {
                    targetRow = i;
                    targetCol = j;
                }
            }
        }

        // אם המטבע שוחרר מחוץ ללוח
        if (targetRow == -1 || targetCol == -1) {
            coin.x = coin.lastX;
            coin.y = coin.lastY;
            return;
        }


        //  בדיקה אם המשבצת תפוסה
        boolean occupied = false;
        for (Coin c : coins) {
            if (c != coin && c.row == targetRow && c.col == targetCol) {
                occupied = true;

            }
        }


//  בדיקת חוקיות התנועה (אלכסון קדימה בלבד)
        boolean legal = false;
        int rowDiff = targetRow - coin.row;
        int colDiff = Math.abs(targetCol - coin.col);

        if (coin.team == Coin.TEAM_WHITE) {
            // לבן זז למטה (שורות עולות)
            if (rowDiff == 1 && colDiff == 1) {
                legal = true;
            }
        }
        else
        {
            // שחור זז למעלה (שורות יורדות)
            if (rowDiff == -1 && colDiff == 1) {
                legal = true;
            }
        }

//  ביצוע המהלך או חזרה אחורה
        // בדיקה: צבע משבצת נכון, לא תפוס, ומהלך חוקי
        if (squares[targetRow][targetCol].color == Color.parseColor("#C19A68") && !occupied && legal) {
            coin.x = squares[targetRow][targetCol].x + w / 2;
            coin.y = squares[targetRow][targetCol].y + w / 2;
            coin.row = targetRow;
            coin.col = targetCol;
            coin.lastX = coin.x;
            coin.lastY = coin.y;
            switchTurn();
        }
        else
        {
            // מהלך לא חוקי - חזרה למיקום הקודם
            coin.x = coin.lastX;
            coin.y = coin.lastY;
        }
    }
}