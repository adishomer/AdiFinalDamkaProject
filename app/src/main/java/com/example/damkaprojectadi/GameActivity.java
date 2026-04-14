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
        setContentView(R.layout.activity_game);

        boardGame = new BoardGame(this);
        setContentView(boardGame);

        fbModule = new FbModule(this);



    }

    public void setPositionFromFb(Position position) {
        Toast.makeText(this, "" + position.getLastCol() + " " + position.getStartCol(), Toast.LENGTH_SHORT).show();
        boardGame.setPositionReceiveFromFirebase(position);
    }
}