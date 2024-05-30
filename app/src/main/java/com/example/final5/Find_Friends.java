package com.example.final5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

/* loaded from: classes3.dex */
public class Find_Friends extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseReference databaseReference;
    private ArrayList<Contacts> list;
    private String mParam1;
    private String mParam2;
    private MyAdapter myAdapter;
    private RecyclerView recyclerView;

    public static Find_Friends newInstance(String param1, String param2) {
        Find_Friends fragment = new Find_Friends();
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
        View view = inflater.inflate(R.layout.fragment_find__frineds, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.find_friends_list1);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.list = new ArrayList<>();
        MyAdapter myAdapter = new MyAdapter(getContext(), this.list);
        this.myAdapter = myAdapter;
        this.recyclerView.setAdapter(myAdapter);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        this.databaseReference = reference;
        reference.addValueEventListener(new ValueEventListener() { // from class: com.example.final5.Find_Friends.1
            @Override // com.google.firebase.database.ValueEventListener
            public void onDataChange(DataSnapshot dataSnapshot) {
                Find_Friends.this.list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Contacts contact = (Contacts) snapshot.getValue(Contacts.class);
                    if (contact != null) {
                        Find_Friends.this.list.add(contact);
                    }
                }
                Find_Friends.this.myAdapter.notifyDataSetChanged();
            }

            @Override // com.google.firebase.database.ValueEventListener
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Find_Friends.this.getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}