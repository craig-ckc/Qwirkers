package com.example.qwirkers;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class GameMode extends AppCompatActivity {
    public static String PLAYERS_MESSAGE = "Players";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_mode);

    }

    public void playOffline(View view) {
        final Dialog dialog = new Dialog (this, R.style.MyDialog);
        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.player_count_dialog);

        dialog.setCanceledOnTouchOutside(true);

        EditText input = dialog.findViewById(R.id.ip_address);

        List<String> numbers = new ArrayList<>();
        numbers.add("2");
        numbers.add("3");
        numbers.add("4");

        Spinner spinner = dialog.findViewById(R.id.number_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, numbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount=0.5f;
        lp.gravity = Gravity.BOTTOM;
        dialog.getWindow().setAttributes(lp);

        Button cancelButton = dialog.findViewById(R.id.cancel_button);
        Button confirmButton = dialog.findViewById(R.id.confirm_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameMode.this, OfflineLobby.class);
                intent.putExtra(PLAYERS_MESSAGE, spinner.getSelectedItem().toString());
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
        final Dialog dialog = new Dialog (this, R.style.MyDialog);
        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.ip_address_dialog);

        dialog.setCanceledOnTouchOutside(true);

        EditText input = dialog.findViewById(R.id.ip_address);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount=0.5f;
        lp.gravity = Gravity.BOTTOM;
        dialog.getWindow().setAttributes(lp);

        Button cancelButton = dialog.findViewById(R.id.cancel_button);
        Button confirmButton = dialog.findViewById(R.id.confirm_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameMode.this, OnlineLobby.class);
                intent.putExtra(PLAYERS_MESSAGE, input.getText().toString());
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
}
