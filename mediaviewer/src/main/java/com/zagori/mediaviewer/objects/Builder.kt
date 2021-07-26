package com.zagori.mediaviewer.objects;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.LayoutRes;

import com.zagori.mediaviewer.ModalViewer;
import com.zagori.mediaviewer.interfaces.Formatter;
import com.zagori.mediaviewer.interfaces.OnDismissListener;
import com.zagori.mediaviewer.interfaces.OnImageChangeListener;
import com.zagori.mediaviewer.views.OverlayView;

import java.util.List;

/**
 * Builder class for {@link ModalViewer}
 */
public class Builder<T> {

    private Context context;
    private DataSet<T> dataSet;
    private @ColorInt
    int backgroundColor = Color.BLACK;
    private int startPosition = 0;
    private OnImageChangeListener imageChangeListener;
    private OnDismissListener onDismissListener;
    private View overlayView;
    private int imageMarginPixels = 0;
    private int[] containerPaddingPixels = new int[4];
    private boolean isStatusBarHidden = true;
    private boolean isZoomingAllowed = true;
    private boolean isSwipeToDismissAllowed = true;

    /**
     * Constructor using a context and images urls array for this builder and the {@link ModalViewer} it creates.
     */
    /*public Builder(Context context, T[] images) {
        this(context, new ArrayList<>(Arrays.asList(images)));
    }*/

    /**
     * Constructor using a context and images urls list for this builder and the {@link ModalViewer} it creates.
     */
    public Builder(Context context, List<T> images) {
        this.context = context;
        this.dataSet = new DataSet<>(images);
    }

    /**
     * If you use an non-string collection, you can use custom {@link Formatter} to represent it as url.
     */
    public Builder setFormatter(Formatter<T> formatter) {
        //this.dataSet.formatter = formatter;
        return this;
    }

    /**
     * Set background color resource for viewer
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    @SuppressWarnings("deprecation")
    public Builder setBackgroundColorRes(@ColorRes int color) {
        return this.setBackgroundColor(context.getResources().getColor(color));
    }

    /**
     * Set background color int for viewer
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setBackgroundColor(@ColorInt int color) {
        this.backgroundColor = color;
        return this;
    }

    /**
     * Set background color int for viewer
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setStartPosition(int position) {
        this.startPosition = position;
        return this;
    }

    /**
     * Set {@link OnImageChangeListener} for viewer.
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setImageChangeListener(OnImageChangeListener imageChangeListener) {
        this.imageChangeListener = imageChangeListener;
        return this;
    }

    /**
     * Set overlay view
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setOverlayView(View view) {
        this.overlayView = view;
        return this;
    }

    public Builder setOverlayView(@LayoutRes int overlayRes){
        //this.overlayView = new Overlay(context, overlayRes);
        this.overlayView = new OverlayView(context, overlayRes);
        return this;
    }

    /**
     * Set space between the images in px.
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setImageMarginPx(int marginPixels) {
        this.imageMarginPixels = marginPixels;
        return this;
    }

    /**
     * Set space between the images using dimension.
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setImageMargin(Context context, @DimenRes int dimen) {
        this.imageMarginPixels = Math.round(context.getResources().getDimension(dimen));
        return this;
    }

    /**
     * Set {@code start}, {@code top}, {@code end} and {@code bottom} padding for zooming and scrolling area in px.
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setContainerPaddingPx(int start, int top, int end, int bottom) {
        this.containerPaddingPixels = new int[]{start, top, end, bottom};
        return this;
    }

    /**
     * Set {@code start}, {@code top}, {@code end} and {@code bottom} padding for zooming and scrolling area using dimension.
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setContainerPadding(Context context,
                                       @DimenRes int start, @DimenRes int top,
                                       @DimenRes int end, @DimenRes int bottom) {
        setContainerPaddingPx(
                Math.round(context.getResources().getDimension(start)),
                Math.round(context.getResources().getDimension(top)),
                Math.round(context.getResources().getDimension(end)),
                Math.round(context.getResources().getDimension(bottom))
        );
        return this;
    }

    /**
     * Set common padding for zooming and scrolling area in px.
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setContainerPaddingPx(int padding) {
        this.containerPaddingPixels = new int[]{padding, padding, padding, padding};
        return this;
    }

    /**
     * Set common padding for zooming and scrolling area using dimension.
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setContainerPadding(Context context, @DimenRes int padding) {
        int paddingPx = Math.round(context.getResources().getDimension(padding));
        setContainerPaddingPx(paddingPx, paddingPx, paddingPx, paddingPx);
        return this;
    }

    /**
     * Set status bar visibility. By default is true.
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder hideStatusBar(boolean isStatusBarHidden) {
        this.isStatusBarHidden = isStatusBarHidden;
        return this;
    }

    /**
     * Allow or disallow zooming. By default is true.
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder allowZooming(boolean value) {
        this.isZoomingAllowed = value;
        return this;
    }

    /**
     * Allow or disallow swipe to dismiss gesture. By default is true.
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder allowSwipeToDismiss(boolean value) {
        this.isSwipeToDismissAllowed = value;
        return this;
    }

    /**
     * Set {@link OnDismissListener} for viewer.
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    /*
    * Getters
    * */

    public Context getContext() {
        return context;
    }

    public DataSet<T> getDataSet() {
        return dataSet;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public OnImageChangeListener getImageChangeListener() {
        return imageChangeListener;
    }

    public OnDismissListener getOnDismissListener() {
        return onDismissListener;
    }

    public View getOverlayView() {
        return overlayView;
    }

    public int getImageMarginPixels() {
        return imageMarginPixels;
    }

    public int[] getContainerPaddingPixels() {
        return containerPaddingPixels;
    }

    public boolean isStatusBarHidden() {
        return isStatusBarHidden;
    }

    public boolean isZoomingAllowed() {
        return isZoomingAllowed;
    }

    public boolean isSwipeToDismissAllowed() {
        return isSwipeToDismissAllowed;
    }
}
