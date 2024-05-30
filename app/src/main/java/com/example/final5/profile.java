package com.example.final5;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class profile extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int GALLERY_PICK = 1;

    private Uri imageUri;
    private String userID;
    private DatabaseReference userRef;
    private String downloadUrl;

    private FirebaseAuth mAuth;
    private StorageReference storageProfilePicsRef;
    private ProgressDialog progressDialog;
    private ImageView profileImageView;
    private EditText userBioET, userNameET, cityET, countryET, learningET, nativeLangET, occupationET;
    private Button saveBtn;

    public static profile newInstance(String param1, String param2) {
        profile fragment = new profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userID = user.getUid();
        }

        progressDialog = new ProgressDialog(getContext());
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        saveBtn = view.findViewById(R.id.save_settings_btn);
        userNameET = view.findViewById(R.id.username_settings);
        userBioET = view.findViewById(R.id.bio_settings);
        profileImageView = view.findViewById(R.id.settings_profile_image);
        countryET = view.findViewById(R.id.country_profile1);
        cityET = view.findViewById(R.id.profile_city1);
        learningET = view.findViewById(R.id.learning1);
        nativeLangET = view.findViewById(R.id.native_lang_profile1);
        occupationET = view.findViewById(R.id.occupation_profile1);

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
            }
        });

        retrieveUserInfo();
        return view;
    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == getActivity().RESULT_OK && data != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
        }
    }

    private void saveUserInfo() {
        if (imageUri != null) {
            progressDialog.setMessage("Updating Profile...");
            progressDialog.show();

            final StorageReference filePath = storageProfilePicsRef.child(userID + ".jpg");
            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadUrl = uri.toString();
                            saveToDatabase();
                        }
                    });
                }
            });
        } else {
            saveToDatabase();
        }
    }

    private void saveToDatabase() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("uid", userID);
        userMap.put("image", downloadUrl);
        userMap.put("name", userNameET.getText().toString());
        userMap.put("bio", userBioET.getText().toString());
        userMap.put("city", cityET.getText().toString());
        userMap.put("country", countryET.getText().toString());
        userMap.put("learning", learningET.getText().toString());
        userMap.put("nativeLanguage", nativeLangET.getText().toString());
        userMap.put("occupation", occupationET.getText().toString());

        userRef.child(userID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(profile.this.getContext(),  Homepage_feature.class);
                    startActivity(intent);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void retrieveUserInfo() {
        userRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("image")) {
                        String imageUrl = dataSnapshot.child("image").getValue().toString();
                        Picasso.get().load(imageUrl).placeholder(R.drawable.profile_image).into(profileImageView);
                    }

                    if (dataSnapshot.hasChild("name")) {
                        String name = dataSnapshot.child("name").getValue().toString();
                        userNameET.setText(name);
                    }

                    if (dataSnapshot.hasChild("bio")) {
                        String bio = dataSnapshot.child("bio").getValue().toString();
                        userBioET.setText(bio);
                    }

                    if (dataSnapshot.hasChild("city")) {
                        String city = dataSnapshot.child("city").getValue().toString();
                        cityET.setText(city);
                    }

                    if (dataSnapshot.hasChild("country")) {
                        String country = dataSnapshot.child("country").getValue().toString();
                        countryET.setText(country);
                    }

                    if (dataSnapshot.hasChild("occupation")) {
                        String occupation = dataSnapshot.child("occupation").getValue().toString();
                        occupationET.setText(occupation);
                    }

                    if (dataSnapshot.hasChild("nativeLanguage")) {
                        String nativeLanguage = dataSnapshot.child("nativeLanguage").getValue().toString();
                        nativeLangET.setText(nativeLanguage);
                    }

                    if (dataSnapshot.hasChild("learning")) {
                        String learning = dataSnapshot.child("learning").getValue().toString();
                        learningET.setText(learning);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
