package com.example.final5.recomandation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.final5.R;
import com.example.final5.YouTubePlayerActivity;
import java.util.List;

/* loaded from: classes4.dex */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private Context context;
    private List<Video> videoList;

    public VideoAdapter(Context context, List<Video> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_itemr, parent, false);
        return new VideoViewHolder(view);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        final Video video = this.videoList.get(position);
        holder.title.setText(video.getTitle());
        Glide.with(this.context).load(video.getThumbnailUrl()).into(holder.thumbnail);
        holder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.example.final5.recomandation.VideoAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                VideoAdapter.this.openYouTubePlayer(video.getUrl());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openYouTubePlayer(String videoUrl) {
        Intent intent = new Intent(this.context, (Class<?>) YouTubePlayerActivity.class);
        intent.putExtra("video_url", videoUrl);
        this.context.startActivity(intent);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.videoList.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title;

        public VideoViewHolder(View itemView) {
            super(itemView);
            this.thumbnail = (ImageView) itemView.findViewById(R.id.video_thumbnail);
            this.title = (TextView) itemView.findViewById(R.id.video_title);
        }
    }
}