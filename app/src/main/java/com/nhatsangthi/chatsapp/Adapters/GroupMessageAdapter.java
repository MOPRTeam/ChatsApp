package com.nhatsangthi.chatsapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.nhatsangthi.chatsapp.Models.GroupMessage;
import com.nhatsangthi.chatsapp.Models.Message;
import com.nhatsangthi.chatsapp.Models.User;
import com.nhatsangthi.chatsapp.R;
import com.nhatsangthi.chatsapp.databinding.ItemReceiveGroupBinding;
import com.nhatsangthi.chatsapp.databinding.ItemSentGroupBinding;

import java.util.ArrayList;

public class GroupMessageAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<GroupMessage> groupMessages;

    final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;

    public GroupMessageAdapter(Context context, ArrayList<GroupMessage> groupMessages) {
        this.context = context;
        this.groupMessages = groupMessages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_sent_group, parent, false);
            return new GroupMessageAdapter.SentViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_receive_group, parent, false);
            return new GroupMessageAdapter.ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        GroupMessage groupMessage = groupMessages.get(position);
        if (FirebaseAuth.getInstance().getUid().equals(groupMessage.getSenderId())) {
            return ITEM_SENT;
        } else {
            return ITEM_RECEIVE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GroupMessage groupMessage = groupMessages.get(position);

        if (holder.getClass() == GroupMessageAdapter.SentViewHolder.class) {
            GroupMessageAdapter.SentViewHolder viewHolder = (GroupMessageAdapter.SentViewHolder) holder;

            if (groupMessage.getType().equals("photo")) {
                viewHolder.binding.image.setVisibility(View.VISIBLE);
                viewHolder.binding.message.setVisibility(View.GONE);
                Glide.with(context)
                        .load(groupMessage.getMessage())
                        .placeholder(R.drawable.placeholder)
                        .into(viewHolder.binding.image);
            }

            viewHolder.binding.message.setText(groupMessage.getMessage());

            viewHolder.binding.feeling.setVisibility(View.GONE);
        } else {
            GroupMessageAdapter.ReceiverViewHolder viewHolder = (GroupMessageAdapter.ReceiverViewHolder) holder;

            if (groupMessage.getType().equals("photo")) {
                viewHolder.binding.image.setVisibility(View.VISIBLE);
                viewHolder.binding.message.setVisibility(View.GONE);
                Glide.with(context)
                        .load(groupMessage.getMessage())
                        .placeholder(R.drawable.placeholder)
                        .into(viewHolder.binding.image);
            }

            viewHolder.binding.name.setText("@" + groupMessage.getName());

            viewHolder.binding.message.setText(groupMessage.getMessage());

            viewHolder.binding.feeling.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return groupMessages.size();
    }

    public class SentViewHolder extends RecyclerView.ViewHolder {

        ItemSentGroupBinding binding;
        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemSentGroupBinding.bind(itemView);
        }
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {

        ItemReceiveGroupBinding binding;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemReceiveGroupBinding.bind(itemView);
        }
    }
}
