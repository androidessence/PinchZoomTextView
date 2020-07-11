package com.androidessence.pinchzoomtextview.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.androidessence.pinchzoomtextview.PinchZoomTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final PinchZoomTextView textView = (PinchZoomTextView) findViewById(R.id.text_view);

        final Button enableToggle = (Button) findViewById(R.id.zoom_toggle);
        enableToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean newEnabledState = !textView.isZoomEnabled();
                textView.setZoomEnabled(newEnabledState);
                enableToggle.setText(
                        newEnabledState ? getString(R.string.zoom_enabled) : getString(R.string.zoom_disabled)
                );
            }
        });
    }
}
