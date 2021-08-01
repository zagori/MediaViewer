package com.zagori.mediaviewer.views

import android.content.Context
import android.view.*
import android.widget.RelativeLayout
import com.zagori.mediaviewer.tools.SwipeToDismissListener.OnViewMoveListener
import com.zagori.mediaviewer.adapters.ViewerAdapter
import com.zagori.mediaviewer.tools.SwipeDirectionDetector
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.core.view.GestureDetectorCompat
import com.zagori.mediaviewer.tools.SwipeToDismissListener
import com.zagori.mediaviewer.R
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.GestureDetector.SimpleOnGestureListener
import com.zagori.mediaviewer.databinding.ImageViewerBinding
import com.zagori.mediaviewer.interfaces.OnDismissListener
import com.zagori.mediaviewer.tools.AnimationUtils
import kotlin.math.abs

class MediaViewerView(context: Context) : RelativeLayout(context), OnDismissListener,
    OnViewMoveListener {

    private var adapter: ViewerAdapter? = null
    private var directionDetector: SwipeDirectionDetector? = null
    private var scaleDetector: ScaleGestureDetector? = null
    private var gestureDetector: GestureDetectorCompat? = null
    private val swipeDismissListener by lazy {
        SwipeToDismissListener(binding.dismissView, this, this)
    }
    private var overlayView: View? = null
    private var direction: SwipeDirectionDetector.Direction? = null
    private var wasScaled = false
    private var onDismissListener: OnDismissListener? = null
    private var isOverlayWasClicked = false
    private var isZoomingAllowed = true
    private var isSwipeToDismissAllowed = true

    private val binding by lazy { ImageViewerBinding.inflate(LayoutInflater.from(getContext())) }

    init {
        addView(binding.root)
        binding.dismissContainer.setOnTouchListener(swipeDismissListener)
        directionDetector = object : SwipeDirectionDetector(context) {
            override fun onDirectionDetected(direction: Direction) {
                this@MediaViewerView.direction = direction
            }
        }
        scaleDetector = ScaleGestureDetector(
            context,
            SimpleOnScaleGestureListener()
        )
        gestureDetector = GestureDetectorCompat(context, object : SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                if (binding.pager.isScrolled) {
                    onClick(e, isOverlayWasClicked)
                }
                return false
            }
        })
    }

    fun setUrls(images: List<String?>?, startPosition: Int) {
        adapter = ViewerAdapter(context, images, isZoomingAllowed)
        binding.pager.adapter = adapter
        setStartPosition(startPosition)
    }

    override fun setBackgroundColor(color: Int) {
        findViewById<View>(R.id.background_view)
            .setBackgroundColor(color)
    }

    fun setOverlayView(view: View) {
        overlayView = view
        if (overlayView != null) {
            // check if view already has a parent. if so, detach it
            if (view.parent != null) (view.parent as ViewGroup).removeView(view)
            binding.dismissContainer.addView(view)
        }
    }

    fun allowZooming(allowZooming: Boolean) {
        isZoomingAllowed = allowZooming
    }

    fun allowSwipeToDismiss(allowSwipeToDismiss: Boolean) {
        isSwipeToDismissAllowed = allowSwipeToDismiss
    }

    fun setImageMargin(marginPixels: Int) {
        binding.pager.pageMargin = marginPixels
    }

    fun setContainerPadding(paddingPixels: IntArray) = binding.pager.setPadding(
        paddingPixels[0], paddingPixels[1], paddingPixels[2], paddingPixels[3]
    )

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        onUpDownEvent(event)
        if (direction == null) {
            if (scaleDetector!!.isInProgress || event.pointerCount > 1) {
                wasScaled = true
                return binding.pager.dispatchTouchEvent(event)
            }
        }
        if (!adapter!!.isScaled(binding.pager.currentItem)) {
            directionDetector!!.onTouchEvent(event)
            if (direction != null) {
                when (direction) {
                    SwipeDirectionDetector.Direction.UP, SwipeDirectionDetector.Direction.DOWN ->
                        if (isSwipeToDismissAllowed && !wasScaled && binding.pager.isScrolled)
                            return swipeDismissListener.onTouch(binding.dismissContainer, event)

                    SwipeDirectionDetector.Direction.LEFT, SwipeDirectionDetector.Direction.RIGHT ->
                        return binding.pager.dispatchTouchEvent(event)
                }
            }
            return true
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onDismiss() {
        if (onDismissListener != null) {
            onDismissListener!!.onDismiss()
        }
    }

    override fun onViewMove(translationY: Float, translationLimit: Int) {
        val alpha = 1.0f - 1.0f / translationLimit / 4 * abs(translationY)
        binding.backgroundView.alpha = alpha
        if (overlayView != null) overlayView!!.alpha = alpha
    }

    fun setOnDismissListener(onDismissListener: OnDismissListener?) {
        this.onDismissListener = onDismissListener
    }

    fun resetScale() {
        adapter!!.resetScale(binding.pager.currentItem)
    }

    val isScaled: Boolean
        get() = adapter!!.isScaled(binding.pager.currentItem)
    val url: String
        get() = adapter!!.getUrl(binding.pager.currentItem)

    fun setPageChangeListener(pageChangeListener: OnPageChangeListener) {
        binding.pager.addOnPageChangeListener(pageChangeListener)
        pageChangeListener.onPageSelected(binding.pager.currentItem)
    }

    private fun setStartPosition(position: Int) {
        binding.pager.currentItem = position
    }

    private fun onUpDownEvent(event: MotionEvent) {
        if (event.action == MotionEvent.ACTION_UP) onActionUp(event)
        if (event.action == MotionEvent.ACTION_DOWN) onActionDown(event)

        scaleDetector!!.onTouchEvent(event)
        gestureDetector!!.onTouchEvent(event)
    }

    private fun onActionDown(event: MotionEvent) {
        direction = null
        wasScaled = false
        binding.pager.dispatchTouchEvent(event)
        swipeDismissListener.onTouch(binding.dismissContainer, event)
        isOverlayWasClicked = dispatchOverlayTouch(event)
    }

    private fun onActionUp(event: MotionEvent) {
        swipeDismissListener.onTouch(binding.dismissContainer, event)
        binding.pager.dispatchTouchEvent(event)
        isOverlayWasClicked = dispatchOverlayTouch(event)
    }

    private fun onClick(event: MotionEvent, isOverlayWasClicked: Boolean) {
        if (overlayView != null && !isOverlayWasClicked) {
            AnimationUtils.animateVisibility(overlayView)
            super.dispatchTouchEvent(event)
        }
    }

    private fun dispatchOverlayTouch(event: MotionEvent): Boolean =
        overlayView != null && overlayView!!.visibility == VISIBLE &&
                overlayView!!.dispatchTouchEvent(event)

}