package com.example.final5.recomandation;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.final5.R;
import java.util.List;

/* loaded from: classes4.dex */
public class VideoListAdapter extends BaseAdapter {
    private Context context;
    private ImageLoader imageLoader;
    private List<Video> videos;

    public VideoListAdapter(Context context, List<Video> videos) {
        this.context = context;
        this.videos = videos;
        this.imageLoader = new ImageLoader(Volley.newRequestQueue(context), new ImageLoader.ImageCache() { // from class: com.example.final5.recomandation.VideoListAdapter.1
            @Override // com.android.volley.toolbox.ImageLoader.ImageCache
            public Bitmap getBitmap(String url) {
                return null;
            }

            @Override // com.android.volley.toolbox.ImageLoader.ImageCache
            public void putBitmap(String url, Bitmap bitmap) {
            }
        });
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.videos.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        return this.videos.get(position);
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return position;
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.video_list_itemr, parent, false);
        }
        Video video = this.videos.get(position);
        NetworkImageView thumbnail = (NetworkImageView) convertView.findViewById(R.id.video_thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.video_title);
        thumbnail.setImageUrl(video.getThumbnailUrl(), this.imageLoader);
        title.setText(video.getTitle());
        return convertView;
    }
}