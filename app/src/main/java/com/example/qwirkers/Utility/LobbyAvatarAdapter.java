package com.example.qwirkers.Utility;

import static com.example.qwirkers.Utility.Utilities.setAvatar;

import android.content.Context;
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

public class LobbyAvatarAdapter extends RecyclerView.Adapter<LobbyAvatarAdapter.LobbyAvatarViewHolder> {
    private List<Player> players;
    private final Context context;

    public LobbyAvatarAdapter(Context context, List<Player> players) {
        this.players = players;
        this.context = context;
    }

    @NonNull
    @Override
    public LobbyAvatarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lobby_player, parent, false);
        LobbyAvatarViewHolder vh = new LobbyAvatarViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull LobbyAvatarViewHolder holder, int position) {
        Player player = players.get(position);
        holder.setPlayer(player);
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public void add(Player player) {
        players.add(player);
        notifyItemChanged(players.size() - 1);
    }

    public void setPlayers(List<Player> players){
        this.players = players;
        notifyDataSetChanged();
    }

    public class LobbyAvatarViewHolder extends RecyclerView.ViewHolder {
        public TextView playerName;
        public ImageView avatar;
        private Player player;

        public LobbyAvatarViewHolder(@NonNull View view) {
            super(view);

            playerName = view.findViewById(R.id.player_hand);
            avatar = view.findViewById(R.id.playerProfile);
        }

        public void setPlayer(Player player) {
            this.player = player;

            playerName.setText(player.getName());
            avatar.setImageResource(setAvatar(player.getAvatar()));
        }

    }
}
