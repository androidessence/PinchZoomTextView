package com.adammcneilly.pinchzoomtextview.kotlin.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        zoom_toggle.setOnClickListener {
            val newEnabledState = !text_view.zoomEnabled
            text_view.zoomEnabled = newEnabledState
            zoom_toggle.text = if (newEnabledState) getString(R.string.zoom_enabled) else getString(R.string.zoom_disabled)
        }
    }
}