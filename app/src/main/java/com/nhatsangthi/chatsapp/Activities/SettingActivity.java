package com.nhatsangthi.chatsapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.nhatsangthi.chatsapp.R;
import com.nhatsangthi.chatsapp.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}