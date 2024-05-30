package com.example.final5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class JitsiActivity extends AppCompatActivity {
    public String  senderUserIdforjitsi, senderUserId;
      private DatabaseReference usersRef;
    private  String callingID="", ringingID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jitsi);
         senderUserIdforjitsi = getIntent().getStringExtra("SENDER_USER_ID");
      //  receiverUserId =getIntent ().getExtras ().get ("visit_user_id") .toString();
        senderUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();

        usersRef = FirebaseDatabase.getInstance ().getReference ().child ("Users");
      //  runProgramAfterDelay();
    }
    public void onBackPressed() {
        // Create an Intent to navigate to the target activity
        cancelCallingUserforjitsi();
        Intent intent = new Intent(JitsiActivity.this, Connect_with_friends.class);

        // Start the target activity
        startActivity(intent);

        // Optional: Call finish() if you want to close the current activity
        finish();
    }
    protected  void  onStart() {


        super.onStart();
        URL serverURL = null;
        try {
            serverURL = new URL("https://meet.guifi.net");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        JitsiMeetConferenceOptions conferenceOptions =
                new JitsiMeetConferenceOptions.Builder()
                        .setServerURL(serverURL)
                        .setRoom(senderUserIdforjitsi)
                        .build();
        //  cancelCallingUserforjitsi();
        JitsiMeetActivity.launch(JitsiActivity.this,conferenceOptions);
        //cancelCallingUserforjitsi();
        finish();

    }
    private void cancelCallingUserforjitsi() {
        usersRef.child(senderUserId)
                .child("Calling")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists() && snapshot.hasChild("calling")){
                            callingID = snapshot.child("calling").getValue().toString();

                            usersRef.child(callingID)
                                    .child("Ringing")
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                usersRef.child(senderUserId)
                                                        .child("Calling")
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
//                                                                startActivity(new Intent(CallingActivityforReciver.this, Connect_with_friends.class));
//                                                                finish();
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                        else {
//                            startActivity(new Intent(CallingActivityforReciver.this, Homepage_feature.class));
//                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // from reciver side
        usersRef.child(senderUserId)
                .child("Ringing")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists() && snapshot.hasChild("ringing")){
                            ringingID = snapshot.child("ringing").getValue().toString();

                            usersRef.child(ringingID)
                                    .child("Calling")
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                usersRef.child(senderUserId)
                                                        .child("Ringing")
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                //   startActivity(new Intent(CallingActivityforReciver.this, Connect_with_friends.class));
                                                                //   finish();
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                        else {
//                            startActivity(new Intent(CallingActivityforReciver.this, Connect_with_friends.class));
//                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {


                    }
                });

    }
    private void runProgramAfterDelay() {
        // Create a Handler
        Handler handler = new Handler(Looper.getMainLooper());

        // Define the runnable to be executed
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Code to be executed after the delay
                Toast.makeText(JitsiActivity.this, "Task executed after delay", Toast.LENGTH_SHORT).show();
                // You can call any method or execute any code here
           //     cancelCallingUserforjitsi();
              //  finishTargetActivity();
            }
        };

        // Post the runnable with a delay
        handler.postDelayed(runnable, 20000); // 5000 milliseconds = 5 seconds
    }
    private void finishTargetActivity() {
        // Send a broadcast to finish the target activity
        Intent intent = new Intent("com.example.ACTION_FINISH");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}