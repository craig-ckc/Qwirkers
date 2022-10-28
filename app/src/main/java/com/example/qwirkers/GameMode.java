package com.example.qwirkers;

import static com.example.qwirkers.Utility.Utilities.PLAYER_COUNT;
import static com.example.qwirkers.Utility.Utilities.SERVER_ADDRESS;
import static com.example.qwirkers.Utility.Utilities.SHEARED_PREF;
import static com.example.qwirkers.Utility.Utilities.USER_NAME;
import static com.example.qwirkers.Utility.Utilities.createDialog;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Server.ClientHandler;
import Server.GameClient;
import Server.messages.Message;
import Server.messages.server.MoveReceived;

public class GameMode extends AppCompatActivity {
    private GameClient client;

    private SharedPreferences sharedPref;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_mode);

        sharedPref = getSharedPreferences(SHEARED_PREF, MODE_PRIVATE);
    }

    public void offlineLobby(View view) {
        final Dialog dialog = createDialog(GameMode.this, R.layout.player_count_dialog, WindowManager.LayoutParams.WRAP_CONTENT);

        List<String> numbers = Arrays.asList("2", "3", "4");

        Spinner spinner = dialog.findViewById(R.id.number_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, // a pre-define layout with text views
                android.R.id.text1, // the id of the text view in which to display the toString value
                numbers);

        spinner.setAdapter(adapter);

        Button cancelButton = dialog.findViewById(R.id.cancel_button);
        Button confirmButton = dialog.findViewById(R.id.confirm_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameMode.this, OfflineLobby.class);
                intent.putExtra(PLAYER_COUNT, spinner.getSelectedItem().toString());
                startActivity(intent);
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void onlineLobby(View view){
        Intent intent = new Intent(GameMode.this, OnlineLobby.class);
        startActivity(intent);
    }

}
