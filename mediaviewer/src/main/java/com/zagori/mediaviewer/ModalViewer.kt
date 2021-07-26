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
    private val dialog by lazy {
        AlertDialog.Builder(context, dialogStyle)
            .setOnKeyListener(this)
            .setOnDismissListener { builder.onDismissListener?.onDismiss()  }
            .create()
    }
    private val viewer by lazy {
        MediaViewerView(context).apply {
            setOnDismissListener(this)
            setBackgroundColor(builder.backgroundColor)
            setImageMargin(builder.imageMarginPixels)
            setContainerPadding(builder.containerPaddingPixels)
            setUrls(builder.medias, builder.startPosition)
            setPageChangeListener(object : SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    builder.imageChangeListener?.onImageChange(position)
                }
            })
        }
    }

    fun start() {
        if (builder.medias.isEmpty()) {
            Log.w(TAG, "Images list cannot be empty! Viewer ignored.")
            return
        }

        dialog.apply { setView(viewer) }.show()
    }

    fun hideStatusBar(hide: Boolean): ModalViewer {
        builder.isStatusBarHidden = hide
        return this
    }

    fun allowZooming(allow: Boolean): ModalViewer {
        viewer.allowZooming(allow)
        return this
    }

    fun allowSwipeToDismiss(allow: Boolean): ModalViewer {
        viewer.allowSwipeToDismiss(allow)
        return this
    }

    fun addOverlay(overlay: View?): ModalViewer {
        viewer.setOverlayView(overlay)
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
        dialog.dismiss()
    }

    /**
     * Resets image on KeyEvent.KEYCODE_BACK to normal scale if needed, otherwise - hide the viewer.
     */
    override fun onKey(dialog: DialogInterface, keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP && !event.isCanceled) {
            if (viewer.isScaled) viewer.resetScale() else dialog.cancel()
        }
        return true
    }

    private val dialogStyle: Int
        get() = if (builder.isStatusBarHidden) android.R.style.Theme_Translucent_NoTitleBar_Fullscreen else android.R.style.Theme_Translucent_NoTitleBar

    companion object {
        private val TAG = ModalViewer::class.java.simpleName
    }
}