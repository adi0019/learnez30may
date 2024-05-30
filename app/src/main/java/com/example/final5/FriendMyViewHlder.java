package com.example.final5;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FriendMyViewHlder extends RecyclerView.ViewHolder {
    LinearLayout Message;
    TextView name, liveStatusTextView;
    ImageView profile, videocallbtn;

    public FriendMyViewHlder(@NonNull View itemView) {
        super(itemView);
        profile = itemView.findViewById(R.id.profile);
        name = itemView.findViewById(R.id.name_notification1);
        liveStatusTextView = itemView.findViewById(R.id.liveStatusTextView);
        videocallbtn = itemView.findViewById(R.id.accept_btn);
        Message = itemView.findViewById(R.id.msg);
    }
}
