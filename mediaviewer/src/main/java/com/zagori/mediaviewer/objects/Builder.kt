package com.zagori.mediaviewer.objects

import android.content.Context
import android.graphics.Color
import android.view.View
import com.zagori.mediaviewer.interfaces.OnDismissListener
import com.zagori.mediaviewer.interfaces.OnImageChangeListener

/**
 * Builder class for ModalViewer
 */
class Builder<T>(val context: Context, images: List<T>) {
    val dataSet: DataSet<T> = DataSet(images)
    var backgroundColor = Color.BLACK
    var startPosition = 0
    var imageChangeListener: OnImageChangeListener? = null
    var onDismissListener: OnDismissListener? = null
    var overlayView: View? = null
    var imageMarginPixels = 0
    var containerPaddingPixels = IntArray(4)
    var isStatusBarHidden = true
    var isZoomingAllowed = true
    var isSwipeToDismissAllowed = true
}