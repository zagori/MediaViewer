package com.zagori.sample

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zagori.mediaviewer.ModalViewer
import com.zagori.mediaviewer.views.OverlayView
import com.zagori.sample.databinding.MainActivityBinding
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private val binding by lazy { MainActivityBinding.inflate(layoutInflater) }
    private val posterImages by lazy { ArrayList<String>(listOf(*resources.getStringArray(R.array.poster_media))) }
    private val posterTitles by lazy { ArrayList(listOf(*resources.getStringArray(R.array.poster_titles))) }
    private val posterDescriptions by lazy { ArrayList(listOf(*resources.getStringArray(R.array.poster_descriptions))) }
    private val overlayView by lazy { OverlayView(this, R.layout.overlay_view) }
    private var btnOverlayDelete: Button? = null
    private var txtOverlayTitle: TextView? = null
    private var txtOverlayIndicator: TextView? = null
    private var txtOverlayDescription: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        btnOverlayDelete = overlayView.findViewById(R.id.delete_media)
        txtOverlayTitle = overlayView.findViewById(R.id.title)
        txtOverlayIndicator = overlayView.findViewById(R.id.indicator)
        txtOverlayDescription = overlayView.findViewById(R.id.description)
    }

    fun startModalMediaViewer(view: View?) = ModalViewer
        .load(this, posterImages)
        .hideStatusBar(true)
        .allowZooming(true)
        .allowSwipeToDismiss(true)
        .addOverlay(overlayView)
        //.addOverlay(R.layout.overlay_view)
        .setImageChangeListener { position ->
            txtOverlayIndicator!!.text =
                getString(R.string.overlay_view_page_count, position + 1, posterImages.size)
            txtOverlayTitle!!.text = posterTitles[position]
            txtOverlayDescription!!.text = posterDescriptions[position]
            btnOverlayDelete?.setOnClickListener {
                Toast.makeText(
                    this@MainActivity, "delete post: " + (position + 1), Toast.LENGTH_SHORT
                ).show()

                //remove post and update modalViewer
                /*posterImages.remove(position)
                posterTitles.remove(position)
                startModalMediaViewer(null)*/
            }
        }
        .start()

    fun startPersistentMediaViewer(view: View?) {}
}