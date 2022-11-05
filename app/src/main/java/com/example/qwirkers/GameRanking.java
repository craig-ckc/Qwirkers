package com.example.qwirkers;

import static com.example.qwirkers.Utility.Utilities.PLAYER_COUNT;
import static com.example.qwirkers.Utility.Utilities.PLAYER_LIST;
import static com.example.qwirkers.Utility.Utilities.setAvatar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qwirkers.Utility.EqualSpaceItemDecoration;
import com.example.qwirkers.Utility.RankingAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import Game.Models.Player;

public class GameRanking extends AppCompatActivity {
    private RankingAdapter adapter;
    private RecyclerView players;
    private List<Player> playerList;
    private Player first;

    private TextView winner;
    private TextView first_ranker;
    public ImageView first_rank_avatar;
    private TextView first_ranker_name;
    private TextView first_ranker_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_ranking);

        playerList = (ArrayList<Player>) getIntent().getSerializableExtra(PLAYER_LIST);

        playerList = playerList.stream()
                .sorted(Comparator.comparingInt(Player::getPoints).reversed())
                .collect(Collectors.toList());

        first = playerList.remove(0);

        winner = findViewById(R.id.winner);
        first_ranker = findViewById(R.id.first_ranker);
        first_rank_avatar = findViewById(R.id.first_rank_avatar);
        first_ranker_name = findViewById(R.id.first_ranker_name);
        first_ranker_score = findViewById(R.id.first_ranker_score);

        winner.setText(first.getName());
        first_ranker.setText(String.valueOf(1));
        first_rank_avatar.setImageResource(setAvatar(first.getAvatar()));
        first_ranker_name.setText(first.getName());
        first_ranker_score.setText(String.valueOf(first.getPoints()));

        players = findViewById(R.id.rankers);
        players.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        players.addItemDecoration(new EqualSpaceItemDecoration(20));

        adapter = new RankingAdapter(playerList);
        players.setAdapter(adapter);

    }

    public void back(View view){
        Intent intent = new Intent(GameRanking.this, OfflineLobby.class);
        intent.putExtra(PLAYER_COUNT, String.valueOf(playerList.size() + 1));
        startActivity(intent);
    }

    public void playAgain(View view){
        Intent intent = new Intent(GameRanking.this, OfflineGame.class);

        playerList.add(first);

        for (Player player : playerList) {
            player.emptyHand();
        }

        intent.putExtra(PLAYER_LIST, new ArrayList<>(playerList));
        startActivity(intent);
    }

    public void home(View view){
        Intent intent = new Intent(GameRanking.this, Home.class);
        startActivity(intent);
    }
}
