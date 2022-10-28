package com.example.qwirkers;

import static com.example.qwirkers.Utility.Utilities.SERVER_ADDRESS;
import static com.example.qwirkers.Utility.Utilities.SHEARED_PREF;
import static com.example.qwirkers.Utility.Utilities.USER_AVATAR;
import static com.example.qwirkers.Utility.Utilities.USER_NAME;
import static com.example.qwirkers.Utility.Utilities.createDialog;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qwirkers.Utility.AvatarAdapter;
import com.example.qwirkers.Utility.EqualSpaceItemDecoration;

import java.util.ArrayList;


public class OnlineLobby extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private EditText input;

    private AvatarAdapter avatarAdapter;
    private RecyclerView avatarSelection;
    private int avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_lobby);

        input = findViewById(R.id.username);
        sharedPref = getSharedPreferences(SHEARED_PREF, MODE_PRIVATE);

        if (sharedPref.contains(USER_NAME)) {
            input.setText(sharedPref.getString(USER_NAME, ""));
            avatar = sharedPref.getInt(USER_AVATAR, -1);
        }

        ArrayList<Integer> avatars = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            avatars.add(i);
        }

        avatarAdapter = new AvatarAdapter(this, avatars, avatar);

        avatarSelection = findViewById(R.id.player_profile_selection);
        avatarSelection.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        avatarSelection.setAdapter(avatarAdapter);
        avatarSelection.addItemDecoration(new EqualSpaceItemDecoration(5));

        avatarAdapter.setOnClickListener(view -> {
            // Get view holder of the view.
            AvatarAdapter.AvatarViewHolder viewHolder = (AvatarAdapter.AvatarViewHolder) avatarSelection.findContainingViewHolder(view);
            if (avatar == viewHolder.avatarInt)
                avatar = -1;
            else
                avatar = viewHolder.avatarInt;

            avatarAdapter.setCurrentAvatar(avatar);
        });
    }

    public void cancel(View view) {
        finish();
    }

    public void confirm(View view) {
        final Dialog dialog = createDialog(OnlineLobby.this, R.layout.ip_address_dialog, WindowManager.LayoutParams.WRAP_CONTENT);

        EditText ipAddress = dialog.findViewById(R.id.ip_address);
        Button cancelButton = dialog.findViewById(R.id.cancel_button);
        Button confirmButton = dialog.findViewById(R.id.confirm_button);

        ipAddress.setText(sharedPref.getString(SERVER_ADDRESS, ""));

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serverAddress = ipAddress.getText().toString();

                if (!sharedPref.contains(SERVER_ADDRESS) || !serverAddress.equals(sharedPref.getString(SERVER_ADDRESS, ""))) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(USER_NAME, input.getText().toString());
                    editor.putInt(USER_AVATAR, avatar);
                    editor.apply();
                }

                Intent intent = new Intent(OnlineLobby.this, OnlineGame.class);
                intent.putExtra(SERVER_ADDRESS, serverAddress);
                startActivity(intent);

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

    /* Decrepit */
    public void _confirm(View view) {
        if (!sharedPref.contains(USER_NAME) || !input.getText().toString().equals(sharedPref.getString(USER_NAME, "")) || avatar != sharedPref.getInt(USER_AVATAR, -1)) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(USER_NAME, input.getText().toString());
            editor.putInt(USER_AVATAR, avatar);
            editor.apply();
        }

        Dialog dialog = createDialog(OnlineLobby.this, R.layout.online_lobby_modal, WindowManager.LayoutParams.WRAP_CONTENT);

        RecyclerView waiting_players = dialog.findViewById(R.id.waiting_players);
        waiting_players.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        // waiting_players.setAdapter(lobbyAvatarAdapter);
        waiting_players.addItemDecoration(new EqualSpaceItemDecoration(5));


        dialog.show();
    }

}
