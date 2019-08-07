package com.nguyenoanh.chats.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.nguyenoanh.chats.Adapter.MessageAdapter;
import com.nguyenoanh.chats.Model.Chat;
import com.nguyenoanh.chats.Model.User;
import com.nguyenoanh.chats.R;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Message extends AppCompatActivity {

    CircleImageView profileImage;
    TextView username;

    ImageButton btnSend;
    EditText edtSend;

    MessageAdapter messageAdapter;
    ArrayList<Chat> listChat;
    RecyclerView recyclerView;

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

        recyclerView = findViewById (R.id.recycler_view_message);
        recyclerView.setHasFixedSize (true);
        LinearLayoutManager layoutManager = new LinearLayoutManager (getApplicationContext ());
        layoutManager.setStackFromEnd (true);
        recyclerView.setLayoutManager (layoutManager);

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

        // handling event on click button Send
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
                    profileImage.setImageResource (R.drawable.anh);
//                }else {
//                    Glide.with (Message.this).load(user.getInmageURL ())
//                            .into(profileImage);
//                }

                readMessage (firebaseUser.getUid (), userid, user.getInmageURL ());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //send data message on database
    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance ().getReference ();

        HashMap<String, Object> hashMap = new HashMap<> ();
        hashMap.put ("sender", sender);
        hashMap.put ("receiver", receiver);
        hashMap.put ("message", message);

        reference.child ("Chats").push().setValue (hashMap);
    }

    //read message on real time database
    private void readMessage(final String myid, final String userid, final String imagurl){
        listChat = new ArrayList<> ();
        reference = FirebaseDatabase.getInstance ().getReference ("Chats");
        reference.addValueEventListener (new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listChat.clear ();

                for(DataSnapshot snapshot : dataSnapshot.getChildren () ){
                    Chat chat = snapshot.getValue (Chat.class);

                    if(chat.getReceiver ().equals (myid) && chat.getSender ().equals (userid) ||
                            chat.getReceiver ().equals (userid) && chat.getSender ().equals (myid)){
                        listChat.add (chat);
                    }

                    messageAdapter = new MessageAdapter (Message.this, listChat, imagurl);
                    recyclerView.setAdapter (messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
