package com.example.qwirkers.Utility;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qwirkers.R;

import Game.Enums.Dimension;

public class Utilities {
    public static final String PLAYER_COUNT = "PLAYER_COUNT";
    public static final String PLAYER_LIST = "PLAYER_LIST";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_AVATAR = "USER_AVATAR";
    public static final String SERVER_ADDRESS = "SERVER_ADDRESS";
    public static final String SHEARED_PREF = "SHEARED_PREF";

    public static Dialog createDialog(Context context, int resource, int height) {
        final Dialog dialog = new Dialog(context, R.style.MyDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(resource);

        dialog.setCanceledOnTouchOutside(true);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, height);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.5f;
        lp.gravity = Gravity.BOTTOM;
        dialog.getWindow().setAttributes(lp);

        return dialog;
    }

    public static RecyclerView createHand(View view, Context context, HandAdapter adapter){
        RecyclerView hand = (RecyclerView) view;
        hand.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        hand.addItemDecoration(new EqualSpaceItemDecoration(5));
        hand.setMinimumHeight((int) Math.round(Dimension.TILESIZE.getDim() * 1.1));
        hand.setMinimumWidth((int) Math.round(Dimension.TILESIZE.getDim() * 6.6));

        hand.setAdapter(adapter);

        return hand;
    }

    public static RecyclerView createPlayers(View view, Context context, PlayerAdapter adapter){
        RecyclerView players = (RecyclerView) view;
        players.setLayoutManager(new GridLayoutManager(context, 2));
        players.addItemDecoration(new EqualSpaceItemDecoration(5));

        players.setAdapter(adapter);

        return players;
    }

    public static GridView createBoard(View view, Context context, BoardAdapter adapter){
        GridView board = (GridView) view;
        board.getLayoutParams().width = (Dimension.TILESIZE.getDim() * (Dimension.DIMX.getDim() + 1));
        board.requestLayout();
        board.setColumnWidth(Dimension.TILESIZE.getDim());

        board.setAdapter(adapter);

        return board;
    }

    public static int setAvatar(int avatar) {
        switch (avatar) {
            case 1:
                return R.drawable.avatar_01;
            case 2:
                return R.drawable.avatar_02;
            case 3:
                return R.drawable.avatar_03;
            case 4:
                return R.drawable.avatar_04;
            case 5:
                return R.drawable.avatar_05;
            case 6:
                return R.drawable.avatar_06;
            case 7:
                return R.drawable.avatar_07;
            case 8:
                return R.drawable.avatar_08;
            case 9:
                return R.drawable.avatar_09;
            case 10:
                return R.drawable.avatar_10;
            case 11:
                return R.drawable.avatar_11;
            case 12:
                return R.drawable.avatar_12;
            default:
                return R.drawable.avatar_empty;

        }
    }

}
