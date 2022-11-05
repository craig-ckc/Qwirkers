package com.example.qwirkers.Utility;

import static com.example.qwirkers.Utility.Utilities.setColor;
import static com.example.qwirkers.Utility.Utilities.setShape;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.qwirkers.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Game.Enums.Dimension;
import Game.Models.Position;
import Game.Models.Tile;

public class BoardAdapter extends ArrayAdapter<Tile> {
    private final Context context;
    private final int resource;
    private final Map<Position, Tile> board;
    private List<Position> validMoves;

    public BoardAdapter(@NonNull Context context, int resource, Map<Position, Tile> board) {
        super(context, resource);

        this.board = board;
        this.resource = resource;
        this.context = context;
        this.validMoves = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return board.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View block = convertView;
        if (block == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            block = inflater.inflate(resource, null);
        }

        View img = block.findViewById(R.id.tile_image);
        img.setLayoutParams(new LinearLayout.LayoutParams(Dimension.TILESIZE.getDim(), Dimension.TILESIZE.getDim()));

        ImageView imageView = (ImageView) img;

        List<Map.Entry<Position, Tile>> stringsList = new ArrayList<>(board.entrySet());
        Tile tile = stringsList.get(position).getValue();


        if (tile == null) {
            imageView.setImageResource(R.drawable.empty);
        } else {
            imageView.setImageResource(setShape(tile.shape()));
            imageView.setColorFilter(setColor(context, tile.color()), PorterDuff.Mode.SRC_IN);
        }

        MaterialCardView card = (MaterialCardView) block;

        Position s = stringsList.get(position).getKey();

        boolean truth = false;


        for (Position pos : validMoves) {
            if (s.equals(pos)) {
                truth = true;
                break;
            }
        }


        if (truth) {
            card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.neutral_100));
        } else {
            card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }

        return card;
    }

    public void highlightValidMoves(List<Position> validMoves) {
        this.validMoves = validMoves;

        notifyDataSetChanged();
    }

    public Position selectedPosition(int position) {
        List<Map.Entry<Position, Tile>> positionList = new ArrayList<>(board.entrySet());

        return positionList.get(position).getKey();
    }
}
