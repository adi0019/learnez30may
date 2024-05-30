package com.example.final5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import java.util.Collections;
import java.util.HashMap;

public class notification_fragment extends Fragment {

    private DatabaseReference contactsRef;
    private DatabaseReference friendRequestRef;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private RecyclerView notificationsList;
    private String currentUserId;
    private String imageUrl;
    private String nameDb;
    private String bioDb;


    public static notification_fragment newInstance(String param1, String param2) {
        notification_fragment fragment = new notification_fragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString("param1");
            String mParam2 = getArguments().getString("param2");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_fragmnet, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        currentUserId = mUser.getUid();


        notificationsList = view.findViewById(R.id.notification_list1);
        notificationsList.setLayoutManager(new LinearLayoutManager(getContext()));

        friendRequestRef = FirebaseDatabase.getInstance().getReference().child("Friend Requests");
        contactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        retrieveUserInfo();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Contacts> options = new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(friendRequestRef.child(currentUserId), Contacts.class)
                .build();

        FirebaseRecyclerAdapter<Contacts, NotificationViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Contacts, NotificationViewHolder>(options) {

                    @Override
                    protected void onBindViewHolder(@NonNull NotificationViewHolder holder, int position, @NonNull Contacts model) {
                      //  holder.acceptBtn.setVisibility(View.VISIBLE);
                      //  holder.declineBtn.setVisibility(View.VISIBLE);
                        holder.accept_btn.setVisibility(View.VISIBLE);
                        holder.decline_btn.setVisibility(View.VISIBLE);


                        String listUserId = getRef(position).getKey();
                        DatabaseReference requestTypeRef = getRef(position).child("request_type").getRef();

                        requestTypeRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String type = snapshot.getValue().toString();
                                    if ("received".equals(type)) {
                                        holder.cardView.setVisibility(View.VISIBLE);
                                        userRef.child(listUserId).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    String nameStr = snapshot.child("name").getValue().toString();
                                                    holder.userNameTxt.setText(nameStr);

                                                    holder.accept_btn.setOnClickListener(v -> acceptFriendRequest(listUserId));
                                                    holder.decline_btn.setOnClickListener(v -> declineFriendRequest(listUserId));
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                // Handle possible errors.
                                            }
                                        });
                                    } else {
                                        holder.cardView.setVisibility(View.GONE);
                                    }
                                } else {
                                    holder.cardView.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle possible errors.
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_cardview, parent, false);
                        return new NotificationViewHolder(view);
                    }
                };

        notificationsList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    private void retrieveUserInfo() {
        userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    imageUrl = dataSnapshot.child("image").getValue().toString();
                    nameDb = dataSnapshot.child("name").getValue().toString();
                    bioDb = dataSnapshot.child("bio").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });
    }

    private void acceptFriendRequest(String listUserId) {
        contactsRef.child(currentUserId).child(listUserId).child("Contact").setValue("Saved")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userRef.child(listUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String namef = snapshot.child("name").getValue().toString();
                                    String biof = snapshot.child("bio").getValue().toString();
                                    String urlf = snapshot.child("image").getValue().toString();

                                    HashMap<String, String> userMap = new HashMap<>();
                                    userMap.put("name", namef);
                                    userMap.put("uid", listUserId);
                                    userMap.put("bio", biof);
                                    userMap.put("image", urlf);

                                    HashMap<String, String> currentUserMap = new HashMap<>();
                                    currentUserMap.put("name", nameDb);
                                    currentUserMap.put("uid", currentUserId);
                                    currentUserMap.put("image", imageUrl);
                                    currentUserMap.put("bio", bioDb);

                                    contactsRef.child(currentUserId).child(listUserId).updateChildren(Collections.unmodifiableMap(userMap))
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    contactsRef.child(listUserId).child(currentUserId).updateChildren(Collections.unmodifiableMap(currentUserMap))
                                                            .addOnCompleteListener(task2 -> {
                                                                if (task2.isSuccessful()) {
                                                                    friendRequestRef.child(currentUserId).child(listUserId).removeValue()
                                                                            .addOnCompleteListener(task3 -> {
                                                                                if (task3.isSuccessful()) {
                                                                                    friendRequestRef.child(listUserId).child(currentUserId).removeValue()
                                                                                            .addOnCompleteListener(task4 -> {
                                                                                                if (task4.isSuccessful()) {
                                                                                                    Toast.makeText(getContext(), "Request Accepted", Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            });
                                                                                }
                                                                            });
                                                                }
                                                            });
                                                }
                                            });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle possible errors.
                            }
                        });
                    }
                });
    }

    private void declineFriendRequest(String listUserId) {
        friendRequestRef.child(currentUserId).child(listUserId).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        friendRequestRef.child(listUserId).child(currentUserId).removeValue()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(getContext(), "Request Declined", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
    }
}
