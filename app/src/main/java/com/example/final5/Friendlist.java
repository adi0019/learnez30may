package com.example.final5;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/* loaded from: classes3.dex */
public class Friendlist extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String UserID;
    DatabaseReference UsersRef;
    FirebaseRecyclerAdapter<Friends, FriendMyViewHlder> adapter;
    private String currentUserID;
    ImageView findPeopleBtn;
    String listUserId;

 public   String senderid;
    FirebaseAuth mAuth;
    private String mParam1;
    private String mParam2;
    DatabaseReference mRef;
    FirebaseUser mUser;
    DatabaseReference mUserRef;
    RecyclerView myContactsList;
    BottomNavigationView navView;
    FirebaseRecyclerOptions options;
    String receiverUserID;
    RecyclerView recyclerView;
    private DatabaseReference usersRef;
    Button videocall;
    public String visituserid;
    String calledBy = "";
    private String str = "";

    public static Friendlist newInstance(String param1, String param2) {
        Friendlist fragment = new Friendlist();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mParam1 = getArguments().getString(ARG_PARAM1);
            this.mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friendlist, container, false);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        this.mAuth = firebaseAuth;
        this.mUser = firebaseAuth.getCurrentUser();
        this.mRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
        this.usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FirebaseAuth.getInstance().getCurrentUser();
        this.currentUserID = this.mAuth.getCurrentUser().getUid();
        //  this.findPeopleBtn = (ImageView) view.findViewById(R.id.find_people_btn);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.contact_list1);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        LoadFriends("");
      //  loadLiveStatus("");

       // updateUserLiveStatus(currentUserID, "online", listUserId);
        return view;
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        checkForReceivingCall();
    }
    public void onDestroy() {
        super.onDestroy();
        // Set the current user as offline when the activity is destroyed
        updateUserLiveStatus(currentUserID, "offline",  senderid);
    }

    private void checkForReceivingCall() {
        this.usersRef.child(this.currentUserID).child("Ringing").addValueEventListener(new ValueEventListener() { // from class: com.example.final5.Friendlist.1
            @Override // com.google.firebase.database.ValueEventListener
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("ringing")) {
                    Intent callingIntent = new Intent(Friendlist.this.getContext(), CallingActivityforReciver.class);
                    callingIntent.putExtra("visit_user_id", Friendlist.this.calledBy);
                    Friendlist.this.startActivity(callingIntent);
                }
            }

            @Override // com.google.firebase.database.ValueEventListener
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private void updateUserLiveStatus(String userId, String live, String send) {
      //  Query query = this.mRef.child(this.mUser.getUid()).orderByChild("uid").startAt(s).endAt(s + "\uf8ff");
     //   mRef.child(userId).child("live").setValue(live)
      //  mRef.child(this.mUser.getUid()).child("live").setValue(live)
        mRef.child(send)  // Navigate to the current user's node
                .child(userId)       // Navigate to the friend's node
                .child("live")       // Navigate to the `live` attribute
                .setValue(live)
                //   mRef.child(mUser.getUid()).child(visituserid).child("live").setValue(live)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("UpdateStatus", "User live status updated successfully");
                        } else {
                            Log.e("UpdateStatus", "Failed to update user live status", task.getException());
                        }
                    }
                });
    }



    private void LoadFriends(String s) {
        Query query = this.mRef.child(this.mUser.getUid()).orderByChild("uid").startAt(s).endAt(s + "\uf8ff");
        this.options = new FirebaseRecyclerOptions.Builder().setQuery(query, Friends.class).build();
        FirebaseRecyclerAdapter<Friends, FriendMyViewHlder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Friends, FriendMyViewHlder>(this.options) { // from class: com.example.final5.Friendlist.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.firebase.ui.database.FirebaseRecyclerAdapter
            public void onBindViewHolder(FriendMyViewHlder holder, int position, Friends model) {
             senderid =    model.getUid();
            updateUserLiveStatus(currentUserID, "online", senderid);
                Picasso.get().load(model.getImage()).into(holder.profile);
                holder.name.setText(model.getName());
                //  holder.liveStatusTextView.setText(model.isLive());
                //  holder.liveStatusTextView.setText();
                   holder.liveStatusTextView.setText(model.getLive());

                Toast.makeText(getContext(), "message"+model.getLive(), Toast.LENGTH_SHORT).show();


                Friendlist.this.visituserid = model.getUid();
                final String listUserId = getRef(position).getKey();
                holder.Message.setOnClickListener(new View.OnClickListener() { // from class: com.example.final5.Friendlist.2.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        Intent intent = new Intent(Friendlist.this.getContext(), (Class<?>) chatMSG.class);
                        intent.putExtra("visit_usrer_id", listUserId);
                        Friendlist.this.startActivity(intent);
                    }
                });
                holder.videocallbtn.setOnClickListener(new View.OnClickListener() { // from class: com.example.final5.Friendlist.2.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        Intent callingIntent = new Intent(Friendlist.this.getContext(), (Class<?>) CallingActivity1.class);
                        callingIntent.putExtra("visit_user_id", Friendlist.this.visituserid);
                        Friendlist.this.startActivity(callingIntent);
                    }
                });
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public FriendMyViewHlder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list, parent, false);
                return new FriendMyViewHlder(view);
            }
        };
        this.adapter = firebaseRecyclerAdapter;
        firebaseRecyclerAdapter.startListening();
        this.recyclerView.setAdapter(this.adapter);
    }

}