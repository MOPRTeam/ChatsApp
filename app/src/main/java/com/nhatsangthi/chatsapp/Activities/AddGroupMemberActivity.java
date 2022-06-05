package com.nhatsangthi.chatsapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nhatsangthi.chatsapp.Adapters.AddMemberAdapter;
import com.nhatsangthi.chatsapp.Adapters.FriendsAdapter;
import com.nhatsangthi.chatsapp.Models.Group;
import com.nhatsangthi.chatsapp.Models.GroupMember;
import com.nhatsangthi.chatsapp.Models.User;
import com.nhatsangthi.chatsapp.databinding.ActivityAddGroupMemberBinding;

import java.util.ArrayList;
import java.util.Objects;

public class AddGroupMemberActivity extends AppCompatActivity {
    ActivityAddGroupMemberBinding binding;
    Group currentGroup;
    ArrayList<User> userList;
    AddMemberAdapter adapter;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddGroupMemberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Add Members");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra("group")) {
            currentGroup = getIntent().getParcelableExtra("group");
        }

        database = FirebaseDatabase.getInstance();
        userList = new ArrayList<>();

        //Initial adapter and set adapter
        adapter = new AddMemberAdapter(this, userList, currentGroup);
        binding.recyclerViewFriends.setAdapter(adapter);
        //Add bottom decider
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        binding.recyclerViewFriends.addItemDecoration(itemDecoration);

        binding.recyclerViewFriends.showShimmerAdapter();

        database.getReference("users").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    User user = snapshot1.getValue(User.class);
                    if (!Objects.isNull(user.getUid()))
                        if (!user.getUid().equals(FirebaseAuth.getInstance().getUid()))
                            userList.add(user);
                }

                adapter.notifyDataSetChanged();
                binding.recyclerViewFriends.hideShimmerAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.getReference("groupDetails").child(currentGroup.getId()).child("members")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<GroupMember> groupMembers = new ArrayList<>();
                        for (DataSnapshot snapshotTemp : snapshot.getChildren()) {
                            groupMembers.add(snapshotTemp.getValue(GroupMember.class));
                        }
                        currentGroup.setMembers(groupMembers);

                        adapter.setArrayList(userList);
                        binding.recyclerViewFriends.hideShimmerAdapter();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}