package com.example.qwirkers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder> {
    private final Context context;
    private final List<AvatarViewHolder> viewHolderList;
    private final List<Integer> avatars;
    private View.OnClickListener onClickListener;
    private int currentAvatar;

    public AvatarAdapter(Context context, List<Integer> avatars) {
        this.context = context;
        this.avatars = avatars;
        this.viewHolderList = new ArrayList<>();
    }

    @NonNull
    @Override
    public AvatarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.avatar_selection, parent, false);
        AvatarViewHolder vh = new AvatarViewHolder(view);
        viewHolderList.add(vh);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AvatarViewHolder holder, int position) {
        int avatar = avatars.get(position);
        holder.setAvatar(avatar);
        holder.itemView.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return avatars.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void highlightAvatar() {
        for(AvatarViewHolder vh : viewHolderList)
            vh.highlight();
    }

    public void setCurrentAvatar(int currentAvatar) {
        this.currentAvatar = currentAvatar;

        highlightAvatar();
    }

    public class AvatarViewHolder extends RecyclerView.ViewHolder {
        public final MaterialCardView itemView;
        public ImageView avatar;
        public int avatarInt;

        public AvatarViewHolder(@NonNull View view) {
            super(view);

            itemView = (MaterialCardView) view;
            avatar = view.findViewById(R.id.playerProfile);
        }

        public void setAvatar(int avatarInt) {
            this.avatarInt = avatarInt;

            avatar.setImageResource(setBlockImage(avatarInt));
        }

        public void highlight() {
            if (currentAvatar == avatarInt) {
                itemView.setStrokeColor(ContextCompat.getColor(context, R.color.highlight));
            } else {
                itemView.setStrokeColor(ContextCompat.getColor(context, R.color.transparent));
            }
        }

        private int setBlockImage(int avatar) {
            switch (avatar) {
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
