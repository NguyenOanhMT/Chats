package com.nguyenoanh.chats.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nguyenoanh.chats.R;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    EditText username;
    EditText email;
    EditText password;
    EditText confirm;
    Button btnRegister;

    FirebaseAuth auth;
    DatabaseReference reference;

    private FirebaseAuth.AuthStateListener authLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar;
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username =(EditText) findViewById(R.id.edtName);
        email =(EditText) findViewById(R.id.edtEmail);
        password =(EditText) findViewById(R.id.edtPass);
        confirm =(EditText) findViewById(R.id.edtConfirmPass);
        btnRegister =(Button) findViewById(R.id.btnRegister);

        auth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edtUsername = username.getText().toString();
                String edtEmail = email.getText().toString();
                String edtPass = password.getText().toString();
                String edtConfirm = confirm.getText().toString();

                if ((TextUtils.isEmpty(edtUsername) || TextUtils.isEmpty(edtEmail)
                        || TextUtils.isEmpty(edtPass)) && edtConfirm.equals(edtPass)){
                    Toast.makeText(Register.this, "All fill are required", Toast.LENGTH_SHORT).show();
                }else if(edtPass.length() < 8)
                    Toast.makeText(Register.this, "Pass least 8 character ", Toast.LENGTH_SHORT).show();
                else
                    register(edtUsername,edtEmail,edtPass);
            }
        });
    }

    private void register(final String username, String email, String password){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();


                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("userName",username);
                            hashMap.put("imageURL", "default");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(Register.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(Register.this, "You can't register with this email or pass", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
        auth.addAuthStateListener(authLinear);
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onStop(){
        super.onStop();
        if(authLinear != null){
            auth.removeAuthStateListener(authLinear);
        }
    }
}
