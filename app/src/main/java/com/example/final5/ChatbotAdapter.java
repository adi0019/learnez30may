package com.example.final5;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/* loaded from: classes3.dex */
public class ChatbotAdapter extends RecyclerView.Adapter<ChatbotAdapter.ViewHolder> {
    private static final int VIEW_TYPE_BOT = 1;
    private static final int VIEW_TYPE_USER = 0;
    private List<ChatMessage> chatMessages;

    public ChatbotAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        return chatMessages.get(position).isFromUser() ? VIEW_TYPE_USER : VIEW_TYPE_BOT;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = viewType == VIEW_TYPE_USER ? R.layout.item_user_message : R.layout.item_bot_message;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatMessage message = chatMessages.get(position);
        holder.messageTextView.setText(message.getMessage());

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        int margin = (int) (holder.itemView.getContext().getResources().getDisplayMetrics().widthPixels * 0.5d);

        if (message.isFromUser()) {
            params.setMarginStart(margin);
            params.setMarginEnd(0);
        } else {
            params.setMarginStart(0);
            params.setMarginEnd(margin);
        }
        holder.itemView.setLayoutParams(params);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return chatMessages.size();
    }

    /* loaded from: classes3.dex */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.MessageTextView);
        }
    }
}
