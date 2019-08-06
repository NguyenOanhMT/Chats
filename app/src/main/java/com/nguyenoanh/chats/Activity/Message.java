package com.nguyenoanh.chats.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenoanh.chats.Model.User;
import com.nguyenoanh.chats.R;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Message extends AppCompatActivity {

    CircleImageView profileImage;
    TextView username;

    ImageButton btnSend;
    EditText edtSend;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_message);

        Toolbar toolbar = (Toolbar) findViewById (R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                finish ();
            }
        });
        profileImage = (CircleImageView) findViewById (R.id.profileImage);
        username = (TextView) findViewById (R.id.tvUserName);
        btnSend = (ImageButton) findViewById (R.id.btn_send);
        edtSend = (EditText) findViewById (R.id.input_message);

        intent = getIntent ();
        final String userid = intent.getStringExtra ("userid");
        firebaseUser = FirebaseAuth.getInstance ().getCurrentUser ();

        btnSend.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String message = edtSend.getText ().toString ();

                if(!message.equals ("")){
                    sendMessage (firebaseUser.getUid (), userid, message);
                }else {
                    Toast.makeText (Message.this, "You can't send empty message", Toast.LENGTH_SHORT).show ();
                }
                edtSend.setText ("");
            }
        });

        reference = FirebaseDatabase.getInstance ().getReference ("Users").child (userid);

        reference.addValueEventListener (new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue (User.class);

                username.setText (user.getUserName ());

//                if(user.getInmageURL ().equals ("default")){
                    profileImage.setImageResource (R.drawable.anh1);
//                }else {
//                    Glide.with (Message.this).load(user.getInmageURL ())
//                            .into(profileImage);
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance ().getReference ();

        HashMap<String, Object> hashMap = new HashMap<> ();
        hashMap.put ("sender", sender);
        hashMap.put ("receiver", receiver);
        hashMap.put ("message", message);

        reference.child ("Chats").push().setValue (hashMap);
    }
}
