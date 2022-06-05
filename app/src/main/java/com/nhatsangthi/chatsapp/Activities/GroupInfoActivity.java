package com.nhatsangthi.chatsapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nhatsangthi.chatsapp.Adapters.GroupMemberAdapter;
import com.nhatsangthi.chatsapp.Models.Group;
import com.nhatsangthi.chatsapp.Models.GroupMember;
import com.nhatsangthi.chatsapp.Models.User;
import com.nhatsangthi.chatsapp.R;
import com.nhatsangthi.chatsapp.databinding.ActivityGroupInfoBinding;
import com.nhatsangthi.chatsapp.databinding.AdminDialogLayoutBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupInfoActivity extends AppCompatActivity {

    ActivityGroupInfoBinding binding;
    Group currentGroup;
    ArrayList<User> userList;
    Uri imageUri;
    GroupMemberAdapter memberAdapter;

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.infoToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentGroup = new Group();
        currentGroup.setMembers(new ArrayList<>());
        database = FirebaseDatabase.getInstance();

        if (getIntent().hasExtra("group")) {
            currentGroup = getIntent().getParcelableExtra("group");
        }

        memberAdapter = new GroupMemberAdapter(this, currentGroup, userList);

        binding.memberRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.memberRecyclerView.setNestedScrollingEnabled(false);
        binding.memberRecyclerView.setAdapter(memberAdapter);

        if (!currentGroup.getImage().equals("No Image")) {
            Glide.with(this).load(currentGroup.getImage()).into(binding.expandedImage);
        }
        binding.collapsingToolbar.setTitle(currentGroup.getName());

        userList = new ArrayList<>();

        database.getReference("groupDetails").child(currentGroup.getId()).child("members")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<GroupMember> groupMembers = new ArrayList<>();
                        for (DataSnapshot snapshotTemp : snapshot.getChildren()) {
                            GroupMember memberModel = snapshotTemp.getValue(GroupMember.class);
                            groupMembers.add(memberModel);
                        }

                        currentGroup.setMembers(groupMembers);

                        for (GroupMember member : currentGroup.getMembers()) {
                            database.getReference().child("users").child(member.getId())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            User user = snapshot.getValue(User.class);

                                            userList.add(user);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }

                        memberAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });





        if (currentGroup.getAdminId().equals(FirebaseAuth.getInstance().getUid())) {
            binding.cardDeleteGroup.setVisibility(View.VISIBLE);
        } else {
            binding.cardDeleteGroup.setVisibility(View.GONE);
        }
    }
}