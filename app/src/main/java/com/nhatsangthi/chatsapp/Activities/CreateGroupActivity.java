package com.nhatsangthi.chatsapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nhatsangthi.chatsapp.Constants.AllConstants;
import com.nhatsangthi.chatsapp.Models.User;
import com.nhatsangthi.chatsapp.databinding.ActivityChatBinding;
import com.nhatsangthi.chatsapp.databinding.ActivityCreateGroupBinding;

public class CreateGroupActivity extends AppCompatActivity {

    ActivityCreateGroupBinding binding;
    ProgressDialog dialog;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Create Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Creating group...");
        dialog.setCancelable(false);

        database = FirebaseDatabase.getInstance();

        binding.imgGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, AllConstants.REQUEST_GET_CONTENT);
            }
        });

        binding.btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGroup();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    void createGroup() {
        String groupName = binding.edtGroupName.getText().toString();

        if (groupName.isEmpty()) {
            binding.edtGroupName.setError("Please type a name");
            return;
        }

        dialog.show();
        DatabaseReference ref = database.getReference().child("groupDetails");
        String groupId = ref.push().getKey();
        if (selectedImage != null) {
            StorageReference reference = storage.getReference().child("groupDetails").child(groupId);
            reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();

                                String uid = auth.getUid();
                                String phone = auth.getCurrentUser().getPhoneNumber();
                                String name = binding.nameBox.getText().toString();

                                User user = new User(uid, name, phone, imageUrl);

                                database.getReference()
                                        .child("users")
                                        .child(uid)
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                dialog.dismiss();
                                                Intent intent = new Intent(SetupProfileActivity.this, DashBoard.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                            }
                        });
                    }
                }
            });
        } else {
            String uid = auth.getUid();
            String phone = auth.getCurrentUser().getPhoneNumber();

            User user = new User(uid, name, phone, "No Image");

            database.getReference()
                    .child("users")
                    .child(uid)
                    .setValue(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            dialog.dismiss();
                            Intent intent = new Intent(SetupProfileActivity.this, DashBoard.class);
                            startActivity(intent);
                            finish();
                        }
                    });
        }

        DatabaseReference ref = database.getReference().child("groupDetails");
        String groupId = ref.push().getKey();
        storage.getReference().child("groupProfiles").child(groupId)
                .
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case AllConstants.REQUEST_GET_CONTENT:
                    if (data != null) {
                        if (data.getData() != null) {
                            binding.imgGroup.setImageURI(data.getData());
                            selectedImage = data.getData();
                        }
                    }
                    break;
            }
        }
    }
}