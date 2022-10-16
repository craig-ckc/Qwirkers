package com.example.qwirkers;

import static com.example.qwirkers.Utility.ActivityKey.SHEARED_PREF;
import static com.example.qwirkers.Utility.ActivityKey.USER_NAME;
import static com.example.qwirkers.Utility.ActivityKey.USER_AVATAR;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qwirkers.Utility.AvatarAdapter;
import com.example.qwirkers.Utility.EqualSpaceItemDecoration;

import java.util.ArrayList;

import Server.ClientHandler;
import Server.GameClient;

public class OnlineLobby extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private EditText input;

    private AvatarAdapter avatarAdapter;
    private int avatar;
    private RecyclerView player_profile_selection;
    private GameClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_lobby);

        client = ClientHandler.getClient();
        input = findViewById(R.id.username);
        sharedPref = getSharedPreferences(SHEARED_PREF, MODE_PRIVATE);

        if(sharedPref.contains(USER_NAME)){
            input.setText(sharedPref.getString(USER_NAME, ""));
            avatar = sharedPref.getInt(USER_AVATAR, -1);
        }

        ArrayList<Integer> avatars = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            avatars.add(i);
        }

        avatarAdapter = new AvatarAdapter(this, avatars, avatar);

        player_profile_selection = findViewById(R.id.player_profile_selection);
        player_profile_selection.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        player_profile_selection.setAdapter(avatarAdapter);
        player_profile_selection.addItemDecoration(new EqualSpaceItemDecoration(5));

        avatarAdapter.setOnClickListener(view -> {
            // Get view holder of the view.
            AvatarAdapter.AvatarViewHolder viewHolder = (AvatarAdapter.AvatarViewHolder) player_profile_selection.findContainingViewHolder(view);
            if (avatar == viewHolder.avatarInt) {
                avatar = -1;
            } else {
                avatar = viewHolder.avatarInt;
            }

            avatarAdapter.setCurrentAvatar(avatar);
        });
    }

    public void confirm(View view) {
        if(!sharedPref.contains(USER_NAME) || !input.getText().toString().equals(sharedPref.getString(USER_NAME, "")) || avatar != sharedPref.getInt(USER_AVATAR, -1)){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(USER_NAME, input.getText().toString());
            editor.putInt(USER_AVATAR, avatar);
            editor.apply();
        }

        client.ready(sharedPref.getString(USER_NAME, "Hero"), sharedPref.getInt(USER_AVATAR, -1));
    }

    public void cancel(View view) {
        client.disconnect();
        finish();
    }

    public void quite(View view) {
        
    }
}
