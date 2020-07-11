package com.androidessence.pinchzoomtextview.sample

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.androidessence.pinchzoomtextview.PinchZoomTextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<PinchZoomTextView>(R.id.text_view)
        val zoomToggle = findViewById<TextView>(R.id.zoom_toggle)

        zoomToggle.setOnClickListener {
            val newEnabledState = !textView.zoomEnabled
            textView.zoomEnabled = newEnabledState
            zoomToggle.text = if (newEnabledState) getString(R.string.zoom_enabled) else getString(R.string.zoom_disabled)
        }
    }
}