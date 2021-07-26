package com.zagori.mediaviewer

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener
import com.zagori.mediaviewer.interfaces.OnDismissListener
import com.zagori.mediaviewer.interfaces.OnImageChangeListener
import com.zagori.mediaviewer.objects.Builder
import com.zagori.mediaviewer.views.MediaViewerView

class ModalViewer(private val context: Context, images: List<String>) : OnDismissListener,
    DialogInterface.OnKeyListener {
    private val builder by lazy { Builder(images) }
    private var dialog: AlertDialog? = null
    private var viewer: MediaViewerView? = null

    fun start() {
        viewer = MediaViewerView(context)
        viewer!!.allowZooming(builder.isZoomingAllowed)
        viewer!!.allowSwipeToDismiss(builder.isSwipeToDismissAllowed)
        viewer!!.setOnDismissListener(this)
        viewer!!.setBackgroundColor(builder.backgroundColor)
        viewer!!.setOverlayView(builder.overlayView)
        viewer!!.setImageMargin(builder.imageMarginPixels)
        viewer!!.setContainerPadding(builder.containerPaddingPixels)
        viewer!!.setUrls(builder.dataSet, builder.startPosition)
        viewer!!.setPageChangeListener(object : SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                if (builder.imageChangeListener != null) {
                    builder.imageChangeListener!!.onImageChange(position)
                }
            }
        })
        dialog = AlertDialog.Builder(context, dialogStyle)
            .setView(viewer)
            .setOnKeyListener(this)
            .create()
        dialog!!.setOnDismissListener { if (builder.onDismissListener != null) builder.onDismissListener!!.onDismiss() }

        if (builder.dataSet.data.isNotEmpty()) {
            dialog!!.show()
        } else {
            Log.w(TAG, "Images list cannot be empty! Viewer ignored.")
        }
    }

    fun hideStatusBar(hide: Boolean): ModalViewer {
        builder.isStatusBarHidden = hide
        return this
    }

    fun allowZooming(allow: Boolean): ModalViewer {
        builder.isZoomingAllowed = allow
        return this
    }

    fun allowSwipeToDismiss(allow: Boolean): ModalViewer {
        builder.isSwipeToDismissAllowed = allow
        return this
    }

    fun addOverlay(overlay: View?): ModalViewer {
        builder.overlayView = overlay
        return this
    }

    fun setImageChangeListener(onImageChangeListener: OnImageChangeListener?): ModalViewer {
        builder.imageChangeListener = onImageChangeListener
        return this
    }

    /**
     * Fires when swipe to dismiss was initiated
     */
    override fun onDismiss() {
        dialog!!.dismiss()
    }

    /**
     * Resets image on KeyEvent.KEYCODE_BACK to normal scale if needed, otherwise - hide the viewer.
     */
    override fun onKey(dialog: DialogInterface, keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP && !event.isCanceled) {
            if (viewer!!.isScaled) viewer!!.resetScale() else dialog.cancel()
        }
        return true
    }

    private val dialogStyle: Int
        get() = if (builder.isStatusBarHidden) android.R.style.Theme_Translucent_NoTitleBar_Fullscreen else android.R.style.Theme_Translucent_NoTitleBar

    companion object {
        private val TAG = ModalViewer::class.java.simpleName
    }
}