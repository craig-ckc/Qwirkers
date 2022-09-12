package com.example.qwirkers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

import QwirkleGame.Game.Player;

public class OfflineLobby extends AppCompatActivity {
    public static String PLAYERS_MESSAGE = "Players";

    private AvatarAdapter avatarAdapter;
    private LobbyAvatarAdapter lobbyAvatarAdapter;

    private RecyclerView waiting_players;
    private RecyclerView player_profile_selection;

    private TextView name_input;
    private int avatar;

    private ArrayList<Integer> avatars;
    private ArrayList<Player> players;

    private int numOfPlayers;
    private int playersAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_lobby);

        numOfPlayers = Integer.parseInt(getIntent().getStringExtra(Home.PLAYERS_MESSAGE));
        playersAdded = 0;

        players = new ArrayList<>();

        avatars = new ArrayList<>();
        avatars.add(1);
        avatars.add(2);
        avatars.add(3);
        avatars.add(4);
        avatars.add(5);
        avatars.add(6);
        avatars.add(7);
        avatars.add(8);
        avatars.add(9);
        avatars.add(10);
        avatars.add(11);
        avatars.add(12);

        avatarAdapter = new AvatarAdapter(this, avatars);

        lobbyAvatarAdapter = new LobbyAvatarAdapter(this, players);

        waiting_players = findViewById(R.id.waiting_players);
        waiting_players.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        waiting_players.setAdapter(lobbyAvatarAdapter);
        waiting_players.addItemDecoration(new EqualSpaceItemDecoration(5));

        player_profile_selection = findViewById(R.id.player_profile_selection);
        player_profile_selection.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        player_profile_selection.setAdapter(avatarAdapter);
        player_profile_selection.addItemDecoration(new EqualSpaceItemDecoration(5));



        name_input = findViewById(R.id.input_field);


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

    public void addPlayer(View view) {
        Player player = new Player(name_input.getText().toString(), avatar);

        lobbyAvatarAdapter.add(player);

        playersAdded++;

        if(playersAdded == numOfPlayers){
            Button play = findViewById(R.id.confirm_button);

            play.setText(R.string.play);

            play.setOnClickListener(v -> {
                Intent intent = new Intent(this, GamePlay.class);
                intent.putExtra(PLAYERS_MESSAGE, (Serializable) players);
                startActivity(intent);
            });
        }

        name_input.setText("");
        name_input.clearFocus();

        avatar = -1;
        avatarAdapter.setCurrentAvatar(avatar);
    }

    public void cancel(View view) {
        finish();
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, GamePlay.class);
        intent.putExtra(PLAYERS_MESSAGE, (Serializable) players);
        startActivity(intent);
    }
}