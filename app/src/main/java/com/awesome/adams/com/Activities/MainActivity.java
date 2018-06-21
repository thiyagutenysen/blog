package com.awesome.adams.com.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.awesome.adams.blog.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener mauthlistener;
    private FirebaseUser user;
    private Button loginbutton,createbutton;
    private EditText emailfield,passwordfield;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailfield = (EditText) findViewById(R.id.emailID);
        passwordfield = (EditText) findViewById(R.id.passwordID);
        loginbutton = (Button) findViewById(R.id.loginbuttonID);
        createbutton = (Button) findViewById(R.id.createbuttonID);

        mauth = FirebaseAuth.getInstance();

        mauthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                user = firebaseAuth.getCurrentUser();

                if (user != null)
                    Toast.makeText(MainActivity.this,"logged in",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this,"not logged in",Toast.LENGTH_SHORT).show();
            }
        };

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(TextUtils.isEmpty(emailfield.getText().toString())) && !(TextUtils.isEmpty(passwordfield.getText().toString()))) {

                    String email =  emailfield.getText().toString();
                    String password = passwordfield.getText().toString();

                    login(email,password);
                }
            }
        });

        createbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"signed out",Toast.LENGTH_SHORT);
                startActivity(new Intent(MainActivity.this,CreateNewAccount.class));
            }
        });
    }

    public void login(String email, String password) {
        mauth = FirebaseAuth.getInstance();

        mauth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("a","a");
                if (task.isSuccessful()) {
                    startActivity(new Intent(MainActivity.this, PostListActivity.class));
                    finish();
                    Toast.makeText(MainActivity.this, "signed in", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(MainActivity.this,"couldnt log in",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_signout) {
            mauth.signOut();

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
        mauth.addAuthStateListener(mauthlistener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mauthlistener != null)
            mauth.removeAuthStateListener(mauthlistener);
    }
}
