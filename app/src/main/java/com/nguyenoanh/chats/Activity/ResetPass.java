package com.nguyenoanh.chats.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nguyenoanh.chats.R;

public class ResetPass extends AppCompatActivity {
    EditText edtEmail;
    Button btnReset;

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_reset_pass);

        Toolbar toolbar;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Reset Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtEmail = (EditText) findViewById (R.id.edt_email);
        btnReset = (Button) findViewById (R.id.btn_reset);

        firebaseAuth = FirebaseAuth.getInstance();

        btnReset.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                final String email = edtEmail.getText ().toString ();

                if(email.equals ("") && email.equals (null)){
                    Toast.makeText (ResetPass.this, "Please fill your email", Toast.LENGTH_SHORT).show ();
                }else {
                    firebaseAuth.sendPasswordResetEmail (email)
                            .addOnCompleteListener (new OnCompleteListener<Void> () {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful ()){
                                Toast.makeText (ResetPass.this, "Please check your email", Toast.LENGTH_SHORT).show ();
                                Intent intent = new Intent (ResetPass.this, Login.class);
                                startActivity (intent);
                            }else {
                                String error = task.getException ().getMessage ();
                                Toast.makeText (ResetPass.this, error,Toast.LENGTH_SHORT).show ();
                            }
                        }
                    });
                }
            }
        });

    }
}
