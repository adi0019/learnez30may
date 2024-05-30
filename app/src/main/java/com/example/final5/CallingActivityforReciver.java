package com.example.final5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;


import java.net.URL;
import java.util.HashMap;

public class CallingActivityforReciver extends AppCompatActivity {
    private TextView nameContact;
    private ImageView profileImage;
    private ImageView cancelCallBtn, makeCallBtn;
    private String receiverUserId="", receiverUserImage="", receiverUserName="";
    private String senderUserId="", senderUserImage="", senderUserName="", checker="";
    private  String callingID="", ringingID="";

    public static  final  String REMOTE_MEETING_ROOM ="meetingRoom";

    private MediaPlayer mediaPlayer;


    private DatabaseReference usersRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling_activityfor_reciver);

        receiverUserId =getIntent ().getExtras ().get ("visit_user_id") .toString();
        senderUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();

        usersRef = FirebaseDatabase.getInstance ().getReference ().child ("Users");

        nameContact = findViewById(R.id.name_calling);
        profileImage =findViewById(R.id.profile_image_calling);
        cancelCallBtn =findViewById(R.id.cancel_call);
        makeCallBtn = findViewById(R.id.make_call);
        mediaPlayer = MediaPlayer.create(this, R.raw.ringing);
        getSupportActionBar().hide();

        getAndSetReciverProfileInfo();
        // checkForReceivingCall();

        cancelCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                checker="clicked";
                cancelCallingUser();

            }
        });
        makeCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // call();
                checkForReceivingCall();


            }
        });
    }



    private void getAndSetReciverProfileInfo() {
        usersRef.addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange (DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(receiverUserId).exists()) {
//                       receiverUserImage = dataSnapshot.child(receiverUserId).child("image").getValue().toString();
//                       receiverUserName = dataSnapshot.child(receiverUserId).child("name").getValue().toString();
//
//                      nameContact.setText(receiverUserName);
//                       Picasso.get().load(receiverUserImage).placeholder(R.drawable.profile_image).into(profileImage);
                }
                if (dataSnapshot.child(senderUserId).exists()) {
                    senderUserImage=dataSnapshot.child(senderUserId).child("image").getValue().toString();
                    senderUserName=dataSnapshot.child(senderUserId).child("name").getValue().toString();
                    nameContact.setText(senderUserName);
                    Picasso.get().load(senderUserImage).placeholder(R.drawable.profile_image).into(profileImage);
                }
            }
            @Override
            public void onCancelled (DatabaseError databaseError) {
            }
        });
    }
    protected  void  onStart() {

        super.onStart();


        usersRef.child(receiverUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!checker.equals("clicked") && !snapshot.hasChild("Calling") && !snapshot.hasChild("Ringing")){
                            mediaPlayer.start();
                            final HashMap<String, Object> callingInfo = new HashMap();

                            callingInfo.put ("calling", receiverUserId);
                            usersRef.child (senderUserId)
                                    .child ("Calling")
                                    .updateChildren (callingInfo)
                                    . addOnCompleteListener(task -> {
                                        if (task.isSuccessful () )
                                        {
                                            final HashMap <String, Object> ringingInfo = new HashMap<>();
                                            ringingInfo.put ("ringing", senderUserId);

                                            usersRef.child(receiverUserId)
                                                    .child("Ringing")
                                                    .updateChildren(ringingInfo);
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(senderUserId).hasChild("Ringing") && !snapshot.child(senderUserId).hasChild("Calling")){
                    makeCallBtn.setVisibility(View.VISIBLE);
                }
                if(snapshot.child(receiverUserId).child("Ringing").hasChild("picked")){
                    try {
                        Intent intent = new Intent(CallingActivityforReciver.this, JitsiActivity.class);

                        // Pass the sender ID to the target activity
                        intent.putExtra("SENDER_USER_ID", senderUserId);

                        // Start the target activity
                        startActivity(intent);
                        finish();

//
//                        //  URL serverURL = new URL("https://meet.jit.si");
//                        URL serverURL = new URL("https://meet.guifi.net");
//                        JitsiMeetConferenceOptions conferenceOptions =
//                                new JitsiMeetConferenceOptions.Builder()
//                                        .setServerURL(serverURL)
//                                        .setFeatureFlag("welcomepage.enabled", false)
//                                        .setRoom(receiverUserId)
//                                        .build();
//                        JitsiMeetActivity.launch(CallingActivityforReciver.this,conferenceOptions);
//                      //  cancelCallingUserforjitsi();
//                        finish();

                    }
                    catch (Exception exception){
                        Toast.makeText(CallingActivityforReciver.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void cancelCallingUser() {
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
                                                                startActivity(new Intent(CallingActivityforReciver.this, Connect_with_friends.class));
                                                                finish();
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                        else {
                            startActivity(new Intent(CallingActivityforReciver.this, Homepage_feature.class));
                            finish();
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
                                                                startActivity(new Intent(CallingActivityforReciver.this, Connect_with_friends.class));
                                                                finish();
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                        else {
                            startActivity(new Intent(CallingActivityforReciver.this, Connect_with_friends.class));
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {


                    }
                });

    }
    private  void  call(){
        final HashMap<String, Object> callingPickUpMap = new HashMap<>();
        callingPickUpMap.put("picked", "picked");
        usersRef.child(senderUserId).child("Ringing")
                .updateChildren(callingPickUpMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        try {
                            Intent intent = new Intent(CallingActivityforReciver.this, JitsiActivity.class);

                            // Pass the sender ID to the target activity
                            intent.putExtra("SENDER_USER_ID", senderUserId);

                            // Start the target activity
                            startActivity(intent);
                            finish();
                        //   URL serverURL = new URL("https://meet.jit.si");
//                        URL serverURL = new URL("https://meet.guifi.net");
//                        JitsiMeetConferenceOptions conferenceOptions =
//                                new JitsiMeetConferenceOptions.Builder()
//                                        .setServerURL(serverURL)
//                                        .setRoom(senderUserId)
//                                        .build();
//                      //  cancelCallingUserforjitsi();
//                        JitsiMeetActivity.launch(CallingActivityforReciver.this,conferenceOptions);
//                        finish();

                    }
                    catch (Exception exception){
                        Toast.makeText(CallingActivityforReciver.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    }
                });
    }
//    private void checkForReceivingCall() {
//        usersRef.child(senderUserId).child("Calling").addValueEventListener(new ValueEventListener() { // from class: com.example.final5.Friendlist.1
//            @Override // com.google.firebase.database.ValueEventListener
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists() || dataSnapshot.hasChild("calling") ) {
//                  call();
//
//                }
//                else  {
//                 //  call();
//                    startActivity(new Intent(CallingActivityforReciver.this, Homepage_feature.class));
//
//                }
//
//
//            }
//
//            @Override // com.google.firebase.database.ValueEventListener
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
//    }
private void checkForReceivingCall() {
    usersRef.child(senderUserId).child("Ringing").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                call();
                finish();// Assuming 'call' is your method to handle the call.
            } else {
                Toast.makeText(CallingActivityforReciver.this, "The calling person ended the call", Toast.LENGTH_SHORT).show();

              //  cancelCallingUserforjitsi();
              //  makeCallBtn.setVisibility(View.GONE);
              //  mediaPlayer.stop();

//                startActivity(new Intent(CallingActivityforReciver.this, Homepage_feature.class));
//                finish();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Handle database error here if needed
            Toast.makeText(CallingActivityforReciver.this, "Failed to read data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
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


}