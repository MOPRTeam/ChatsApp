package com.nhatsangthi.chatsapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.nhatsangthi.chatsapp.Adapters.GroupMessageAdapter;
import com.nhatsangthi.chatsapp.Adapters.PublicMessagesAdapter;
import com.nhatsangthi.chatsapp.Models.Group;
import com.nhatsangthi.chatsapp.Models.GroupLastMessage;
import com.nhatsangthi.chatsapp.Models.GroupMessage;
import com.nhatsangthi.chatsapp.Models.Message;
import com.nhatsangthi.chatsapp.Models.User;
import com.nhatsangthi.chatsapp.R;
import com.nhatsangthi.chatsapp.databinding.ActivityGroupChatBinding;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {

    ActivityGroupChatBinding binding;

    Group currentGroup;
    User currentUser;
    GroupMessageAdapter adapter;
    ArrayList<GroupMessage> groupMessages;

    FirebaseDatabase database;
    FirebaseStorage storage;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        currentUser = new User();
        currentGroup = new Group();
        groupMessages = new ArrayList<>();

        database.getReference().child("users").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        currentUser = snapshot.getValue(User.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        if (getIntent().hasExtra("group")) {
            currentGroup = getIntent().getParcelableExtra("group");
            binding.name.setText(currentGroup.getName());
            Glide.with(GroupChatActivity.this)
                    .load(currentGroup.getImage())
                    .placeholder(R.drawable.group_avatar)
                    .into(binding.profile);
            readMessages();
        }

        binding.imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        adapter = new GroupMessageAdapter(this, groupMessages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(adapter);

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageTxt = binding.messageBox.getText().toString();

                if (messageTxt.trim().equals(""))
                    return;

                sendMessage(messageTxt);

                binding.messageBox.setText("");
            }
        });
    }

    private void readMessages() {
        database.getReference("groupMessages")
                .child(currentGroup.getId())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    groupMessages.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        GroupMessage groupMessage = ds.getValue(GroupMessage.class);

                        if (!groupMessage.getSenderId().equals(currentUser.getUid())) {
                            database.getReference("users")
                                    .child(groupMessage.getSenderId())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snap) {
                                    if (snap.exists()) {
                                        User user = snap.getValue(User.class);
                                        groupMessage.setName(user.getName());
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }


                        groupMessages.add(groupMessage);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void sendMessage(String message) {
        Long date = System.currentTimeMillis();
        GroupMessage groupMessage = new GroupMessage("text", message, currentUser.getUid(), currentUser.getName(), date);
        binding.messageBox.setText("");

        database.getReference().child("groupMessages").child(currentGroup.getId())
                .push()
                .setValue(groupMessage);

        GroupLastMessage groupLastMessage = new GroupLastMessage();
        groupLastMessage.setLastMsg(message);
        groupLastMessage.setLastMsgTime(date);
        groupLastMessage.setSenderUid(currentUser.getUid());

        database.getReference("groupDetails").child(currentGroup.getId())
                .child("groupLastMessage")
                .setValue(groupLastMessage);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.group_message_menu, menu);
        menu.findItem(R.id.btnAddMember).setVisible(currentGroup.getIsAdmin());
        menu.findItem(R.id.btnExitGroup).setVisible(!currentGroup.getIsAdmin());
        menu.findItem(R.id.btnDeleteGroup).setVisible(currentGroup.getIsAdmin());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.btnAddMember:
//                Intent member = new Intent(this, AddMemberActivity.class);
//                member.putExtra("group", currentGroup);
//                startActivityForResult(member, 101);

                break;
            case R.id.btnGroupInfo:
                Intent intent = new Intent(this, GroupInfoActivity.class);
                intent.putExtra("group", currentGroup);
                startActivityForResult(intent, 90);
                break;
            case R.id.btnExitGroup:
                exitGroup();
                break;
            case R.id.btnDeleteGroup:
                deleteGroup();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteGroup() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Group")
                .setMessage("Are you sure to delete the group?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.getReference("groupDetails")
                                .child(currentGroup.getId())
                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    database.getReference("groupMessages")
                                            .child(currentGroup.getId())
                                            .removeValue();
                                    Toast.makeText(GroupChatActivity.this, "Group Deleted", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                } else {
                                    Toast.makeText(GroupChatActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create().show();
    }

    private void exitGroup() {
        new AlertDialog.Builder(this)
                .setTitle("Leave Group")
                .setMessage("Are you sure to leave the group?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.getReference("groupDetails")
                                .child(currentGroup.getId())
                                .child("members").child(currentUser.getUid())
                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(GroupChatActivity.this, "Group Left", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                } else {
                                    Toast.makeText(GroupChatActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create().show();
    }
}