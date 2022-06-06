package com.nhatsangthi.chatsapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nhatsangthi.chatsapp.Models.Group;
import com.nhatsangthi.chatsapp.Models.User;
import com.nhatsangthi.chatsapp.R;
import com.nhatsangthi.chatsapp.databinding.ChatItemLayoutBinding;
import com.nhatsangthi.chatsapp.databinding.MemberItemLayoutBinding;

import java.util.ArrayList;

public class GroupMemberAdapter extends RecyclerView.Adapter<GroupMemberAdapter.ViewHolder> {

    Context context;
    ArrayList<User> userList;
    Group currentGroup;

    public GroupMemberAdapter(Context context, Group currentGroup, ArrayList<User> userList) {
        this.context = context;
        this.currentGroup = currentGroup;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.member_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);

        holder.binding.txtMemberName.setText(user.getName());
        if (!currentGroup.getAdminId().equals(user.getUid())) {
            holder.binding.status.setVisibility(View.GONE);
            if (currentGroup.getAdminId().equals(FirebaseAuth.getInstance().getUid())) {
                holder.binding.btnRemove.setVisibility(View.VISIBLE);
            }
        }


        holder.binding.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("groupDetails")
                        .child(currentGroup.getId()).child("members").child(user.getUid())
                        .removeValue();
            }
        });

        Glide.with(context).load(user.getProfileImage())
                .placeholder(R.drawable.avatar)
                .into(holder.binding.imgMember);
    }

    @Override
    public int getItemCount() {
        if (userList != null)
            return userList.size();
        else
            return 0;
    }

    public void setUserList(@NonNull ArrayList<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        MemberItemLayoutBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = MemberItemLayoutBinding.bind(itemView);
        }
    }
}