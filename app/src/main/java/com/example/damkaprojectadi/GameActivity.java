package com.example.damkaprojectadi;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    public FbModule fbModule;
    private BoardGame boardGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game); //טעינת קובץ העיצוב הסטנדרטי של האקטיביטי

        boardGame = new BoardGame(this); //יצירת לוח המשחק הלוגי והגרפי
        setContentView(boardGame); // השמת הלוח שיצרנו כתצוגה הראשית המוצגת למשתמש על המסך

        fbModule = new FbModule(this);

        fbModule.setPositionInFirebase(null);

    }

    public void setPositionFromFb(Position position) {
        Toast.makeText(this, "" + position.getStartRow() + " " + position.getStartCol(), Toast.LENGTH_SHORT).show();
        boardGame.setPositionReceiveFromFirebase(position); //העברת אובייקט ה-Position שהתקבל מהענן אל לוח המשחק כדי לעדכן את מיקום החיילים הפיזיים
    }
}