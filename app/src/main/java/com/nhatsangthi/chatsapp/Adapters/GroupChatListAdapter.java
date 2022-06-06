package com.nhatsangthi.chatsapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nhatsangthi.chatsapp.Activities.ChatActivity;
import com.nhatsangthi.chatsapp.Activities.GroupChatActivity;
import com.nhatsangthi.chatsapp.Models.Group;
import com.nhatsangthi.chatsapp.R;
import com.nhatsangthi.chatsapp.Utils.Util;
import com.nhatsangthi.chatsapp.databinding.ChatItemLayoutBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GroupChatListAdapter extends RecyclerView.Adapter<GroupChatListAdapter.GroupChatListViewHolder> {

    Context context;
    ArrayList<Group> groupList;

    public GroupChatListAdapter(Context context, ArrayList<Group> groupList) {
        this.context = context;
        this.groupList = groupList;
    }

    @NonNull
    @Override
    public GroupChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_item_layout, parent, false);

        return new GroupChatListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupChatListAdapter.GroupChatListViewHolder holder, int position) {
        Group group = groupList.get(position);

        String lastMsg = group.getGroupLastMessage().getLastMsg();
        long time = group.getGroupLastMessage().getLastMsgTime();

        holder.binding.lastMsg.setText(Util.mySubString(lastMsg, 0, 25));
        holder.binding.msgTime.setText(Util.getTimeAgo(time));

        holder.binding.username.setText(group.getName());
        Glide.with(context).load(group.getImage())
                .placeholder(R.drawable.group_avatar)
                .into(holder.binding.profile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GroupChatActivity.class);
                intent.putExtra("group", group);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public class GroupChatListViewHolder extends RecyclerView.ViewHolder {

        ChatItemLayoutBinding binding;
        public GroupChatListViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ChatItemLayoutBinding.bind(itemView);
        }
    }
}