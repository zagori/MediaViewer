package com.zagori.mediaviewer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.core.view.GestureDetectorCompat;
import androidx.viewpager.widget.ViewPager;

import com.zagori.mediaviewer.R;
import com.zagori.mediaviewer.adapters.ViewerAdapter;
import com.zagori.mediaviewer.interfaces.OnDismissListener;
import com.zagori.mediaviewer.tools.AnimationUtils;
import com.zagori.mediaviewer.tools.SwipeDirectionDetector;
import com.zagori.mediaviewer.tools.SwipeToDismissListener;

import java.util.List;

public class MediaViewerView extends RelativeLayout
        implements OnDismissListener, SwipeToDismissListener.OnViewMoveListener {

    private View backgroundView;
    private MultiTouchViewPager pager;
    private ViewerAdapter adapter;
    private SwipeDirectionDetector directionDetector;
    private ScaleGestureDetector scaleDetector;
    private ViewPager.OnPageChangeListener pageChangeListener;
    private GestureDetectorCompat gestureDetector;

    private ViewGroup dismissContainer;
    private SwipeToDismissListener swipeDismissListener;
    private View overlayView;

    private SwipeDirectionDetector.Direction direction;

    private boolean wasScaled;
    private OnDismissListener onDismissListener;
    private boolean isOverlayWasClicked;

    private boolean isZoomingAllowed = true;
    private boolean isSwipeToDismissAllowed = true;

    public MediaViewerView(Context context) {
        super(context);
        init();
    }

    public MediaViewerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MediaViewerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setUrls(List<String> images, int startPosition) {
        adapter = new ViewerAdapter(getContext(), images, isZoomingAllowed);
        pager.setAdapter(adapter);
        setStartPosition(startPosition);
    }

    @Override
    public void setBackgroundColor(int color) {
        findViewById(R.id.background_view)
                .setBackgroundColor(color);
    }

    public void setOverlayView(View view) {
        this.overlayView = view;
        if (overlayView != null) {
            // check if view already has a parent. if so, detach it
            if (view.getParent() != null) ((ViewGroup)view.getParent()).removeView(view);
            dismissContainer.addView(view);
        }
    }

    public void allowZooming(boolean allowZooming) {
        this.isZoomingAllowed = allowZooming;
    }

    public void allowSwipeToDismiss(boolean allowSwipeToDismiss) {
        this.isSwipeToDismissAllowed = allowSwipeToDismiss;
    }

    public void setImageMargin(int marginPixels) {
        pager.setPageMargin(marginPixels);
    }

    public void setContainerPadding(int[] paddingPixels) {
        pager.setPadding(
                paddingPixels[0],
                paddingPixels[1],
                paddingPixels[2],
                paddingPixels[3]);
    }

    private void init() {
        inflate(getContext(), R.layout.image_viewer, this);

        backgroundView = findViewById(R.id.background_view);
        pager = (MultiTouchViewPager) findViewById(R.id.pager);

        dismissContainer = (ViewGroup) findViewById(R.id.container);
        swipeDismissListener = new SwipeToDismissListener(findViewById(R.id.dismiss_view), this, this);
        dismissContainer.setOnTouchListener(swipeDismissListener);

        directionDetector = new SwipeDirectionDetector(getContext()) {
            @Override
            public void onDirectionDetected(Direction direction) {
                MediaViewerView.this.direction = direction;
            }
        };

        scaleDetector = new ScaleGestureDetector(getContext(),
                new ScaleGestureDetector.SimpleOnScaleGestureListener());

        gestureDetector = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (pager.isScrolled()) {
                    onClick(e, isOverlayWasClicked);
                }
                return false;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        onUpDownEvent(event);

        if (direction == null) {
            if (scaleDetector.isInProgress() || event.getPointerCount() > 1) {
                wasScaled = true;
                return pager.dispatchTouchEvent(event);
            }
        }

        if (!adapter.isScaled(pager.getCurrentItem())) {
            directionDetector.onTouchEvent(event);
            if (direction != null) {
                switch (direction) {
                    case UP:
                    case DOWN:
                        if (isSwipeToDismissAllowed && !wasScaled && pager.isScrolled()) {
                            return swipeDismissListener.onTouch(dismissContainer, event);
                        } else break;
                    case LEFT:
                    case RIGHT:
                        return pager.dispatchTouchEvent(event);
                }
            }
            return true;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onDismiss() {
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
        }
    }

    @Override
    public void onViewMove(float translationY, int translationLimit) {
        float alpha = 1.0f - (1.0f / translationLimit / 4) * Math.abs(translationY);
        backgroundView.setAlpha(alpha);
        if (overlayView != null) overlayView.setAlpha(alpha);
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public void resetScale() {
        adapter.resetScale(pager.getCurrentItem());
    }

    public boolean isScaled() {
        return adapter.isScaled(pager.getCurrentItem());
    }

    public String getUrl() {
        return adapter.getUrl(pager.getCurrentItem());
    }

    public void setPageChangeListener(ViewPager.OnPageChangeListener pageChangeListener) {
        pager.removeOnPageChangeListener(this.pageChangeListener);
        this.pageChangeListener = pageChangeListener;
        pager.addOnPageChangeListener(pageChangeListener);
        pageChangeListener.onPageSelected(pager.getCurrentItem());
    }

    private void setStartPosition(int position) {
        pager.setCurrentItem(position);
    }

    private void onUpDownEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            onActionUp(event);
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            onActionDown(event);
        }

        scaleDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
    }

    private void onActionDown(MotionEvent event) {
        direction = null;
        wasScaled = false;
        pager.dispatchTouchEvent(event);
        swipeDismissListener.onTouch(dismissContainer, event);
        isOverlayWasClicked = dispatchOverlayTouch(event);
    }

    private void onActionUp(MotionEvent event) {
        swipeDismissListener.onTouch(dismissContainer, event);
        pager.dispatchTouchEvent(event);
        isOverlayWasClicked = dispatchOverlayTouch(event);
    }

    private void onClick(MotionEvent event, boolean isOverlayWasClicked) {
        if (overlayView != null && !isOverlayWasClicked) {
            AnimationUtils.animateVisibility(overlayView);
            super.dispatchTouchEvent(event);
        }
    }

    private boolean dispatchOverlayTouch(MotionEvent event) {
        return overlayView != null
                && overlayView.getVisibility() == VISIBLE
                && overlayView.dispatchTouchEvent(event);
    }
}
