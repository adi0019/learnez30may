package com.example.final5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Reels extends AppCompatActivity
{
    ViewPager2 viewPager2;
    videoadapter adapter;

    private Context context;
    private List<String> videoUrls;
    TextView readMoreTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reels);

        getSupportActionBar().hide();
      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        viewPager2=(ViewPager2)findViewById(R.id.vpager);


        FirebaseRecyclerOptions<videomodel> options =
                new FirebaseRecyclerOptions.Builder<videomodel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("English reels"), videomodel.class)
                        .build();

        adapter=new videoadapter(options);
        viewPager2.setAdapter(adapter);



    }




    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }


    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}