package com.example.qwirkers.Utility;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qwirkers.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import QwirkleGame.Enums.Color;
import QwirkleGame.Enums.Dimension;
import QwirkleGame.Enums.Shape;
import QwirkleGame.Game.Tile;

public class HandAdapter extends RecyclerView.Adapter<HandAdapter.TileViewHolder> {
    private final Context context;
    private final List<TileViewHolder> viewHolderList;
    private View.OnClickListener onClickListener;
    private List<Tile> tiles;
    private Tile selectedTile;

    public HandAdapter(Context context, List<Tile> tiles) {
        this.context = context;
        this.tiles = tiles;
        this.viewHolderList = new ArrayList<>();
    }

    @NonNull
    @Override
    public TileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_tile, parent, false);
        HandAdapter.TileViewHolder vh = new HandAdapter.TileViewHolder(view);
        viewHolderList.add(vh);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull TileViewHolder holder, int position) {
        Tile tile = tiles.get(position);
        holder.setTile(tile);
        holder.itemView.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return tiles.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void add(Tile tile) {
        tiles.add(tile);
        notifyItemChanged(tiles.size() - 1);
    }

    public void add(List<Tile> tiles) {
        for(Tile tile :tiles){
            add(tile);
        }
    }

    public void remove(int position) {
        tiles.remove(position);
        notifyItemRemoved(position);
    }

    public void remove(Tile tile) {
        remove(tiles.indexOf(tile));
    }

    public void remove(List<Tile> tiles) {
        for(Tile tile :tiles){
            remove(tile);
        }
    }

    public Tile get(int position) {
        return tiles.get(position);
    }

    public void setSelectedTile(Tile selectedTile) {
        this.selectedTile = selectedTile;

        highlightTile();
    }

    public void highlightTile() {
        for(TileViewHolder vh : viewHolderList)
            vh.highlight();
    }

    public void setTiles(List<Tile> tiles) {
        this.tiles.clear();

        for(Tile tile : tiles){
            add(tile);
        }


//        notifyDataSetChanged();
    }

    public List<TileViewHolder> getViewHolderList() {
        return viewHolderList;
    }

    public class TileViewHolder extends RecyclerView.ViewHolder {
        public final MaterialCardView itemView;
        public ImageView tileImage;
        public Tile tile;

        public TileViewHolder(@NonNull View view) {
            super(view);

            itemView = (MaterialCardView) view;
            tileImage = view.findViewById(R.id.tile_image);
        }

        public void setTile(Tile tile) {
            this.tile = tile;
            tileImage.setLayoutParams(new LinearLayout.LayoutParams(Dimension.TILESIZE.getDim(), Dimension.TILESIZE.getDim()));
            tileImage.setImageResource(getTile(tile.getShape()));
            tileImage.setColorFilter(getColor(tile.getColor()), PorterDuff.Mode.SRC_IN);
        }

        private int getTile(Shape shape){
            switch (shape){
                case STAR:
                    return R.drawable.ic_star;
                case CROSS:
                    return R.drawable.ic_cross;
                case CIRCLE:
                    return R.drawable.ic_circle;
                case CLOVER:
                    return R.drawable.ic_clover;
                case SQUARE:
                    return R.drawable.ic_square;
                case DIAMOND:
                    return R.drawable.ic_diamond;
                default:
                    return R.drawable.ic_empty;
            }
        }

        private int getColor(Color color){
            switch (color){
                case RED:
                    return ContextCompat.getColor(context, R.color.red);
                case BLUE:
                    return ContextCompat.getColor(context, R.color.blue);
                case GREEN:
                    return ContextCompat.getColor(context, R.color.green);
                case ORANGE:
                    return ContextCompat.getColor(context, R.color.orange);
                case PURPLE:
                    return ContextCompat.getColor(context, R.color.purple);
                case YELLOW:
                    return ContextCompat.getColor(context, R.color.yellow);
                default:
                    return ContextCompat.getColor(context, R.color.transparent);
            }
        }

        public void highlight() {
            if (selectedTile == tile) {
                itemView.setStrokeColor(ContextCompat.getColor(context, R.color.highlight));
            } else {
                itemView.setStrokeColor(ContextCompat.getColor(context, R.color.transparent));
            }
        }
    }
}
