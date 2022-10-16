package com.example.qwirkers;

import static com.example.qwirkers.Utility.ActivityKey.PLAYER_COUNT;
import static com.example.qwirkers.Utility.ActivityKey.SERVER_ADDRESS;
import static com.example.qwirkers.Utility.ActivityKey.SHEARED_PREF;
import static com.example.qwirkers.Utility.ActivityKey.USER_AVATAR;
import static com.example.qwirkers.Utility.ActivityKey.USER_NAME;

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

    public void playOffline(View view) {
        final Dialog dialog = createDialog(R.layout.player_count_dialog);

        List<String> numbers = new ArrayList<>();
        numbers.add("2");
        numbers.add("3");
        numbers.add("4");

        Spinner spinner = dialog.findViewById(R.id.number_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, numbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

    public void playOnline(View view) {
        final Dialog dialog = createDialog(R.layout.ip_address_dialog);

        EditText input = dialog.findViewById(R.id.ip_address);

        Button cancelButton = dialog.findViewById(R.id.cancel_button);
        Button confirmButton = dialog.findViewById(R.id.confirm_button);

        if(sharedPref.contains(SERVER_ADDRESS)){
            input.setText(sharedPref.getString(SERVER_ADDRESS, ""));
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serverAddress = input.getText().toString();
                String handle = sharedPref.getString(USER_NAME, "Hero");


                int avatar = sharedPref.getInt(USER_AVATAR, -1);

                if(!sharedPref.contains(SERVER_ADDRESS) || !serverAddress.equals(sharedPref.getString(SERVER_ADDRESS, ""))){
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(SERVER_ADDRESS, serverAddress);
                    editor.apply();
                }

                client = new GameClient(message -> runOnUiThread(() -> onMessageReceived(message)));

                client.connect(serverAddress, handle);

                long start = System.currentTimeMillis();

                while(((System.currentTimeMillis() - start) / 1000f) < 0.5){}

                if(client.isConnected()){
                    Toast.makeText(GameMode.this, "Connected", Toast.LENGTH_SHORT).show();

                    ClientHandler.setClient(client);

                    Intent intent = new Intent(GameMode.this, OnlineLobby.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(GameMode.this, "Not connected", Toast.LENGTH_SHORT).show();
                }

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

    public Dialog createDialog(int resource) {
        final Dialog dialog = new Dialog(this, R.style.MyDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(resource);

        dialog.setCanceledOnTouchOutside(true);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.5f;
        lp.gravity = Gravity.BOTTOM;
        dialog.getWindow().setAttributes(lp);

        return dialog;
    }

    public void onMessageReceived(Message msg) {
        if(msg instanceof MoveReceived)
            addChatMessage((MoveReceived) msg);
        else
            addLogMessage(msg.toString());
    }

    protected void addLogMessage(String text) {
    }

    protected void addChatMessage(MoveReceived message) {
    }

}
