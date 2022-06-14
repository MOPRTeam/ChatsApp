package com.nhatsangthi.chatsapp.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nhatsangthi.chatsapp.Activities.CreateGroupActivity;
import com.nhatsangthi.chatsapp.Adapters.GroupChatListAdapter;
import com.nhatsangthi.chatsapp.Models.Group;
import com.nhatsangthi.chatsapp.Models.GroupLastMessage;
import com.nhatsangthi.chatsapp.Models.GroupMember;
import com.nhatsangthi.chatsapp.Models.User;
import com.nhatsangthi.chatsapp.R;
import com.nhatsangthi.chatsapp.Utils.Util;
import com.nhatsangthi.chatsapp.databinding.FragmentGroupBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class GroupFragment extends Fragment {

    private FragmentGroupBinding binding;
    private Util util;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private ArrayList<Group> groupList;
    private GroupChatListAdapter groupChatListAdapter;
    private User currentUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = FragmentGroupBinding.inflate(inflater, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Groups");
        util = new Util();

        groupList = new ArrayList<>();
        groupChatListAdapter = new GroupChatListAdapter(getActivity(), groupList);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        currentUser = new User();
        database.getReference().child("users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        currentUser = snapshot.getValue(User.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        binding.groupChatRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.groupChatRecyclerView.setHasFixedSize(false);

        binding.groupChatRecyclerView.setAdapter(groupChatListAdapter);
        binding.groupChatRecyclerView.showShimmerAdapter();

        getGroupList();

        return binding.getRoot();
    }

    private void getGroupList() {

        database.getReference().child("groupDetails")
                .addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    groupList.clear();

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (ds.child("members").child(firebaseAuth.getUid()).exists()) {
                            HashMap<String, Object> hashMap = (HashMap<String, Object>) ds.getValue();

                            Group groupModel = new Group();
                            groupModel.setAdminName((String) hashMap.get("adminName"));
                            groupModel.setCreatedAt((String) hashMap.get("createdAt"));
                            groupModel.setImage((String) hashMap.get("image"));
                            groupModel.setAdminId((String) hashMap.get("adminId"));
                            groupModel.setName((String) hashMap.get("name"));
                            groupModel.setId((String) hashMap.get("id"));
                            groupModel.setIsAdmin((boolean) hashMap.get("isAdmin"));

                            GroupLastMessage lastMessageModel = ds.child("groupLastMessage").getValue(GroupLastMessage.class);
                            groupModel.setGroupLastMessage(lastMessageModel);

                            DataSnapshot dataSnapshot = ds.child("members");
                            List<GroupMember> memberModelList = new ArrayList<>();
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                GroupMember memberModel = data.getValue(GroupMember.class);
                                memberModelList.add(memberModel);
                            }

                            groupModel.setIsAdmin(ds.child("members").child(firebaseAuth.getUid()).child("role")
                                    .getValue().toString().equals("admin"));

                            groupModel.setMembers(memberModelList);
                            groupList.add(groupModel);
                        }
                    }

                    Collections.sort(groupList, (a, b) ->
                            b.getGroupLastMessage().getLastMsgTime().compareTo(a.getGroupLastMessage().getLastMsgTime()));

                    groupChatListAdapter.notifyDataSetChanged();
                    binding.groupChatRecyclerView.hideShimmerAdapter();
                    binding.groupChatRecyclerView.scheduleLayoutAnimation();
                }

                groupChatListAdapter.notifyDataSetChanged();
                binding.groupChatRecyclerView.hideShimmerAdapter();
                binding.groupChatRecyclerView.scheduleLayoutAnimation();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }

    @Override
    public void onPause() {
//        util.updateOnlineStatus(String.valueOf(System.currentTimeMillis()));
        super.onPause();
    }

    @Override
    public void onResume() {
//        util.updateOnlineStatus("online");
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.group_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btnCreateGroup) {
            Intent intent = new Intent(requireContext(), CreateGroupActivity.class);
            intent.putExtra("uid", currentUser.getUid());
            intent.putExtra("name", currentUser.getName());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}