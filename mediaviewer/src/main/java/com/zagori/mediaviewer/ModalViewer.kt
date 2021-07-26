package com.zagori.mediaviewer;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;

import com.zagori.mediaviewer.interfaces.OnDismissListener;
import com.zagori.mediaviewer.interfaces.OnImageChangeListener;
import com.zagori.mediaviewer.objects.Builder;
import com.zagori.mediaviewer.views.MediaViewerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModalViewer implements OnDismissListener, DialogInterface.OnKeyListener {

    private static final String TAG = ModalViewer.class.getSimpleName();

    private Builder builder;
    private AlertDialog dialog;
    private MediaViewerView viewer;

    public static ModalViewer load(Context context, String[] images){
        return new ModalViewer(context, new ArrayList<>(Arrays.asList(images)));
    }

    public static ModalViewer load(Context context, List<String> images){
        return new ModalViewer(context, images);
    }

    public ModalViewer build(){
        return new ModalViewer();
    }

    private ModalViewer(){ }

    private ModalViewer(Context context, List<String> images){
        this.builder = new Builder<>(context, images);
    }

    public void start(){
        createDialog();
        if (!builder.getDataSet().getData().isEmpty()) {
            dialog.show();
        } else {
            Log.w(TAG, "Images list cannot be empty! Viewer ignored.");
        }
    }




    /*public ModalViewer(Builder builder) {
        this.builder = builder;
        createDialog();
    }*/

    /*public static void start(Builder builder){
        ModalViewer modalViewer = new ModalViewer(builder);
        modalViewer.show();
    }*/

    /**
     * Displays the built viewer if passed images list isn't empty
     */
    /*public void show() {
        if (!builder.getDataSet().getData().isEmpty()) {
            dialog.show();
        } else {
            Log.w(TAG, "Images list cannot be empty! Viewer ignored.");
        }
    }*/

    public ModalViewer hideStatusBar(boolean hide){
        builder.hideStatusBar(hide);
        return this;
    }

    public ModalViewer allowZooming(boolean allow){
        builder.allowZooming(allow);
        return this;
    }

    public ModalViewer allowSwipeToDismiss(boolean allow){
        builder.allowSwipeToDismiss(allow);
        return this;
    }

    public ModalViewer addOverlay(View overlay){
        builder.setOverlayView(overlay);
        return this;
    }

    public ModalViewer addOverlay(@LayoutRes int overlayRes){
        builder.setOverlayView(overlayRes);
        return this;
    }

    public ModalViewer setImageChangeListener(OnImageChangeListener onImageChangeListener){
        builder.setImageChangeListener(onImageChangeListener);
        return this;
    }


    public String getUrl() {
        return viewer.getUrl();
    }

    private void createDialog() {
        viewer = new MediaViewerView(builder.getContext());
        viewer.allowZooming(builder.isZoomingAllowed());
        viewer.allowSwipeToDismiss(builder.isSwipeToDismissAllowed());
        viewer.setOnDismissListener(this);
        viewer.setBackgroundColor(builder.getBackgroundColor());
        viewer.setOverlayView(builder.getOverlayView());
        viewer.setImageMargin(builder.getImageMarginPixels());
        viewer.setContainerPadding(builder.getContainerPaddingPixels());
        viewer.setUrls(builder.getDataSet(), builder.getStartPosition());
        viewer.setPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (builder.getImageChangeListener() != null) {
                    builder.getImageChangeListener().onImageChange(position);
                }
            }
        });

        dialog = new AlertDialog.Builder(builder.getContext(), getDialogStyle())
                .setView(viewer)
                .setOnKeyListener(this)
                .create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (builder.getOnDismissListener() != null) {
                    builder.getOnDismissListener().onDismiss();
                }
            }
        });
    }

    /**
     * Fires when swipe to dismiss was initiated
     */
    @Override
    public void onDismiss() {
        dialog.dismiss();
    }

    /**
     * Resets image on {@literal KeyEvent.KEYCODE_BACK} to normal scale if needed, otherwise - hide the viewer.
     */
    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_UP &&
                !event.isCanceled()) {
            if (viewer.isScaled()) {
                viewer.resetScale();
            } else {
                dialog.cancel();
            }
        }
        return true;
    }

    private @StyleRes
    int getDialogStyle() {
        return builder.isStatusBarHidden()
                ? android.R.style.Theme_Translucent_NoTitleBar_Fullscreen
                : android.R.style.Theme_Translucent_NoTitleBar;
    }


}
