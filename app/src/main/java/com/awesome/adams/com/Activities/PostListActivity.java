package com.awesome.adams.com.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.awesome.adams.com.Data.BlogRecyclerAdapter;
import com.awesome.adams.com.Model.Blog;
import com.awesome.adams.blog.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class PostListActivity extends AppCompatActivity {

    private FirebaseDatabase mdatabase;
    private DatabaseReference mdatabasereference;
    private RecyclerView mrecyclerView;
    private BlogRecyclerAdapter mblogrecycleradapter;
    private List<Blog> bloglist;
    private FirebaseUser muser;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        mauth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance();
        mdatabasereference = mdatabase.getReference("MBlog");
        muser = mauth.getCurrentUser();
        mdatabasereference.keepSynced(true);

        bloglist = new ArrayList<>();

        mrecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mrecyclerView.setHasFixedSize(true);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

            switch (item.getItemId()) {

                case R.id.action_add:
                    if ( muser != null && mauth != null ) {
                        startActivity(new Intent(PostListActivity.this, AddPostActivity.class));
                        finish();
                    }

                    break;

                case R.id.action_signout:
                    if ( muser != null && mauth != null ) {
                        mauth.signOut();

                        startActivity(new Intent(PostListActivity.this, MainActivity.class));
                    }
                    }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mdatabasereference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Blog blog = dataSnapshot.getValue(Blog.class);
                bloglist.add(blog);
                mblogrecycleradapter = new BlogRecyclerAdapter(PostListActivity.this,bloglist);
                mrecyclerView.setAdapter(mblogrecycleradapter);
                mblogrecycleradapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
