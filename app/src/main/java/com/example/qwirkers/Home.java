package com.example.qwirkers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {
    public static String PLAYERS_MESSAGE = "Players";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }

    public void gameMode(View view) {
        Intent intent = new Intent(Home.this, GameMode.class);
        startActivity(intent);
    }
}