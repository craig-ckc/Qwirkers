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
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qwirkers.Utility.AvatarAdapter;
import com.example.qwirkers.Utility.EqualSpaceItemDecoration;
import com.example.qwirkers.Utility.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class OnlineLobby extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private EditText input;

    private AvatarAdapter avatarAdapter;
    private RecyclerView avatarSelection;
    private int avatar;

    private Dialog dialog;
    private Utilities.LoadingAnimation loading;

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

    @Override
    protected void onResume() {
        super.onResume();

        if (dialog != null)
            dialog.dismiss();

        if (loading != null)
            loading.interrupt();
    }

    public void cancel(View view) {
        finish();
    }

    public void confirm(View view) {
        dialog = createDialog(OnlineLobby.this, R.layout.ip_address_dialog, Gravity.BOTTOM, WindowManager.LayoutParams.WRAP_CONTENT);

        EditText ipAddress = dialog.findViewById(R.id.ip_address);
        Button cancelButton = dialog.findViewById(R.id.cancel_button);
        Button confirmButton = dialog.findViewById(R.id.confirm_button);

        ipAddress.setText(sharedPref.getString(SERVER_ADDRESS, ""));

        dialog.findViewById(R.id.loading).setVisibility(View.INVISIBLE);

        List<View> circles = Arrays.asList(
                dialog.findViewById(R.id.circle_01),
                dialog.findViewById(R.id.circle_02),
                dialog.findViewById(R.id.circle_03));

        loading = new Utilities.LoadingAnimation(OnlineLobby.this, circles);
        loading.start();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.findViewById(R.id.loading).setVisibility(View.VISIBLE);

//                if(loading.isInterrupted())
//                    loading.start();

                String serverAddress = ipAddress.getText().toString();

                SharedPreferences.Editor editor = sharedPref.edit();

                if (!sharedPref.contains(SERVER_ADDRESS) || !serverAddress.equals(sharedPref.getString(SERVER_ADDRESS, ""))) {
                    editor.putString(USER_NAME, input.getText().toString());
                    editor.putString(SERVER_ADDRESS, serverAddress);
                }

                if (!sharedPref.contains(USER_AVATAR) || avatar != sharedPref.getInt(USER_AVATAR, -1)) {
                    editor.putInt(USER_AVATAR, avatar);
                }

                editor.apply();

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

}
