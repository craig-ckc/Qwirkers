package com.example.qwirkers.Utility;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class EqualSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int spaceHeight;

    public EqualSpaceItemDecoration(int spaceHeight) {
        this.spaceHeight = spaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.bottom = spaceHeight;
        outRect.top = spaceHeight;
        outRect.left = spaceHeight;
        outRect.right = spaceHeight;
    }
}
