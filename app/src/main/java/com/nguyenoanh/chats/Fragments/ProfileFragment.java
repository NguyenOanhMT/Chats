package com.nguyenoanh.chats.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenoanh.chats.Model.User;
import com.nguyenoanh.chats.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private CircleImageView imageProfile;
    private TextView username;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    ArrayList<User> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_profile, container, false);

        imageProfile = view.findViewById (R.id.profileImage);
        username = view.findViewById (R.id.tvUserName);

        firebaseUser = FirebaseAuth.getInstance ().getCurrentUser ();
        reference = FirebaseDatabase.getInstance ().getReference ("Users").child (firebaseUser.getUid ());

        reference.addValueEventListener (new ValueEventListener () {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue (User.class);
                username.setText (user.getUserName ());
//                if(user.getInmageURL ().equals ("default")){
                    imageProfile.setImageResource (R.drawable.anh);
//                }else {
//                    Glide.with (getContext ()).load (user.getInmageURL ()).into (imageProfile);
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}
