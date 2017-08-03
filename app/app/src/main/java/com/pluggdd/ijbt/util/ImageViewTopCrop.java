package com.pluggdd.ijbt.util;


import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

public class ImageViewTopCrop extends ImageView {
    public ImageViewTopCrop(Context context) {
        super(context);
        setScaleType(ScaleType.MATRIX);
    }

    public ImageViewTopCrop(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScaleType(ScaleType.MATRIX);
    }

    public ImageViewTopCrop(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        final Matrix matrix = getImageMatrix();
        if (getDrawable() != null) {
            final float intrinsicWidth = (float) getDrawable().getIntrinsicWidth();
            ViewTreeObserver viewTreeObserver = getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        float scaleFactor = getWidth() / intrinsicWidth;
                        matrix.setScale(scaleFactor, scaleFactor, 0, 0);
                        setImageMatrix(matrix);
                    }
                });
            }
        }
        return super.setFrame(l, t, r, b);
    }
}