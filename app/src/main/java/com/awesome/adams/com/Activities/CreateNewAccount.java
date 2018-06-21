package com.awesome.adams.com.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.awesome.adams.blog.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateNewAccount extends AppCompatActivity {

    private EditText firstname,lastname,email,password;
    private Button createbutton;

    private FirebaseDatabase database;
    private DatabaseReference dreference;
    private FirebaseUser user;
    private FirebaseAuth auth;

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_account);

        database = FirebaseDatabase.getInstance();
        dreference = database.getReference().child("MyUsers");

        auth = FirebaseAuth.getInstance();

        progress = new ProgressDialog(this);
        progress.setMessage("Getting Ready...");

        firstname = (EditText) findViewById(R.id.first_name);
        lastname = (EditText) findViewById(R.id.last_name);
        email = (EditText) findViewById(R.id.emailcreate);
        password = (EditText) findViewById(R.id.passwords);
        createbutton= (Button) findViewById(R.id.create);

        createbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createnewaccount();
            }
        });
    }

    private void createnewaccount() {

        final String ftname = firstname.getText().toString().trim();
        final String ltname = lastname.getText().toString().trim();
        String em = email.getText().toString().trim();
        String pd = password.getText().toString().trim();

        if (!TextUtils.isEmpty(ftname))
            if (!TextUtils.isEmpty(ltname))
                if (!TextUtils.isEmpty(em))
                    if (!TextUtils.isEmpty(pd)) {

                        auth.createUserWithEmailAndPassword(em,pd)
                                .addOnCompleteListener(CreateNewAccount.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                Log.d("messa","reached");

                                if (task.isSuccessful()) {
                                    progress.show();

                                    user = auth.getCurrentUser();

                                    String userID = user.getUid();

                                    DatabaseReference userreference = dreference.child(userID);
                                    userreference.child("first name").setValue(ftname);
                                    userreference.child("last name").setValue(ltname);
                                    userreference.child("image").setValue("none");

                                    progress.dismiss();

                                    Intent intent = new Intent(CreateNewAccount.this, PostListActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            }
                        });
                    }
    }
}
