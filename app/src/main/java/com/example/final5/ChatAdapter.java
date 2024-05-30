package com.example.final5;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/* loaded from: classes3.dex */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private static final int VIEW_TYPE_RECEIVER = 1;
    private static final int VIEW_TYPE_SENDER = 0;
    private String currentUserID;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private List<chatingmsg> messages;

    public ChatAdapter(List<chatingmsg> messages) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        this.mAuth = firebaseAuth;
        this.mUser = firebaseAuth.getCurrentUser();
        this.currentUserID = this.mAuth.getCurrentUser().getUid();
        this.messages = messages;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = viewType == 0 ? R.layout.item_sender_chating : R.layout.item_reciver_chating;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    private String formatTimestamp(long timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault());
        return dateFormat.format(new Date(timestamp));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder holder, int position) {
        chatingmsg message = this.messages.get(position);
        holder.messageTextView.setText(message.getMessage());
        holder.time.setText(formatTimestamp(message.getTimestamp()));
    }


    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.messages.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        chatingmsg message = this.messages.get(position);
        String senderId = message.getSender();
        if (senderId.equals(this.currentUserID)) {
            return 0;
        }
        return 1;
    }

    /* loaded from: classes3.dex */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        TextView senderTextView;
        TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            this.messageTextView = (TextView) itemView.findViewById(R.id.messageTextView1);
            this.time = (TextView) itemView.findViewById(R.id.timestampTextView);
        }
    }
}