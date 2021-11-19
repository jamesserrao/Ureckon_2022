package com.sreesubh.ureckon2022;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainDashedBoard extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    CircleImageView image_id;
    TextView name_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashed_board);
        auth=FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        image_id = findViewById(R.id.image_id);

        name_id = findViewById(R.id.name_id);
        name_id.setText(user.getDisplayName());
        Glide.with(this)
                .load(user.getPhotoUrl())
        .into(image_id);

    }
}