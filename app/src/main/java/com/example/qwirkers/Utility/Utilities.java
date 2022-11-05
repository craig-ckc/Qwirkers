package com.example.qwirkers.Utility;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qwirkers.R;

import java.util.List;

import Game.Enums.Color;
import Game.Enums.Dimension;
import Game.Enums.Shape;

public class Utilities {
    public static final String PLAYER_COUNT = "PLAYER_COUNT";
    public static final String PLAYER_LIST = "PLAYER_LIST";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_AVATAR = "USER_AVATAR";
    public static final String SERVER_ADDRESS = "SERVER_ADDRESS";
    public static final String SHEARED_PREF = "SHEARED_PREF";

    public static Dialog createDialog(Context context, int resource, int gravity, int height) {
        final Dialog dialog = new Dialog(context, R.style.MyDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(resource);

        dialog.setCanceledOnTouchOutside(true);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, height);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.5f;
        lp.gravity = gravity;
        dialog.getWindow().setAttributes(lp);

        return dialog;
    }

//    public static Dialog createDialog(Context context, int resource, int height) {
//        final Dialog dialog = new Dialog(context, R.style.MyDialog);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(true);
//        dialog.setContentView(resource);
//
//        dialog.setCanceledOnTouchOutside(true);
//
//        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, height);
//
//        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//        lp.dimAmount = 0.5f;
//        lp.gravity = Gravity.CENTER;
//        dialog.getWindow().setAttributes(lp);
//
//        return dialog;
//    }

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

    public static int setColor(Context context, Color color) {
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

    public static int setShape(Shape shape) {
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

    public static class LoadingAnimation extends Thread{
        private final Context context;
        private final List<View> circles;

        public LoadingAnimation(Context context, List<View> circles){
            this.context = context;
            this.circles = circles;
        }

        @Override
        public void run() {
            super.run();

            while(!isInterrupted()){
                Animation fade_01 = AnimationUtils.loadAnimation(context, R.anim.fade_01);
                Animation fade_02 = AnimationUtils.loadAnimation(context, R.anim.fade_02);
                Animation fade_03 = AnimationUtils.loadAnimation(context, R.anim.fade_03);

                circles.get(0).startAnimation(fade_01);
                circles.get(1).startAnimation(fade_02);
                circles.get(2).startAnimation(fade_03);

                try {
                    Thread.sleep(1200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
