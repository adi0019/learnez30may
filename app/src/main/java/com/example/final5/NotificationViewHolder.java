package com.example.final5;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes3.dex */
public class NotificationViewHolder extends RecyclerView.ViewHolder {
    Button accept_btn;
    CardView cardView;
    Button decline_btn;
    ImageView profileImageView;
    TextView userNameTxt;

    public NotificationViewHolder(View itemview) {
        super(itemview);
        this.userNameTxt = (TextView) itemview.findViewById(R.id.name_notificationn);
        this.profileImageView = (ImageView) itemview.findViewById(R.id.image_notificationn);
        this.cardView = (CardView) itemview.findViewById(R.id.card_view_notification);
        this.accept_btn = (Button) itemview.findViewById(R.id.accept_btn11);
        this.decline_btn = (Button) itemview.findViewById(R.id.decline_btn11);
    }
}