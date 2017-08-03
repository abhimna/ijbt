package com.pluggdd.ijbt.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class AppImageView extends ImageView {

    ImageSizeChangedCallback sizeChangedCallback = null;
    public AppImageView(Context context) {
        super(context);
    }

    public AppImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AppImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w == 0 || h == 0) {
            return;
        }
        else{
            if(sizeChangedCallback != null)
                sizeChangedCallback.invoke(this, w, w);
        }
    }

    public void setOnImageViewSizeChanged(ImageSizeChangedCallback sizeChangedCallback){
        this.sizeChangedCallback = sizeChangedCallback;

        if (getWidth() != 0 && getHeight() != 0) {
            sizeChangedCallback.invoke(this, getWidth(), getHeight());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        setMeasuredDimension(width, width);
    }

}
