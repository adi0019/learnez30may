package com.example.final5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

import java.util.ArrayList;
import java.util.HashMap;

public class notification extends AppCompatActivity {


    private DatabaseReference friendRequestRef, contactsRef, userRef;

    private RecyclerView notifications_list;
    private FirebaseAuth mAuth;
    private  String currentUserId;

    private FirebaseUser mUser;
    ArrayList<Contacts> list;
    String UserID;
    //private DatabaseReference userRef1;

    String imageUrl, nameDb, bioDb, uid;




//    Button accept_btn, decline_btn;

    public notification() {
    }

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
       // userRef1 = FirebaseDatabase.getInstance().getReference().child("Users");

        notifications_list = findViewById(R.id.notification_list);
        notifications_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        currentUserId =mAuth.getCurrentUser().getUid();

        friendRequestRef = FirebaseDatabase.getInstance().getReference().child("Friend Requests");
        contactsRef =FirebaseDatabase.getInstance().getReference().child("Contacts");
        userRef =FirebaseDatabase.getInstance().getReference().child("Users");
        retrievUserInfo();

    }
    protected  void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Contacts>()
                        .setQuery(friendRequestRef.child(currentUserId), Contacts.class)
                        .build();

        FirebaseRecyclerAdapter<Contacts, NotificationViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Contacts, NotificationViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull NotificationViewHolder holder, int position, @NonNull Contacts model) {
                holder.accept_btn.setVisibility (View.VISIBLE);
                holder.decline_btn.setVisibility (View.VISIBLE) ;
          //      Contacts contacts =list.get(position);
             //   holder.userNameTxt.setText(contacts.getName());
//            final  String listUserId =getRef(position).getKey();
//            final  String listUsername =getRef(position).get
            final  String listUserId =getRef(position).getKey();
//            final String  name = list.get(position).getName();
//            final String  bio = list.get(position).getbio();
//            final String  image = list.get(position).getImage();
                DatabaseReference requestTypeRef =getRef(position).child ("request_type").getRef ();
                requestTypeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String type= snapshot.getValue().toString();
                            if(type.equals("received")){
                             holder.cardView.setVisibility(View.VISIBLE);

                             userRef.child(listUserId).addValueEventListener(new ValueEventListener() {
                                 @Override
                                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.hasChild("image")){
//                                        final  String imageStr= snapshot.child("imgae").getValue().toString();
      //                                  final  String nameStr= snapshot.child("imgae").getValue().toString();

   //                                     Picasso.get().load(imageStr).into(holder.profileImageView);
                                      //  holder.userNameTxt.setText(nameStr);
                                    }


                                        final  String nameStr = snapshot.child("name").getValue().toString();
                                        holder.userNameTxt.setText(nameStr);

                                    holder.accept_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            contactsRef.child(currentUserId).child(listUserId)
                                                    .child("Contact").setValue("Saved")
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                HashMap hashMap=new HashMap();
                                                            //    hashMap.put("status","friend");
                                                           //     hashMap.put("name", name);
                                                                hashMap.put("uid", listUserId);
//                                                                hashMap.put("bio",bio);
//                                                                hashMap.put("image", image);

                                                                HashMap hashMap1=new HashMap();
                                                                hashMap1.put("name", nameDb);
                                                                hashMap1.put("uid", currentUserId);
                                                                hashMap1.put("image",imageUrl);
                                                                hashMap1.put("bio",bioDb);
                                                                //  hashMap1.put("profileImageUrl",myPrfileImageUrl);
                                                                contactsRef.child(mUser.getUid()).child (listUserId).updateChildren (hashMap).addOnCompleteListener(new OnCompleteListener() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task task) {



                                                                        contactsRef.child(listUserId).child(currentUserId)
                                                                                .child("Contact").setValue("Saved")
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if(task.isSuccessful()){
                                                                                            friendRequestRef.child (currentUserId). child (listUserId)
                                                                                                    .removeValue()
                                                                                                    .addOnCompleteListener(new OnCompleteListener <Void> () {
                                                                                                        @Override
                                                                                                        public void onComplete (@NonNull Task<Void> task) {


                                                                                                            {
                                                                                                                if (task.isSuccessful()) {
                                                                                                                    contactsRef.child(listUserId).child(mUser.getUid()).updateChildren(hashMap1).addOnCompleteListener(new OnCompleteListener() {
                                                                                                                        @Override
                                                                                                                        public void onComplete(@NonNull Task task) {


                                                                                                                            friendRequestRef.child (listUserId). child (currentUserId)
                                                                                                                                    .removeValue()
                                                                                                                                    .addOnCompleteListener(new OnCompleteListener <Void> () {
                                                                                                                                        @Override
                                                                                                                                        public void onComplete (@NonNull Task<Void> task) {


                                                                                                                                            {
                                                                                                                                                if (task.isSuccessful()) {


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
                                    });
                                    holder.decline_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            friendRequestRef.child (currentUserId). child (listUserId)
                                                    .removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener <Void> () {
                                                        @Override
                                                        public void onComplete (@NonNull Task<Void> task) {


                                                            {
                                                                if (task.isSuccessful()) {
                                                                    friendRequestRef.child (listUserId). child (currentUserId)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener <Void> () {
                                                                                @Override
                                                                                public void onComplete (@NonNull Task<Void> task) {


                                                                                    {
                                                                                        if (task.isSuccessful()) {
                                                                                            Toast.makeText(notification.this, "Request is cancel", Toast.LENGTH_SHORT).show();

                                                                                        }
                                                                                    }
                                                                                }
                                                                            });

                                                                }
                                                            }
                                                        }
                                                    });

                                        }
                                    });
                                 }

                                 @Override
                                 public void onCancelled(@NonNull DatabaseError error) {

                                 }
                             });
                            }

                        }
                        else{

                            holder.cardView.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @NonNull
            @Override

            public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.notifcation_cardview, parent, false);

                return new NotificationViewHolder(view);
            }
        };
        notifications_list.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }
    public void retrievUserInfo() {
        //  super.onStart();
        // DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        //  String userId = "user_id";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserID=user.getUid();
        userRef.child(UserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            imageUrl = dataSnapshot.child("image").getValue().toString();
                            nameDb = dataSnapshot.child("name").getValue().toString();
                            bioDb = dataSnapshot.child("bio").getValue().toString();
                            uid = dataSnapshot.child("uid").getValue().toString();
                            // userNameET.setText(nameDb);
                            // userBioET.setText(bioDb);
                            // updateurl =imageUrl;


                            //  Picasso.get ().load (imageUrl) .placeholder (R. drawable.profile_image).into(profileImageView);
                            // Glide.with(settings.this).load(imageUrl).into(profileImageView);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }




}
