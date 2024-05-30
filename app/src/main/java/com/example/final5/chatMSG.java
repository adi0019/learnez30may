package com.example.final5;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.autofill.HintConstants;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class chatMSG extends AppCompatActivity {
    String OtherUserProfileImageLink;
    String OtherUserStatus;
    String OtherUsername;
    private ChatAdapter adapter;
    ImageView btnSend;
    EditText inputSms;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    public DatabaseReference mUserref;
    private EditText messageEditText;
    private List<chatingmsg> messages;
    private DatabaseReference messagesRef;
    public String receiverId;
    RecyclerView recyclerView;
    private Button sendButton;
    public DatabaseReference smsRef;
    TextView status;
    Toolbar toolbar;
    ImageView userProfileImageAppbar;
    TextView usernameAppbar;

    @SuppressLint("MissingInflatedId")
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_msg);
        this.mUserref = FirebaseDatabase.getInstance().getReference().child("Users");
        this.toolbar = (Toolbar) findViewById(R.id.app_bar);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        this.mAuth = firebaseAuth;
        this.mUser = firebaseAuth.getCurrentUser();
        FirebaseAuth firebaseAuth2 = FirebaseAuth.getInstance();
        this.mAuth = firebaseAuth2;
        firebaseAuth2.getCurrentUser();
        Intent intent = getIntent();
        this.receiverId = intent.getStringExtra("visit_usrer_id");
        this.recyclerView = (RecyclerView) findViewById(R.id.msg_recycler);
        this.inputSms = (EditText) findViewById(R.id.messageEditText);
        this.btnSend = (ImageView) findViewById(R.id.msgsend);
        this.userProfileImageAppbar = (ImageView) findViewById(R.id.userProfileImageAppbar);
        this.usernameAppbar = (TextView) findViewById(R.id.usernamAdapter);
        this.messagesRef = FirebaseDatabase.getInstance().getReference("messages");
        this.messages = new ArrayList();
        this.adapter = new ChatAdapter(this.messages);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(this.adapter);
        LoadOtherUser();
        this.btnSend.setOnClickListener(new View.OnClickListener() { // from class: com.example.final5.chatMSG.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                String message = chatMSG.this.inputSms.getText().toString().trim();
                if (!message.isEmpty()) {
                    chatMSG.this.sendMessage(message);
                    chatMSG.this.inputSms.setText("");
                }
            }
        });
        this.messagesRef.addChildEventListener(new ChildEventListener() { // from class: com.example.final5.chatMSG.2
            @Override // com.google.firebase.database.ChildEventListener
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                chatingmsg chatMessage1 = (chatingmsg) snapshot.getValue(chatingmsg.class);
                chatMSG.this.messages.add(chatMessage1);
                chatMSG.this.adapter.notifyDataSetChanged();
                chatMSG.this.recyclerView.smoothScrollToPosition(chatMSG.this.messages.size() - 1);
            }

            @Override // com.google.firebase.database.ChildEventListener
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
            }

            @Override // com.google.firebase.database.ChildEventListener
            public void onChildRemoved(DataSnapshot snapshot) {
            }

            @Override // com.google.firebase.database.ChildEventListener
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {
            }

            @Override // com.google.firebase.database.ChildEventListener
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMessage(String message) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        chatingmsg chatMessage = new chatingmsg(message, currentUser.getUid(), System.currentTimeMillis());
        this.messagesRef.push().setValue(chatMessage);
    }

    private void LoadOtherUser() {
        this.mUserref.child(this.receiverId).addValueEventListener(new ValueEventListener() { // from class: com.example.final5.chatMSG.3
            @Override // com.google.firebase.database.ValueEventListener
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    chatMSG.this.OtherUsername = snapshot.child(HintConstants.AUTOFILL_HINT_NAME).getValue().toString();
                    chatMSG.this.OtherUserProfileImageLink = snapshot.child("image").getValue().toString();
                    Picasso.get().load(chatMSG.this.OtherUserProfileImageLink).into(chatMSG.this.userProfileImageAppbar);
                    chatMSG.this.usernameAppbar.setText(chatMSG.this.OtherUsername);
                }
            }

            @Override // com.google.firebase.database.ValueEventListener
            public void onCancelled(DatabaseError error) {
            }
        });
    }
}