package com.nhatsangthi.chatsapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nhatsangthi.chatsapp.Models.Group;
import com.nhatsangthi.chatsapp.Models.GroupMember;
import com.nhatsangthi.chatsapp.Models.User;
import com.nhatsangthi.chatsapp.R;
import com.nhatsangthi.chatsapp.databinding.ItemAddGroupMemberBinding;
import com.nhatsangthi.chatsapp.databinding.ItemFriendBinding;

import java.util.ArrayList;

public class AddMemberAdapter extends RecyclerView.Adapter<AddMemberAdapter.MemberViewHolder> {

    Context context;
    ArrayList<User> userList;
    Group currentGroup;
    ArrayList<String> listMemberId;
    FirebaseDatabase database;

    public AddMemberAdapter(Context context, ArrayList<User> userList, Group currentGroup) {
        this.context = context;
        this.userList = userList;
        this.currentGroup = currentGroup;
        database = FirebaseDatabase.getInstance();

        listMemberId = new ArrayList<>();
        for (GroupMember member : currentGroup.getMembers()) {
            listMemberId.add(member.getId());
        }
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_add_group_member, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        User user = userList.get(position);

        if (listMemberId.contains(user.getUid())) {
            holder.binding.addRemove.setText("REMOVE");
            holder.binding.addRemove.setBackgroundColor(context.getResources().getColor(R.color.semiRed));
            holder.binding.addRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        database.getReference("groupDetails")
                                .child(currentGroup.getId()).child("members").child(user.getUid())
                                .removeValue();
                        Toast.makeText(context, "Removed " + user.getName() + " successfully!", Toast.LENGTH_LONG).show();
                    }
                }
            );
        } else {
            holder.binding.addRemove.setText("ADD");
            holder.binding.addRemove.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.binding.addRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GroupMember member = new GroupMember(user.getUid(), "member");
                        database.getReference("groupDetails").child(currentGroup.getId())
                                .child("members").child(user.getUid())
                                .setValue(member);
                        Toast.makeText(context, "Added " + user.getName() + " successfully!", Toast.LENGTH_LONG).show();
                    }
                }
            );
        }

        Glide.with(context)
                .load(user.getProfileImage())
                .placeholder(R.drawable.avatar)
                .into(holder.binding.image);

        holder.binding.username.setText(user.getName());
        holder.binding.phone.setText(user.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setArrayList(@NonNull ArrayList<User> userList) {
        this.userList = userList;
        listMemberId.clear();
        for (GroupMember member : currentGroup.getMembers()) {
            listMemberId.add(member.getId());
        }
        notifyDataSetChanged();
    }

    class MemberViewHolder extends RecyclerView.ViewHolder {

        ItemAddGroupMemberBinding binding;
        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemAddGroupMemberBinding.bind(itemView);
        }
    }
}
