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
import com.nguyenoanh.chats.Model.User;
import com.nguyenoanh.chats.R;

import java.util.ArrayList;

public class UserFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<User> listUser;
    private UserAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view =  inflater.inflate (R.layout.fragment_user, container, false);

        recyclerView = view.findViewById (R.id.recycler_view_user);
        recyclerView.setHasFixedSize (true);
        LinearLayoutManager layoutManager = new LinearLayoutManager (getContext ());
        recyclerView.setLayoutManager (layoutManager);

        listUser = new ArrayList<> ();
        readUsers();

        return view;
    }

    private void readUsers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance ().getCurrentUser ();
        DatabaseReference reference = FirebaseDatabase.getInstance ().getReference ("Users");

        reference.addValueEventListener (new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listUser.clear ();

                for (DataSnapshot snapshot: dataSnapshot.getChildren ()){
                    User user = snapshot.getValue (User.class);

                    assert user != null;
                    assert firebaseUser != null;
                    if(!user.getId ().equals (firebaseUser.getUid ())){
                        listUser.add (user);
                    }
                }

                adapter = new UserAdapter (getContext (), listUser);
                recyclerView.setAdapter (adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
