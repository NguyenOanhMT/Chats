package com.nguyenoanh.chats.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nguyenoanh.chats.Adapter.UserAdapter;
import com.nguyenoanh.chats.Model.User;
import com.nguyenoanh.chats.R;

import java.util.ArrayList;

public class UserFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<User> listUser;
    private UserAdapter adapter;

    EditText searchUser;

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

        searchUser = view.findViewById (R.id.edt_search);
        searchUser.addTextChangedListener (new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString().toLowerCase ());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void searchUsers(String str){
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("search")
                .startAt(str)
                .endAt(str+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listUser.clear ();
                for (DataSnapshot snapshot : dataSnapshot.getChildren ()) {
                    User user = snapshot.getValue (User.class);

                    assert user != null;
                    assert fuser != null;
                    if (!user.getId ().equals (fuser.getUid ())) {
                        listUser.add (user);
                    }
                }

                adapter = new UserAdapter (getContext (), listUser, false);
                recyclerView.setAdapter (adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

                adapter = new UserAdapter (getContext (), listUser, false);
                recyclerView.setAdapter (adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
