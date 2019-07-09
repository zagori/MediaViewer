package com.zagori.mediaviewer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.LayoutRes;

public class OverlayView extends RelativeLayout {

    public OverlayView(Context context, @LayoutRes int overlayRes) {
        super(context);
        inflate(getContext(), overlayRes, this);
    }

    public OverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*public Overlay(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }*/


    private void init(@LayoutRes int overlayRes){
        View view = inflate(getContext(), overlayRes, this);
    }

}
