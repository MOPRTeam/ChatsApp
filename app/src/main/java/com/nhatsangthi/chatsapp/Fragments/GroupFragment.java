package com.nhatsangthi.chatsapp.Fragments;

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
import com.nhatsangthi.chatsapp.Activities.CreateGroupActivity;
import com.nhatsangthi.chatsapp.Adapters.GroupChatListAdapter;
import com.nhatsangthi.chatsapp.Models.Group;
import com.nhatsangthi.chatsapp.R;
import com.nhatsangthi.chatsapp.Utils.Util;
import com.nhatsangthi.chatsapp.databinding.FragmentGroupBinding;

import java.util.ArrayList;

public class GroupFragment extends Fragment {

    private FragmentGroupBinding binding;
    private Util util;
    private FirebaseAuth firebaseAuth;
    private GroupChatListAdapter groupChatListAdapter;
    private ArrayList<Group> groups;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = FragmentGroupBinding.inflate(inflater, container, false);
        util = new Util();
        firebaseAuth = FirebaseAuth.getInstance();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Groups");

        groups = new ArrayList<>();
//        groupChatAdapter = new GroupChatAdapter();

        binding.groupChatRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.groupChatRecyclerView.setHasFixedSize(false);

//        binding.groupChatRecyclerView.setAdapter(groupAdapter);
//        groupAdapter.setGroup(groups);
//
//        getGroupList();

        return binding.getRoot();
    }

    private void getGroupList() {

//        Query query = FirebaseDatabase.getInstance().getReference("Group Detail");
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//
//                    groups.clear();
//
//                    for (DataSnapshot ds : snapshot.getChildren()) {
//                        if (ds.child("Members").child(firebaseAuth.getUid()).exists()) {
//                            GroupModel groupModel = ds.getValue(GroupModel.class);
//                            DataSnapshot dataSnapshot = ds.child("Members");
//                            List<GroupMemberModel> memberModelList = new ArrayList<>();
//                            GroupLastMessageModel lastMessageModel = ds.child("lastMessageModel").getValue(GroupLastMessageModel.class);
//                            if (lastMessageModel != null) {
//                                lastMessageModel.date = Util.getTimeAgo(Long.parseLong(lastMessageModel.date));
//                                groupModel.lastMessageModel = lastMessageModel;
//                            }
//
//                            for (DataSnapshot data : dataSnapshot.getChildren()) {
//
//                                GroupMemberModel memberModel = data.getValue(GroupMemberModel.class);
//                                memberModelList.add(memberModel);
//                            }
//
//                            groupModel.isAdmin = ds.child("Members").child(firebaseAuth.getUid()).child("role")
//                                    .getValue().toString().equals("admin");
//
//                            groupModel.members = memberModelList;
//                            groups.add(groupModel);
//
//                        }
//                    }
//
//                    groupAdapter.setGroupModels(groups);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

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
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}