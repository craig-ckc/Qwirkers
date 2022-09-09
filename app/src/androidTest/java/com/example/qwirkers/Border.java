package com.example.qwirkers;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Border extends Drawable {
    public Paint paint;
    public Rect bounds_rect;

    public Border(int colour, int width) {
        this.paint = new android.graphics.Paint();
        this.paint.setColor(colour);
        this.paint.setStrokeWidth(width);
        this.paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onBoundsChange(@NonNull Rect bounds) {
        super.onBoundsChange(bounds);
        this.bounds_rect = bounds;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawRect(this.bounds_rect, this.paint);
    }

    @Override
    public void setAlpha(int i) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
