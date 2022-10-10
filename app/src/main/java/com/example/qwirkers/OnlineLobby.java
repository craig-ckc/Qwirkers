package com.example.qwirkers;

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

public class OnlineLobby extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private EditText input;

    private AvatarAdapter avatarAdapter;

    private int avatar;

    private RecyclerView player_profile_selection;

    private final String USER_NAME = "USER_NAME";
    private final String USER_AVATAR = "USER_AVATAR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_lobby);

        input = findViewById(R.id.username);

        sharedPref = getPreferences(MODE_PRIVATE);

        String username = sharedPref.getString(USER_NAME, "");

        if(sharedPref.contains(USER_NAME)){
            input.setText(username);
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
    }

    public void cancel(View view) {
        finish();
    }

    public void quite(View view) {
        
    }
}
