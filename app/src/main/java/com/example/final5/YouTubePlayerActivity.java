package com.example.final5;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

/* loaded from: classes3.dex */
public class YouTubePlayerActivity extends AppCompatActivity {
    private YouTubePlayerView youtubePlayerView;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_youtube_player);
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubePlayerView);
        this.youtubePlayerView = youTubePlayerView;
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() { // from class: com.example.final5.YouTubePlayerActivity.1
            @Override // com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener, com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
            public void onReady(YouTubePlayer youTubePlayer) {
                String videoUrl = YouTubePlayerActivity.this.getIntent().getStringExtra("video_url");
              //  Toast.makeText(YouTubePlayerActivity.this, videoUrl, Toast.LENGTH_SHORT).show();
                if (videoUrl != null) {
                    String videoId = YouTubePlayerActivity.this.extractYouTubeVideoId(videoUrl);
                    youTubePlayer.loadVideo(videoId, 0.0f);
                } else {
                    Toast.makeText(YouTubePlayerActivity.this, "Invalid video URL", Toast.LENGTH_SHORT).show();
                    YouTubePlayerActivity.this.finish();
                }
            }
        });
        this.youtubePlayerView.setEnableAutomaticInitialization(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String extractYouTubeVideoId(String videoUrl) {
        if (videoUrl.contains("watch?v=")) {
            return videoUrl.substring(videoUrl.indexOf("watch?v=") + 8);
        }
        if (videoUrl.contains("youtu.be/")) {
            return videoUrl.substring(videoUrl.indexOf("youtu.be/") + 9);
        }
        return videoUrl;
    }
}