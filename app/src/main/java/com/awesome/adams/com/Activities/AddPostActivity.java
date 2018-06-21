package com.awesome.adams.com.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.awesome.adams.blog.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {

    private ImageButton mimageview;
    private EditText mposttitle,mpostdesc;
    private Button msubmitbutton;
    private DatabaseReference mdatabasereference;
    private FirebaseDatabase mdatabase;
    private FirebaseUser muser;
    private FirebaseAuth mauth;
    private Uri imageuri;
    private ProgressDialog mprogress;
    private StorageReference mstoragereference;
    private static final int GALLERY_CODE =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        mprogress = new ProgressDialog(this);

        mauth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance();
        muser = mauth.getCurrentUser();
        mdatabasereference = mdatabase.getReference().child("MBlog");

        mimageview = (ImageButton) findViewById(R.id.imageButton);
        mposttitle = (EditText) findViewById(R.id.addtitle);
        mpostdesc = (EditText) findViewById(R.id.adddesc);
        msubmitbutton = (Button) findViewById(R.id.submitbutton);
        mstoragereference = FirebaseStorage.getInstance().getReference();

        mimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent,GALLERY_CODE);

            }
        });

        msubmitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //posting to our  database
                starttopost();

            }
        });
    }

    private void starttopost() {

        mprogress.setMessage("Posting...");

        final String title = mposttitle.getText().toString().trim();
        final String desc = mpostdesc.getText().toString().trim();

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc) && imageuri != null) {
            //start the uploading...
            mprogress.show();

            StorageReference filepath = mstoragereference.child("MBlog_images").child(imageuri.getLastPathSegment());
            filepath.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadurl = taskSnapshot.getDownloadUrl();

                    DatabaseReference newpost = mdatabasereference.push();

                    Map<String,String> datatosave = new HashMap<>();
                    datatosave.put("desc",desc);
                    datatosave.put("title",title);
                    datatosave.put("image",downloadurl.toString());
                    datatosave.put("timestamp",String.valueOf(java.lang.System.currentTimeMillis()));
                    datatosave.put("userid",muser.getUid());

                    newpost.setValue(datatosave);

                    mprogress.dismiss();

                    startActivity(new Intent(AddPostActivity.this,PostListActivity.class));
                    finish();
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            imageuri = data.getData();
            mimageview.setImageURI(imageuri);


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddPostActivity.this,PostListActivity.class));
    }
}
