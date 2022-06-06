package com.nhatsangthi.chatsapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nhatsangthi.chatsapp.Activities.ChatActivity;
import com.nhatsangthi.chatsapp.R;
import com.nhatsangthi.chatsapp.Models.User;
import com.nhatsangthi.chatsapp.Utils.Util;
import com.nhatsangthi.chatsapp.databinding.ChatItemLayoutBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {

    Context context;
    ArrayList<User> originListUser;

    public ChatListAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.originListUser = users;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_item_layout, parent, false);

        return new ChatListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        User user = originListUser.get(position);
        String senderId = FirebaseAuth.getInstance().getUid();

        FirebaseDatabase.getInstance().getReference()
                .child("chatLists")
                .child(senderId)
                .child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            String lastMsg = snapshot.child("lastMsg").getValue(String.class);
                            long time = snapshot.child("lastMsgTime").getValue(Long.class);
                            holder.binding.lastMsg.setText(Util.mySubString(lastMsg, 0, 25));
                            holder.binding.msgTime.setText(Util.getTimeAgo(time));
                        } else {
                            holder.binding.lastMsg.setText("Tap to chat");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });

        holder.binding.username.setText(user.getName());

        Glide.with(context).load(user.getProfileImage())
                .placeholder(R.drawable.avatar)
                .into(holder.binding.profile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("name", user.getName());
                intent.putExtra("image", user.getProfileImage());
                intent.putExtra("uid", user.getUid());
                intent.putExtra("token", user.getToken());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return originListUser.size();
    }

    public class ChatListViewHolder extends RecyclerView.ViewHolder {

    ChatItemLayoutBinding binding;
        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ChatItemLayoutBinding.bind(itemView);
        }
    }
}