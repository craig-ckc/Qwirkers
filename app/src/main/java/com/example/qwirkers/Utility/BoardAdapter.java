package com.example.qwirkers.Utility;

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

import Game.Enums.Color;
import Game.Enums.Dimension;
import Game.Enums.Shape;
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
            imageView.setImageResource(getTile(tile.getShape()));
            imageView.setColorFilter(getColor(tile.getColor()), PorterDuff.Mode.SRC_IN);
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

//        for (Position pos : validMoves) {
//            int position = getPos(pos);
//            notifyDataSetChanged();
//        }

        notifyDataSetChanged();
    }

    private int getPos(Position pos) {
        return (Dimension.DIMX.getDim() * (pos.getX() % Dimension.DIMX.getDim())) + (Dimension.DIMY.getDim() * (pos.getY() % Dimension.DIMY.getDim()));
    }


    public Position selectedPosition(int position) {
        List<Map.Entry<Position, Tile>> positionList = new ArrayList<>(board.entrySet());

        return positionList.get(position).getKey();
    }

    private int getTile(Shape shape) {
        switch (shape) {
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

    private int getColor(Color color) {
        switch (color) {
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
}
