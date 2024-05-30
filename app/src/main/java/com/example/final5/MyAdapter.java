package com.example.final5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Contacts> list;
    String visit_user_id;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    public MyAdapter(Context context, ArrayList<Contacts> list) {
        this.context = context;
        this.list = list;
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.find_friend_design, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Contacts contacts = list.get(position);

        if (contacts != null) {
            holder.firstName.setText(contacts.getName());
            holder.bio.setText(contacts.getBio());
         //   holder.live1.setText(contacts.getLive());
            holder.occupation.setText(contacts.getLearning());


            Log.d("MyAdapter", "occu: " + contacts.getOccupation());
        }


        if (!mUser.getUid().equals(contacts.getUid())) {
            Picasso.get().load(contacts.getImage()).into(holder.contactsimageview);
            holder.firstName.setText(contacts.getName());
            holder.bio.setText(contacts.getBio());
            holder.city.setText(contacts.getCity());
            holder.country.setText(contacts.getCountry());

            visit_user_id = contacts.getUid();
        } else {
            String name = contacts.getName();
            String bio = contacts.getBio();
            String image = contacts.getImage();
            String userId = contacts.getUid();
            String country = contacts.getCountry();
            String nativeLanguage = contacts.getNativeLanguage();
            String city = contacts.getCity();
            String learning = contacts.getLearning();
            String occupation = contacts.getOccupation();

            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra("image", image);
            intent.putExtra("Userid", visit_user_id);
            intent.putExtra("bio", bio);
            intent.putExtra("name", name);
            intent.putExtra("country", country);
            intent.putExtra("nativeLanguage", nativeLanguage);
            intent.putExtra("city", city);
            intent.putExtra("learning", learning);
            intent.putExtra("occupation", occupation);

            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String country = contacts.getCountry();
                String nativeLanguage = contacts.getNativeLanguage();
                String city = contacts.getCity();
                String bio = contacts.getBio();
                String learning = contacts.getLearning();
                String image = contacts.getImage();
                String occupation = contacts.getOccupation();

                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("visit_user_id", visit_user_id);
                intent.putExtra("profile_image", contacts.getImage());
                intent.putExtra("profile_name", contacts.getName());
                intent.putExtra("country", country);
                intent.putExtra("nativeLanguage", nativeLanguage);
                intent.putExtra("city", city);
                intent.putExtra("learning", learning);
                intent.putExtra("bio", bio);
                intent.putExtra("occupation", occupation);
                intent.putExtra("image", image);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView firstName, bio, country, city, live1, occupation;
        ImageView contactsimageview;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.name_notification11);
            contactsimageview = itemView.findViewById(R.id.image_notification1);
            bio = itemView.findViewById(R.id.bio_friend_list1);
            cardView = itemView.findViewById(R.id.cardView_findfriend);
            country = itemView.findViewById(R.id.country);
            city =itemView.findViewById(R.id.city);
            occupation =itemView.findViewById(R.id.occupation1);
       //     live1=itemView.findViewById(R.id.userlive);
        }
    }
}
