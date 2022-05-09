package com.nhatsangthi.chatsapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nhatsangthi.chatsapp.Adapters.FriendsAdapter;
import com.nhatsangthi.chatsapp.Enums.FriendState;
import com.nhatsangthi.chatsapp.Models.User;
import com.nhatsangthi.chatsapp.R;
import com.nhatsangthi.chatsapp.databinding.ActivityFriendBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FriendActivity extends AppCompatActivity {

    private ActivityFriendBinding binding;
    private FriendsAdapter friendsAdapter;
    private ArrayList<User> users;
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityFriendBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Find People");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Get list users from intent
        Intent intent=getIntent();
        users = (ArrayList<User>) intent.getSerializableExtra("UserList");
        User currentUser= (User) intent.getSerializableExtra("CurrentUser");
        //Initial adapter and set adapter
        friendsAdapter = new FriendsAdapter(this,users,currentUser);
        binding.recyclerViewFriends.setAdapter(friendsAdapter);

        //Add bottom decider
        RecyclerView.ItemDecoration itemDecoration=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        binding.recyclerViewFriends.addItemDecoration(itemDecoration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);

        SearchManager searchManager=(SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView= (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                  @Override
                  public boolean onQueryTextSubmit(String query) {
                      friendsAdapter.getFilter().filter(query);
                      return false;
                  }

                  @Override
                  public boolean onQueryTextChange(String newText) {
                      friendsAdapter.getFilter().filter(newText);
                      return false;
                  }
              }
        );
        return true;

    }

    @Override
    public void onBackPressed() {
        if(!searchView.isIconified())
        {
            searchView.setIconified(true);
            return;
        }
        if(searchView.isIconified()) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}