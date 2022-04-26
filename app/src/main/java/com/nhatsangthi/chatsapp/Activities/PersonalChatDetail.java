package com.nhatsangthi.chatsapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.nhatsangthi.chatsapp.R;
import com.nhatsangthi.chatsapp.databinding.ActivityChatBinding;
import com.nhatsangthi.chatsapp.databinding.ActivityPersonalChatDetailBinding;

public class PersonalChatDetail extends AppCompatActivity {

    ActivityPersonalChatDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPersonalChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Personal Info");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String name = getIntent().getStringExtra("name");
        String profile = getIntent().getStringExtra("image");

        binding.name.setText(name);
        Glide.with(PersonalChatDetail.this)
                .load(profile)
                .placeholder(R.drawable.avatar)
                .into(binding.imageView);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}