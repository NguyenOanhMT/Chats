package com.nguyenoanh.chats.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.nguyenoanh.chats.Adapter.UserAdapter;
import com.nguyenoanh.chats.Model.Chat;
import com.nguyenoanh.chats.Model.User;
import com.nguyenoanh.chats.R;

import java.util.ArrayList;

public class ChatFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private ArrayList<User> list;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    private ArrayList<String> userList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate (R.layout.fragment_chat, container, false);

        recyclerView = view.findViewById (R.id.recycler_view_chat);
        recyclerView.setHasFixedSize (true);
        LinearLayoutManager layoutManager = new LinearLayoutManager (getContext ());
        recyclerView.setLayoutManager (layoutManager);

        firebaseUser = FirebaseAuth.getInstance ().getCurrentUser ();

        userList = new ArrayList<> ();

        reference = FirebaseDatabase.getInstance ().getReference ("Chats");

        reference.addValueEventListener (new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear ();

                for (DataSnapshot snapshot: dataSnapshot.getChildren ()){
                    Chat chat = snapshot.getValue (Chat.class);

                    if(chat.getSender ().equals (firebaseUser.getUid ())){
                        userList.add (chat.getReceiver ());
                    }
                    if (chat.getReceiver ().equals (firebaseUser.getUid ())){
                        userList.add (chat.getSender ());
                    }
                }
                readChat();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }


    private void readChat(){
        list = new ArrayList<> ();

        reference = FirebaseDatabase.getInstance ().getReference ("Users");
        reference.addValueEventListener (new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear ();

                // display 1 user from chats
                for(DataSnapshot snapshot : dataSnapshot.getChildren () ){
                    User user = snapshot.getValue (User.class);

                    for (String id: userList){
                        if(user.getId ().equals (id)){
                            if(list.size () != 0){
                                for (User user1: list){
                                    if(!user.getId ().equals (user1.getId ())){
                                        list.add (user);
                                    }
                                }
                            }else {
                                list.add (user);
                        }
                        }
                    }
                }
                userAdapter = new UserAdapter (getContext (), list);
                recyclerView.setAdapter (userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
