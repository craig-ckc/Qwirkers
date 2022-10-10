package com.example.qwirkers.Utility;

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

import QwirkleGame.Game.Player;

public class LobbyAvatarAdapter extends RecyclerView.Adapter<LobbyAvatarAdapter.LobbyAvatarViewHolder> {
    private final List<Player> players;
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

    public void remove(int position) {
        players.remove(position);
        notifyItemRemoved(position);
    }

    public class LobbyAvatarViewHolder extends RecyclerView.ViewHolder {
        public TextView playerName;
        public ImageView playerProfile;
        private Player player;

        public LobbyAvatarViewHolder(@NonNull View view) {
            super(view);

            playerName = view.findViewById(R.id.player_hand);
            playerProfile = view.findViewById(R.id.playerProfile);
        }

        public void setPlayer(Player player) {
            this.player = player;

            playerName.setText(player.getName());
            playerProfile.setImageResource(setBlockImage(player.getProfileImg()));
        }

        private int setBlockImage(int profileImg) {
            switch (profileImg) {
                case 1:
                    return R.drawable.prof_01;
                case 2:
                    return R.drawable.prof_02;
                case 3:
                    return R.drawable.prof_03;
                case 4:
                    return R.drawable.prof_04;
                case 5:
                    return R.drawable.prof_05;
                case 6:
                    return R.drawable.prof_06;
                case 7:
                    return R.drawable.prof_07;
                case 8:
                    return R.drawable.prof_08;
                case 9:
                    return R.drawable.prof_09;
                case 10:
                    return R.drawable.prof_10;
                case 11:
                    return R.drawable.prof_11;
                case 12:
                    return R.drawable.prof_12;
                default:
                    return R.drawable.empty;

            }
        }

    }
}
