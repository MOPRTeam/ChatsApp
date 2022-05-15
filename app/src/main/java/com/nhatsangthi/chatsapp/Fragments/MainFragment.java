package com.nhatsangthi.chatsapp.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nhatsangthi.chatsapp.Activities.FriendActivity;
import com.nhatsangthi.chatsapp.Adapters.TopStatusAdapter;
import com.nhatsangthi.chatsapp.Adapters.UsersAdapter;
import com.nhatsangthi.chatsapp.DTOs.ChatDTO;
import com.nhatsangthi.chatsapp.DTOs.SenderUserDTO;
import com.nhatsangthi.chatsapp.Enums.FriendState;
import com.nhatsangthi.chatsapp.Models.Status;
import com.nhatsangthi.chatsapp.Models.User;
import com.nhatsangthi.chatsapp.Models.UserStatus;
import com.nhatsangthi.chatsapp.R;
import com.nhatsangthi.chatsapp.databinding.FragmentMainBinding;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MainFragment extends Fragment {

    FragmentMainBinding binding;
    FirebaseDatabase database;
    ArrayList<User> users;
    ArrayList<User> listFriends;
    UsersAdapter usersAdapter;
    TopStatusAdapter statusAdapter;
    ArrayList<UserStatus> userStatuses;
    ProgressDialog dialog;

    User currentUser = new User();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainBinding.bind(inflater.inflate(R.layout.fragment_main, container, false));

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("ChatsApp");
        setHasOptionsMenu(true);

        database = FirebaseDatabase.getInstance();

        FirebaseMessaging.getInstance()
                .getToken()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String token) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("token", token);
                        database.getReference()
                                .child("users")
                                .child(FirebaseAuth.getInstance().getUid())
                                .updateChildren(map);
                    }
                });

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Uploading Image...");
        dialog.setCancelable(false);

        users = new ArrayList<>();
        listFriends = new ArrayList<>();
        userStatuses = new ArrayList<>();

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

        usersAdapter = new UsersAdapter(getActivity(), listFriends);
        statusAdapter = new TopStatusAdapter(getActivity(), userStatuses);

//        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.statusList.setLayoutManager(layoutManager);
        binding.statusList.setAdapter(statusAdapter);

        binding.recyclerView.setAdapter(usersAdapter);

        binding.recyclerView.showShimmerAdapter();
        binding.statusList.showShimmerAdapter();

        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                    User user = snapshot1.getValue(User.class);
                    if(!Objects.isNull(user.getUid()))
                        if(!user.getUid().equals(FirebaseAuth.getInstance().getUid()))
                            users.add(user);
                }
                binding.recyclerView.hideShimmerAdapter();
                filterFriend();
                usersAdapter.notifyDataSetChanged();
                statusAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (data.getData() != null) {
                dialog.show();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                Date date = new Date();
                StorageReference reference =
                        storage.getReference().child("status").child(date.getTime() + "");

                reference.putFile(data.getData()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    UserStatus userStatus = new UserStatus();
                                    userStatus.setName(currentUser.getName());
                                    userStatus.setProfileImage(currentUser.getProfileImage());
                                    userStatus.setLastUpdated(date.getTime());

                                    HashMap<String, Object> obj = new HashMap<>();
                                    obj.put("name", userStatus.getName());
                                    obj.put("profileImage", userStatus.getProfileImage());
                                    obj.put("lastUpdated", userStatus.getLastUpdated());

                                    String imageUrl = uri.toString();
                                    Status status = new Status(imageUrl, userStatus.getLastUpdated());

                                    database.getReference()
                                            .child("stories")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .updateChildren(obj);

                                    database.getReference()
                                            .child("stories")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .child("statuses")
                                            .push()
                                            .setValue(status);

                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.topmenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addFriend:
                Intent intent = new Intent(getActivity(), FriendActivity.class);
                intent.putExtra("UserList", users);
                intent.putExtra("CurrentUser", currentUser);
                startActivity(intent);
                break;
            case R.id.group:
//                startActivity(new Intent(this, GroupChatActivity.class));
                break;
            case R.id.logout:
//                FirebaseAuth.getInstance().signOut();
//                finish();
//                startActivity(new Intent(this, PhoneNumberActivity.class));
                break;
            case R.id.settings:
//                database.getReference().child("users")
//                        .child(currentUser.getUid())
//                        .child("friendList").child("bty4EsaYCdOrUfrW7MtNdtnPze32").removeValue();
//                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void filterFriend()
    {
        database.getReference().child("friends").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (users.size() > 0) {
                    listFriends.clear();
                    ArrayList<String> listIdFriend = new ArrayList<>();
                    for (DataSnapshot snapshotTemp : snapshot.getChildren()) {
                        if (snapshotTemp.getValue().equals(FriendState.FRIEND.name())) {
                            listIdFriend.add(snapshotTemp.getKey());
                        }
                    }
                    for (User user : users) {
                        if (listIdFriend.contains(user.getUid()))
                            listFriends.add(user);
                    }
                    showStoriesOfFriend(listIdFriend);
                    sortUsers();
                } else {
                    Toast.makeText(getActivity(), "No Friend List", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showStoriesOfFriend(ArrayList<String> listIDFriend)
    {
        database.getReference().child("stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userStatuses.clear();

                    for (DataSnapshot storySnapshot : snapshot.getChildren()) {
                        if (listIDFriend.contains(storySnapshot.getKey())) {
                            UserStatus status = new UserStatus();
                            status.setName(storySnapshot.child("name").getValue(String.class));
                            status.setProfileImage(storySnapshot.child("profileImage").getValue(String.class));
                            status.setLastUpdated(storySnapshot.child("lastUpdated").getValue(Long.class));

                            ArrayList<Status> statuses = new ArrayList<>();

                            for (DataSnapshot statusSnapshot : storySnapshot.child("statuses").getChildren()) {
                                Status sampleStatus = statusSnapshot.getValue(Status.class);
                                statuses.add(sampleStatus);
                            }

                            status.setStatuses(statuses);
                            userStatuses.add(status);
                        }
                    }

                    binding.statusList.hideShimmerAdapter();
                    statusAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sortUsers()
    {
        database.getReference().child("chats").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (listFriends.size() > 0) {
                    List<SenderUserDTO> senderUsers = new ArrayList<>();
                    for (User user : listFriends) {
                        senderUsers.add(new SenderUserDTO(user,Long.MAX_VALUE));
                    }

                    for (DataSnapshot snapshotTemp : snapshot.getChildren()) {
                        if (snapshotTemp.getKey().startsWith(FirebaseAuth.getInstance().getUid())) {
                            ChatDTO temp = snapshotTemp.getValue(ChatDTO.class);
                            Optional<User> tempUser = listFriends.stream().filter(e -> snapshotTemp.getKey().endsWith(e.getUid())).findFirst();
                            if (tempUser.isPresent()) {
                                senderUsers.remove(senderUsers.stream().filter(e -> e.getUser().getUid().equals(tempUser.get().getUid())).findFirst().get());
                                senderUsers.add(new SenderUserDTO(tempUser.get(), temp.getLastMsgTime()));
                            }
                        }
                    }
                    senderUsers.sort(Comparator.comparingLong(SenderUserDTO::getLastMessageTime).reversed());
                    listFriends.clear();
                    for (SenderUserDTO user : senderUsers) {
                        listFriends.add(user.getUser());
                    }

                    binding.recyclerView.hideShimmerAdapter();
                    usersAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "No Friend List", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }



}