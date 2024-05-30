package com.example.final5;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class Homepage_feature extends AppCompatActivity {
    CardView cardView, cardView1, translation, reelscardview, chatbot;

    private ViewPager viewPager;
    private String currentUserID;
    private DatabaseReference usersRef;
    private ImagePagerAdapter adapter;
    String calledBy = "";



    private Handler handler;
    private Runnable runnable;

    FirebaseAuth mAuth;
    private int currentPage = 0;

    // Sample images
    private int[] imageResources = {
            R.drawable.first,
            R.drawable.second,
            R.drawable.thired
            // Add more images as needed
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hompage_test);
        cardView = findViewById(R.id.practice_with_friends);
        cardView1 =findViewById(R.id.practice_with_ai);
        translation = findViewById(R.id.translation);
        reelscardview = findViewById(R.id.reels);
        chatbot = findViewById(R.id.chatbot);
        getSupportActionBar().hide();
        mAuth=FirebaseAuth.getInstance();

        this.usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FirebaseAuth.getInstance().getCurrentUser();
        this.currentUserID = this.mAuth.getCurrentUser().getUid();

        viewPager = findViewById(R.id.pager);
        adapter = new ImagePagerAdapter(this, imageResources);
        viewPager.setAdapter(adapter);



        // Auto-scroll functionality
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (currentPage == adapter.getCount()) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 3000); // Change image every 3 seconds
            }
        };
        handler.post(runnable);



        viewPager = findViewById(R.id.pager);
        adapter = new ImagePagerAdapter(this, imageResources);
        viewPager.setAdapter(adapter);

        chatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Homepage_feature.this, ChatbotACtivity.class));
            }
        });


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Homepage_feature.this, Connect_with_friends.class));
            }
        });

        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Homepage_feature.this, practice_with_aitest.class));
            }
        });
        translation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Homepage_feature.this, Translation1.class));
            }
        });
        reelscardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Homepage_feature.this, Reels.class));
            }
        });
    }
    private void saveTokenToDatabase(String token) {
        // Your code to save the token to your database
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        databaseReference.child("fcmToken").setValue(token);
    }
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.logout:
                //   firebaseAuth.signOut();
                mAuth.signOut();
                // finish();
                startActivity(new Intent(Homepage_feature.this, MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        checkForReceivingCall();
    }

    private void checkForReceivingCall() {
        this.usersRef.child(this.currentUserID).child("Ringing").addValueEventListener(new ValueEventListener() { // from class: com.example.final5.Friendlist.1
            @Override // com.google.firebase.database.ValueEventListener
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("ringing")) {
                    Intent callingIntent = new Intent(Homepage_feature.this, CallingActivityforReciver.class);
                    callingIntent.putExtra("visit_user_id", Homepage_feature.this.calledBy);
                    Homepage_feature.this.startActivity(callingIntent);
                }
            }

            @Override // com.google.firebase.database.ValueEventListener
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }


}