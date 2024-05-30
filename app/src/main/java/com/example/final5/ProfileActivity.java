package com.example.final5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    private String receiverUserID = "", receiverUserName = "", receiverUserImage = "";
    String sendername;
    private String myUsername, myPrfileImageUrl;

    String imageUrl, nameDb, bioDb, uid;
    String userid, name,image,bio;

    String UserID;
    private DatabaseReference userRef1;




    private String visit_user_id = "", profile_name = "";
    private ImageView background_profile_view;
    private TextView name_profile;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String senderUserId, senderImage;
    //  private String currentUserId= "new";
    private DatabaseReference friendRequestRef, requestRef, contactsRef, mUserRef;

    Button add_friend, decline_friend_request;

    TextView country, city, learning, nativie_lang, bio1, occupation;
    String currentState = "new";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_testing);



        receiverUserID = getIntent().getExtras().get("visit_user_id").toString();
        //   receiverUserID = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("visit_user_id")).toString ();
        //  receiverUserID = getIntent().getStringExtra("visit_user_id");
        //  receiverUserID="6BPMWQdYitMmWmSvxnUgyGo2KKn2";

        userRef1 = FirebaseDatabase.getInstance().getReference().child("Users");
        receiverUserImage = getIntent().getExtras().get("profile_image").toString();
        // receiverUserName = getIntent().getExtras ().get ("profile_name") .toString();
        receiverUserName = getIntent().getStringExtra("profile_name");
        name = getIntent().getStringExtra("name");
        image = getIntent().getStringExtra("image");
        bio = getIntent().getStringExtra("bio");
        String occu = getIntent().getStringExtra("occupation");
       // Toast.makeText(ProfileActivity.this, "text"+occu, Toast.LENGTH_SHORT ).show();
        userid = getIntent().getStringExtra("Userid");
        String country1 = getIntent().getStringExtra("country");
        String nativeLanguage1 = getIntent().getStringExtra("nativeLanguage");
        String city1 = getIntent().getStringExtra("city");
        String learning1 = getIntent().getStringExtra("learning");

        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        senderUserId = mAuth.getCurrentUser().getUid();
        // senderImage= mAuth.getCurrentUser().getim()
        sendername = mAuth.getCurrentUser().getDisplayName();
        mUser = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(receiverUserID);
        friendRequestRef = FirebaseDatabase.getInstance().getReference().child("Friend Requests");
        contactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");

        requestRef = FirebaseDatabase.getInstance().getReference().child("Requests");
        // friends = FirebaseDatabase.getInstance().getReference().child("Friends");

        background_profile_view = findViewById(R.id.background_profile_view);
        name_profile = findViewById(R.id.profile_name);
        add_friend = findViewById(R.id.Add_Friend);
        decline_friend_request = findViewById(R.id.decline_friend_request);
        country =findViewById(R.id.country_profile);
        city =findViewById(R.id.profile_city);
        occupation=findViewById(R.id.occupation_profile);
        learning= findViewById(R.id.learning);
        nativie_lang =findViewById(R.id.native_lang_profile);
        bio1= findViewById(R.id.bio_profile);




        Picasso.get().load(receiverUserImage).into(background_profile_view);
        name_profile.setText(receiverUserName);
        city.setText(city1);
        country.setText(country1);
        learning.setText(learning1);
        nativie_lang.setText(nativeLanguage1);
        bio1.setText(bio);
        occupation.setText(occu);


        retrievUserInfo();
        check();
        manageClickEvent();

    }

    private void check() {
        super.onStart();
        contactsRef.child(senderUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(receiverUserID)){
                            currentState ="friends";
                            add_friend.setText("Delete Contact");
                            add_friend.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteContact();
                                }
                            });
                        }
                        else{
                            currentState = "new";
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void manageClickEvent() {
        friendRequestRef.child (senderUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(receiverUserID)) {
                            String requestType = dataSnapshot.child(receiverUserID).child("request_type").getValue().toString();

                            if(requestType.equals("sent")){

                                currentState ="request_sent";
                                add_friend.setText("Cancel Friend Request");


                            } else if (requestType.equals("received")) {

                                currentState ="request_received";
                                add_friend.setText("Accept Friend Request");

                                decline_friend_request.setVisibility(View.VISIBLE);
                                decline_friend_request.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CancelFriendRequest();

                                    }
                                });

                            }

                        }
                        else{
                            contactsRef.child(senderUserId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(dataSnapshot.hasChild(receiverUserID)){
                                                currentState ="friends";
                                                add_friend.setText("Delete Contact");
                                                add_friend.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        deleteContact();
                                                    }
                                                });
                                            }
                                            else{
                                                currentState = "new";
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



        if (senderUserId.equals (receiverUserID))
        {
            add_friend.setVisibility (View.GONE);
        }

        else
        {
            add_friend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentState.equals ("new")) {
                        sendFriendRequest();

                    }
                    if(currentState.equals ("request_sent")){
                        CancelFriendRequest();

                    }
                    if(currentState.equals("request_received")){
                        AcceptFriendRequest();

                    }


                    if (currentState.equals("friends")){
                        deleteContact();
                    }

                }
            });


        }
    }


    private void deleteContact() {
        contactsRef.child(senderUserId).child(receiverUserID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            contactsRef.child(receiverUserID).child(senderUserId)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                currentState = "new";
                                                add_friend.setText("Add Friend");
                                                Toast.makeText(ProfileActivity.this,"Friendship Cancelled!",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }





    private void AcceptFriendRequest() {
        contactsRef.child(senderUserId).child(receiverUserID)
                .child("Contact").setValue("Saved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            HashMap hashMap=new HashMap();
                            hashMap.put("status","friend");
                            hashMap.put("name", receiverUserName);
                            hashMap.put("uid", receiverUserID);
                            hashMap.put("bio",bio);
                            hashMap.put("image", receiverUserImage);

                            HashMap hashMap1=new HashMap();
                            hashMap1.put("name", nameDb);
                            hashMap1.put("uid", senderUserId);
                            hashMap1.put("image",imageUrl);
                            hashMap1.put("bio",bioDb);
                            //  hashMap1.put("profileImageUrl",myPrfileImageUrl);
                            contactsRef.child(mUser.getUid ()).child (receiverUserID).updateChildren (hashMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {



                                    contactsRef.child(receiverUserID).child(senderUserId)
                                            .child("Contact").setValue("Saved")
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        friendRequestRef.child (senderUserId). child (receiverUserID)
                                                                .removeValue()
                                                                .addOnCompleteListener(new OnCompleteListener <Void> () {
                                                                    @Override
                                                                    public void onComplete (@NonNull Task<Void> task) {


                                                                        {
                                                                            if (task.isSuccessful()) {
                                                                                contactsRef.child(receiverUserID).child(mUser.getUid()).updateChildren(hashMap1).addOnCompleteListener(new OnCompleteListener() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task task) {


                                                                                        friendRequestRef.child (receiverUserID). child (senderUserId)
                                                                                                .removeValue()
                                                                                                .addOnCompleteListener(new OnCompleteListener <Void> () {
                                                                                                    @Override
                                                                                                    public void onComplete (@NonNull Task<Void> task) {


                                                                                                        {
                                                                                                            if (task.isSuccessful()) {
                                                                                                                currentState = "friends";
                                                                                                                add_friend.setText("Delete Contact");

                                                                                                                decline_friend_request.setVisibility(View.GONE);

                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                    }
                                                                                });

                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                    }

                                                }

                                            });
                                }
                            });

                        }

                    }

                });
    }

    private void CancelFriendRequest() {

        friendRequestRef.child (senderUserId). child (receiverUserID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener <Void> () {
                    @Override
                    public void onComplete (@NonNull Task<Void> task) {


                        {
                            if (task.isSuccessful()) {
                                friendRequestRef.child (receiverUserID). child (senderUserId)
                                        .removeValue()
                                        .addOnCompleteListener(new OnCompleteListener <Void> () {
                                            @Override
                                            public void onComplete (@NonNull Task<Void> task) {


                                                {
                                                    if (task.isSuccessful()) {
                                                        currentState = "new";
                                                        add_friend.setText("Add Friend");

                                                    }
                                                }
                                            }
                                        });

                            }
                        }
                    }
                });


    }

    private void sendFriendRequest() {
        friendRequestRef.child (senderUserId).child (receiverUserID)
                .child("request_type").setValue ("sent")
                .addOnCompleteListener(new OnCompleteListener<Void> () {
                    public void onComplete (@NonNull Task <Void> task){
                        if (task.isSuccessful () ){
                            friendRequestRef.child (receiverUserID). child (senderUserId)
                                    .child("request_type").setValue ("received")
                                    .addOnCompleteListener(new OnCompleteListener <Void> () {
                                        public void onComplete (@NonNull Task <Void> task){
                                            if(task.isSuccessful()){
                                                currentState ="request_sent";
                                                add_friend.setText("Cancel Friend Request");
                                                Toast.makeText(ProfileActivity.this, "Friend Request sent", Toast.LENGTH_SHORT).show();
                                            }


                                        }

                                    });
                        }

                    }

                });



    }
    public void retrievUserInfo() {
        //  super.onStart();
        // DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        //  String userId = "user_id";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserID=user.getUid();
        userRef1.child(UserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            imageUrl = dataSnapshot.child("image").getValue().toString();
                            nameDb = dataSnapshot.child("name").getValue().toString();
                            bioDb = dataSnapshot.child("bio").getValue().toString();
                            uid = dataSnapshot.child("uid").getValue().toString();
//                            String country= dataSnapshot.child("Country").getValue().toString();
//                            String City= dataSnapshot.child("city").getValue().toString();
//                            String native_languae= dataSnapshot.child("Native_language").getValue().toString();
//                            String country= dataSnapshot.child("Country").getValue().toString();
//                            String country= dataSnapshot.child("Country").getValue().toString();
                            // userNameET.setText(nameDb);
                            // userBioET.setText(bioDb);
                            // updateurl =imageUrl;


                              Picasso.get ().load (image) .placeholder (R. drawable.profile_image).into(background_profile_view);
                            // Glide.with(settings.this).load(imageUrl).into(profileImageView);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


}

