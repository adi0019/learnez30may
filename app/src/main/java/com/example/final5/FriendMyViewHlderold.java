package com.example.final5;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FriendMyViewHlderold extends RecyclerView.ViewHolder {
    ImageView profile;
    TextView name,bio,userid;
    Button videocallbtn;
    @SuppressLint("WrongViewCast")
    public  FriendMyViewHlderold(View itemView){
        super(itemView);
        profile=itemView.findViewById(R.id.profile);
        name=itemView.findViewById(R.id.name_notification1);
     //   bio=itemView.findViewById(R.id.user_bio1);
        videocallbtn=itemView.findViewById(R.id.accept_btn);
      //  userid =itemView.findViewById(R.id.userid);



    }


}
