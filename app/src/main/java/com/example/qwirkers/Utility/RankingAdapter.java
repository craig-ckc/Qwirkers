package com.example.qwirkers.Utility;

import static com.example.qwirkers.Utility.Utilities.setAvatar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qwirkers.R;

import java.util.List;

import Game.Models.Player;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.PlayerViewHolder> {
    private List<Player> players;

    public RankingAdapter(List<Player> players) {
        this.players = players;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranked_player, parent, false);
        RankingAdapter.PlayerViewHolder vh = new RankingAdapter.PlayerViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        Player player = players.get(position);
        holder.setPlayer(player);
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder {
        public TextView ranker_rank;
        public TextView ranker_name;
        public ImageView ranker_avatar;
        public TextView ranker_score;
        public Player player;

        public PlayerViewHolder(@NonNull View view) {
            super(view);

            ranker_rank = view.findViewById(R.id.ranker_rank);
            ranker_name = view.findViewById(R.id.ranker_name);
            ranker_avatar = view.findViewById(R.id.ranker_avatar);
            ranker_score = view.findViewById(R.id.ranker_score);
        }

        public void setPlayer(Player player) {
            this.player = player;

            ranker_rank.setText(String.valueOf(players.indexOf(player) + 2));
            ranker_name.setText(player.getName());
            ranker_score.setText(String.valueOf(player.getPoints()));
            ranker_avatar.setImageResource(setAvatar(player.getAvatar()));
        }
    }
}
