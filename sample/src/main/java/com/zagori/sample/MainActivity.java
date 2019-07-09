package com.zagori.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zagori.mediaviewer.ModalViewer;
import com.zagori.mediaviewer.interfaces.OnImageChangeListener;
import com.zagori.mediaviewer.views.OverlayView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private List<String> posterImages;
    private List<String> posterTitles;
    private List<String> posterDescriptions;

    private OverlayView overlayView;
    private Button btnOverlayDelete;
    private TextView txtOverlayTitle;
    private TextView txtOverlayIndicator;
    private TextView txtOverlayDescription;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        posterImages = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.poster_media)));
        posterTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.poster_titles)));
        posterDescriptions = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.poster_descriptions)));

        overlayView = new OverlayView(this, R.layout.overlay_view);
        btnOverlayDelete = overlayView.findViewById(R.id.delete_media);
        txtOverlayTitle = overlayView.findViewById(R.id.title);
        txtOverlayIndicator = overlayView.findViewById(R.id.indicator);
        txtOverlayDescription = overlayView.findViewById(R.id.description);
    }

    public void startModalMediaViewer(View view){
        ModalViewer
                .load(this, posterImages)
                .hideStatusBar(true)
                .allowZooming(true)
                .allowSwipeToDismiss(true)
                .addOverlay(overlayView)
                //.addOverlay(R.layout.overlay_view)
                .setImageChangeListener(new OnImageChangeListener(){
                    @Override
                    public void onImageChange(final int position) {
                        txtOverlayIndicator.setText(getString(R.string.overlay_view_page_count, position+1, posterImages.size()));
                        txtOverlayTitle.setText(posterTitles.get(position));
                        txtOverlayDescription.setText(posterDescriptions.get(position));

                        btnOverlayDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(MainActivity.this, "delete post: " + (position+1), Toast.LENGTH_SHORT).show();

                                //remove post and update modalViewer
                                /*posterImages.remove(position);
                                posterTitles.remove(position);
                                startModalMediaViewer(null);*/
                            }
                        });
                    }
                })
                .start();
    }

    public void startPersistentMediaViewer(View view){

    }

}
