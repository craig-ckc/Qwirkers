package com.example.qwirkers.Utility;

import static com.example.qwirkers.Utility.Utilities.setAvatar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qwirkers.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import Game.Models.Player;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {
    private List<Player> players;
    private final Context context;
    private final List<PlayerViewHolder> viewHolderList;
    private Player currentPlayer;

    public PlayerAdapter(Context context, List<Player> players) {
        this.context = context;
        this.players = players;
        this.viewHolderList = new ArrayList<>();
    }

    public PlayerAdapter(Context context, List<Player> players, Player currentPlayer) {
        this.context = context;
        this.players = players;
        this.viewHolderList = new ArrayList<>();

        setCurrentPlayer(currentPlayer);
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_card, parent, false);
        PlayerViewHolder vh = new PlayerViewHolder(view);
        viewHolderList.add(vh);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        Player player = players.get(position);
        holder.setPlayer(player);

        if (currentPlayer == player)
            holder.highlight();
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public void add(Player player) {
        players.add(player);
        notifyItemChanged(players.size() - 1);
    }

    public void remove(int position) {
        players.remove(position);
        notifyItemRemoved(position);
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
        notifyDataSetChanged();
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        highlightPlayer();
        notifyItemChanged(players.indexOf(currentPlayer));
    }

    public Player currentPlayer() {
        return currentPlayer;
    }

    public void highlightPlayer() {
        for (PlayerViewHolder vh : viewHolderList)
            vh.highlight();
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder {
        public MaterialCardView playerCard;
        public TextView playerName;
        public TextView playerPoints;
        public ImageView playerProfile;
        public TextView playerHand;
        public Player player;

        public PlayerViewHolder(@NonNull View view) {
            super(view);

            playerCard = view.findViewById(R.id.player_card);
            playerName = view.findViewById(R.id.player_name);
            playerPoints = view.findViewById(R.id.player_points);
            playerProfile = view.findViewById(R.id.playerProfile);
            playerHand = view.findViewById(R.id.player_hand);
        }

        public void setPlayer(Player player) {
            this.player = player;

            playerName.setText(player.getName());
            playerPoints.setText(String.valueOf(player.getPoints()));
            playerHand.setText(String.valueOf(player.getHand().size()));
            playerProfile.setImageResource(setAvatar(player.getAvatar()));
        }

        public void highlight() {
            if (currentPlayer == player) {
                playerCard.setStrokeColor(ContextCompat.getColor(context, R.color.highlight));
            } else {
                playerCard.setStrokeColor(ContextCompat.getColor(context, R.color.transparent));
            }
        }

    }
}
