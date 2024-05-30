package com.example.final5;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class IncomingCallActivity extends AppCompatActivity {

    private String callerId;
    private String callerName;
    private String callerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call);

        // Get caller details from the intent
        Intent intent = getIntent();
        callerId = intent.getStringExtra("callerId");
        callerName = intent.getStringExtra("callerName");
        callerImage = intent.getStringExtra("callerImage");

        // Initialize views
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView callerNameView = findViewById(R.id.callerName);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView callerImageView = findViewById(R.id.callerImage);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button acceptCallButton = findViewById(R.id.acceptCallButton);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button rejectCallButton = findViewById(R.id.rejectCallButton);

        // Set caller name
        callerNameView.setText(callerName);

        // Load caller image using Glide library
        Glide.with(this)
                .load(callerImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.profile_image)
                .error(R.drawable.profile_image)
                .into(callerImageView);

        // Handle button clicks
        acceptCallButton.setOnClickListener(v -> acceptCall());
        rejectCallButton.setOnClickListener(v -> rejectCall());
    }

    private void acceptCall() {
        // Logic to accept the call
        // You can start a new activity to handle the call
        Toast.makeText(IncomingCallActivity.this, "task is completed", Toast.LENGTH_SHORT).show();
      //  startActivity(IncomingCallActivity.this, Connect_with_friends.class);
    }



    private void rejectCall() {
        // Logic to reject the call
        // You can dismiss the incoming call notification and finish this activity
        finish();
    }
}
